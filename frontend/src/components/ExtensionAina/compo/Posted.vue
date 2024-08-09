<template>
    <PubliTop/>
    <div class="w">
        Be carefully because you will cannot change anything after
    </div>
    <div class="in">
        <ItemInputAina word="Title"/>
        <ItemInputAina word="Co-author"/>
        <ItemInputAina word="Main discipline"/>
        <ItemInputAina word="Key words"/>
        <ItemInputAina word="Language (form ISO 639-2)"/>
        <ItemInputAina word="Commentary"/>
    </div>
    <ItemAddAina class="import" word="Your doc"/> 

    <!--fait un popup de confirmation-->
    <ItemAddAina class="ok" word="post"/>
</template>
<script>
import ItemInputAina from '../elem/ItemInputAina.vue';
import ItemAddAina from '../elem/ItemAddAina.vue';
import PubliTop from '../elem/PubliTop.vue';

export default{
    components: { ItemInputAina, PubliTop, ItemAddAina }
}
</script>
<style scoped>
.in{
    position: absolute;
    left: 10%;
    top: 20%;
}
.w{
  position: fixed;
  top: 12%;
  left: 16%;
  z-index: 91;
  font-size: 50px;
  font-family: Roboto,sans-serif;
}
.import{
    position: absolute;
    left: 32%;
    bottom: 5%;
}
.ok{
    position: absolute;
    right: 12%;
    top: 23%;
}
</style>