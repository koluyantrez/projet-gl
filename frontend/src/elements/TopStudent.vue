<template>
    <MoodleTop/>
    <img class="la" alt="Change the language" src="../assets/lang.png" @click="switchLang"/>

    <router-link to="/student/profil">
        <img class="pic" :src="pic"/>
    </router-link>

    <div class="te">
      <router-link to="/student">
        <div class="cours">{{cLang.Top.course}}</div>
      </router-link>
      <div class="loc">{{cLang.Top.loc}}</div>
      <div class="bu">Bulletin</div>
      <div class="dospii">DOSPII</div>
    </div>
  
</template>
<script>
import MoodleTop from './MoodleTop.vue';
import { useStore } from 'vuex';
import { computed, watch, ref, onMounted } from 'vue';
import fr from '../views/fr.js';
import en from '../views/en.js';
import axios from 'axios';
import Cookies from 'js-cookie';

export default {
  components: { MoodleTop },

  setup() {
    const matricule = ref(Cookies.get('matriculeStudent'));
    const pic = ref(null);

    const getPic = () => {
      axios.get('http://localhost:1937/students/findById', {
        params: { matricule: matricule.value }
      })
      .then(response => {
        const info = response.data;
        pic.value = `data:image/jpeg;base64,${info.image}`;
      })
      .catch(error => {
        console.error(error);
      });
    };

    onMounted(() => {
      getPic();
    });

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);
    const switchLang = () => {
      if (idLa.value === 'fr') {
        store.commit('setLang', 'en');
      } else if (idLa.value === 'en') {
        store.commit('setLang', 'fr');
      }
    };

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    return {
      switchLang,
      cLang,
      pic
    };
  }
};
</script>

<style scoped>

.la{
  position: absolute;
  top: 1rem;
  left: 13rem;
  width: 4rem; 
  height: auto;
  z-index: 91;
}

.pic{
  position: absolute; 
  top: 0.5rem;
  right: 1rem;
  width: 5rem;
  height: 5rem;
  z-index: 92;
}

.te{
  position: relative;
  top: 0rem;
  font-family: Roboto, sans-serif;
  font-size: 60px;
  color: azure;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  z-index: 92;
}

.cours,
.loc,
.bu,
.dospii {
  position: absolute;
  color:azure;
}

.cours{
  right: 20rem;
  width: 5rem; 
  height: auto;
}

.loc{
  right: 32rem;
  width: 6rem; 
  height: auto;
}

.bu{
  right: 48rem;
  width: 5rem; 
  height: auto;
}

.dospii{
  right: 62rem;
  width: 5rem; 
  height: auto;
}




</style> 
