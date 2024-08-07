<template>
  <div class="container">
    <header>
      <h1>Illumis</h1>
      <h2>Université de Illumis</h2>
    </header>
    <section v-if="reservation">
      <h3>Modifier le statut de la demande :</h3>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="id-reservation">ID de la réservation :</label>
          <input type="text" id="id-reservation" :value="reservation.id" disabled>
        </div>
        <div class="form-group">
          <label for="nom-demandeur">Nom du demandeur :</label>
          <input type="text" id="nom-demandeur" :value="reservation.firstName + ' ' + reservation.lastName" disabled>
        </div>
        <div class="form-group">
          <label for="salle-souhaitee">Salle souhaitée :</label>
          <input type="text" id="salle-souhaitee" v-model="reservation.desiredRoom" disabled>
          <a href="#" @click.prevent="verifierDisponibilite">Vérifier la disponibilité de la salle.</a>
        </div>
        <div class="form-group">
          <label for="date-heure">Date et heure :</label>
          <input type="datetime-local" id="date-heure" v-model="reservation.dateHeure" disabled>
        </div>
        <div class="form-group">
          <label>Status :</label>
          <div class="radio-group">
            <input type="radio" id="acceptee" value="APPROVED" v-model="status">
            <label for="acceptee">Acceptée</label>
            <input type="radio" id="refusee" value="REJECTED" v-model="status">
            <label for="refusee">Refusée</label>
          </div>
        </div>
        <button type="submit">Valider</button>
      </form>
    </section>
  </div>
</template>

<script>
import Cookies from 'js-cookie';

export default {
  data() {
    return {
      reservation: {
        firstName: '',
        lastName: '',
        desiredRoom: '',
        dateHeure: '',
      },
      status: ''
    };
  },
  mounted() {
    const reservationId = Cookies.get('reservationId');
    if (reservationId) {
      this.loadReservationDetails(reservationId);
    } else {
      alert('Aucune réservation sélectionnée.');
      this.$router.push('/reservationList');
    }
  },
  methods: {
    loadReservationDetails(id) {
      fetch(`http://localhost:1937/api/reservation/${id}`)
          .then(response => response.json())
          .then(data => {
            this.reservation = data;
            this.reservation.dateHeure = new Date(data.date).toISOString().slice(0, 16); // Set the datetime-local input format
          })
          .catch(error => console.error('Erreur:', error));
    },
    verifierDisponibilite() {
      alert('Vérification de la disponibilité de la salle.');
    },
    handleSubmit() {
      fetch(`http://localhost:1937/api/${this.reservation.id}/status?newStatus=${this.status}`, {
        method: 'PUT'
      })
          .then(response => {
            if (response.ok) {
              alert('Statut mis à jour avec succès');
              this.$router.push('/reservationList');
            } else {
              alert('Erreur lors de la mise à jour du statut');
            }
          })
          .catch(error => console.error('Erreur:', error));
    }
  }
};
</script>

<style>
.container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

header {
  margin-bottom: 20px;
}

h1 {
  color: #b70000;
  margin-bottom: 5px;
}

h2 {
  color: #666;
  margin-bottom: 20px;
}

h3 {
  color: #b70000;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
  text-align: left;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

input[type="text"],
input[type="datetime-local"] {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

a {
  color: #b70000;
  text-decoration: none;
}

.radio-group {
  display: flex;
  gap: 20px;
}

button {
  background-color: #b70000;
  color: white;
  padding: 12px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #a00000;
}
</style>
