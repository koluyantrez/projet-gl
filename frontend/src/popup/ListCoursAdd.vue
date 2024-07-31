<template>
    <div class="popup">
        <div class="inner">
            <div class="course-list">
                <ul class="course-scroll">
                    <li v-for="(cours, index) in availableCourses" :key="index" class="course-item">
                        <div class="course-item-content">
                            <CirclePlus @click="addCourse(cours)"/>
                            <span>{{ cours }}</span>
                        </div>
                    </li>
                </ul>
            </div>
            <ItemAdd class="back" :word="cLang.pw.back" @click="ToShow()" />
        </div>
    </div>
</template>

<script>
import ItemAdd from '../elements/ItemAdd.vue';
import CirclePlus from '../elements/CirclePlus.vue';
import { useStore } from 'vuex';
import { computed, watch, ref, onMounted } from 'vue';
import fr from '../views/fr.js';
import en from '../views/en.js';
import axios from 'axios';
import Cookies from 'js-cookie';

export default {
  components: { ItemAdd, CirclePlus },
  props: ['ToShow'],

  data() {
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    return {
      cLang,
      availableCourses: [],
    };
  },
  methods: {
    showCourseSelection() {
      axios.get('http://localhost:1937/api/cours/All')
        .then(response => {
          const courses = response.data;
          this.availableCourses = courses.map(cours => cours.name);
        })
        .catch(error => {
          console.error(error);
        });
    },
    addCourse(cours) {
      const id = Cookies.get('matriculeProfesseur');
      const coursName = cours;

      axios.post(`http://localhost:1937/assign-course${id}`, null, {
        params: {
          coursName: coursName
        }
      })
      .then(response => {
        const result = response.data;
        if (result === 'attribute success') {
          this.getCourses();
          this.hideCourseSelection();
          alert("attribute success");
        }
      })
      .catch(error => {
        console.error(error);
      });
    },
  },
  created() {
    this.showCourseSelection();
  }
}
</script>

<style scoped>

.back{
    position: relative;
    bottom: -1rem;
    left: 8rem;
}

.popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 99;
}

.inner {
  position: relative;
  background: #fff;
  padding: 39px;
  border-radius: 10%;
}

.course-list {
  height: 16rem;
  width: 25rem;
  overflow-y: auto;
}

.course-scroll {
  padding: 0;
  margin: 0;
  color: #9e0b17;
}

.course-item {
  font-size: 20px;
  font-family: Roboto, sans-serif;
  color: #9e0b17;
}

.course-item-content {
  display: flex;
  align-items: center;
}

.course-item-content span {
  margin-left: 10px;
}
</style>
