<template>
  <button class="round-button">+</button>
</template>

<script>
</script>

<style scoped>
.round-button {
  width: 1.2rem;
  height: 1.2rem;
  background-color: rgb(236, 181, 181);
  border: none;
  border-radius: 50%;
  color: white;
  font-size: 20px;
  line-height: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.3s;
}

.round-button:hover {
  background-color: #9e0b17;
}

.round-button:active {
  background-color: #990000;
}
</style>
