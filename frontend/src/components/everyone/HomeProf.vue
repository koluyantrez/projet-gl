<template>
    <TopProf/>
    <div class="container">
    <ItemAdd class="add" @click="() => ToShow('modalIsOpen')" :word="cLang.AddCours.title" />

      <div class="place" :style="{pointerEvents: modalIsOpen ? 'none' : 'auto'}">
        <ItemCours v-for="(cours, index) in courses" :word="cours" :key="index" :role="role" :get-courses="getCourses" @show-details="showDetailsHandler" />
      </div>
    </div>

    <ListCoursAdd v-if="li.modalIsOpen" :ToShow="() => ToShow('modalIsOpen')" />
</template>

<script>
import ItemCours from '../../elements/ItemCours.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import TopProf from '../../elements/TopProf.vue';
import ListCoursAdd from '../../popup/ListCoursAdd.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import axios from 'axios';
import Cookies from 'js-cookie';

export default {
  components: { ItemCours, TopProf, ItemAdd, ListCoursAdd },
  data() {

    const li = ref({
        modalIsOpen: false
    });
    const ToShow = (tri) => {
        li.value[tri] = !li.value[tri]
    };

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    return {
      courses: [],
      matricule: '',
      showModal: false,
      li,
      ToShow,
      cLang
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
  },
  created() {
    this.getCourses();
  }
}
</script>

<style scoped>
.container {
  position: absolute;
  width: 99%;
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
  position: absolute;
  top: 3rem;
  left: 3rem;
  margin-bottom: 20px;
}


.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 20);
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
