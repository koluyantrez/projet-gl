<template>
    <MoodleTop/>
    
    
    <div class="te">
    <div class="pic">
      <router-link to="/prof/profil">
        <ProfilPhoto :src="pp"/>
      </router-link>
    </div>

      <router-link to="/prof">
    <div class="cours"><TextTravel :word="cLang.Top.course"/></div>
     </router-link>
    <div class="loc"><TextTravel :word="cLang.Top.loc"/></div>
    <div class="dospii"><TextTravel word="DOSPII"/></div>
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

        return {
            switchLang,
            cLang,
            pp: require('../assets/profil.png'),
        };
    }
  }
</script>

<style scoped>


.pic{
  position: absolute; 
  top: 0rem; 
  right: 0rem;
  width: 100rem; 
  height: auto;
  z-index: 91;
}
.cours{
  position: fixed; 
  top: -2rem; 
  right: 18rem; 
  width: 5rem; 
  height: auto;
  z-index: 91;
  color:azure;
}

.loc{
  position: absolute; 
  top: -2rem;
  right: 29rem; 
  width: 6rem; 
  height: auto;
  z-index: 91;
}


.dospii{
  position: absolute; 
  top: -2rem; 
  right: 42rem; 
  width: 5rem; 
  height: auto;
  z-index: 91;
}

.te{
  font-family: Roboto, sans-serif;
  color:azure;
}
</style> 