<template>
<PubliTop/>
<div class="w">DOSPII</div>
<div class="item">
    <ItemAddAina class="itOne" word="Publication"/>
    <ItemAddAina class="itTwo" word="My CV"/>
    <ItemAddAina class="itThree" word="Contact"/>
</div>
<img class="pic" :src="default" />

<div class="pr">
  <div class="who">This is...</div>
  <div>Specialist in...</div>
  <div>Here since...</div>
  <div>Co-author are :</div>
</div>

<div class="info">
  <div>Adress :</div>
  <div>Email :</div>
  <div>Phone :</div>
  <div>Social Network :</div>
</div>
<hr class="l1">
<hr class="l2">
<div class="descri">
  About me
  <p></p>
</div>
<div class="stat">
  Statistics
  <p></p>
</div>

</template>
<script>
import PubliTop from '../elem/PubliTop.vue';
import ItemAddAina from '../elem/ItemAddAina.vue';

export default {
  components: { PubliTop,ItemAddAina },
  setup(){
    return{
        default: require("../../../assets/profil.png")
    }
  }
}
</script>
<style scoped>
.pr{
  font-family: Roboto,sans-serif;
  font-size: 40px;
  position: absolute;
  top: 25%;
  left: 23%;
}

.info{
  font-family: Roboto,sans-serif;
  font-size: 30px;
  position: absolute;
  top: 32%;
  right: 20%;
}

.stat{
  font-family: Roboto,sans-serif;
  font-size: 60px;
  position: absolute;
  bottom: 5%;
  left: 5%;
}
.descri{
  font-family: Roboto,sans-serif;
  font-size: 60px;
  position: absolute;
  bottom: 27%;
  left: 5%;
}
.w{
  position: absolute;
  top: 12%;
  left: 1%;
  z-index: 91;
  font-size: 50px;
  font-family: Roboto,sans-serif;
}

.who{
  font-size: 75px;
}

.item{
    position: absolute;
    top: 12%;
    z-index: 91;
}

.itOne{
    left: 300%;
}
.itTwo{
    left: 310%;
}
.itThree{
    left: 190%;
}

.pic{
    position: absolute;
    top: 25%;
    left: 5%;
    width: 15%;
    height: 30%;
}

.l1 {
  position: absolute;
  top: 59%;
  left: 20%;
  width: 70%; /* Adjust the length of the line */
  height: 1%; /* Adjust the thickness of the line */
  background-color: black; /* Adjust the color of the line */
  border: none;
}

.l2 {
  position: absolute;
  top: 81%;
  left: 20%;
  width: 70%; /* Adjust the length of the line */
  height: 1%; /* Adjust the thickness of the line */
  background-color: black; /* Adjust the color of the line */
  border: none;
}
</style>
<style>
body{
  background-color: whitesmoke;
}
p{
  font-size: 30px;
}
</style>