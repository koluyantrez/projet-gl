<template>
  <TopSecretariat/>
  <div class="container">
  <ItemSearch class="s" @search="filterCourses" />
    <div class="place">
      <ItemAdd :word="cLang.Top.addco" @click="() => ToCreatePopup('buCreate')"/>
      <ItemAdd word="PAE" @click="goToPAERequests"/>
      <ItemCours v-for="(item,index) in filteredCourses" :word="item.name" :key="index" @show-details="showDetailsHandler"/>
    </div>
  </div>
  <AddCours v-if = "popupCreate.buCreate" :ToCreatePopup="() => ToCreatePopup('buCreate')" />
</template>
<script>
import axios from 'axios';
import Cookies from 'js-cookie';
import TopSecretariat from '../../elements/TopSecretariat.vue';
import ItemCours from '../../elements/ItemCours.vue';
import ItemSearch from '../../elements/ItemSearch.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import AddCours from '../../popup/AddCours.vue';
import { useStore } from 'vuex';
import {computed, watch, ref, onMounted} from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import ItemButton from "@/elements/ItemButton.vue";

;

export default {
  components: {ItemButton, ItemCours,TopSecretariat,ItemSearch,ItemAdd,AddCours},
  setup(){

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const popupCreate = ref({
      buCreate: false
    });
    const ToCreatePopup = (tri1)=>{
      popupCreate.value[tri1]=!popupCreate.value[tri1]
    };

    const itemc= ref( []);

    const filteredCourses = ref(itemc.value);

    const fetchCourses = async () =>{
      try{
        const response = await axios.get('http://localhost:1937/api/cours/All');
        itemc.value = response.data;
        filteredCourses.value = itemc.value;
      }
      catch (error){
        console.error('Error fetching courses', error);
      }
    };

    const filterCourses = (query) => {
      filteredCourses.value = itemc.value.filter(course =>
          course.name.toLowerCase().includes(query.toLowerCase())
      );
      console.log(filteredCourses.value);
    };

    onMounted(fetchCourses);


    return{
      popupCreate,
      ToCreatePopup,
      cLang,
      itemc,
      filteredCourses,
      filterCourses,
      name: '+cours',
    }
  },
 methods:{
     showDetailsHandler(courseName) {
       Cookies.set('selectedCourse', courseName);
       this.$router.push({name: 'DetailsCours'});
     },
     getCours(){
         Cours.getAllCours().then((response)=>{this.cours=response.data})
     },
   goToPAERequests() {
     this.$router.push('/pae-requests');
   }
 }

}

</script>
<style scoped>
.s{
    position: absolute;
    top: 5%;
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
  color:azure;
}
</style>