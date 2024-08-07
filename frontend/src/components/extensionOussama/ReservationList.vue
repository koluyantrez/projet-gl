<template>
  <div class="container">
    <header>
      <h1>Illumis</h1>
      <h2>Université d'Illumis</h2>
    </header>
    <section>
      <h3>Liste des demandes de réservations :</h3>
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Demandeur</th>
          <th>Date de la demande</th>
          <th>Heure de début</th>
          <th>Heure de fin</th>
          <th>Salle</th>
          <th>Motif</th>
          <th>Statut</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="reservation in reservations" :key="reservation.id">
          <td>{{ reservation.id }}</td>
          <td>{{ reservation.firstName }} {{ reservation.lastName }}</td>
          <td>{{ new Date(reservation.date).toLocaleDateString() }}</td>
          <td>{{ reservation.start }}</td>
          <td>{{ reservation.end }}</td>
          <td>{{ reservation.desiredRoom }}</td>
          <td>{{ reservation.reason }}</td>
          <td>{{ reservation.reservationStatus }}</td>
          <td>
            <button class="modify-btn" @click="handleModify(reservation.id)" v-if="reservation.reservationStatus === 'PENDING'">Modifier</button>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button @click="prevPage" :disabled="currentPage === 1">&lt;</button>
        <button @click="nextPage">&gt;</button>
      </div>
    </section>
  </div>
</template>

<script>
import Cookies from 'js-cookie';

export default {
  data() {
    return {
      currentPage: 1,
      itemsPerPage: 10,
      reservations: []
    };
  },
  mounted() {
    this.loadReservations(this.currentPage, this.itemsPerPage);
  },
  methods: {
    loadReservations(page, limit) {
      fetch(`http://localhost:1937/api/reservations?page=${page}&limit=${limit}`)
          .then(response => response.json())
          .then(data => {
            this.reservations = data;
          })
          .catch(error => console.error('Erreur:', error));
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.loadReservations(this.currentPage, this.itemsPerPage);
      }
    },
    nextPage() {
      this.currentPage++;
      this.loadReservations(this.currentPage, this.itemsPerPage);
    },
    handleModify(reservationId) {
      Cookies.set('reservationId', reservationId);
      this.$router.push({ name: 'updateReservation' });
    }
  }
};
</script>

<style>
.container {
  max-width: 1000px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

header {
  text-align: center;
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
  text-align: center;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

th, td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

th {
  background-color: #b70000;
  color: white;
}

.modify-btn {
  background-color: #000;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.modify-btn:hover {
  background-color: #333;
}

.pagination {
  text-align: center;
}

button {
  background-color: #b70000;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
