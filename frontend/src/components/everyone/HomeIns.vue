<template>
  <TopSec/>
  <div class="container">
  <ItemSearch class="look"/>
    <div class="place">

      <div v-for="(person, index) in personnel" :key="index">
        <ItemAdd :word="cLang.ServeIns.info" @click="() => ToShow('buShow', person)"/>
        <ItemAdd :word="cLang.ServeIns.rm" @click="() => ToUnsubPopup('buTriUnsub', person)"/>
        {{ person.name }}
      </div>
    </div>
  </div>

  <YesOrNo v-if="popupUnsub.buTriUnsub" :ToUnsubPopup="() => ToUnsubPopup('buTriUnsub')" />
  <GiveInfo v-if="popupShow.buShow" :toShow="toShow" /> <!-- Passer la méthode ToShow sans la fonction fléchée -->

</template>

<script>
import YesOrNo from '../../popup/YesOrNo.vue';
import GiveInfo from '../../popup/GiveInfo.vue';
import TopSec from '../../elements/TopSec.vue';
import ItemSearch from '../../elements/ItemSearch.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import axios from 'axios';
import Cookies from 'js-cookie';

export default {
  components: { TopSec, ItemSearch, ItemAdd, YesOrNo, GiveInfo },
  data() {
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const popupUnsub = ref({
      buTriUnsub: false
    });
    const ToUnsubPopup = (tri1, person) => {
      popupUnsub.value[tri1] = !popupUnsub.value[tri1];
      // Stocker le nom du personnel dans les cookies
      Cookies.set('selectedPersonName', person.name);
    };

    const popupShow = ref({
      buShow: false
    });
    const ToShow = (tri2, person) => {
      popupShow.value[tri2] = !popupShow.value[tri2];
      Cookies.set('selectedPersonName', person.name);
    };
    const toShow = (tri2) => {
      popupShow.value[tri2] = !popupShow.value[tri2];
    }

    return {
      popupUnsub,
      ToUnsubPopup,
      popupShow,
      ToShow,
      toShow,
      cLang,
      personnel: [],// Initialiser la liste du personnel à vide
    };
  },
  methods: {
    inputData() {
      this.$router.push('/ins/signup');
    },
    getAllPersonnel() {
      axios.get('http://localhost:1937/api/student/all')
          .then(response => {
            this.personnel = response.data; // Mettre à jour la liste du personnel avec ceux récupérés depuis le backend
          })
          .catch(error => {
            console.error('Error fetching personnel:', error);
          });
    },
    storePersonName(person) {
      // Stocker le nom du personnel dans les cookies lorsqu'on clique sur le bouton "Profil"
      Cookies.set('selectedPersonName', person.name);
      // Rediriger vers la page du profil du personnel
      this.$router.push('/profil');
    }
  },
  created() {
    this.getAllPersonnel(); // Appel de la méthode pour récupérer la liste du personnel depuis le backend
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
  top: 5rem;
  left: 10rem;
  overflow: auto;
  font-family: Roboto, sans-serif;
}

.look {
  position: absolute;
  top: 4%;
  right: 15%;
}
</style>
