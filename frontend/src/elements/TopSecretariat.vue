<template>
  <MoodleTop/>
  <div class="te">
    <div class="pic">
      <router-link to="/sec/profil">
        <img class="pic" :src="pic"/>
      </router-link>
    </div>
    <router-link to="/sec">
      <div class="la">{{ cLang.Top.course }}</div>
    </router-link>
    <router-link to="/reservationList">
        <div class="roomReservation">{{ cLang.Top.askloc }}</div>
    </router-link>
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


        const matricule = ref(Cookies.get('loginUser'));
        const pic = ref(null);

        const getPic = () => {
          const userId = matricule.value;
          axios.get(`http://localhost:1937/users/${userId}`)
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
  top: 0.2rem;
  right: 0.3rem;
  width: 5rem;
  height: 5rem;
  z-index: 92;
}

.te .la {
  position: absolute;
  right: 20rem;
  width: 5rem;
  height: auto;
  z-index: 91;
  color: azure;
}

.te .loc {
  position: absolute;
  right: 40rem;
  width: 6rem;
  height: auto;
  z-index: 99;
  color: azure;
}

.te .roomReservation{
  position: absolute;
  color: azure;
  right: 32rem;
  font-size: 30px;
  width: 5rem;
  height: auto;
  z-index: 91;
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

