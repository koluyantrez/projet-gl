<template>
  <div>
    <TopStudentPAE/>
    <div class="content">
      <h2>{{cLang.PAE.cur}}</h2>
      <ul>
        <li v-for="course in currentPAE.courses" :key="course.id">
          <ItemCours :word="course.name" :credits="course.credits"/>
        </li>
      </ul>
      <p>{{cLang.PAE.tot}}: {{ totalCredits }}</p>
      <router-link to="/PAERequestPage">
        <ItemButton :name="cLang.PAE.rq" @click="navigateToPAERequest"/>
      </router-link>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import TopStudentPAE from "@/elements/PAE/TopStudentPAE.vue";
import ItemButton from "@/elements/ItemButton.vue";
import ItemCours from "@/elements/ItemStudent.vue";
import Cookies from "js-cookie";
import {computed, ref, watch} from "vue";
import { useStore } from 'vuex';
import fr from '../../views/fr';
import en from '../../views/en';

export default {
  components: {ItemButton, TopStudentPAE, ItemCours },
  setup() {
    const matricule = ref(Cookies.get('matriculeStudent'));
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
  data() {
    return {
      currentPAE: {
        courses: []
      }
    };
  },
  computed: {
    totalCredits() {
      return (this.currentPAE.courses || []).reduce((sum, course) => sum + course.credits, 0);
    }
  },
  methods: {
    fetchCurrentPAE() {
      const matricule = Cookies.get('matriculeStudent');
      axios.get(`http://localhost:1937/api/students/${matricule}/actuel-pae`)
          .then(response => {
            this.currentPAE = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    },
    navigateToPAERequest() {
      this.$router.push({ name: 'PAERequestPage' });
    }
  },
  mounted() {
    this.fetchCurrentPAE();
  }
};
</script>

<style scoped>
.content {
  margin-top: 100px;
  position: relative;
  z-index: 1;
}
</style>