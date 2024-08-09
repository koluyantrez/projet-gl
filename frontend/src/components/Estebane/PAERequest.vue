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
    <PAEDetailsPopup v-if="showPopup" :request="selectedRequest" @close="closeRequestPopup"/>
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

    const filterRequest = (query) => {
      filteredRequest.value = paeRequests.value.filter((request) => {
        return request.studentId.toString().includes(query);
      });
    };

    onMounted(() => {
      getPAERequests();
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

    return {
      cLang,
      paeRequests,
      filteredRequest,
      selectedRequest,
      showPopup,
      filterRequest,
      showRequestPopup,
      closeRequestPopup
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