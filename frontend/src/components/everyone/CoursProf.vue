<template>
  <TopProf/>
  <div class="add"><ItemAdd :word="cLang.Courses.edit" @click="() => ToRenamePopup('buRename')"/></div>
  <div class="w">
    <h1>{{name}}</h1> <!--co.code+co.name-->
  </div>
  <div class="info">
    <h2>{{ cLang.Courses.prof }} : {{ prof }}</h2> <!--cours.prof-->
    <h3>{{ cLang.Courses.assi }}<li v-for="(item, index) in assi" :key="index">{{ item.a }}</li></h3>
    <!--<li v-for="co in cours" v-bind:key="co.assistant">{{ co.assistant }}</li>-->
  </div>
  <RenameCo v-if = "NameCours.buRename" :ToRenamePopup="() => ToRenamePopup('buRename')" />
</template>
<script>
import TopProf from '../../elements/TopProf.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import RenameCo from '../../popup/RenameCo.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
//import Cours from '../../model/Cours.java';

export default{
  components: {TopProf,ItemAdd,RenameCo},
  setup() {

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const NameCours = ref({
      buRename: false
    });
    const ToRenamePopup = (tri)=>{
      NameCours.value[tri]=!NameCours.value[tri]
    }



    return{
      NameCours,
      ToRenamePopup,
      name: 'S-INFO-810 Quantum Computing',
      prof: 'Seweryn Dynerowicz',
      assi: [
        {a: 'Julien Ladeuze'},
        {a: 'Maxime Bazin'},
        {a: 'Kacem Barkani'}
      ],
      cLang,

    }
  }
}
</script>
<style scoped>
.w{
  position: absolute;
  left: 110px;
  top: 70px;
}

.info{
  position: absolute;
  left: 120px;
  top: 225px;
  color: rgb(158, 11, 23);
  font-family: Roboto,sans-serif;
  font-size: 43px;
}
.add{
  position: absolute;
  top: 150px;
  right: 100px;
}

</style>
<style>
h1{
  color: rgb(158, 11, 23);
  font-family: Roboto,sans-serif;
  font-weight: 450;
  font-size: 90px;
}
h2{
  color: rgb(158, 11, 23);
  font-family: Roboto,sans-serif;
  font-weight: 450;
  font-size: 30px;
}
h3{
  color: rgb(158, 11, 23);
  font-family: Roboto,sans-serif;
  font-weight: 300;
  font-size: 30px;
}
</style>