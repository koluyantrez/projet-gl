<template>
  <MoodleTop/>

  <div class="te">
    <div class="pic">
      <router-link to="/prof/profil">
        <img class="pic" :src="pic"/>
      </router-link>
    </div>

    <router-link to="/prof">
      <div class="cours">{{cLang.Top.course}}</div>
    </router-link>
    <div class="loc">{{cLang.Top.loc}}</div>


    <router-link to="/dospii">
      <div class="dospii">DOSPII</div>
    </router-link>

    <router-link to="/roomReservationRequest">
      <div class="roomReservation">Demande Reservation</div>
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

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const matricule = ref(Cookies.get('matriculeProfesseur'));
    const pic = ref(null);

    const getPic = () => {
      axios.get(`http://localhost:1937/api/professeurs/${matricule.value}`)
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


.pic{
  position: absolute;
  top: 0.3rem;
  right: 0.4rem;
  width: 5rem;
  height: 5rem;
  z-index: 92;
}

.te .cours{
  position: absolute;
  right: 20rem;
  width: 5rem;
  height: auto;
  z-index: 91;
  color:azure;
}

.te .loc{
  position: absolute;
  right: 32rem;
  width: 6rem;
  height: auto;
  z-index: 91;
}


.te .dospii{
  position: absolute;
  color: azure;
  right: 47rem;
  width: 5rem;
  height: auto;
  z-index: 91;
}

.te .roomReservation{
  position: absolute;
  color: azure;
  right: 60rem;
  font-size: 30px;
  width: 5rem;
  height: auto;
  z-index: 91;
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
}

</style>