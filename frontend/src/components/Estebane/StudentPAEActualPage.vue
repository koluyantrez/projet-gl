<template>
  <div>
    <TopStudentPAE/>
    <div class="content">
      <h2>Current PAE</h2>
      <ul>
        <li v-for="course in currentPAE.courses" :key="course.id">
          {{ course.name }} - {{ course.credits }} credits
        </li>
      </ul>
      <p>Total Credits: {{ totalCredits }}</p>
      <ItemButton name="Add Course" @click="showPopup = true"/>
    </div>
    <div v-if="showPopup" class="popup">
      <h3>All Courses</h3>
      <ul>
        <li v-for="course in itemc" :key="course.id">
          {{ course.name }} - {{ course.credits }} credits
          <ItemButton name="+" @click="addCourseToPAE(course.id)" />
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

export default {
  components: {ItemButton, TopStudentPAE },
  data() {
    return {
      currentPAE: {
        courses: []
      },
      itemc: [],
      showPopup: false
    };
  },
  computed: {
    totalCredits() {
      return this.currentPAE.courses.reduce((sum, course) => sum + course.credits, 0);
    }
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

    fetchCurrentPAE() {
      axios.get('/api/pae/current')
          .then(response => {
            this.currentPAE = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    },
    fetchAllCourses() {
      axios.get('/api/cours')
          .then(response => {
            this.allCourses = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    },
    addCourseToPAE(courseId) {
      axios.post(`/api/cours/add/${this.currentPAE.id}/${courseId}`)
          .then(() => {
            this.fetchCurrentPAE();
          })
          .catch(error => {
            console.error(error);
          });
    }
  },
  mounted() {
    this.fetchCurrentPAE();
    this.fetchAllCourses();
  },
  created() {
    this.getCours();
  }
};
</script>

<style scoped>

.content{
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