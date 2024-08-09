<template>
<component :is="top"/>
    <div class="details-container">
    <center>
      <h1 class="details-title">{{ courseDetails.name }}</h1>
      <div class="details-info">

        <p><strong>{{ cLang.SignUp.section }} :</strong> {{ courseDetails.filiere }}</p>
        <p><strong>{{ cLang.SignUp.dep }} :</strong> {{ courseDetails.departement }}</p>
        <p><strong>{{ cLang.Courses.prof }} :</strong> {{ courseDetails.teacherName }}</p>
        <p><strong>Code :</strong> {{ courseDetails.code }}</p>
        <p><strong>{{ cLang.Top.prof }} :</strong></p>
        <ul>
          <li v-for="(teacher, index) in courseDetails.listOfAllteachersToThisCours" :key="index">{{ teacher }}</li>
        </ul>
        <p><strong>{{ cLang.Top.stu }} :</strong></p>
        <ul>
          <li v-for="(student, index) in courseDetails.studentList" :key="index">{{ student }}</li>
        </ul>

      </div>
      </center>
    </div>

</template>



<script>
import TopStudent from '../../elements/TopStudent.vue';
import TopSecretariat from '../../elements/TopSecretariat.vue';
import TopProf from '../../elements/TopProf.vue';
import TopGuest from '../../elements/TopGuest.vue';
import { computed, watch, ref } from 'vue';
import axios from 'axios';
import { useStore } from 'vuex';
import Cookies from 'js-cookie';
import fr from '../../views/fr.js';
import en from '../../views/en.js';

export default {
  components: {TopStudent, TopProf, TopGuest, TopSecretariat},
  data() {
      const store = useStore();
      const idLa = computed(() => store.state.lang.curLang);
      const cLang = ref(idLa.value === 'fr' ? fr : en);
      watch(idLa, (newLang) => {
        cLang.value = newLang === 'fr' ? fr : en;
      });

    const type = ref(Cookies.get('role'));
    console.log("role : "+type.value);
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
      courseDetails: {},
      top,
      cLang
    };
  },
  mounted() {
    // Récupérer le nom du cours depuis les cookies et charger les détails du cours
    const coursName = Cookies.get('selectedCourse');
    if (coursName) {
      this.getCourseDetails(coursName);
    }
  },
  methods: {
    getCourseDetails(coursName) {
      axios.get(`http://localhost:1937/api/cours/getByName?coursName=${coursName}`)
          .then(response => {
            // Mise à jour des détails du cours avec les données reçues du backend
            this.courseDetails = response.data;
          })
          .catch(error => {
            console.error('Erreur lors de la récupération des détails du cours :', error);
          });
    }
  }
};
</script>
<style >
.details-container {
  position: fixed;
  top: 12%;
  left: 30%;
  width: 90%;
  height: 60%;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.details-title {
  font-size: 40px;
  margin-bottom: 20px;
  color: #333;
}

.details-info {
  font-size: 23px;
  color: #666;
}

.details-info p {
  margin-bottom: 10px;
}

.details-info ul {
  list-style-type: none;
  padding-left: 0;
}

.details-info ul li {
  margin-bottom: 5px;
}

strong {
  font-weight: bold;
  color: #333;
}
</style>
