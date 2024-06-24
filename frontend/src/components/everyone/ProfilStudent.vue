<template>
  <div>
    <TopStudent />
    <div class="b">
      <ItemButton :name="cLang.Profile.unsi" @click="() => ToUnsubPopup('buTriUnsub')" />
      <ItemButton name="Photo" @click="() => ToPicPopup('buPic')" />
      <ItemButton :name="cLang.Profile.edit" @click="() => ToModPopup('buTriMod')" />
      <ItemButton :name="cLang.Profile.logout" />
      <ItemButton :name="cLang.Profile.pw" @click="() => ToPassPopup('buPass')" />
      <ItemButton name="PAE" />
    </div>
    <YesOrNo v-if="popupUnsub.buTriUnsub" :ToUnsubPopup="() => ToUnsubPopup('buTriUnsub')" />
    <ModifPro v-if="popupMod.buTriMod" :ToModPopup="() => ToModPopup('buTriMod')" />
    <PassWord v-if="pwMod.buPass" :ToPassPopup="() => ToPassPopup('buPass')" />
    <DropImg v-if="AddPic.buPic" :ToPicPopup="() => ToPicPopup('buPic')" />
    <div class="info">
      <p></p>
      <br>
      <p>{{ studentInfo.name }}</p>
      <p>{{ studentInfo.adresse }}</p>
      <p>{{ studentInfo.numero }}</p>
      <p>{{ studentInfo.email }}</p>
    </div>
  </div>
</template>

<script>
import TopStudent from '../../elements/TopStudent.vue';
import DropImg from '../../popup/DropImg.vue';
import ItemButton from '../../elements/ItemButton.vue';
import YesOrNo from '../../popup/YesOrNo.vue';
import ModifPro from '../../popup/ModifPro.vue';
import PassWord from '../../popup/PassWord.vue';
import { useRouter } from 'vue-router';
import { computed, watch, ref, onMounted } from 'vue';
import axios from 'axios';
import fr from '../../views/fr';
import en from '../../views/en';
import { useStore } from 'vuex';
import Cookies from 'js-cookie';


export default {
  components: { TopStudent, ItemButton, YesOrNo, ModifPro, PassWord, DropImg },


  setup() {

    const matricule = ref(Cookies.get('matriculeStudent'));
    console.log("la valeur de matricule " + matricule.value);
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const AddPic = ref({
      buPic: false
    });
    const ToPicPopup = (tri) => {
      AddPic.value[tri] = !AddPic.value[tri];
    };

    const popupUnsub = ref({
      buTriUnsub: false
    });
    const ToUnsubPopup = (tri1) => {
      popupUnsub.value[tri1] = !popupUnsub.value[tri1];
    };

    const popupMod = ref({
      buTriMod: false
    });
    const ToModPopup = (tri2) => {
      popupMod.value[tri2] = !popupMod.value[tri2];
    };

    const pwMod = ref({
      buPass: false
    });
    const ToPassPopup = (tri3) => {
      pwMod.value[tri3] = !pwMod.value[tri3];
    };

    const studentInfo = ref({
      name: '',
      adresse: '',
      numero: '',
      email: ''
    });

    const router = useRouter();
    //const matricule = ref(router.currentRoute.value.query.matricule);
    console.log("la valeur de matricule " + matricule.value);

    const fetchStudentInfo = async () => {
      try {
        const response = await axios.get('http://localhost:1937/students/findById', {
          params: {
            matricule: matricule.value
          }
        });
        const studentData = response.data;
        studentInfo.value.name = studentData.name;
        studentInfo.value.adresse = studentData.adresse;
        studentInfo.value.numero = studentData.numero;
        studentInfo.value.email = studentData.email;
      } catch (error) {
        console.error('Error fetching student info:', error);
      }
    };

    onMounted(() => {
      fetchStudentInfo();
    });

    return {
      cLang,
      AddPic,
      ToPicPopup,
      popupUnsub,
      ToUnsubPopup,
      popupMod,
      ToModPopup,
      pwMod,
      ToPassPopup,
      studentInfo
    };
  }
}


</script>

<style scoped>
.w {
  position: absolute;
  left: 110px;
  top: 70px;
}

.b {
  position: absolute;
  left: 55px;
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
  left: 530px;
  bottom: 300px;
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
