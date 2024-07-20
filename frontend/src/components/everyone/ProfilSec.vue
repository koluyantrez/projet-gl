<template>
  <TopSec/>
    <div class="info">
      <br>
      <p><h1>{{ insInfo.name }}</h1></p>
      <p>{{ insInfo.email }}</p>
      <p>{{ insInfo.adresse }}</p>
      <p>+{{ insInfo.numero }}</p>
    </div>
  <div class="b">
    <ItemButton :name="cLang.Profile.edit"  @click="() => ToModPopup('buTriMod')"/>
    <ItemButton name="Photo" @click="() => ToPicPopup('buPic')"/>
    <ItemButton :name="cLang.Profile.pw" @click="() => ToPassPopup('buPass')"/>
    <router-link to="/">
      <ItemButton :name="cLang.Profile.logout"/>
    </router-link>

  </div>
  <img class="photo" :src="insInfo.pic"/>

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
import { computed, watch, ref, onMounted } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import axios from 'axios';
import Cookies from 'js-cookie';

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

    const insInfo = ref({
      name: '',
      adresse: '',
      numero: '',
      email: '',
      pic: null
    });
    const matricule = ref(Cookies.get('matriculeInscription'));
    const fetchInsInfo = async () => {
          try {
            const response = await axios.get(`http://localhost:1937/inscription/membreServiceInscription`, {
              params: { matricule: matricule.value }
            });
            const insData = response.data;
            console.log(insData);
            insInfo.value.name = insData.name;
            insInfo.value.adresse = insData.adresse;
            insInfo.value.numero = insData.numero;
            insInfo.value.email = insData.email;
            insInfo.value.pic = `data:image/jpeg;base64,${insData.image}`;
          } catch (error) {
            console.error(error);
          }
        };

        onMounted(() => {
          fetchInsInfo();
        });

    return{

      cLang,
      popupMod,
      ToModPopup,
      pwMod,
      ToPassPopup,
      AddPic,
      ToPicPopup,
      uploadedFile,
      handleFileUpload,
      insInfo
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
.photo{
  position: absolute;
  left: 5rem;
  top: 12rem;
  width: 25rem;
  height: 25rem;
}
.info {
  position: absolute;
  left: 35rem;
  top: 0rem;
  color: rgb(158, 11, 23);
  font-family: Roboto, sans-serif;
  font-size: 43px;
  transform: translateY(20px);
}
</style>