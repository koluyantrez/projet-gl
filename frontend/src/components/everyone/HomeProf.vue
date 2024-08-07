<template>
  <TopProf/>
  <div class="container">
    <ItemAdd class="add" @click="() => ToShow('modalIsOpen')" :word="cLang.AddCours.title" />

    <div class="place" :style="{pointerEvents: modalIsOpen ? 'none' : 'auto'}">
      <ItemCours v-for="(cours, index) in courses" :word="cours" :key="index" :role="role" :get-courses="getCourses" @show-details="showDetailsHandler" />
    </div>
  </div>

  <ListCoursAdd v-if="li.modalIsOpen" :ToShow="() => ToShow('modalIsOpen')"/>
</template>

<script>
import ItemCours from '../../elements/ItemCours.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import TopProf from '../../elements/TopProf.vue';
import ListCoursAdd from '../../popup/ListCoursAdd.vue';
import {useStore} from 'vuex';
import {computed, watch, ref} from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import axios from 'axios';
import Cookies from 'js-cookie';

export default {
  components: {ItemCours, TopProf, ItemAdd, ListCoursAdd},
  data() {
    const li = ref({
      modalIsOpen: false
    });

    const ToShow = (tri) => {
      li.value[tri] = !li.value[tri];
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
      this.$router.push({name: 'DetailsCours'});
    },
    hideDetail() {
      // Méthode pour cacher la fenêtre modale de détails
      this.showDetailModal = false;
    },
    extractNumberBeforeAt(email) {
      const atIndex = email.indexOf('@');
      if (atIndex !== -1) {
        return email.substring(0, atIndex);
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
      this.matricule = this.extractNumberBeforeAt(login.email);
      Cookies.set('matriculeProfesseur', this.matricule);
      Cookies.set('demandeur',this.matricule);
      axios.post(`http://localhost:1937/api/professeurs/courses`, {
        email: Cookies.get('emailProfesseur'),
        password: Cookies.get('passwordProfesseur')
      })
          .then(response => {
            // Supposons que la réponse contienne un tableau de cours
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
  width: 100rem;
  height: 54rem;
  bottom: 0.1rem;
  /*border: 3px solid rgb(6, 148, 37); /* Bordure de la zone conteneur */
}

.place {
  position: absolute;
  top: 7rem;
  left: 10rem;
}

.add {
  position: absolute;
  top: 3rem;
  left: 3rem;
  margin-bottom: 20px;
}

</style>