<template>
  <div>
    <TopProf/>
    <div class="container">
      <div class="add">
        <button @click="showCourseSelection">Ajouter un cours</button>
      </div>
      <div class="place" :style="{pointerEvents: modalIsOpen ? 'none' : 'auto'}">
        <ItemCours v-for="(cours, index) in courses" :word="cours" :key="index" :role="role" :get-courses="getCourses" @show-details="showDetailsHandler" />
      </div>
    </div>

    <!-- Fenêtre modale de sélection des cours -->
    <div v-if="modalIsOpen" class="modal">
      <!-- Contenu de la fenêtre de sélection des cours -->
      <div class="modal-content">
        <h2 class="modal-title">Sélection des cours</h2>
        <div class="course-list">
          <ul class="course-scroll">
            <li v-for="(cours, index) in availableCourses" :key="index" class="course-item">
              {{ cours }}
              <button @click="addCourse(cours)" class="button-ajouter" id="buttonAjouter">Ajouter</button>
            </li>
          </ul>
        </div>
        <button @click="hideCourseSelection">Fermer</button>
      </div>
    </div>
  </div>
</template>

<script>
import ItemCours from '../../elements/ItemCours.vue';
import TopProf from '../../elements/TopProf.vue';
import axios from 'axios';
import Cookies from 'js-cookie';

export default {
  components: { ItemCours, TopProf },
  data() {
    return {
      courses: [],
      matricule: '',
      showModal: false,
      availableCourses: [],
      modalIsOpen: false,
    };
  },
  methods: {
    showDetailsHandler(courseName) {
      // Enregistrer le nom du cours dans les cookies
      Cookies.set('selectedCourse', courseName);
      // Rediriger vers la page DetailsCours
      this.$router.push({ name: 'DetailsCours' });
    },
    hideDetail() {
      // Méthode pour cacher la fenêtre modale de détails
      this.showDetailModal = false;
    },
    addCourse(cours) {
      const id = Cookies.get('matriculeProfesseur');
      const coursName = cours;

      axios.post(`http://localhost:1937/assign-course${id}`, null, {
        params: {
          coursName: coursName
        }
      })
          .then(response => {
            const result = response.data;
            // Gérer la réponse de la méthode attributeCourse
            if (result === 'attribute success') {
              // Le cours a été attribué avec succès au professeur
              // Mettre à jour la liste des cours
              this.getCourses();
              // Fermer la fenêtre de sélection des cours
              this.hideCourseSelection();
              alert("attribute success");
            } else {
              // Gérer les autres cas de retour de la méthode attributeCourse
            }
          })
          .catch(error => {
            console.error(error);
            // Gérer les erreurs de requête
          });
    },
    extractNumberBeforeAt(email) {
      const atIndex = email.indexOf('@');
      if (atIndex !== -1) {
        const number = email.substring(0, atIndex);
        return number;
      } else {
        return null;
      }
    },
    getCourses() {
      const email = Cookies.get('emailProfesseur');
      const password = Cookies.get('passwordProfesseur');
      if (!email || !password) {
        // Gérer le cas où les cookies ne sont pas présents
        return;
      }
      const login = {
        email: email,
        password: password
      };
      console.log("email :  " + login.email);
      this.matricule = this.extractNumberBeforeAt(login.email);
      Cookies.set('matriculeProfesseur', this.matricule);
      axios.post(`http://localhost:1937/teachers/getCourses`, login)
          .then(response => {
            const courses = response.data;
            this.courses = courses;
          })
          .catch(error => {
            console.error(error);
            // Gérer les erreurs de requête
          });
    },
    showCourseSelection() {
      axios.get('http://localhost:1937/cours/AllCours')
          .then(response => {
            const courses = response.data;
            this.availableCourses = courses.map(cours => cours.name);
            this.modalIsOpen = true;
          })
          .catch(error => {
            console.error(error);
            // Gérer les erreurs de requête
          });
    },
    hideCourseSelection() {
      this.modalIsOpen = false;
    },
  },
  created() {
    this.getCourses();
  }
}
</script>

<style scoped>
.container {
  position: absolute;
  width: 100%;
  height: 89%;
  top: 100px;
  overflow: auto;
}

.place {
  position: absolute;
  top: 5%;
  left: 10%;
}

.add {
  position: relative;
  top: 0;
  left: 0;
  margin-bottom: 20px;
}

.add button {
  font-size: 18px;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.add button:hover {
  background-color: #45a049;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal h2 {
  color: white;
}

.modal ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
  color: white;
}

#buttonAjouter {
  padding: 10px 10px;
  font-size: 15px;
  border-radius: 20px;
  color: white;
  cursor: pointer;
  background-color: white;
  color: black;
  border: solid black 2px;

}

#buttonAjouter:hover {
  background-color: green;
}

.small {
  background-color: green;
  font-size: 12px;
}

.medium {
  background-color: orange;
  font-size: 14px;
}

.large {
  background-color: red;
  font-size: 16px;
}

.modal button {
  font-size: 18px;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.modal button:hover {
  background-color: #45a049;
}

.course-item {
  list-style-type: disc;
  font-size: 20px;
}

.modal-title {
  font-size: 30px;
  transform: translateX(-100px);
}

.course-item {
  list-style-type: disc;
  font-size: 20px;
}

.course-list {
  max-height: 300px; /* Hauteur maximale de la liste des cours */
  overflow-y: auto; /* Activation du défilement vertical */
}

.course-scroll {
  padding: 0;
  margin: 0;
  color: white;
  overflow: hidden; /* Masquer le contenu qui dépasse */
}
</style>
