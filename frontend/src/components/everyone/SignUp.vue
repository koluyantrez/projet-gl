<template>
  <TopGuest/>
  <div class="data">
    <ItemInput v-model="name" :name="cLang.SignUp.ln"/>
    <ItemInput v-model="firstName" :name="cLang.SignUp.fn"/>
    <ItemInput v-model="birthDate" :name="cLang.SignUp.born"/>
    <ItemInput v-model="address" :name="cLang.SignUp.ad"/>
    <ItemInput v-model="city" :name="cLang.SignUp.city"/>
    <ItemInput v-model="phone" :name="cLang.SignUp.phone"/>
    <ItemInput v-model="filial" :name="cLang.SignUp.dp"/>
    <!--    <label for="filial">Filial</label>-->
    <!--    <select v-model="request.filial" id="departement">-->
    <!--      <option v-for="departement in departements" :key="departement" :value="departement">-->
    <!--        {{ departement }}-->
    <!--      </option>-->
    <!--    </select>-->
  </div>
  <ItemAdd class="pic" word="Photo" @click="() => ToPicPopup('buPic')"/>
  <ItemButton class="finish" :name="cLang.SignUp.done" @click="submitSignup"/>
  <ItemGrade class="tmp"/>
  <input id="file" type="file" style="display: none;" @change="handleFileUpload" />

  <div class="uploaded-image" v-if="uploadedFile">
    <img class="see" :src="uploadedFile"  />
  </div>


  <DropImg v-if = "AddPic.buPic" :ToPicPopup="() => ToPicPopup('buPic')" />
</template>
<script>
import TopGuest from '../../elements/TopGuest.vue';
import ItemInput from '../../elements/ItemInput.vue';
import ItemButton from '../../elements/ItemButton.vue';
import ItemAdd from '../../elements/ItemAdd.vue';
import ItemGrade from '../../elements/ItemGrade.vue';
import DropImg from '../../popup/DropImg.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../../views/fr.js';
import en from '../../views/en.js';
import axios from "axios";
export default{
  components: { TopGuest, ItemInput, ItemButton ,ItemAdd, DropImg,ItemGrade},
  setup(){

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);


    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const AddPic = ref({
      buPic: false
    });
    const ToPicPopup = (tri)=>{
      AddPic.value[tri]=!AddPic.value[tri]
    }

    const uploadedFile = ref(null);
    const handleFileUpload = (event) => {
      const file = event.target.files[0];
      uploadedFile.value = URL.createObjectURL(file);
    };

    const name = ref('');
    const firstName = ref('');
    const birthDate = ref('');
    const address = ref('');
    const city = ref('');
    const phone = ref('');
    const filial = ref('');

    const submitSignup = async () => {
      try {
        const response = await axios.post('http://localhost:1937/api/guest/signup', {
          name: name.value,
          firstName: firstName.value,
          birthDate: birthDate.value,
          address: address.value,
          city: city.value,
          phone: phone.value,
          filial: filial.value,
        });
        alert('Signup request sent successfully!');
      } catch (error) {
        console.error('Error sending signup request:', error);
        alert('Failed to send signup request.');
      }
    };

    return{
      AddPic,
      ToPicPopup,
      uploadedFile,
      handleFileUpload,

      cLang,

      name,
      firstName,
      birthDate,
      address,
      city,
      phone,
      filial,
      submitSignup


    }

  }
}
</script>
<style scoped>

.pic{
  position: absolute;
  top: 15rem;
  right: 15rem;

}

.finish{
  position: absolute;
  bottom: 5rem;
  left: 5rem;
}

.data{
  position: absolute;
  left: 5rem;
  top: 8rem;
}

.tmp{
  position: absolute;
  top: 10rem;
  right: 15rem;
}

.uploaded-image{
  position: absolute;
  bottom: 40rem;
  left: 50rem;
}

.see{
  position: absolute;
  width: 25rem;
  height: 25rem;
}
</style>