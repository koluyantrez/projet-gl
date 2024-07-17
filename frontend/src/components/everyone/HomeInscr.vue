<template>
  <TopSec/>
  <div class="container">
    <div class="place">
      <ItemAdd :word="cLang.Top.addel" @click="() => ToCreatePopup('buCreate')"/>
      <ItemSearch/>
      <ItemCours v-for="(student, index) in students" :word="student.name" :key="index"/>
    </div>
  </div>
  <AddStudent v-if="popupCreate.buCreate" :ToCreatePopup="() => ToCreatePopup('buCreate') "/>
</template>

<script>
import TopSec from '../../elements/TopSec.vue';
import ItemCours from '../../elements/ItemStudent.vue';
import ItemSearch from '../../elements/ItemSearch.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import AddCours from '../../popup/AddCours.vue';
import { useStore } from 'vuex';
import { computed, watch, ref, onMounted } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import axios from "axios";
import AddStudent from "@/popup/AddStudent.vue";

export default {
  components: {
    AddStudent,
    ItemCours,
    TopSec,
    ItemSearch,
    ItemAdd,
    AddCours
  },
  setup() {
    const students = ref([]);

    const getStudents = () => {
      axios.get('http://localhost:1937/students/getAll')
          .then((response) => {
            students.value = response.data;
          })
          .catch((error) => {
            console.error(error);
          });
    };

    onMounted(getStudents);

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const popupCreate = ref({
      buCreate: false
    });

    const ToCreatePopup = (tri1) => {
      popupCreate.value[tri1] = !popupCreate.value[tri1];
      if (!popupCreate.value[tri1]) {
        getStudents();
      }
    };

    return {
      students,
      popupCreate,
      ToCreatePopup,
      cLang
    };
  }
}
</script>

<style>
.container {
  position: absolute;
  width: 99%;
  height: 89%;
  top: 100px;
  overflow: auto;
}

.place {
  position: absolute;
  top: 5%;
  left: 10%;
}

.pic{
  position: absolute;
  top: -0px;
  right: 0px;
  width: 90px;
  height: auto;
  z-index: 10;
}

.cours{
  position: absolute;
  top: -30px;
  right: 250px;
  width: 100px;
  height: 100px;
  z-index: 10;
  color: azure;
}
</style>