<template>
  <div class="container">
    <header>
      <div class="logo">
        <h1>ILLUMIS</h1>
        <p>Université d'Illumis</p>
      </div>
    </header>
    <main>
      <h2>Formulaire de demande d'une réservation :</h2>
      <form @submit.prevent="submitReservationForm" id="reservation-form">
        <div class="form-group">
          <label for="firstName">-Prénom :</label>
          <input type="text" v-model="firstName" id="firstName" required  readonly/>
        </div>
        <div class="form-group">
          <label for="lastName">-Nom :</label>
          <input type="text" v-model="lastName" id="lastName" required readonly/>
        </div>
        <div class="form-group">
          <label for="date">-Date de réservation :</label>
          <input type="date" v-model="date" id="date" required />
        </div>
        <div class="form-group">
          <label for="start">-Heure de début :</label>
          <input type="time" v-model="start" id="start" required />
        </div>
        <div class="form-group">
          <label for="end">-Heure de fin :</label>
          <input type="time" v-model="end" id="end" required />
        </div>
        <div class="form-group">
          <label for="reason">-Motif de la demande :</label>
          <input type="text" v-model="reason" id="reason" list="reasons" required />
          <datalist id="reasons">
            <option v-for="reason in reasons" :key="reason" :value="reason">{{ reason }}</option>
          </datalist>
        </div>
        <div class="form-group">
          <label for="building">-Bâtiment :</label>
          <input type="text" v-model="building" id="building" list="buildings" @change="loadRooms" required />
          <datalist id="buildings">
            <option v-for="building in buildings" :key="building.name" :value="building.name">{{ building.name }}</option>
          </datalist>
        </div>
        <div class="form-group">
          <label for="desiredRoom">-Salle souhaitée :</label>
          <input type="text" v-model="desiredRoom" id="desiredRoom" list="rooms" required />
          <datalist id="rooms">
            <option v-for="room in rooms" :key="room.name" :value="room.name">{{ room.name }}</option>
          </datalist>
        </div>
        <button type="submit">Envoyer</button>
      </form>
    </main>
  </div>
</template>

<script>
import Cookies from 'js-cookie';

export default {
  data() {
    return {
      firstName: '',
      lastName: '',
      date: '',
      start: '',
      end: '',
      reason: '',
      building: '',
      desiredRoom: '',
      reasons: [],
      buildings: [],
      rooms: [],
    };
  },
  mounted() {
    this.loadBuildings();
    this.loadReasons();
    this.loadUserDetails();
  },
  methods: {
    loadBuildings() {
      fetch('http://localhost:1937/api/buildings')
          .then((response) => response.json())
          .then((data) => {
            this.buildings = data;
          })
          .catch((error) => console.error('Erreur lors de la récupération des bâtiments:', error));
    },
    loadRooms() {
      const buildingName = this.building;
      if (!buildingName) {
        this.rooms = [];
        return;
      }

      fetch(`http://localhost:1937/api/rooms/building/${buildingName}`)
          .then((response) => response.json())
          .then((data) => {
            this.rooms = data.filter((room) => room.type !== 'OFFICE');
          })
          .catch((error) => console.error('Erreur lors de la récupération des salles:', error));
    },
    loadReasons() {
      fetch('http://localhost:1937/api/reservation/reasons')
          .then((response) => response.json())
          .then((data) => {
            this.reasons = data;
          })
          .catch((error) => console.error('Erreur lors de la récupération des motifs:', error));
    },
    loadUserDetails() {
      const matricule = Cookies.get('demandeur');
      console.log("mat : " + matricule);
      if (matricule) {
        fetch(`http://localhost:1937/users/${matricule}`)
            .then((response) => response.json())
            .then((data) => {
              this.firstName = data.firstName;
              this.lastName = data.lastName;
            })
            .catch((error) => console.error('Erreur lors de la récupération des détails de l\'étudiant:', error));
      }
    },
    submitReservationForm() {
      const {firstName, lastName, date, start, end, reason, desiredRoom} = this;
      if (!firstName || !lastName || !date || !start || !end || !reason || !desiredRoom) {
        alert('Veuillez remplir tous les champs.');
        return;
      }
      console.log('firstName :  '+ firstName);
      fetch('http://localhost:1937/api/roomReservation/addReservation', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({firstName, lastName, date, start, end, reason, desiredRoom}),
      })
          .then((response) => response.json())
          .then(() => {
            alert('Réservation envoyée avec succès');
            this.resetForm();
          })
          .catch((error) => {
            console.error('Erreur lors de l\'envoi de la réservation:', error);
            alert('Une erreur est survenue lors de l\'envoi de la réservation.');
          });
    },
    resetForm() {
      this.firstName = '';
      this.lastName = '';
      this.date = '';
      this.start = '';
      this.end = '';
      this.reason = '';
      this.building = '';
      this.desiredRoom = '';
      this.rooms = [];
    },
  },
};
</script>

<style scoped>
body {
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
  margin: 0;
  padding: 0;
}

.container {
  width: 80%;
  margin: 0 auto;
  background-color: white;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

header {
  text-align: center;
  margin-bottom: 20px;
}

header .logo h1 {
  margin: 0;
  font-size: 2.5em;
  color: #b70000;
}

header .logo p {
  margin: 0;
  font-size: 1.2em;
  color: #666;
}

h2 {
  color: #b70000;
  text-align: center;
  margin-bottom: 20px;
}

form {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

form .form-group {
  margin-bottom: 15px;
}

form label {
  font-size: 1.1em;
  margin-right: 10px;
}

form input[type="text"],
form input[type="date"],
form input[type="time"],
form select {
  padding: 5px;
  font-size: 1.1em;
}

button {
  background-color: #b70000;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  align-self: flex-end;
}

button:hover {
  background-color: #930000;
}
</style>
