<template>
    <MoodleTop/>
    <img class="la" alt="Change the language" src="../assets/lang.png" @click="switchLang"/>
    <div class="te">
      <router-link to="/guest/signup">
        <p class="ins" ><TextTravel :word="cLang.Top.signin"/></p>
      </router-link>
    <router-link to="/guest">
    <p class="cours"><TextTravel :word="cLang.Top.course" /></p>
    </router-link>
    <p class="dospii"><TextTravel word="DOSPII" /></p>
    </div>
</template>
<script>
  import MoodleTop from './MoodleTop.vue';
  import TextTravel from './TextTravel.vue';
  import { useStore } from 'vuex';
  import { computed, watch, ref } from 'vue';
  import fr from '../views/fr.js';
  import en from '../views/en.js';
  export default {
    components: { TextTravel, MoodleTop },
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

.ins{
  position: fixed; 
  top: -5rem; 
  right: 24rem; 
  width: 15rem; 
  height: auto;
  z-index: 91;
  color:azure;
}

.cours{
  position: fixed; 
  top: -5rem; 
  right: 18rem; 
  width: 5rem; 
  height: auto;
  z-index: 91;
  color:azure;
}

.dospii{
  position: fixed; 
  top: -5rem;
  right: 45rem; 
  width: 7rem; 
  height: auto;
  z-index: 91;
}

.te{
  font-family: Roboto, sans-serif;
  color:azure;
}

</style> 
