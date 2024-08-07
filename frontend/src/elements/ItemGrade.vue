<template>
<div class="wrapper">
  <div class="option">
    <input checked="" value="option1" name="btn" type="radio" class="input">
    <div class="btn">
      <span class="span">BACHELIER</span>
    </div>
  </div>
  <div class="option">
    <input value="option2" name="btn" type="radio" class="input">
    <div class="btn">
      <span class="span">MASTER</span>
    </div>  </div>
  <div class="option">
    <input value="option3" name="btn" type="radio" class="input">
    <div class="btn">
      <span class="span">DOCTORAT</span>
    </div>  
  </div>
</div>

</template>
<script>

</script>
<style scoped>
.wrapper {
  --font-color-dark: azure;
  --font-color-light: black;
  --bg-color: rgb(158, 11, 23);
  --main-color: rgb(236, 181, 181);
  position: relative;
  width: 367px;
  height: 45px;
  background-color: var(--bg-color);
  border: 2px solid var(--main-color);
  border-radius: 34px;
  display: flex;
  flex-direction: row;
  box-shadow: 4px 4px var(--main-color);
}

.option {
  width: 121px;
  height: 42px;
  position: relative;
  top: 2px;
  left: 2px;
}

.input {
  width: 100%;
  height: 100%;
  position: absolute;
  left: 0;
  top: 0;
  appearance: none;
  cursor: pointer;
}

.btn {
  width: 100%;
  height: 100%;
  background-color: var(--bg-color);
  border-radius: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.span {
  color: var(--font-color-dark);
  font-size: 15px;
  font-family: Roboto, sans-serif;
}

.input:checked + .btn {
  background-color: var(--main-color);
}

.input:checked + .btn .span {
  color: var(--font-color-light);
}
</style>