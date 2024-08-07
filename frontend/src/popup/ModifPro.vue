<template>
  <div class="popup">
    <div class="inner">
      <center>
        <div class="allInput">
          <div class="input-group">
            <label>{{ cLang.EditPro.num }}</label>
            <input type="tel" v-model="num" class="custom-input" />
          </div>

          <div class="input-group">
            <label>{{ cLang.EditPro.ad }}</label>
            <input type="text" v-model="address" class="custom-input" />
          </div>

          <div class="input-group">
            <label>{{ cLang.EditPro.city }}</label>
            <input type="text" v-model="city" class="custom-input" />
          </div>
        </div>

        <ItemAdd :word="cLang.AddCours.ok" @click="confirmAndReload" />
        <ItemAdd :word="cLang.AddCours.back" @click="ToModPopup()" />
      </center>
    </div>
  </div>
</template>

<script>
import ItemAdd from '../elements/ItemAdd.vue';
import { ref, computed, watch } from 'vue';
import { useStore } from 'vuex';
import axios from 'axios';
import Cookies from 'js-cookie';
import fr from '../views/fr.js';
import en from '../views/en.js';

export default {
  components: { ItemAdd },
  props: ['ToModPopup'],

  setup(props) {
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    const address = ref('');
    const city = ref('');
    const num = ref('');

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    async function updateAddress(matricule, newAddress) {
      try {
        const response = await axios.put(`http://localhost:1937/users/${matricule}/address`, null, {
          params: {
            newAddress
          }
        });
        console.log(response.data);
      } catch (error) {
        console.error(error);
      }
    }

    async function updatePhoneNumber(matricule, newPhoneNumber) {
      try {
        const response = await axios.put(`http://localhost:1937/users/${matricule}/phone`, null, {
          params: {
            newPhoneNumber
          }
        });
        console.log(response.data);
      } catch (error) {
        console.error(error);
      }
    }

    async function confirmAndReload() {
      const matricule = Cookies.get('loginUser'); // replace with actual matricule value
      console.log(matricule);
      await updateAddress(matricule, address.value+" "+city.value);
      await updatePhoneNumber(matricule, num.value);
      window.location.reload();
    }

    return {
      cLang,
      address,
      city,
      num,
      ToModPopup: props.ToModPopup,
      confirmAndReload,
    };
  },
};
</script>

<style scoped>
.allInput {
  position: relative;
  bottom: 2.5rem;
}

.input-group {
  margin-bottom: 1.5rem;
}

.input-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 0.5rem;
  font-size: 1rem;
  color: #7a7a7a;
}

.custom-input {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
  color: #7a7a7a;
  transition: border-color 0.3s ease;
}

.custom-input:focus {
  outline: none;
  border-color: #007bff;
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
  padding: 2rem;
  border-radius: 10%;
}

.details {
  margin-top: 20px;
  font-size: 18px;
  color: rgb(134, 10, 10);
}

.po {
  font-size: 30px;
  color: rgb(134, 10, 10);
}
</style>
