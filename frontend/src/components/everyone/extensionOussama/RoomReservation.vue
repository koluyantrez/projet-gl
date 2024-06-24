<template>
  <div>
    <header>
      <img src="../../../assets/illumis.png" id="logoUmons" style="height: 100px; background-color: white;" >
    </header>

    <main>
      <article class="container">
        <div> -Nom : <input type="text" v-model="nom"> </div>
        <div> -Prenom : <input type="text" v-model="prenom"> </div>
        <div> -Votre matricule : <input type="number" v-model="matricule"></div>
        <div> -Id de salle souhaitée : <input type="number" v-model="idRoom"></div>
        <div> -Date de réservation : <input type="date" v-model="date"></div>
        <div> -Heure de début : <input type="time" v-model="start"></div>
        <div> -Heure de fin : <input type="time" v-model="end"></div>
        <div> -Motif de la demande  :
          <select v-model="motif">
            <option value="evenement">EVENEMENT</option>
            <option value="cours">COURS</option>
            <option value="seminaire">SEMINAIRE</option>
            <option value="remediation">Remediation</option>
          </select>
        </div>
        <button type="submit" id="buttonEnvoyer" @click="submitForm"> Envoyer</button>
      </article>
    </main>
  </div>
</template>

<script>
export default {
  data() {
    return {
      nom: '',
      prenom: '',
      matricule: '',
      idRoom: '',
      date: '',
      start: '',
      end: '',
      motif: 'evenement'
    };
  },
  methods: {
    submitForm() {
      // Vérifier si tous les champs sont remplis
      if (this.nom === '' || this.prenom === '' || this.matricule === '' || this.idRoom === '' || this.date === '' || this.start === '' || this.end === '' || this.motif === '') {
        alert("Veuillez remplir tous les champs avant d'envoyer la demande.");
        return;
      }

      // Créer un objet avec les données
      const reservation = {
        nom: this.nom,
        prenom: this.prenom,
        matricule: this.matricule,
        idRoom: this.idRoom,
        date: this.date,
        start: this.start,
        end: this.end,
        motif: this.motif
      };

      // Envoyer les données à votre backend via une requête HTTP POST
      fetch('http://localhost:1937/addRoomReservationRequest', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(reservation)
      })
          .then(response => {
            if (response.ok) {
              alert("Demande envoyée avec succès !");
            } else {
              alert("Erreur lors de l'envoi de la demande !");
            }
          })
          .catch(error => console.error('Erreur :', error));
    }
  }
};
</script>

<style>
* {
  padding: 0%;
  margin: 0%;
}

header {
  background-color: rgba(187, 33, 33, 0.877);
  width: 100%;
  height: 100px;
}

div {
  padding: 10px;
  font-style: italic;
  font-weight: 300px;
  font-size: 20px;
}

div input {
  width: 200px;
  border-radius: 10px;
  height: 20px;
  text-align: center;
  box-shadow: 5px 4px 5px rgba(0, 0, 0, 0.5);
  border: 2px solid black;
  display: flex;
  flex-direction: column;
  margin-top: 5px;
  transform: translateX(130px);
}

.container {
  transform: translateX(700px);
  padding: 50px;
  border: 2px solid black;
  width: 500px;
  text-align: center;
  margin-top: 100px;
  box-shadow: 15px 4px 10px rgba(19, 18, 18, 0.5);
}

#buttonEnvoyer {
  background-color: white;
  height: 30px;
  width: 100px;
  border-radius: 10px;
  text-align: center;
  font-style: italic;
  box-shadow: 5px 4px 5px rgba(0, 0, 0, 0.5);
}

#buttonEnvoyer:hover {
  background-color: brown;
  color: white;
}

body {
  background-image: url("../../../assets/bgHome.png");
  background-size: cover; /* Pour couvrir tout l'arrière-plan */
  background-position: center; /* Pour centrer l'image */
  background-repeat: no-repeat; /* Pour éviter la répétition de l'image */
}

main article {
  background-color: rgb(255, 255, 255);
}
</style>
