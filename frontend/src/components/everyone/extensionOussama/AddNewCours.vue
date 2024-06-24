<template>
  <div>
    <header>
      <img src="@/assets/illumis.png" id="logoUmons" style="height: 100px; background-color: white;">
    </header>

    <main>
      <article class="container">
        <div>
          - Nom du cours : <input type="text" v-model="nomCours">
        </div>
        <div>
          - Filière : <input type="text" v-model="filiere">
        </div>
        <div>
          - Département : <input type="text" v-model="departement">
        </div>
        <div>
          - Nom de l'enseignant : <input type="text" v-model="teacherName">
        </div>
        <button type="submit" id="buttonEnvoyer" @click="submitForm">Envoyer</button>
      </article>
    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      id: '',
      nomCours: '',
      filiere: '',
      departement: '',
      teacherName: ''
    };
  },
  methods: {
    submitForm() {
      // Vérifier si tous les champs sont remplis
      if (
          this.nomCours === '' ||
          this.filiere === '' ||
          this.departement === '' ||
          this.teacherName === ''
      ) {
        alert("Veuillez remplir tous les champs avant d'envoyer le formulaire.");
        return;
      }

      // Créer un objet avec les données du cours
      const nouveauCours = {
        name: this.nomCours,
        filiere: this.filiere,
        departement: this.departement,
        teacherName: this.teacherName,
      };

      axios.post('http://localhost:1937/AddNewCours', nouveauCours)
          .then(response => {
            if (response.status === 200) {
              alert("Le cours a été ajouté avec succès !");
            } else if (response.status === 400) {
              if (response.data === 'Cours already exists') {
                alert("Le cours existe déjà !");
              } else if (response.data === 'Teacher not found') {
                alert("Le professeur n'a pas été trouvé !");
              } else {
                alert("Erreur lors de l'ajout du cours !");
              }
            } else {
              alert("Erreur lors de l'ajout du cours !");
            }
          })
          .catch(error => {
            console.error('Erreur :', error);
            alert("Erreur lors de l'ajout du cours !");
          });
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
  font-weight: 300;
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
