<template>
  <div class="popup">
    <div class="inner">
      <slots></slots>
      <center>
        <input v-model="student.firstName" :placeholder="cLang.AddStudent.fn" />
        <input v-model="student.lastName" :placeholder="cLang.AddStudent.ln" />
        <input v-model="student.departement" :placeholder="cLang.AddStudent.dp" />
        <input v-model="student.filiere" :placeholder="cLang.AddStudent.fl" />
        <input v-model="student.numero" :placeholder="cLang.AddStudent.nu" />
        <input v-model="student.adresse" :placeholder="cLang.AddStudent.ad" />

        <ItemAdd :word="cLang.AddCours.ok" @click="createStudent" />
        <ItemAdd class="close" :word="cLang.AddCours.back" @click="ToCreatePopup()" />
      </center>
    </div>
  </div>
</template>

<script>
import ItemAdd from '../elements/ItemAdd.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../views/fr.js';
import en from '../views/en.js';
import axios from 'axios';

export default {
  components: { ItemAdd },
  props: ['ToCreatePopup', 'getStudents'],

  setup() {
    const student = ref({
      firstName: '',
      lastName: '',
      departement: '',
      adresse: '',
      filiere: '',
      numero: '',
    });

    const createStudent = () => {
      console.log('Student:', student.value);
      axios
          .post('http://localhost:1937/AddStudent', student.value, {
            headers: {
              'Content-Type': 'application/json',
            },
          })
          .then(() => {
            console.log('Student created');
            student.value = {
              firstName: '',
              lastName: '',
              departement: '',
              adresse: '',
              filiere: '',
              numero: '',
            };
          })
          .catch((error) => {
            console.error(error);
          });
    };

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    return {
      student,
      createStudent,
      cLang,
    };
  },
};
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

.po {
  font-size: 30px;
  color: rgb(134, 10, 10);
}
</style>