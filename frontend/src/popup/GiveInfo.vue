<template>
  <div class="popup">
    <div class="inner">
      <slot></slot>
      <center>
        <img :src="pic" style="width: 7rem; height: 7rem;"/>
        <div class="info">
          <p>{{email }}</p>
          <p>{{name }}</p>
          <p>{{matricule }}</p>
          <p>{{adresse }}</p>
          <p>{{numero }}</p>
        </div>
        <router-link to="/ins">
          <ItemAdd :word="cLang.pw.back" @click="closePopup"/>
        </router-link>
      </center>
    </div>
  </div>
</template>

<script>
import ItemAdd from '../elements/ItemAdd.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../views/fr.js'
import en from '../views/en.js'
import Cookies from "js-cookie";
import axios from 'axios';

export default {
  components: { ItemAdd },
  props: ['toShow'],

  data() {
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);
    const namePer = Cookies.get('selectedPersonName');
    console.log(namePer);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    return {
      cLang,
      src: require('../assets/profil.png'),
      name : '',
      email: '',
      matricule: '',
      adresse: '',
      numero: '',
      pic: null,
    }
  },
  methods: {
    closePopup() {
      this.toShow('buShow');
    },
    getPersonnelInfo(name) {
      axios.get(`http://localhost:1937/api/personnel/name/${name}`)
          .then(response => {
            const data = response.data;
            this.email = data.email;
            this.matricule = data.matricule;
            this.adresse = data.adresse;
            this.numero = data.numero;
            this.name = data.name;
            this.pic = `data:image/jpeg;base64,${data.image}`;
            console.log(data);
          })
          .catch(error => {
            console.error('Error fetching personnel info:', error);
          });
    }
  },
  created() {
    const namePer = Cookies.get('selectedPersonName');
    if (namePer) {
      this.getPersonnelInfo(namePer);
    }
  }
}
</script>

<style scoped>
.popup {
  position: fixed;
  top: 0px;
  bottom: 0px;
  left: 0px;
  right: 0px;
  z-index: 99;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.inner {
  position: relative;
  background: rgb(255, 255, 255);
  padding: 39px;
  border-radius: 10%;
}

.info {
  position: relative;
  color: rgb(158, 11, 23);
  font-family: Roboto,sans-serif;
}
</style>