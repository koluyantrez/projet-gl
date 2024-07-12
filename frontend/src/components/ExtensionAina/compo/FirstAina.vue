<template>
  <PubliTop/>
  <div class="w">
    Welcome to the Directory Of Scientific Publication In Illumis
  </div>
  <div class="bo">
    <i>
      If you have to ask, you'll never know. If you know, you need only ask.
    </i>
  </div>
  <ItemSearch class="is"/>
    <div class="item">
      
      <ItemAddAina word="Author"/>
      <ItemAddAina word="Post"/>
    </div>
  <div class="back">
    <ItemButtonAina m1="Just key word?" m2="OK let me search..."/>
    The DOSPIM includes x documents from y authors all from the scientific faculty.
    <div class="show"><img class="bg"  src="../../../assets/gang.jpg"/></div>
  </div>
  <div class="cit">
    <center>
      To succeed is not always what we believe. It is not to become famous, rich or powerful. To succeed is to get out of bed in the morning and to be happy of what we will do during the day, so happy that we have the impression to fly away. It’s working with people you love. Success means being in touch with the world and communicating your passion.
    </center>
  </div>
</template>
<script>
import PubliTop from '../elem/PubliTop.vue';
import ItemButtonAina from '../elem/ItemButtonAina.vue';
import ItemSearch from '../../../elements/ItemSearch.vue';
import ItemAddAina from '../elem/ItemAddAina.vue';

export default {
  components: { PubliTop, ItemSearch, ItemAddAina, ItemButtonAina }
}

</script>
<style scoped>
.cit{
  position: absolute;
  font-family: Roboto,sans-serif;
  font-size: 40px;
  color: rgb(194, 0, 0);
  bottom: 6rem;
  right: 2rem;
  z-index: 90;
}

.bo{
  position: absolute;
  top: 22%;
  left: 25%;
  font-size: 40px;
  font-family: Roboto,sans-serif;
}

.item{
  position: absolute;
  right: 43.5%;
  top: 38%;
}

.back{
  position: absolute;
  bottom: 41%;
  left: 27%;
  font-size: 24px;
  font-family: Roboto,sans-serif;
}

.bg{
  position: absolute;
  bottom: -25rem;
  left: 0rem; /* Ajustez selon la partie de l'image à afficher */
}

.show {
  position: absolute;
    width: 224.8%; 
    height: 300%; 
    bottom: -25.6rem; 
    left: -32.8rem;  
    opacity: 0.4;
    overflow: hidden;
    /*border: 3px solid rgb(6, 148, 37); /* Bordure de la zone conteneur */
}

.is{
  position: absolute;
  top: 30%;
  left: 43%
}

.w{
  position: fixed;
  top: 12%;
  left: 16%;
  z-index: 91;
  font-size: 50px;
  font-family: Roboto,sans-serif;
}

</style>