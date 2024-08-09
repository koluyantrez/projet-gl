<template>
  <component :is="top"/>
  <ItemAdd v-if="isProfessorRole" class="bu" :word="cLang.RenameCo.rho" @click="() => ToRenamePopup('buTriMod')" />
  <div class="nc">{{code}} {{courseName}}</div>
  <div class="rec">
    <Cours :word="cLang.Course.th"/>
    <Cours :word="cLang.Course.tp"/>
    <Cours :word="cLang.Course.exam"/>
    <Cours :word="cLang.Course.link"/>
    <Cours word="Forum"/>
  </div>
  <div class="who">
    <h2>{{cLang.Course.mp}}</h2>
    <p>{{prof}}</p>
    {{emailProf}}
  </div>
    <RenameCo v-if="popupMod.buTriMod" :ToRenamePopup="() => ToRenamePopup('buTriMod')" />
</template>


<script>
import RenameCo from '../../popup/RenameCo.vue';
import TopStudent from '../../elements/TopStudent.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import TopProf from '../../elements/TopProf.vue';
import TopGuest from '../../elements/TopGuest.vue';
import TopSecretariat from '../../elements/TopSecretariat.vue';
import Cours from '../../elements/Cours.vue';
import axios from 'axios';
import Cookies from 'js-cookie';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';


export default {
  components: {RenameCo, ItemAdd, TopStudent, TopProf, TopGuest, TopSecretariat, Cours},

  data() {
    const isProfessorRole = ref(Cookies.get('role') === 'professeur');
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);
    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

        const popupMod = ref({
          buTriMod: false
        });
        const ToRenamePopup = (tri2) => {
          popupMod.value[tri2] = !popupMod.value[tri2]
        }

    const type = ref(Cookies.get('role'));
    const top = computed(() => {
      let result;
      if (type.value === 'student') {
        result = 'TopStudent';
      } else if (type.value === 'professeur') {
        result = 'TopProf';
      } else if (type.value === 'secretariat') {
              result = 'TopSecretariat';
      } else {
        result = 'TopGuest';
      }
      return result;
    });

    return {
    popupMod,
    ToRenamePopup,
    isProfessorRole,
      cLang,
      top,
      courseName: '',
      prof: '',
      emailProf: '',
      code: '',
    };
  },

  name: 'courseSection',
  computed: {
    course() {
      return this.$route.params.cours;
    }
  },

  mounted() {
    this.getStudentName();
    this.getSensei();
  },

  methods: {
    getSensei() {
      axios.get(`http://localhost:1937/api/cours/getByName?coursName=${this.$route.params.cours}`)
          .then(response => {
            const info = response.data;
            this.code = info.code;
            console.log(this.code);
            this.courseName = info.name;
            this.prof = info.teacherName;
            console.log(info.teacherName);
            axios.get(`http://localhost:1937/api/professeurs/findByName?teacherName=${this.prof}`)
                .then(response => {
                  const sensei = response.data;
                  this.emailProf = sensei.email;
                })
                .catch(error => {
                  console.error(error);
                });
          })
          .catch(error => {
            console.error(error);
          });


    },

    getStudentName() {
      const role = Cookies.get('role');
      let matricule = '';

      if (role === 'professeur') {
        matricule = Cookies.get('matriculeProfesseur');
        axios.get(`http://localhost:1937/api/professeurs/${matricule}`)
            .then(response => {
              const teacher = response.data;
              this.studentName = teacher.name; // Mettre à jour le nom du professeur
              console.log(teacher.name);
            })
            .catch(error => {
              console.error(error);
            });
      } else if (role === 'student') {
        matricule = Cookies.get('matriculeStudent');
        axios.get(`http://localhost:1937/api/students/${matricule}`)
            .then(response => {
              const student = response.data;
              this.studentName = student.name; // Mettre à jour le nom de l'étudiant
              console.log(response.data);
            })
            .catch(error => {
              console.error(error);
            });
      }
    },


    getCookie(name) {
      const value = `; ${document.cookie}`;
      const parts = value.split(`; ${name}=`);
      if (parts.length === 2) {
        return parts.pop().split(';').shift();
      }
      return null;
    }
  }
}
</script>
<style scoped>

.nc {
  position: absolute;
  top: 10%;
  left: 2%;
  font-size: 70px;
  font-family: Roboto, sans-serif;
  color: #9F0924;

}

.bu{
  position: absolute;
  top: 20%;
  left: 2%;
}

.rec {
  position: absolute;
  top: 28%;
  left: 2%;
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* Trois colonnes de largeur égale */
  column-gap: 15%; /* Espacement uniforme entre les éléments */
  row-gap: 20%;
}

.who {
  position: absolute;
  top: 25%;
  right: 5%;
  color: black;
  font-size: 30px;
  font-family: Roboto, sans-serif;
}
</style>