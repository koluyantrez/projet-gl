<template>
  <div>
    <TopStudent/>
    <div class="place">
    <ItemSearch/>
      <ItemCours v-for="(item, index) in itemc" :word="item" :key="index"/>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import TopStudent from '../../elements/TopStudent.vue';
import ItemCours from '../../elements/ItemCours.vue';
import ItemSearch from '../../elements/ItemSearch.vue';
import Cookies from 'js-cookie';

export default {
  components: { ItemCours, TopStudent, ItemSearch },
  data() {
    return {
      itemc: [],
      matricule: ''
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

    getCours() {
      const email = Cookies.get('emailStudent');
      const password = Cookies.get('passwordStudent');
      if (!email || !password) {
        // Gérer le cas où les cookies ne sont pas présents
        return;
      }

      const login = {
        email: email,
        password: password
      };

      console.log("email : " + login.email);
      this.matricule = this.extractNumberBeforeAt(login.email)
      console.log("matricule : " + this.matricule);
      Cookies.set('matriculeStudent', this.matricule);
      axios.post('http://localhost:1937/students/getActuelCours', login)
          .then(response => {
            const cours = response.data.map(cours => cours.name);
            this.itemc = cours;
            this.$router.push({
              query: {
                matricule: login.email
              }
            });
          })
          .catch(error => {
            console.error(error);
          });
    },

    goToProfil() {
      const email = Cookies.get('email');

    }
  },

  created() {
    this.getCours();
    this.goToProfil(); // Appeler la méthode goToProfil()
  }
};
</script>

<style scoped>
.place {
    position: absolute;
    top: 20%;
    left: 8%;
}
</style>