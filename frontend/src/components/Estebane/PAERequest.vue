<template>
  <div>
    <TopSecretariat/>
    <div class="container">
      <h1>{{cLang.PAE.rq}}</h1>
      <ItemSearch class="se" @search="filterRequest"/>
      <div class="place">
        <ItemCours v-for="(request, index) in filteredRequest" :word="request.studentId" :key="index" @click="showRequestPopup(request)"/>
      </div>
    </div>
    <PAEDetailsPopup v-if="showPopup" :request="selectedRequest" @close="closeRequestPopup" @accept="acceptPAERequest"/>
  </div>
</template>

<script>
import PAEDetailsPopup from '../../popup/PAEDetailsPopup.vue';
import TopSec from "@/elements/TopSec.vue";
import TopSecretariat from "@/elements/TopSecretariat.vue";
import ItemCours from "@/elements/ItemStudent.vue";
import ItemSearch from "@/elements/ItemSearch.vue";
import {computed, ref, watch, onMounted} from "vue";
import Cookies from "js-cookie";
import {useStore} from "vuex";
import fr from "@/views/fr";
import en from "@/views/en";
import axios from "axios";

export default {
  components: {
    TopSecretariat,
    TopSec,
    PAEDetailsPopup,
    ItemCours,
    ItemSearch
  },
  setup() {
    const paeRequests = ref([]);
    const students = ref([]);
    const filteredRequest = ref(paeRequests.value);
    const selectedRequest = ref(null);
    const showPopup = ref(false);

    const getPAERequests = () => {
      axios.get('http://localhost:1937/api/pae-requests')
          .then(response =>{
            paeRequests.value = response.data;
            filteredRequest.value = paeRequests.value;
          })
          .catch(error => {
            console.error(error);
          });

    };

    const getStudents = () => {
      axios.get('http://localhost:1937/api/students')
          .then(response => {
            students.value = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    };

    const filterRequest = (query) => {
      filteredRequest.value = paeRequests.value.filter((request) => {
        const studentName = getStudentName(request.studentId);
        return studentName.toLowerCase().includes(query.toLowerCase());
      });
    };

    const getStudentName = (studentId) => {
      const student = students.value.find(student => student.id === studentId);
      return student.firstName;
    };

    onMounted(() => {
      getPAERequests();
      getStudents();
    });

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const showRequestPopup = (request) => {
      selectedRequest.value = request;
      showPopup.value = true;
    };

    const closeRequestPopup = () => {
      showPopup.value = false;
      selectedRequest.value = null;
    };

    const acceptPAERequest = (request) => {
      axios.post(`http://localhost:1937/api/students/pae/accept/${request.studentId}`, request)
          .then(() => {
            getPAERequests();
            closeRequestPopup();
          })
          .catch(error => {
            console.error(error);
          });
    };

    return {
      cLang,
      paeRequests,
      students,
      filteredRequest,
      selectedRequest,
      showPopup,
      filterRequest,
      getStudentName,
      showRequestPopup,
      closeRequestPopup,
      acceptPAERequest
    };
  }
}
</script>

<style scoped>
.se {
  position: absolute;
  top: 4%;
  right: 15%;
}

.container {
  position: absolute;
  width: 100%;
  height: 89%;
  top: 100px;
}

.place {
  position: absolute;
  top: 15%;
  left: 10%;
}
</style>