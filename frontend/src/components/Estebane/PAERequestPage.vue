<template>
  <div>
    <TopStudentPAE/>
    <div class="content">
      <h2>{{cLang.PAE.rq}}</h2>
      <p>Status: {{ paeRequest.status }}</p> <!-- Ensure status is displayed -->
      <ul>
        <li v-for="course in paeRequest.courses" :key="course.id">
          <ItemCours :word="course.name" :credits="course.credits"/>
        </li>
      </ul>
      <ItemButton :name="cLang.AddCours.ad" @click="showPopup = true"/>
      <ItemButton :name="cLang.PAE.sub" @click="submitPAERequest"/>
    </div>
    <div v-if="showPopup" class="popup">
      <h3>All Courses</h3>
      <ul>
        <li v-for="course in itemc" :key="course.id">
          {{ course.name }} - {{ course.credits }} credits
          <ItemButton name="+" @click="addCourseToPAERequest(course.id)" />
        </li>
      </ul>
      <ItemButton name="Close" @click="showPopup = false"/>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import TopStudentPAE from "@/elements/PAE/TopStudentPAE.vue";
import ItemButton from "@/elements/ItemButton.vue";
import ItemCours from "@/elements/ItemStudent.vue";
import Cookies from "js-cookie";
import {computed, ref, watch} from "vue";
import { useStore } from 'vuex';
import fr from '../../views/fr';
import en from '../../views/en';

class PAERequest {
  constructor(studentId) {
    this.courses = [];
    this.studentId = studentId;
    this.status = ''; // Ensure status is initialized
  }

  addCourse(course) {
    this.courses.push(course);
  }

  clear() {
    this.courses = [];
  }
}

export default {
  components: {ItemButton, TopStudentPAE, ItemCours },
  setup() {
    const matricule = ref(Cookies.get('matriculeStudent'));
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    return {
      cLang,
      matricule
    };
  },
  data() {
    return {
      itemc: [],
      showPopup: false,
      paeRequest: new PAERequest(Cookies.get('matriculeStudent'))
    };
  },
  methods: {
    getCours() {
      axios.get('http://localhost:1937/api/cours/All')
          .then((response) => {
            this.itemc = response.data;
          })
          .catch((error) => {
            console.error(error);
          });
    },
    addCourseToPAERequest(courseId) {
      const course = this.itemc.find(c => c.id === courseId);
      if (course) {
        this.paeRequest.addCourse(course);
      }
    },
    submitPAERequest() {
      axios.post('http://localhost:1937/api/pae-requests', this.paeRequest)
          .then(() => {
            this.paeRequest.clear();
            this.$router.push({ name: 'student/PAE/actual' });
          })
          .catch(error => {
            console.error(error);
          });
    }
  },
  mounted() {
    this.getCours();
  }
};
</script>

<style scoped>
.content {
  margin-top: 100px;
  position: relative;
  z-index: 1;
}
.popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  width: 80%;
  max-width: 500px;
}
.popup h3 {
  margin-top: 0;
  font-size: 1.5em;
  text-align: center;
}
.popup ul {
  list-style: none;
  padding: 0;
}
.popup li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}
.popup button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}
.popup button:hover {
  background-color: #0056b3;
}
.popup button:active {
  background-color: #004085;
}
</style>