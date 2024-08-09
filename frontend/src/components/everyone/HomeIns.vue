<template>
  <TopSec/>
  <div class="container">
  <ItemSearch class="look" @search="filterStudents"/>
    <div class="place">
      <ItemStudent v-for="(student, index) in filteredStudents" :word="student.lastName + ' ' + student.firstName" :key="index" @click="showStudentInfo(student)"/>
    </div>
    <StudentInfoPopup v-if="showPopup" :student="selectedStudent" @close="closePopup"/>
  </div>


</template>

<script>
import YesOrNo from '../../popup/YesOrNo.vue';
import GiveInfo from '../../popup/GiveInfo.vue';
import TopSec from '../../elements/TopSec.vue';
import ItemSearch from '../../elements/ItemSearch.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import { useStore } from 'vuex';
import {computed, watch, ref, onMounted} from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import axios from 'axios';
import Cookies from 'js-cookie';
import ItemCours from "@/elements/ItemStudent.vue";
import ItemStudent from "@/elements/ItemStudent.vue";
import StudentInfoPopup from "@/popup/StudentInfoPopup.vue";

export default {
  components: {StudentInfoPopup, ItemStudent, ItemCours, TopSec, ItemSearch, ItemAdd, YesOrNo, GiveInfo },
  setup() {
    const students = ref([]);
    const filteredStudents = ref(students.value);
    const selectedStudent = ref(null);
    const showPopup = ref(false);

    const getSignupRequests = () => {
      axios.get('http://localhost:1937/api/students')
          .then((response) => {
            students.value = response.data;
            filteredStudents.value = students.value;
          })
          .catch((error) => {
            console.error(error);
          });
    };

    const filterStudents = (query) => {
      filteredStudents.value = students.value.filter((student) => {
        return student.lastName.toLowerCase().includes(query.toLowerCase()) || student.firstName.toLowerCase().includes(query.toLowerCase());
      });
    };

    const showStudentInfo = (student) => {
      selectedStudent.value = student;
      showPopup.value = true;
    };

    const closePopup = () => {
      showPopup.value = false;
      selectedStudent.value = null;
    };

    onMounted(getSignupRequests);

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const popupCreate = ref({
      buCreate: false
    });

    const ToCreatePopup = (tri1) => {
      popupCreate.value[tri1] = !popupCreate.value[tri1];
      if (!popupCreate.value[tri1]) {
        getSignupRequests();
      }
    };


    return {
      students,
      selectedStudent,
      showPopup,
      showStudentInfo,
      closePopup,
      ToCreatePopup,
      cLang,
      filterStudents,
      filteredStudents
    };
  }
};
</script>

<style scoped>
.s {
  position: absolute;
  top: -2rem;
  right: -24rem;
}

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
</style>
