<template>
  <div  class="hitop">
    <img class="la" alt="Change the language" src="../../../assets/lang.png" @click="switchLang"/>
    <router-link to="/dospii">
      <LogoHome :src="src2"/>
    </router-link>
    <ProfilPhoto :src="src1"/> 
  </div>
  <div class="lowtop"></div>
</template>
<script>
import ProfilPhoto from '../../../elements/ProfilPhoto.vue';
import LogoHome from '../../../elements/LogoHome.vue';



export default{
    components: { LogoHome, ProfilPhoto },
    setup(){
        return {
            src1: require('../../../assets/profil.png'),
            src2: require('../../../assets/illumis.png'),
        }
    }
}
</script>
<style scoped>
.hitop {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 10%;
  background-color: #FFC803;
  z-index: 90;
}
.lowtop {
  position: absolute;
  top: 10%;
  left: 0;
  width: 100%;
  height: 10%;
  background-color: #FFE662;
  z-index: 90;
}



.la{
  position: absolute;
  top: 1rem;
  left: 13rem;
  width: 4rem; 
  height: auto;
  z-index: 91;
}
</style>
<style>
body{
  background-color: whitesmoke;
}
</style>