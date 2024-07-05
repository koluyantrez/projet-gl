<template>
<div class="course-section course-section-hover">
    <h2>{{word}}</h2>
</div>
</template>
<script>
export default{
    props: ['word']
}
</script>
<style scoped>



.course-section {
  background-color: whitesmoke;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 5%;
  transition: transform 0.3s;
  width: 350%;
  height: 125%;
  margin-left: 5%;
  margin-right: 2%;

}

.course-section h2 {
  margin-top: 0;
  margin-left: 2%;
  font-size: 40px;
  font-family: Roboto,sans-seriff;
  color: black;
}

.course-section-hover {
  cursor: pointer;
}

.course-section-hover:hover {
  transform: scale(1.05);
}
</style>