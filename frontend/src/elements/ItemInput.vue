<template>
  <center>
    <div class="form__group field">
      <input :value="modelValue" :type="type" class="form__field" :placeholder="name" required="" @input="$emit('update:modelValue', $event.target.value)"  >
      <label class="form__label">{{ name }}</label>
    </div>
  </center>
</template>

<script>
export default {
  props: {
    type:{
      type: String,
      default: 'text'
    },
    name:String,
    modelValue: String
  },

  emits: ['update:val'],
  methods: {
    onInput(event) {
      console.log('Input event:', event.target.value);
      this.$emit('update:val', event.target.value);
    }
  }
}
</script>


<style scoped>

.form__group {
position: relative;
padding: 20px 0 0;
width: 100%;
max-width: 500px;
margin-top: 15px;
}

.form__field {
font-family: inherit;
width: 31rem;
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
transition: 0.3s;
font-size: 30px;
color: #9b9b9b;
pointer-events: none;
font-family: roboto, sans-serif;
}

.form__field:focus {
padding-bottom: 6px;
font-weight: 700;
border-width: 3px;
border-image: linear-gradient(to right, #991111, #ef3838);
border-image-slice: 1;
}

.form__field:focus ~ .form__label {
position: absolute;
top: 0;
display: block;
transition: 0.3s;
font-size: 20px;
color: #5c3a3a;
font-weight: 700;
}


.form__field:required, .form__field:invalid {
box-shadow: none;
}

</style>