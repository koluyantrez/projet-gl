<template>
<form class="formField">
  <input required="" type="text" v-model="val"/>
  <span>{{word}}</span>
</form>

</template>
<script>
export default{
    props: ['word'],
    setup(){
      return{
        val: ''
      }
    }
    
}
</script>
<style scoped>
.formField {
  margin: 30px;
  position: relative;
}

.formField input {
  padding: 8% 5% 5% 5%;
  width: 300%;
  outline: none;
  border: none;
  border-radius: 5px;
  background-color: #FFF9E3;
  color: rgb(0, 0, 0);
  font-size: 25px;
  font-family: Roboto,sans-serif;
  font-weight: 550;
  transition: 0.3s ease-in-out;
  box-shadow: 0 0 0 5px transparent;
}

.formField input:hover,
.formField input:focus {
  box-shadow: 0 0 0 3px #333;
}

.formField span {
  position: absolute;
  left: 0;
  top: 0;
  padding: 5% 6%;
  color: #333;
  font-size: 18px;
  font-family: Roboto,sans-serif;;
  text-shadow: -1px -1px 0 #f1f1f1, 1px -1px 0 #f1f1f1, -1px 1px 0 #f1f1f1,
    1px 1px 0 #f1f1f1;
  transition: 0.3s ease-in-out;
  pointer-events: none;
}

.formField input:focus + span,
.formField input:valid + span {
  transform: translateY(-12px) translateX(-5px) scale(0.8);
  transition: 0.3s ease-in-out;
}

</style>