<template>
  <component :is="top"/>
  <div class="b">
    <ItemButton v-if="isStudentRole" :name="cLang.Profile.unsi" @click="() => ToUnsubPopup('buTriUnsub')" />
    <ItemButton name="Photo" @click="() => ToPicPopup('buPic')" />
    <ItemButton :name="cLang.Profile.edit" @click="() => ToModPopup('buTriMod')" />
    <router-link to="/">
      <ItemButton :name="cLang.Profile.logout" @click="clearCookies"/>
    </router-link>
    <ItemButton :name="cLang.Profile.pw" @click="() => ToPassPopup('buPass')" />
    <router-link to="/student/PAE">
      <ItemButton v-if="isStudentRole" name="PAE" />
    </router-link>
  </div>
  <YesOrNo v-if="popupUnsub.buTriUnsub" :ToUnsubPopup="() => ToUnsubPopup('buTriUnsub')" />
  <ModifPro v-if="popupMod.buTriMod" :ToModPopup="() => ToModPopup('buTriMod')" />
  <PassWord v-if="pwMod.buPass" :ToPassPopup="() => ToPassPopup('buPass')" />
  <DropImg v-if="AddPic.buPic" :ToPicPopup="() => ToPicPopup('buPic')" />
  <div class="info">
    <br>
    <p><h1>{{ studentInfo.name }}</h1></p>
    <p>{{ studentInfo.email }}</p>
    <p>{{ studentInfo.departement }}/{{ studentInfo.fili }}</p>
    <p>{{ studentInfo.adresse }}</p>
    <p>+{{ studentInfo.numero }}</p>
  </div>
  <img class="photo" :src="studentInfo.pic"/>
</template>

<script>
import TopStudent from '../../elements/TopStudent.vue';
import TopSec from '../../elements/TopSec.vue';
import TopProf from '../../elements/TopProf.vue';
import TopSecretariat from '../../elements/TopSecretariat.vue';
import DropImg from '../../popup/DropImg.vue';
import ItemButton from '../../elements/ItemButton.vue';
import YesOrNo from '../../popup/YesOrNo.vue';
import ModifPro from '../../popup/ModifPro.vue';
import PassWord from '../../popup/PassWord.vue';
import {useRouter} from 'vue-router';
import {computed, watch, ref, onMounted} from 'vue';
import axios from 'axios';
import fr from '../../views/fr';
import en from '../../views/en';
import {useStore} from 'vuex';
import Cookies from 'js-cookie';

export default {
  components: {TopStudent, TopSec, TopProf, TopSecretariat, ItemButton, YesOrNo, ModifPro, PassWord, DropImg},
  methods: {
    clearCookies() {
      Object.keys(Cookies.get()).forEach(cookieName => {
        Cookies.remove(cookieName);
      });
    }
  },
  setup() {
    const matricule = ref(Cookies.get('matriculeStudent'));
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
      email: '',
      departement: '',
      fili: '',
      pic: null
    });

    const type = ref(Cookies.get('role'));
    const top = computed(() => {
      let result;
      if (type.value === 'student') {
        result = 'TopStudent';
      } else if (type.value === 'professeur') {
        result = 'TopProf';
      } else if (type.value === 'secretariat') {
        result = 'TopSecretariat';
      } else {
        result = 'TopSec';
      }
      return result;
    });

    const role = ref(Cookies.get('role'));
    const isStudentRole = computed(() => role.value === 'student');

    const router = useRouter();
    const userID = Cookies.get('loginUser');
    console.log('loginUser=' + userID);
    const fetchStudentInfo = async () => {
      try {
        const response = await axios.get(`http://localhost:1937/users/${userID}`);
        const studentData = response.data;
        console.log(response.data);
        studentInfo.value.name = studentData.name;
        studentInfo.value.adresse = studentData.adresse;
        studentInfo.value.numero = studentData.numero;
        studentInfo.value.email = studentData.email;
        studentInfo.value.departement = studentData.departement;
        studentInfo.value.fili = studentData.filiere;
        studentInfo.value.pic = `data:image/jpeg;base64,${studentData.image}`;
      } catch (error) {
        console.error('Error fetching student info:', error);
      }
    };

    onMounted(() => {
      fetchStudentInfo();
    });

    return {
      isStudentRole,
      cLang,
      top,
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
.b {
  position: absolute;
  left: 5rem;
  bottom: 3rem;
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

.photo {
  position: absolute;
  left: 5rem;
  top: 12rem;
  width: 25rem;
  height: 25rem;
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