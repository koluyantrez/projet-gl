<template>
  <div id="rectangle" class="redtop">
    <LogoHome :src="src2"/>
    <img class="la" alt="Change the language" src="../assets/lang.png" @click="switchLang"/>
  </div>
</template>


<script>
import LogoHome from './LogoHome.vue';
import { useStore } from 'vuex';
import { computed, ref } from 'vue';
import fr from '../views/fr.js';
import en from '../views/en.js';



export default {
  components: {LogoHome},
  setup() {

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    const switchLang = () => {
        if (idLa.value === 'fr') {
            store.commit('setLang', 'en');
        }
        else if (idLa.value === 'en') {
            store.commit('setLang', 'fr');
        }
    };


    return {
      switchLang,
      cLang,
      src1: require('../assets/profil.png'),
      src2: require('../assets/illumis.png'),
    }
  },
  }
</script>

<style scoped>
.redtop {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100px;
  background-color: rgb(158, 11, 23);
  z-index: 90;
}

.la{
  position: absolute;
  top: 1rem;
  left: 13rem;
  width: 4rem; 
  height: auto;
  z-index: 91;
}

.bg {
  position: absolute;
  top: 10;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: require('../assets/bgHome.png');
  z-index: 99;
}
</style>

<style>
body{
    background-image: url('../assets/bgHome.png');
}
</style>

