<template>
  <center>
    <div class="form__group">
      <input type="text" class="form__field" v-model="inputValue" required>
      <label class="form__label">{{ name }}</label>
    </div>
  </center>
</template>

<script>
export default {
  props: ['name', 'value'],
  computed: {
    inputValue: {
      get() {
        return this.value;
      },
      set(newValue) {
        this.$emit('update:value', newValue);
      }
    }
  }
}
</script>

<style >
.form__group {
  position: relative;
  padding: 20px 0 0;
  width: 100%;
  max-width: 500px;
  margin-top: 15px;
}

.form__field {
  font-family: inherit;
  width: 500px;
  border: none;
  border-bottom: 2px solid #9b9b9b;
  outline: 0;
  font-size: 20px;
  color: rgb(238, 165, 165);
  padding: 7px 0;
  background: transparent;
  transition: border-color 0.3s;
  font-family: Roboto, sans-serif;
}

.form__field::placeholder {
  color: transparent;
}

.form__field:placeholder-shown ~ .form__label {
  font-size: 30px;
  cursor: text;
  top: 20px;
}

.form__label {
  position: absolute;
  top: 0;
  display: block;
  transition: 0.2s;
  font-size: 24px;
  color: #9b9b9b;
}

.form__field:focus ~ .form__label,
.form__field:valid ~ .form__label {
  top: -20px;
  font-size: 20px;
  color: #666;
}
</style>