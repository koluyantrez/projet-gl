<template>
  <center>
    <p><button class="button" @click="$emit('course-clicked',word)"> {{word}}</button></p>
  </center>
</template>

<script>
export default{
  props: ['word']
}
</script>

<style scoped>
.button {
  --color: rgb(158, 11, 23);
  width: 1600px;
  height: 90px;
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: .3em;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  transition: .5s;
  font-weight: 400;
  font-size: 20px;
  border: 2px solid;
  font-family: Roboto,sans-serif;
  text-transform: uppercase;
  color: var(--color);
  z-index: 1;
}

.button::before, .button::after {
  content: '';
  display: block;
  width: 60px;
  height: 60px;
  transform: translate(-50%, -50%);
  position: absolute;
  border-radius: 50%;
  z-index: -1;
  background-color: var(--color);
  transition: 0.8s ease;
}

.button::before {
  top: -1em;
  left: -1em;
}

.button::after {
  left: calc(100% + 1em);
  top: calc(100% + 1em);
}

.button:hover::before, .button:hover::after {
  height: 1700px;
  width: 1700px;
}

.button:hover {
  color: rgb(236, 181, 181);
}

.button:active {
  filter: brightness(.8);
}

</style>