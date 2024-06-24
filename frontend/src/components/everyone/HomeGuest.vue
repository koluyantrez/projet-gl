<template>
  <TopGuest/>
  <div class="container">
    <div class="place">
      <ItemSearch/>
      <ItemCoursGuest v-for="(item,index) in itemc" :word="item.name" :key="index" @course-clicked="goToCourse"/>
      <!--ItemCours v-for="co in cours" :word="co.code+ +co.name" v-bind:key="co.id"/-->
    </div>
  </div>
</template>
<script>
import TopGuest from '../../elements/TopGuest.vue';
import ItemCoursGuest from '../../elements/ItemCoursGuest.vue';
import ItemSearch from '../../elements/ItemSearch.vue';
import axios from "axios";

//import Cours from '../../entites/Cours.java';

export default {
  components: {ItemCoursGuest, TopGuest, ItemSearch},
  data: () => {
    return {
      itemc: [],
    }
  },

  methods: {
    getCours() {
      axios.get('http://localhost:1937/cours/AllCours') // Remplacez par l'URL de votre API
          .then((response) => {
            this.itemc = response.data;
          })
          .catch((error) => {
            console.error(error);
          });
    },
    goToCourse(courseName) {
      const courseNameParts = courseName.split('-');
      const Name = courseNameParts[1];
      //this.$router.push(`cours/${Name}`);
    }
  },
  created() {
    this.getCours()
  }
}

</script>
<style scoped>
.container {
  position: absolute;
  width: 100%;
  height: 54rem;
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