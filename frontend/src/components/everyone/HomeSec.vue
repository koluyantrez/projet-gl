<template>
  <TopSec/>
  <div class="container">
    <div class="place">
      <ItemAdd :word="cLang.Top.addco" @click="() => ToCreatePopup('buCreate')"/>
      <ItemSearch/>
      <ItemCours v-for="(item,index) in itemc" :word="item.cours" :key="index"/>
      <!--ItemCours v-for="co in cours" :word="co.code+ +co.name" v-bind:key="co.id"/-->
    </div>
  </div>
  <AddCours v-if = "popupCreate.buCreate" :ToCreatePopup="() => ToCreatePopup('buCreate')" />
</template>
<script>
import TopSec from '../../elements/TopSec.vue';
import ItemCours from '../../elements/ItemCours.vue';
import ItemSearch from '../../elements/ItemSearch.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import AddCours from '../../popup/AddCours.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
//import Cours from '../../model/Cours.java';

export default {
  components: {ItemCours,TopSec,ItemSearch,ItemAdd,AddCours},
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
    }


    return{


      popupCreate,
      ToCreatePopup,
      cLang,

      itemc: [
        {cours : 'S-INFO-150 Introduction to Computer Science'},
        {cours : 'S-INFO-212 Data Structures and Algorithms'},
        {cours : 'S-INFO-245 Database Management Systems'},
        {cours : 'S-INFO-320 Web Development Technologies'},
        {cours : 'S-INFO-375 Software Engineering Principles'},
        {cours : 'S-INFO-410 Artificial Intelligence Fundamentals'},
        {cours : 'S-INFO-450 Machine Learning and Data Mining'},
        {cours : 'S-INFO-485 Cybersecurity and Information Assurance'},
        {cours : 'S-INFO-510 Advanced Database Systems'},
        {cours : 'S-INFO-545 Cloud Computing Technologies'},
        {cours : 'S-INFO-590 Big Data Analytics'},
        {cours : 'S-INFO-625 Internet of Things (IoT)'},
        {cours : 'S-INFO-660 Blockchain Fundamentals'},
        {cours : 'S-INFO-705 Computer Vision and Image Processing'},
        {cours : 'S-INFO-740 Natural Language Processing'},
        {cours : 'S-INFO-775 Reinforcement Learning'},
        {cours : 'S-INFO-810 Quantum Computing'},
        {cours : 'S-INFO-845 Biometrics and Security Systems'},
        {cours : 'S-INFO-880 Ethical Hacking and Penetration Testing'},
      ],
      name: '+cours',
    }
  }
  /*
 methods:{
     getCours(){
         Cours.getAllCours().then((response)=>{this.cours=response.data})
     }
 },
 created(){
     this.getAllCours()
 },

 data: () => {
     return{
         cours: []
     }
 }
  */
}

</script>
<style scoped>
.container {
  position: absolute;
  width: 100%;
  height: 89%;
  top: 100px;
  overflow: auto;
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