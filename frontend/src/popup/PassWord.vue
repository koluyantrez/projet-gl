<template>
  <div class="popup">
    <div class="inner">
      <center>
        <div class="allInput">
          <input type="password" :placeholder="cLang.pw.old" v-model="currentPassword" />
          <input type="password" :placeholder="cLang.pw.new" v-model="newPassword" />
          <input type="password" :placeholder="cLang.pw.check" v-model="confirmPassword" />
        </div>
        <button @click="changePassword">{{ cLang.pw.ok }}</button>
        <button @click="ToPassPopup">{{ cLang.pw.back }}</button>
      </center>
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../views/fr.js'
import en from '../views/en.js';
import axios from 'axios';
import Cookies from 'js-cookie';

export default {
  props: ['ToPassPopup'],

  data() {
    return {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
  },

  methods: {
    async changePassword() {
      let currentPasswordCookie;
      const roleAuth = Cookies.get('role');
      if (roleAuth === 'student') {
        currentPasswordCookie = Cookies.get('passwordStudent');
      } else if (roleAuth === 'professeur') {
        currentPasswordCookie = Cookies.get('passwordProfesseur');
      } else if (roleAuth === 'admin') {
        currentPasswordCookie = Cookies.get('passwordAdmin');
      } else if (roleAuth === 'inscription') {
        currentPasswordCookie = Cookies.get('passwordInscription');
      }

      console.log("currentPasswordCookies: " + currentPasswordCookie);
      console.log("currentPass :  " + this.currentPassword);

      if (this.currentPassword !== currentPasswordCookie) {
        alert("Mot de passe incorrect");
        return;
      }

      if (this.newPassword !== this.confirmPassword) {
        alert("Les mots de passe ne correspondent pas");
        return;
      }

      const id = Cookies.get('loginUser');
      if (!id) {
        alert("ID utilisateur non trouvé");
        return;
      }

      try {
        await axios.put(`http://localhost:1937/users/${id}/password`, null, {
          params: {
            newPassword: this.newPassword
          },
          data: {
            oldPassword: this.currentPassword
          },
          headers: {
            'Content-Type': 'application/json'
          }
        });
        // Mettre à jour le mot de passe stocké dans les cookies si nécessaire
        if (roleAuth === 'student') {
          Cookies.set('passwordStudent', this.newPassword);
        } else if (roleAuth === 'professeur') {
          Cookies.set('passwordProfesseur', this.newPassword);
        } else if (roleAuth === 'admin') {
          Cookies.set('passwordAdmin', this.newPassword);
        } else if (roleAuth === 'inscription') {
          Cookies.set('passwordInscription', this.newPassword);
        }
        window.location.reload();
        window.close();
      } catch (error) {
        if (error.response) {
          console.error('Error response:', error.response.data);
          alert(`Erreur : ${error.response.data.message}`);
        } else if (error.request) {
          console.error('Error request:', error.request);
        } else {
          console.error('Error message:', error.message);
        }
      }
    }
  },

  setup() {
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    return {
      cLang
    };
  }
}
</script>

<style scoped>
.popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.inner {
  background: #fff;
  padding: 39px;
  border-radius: 10px;
}

.allInput {
  margin-bottom: 1.5rem;
}

input {
  display: block;
  margin: 0.5rem 0;
  padding: 0.5rem;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
}

button {
  padding: 0.5rem 1rem;
  border: none;
  background-color: #007bff;
  color: #fff;
  border-radius: 5px;
  cursor: pointer;
  margin: 0.5rem;
}
</style>
