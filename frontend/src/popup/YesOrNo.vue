<template>
  <div class="popup">
    <div class="inner">
      <center>
        <p class="po">{{cLang.JungKook.q}}</p>
        <ItemAdd :word="cLang.JungKook.y"/>
        <ItemAdd class="close" :word="cLang.JungKook.n" @click="ToUnsubPopup()"/>
      </center>
    </div>
  </div>
</template>

<script>
import ItemAdd from '../elements/ItemAdd.vue';
import {useStore} from 'vuex';
import {computed, watch, ref} from 'vue';
import fr from '../views/fr.js'
import en from '../views/en.js'

export default {
  components: {ItemAdd},
  props: ['ToUnsubPopup'],

  setup() {
    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    return {
      cLang
    };
  }
}
</script>

<style scoped>
.popup {
  position: fixed;
  top: 0px;
  bottom: 0px;
  left: 0px;
  right: 0px;
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
  font-size: 20px;
  margin-bottom: 20px;
}
</style>