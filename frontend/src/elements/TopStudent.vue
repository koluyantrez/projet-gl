<template>
    <MoodleTop/>
    <img class="la" alt="Change the language" src="../assets/lang.png" @click="switchLang"/>
    <div class="pic">
      <router-link to="/student/profil">
        <ProfilPhoto   :src="pp"/>
      </router-link>
    </div>

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
  import ProfilPhoto from './ProfilPhoto.vue';
  import TextTravel from './TextTravel.vue';
  import { useStore } from 'vuex';
  import { computed, watch, ref } from 'vue';
  import fr from '../views/fr.js';
  import en from '../views/en.js';
  export default {
    components: { TextTravel, MoodleTop, ProfilPhoto },
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
            pp: require('../assets/profil.png'),
        };
    }
  }
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
  top: 0rem; 
  right: 0rem;
  width: 100rem; 
  height: auto;
  z-index: 91;
}

.te{
  position: relative;
  top: 1.5rem;
  font-family: Roboto, sans-serif;
  color:azure;
  z-index: 92;

}

.cours,
.loc,
.bu,
.dospii {
  position: absolute;
}

.cours{
  right: 18rem; 
  width: 5rem; 
  height: auto;
  color:azure;
}

.loc{
  right: 28rem; 
  width: 6rem; 
  height: auto;
}

.bu{
  right:41rem; 
  width: 5rem; 
  height: auto;
}

.dospii{
  right: 54rem; 
  width: 5rem; 
  height: auto;
}




</style> 
