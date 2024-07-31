<template>
  <TopGuest/>
  <div class="container">
    <ItemSearch class="is"/>
    <div class="place">
      
      <!--ItemCoursGuest v-for="(item,index) in itemc" :word="item.name" :key="index" @course-clicked="goToCourse"/-->
      <ItemCours v-for="(item, index) in filteredCourses" :word="item.name" :key="index" @course-clicked="goToCourse"/>
    </div>
  </div>
</template>
<script>
import TopGuest from '../../elements/TopGuest.vue';
import ItemCoursGuest from '../../elements/ItemCoursGuest.vue';
import ItemSearch from '../../elements/ItemSearch.vue';
import ItemCours from '../../elements/ItemCours.vue';
import axios from 'axios';

export default {
  components: {ItemCoursGuest, TopGuest, ItemSearch, ItemCours},
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
    position: absolute;
    width: 100rem; 
    height: 54rem; 
    bottom: 0.1rem; 
    /*border: 3px solid rgb(6, 148, 37); /* Bordure de la zone conteneur */
}

.place {
    position: absolute;
    top: 7rem;
    left: 10rem;
}

.is{
  position: absolute;
  top: 2rem;
  right: 9rem;
}
</style>