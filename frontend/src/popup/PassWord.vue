<template>
  <div class="popup">
    <div class="inner">
      <center>
        <input type="password" placeholder="Ancien mot de passe" v-model="currentPassword" />
        <input type="password" placeholder="Nouveau mot de passe" v-model="newPassword" />
        <input type="password" placeholder="Confirmer le nouveau mot de passe" v-model="confirmPassword" />

        <!-- <ItemInput type="password" :name="cLang.pw.old" v-model:val="currentPassword"/>
        <ItemInput type="password" :name="cLang.pw.new" v-model:val="newPassword"/>
        <ItemInput type="password" :name="cLang.pw.check" v-model:val="confirmPassword"/> -->

        <ItemAdd :word="cLang.pw.ok" @click="changePassword()" />
        <ItemAdd class="close" :word="cLang.pw.back" @click="ToPassPopup()" />
      </center>
    </div>
  </div>
</template>

<script>
import ItemAdd from '../elements/ItemAdd.vue';
import ItemInput from '../elements/ItemInput.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../views/fr.js'
import en from '../views/en.js';
import axios from 'axios';
import Cookies from 'js-cookie';

export default {
  components: { ItemAdd, ItemInput },
  props: ['ToPassPopup'],

  data() {
    return {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
  },

  methods: {
    changePassword() {
      let currentPasswordCookie;
      const roleAuth = Cookies.get('role');
      if (roleAuth === 'student'){
         currentPasswordCookie = Cookies.get('passwordStudent');
      }else if (roleAuth === 'professeur'){
         currentPasswordCookie = Cookies.get('passwordProfesseur');
      }
      if (this.currentPassword !== currentPasswordCookie) {
        // Afficher un message d'erreur ou gérer le cas où les mots de passe ne correspondent pas
        alert("mot de passe incorrecte");
        return;
      }

      if (this.newPassword !== this.confirmPassword) {
        // Afficher un message d'erreur ou gérer le cas où les mots de passe ne correspondent pas
        alert("mot de passe incompatible");
        return;
      }

      const newPassword = this.newPassword;

      console.log('Valeur de currentPassword:', this.currentPassword);
      console.log('Valeur de newPassword:', this.newPassword);
      console.log('Valeur de confirmPassword:', this.confirmPassword);

      const role = Cookies.get('role');
      console.log("le role pour changer le mdp est :  " + role);
      if (role === 'student' ){
        const matricule = Cookies.get('matriculeStudent');
        axios
            .put(`http://localhost:1937/students/updatePassword/${matricule}`, null, { params: { newPassword } })
            .then(response => {
              console.log(response);
              // Afficher un message de succès ou effectuer d'autres actions nécessaires
              Cookies.set('passwordStudent' , this.newPassword);
              window.close();
            })
            .catch(error => {
              console.error(error);
              // Afficher un message d'erreur ou effectuer d'autres actions nécessaires
            });
      } else if (role === 'professeur'){
        const matricule = Cookies.get('matriculeProfesseur');
        axios
            .put(`http://localhost:1937/teachers/updatePassword/${matricule}`, null, { params: { newPassword } })
            .then(response => {
              console.log(response);
              // Afficher un message de succès ou effectuer d'autres actions nécessaires
              Cookies.set('passwordStudent' , this.newPassword);
              window.close();
            })
            .catch(error => {
              console.error(error);
              // Afficher un message d'erreur ou effectuer d'autres actions nécessaires
            });
      }


      this.showPassPopup = false;
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
  top: 0px;
  bottom: 0px;
  left: 0px;
  right: 0px;
  z-index: 99;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.inner {
  position: relative;
  background: rgb(255, 255, 255);
  padding: 39px;
  border-radius: 10%;
}
</style>