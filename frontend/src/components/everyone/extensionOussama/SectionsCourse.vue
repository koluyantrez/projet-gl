<template>
  <div class="course-page">
    <header class="course-header">
      <img src="@/assets/illumis.png" alt="Logo de l'université" class="logo-universite">
      <div class="student-name">{{ studentName }}</div>
    </header>

    <h1>{{ courseName }}</h1>
    <div class="course-content" style="background-image: url('../../../assets/bghomed.jpg');">
      <!-- Sections -->
      <div class="course-section course-section-hover">
        <h2>Cours</h2>
        <!-- Contenu de la section Cours -->
      </div>
      <div class="course-section course-section-hover">
        <h2>Exercices</h2>
        <!-- Contenu de la section Exercices -->
      </div>
      <div class="course-section course-section-hover">
        <h2>Anciens examens</h2>
        <!-- Contenu de la section Anciens examens -->
      </div>
      <div class="course-section course-section-hover">
        <h2>Forum</h2>
        <!-- Contenu de la section Forum -->
      </div>
      <div class="course-section course-section-hover">
        <h2>Liens utiles</h2>
        <!-- Contenu de la section Liens utiles -->
      </div>
    </div>

  </div>
</template>


<script>
import axios from 'axios';
import Cookies from 'js-cookie';


export default {
  data() {
    return {
      studentName: '' // Nom de l'étudiant
    };
  },
  mounted() {
    this.getStudentName();
  },
  methods: {
    getStudentName() {
      const role = Cookies.get('role');
      let matricule = '';

      if (role === 'professeur') {
        matricule = Cookies.get('matriculeProfesseur');
        axios.get(`http://localhost:1937/teachers/findById?matricule=${matricule}`)
            .then(response => {
              const teacher = response.data;
              this.studentName = teacher.name; // Mettre à jour le nom du professeur
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
<style>
* {
  box-sizing: border-box;
}

header{
  border-radius: 20px;
}
.student-name {
  margin-left: auto;
  font-size: 20px;
  color: #333;
}

body {
  margin: 0;
  font-family: Arial, sans-serif;
}

.course-page {
  padding: 20px;
}

.course-header {
  display: flex;
  align-items: center;
  //background-color: #f2f2f2;
  padding: 20px;
  margin-bottom: 20px;
}

body {
  background-image: url("../../../assets/bgHome.png");
  background-size: cover; /* Pour couvrir tout l'arrière-plan */
  background-position: center; /* Pour centrer l'image */
  background-repeat: no-repeat; /* Pour éviter la répétition de l'image */
}


.logo-universite {
  height: 60px;
  margin-right: 10px;
}

.course-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.course-section {
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: transform 0.3s;
}

.course-section h2 {
  margin-top: 0;
  font-size: 24px;
  color: #333;
}

.course-section-hover {
  cursor: pointer;
}

.course-section-hover:hover {
  transform: scale(1.1);
}
</style>