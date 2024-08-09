<template>
  <div class="popup">
    <div class="inner">
      <center>
        <p>Nom: {{ student.lastName }} {{ student.firstName }}</p>
        <p>Matricule: {{ student.matricule }}</p>
        <button @click="$emit('delete', student.matricule)">Supprimer</button>
        <button @click="$emit('close')">Fermer</button>
      </center>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    student: Object
  }
}
</script>

<style scoped>
.popup {
  position: fixed;
  top: 0px;
  bottom: 0px;
  left: 0px;
  right: 0px;
  z-index: 99;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.inner {
  position: relative;
  background: rgb(255, 255, 255);
  padding: 39px;
  border-radius: 10%;
}
</style>