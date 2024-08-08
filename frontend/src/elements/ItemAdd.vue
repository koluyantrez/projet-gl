<template>
<button @click="cli">
    <span class="circle1"></span>
    <span class="circle2"></span>
    <span class="circle3"></span>
    <span class="circle4"></span>
    <span class="circle5"></span>
    <span class="text">{{ word }}</span>
</button>
</template>
<script>
export default{
    props: ['word','click'],
    methods:{
        cli(){
        }
    }
}

</script>
<style scoped>
button {
  font-family: Roboto, sans-serif;
  color: 37373a;
  background-color: #d4d4d4;
  padding: 1em 2em;
  border: none;
  border-radius: .6rem;
  position: relative;
  cursor: pointer;
  overflow: hidden;
  margin-right: 1.5rem;
}

button span:not(:nth-child(6)) {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: 43px;
  width: 43px;
  background-color: rgb(236, 181, 181);
  border-radius: 50%;
  transition: 1s ease;
}

button span:nth-child(6) {
  position: relative;
}

button span:nth-child(1) {
  transform: translate(-4em, -5.6em); /* up left*/ 
}

button span:nth-child(2) {
  transform: translate(-6em, 2.3em); /* down left*/ 
}

button span:nth-child(3) {
  transform: translate(-.5em, 2.3em); /* down */ 
}

button span:nth-child(4) {
  transform: translate(4.4em, 1.7em); /* down right*/ 
}

button span:nth-child(5) {
  transform: translate(3.2em, -5.5em); /* up right*/ 
}

button:hover span:not(:nth-child(6)) {
  transform: translate(-50%, -50%) scale(4);
  transition: 1.1s ease;
}
  
</style>