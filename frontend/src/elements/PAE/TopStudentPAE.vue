<template>
  <MoodleTop/>
  <div class="te">

    <div class="pic">
      <router-link to="/student/profil">
        <img class="pic" :src="pp"/>
      </router-link>
    </div>

    <router-link to="/student/PAE">
      <div class="gp">PAE</div>
    </router-link>

    <div class="his">{{cLang.PAE.his}}</div>

    <router-link to="/student/PAE/actual">
      <div class="act">{{cLang.PAE.act}}</div>
    </router-link >
  </div>

</template>
<script>
import MoodleTop from '../MoodleTop.vue';
import ProfilPhoto from '../ProfilPhoto.vue';
import { useStore } from 'vuex';
import {computed, watch, ref, onMounted} from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import Cookies from "js-cookie";
import axios from "axios";
export default {
  components: { MoodleTop, ProfilPhoto },
  setup() {

    const matricule = ref(Cookies.get('matriculeStudent'));
    const pic = ref(null);

    const getPic = () => {
      axios.get('http://localhost:1937/api/students/', {
        params: { matricule: matricule.value }
      })
          .then(response => {
            console.log(response.data);
            const info = response.data;
            pic.value = `data:image/jpeg;base64,${info.image}`;
          })
          .catch(error => {
            console.error(error);
          });
    };

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

    onMounted(() => {
      getPic();
    });

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });
    console.log(cLang.value);
    return {
      switchLang,
      cLang,
      pic,
      pp: require('../../assets/profil.png'),
    };
  }
}
</script>

<style scoped>

.la{
  position: fixed;
  top: 1rem;
  left: 13rem;
  width: 4rem;
  height: auto;
  z-index: 91;
}

.pic{
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  width: 75px;
  height: 75px;
  z-index: 98;
}


.gp{
  position: fixed;
  top: 0rem;
  right: 2rem;
  left : 20rem;
  width: 5rem;
  height: auto;
  z-index: 91;
  color:black;
}

.te .act {
  position: fixed;
  top: 0;
  right: 19rem;
  width: 5rem;
  height: auto;
  z-index: 91;
  color: azure;
}

.te .his {
  position: absolute;
  top: 0;
  right: 39rem;
  width: 6rem;
  height: auto;
  z-index: 99;
  color: azure;
}

.te {
  position: relative;
  top: 0;
  font-family: Roboto, sans-serif;
  font-size: 60px;
  color: azure;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}


</style>
