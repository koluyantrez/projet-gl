<template>
  <MoodleTop/>
  <img class="la" alt="Change the language" src="../../assets/lang.png" @click="switchLang"/>
  <div class="pic">
    <router-link to="/student/profil">
      <ProfilPhoto   :src="pp"/>
    </router-link>
  </div>

  <div class="te">
    <div class="gp">cLang.PAE.gp</div>
    <div class="his">cLang.PAE.his</div>
    <div class="act">cLang.PAE.act</div>
  </div>

</template>
<script>
import MoodleTop from '../MoodleTop.vue';
import ProfilPhoto from '../ProfilPhoto.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
export default {
  components: { MoodleTop, ProfilPhoto },
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

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });
    console.log(cLang.value);
    return {
      switchLang,
      cLang,
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
  top: 0rem;
  right: 0rem;
  width: 100rem;
  height: auto;
  z-index: 91;
}


.gp{
  position: fixed;
  top: -3.5rem;
  right: 2rem;
  left : 20rem;
  width: 5rem;
  height: auto;
  z-index: 91;
  color:black;
}

.his{
  position: fixed;
  top: -2rem;
  right: 22rem;
  width: 5rem;
  height: auto;
  z-index: 91;
  color:azure;
}

.act{
  position: absolute;
  top: -2rem;
  right: 32rem;
  width: 6rem;
  height: auto;
  z-index: 91;
}


.te{
  font-family: Roboto, sans-serif;
  color:azure;
}


</style>
