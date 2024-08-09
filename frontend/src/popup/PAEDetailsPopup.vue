<template>
<div class="popup" v-if="request">
<h2>Détails de la demande de PAE</h2>
<p><strong>Request ID:</strong> {{ request.id }}</p>
<p><strong>Student ID:</strong> {{ request.studentId }}</p>
<p><strong>Courses:</strong> {{ request.courses.join(', ') }}</p>
<button @click="acceptPAE">Accepter</button>
<button @click="rejectPAE">Refuser</button>
<button @click="closePopup">Fermer</button>
</div>
<div v-else>
<p>No request details available.</p>
</div>
</template>

<script>
import axios from "axios";

export default {
  props: {
    request: {
      type: Object,
      required: false,
      default: null
    }
  },
  methods: {
    acceptPAE() {
      axios.post(`http://localhost:1937/api/pae-requests/${this.request.id}/accept`)
          .then(() => {
            this.updateStudentPAE();
            this.$emit('accept', this.request);
            this.deleteRequest();
          })
          .catch((error) => {
            console.error(error);
          });
    },
    rejectPAE() {
      axios.post(`http://localhost:1937/api/pae-requests/${this.request.id}/reject`)
          .then(() => {
            this.$emit('reject', this.request);
          })
          .catch((error) => {
            console.error(error);
          });
    },
    deleteRequest() {
      axios.delete(`http://localhost:1937/api/pae-requests/${this.request.id}`)
          .then(() => {
            this.$emit('delete', this.request);
          })
          .catch((error) => {
            console.error(error);
          });
    },
    updateStudentPAE() {
      const studentId = this.request.studentId;
      const newCourses = this.request.courses;

      axios.get(`http://localhost:1937/api/students/${studentId}/pae`)
          .then(response => {
            const currentPAE = response.data.pae;
            const updatedPAE = [...currentPAE, ...newCourses];

            axios.put(`http://localhost:1937/api/students/${studentId}/pae`, { pae: updatedPAE })
                .then(() => {
                  console.log("Student PAE updated");
                })
                .catch((error) => {
                  console.error(error);
                });
          })
          .catch((error) => {
            console.error(error);
          });
    },
    closePopup() {
      this.$emit('close');
    }
  }
}
</script>

<style>
.popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border: 1px solid #ccc;
}
</style>