<template>
  <TopSec/>
  <div class="container">
    <div class="place">
      <ItemAdd word="inscription"/>
      <ItemAdd word="désinscription"/>
      <ItemAdd word="tous"/>
      <ItemSearch/>
      <ItemCours v-for="(student, index) in students" :word="student.name" :key="index" @click="() => showStudentInfo(student.name)"/>
    </div>
  </div>
  <GiveInfo v-if="popupShow.buShow" :student="selectedStudent" :ToShow="() => ToShow('buShow')" />

</template>

<script>
import TopSec from '../../elements/TopSec.vue';
import ItemCours from '../../elements/ItemStudent.vue';
import ItemSearch from '../../elements/ItemSearch.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import AddCours from '../../popup/AddCours.vue';
import GiveInfo from '../../popup/GiveInfo.vue';
import { useStore } from 'vuex';
import { computed, watch, ref, onMounted } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import axios from "axios";


export default {
  components: {
    ItemCours,
    TopSec,
    ItemSearch,
    ItemAdd,
    AddCours,
    GiveInfo
  },
  setup() {
    const students = ref([]);
    const selectedStudent = ref(null);

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

    const popupShow = ref({
        buShow: false
    });
    const ToShow = (tri2)=>{
        popupShow.value[tri2]=!popupShow.value[tri2]
    }

    const ToCreatePopup = (tri1) => {
      popupCreate.value[tri1] = !popupCreate.value[tri1];
      if (!popupCreate.value[tri1]) {
        getStudents();
      }
    };

    const showStudentInfo = async (name) => {
      try {
        const response = await axios.get(`http://localhost:1937/students/findByName?name=${name}`)
        selectedStudent.value = response.data;
        popupShow.value.buShow = true;
      } catch (error) {
        console.error('Error fetching student info:', error);
      }
    };

    onMounted(() => {
      getStudents();
    });

    return {
      students,
      popupCreate,
      ToCreatePopup,
      cLang,
      popupShow,
      ToShow,
      showStudentInfo,
      selectedStudent
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