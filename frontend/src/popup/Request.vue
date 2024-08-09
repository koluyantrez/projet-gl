<template>
  <div class="popup">
    <div class="inner">
      <h3>Request Details</h3>
      <p><strong>Name:</strong> {{ request.name }}</p>
      <p><strong>First Name:</strong> {{ request.firstName }}</p>
      <p><strong>Birth Date:</strong> {{ request.birthDate }}</p>
      <p><strong>Address:</strong> {{ request.address }}</p>
      <p><strong>City:</strong> {{ request.city }}</p>
      <p><strong>Phone:</strong> {{ request.phone }}</p>
      <p><strong>Filial:</strong> {{ request.filial }}</p>
      <ItemAdd word="Accept" @click="acceptRequest"/>
      <ItemAdd word="Reject" @click="rejectRequest"/>
      <ItemAdd word="Close" @click="closePopup"/>
    </div>
  </div>
</template>

<script>
import ItemAdd from "@/elements/ItemAdd.vue";
import axios from "axios";

export default {
  components: {ItemAdd},
  props: {
    request: Object,
    show: Boolean
  },
  methods: {
    acceptRequest() {
      axios.post(`http://localhost:1937/secretariat/approve/${this.request.id}`)
          .then(() => {
            this.createStudent();
            this.$emit('accept', this.request);
            this.deleteRequest()
          })
          .catch((error) => {
            console.error(error);
          });
    },
    rejectRequest() {
      axios.post(`http://localhost:1937/secretariat/reject/${this.request.id}`)
          .then(() => {
            this.$emit('reject', this.request);
            this.deleteRequest()
          })
          .catch((error) => {
            console.error(error);
          });    },

    deleteRequest() {
      axios.delete(`http://localhost:1937/api/guest/signup/${this.request.id}`)
          .then(() => {
            this.$emit('delete', this.request);
          })
          .catch((error) => {
            console.error(error);
          });
    },

    createStudent() {
      const student = {
        lastName: this.request.name,
        firstName: this.request.firstName,
        name : this.request.name + " " + this.request.firstName,
        naissance: this.request.birthDate,
        adresse: this.request.city,
        //city: this.request.city,
        numero: this.request.phone,
        departement: this.request.filial,
        annee:2023-2024,
        inscrit: true

      };
      axios.post('http://localhost:1937/api/students', student)
          .then(() => {
            console.log("Student created");
          })
          .catch((error) => {
            console.error(error);
          });
    },

    closePopup() {
      this.$emit('close');
    }
  }
};
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
  font-family: 'Arial', sans-serif;
}


</style>