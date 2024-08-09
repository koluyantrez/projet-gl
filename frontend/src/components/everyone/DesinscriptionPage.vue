<template>
  <div>
    <TopSec/>
    <div class="container">
      <ItemSearch class="look" @search="filterStudents"/>
      <div class="place">
        <div v-for="(student, index) in filteredStudents" :key="index" class="student-item">
          <ItemStudent :word="student.lastName + ' ' + student.firstName"/>
          <button @click="showPopup(student)">&#x2716;</button>
        </div>
      </div>
      <StudentPopup v-if="showPopupFlag" :student="selectedStudent" @close="closePopup" @delete="deleteStudent"/>
    </div>
  </div>
</template>

<script>
import TopSec from '@/elements/TopSec.vue';
import ItemSearch from '@/elements/ItemSearch.vue';
import ItemStudent from '@/elements/ItemStudent.vue';
import StudentPopup from '../../popup/StudentPopup.vue';
import axios from 'axios';
import { ref } from 'vue';

export default {
  components: { TopSec, ItemSearch, ItemStudent, StudentPopup },
  setup() {
    const students = ref([]);
    const filteredStudents = ref([]);
    const selectedStudent = ref(null);
    const showPopupFlag = ref(false);

    const getStudents = () => {
      axios.get('http://localhost:1937/api/students')
          .then(response => {
            students.value = response.data;
            filteredStudents.value = students.value;
          })
          .catch(error => {
            console.error(error);
          });
    };

    const filterStudents = (query) => {
      filteredStudents.value = students.value.filter(student => {
        return student.lastName.toLowerCase().includes(query.toLowerCase()) || student.firstName.toLowerCase().includes(query.toLowerCase());
      });
    };

    const showPopup = (student) => {
      selectedStudent.value = student;
      showPopupFlag.value = true;
    };

    const closePopup = () => {
      showPopupFlag.value = false;
      selectedStudent.value = null;
    };

    const deleteStudent = (studentId) => {
      axios.delete(`http://localhost:1937/api/students/${studentId}`)
          .then(() => {
            students.value = students.value.filter(s => s.id !== studentId);
            filteredStudents.value = filteredStudents.value.filter(s => s.id !== studentId);
            closePopup();
          })
          .catch(error => {
            console.error(error);
          });
    };

    getStudents();

    return {
      filteredStudents,
      filterStudents,
      showPopup,
      closePopup,
      deleteStudent,
      showPopupFlag,
      selectedStudent
    };
  }
};
</script>

<style scoped>
.container {
  position: absolute;
  width: 100%;
  height: 54rem;
  bottom: 0.1rem;
  left: -0.42rem;
}

.place {
  position: absolute;
  top: 15%;
  left: 10%;
}

.look {
  position: absolute;
  top: 4%;
  right: 15%;
}

.student-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.student-item button {
  background: none;
  border: none;
  color: red;
  font-size: 20px;
  cursor: pointer;
}
</style>