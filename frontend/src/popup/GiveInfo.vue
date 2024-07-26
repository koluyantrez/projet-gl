<template>
  <div class="popup">
    <div class="inner">
      <slot></slot>
      <center>
        <img :src="pic" style="width: 7rem; height: 7rem;"/>
        <div class="info">
          <p>{{student.email }}</p>
          <p>{{student.name }}</p>
          <p>{{student.matricule }}</p>
          <p>{{student.adresse }}</p>
          <p>+{{student.numero }}</p>
        </div>
        <ItemAdd class="close" :word="cLang.pw.back" @click="ToShow()"/>
      </center>
    </div>
  </div>
</template>


<script>
import ItemAdd from '../elements/ItemAdd.vue';
import { useStore } from 'vuex';
import { computed, watch, ref, onMounted } from 'vue';
import fr from '../views/fr.js'
import en from '../views/en.js'
import Cookies from "js-cookie";
import axios from 'axios';

export default {
  components: { ItemAdd },
  props: ['ToShow','student'],

  data() {
    const pic = ref(null);
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    const namePer = Cookies.get('selectedPersonName');
    console.log(namePer);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const getPic = () => {
        pic.value = `data:image/jpeg;base64,${this.student.image}`
    }

    onMounted(() => {
        getPic();
    });

    return {
      cLang,
      pic
    }
  },
  created() {
    const namePer = Cookies.get('selectedPersonName');
    if (namePer) {
      this.getPersonnelInfo(namePer);
    }
  }
}
</script>

<style scoped>
.popup {
  position: fixed;
  top: 0px;
  bottom: 0px;
  left: 0px;
  right: 0px;
  z-index: 99;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.inner {
  position: relative;
  background: rgb(255, 255, 255);
  padding: 39px;
  border-radius: 10%;
}

.info {
  position: relative;
  color: rgb(158, 11, 23);
  font-family: Roboto,sans-serif;
}
</style>
