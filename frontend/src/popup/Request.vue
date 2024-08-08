<template>
  <div class="popup">
    <div class="inner">
      <h3>Request Details</h3>
      <p><strong>{{ cLang.SignUp.ln }} :</strong> {{ request.name }}</p>
      <p><strong>{{ cLang.SignUp.fn }} :</strong> {{ request.firstName }}</p>
      <p><strong>{{ cLang.SignUp.born }} :</strong> {{ request.birthDate }}</p>
      <p><strong>{{ cLang.SignUp.ad }} :</strong> {{ request.address }}</p>
      <p><strong>{{ cLang.SignUp.city }} :</strong> {{ request.city }}</p>
      <p><strong>{{ cLang.SignUp.phone }} :</strong> {{ request.phone }}</p>
      <p><strong>{{ cLang.SignUp.section }} :</strong> {{ request.filial }}</p>
      <ItemAdd :word="cLang.AddCours.ok" @click="acceptRequest"/>
      <ItemAdd :word="cLang.AddCours.not" @click="rejectRequest"/>
      <ItemAdd :word="cLang.AddCours.bye" @click="closePopup"/>
    </div>
  </div>
</template>


      <p><strong>Name:</strong> {{ cLang.SignUp.ln }}</p>
      <p><strong>First Name:</strong> {{ cLang.SignUp.fn }}</p>
      <p><strong>Birth Date:</strong> {{ cLang.SignUp.born }}</p>
      <p><strong>Address:</strong> {{ cLang.SignUp.ad }}</p>
      <p><strong>City:</strong> {{ cLang.SignUp.city }}</p>
      <p><strong>Phone:</strong> {{ cLang.SignUp.phone }}</p>
      <p><strong>Filial:</strong> {{ request.filial }}</p>


<script>
import ItemAdd from "@/elements/ItemAdd.vue";
import axios from "axios";
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../views/fr.js'
import en from '../views/en.js';

export default {
  components: {ItemAdd},
  props: {
    request: Object,
    show: Boolean
  },
  setup() {
      const store = useStore();
      const idLa = computed(() => store.state.lang.curLang);
      const cLang = ref(idLa.value === 'fr' ? fr : en);

      watch(idLa, (newLang) => {
        cLang.value = newLang === 'fr' ? fr : en;
      });

      return {
        cLang
      };
    },
  methods: {
    acceptRequest() {
      axios.post(`http://localhost:1937/secretariat/approve/${this.request.id}`)
          .then(() => {
            this.$emit('accept', this.request);
          })
          .catch((error) => {
            console.error(error);
          });
    },
    rejectRequest() {
      axios.post(`http://localhost:1937/secretariat/reject/${this.request.id}`)
          .then(() => {
            this.$emit('reject', this.request);
          })
          .catch((error) => {
            console.error(error);
          });    },
    closePopup() {
      this.$emit('close');
    }
  }
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
  font-family: 'Arial', sans-serif;
}


</style>