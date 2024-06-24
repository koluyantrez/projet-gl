<template>
    <img id="picture" :src="src" width="1" height="1" class="logoprofil">
</template>

<script>
export default{
  props: ['src'],
  imageSize: {
    type: String,
    default: '70px' // Taille initiale de l'image
  }
}
</script>

<style scoped>
.logoprofil{
  position: absolute; 
  top: 10px; 
  right: 25px;
}

#picture {
  width: 75px;
  height: 75px;
  z-index: 20;
}
</style>