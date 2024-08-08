<template>
  <TopGuest/>
  <div class="container">
  <ItemSearch class="se" @search="filterCourses"/>
    <div class="place">
      <ItemCours v-for="(item, index) in filteredCourses" :word="item.name" :key="index" @show-details="showDetailsHandler"/>
    </div>
  </div>
</template>
<script>

import TopGuest from '../../elements/TopGuest.vue';
import ItemCours from '../../elements/ItemCours.vue';
import axios from 'axios';
import { ref } from 'vue';
import Cookies from 'js-cookie';
import ItemSearch from "@/elements/ItemSearch.vue";

export default {
  components: {ItemSearch, TopGuest,  ItemCours},
  data () {
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
    showDetailsHandler(courseName) {
      Cookies.set('selectedCourse', courseName);
      this.$router.push({name: 'DetailsCours'});
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

.se{
  position: absolute;
  top: 4%;
  right: 15%;
}

.container {
  position: absolute;
  width: 100%;
  height: 89%;
  top: 100px;
  /*border: 3px solid rgb(6, 148, 37); /* Bordure de la zone conteneur */
}

.place {
  position: absolute;
  top: 10%;
  left: 10%;
}


</style>