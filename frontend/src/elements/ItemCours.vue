<template>
  <div class="center">
    <div class="item-cours">
      <router-link :to="{ name: 'courseSection', params: { cours: word } }">
        <button class="button" @click="redirectToSection">{{ word }}</button>
      </router-link>
      <center><button v-if="isProfessorRole()" class="delete-button" @click="showDeleteConfirmation">{{cLang.ServeIns.rm}}</button>
      <button class="details-button" @click="showDetails">Info</button></center>
    </div>
  </div>
</template>

<script>
import Cookies from 'js-cookie';
import Swal from 'sweetalert2'; // Importation de SweetAlert2
import axios from 'axios';
import fr from '../views/fr.js';
import en from '../views/en.js';
import { ref, computed, watch } from 'vue';
import { useStore } from 'vuex';

export default {
  props: ['word'],
  setup(){
      const store = useStore();
      const idLa = computed(() => store.state.lang.curLang);
      const cLang = ref(idLa.value === 'fr' ? fr : en);
       watch(idLa, (newLang) => {
             cLang.value = newLang === 'fr' ? fr : en;
           });
      return{
      cLang
      }
  },
  methods: {

    redirectToSection() {
      // Enregistrer le nom du cours dans les cookies
      Cookies.set('selectedCourse', this.word);
      // Rediriger vers la page de section
      this.$router.push({ name: 'courseSection', params: {cours: this.word} });
    },

    isProfessorRole() {
      const role = Cookies.get('role');
      return role === 'professeur';
    },
    showDeleteConfirmation() {
      Swal.fire({
        title: 'Êtes-vous sûr de vouloir supprimer ce cours ?',
        text: "Cette action est irréversible.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Oui, supprimer',
        cancelButtonText: 'Annuler'
      }).then((result) => {
        if (result.isConfirmed) {
          this.deleteCourse();
        }
      });
    },
    deleteCourse() {
      if (this.isProfessorRole()) {
        // Récupérer le nom du cours à supprimer
        const courseName = this.word;

        // Envoyer une requête DELETE à votre backend
        axios.delete(`http://localhost:1937/teachers/deleteCoursFromProf?coursName=${courseName}`)
            .then(response => {
              // Gérer la réponse du backend
              console.log(response.data);
              // Mettre à jour la liste des cours en appelant la méthode getCourses
              this.getCourses(); // Appel de la méthode getCourses passée en prop
            })
            .catch(error => {
              // Gérer les erreurs de requête
              console.error(error);
            });
      } else {
        // Gérer le cas où l'utilisateur n'a pas les autorisations nécessaires
        console.log("Vous n'êtes pas autorisé à supprimer le cours.");
      }
    },
    showDetails() {
      // Émettre un événement pour signaler que le bouton "Détails" a été cliqué
      this.$emit('show-details', this.word);
    },
    redirectToDetails() {
      // Enregistrer le nom du cours dans les cookies
      Cookies.set('selectedCourse', this.word);
      // Rediriger vers la page "DetailsCourse"
      this.$router.push({ name: 'DetailsCourse', params: { courseName: this.word }});
    }
  }
}
</script>

<style scoped>

.center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.button {
  --color: rgb(158, 11, 23);
  width: 100rem;
  height: 6rem;
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: .3em;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  transition: .5s;
  font-weight: 400;
  font-size: 20px;
  border: 2px solid;
  font-family: Roboto,sans-serif;
  text-transform: uppercase;
  color: var(--color);
  z-index: 1;
  margin-top: 3rem;
}

.button::before, .button::after {
  content: '';
  display: block;
  width: 3rem;
  height: 1rem;
  transform: translate(-50%, -50%);
  position: absolute;
  border-radius: 50%;
  z-index: -1;
  background-color: var(--color);
  transition: 0.8s ease;
}

.button::before {
  top: -1em;
  left: -1em;
}

.button::after {
  left: calc(100% + 1em);
  top: calc(100% + 1em);
}

.button:hover::before, .button:hover::after {
  height: 1700px;
  width: 1700px;
}

.button:hover {
  color: rgb(236, 181, 181);
}

.button:active {
  filter: brightness(.8);
}

.delete-button {
  margin-left: 10px;
  background-color: red;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: darkred;
}

.details-button {
  margin-left: 10px;
  background-color: grey;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
}

.details-button:hover {
  background-color: darkred;
}
</style>