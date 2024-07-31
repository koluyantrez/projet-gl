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
  border-radius: 0.2rem;
  box-shadow: 0 0.2rem 0.4rem rgba(0, 0, 0, 0.1);
  padding: 1rem; /* Adjust padding to your needs */
  transition: transform 0.3s;
  width: 36rem; /* Set fixed width */
  height: 9rem; /* Set fixed height */
  margin: 0.1rem ; /* Center the section horizontally */
}

.course-section h2 {
  margin-top: 0;
  font-size: 35px;
  font-family: Roboto, sans-serif;
  color: black;
}

.course-section-hover {
  cursor: pointer;
}

.course-section-hover:hover {
  transform: scale(1.05);
}
</style>
