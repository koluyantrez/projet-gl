<template>
  <div class="popup">
    <div class="inner">
      <slots/>
      <center>
        <ItemInput :name="cLang.AddCours.name" v-model:val="name"/>
        <ItemInput :name="cLang.AddCours.titu" v-model:val="prof"/>
        <ItemInput :name="cLang.AddCours.assi" v-model:val="assi"/>
        <ItemAdd :word="cLang.AddCours.ok"/>
        <ItemAdd class="close" :word="cLang.AddCours.back" @click="ToCreatePopup()"/>
      </center>

    </div>
  </div>
</template>

<script>
import ItemInput from '../elements/ItemInput.vue';
import ItemAdd from '../elements/ItemAdd.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../views/fr.js'
import en from '../views/en.js'

export default{
  components: { ItemAdd, ItemInput },
  props: ['ToCreatePopup'],

  data: () => {

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    return{
      cLang,
    }
  }
}


</script>
<style scoped>
.popup{
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

  .inner{
    position: relative;
    background: rgb(255, 255, 255);
    padding: 39px;
    border-radius: 10%;
  }
}
.po{
  font-size: 30px;
  color: rgb(134, 10, 10);
}
</style>
  