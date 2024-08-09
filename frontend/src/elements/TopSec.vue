<template>
  <MoodleTop/>
  <div class="te">

    <div class="pic">
      <router-link to="/sec/profil">
        <img class="pic" :src="pic"/>
      </router-link>
    </div>
    <router-link to="/ins">
      <div class="la">{{ cLang.Top.stu }}</div>
    </router-link>
    <router-link to="/inscription">
      <div class="loc">{{ cLang.Top.askin }}</div>
    </router-link>
    <router-link to="/desinscription">
      <div class="bye">{{ cLang.Top.askout }}</div>
    </router-link>
    <div class="cours">{{ cLang.Top.loc }}</div>
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
        const store = useStore();
        const idLa = computed(() => store.state.lang.curLang);
        const cLang = ref(idLa.value === 'fr' ? fr : en);
        console.log(cLang.value);
        watch(idLa, (newLang) => {
            cLang.value = newLang === 'fr' ? fr : en;
        });


        const matricule = ref(Cookies.get('matriculeInscription'));
        const pic = ref(null);

        const getPic = () => {
          axios.get('http://localhost:1937/api/inscription/membreServiceInscription', {
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

        return {
            cLang,
            pic
        };
    }
  }
</script>

<style scoped>
.pic {
  position: absolute;
  top: 0.1rem;
  right: 0rem;
  width: 5rem;
  height: 5rem;
  z-index: 92;
}

.te .la {
  position: absolute;
  right: 56rem;
  width: 5rem;
  height: auto;
  z-index: 91;
  color: azure;
}

.te .cours {
  position: absolute;
  right: 19rem;
  width: 5rem;
  height: auto;
  z-index: 91;
  color: azure;
}

.te .loc {
  position: absolute;
  right: 39rem;
  width: 6rem;
  height: auto;
  z-index: 99;
  color: azure;
}

.te .bye {
  position: absolute;
  right: 80rem;
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

