<template>
  <div class="popup">
    <div class="inner">
      <center> 
        <div class="allInput">
        <ItemInput :name="cLang.EditPro.num" v-model="num"/>
        <ItemInput :name="cLang.EditPro.ad" v-model="address"/>
        <ItemInput :name="cLang.EditPro.city" v-model="city"/>
      </div>
        <ItemAdd :word="cLang.AddCours.ok" @click="updateUserDetails"/>
        <ItemAdd :word="cLang.AddCours.back" @click="ToModPopup()"/>
      </center>
    </div>
  </div>
</template>

<script>
import ItemInput from '../elements/ItemInput.vue';
import ItemAdd from '../elements/ItemAdd.vue';
import axios from 'axios';
import Cookies from 'js-cookie';
import { ref, computed, watch } from 'vue';
import { useStore } from 'vuex';
import fr from '../views/fr.js';
import en from '../views/en.js';

export default {
  components: { ItemInput, ItemAdd },
  props: ['ToModPopup'],

  setup(props) {
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    // Réactifs pour les champs d'entrée
    const address = ref('');
    const city = ref('');
    const num = ref('');

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    function getUserId() {
      return Cookies.get('matriculeProfesseur') || Cookies.get('matriculeStudent') || Cookies.get('matriculeInscription');
    }

    async function updateUserDetails() {
      const userId = getUserId();
      console.log("test de printer l'adresse : " + address.value)
      if (!userId) {
        console.error('User ID not found in cookies');
        return;
      }

      // Assurez-vous que les valeurs ne sont pas vides avant d'envoyer
      const addressValue = address.value.trim();
      const cityValue = city.value.trim();
      const completeAddress = `${addressValue}, ${cityValue}`;

      if (!addressValue || !cityValue || !num.value) {
        console.error('Address, city, or phone number is empty');
        return;
      }

      try {
        // Mettre à jour l'adresse
        await axios.put(`http://localhost:1937/users/${userId}/address`, completeAddress, {
          headers: {
            'Content-Type': 'text/plain'
          }
        });
        console.log('Address updated successfully.');

        // Mettre à jour le numéro de téléphone
        await axios.put(`http://localhost:1937/users/${userId}/phone`, { phoneNumber: num.value }, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        console.log('Phone number updated successfully.');
      } catch (error) {
        if (error.response) {
          console.error('Error response:', error.response.data);
        } else {
          console.error('Error:', error.message);
        }
      }
    }

    return {
      cLang,
      address,
      city,
      num,
      getUserId,
      updateUserDetails,
      ToModPopup: props.ToModPopup
    };
  }
};
</script>

<style scoped>

.allInput{
  position: relative;
  bottom: 2.5rem;
}
.popup {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 99;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.inner {
  position: relative;
  background: rgb(255, 255, 255);
  padding: 39px;
  border-radius: 10%;
}

.po {
  font-size: 30px;
  color: rgb(134, 10, 10);
}
</style>
