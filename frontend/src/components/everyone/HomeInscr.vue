<template>
  <TopSec/>
  <div class="container">
    <div class="place">
      <ItemAdd :word="cLang.Top.addel" @click="() => ToCreatePopup('buCreate')"/>
      <ItemSearch @search="filterRequest"/>
      <ItemCours v-for="(request, index) in filteredRequest" :word="request.name + ' ' + request.firstName" :key="index" @click="showRequestPopup(request)"/>
    </div>
  </div>
  <AddStudent v-if="popupCreate.buCreate" :ToCreatePopup="() => ToCreatePopup('buCreate') "/>
  <Request v-if="showPopup" :request="selectedRequest" @close="closeRequestPopup" @accept="handleAcceptRequest" @reject="handleRejectRequest"/>
</template>

<script>
import TopSec from '../../elements/TopSec.vue';
import ItemCours from '../../elements/ItemStudent.vue';
import ItemSearch from '../../elements/ItemSearch.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import AddCours from '../../popup/AddCours.vue';
import { useStore } from 'vuex';
import { computed, watch, ref, onMounted } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import axios from "axios";
import AddStudent from "@/popup/AddStudent.vue";
import Request from "@/popup/Request.vue";

export default {
  components: {
    Request,
    AddStudent,
    ItemCours,
    TopSec,
    ItemSearch,
    ItemAdd,
    AddCours
  },
  setup() {
    const signupRequests = ref([]);
    const filteredRequest = ref(signupRequests.value);
    const selectedRequest = ref(null);
    const showPopup = ref(false);

    const getSignupRequests = () => {
      axios.get('http://localhost:1937/secretariat/requests')
          .then((response) => {
            signupRequests.value = response.data;
            filteredRequest.value = signupRequests.value;
          })
          .catch((error) => {
            console.error(error);
          });
    };

    const filterRequest = (query) => {
      filteredRequest.value = signupRequests.value.filter((request) => {
        return request.name.toLowerCase().includes(query.toLowerCase()) || request.firstName.toLowerCase().includes(query.toLowerCase());
      });
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

    const showRequestPopup = (request) => {
      selectedRequest.value = request;
      showPopup.value = true;
    };

    const closeRequestPopup = () => {
      showPopup.value = false;
      selectedRequest.value = null;
    };

    const handleAcceptRequest = (request) => {
      // Handle additional logic if needed
      closeRequestPopup();
      getSignupRequests();
    };

    const handleRejectRequest = (request) => {
      // Handle additional logic if needed
      closeRequestPopup();
      getSignupRequests();
    };

    return {
      signupRequests,
      selectedRequest,
      showPopup,
      popupCreate,
      ToCreatePopup,
      showRequestPopup,
      closeRequestPopup,
      handleAcceptRequest,
      handleRejectRequest,
      cLang,
      filterRequest,
      filteredRequest
    };
  }
}
</script>

<style>
.container {
  position: absolute;
  width: 99%;
  height: 89%;
  top: 100px;
  overflow: auto;
}

.place {
  position: absolute;
  top: 5%;
  left: 10%;
}

.pic {
  position: absolute;
  top: -0px;
  right: 0px;
  width: 90px;
  height: auto;
  z-index: 10;
}

.cours {
  position: absolute;
  top: -30px;
  right: 250px;
  width: 100px;
  height: 100px;
  z-index: 10;
  color: azure;
}
</style>