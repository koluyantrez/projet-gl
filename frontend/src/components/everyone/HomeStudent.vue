<template>
  <div>
    <TopStudent />
    <div class="place">
      <ItemSearch @search="filterCourses" />
      <ItemCours v-for="(item, index) in filteredCourses" :word="item" :key="index" @show-details="handleShowDetails" />
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
      itemc: [], // Liste complète des cours
      filteredCourses: [], // Liste filtrée des cours
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
      this.matricule = this.extractNumberBeforeAt(login.email);
      console.log("matricule : " + this.matricule);
      Cookies.set('matriculeStudent', this.matricule);
      axios.post('http://localhost:1937/students/getActuelCours', login)
          .then(response => {
            const cours = response.data.map(cours => cours.name);
            this.itemc = cours;
            this.filteredCourses = cours; // Initialiser les cours filtrés
          })
          .catch(error => {
            console.error(error);
          });
    },

    goToProfil() {
      const email = Cookies.get('email');
    },

    handleShowDetails(word) {
      this.$router.push({ name: 'courseSection', params: { courseName: word } });
    },

    filterCourses(query) {
      console.log('Filter query:', query); // Debug: vérifier la valeur de la requête
      this.filteredCourses = this.itemc.filter(course =>
          course.toLowerCase().includes(query.toLowerCase())
      );
      console.log('Filtered courses:', this.filteredCourses); // Debug: vérifier les cours filtrés
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
  width: 80%; /* Assurez-vous que la largeur est suffisante */
  display: flex;
  flex-direction: column;
  align-items: center;
}

.item-search {
  margin-bottom: 20px; /* Espacer la barre de recherche des cours */
}
</style>
