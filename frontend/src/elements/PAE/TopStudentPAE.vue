<template>
  <MoodleTop/>
  <div class="te">

    <div class="pic">
      <router-link to="/student/profil">
        <img class="pic" :src="pic"/>
      </router-link>
    </div>

    <router-link to="/student/PAE">
      <div class="gp">PAE</div>
    </router-link>

    <router-link to="/student/PAE">
      <div class="his">{{cLang.PAE.his}}</div>
    </router-link>

    <router-link to="/student/PAE/actual">
      <div class="act">{{cLang.PAE.act}}</div>
    </router-link >
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
  top: 0.5rem;
  right: 2rem;
  left : 20rem;
  width: 5rem;
  height: auto;
  z-index: 91;
  color:black;
}

.te .act {
  position: fixed;
  top: 0.5rem;
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