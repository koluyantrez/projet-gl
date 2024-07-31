<template>
  <div class="details-container">
    <h1 class="details-title">{{ courseDetails.name }}</h1>
    <div class="details-info">
      <p><strong>Filière :</strong> {{ courseDetails.filiere }}</p>
      <p><strong>Département :</strong> {{ courseDetails.departement }}</p>
      <p><strong>Enseignant :</strong> {{ courseDetails.teacherName }}</p>
      <p><strong>Code :</strong> {{ courseDetails.code }}</p>
      <p><strong>Liste des enseignants :</strong></p>
      <ul>
        <li v-for="(teacher, index) in courseDetails.listOfAllteachersToThisCours" :key="index">{{ teacher }}</li>
      </ul>
      <p><strong>Liste des étudiants :</strong></p>
      <ul>
        <li v-for="(student, index) in courseDetails.studentList" :key="index">{{ student }}</li>
      </ul>
    </div>
  </div>
</template>


<script>
import axios from 'axios';
import Cookies from 'js-cookie';

export default {
  data() {
    return {
      courseDetails: {}
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
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.details-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.details-info {
  font-size: 18px;
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
