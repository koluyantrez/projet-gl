<template>
  <MoodleTop/>
  <img class="la" alt="Change the language" src="../assets/lang.png" @click="switchLang"/>
  <div class="pic">
    <router-link to="/">
      <ProfilPhoto class="pic" :src="pp"/>
    </router-link>
  </div>
  <div class="te">
    <router-link to="/guest/signup">
      <p class="ins" >{{cLang.Top.signin}}</p>
    </router-link>
    <router-link to="/guest">
      <p class="cours">{{cLang.Top.course}}</p>
    </router-link>
    <router-link to="/dospii">
      <p class="dospii">DOSPII</p>
    </router-link>
  </div>
</template>
<script>
import MoodleTop from './MoodleTop.vue';
import ProfilPhoto from './ProfilPhoto.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../views/fr.js';
import en from '../views/en.js';
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
      pp: require('../assets/profil.png')
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

.te .ins{
  position: absolute;
  right: 31rem;
  width: 15rem;
  height: auto;
  z-index: 91;
  color:azure;
}

.te .cours{
  position: absolute;
  right: 21rem;
  width: 5rem;
  height: auto;
  z-index: 91;
  color:azure;
}

.te .dospii{
  position: absolute;
  right: 53rem;
  width: 7rem;
  height: auto;
  z-index: 91;
}

.te{
  position: relative;
  top: -3rem;
  font-family: Roboto, sans-serif;
  font-size: 60px;
  color: azure;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.pic{
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  width: 100rem;
  height: auto;
  z-index: 98;
}

</style>