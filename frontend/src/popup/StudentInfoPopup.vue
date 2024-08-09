<template>
  <div class="popup">
    <div class="popup-content">
      <h2>{{ student.lastName }} {{ student.firstName }}</h2>
      <p>Email: {{ student.email }}</p>
      <p>Matricule: {{ student.matricule }}</p>
      <p>Department: {{ student.departement }}</p>
      <p>Address: {{ student.adresse }}</p>
      <p>Birth Date: {{ student.naissance }}</p>
      <!-- Add other student details here -->
      <button @click="$emit('close')">Close</button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    student: Object
  }
};
</script>

<style scoped>
.popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.popup-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  width: 300px;
  text-align: center;
}
</style>