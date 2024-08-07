<template>
  <div class="info">
    <p>{{ "name : " + name }}</p>
    <p>{{ "id : " + matricule }}</p>
    <p>{{ "email : " + email }}</p>
    <p>{{ "adresse : " + adresse }}</p>
    <p>{{ "numero : " + numero }}</p>
  </div>

  <img :src="src" alt="Profil" class="i"/>
</template>

<script>
import Cookies from "js-cookie";
import axios from "axios";

export default {
  data() {
    return {
      src: require('../assets/profil.png'),
      name: '',
      matricule: '',
      email: '',
      adresse: '',
      numero: ''
    }
  },
  created() {
    this.getInfo();
  },
  methods: {
    getInfo() {
      const matricule = Cookies.get('matriculeInscription');
      if (!matricule) {
        // Gérer le cas où le matricule n'est pas présent dans les cookies
        console.log("matricule n'est pas dans les cookies");
        return;
      }
      axios.get('http://localhost:1937/inscription/membreServiceInscription?matricule=' + matricule)
          .then(response => {
            const membre = response.data;
            this.name = membre.name;
            this.matricule = membre.matricule;
            this.email = membre.email;
            this.adresse = membre.adresse;
            this.numero = membre.numero;
          })
          .catch(error => {
            console.error(error);
          });
    }
  }
}
</script>

<style scoped>
.w {
  position: absolute;
  left: 110px;
  top: 70px;
}

.i {
  position: absolute;
  left: 90px;
  bottom: 350px;
  width: 300px;
  height: 300px;
}

.info {
  position: absolute;
  left: 530px;
  bottom: 300px;
  color: rgb(158, 11, 23);
  font-family: Roboto, sans-serif;
  font-size: 30px;
  transform: translateY(30px);
}

h1 {
  color: rgb(158, 11, 23);
  font-family: Roboto, sans-serif;
  font-weight: 450;
  font-size: 100px;
}
</style>