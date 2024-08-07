<template>
  <TopGuest/>
  <div class="container">
    <div class="place">
      <ItemSearch @search="filterCourses"/>
      <!--ItemCoursGuest v-for="(item,index) in itemc" :word="item.name" :key="index" @course-clicked="goToCourse"/-->
      <ItemCours v-for="(item, index) in filteredCourses" :word="item.name" :key="index" @course-clicked="goToCourse"/>
    </div>
  </div>
</template>
<script>

import TopGuest from '../../elements/TopGuest.vue';
import ItemCoursGuest from '../../elements/ItemCoursGuest.vue';

import ItemCours from '../../elements/ItemCours.vue';
import axios from 'axios';
import ItemSearch from "@/elements/ItemSearch.vue";

export default {
  components: {ItemSearch, ItemCoursGuest, TopGuest,  ItemCours},
  data: () => {
    return {
      itemc: [],
      filteredCourses: []
    }
  },

  methods: {
    getCours() {
      axios.get('http://localhost:1937/api/cours/All')
          .then((response) => {
            this.itemc = response.data;
            this.filteredCourses = this.itemc;
          })
          .catch((error) => {
            console.error(error);
          });
    },
    goToCourse(courseName) {
      this.$router.push({ name: 'courseSection', params: { cours: courseName } });
    },
    filterCourses(query) {
      this.filteredCourses = this.itemc.filter(course =>
          course.name.toLowerCase().includes(query.toLowerCase())
      );
    }
  },
  created() {
    this.getCours();
  }
}

</script>
<style scoped>
.container {
  position: relative;
  width: 100%;
  height: calc(100vh - 100px);
  top: 100px;
  overflow: auto;
  /*border: 3px solid rgb(6, 148, 37); /* Bordure de la zone conteneur */
}

.place {
  position: absolute;
  top: 5%;
  left: 0;
}

.item-cours {
  width: 100%;
  margin-bottom: 20px;
}


</style>
