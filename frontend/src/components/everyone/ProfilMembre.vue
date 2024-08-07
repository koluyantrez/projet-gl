<template>
  <TopProf/>
  <Profil/>
  <div class="b">
    <ItemButton :name="cLang.Profile.edit"  @click="() => ToModPopup('buTriMod')"/>
    <ItemButton name="Photo" @click="() => ToPicPopup('buPic')"/>
    <ItemButton :name="cLang.Profile.pw" @click="() => ToPassPopup('buPass')"/>
    <router-link to="/">
      <ItemButton :name="cLang.Profile.logout"/>
    </router-link>
    <ItemButton :name="cLang.Profile.unsi" @click="() => ToUnsubPopup('buTriUnsub')" />
  </div>

  <YesOrNo v-if = "popupUnsub.buTriUnsub" :ToUnsubPopup="() => ToUnsubPopup('buTriUnsub')" />
  <ModifPro v-if = "popupMod.buTriMod" :ToModPopup="() => ToModPopup('buTriMod')" />
  <DropImg v-if = "AddPic.buPic" :ToPicPopup="() => ToPicPopup('buPic')" />
  <PassWord v-if = "pwMod.buPass" :ToPassPopup="() => ToPassPopup('buPass')" />
</template>
<script>
import TopProf from '../../elements/TopProf.vue';
import Profil from '../../elements/Profil.vue';
import ItemButton from '../../elements/ItemButton.vue';
import YesOrNo from '../../popup/YesOrNo.vue';
import PassWord from '../../popup/PassWord.vue';
import DropImg from '../../popup/DropImg.vue';
import ModifPro from '../../popup/ModifPro.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
//import Personnel from '../../model/Personnel.java';

export default{
  components: {TopProf,Profil,ItemButton,YesOrNo,ModifPro,PassWord,DropImg},
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
    const popupUnsub = ref({
      buTriUnsub: false
    });
    const ToUnsubPopup = (tri1)=>{
      popupUnsub.value[tri1]=!popupUnsub.value[tri1]
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
      popupUnsub,
      ToUnsubPopup,
      popupMod,
      ToModPopup,
      pwMod,
      ToPassPopup,
      AddPic,
      ToPicPopup,
      uploadedFile, //update the database
      handleFileUpload,

      cLang,

      src: require('../../assets/profil.png'),
      id: 'Rosalie Marchal',
      mail: 'rosalie.marchal@uillumis.ka',
      matricule: '35624',
      loc: '55, rue des arbres d\'or 7501 Francorchamps',
      nu: '+26101870539',

    }
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

</script>

<style scoped>
.w{
  position: fixed;
  left: 110px;
  top: 70px;
}

.b{
  position: fixed;
  left: 100px;
  bottom: 100px;
}

.i{
  position: fixed;
  left: 90px;
  bottom: 350px;
  width: 300px;
  height: 300px;
}

.info{
  position: fixed;
  left: 530px;
  bottom: 300px;
  color: rgb(158, 11, 23);
  font-family: Roboto,sans-serif;
  font-size: 43px;
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