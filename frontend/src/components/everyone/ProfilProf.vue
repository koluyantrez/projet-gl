<template>
  <TopProf/>
    <div class="info">
      <br>
      <p><h1>{{ professeurInfo.name }}</h1></p>
      <p>{{ professeurInfo.email }}</p>
      <p>{{ professeurInfo.dep }}/{{ professeurInfo.fili }}</p>
      <p>{{ professeurInfo.adresse }}</p>
      <p>+{{ professeurInfo.numero }}</p>
    </div>
  <div class="b">
    <ItemButton :name="cLang.Profile.edit"  @click="() => ToModPopup('buTriMod')"/>
    <ItemButton name="Photo" @click="() => ToPicPopup('buPic')"/>
    <ItemButton :name="cLang.Profile.pw" @click="() => ToPassPopup('buPass')"/>
    <router-link to="/">
      <ItemButton :name="cLang.Profile.logout"/>
    </router-link>

  </div>
  <ModifPro v-if="popupMod.buTriMod" :ToModPopup="() => ToModPopup('buTriMod')" />
  <DropImg v-if="AddPic.buPic" :ToPicPopup="() => ToPicPopup('buPic')" />
  <PassWord v-if="pwMod.buPass" :ToPassPopup="() => ToPassPopup('buPass')" />
  <img class="photo" :src="professeurInfo.pic"/>
</template>


<script>
import TopProf from '../../elements/TopProf.vue';
import ItemButton from '../../elements/ItemButton.vue';
import ModifPro from '../../popup/ModifPro.vue';
import PassWord from '../../popup/PassWord.vue';
import DropImg from '../../popup/DropImg.vue';
import { useStore } from 'vuex';
import { computed, watch, ref, onMounted } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import Cookies from "js-cookie";
import axios from "axios";
//import Personnel from '../../model/Personnel.java';

export default {
  components: { TopProf, ItemButton, ModifPro, PassWord, DropImg },
  setup() {

    const matricule = ref(Cookies.get('matriculeProfesseur'));
    console.log("le matricule de prof est : " + matricule.value);
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
    const ToPicPopup = (tri) => {
      AddPic.value[tri] = !AddPic.value[tri]
    }
    const popupMod = ref({
      buTriMod: false
    });
    const ToModPopup = (tri2) => {
      popupMod.value[tri2] = !popupMod.value[tri2]
    }

    const pwMod = ref({
      buPass: false
    });
    const ToPassPopup = (tri3) => {
      pwMod.value[tri3] = !pwMod.value[tri3]
    };
    const professeurInfo = ref({
      name: '',
      adresse: '',
      numero: '',
      email: '',
      matricule: '',
      dep: '',
      fili: '',
      pic: null
    });

    const fetchProfInfo = async () => {
      try {
        const response = await axios.get('http://localhost:1937/teachers/findById', {
          params: {
            matricule: matricule.value
          }
        });
        const professeurData = response.data;
        console.log(professeurData);
        professeurInfo.value.name = professeurData.name;
        professeurInfo.value.adresse = professeurData.adresse;
        professeurInfo.value.numero = professeurData.numero;
        professeurInfo.value.email = professeurData.email;
        professeurInfo.value.dep = professeurData.departement;
        professeurInfo.value.fili = professeurData.filiere;
        professeurInfo.value.pic = `data:image/jpeg;base64,${professeurData.image}`;
        professeurInfo.value.matricule = professeurData.matricule; // Set the matricule value
      } catch (error) {
        console.log('error fetching teacher info : ', error);
      }
    };

    onMounted(() => {
      fetchProfInfo();
    });

    return {

      cLang,

      popupMod,
      ToModPopup,
      pwMod,
      ToPassPopup,
      AddPic,
      ToPicPopup,
      uploadedFile, //update the database
      handleFileUpload,
      professeurInfo
    };
  }
}
</script>

<style scoped>
.photo{
  position: absolute;
  left: 5rem;
  top: 12rem;
  width: 20rem;
  height: 25rem;
}

.w {
  position: absolute;
  left: 110px;
  top: 70px;
}

.b {
  position: absolute;
  left: 100px;
  bottom: 100px;
}

.i {
  position: absolute;
  left: 90px;
  bottom: 350px;
  width: 300px;
  height: 300px;
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


<style>
h1 {
  color: rgb(158, 11, 23);
  font-family: Roboto, sans-serif;
  font-weight: 450;
  font-size: 100px;
}
</style>