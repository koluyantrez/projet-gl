<template>
  <component :is="top"/>
    <div class="nc"><h1>{{code}} {{course}}</h1></div>
    <div class="rec">
        <Cours :word="cLang.Course.th"/>
        <Cours :word="cLang.Course.tp"/>
        <Cours :word="cLang.Course.exam"/>
        <Cours :word="cLang.Course.link"/>
        <Cours word="Forum"/>
    </div>
    <div class="who">
        <h4>{{cLang.Course.mp}}</h4>
        <p>{{prof}}</p>
        {{emailProf}}
        <h4>Assistant(s)</h4>
        <p>Wout Faes</p>
        205@Illumis.assistant.ac.be
        <p>Maxime De Cuyper</p>
        206@Illumis.assistant.ac.be
    </div>
</template>


<script>
import TopStudent from '../../elements/TopStudent.vue';
import TopProf from '../../elements/TopProf.vue';
import TopGuest from '../../elements/TopGuest.vue';
import Cours from '../../elements/Cours.vue';
import axios from 'axios';
import Cookies from 'js-cookie';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';


export default {
  components: {TopStudent, TopProf, TopGuest, Cours},

    data() {
      const store = useStore();
      const idLa = computed(() => store.state.lang.curLang);
      const cLang = ref(idLa.value === 'fr' ? fr : en);
      watch(idLa, (newLang) => {
          cLang.value = newLang === 'fr' ? fr : en;
      });

      const type = ref(Cookies.get('role'));
      const top = computed(() => {
            let result;
            if (type.value === 'student') {
              result = 'TopStudent';
            } else if (type.value === 'professeur') {
              result = 'TopProf';
            } else {
              result = 'TopGuest';
            }
            return result;
          });

      return {
        cLang,
        top,
        course: '',
        studentName: '',
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
    getSensei(){
        axios.get(`http://localhost:1937/GetCoursByName?coursName=${this.$route.params.cours}`)
            .then(response => {
                const info = response.data;
                this.code = info.code;
                this.prof = info.teacherName;
                console.log(response.data);
                axios.get(`http://localhost:1937/teachers/findByName?name=${info.teacherName}`)
                            .then(response => {
                                const sensei = response.data;
                                console.log(sensei.data);
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
        axios.get(`http://localhost:1937/teachers/findById?matricule=${matricule}`)
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
        axios.get(`http://localhost:1937/students/findById?matricule=${matricule}`)
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

.nc{
    position: absolute;
    top: 5%;
    left: 2%;
    font-size: 42px;
    font-family: Roboto,sans-serif;
    color: #9F0924;

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

.who{
  position: absolute;
  top: 25%;
  right: 5%;
  color: black;
  font-size: 25px;
  font-family: Roboto,sans-serif;
}

</style>
