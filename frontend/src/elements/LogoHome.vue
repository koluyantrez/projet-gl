<template>
    <img id="picture" :src="src" width="1" height="1" class="logo">
</template>

<script>
export default{
  props: ['src']
}
</script>

<style scoped>
.logo {
  position: absolute; 
  top: 10px; 
  left: 25px; 
  width: 100px; 
  height: 100px; 
}

#picture {
  width: 155px;
  height: 75px;
}
</style>