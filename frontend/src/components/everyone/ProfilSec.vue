<template>

  <TopSec/>
  <Profil style="z-index: 99;"/>
  <div class="b">
    <ItemButton :name="cLang.Profile.edit"  @click="() => ToModPopup('buTriMod')"/>
    <ItemButton name="Photo" @click="() => ToPicPopup('buPic')"/>
    <ItemButton :name="cLang.Profile.pw" @click="() => ToPassPopup('buPass')"/>
    <router-link to="/">
      <ItemButton :name="cLang.Profile.logout"/>
    </router-link>

  </div>


  <DropImg v-if = "AddPic.buPic" :ToPicPopup="() => ToPicPopup('buPic')" />
  <ModifPro v-if = "popupMod.buTriMod" :ToModPopup="() => ToModPopup('buTriMod')" />
  <PassWord v-if = "pwMod.buPass" :ToPassPopup="() => ToPassPopup('buPass')" />
</template>
<script>
import TopSec from '../../elements/TopSec.vue';
import Profil from '../../elements/ProfilIns.vue';
import ItemButton from '../../elements/ItemButton.vue';
import ModifPro from '../../popup/ModifPro.vue';
import PassWord from '../../popup/PassWord.vue';
import DropImg from '../../popup/DropImg.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
//import Personnel from '../../model/Personnel.java';

export default{
  components: {TopSec,ItemButton,ModifPro,PassWord,DropImg,Profil},
  setup(){

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const uploadedFile = ref(null);
    const handleFileUpload = (event) => {
      const file = event.target.files[0];
      uploadedFile.value = URL.createObjectURL(file);
    };
    const AddPic = ref({
      buPic: false
    });
    const ToPicPopup = (tri)=>{
      AddPic.value[tri]=!AddPic.value[tri]
    }
    const popupMod = ref({
      buTriMod: false
    });
    const ToModPopup = (tri2)=>{
      popupMod.value[tri2]=!popupMod.value[tri2]
    }

    const pwMod = ref({
      buPass: false
    });
    const ToPassPopup = (tri3)=>{
      pwMod.value[tri3]=!pwMod.value[tri3]
    }

    return{

      cLang,

      popupMod,
      ToModPopup,
      pwMod,
      ToPassPopup,
      AddPic,
      ToPicPopup,
      uploadedFile, //update the database
      handleFileUpload,

      src: require('../../assets/profil.png'),
      id: 'Rosalie Marchal',
      mail: 'rosalie.marchal@uillumis.ka',
      matricule: '35624',
      loc: '55, rue des arbres d\'or 7501 Francorchamps',
      nu: '+26101870539',
      /* methods:{
  getAllData(){
      Personnel.getAllData().then((response)=>{this.cours=response.data})
  }
  },
  created(){
      this.getAllData()
  } */
    }
  }
}
</script>

<style scoped>

.b{
  position: absolute;
  left: 100px;
  bottom: 100px;
}

</style>

<style>

h1{
  color: rgb(158, 11, 23);
  font-family: Roboto,sans-serif;
  font-weight: 450;
  font-size: 100px;
}

</style>