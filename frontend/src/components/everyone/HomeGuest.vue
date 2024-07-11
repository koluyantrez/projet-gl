<template>
  <TopGuest/>
  <div class="container">
    <div class="place">
      <ItemSearch/>
      <!--ItemCoursGuest v-for="(item,index) in itemc" :word="item.name" :key="index" @course-clicked="goToCourse"/-->
      <ItemCours v-for="(item, index) in itemc" :word="item.name" :key="index" @course-clicked="goToCourse"/>
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
    }
  },

  methods: {
    getCours() {
      axios.get('http://localhost:1937/cours/AllCours')
          .then((response) => {
            this.itemc = response.data;
          })
          .catch((error) => {
            console.error(error);
          });
    },
    goToCourse(courseName) {
      this.$router.push({ name: 'courseSection', params: { cours: courseName } });
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
  width: 100%;
  height: 56rem;
  bottom: 0.1rem;
  left: -0.42rem;
  overflow: auto;
  /* border: 3px solid rgb(6, 148, 37); *//* Bordure de la zone conteneur*/
}

.place {
  position: absolute;
  top: 1rem;
  left: 10rem;
}


</style>