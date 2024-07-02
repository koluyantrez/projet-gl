<template>
  <div class="container1">
    <center>
      <img class="logo" src="../../assets/illumis.png" alt="MyLogo">
      <h1>{{cLang.FirstPage.region}}</h1>
      <ItemInput type="text" :name="cLang.FirstPage.id" v-model:val="email"/>
      <ItemInput type="password" :name="cLang.FirstPage.pw" v-model:val="password"/>
    </center>
    <div class="forb">
      <ItemButton :name="cLang.FirstPage.login" @click="submitform" />
      <router-link to="/guest">
        <ItemButton :name="cLang.FirstPage.guest"/>
      </router-link>
    </div>
    <img class="la" alt="Change the language" src="../../assets/lang.png" @click="switchLang"/>
  </div>
</template>

<script>
import ItemButton from '../../elements/ItemButton.vue';
import ItemInput from '../../elements/ItemInput.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js'
import en from '../../views/en.js'
import Cookies from 'js-cookie';

function determineUserRole(email) {
  console.log(email);
  if (email.endsWith("@Illumis.assistant.ac.be")) {
    return "assistant";
  } else if (email.endsWith("@Illumis.professeur.ac.be")) {
    return "professeur";
  } else if (email.endsWith("@Illumis.student.ac.be")) {
    return "student";
  }else if (email.endsWith("@Illumis.inscription.ac.be")) {
    return "inscription";
  }
  else {
    return "inconnu";
  }
}
export default {
  components: { ItemButton, ItemInput },
  data() {
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);
    const switchLang = () => {
        if (idLa.value === 'fr') {
            store.commit('setLang', 'en');
        }
        else if (idLa.value === 'en') {
            store.commit('setLang', 'fr');
        } 
    };
    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });
    return {
      switchLang,
      cLang,
      email: '',
      password: '',
      role: 'inconnu'
    };
  },
  methods: {
    extractNumberBeforeAt(email) {
      const atIndex = email.indexOf('@');
      if (atIndex !== -1) {
        const number = email.substring(0, atIndex);
        return number;
      } else {
        return null;
      }
    },
    submitform() {

      const authentification = {
        email: this.email,
        password: this.password,
      };
      fetch('http://localhost:1937/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(authentification)
      })
          .then(response => {
            this.role = determineUserRole(this.email);
            if (response.ok) {
              if (this.role === 'student') {
                // Stockage du mail et du mot de passe dans les cookies
                Cookies.set('emailStudent', this.email);
                Cookies.set('passwordStudent', this.password);
                Cookies.set('role' , this.role);
                console.log("cookies student : " + Cookies.get('emailStudent'));
                console.log("cookies password student " + Cookies.get('passwordStudent'));
                this.$router.push({
                  name: 'student',
                  query: {
                    email: this.email,
                    password: this.password
                  }
                });
              } else if (this.role === 'professeur'){
                Cookies.set('emailProfesseur', this.email);
                Cookies.set('passwordProfesseur', this.password);
                Cookies.set('role' , this.role);
                this.$router.push({
                  name: 'prof',
                  query: {
                    email: this.email,
                    password: this.password
                  }
                });
              }else if (this.role === 'inscription'){
                Cookies.set('emailInscription', this.email);
                Cookies.set('passwordInscription', this.password);
                Cookies.set('role' , this.role);
                const mat = this.extractNumberBeforeAt(this.email)
                Cookies.set('matriculeInscription' , mat);
                this.$router.push({
                  name: 'inscription',
                });
              }
            } else if (response.status === 401) {
              alert("Mot de passe incorrect");
            } else if (response.status === 404) {
              alert("User non trouvé");
            } else if (response.status === 500) {
              alert("Erreur inattendue");
            } else {
              alert("Erreur de serveur inattendue");
            }
          })
          .then(() => {
            console.log("role : " + this.role);
            Cookies.set('role ' , this.role);
            console.log("cookies role :  " + this.role);
            if (this.role === 'assistant') {
              // Reste du code pour le rôle d'assistant
            } else if (this.role === 'professeur') {
              // Reste du code pour le rôle de professeur
            } else if (this.role === 'student') {
              // Reste du code pour le rôle d'étudiant
            } else {
              console.log('Rôle d\'utilisateur inconnu');
            }
          })
          .catch(error => {
            console.error('Erreur : ', error.message);
            alert('Erreur lors de la connexion au serveur : ' + error.message);
          });
    }
  }
  
};
</script>

<style scoped>
.container1 h1 {
  font-family: Roboto, sans-serif;
  font-optical-sizing: auto;
  font-size: 3rem;
  font-stretch: 100%;
  font-weight: 600;
  color: rgb(236, 210, 210);
}

.container1 {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #9F0924;
  z-index: 90;
}

.logo {
  width: 500px;
  height: 220px;
}

.forb {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
  align-items: center;
}

.la{
  position: relative;
  width: 5rem;
  height: auto;
  bottom: -30px;
  left: 47%;
  }
</style>

