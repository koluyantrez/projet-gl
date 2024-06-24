<template>
    <div class="display">
      <p class="p1" v-text="word"></p>
    </div>
</template>

<script>
export default{
  props: ['word']
}
</script>

<style scoped>
.p1{
  text-align: center;
}
</style>

<style>

body{
  color: azure;
  font-family: Roboto, sans-serif;
  font-size: 50px;
}
</style>
