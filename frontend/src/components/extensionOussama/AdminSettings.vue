<template>
  <div class="admin-settings">
    <h2>Paramètres de l'Administrateur</h2>
    <div>
      <label for="address">Adresse</label>
      <input type="text" id="address" v-model="address" placeholder="Entrer l'adresse">
      <button @click="updateAddress">Mettre à jour</button>
    </div>
    <div>
      <label for="phone">Numéro de téléphone</label>
      <input type="text" id="phone" v-model="phone" placeholder="Entrer le numéro de téléphone">
      <button @click="updatePhoneNumber">Mettre à jour</button>
    </div>
    <div>
      <label for="password">Nouveau mot de passe</label>
      <input type="password" id="password" v-model="newPassword" placeholder="Entrer le nouveau mot de passe">
    </div>
    <div>
      <label for="confirm-password">Confirmer le mot de passe</label>
      <input type="password" id="confirm-password" v-model="confirmNewPassword" placeholder="Confirmer le mot de passe">
    </div>
    <button @click="updatePassword">Mettre à jour le mot de passe</button>
  </div>
</template>


<script>
export default {
  data() {
    return {
      address: '',
      phone: '',
      password: '',
      confirmPassword: ''
    };
  },
  methods: {
    async updateAddress() {
      const adminId = this.getCookie('loginUser');
      try {
        const response = await fetch(`http://localhost:1937/api/admin/${adminId}/address`, {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.address) // Envoie la nouvelle adresse sous forme de chaîne de caractères
        });

        if (!response.ok) {
          throw new Error("Erreur lors de la mise à jour de l'adresse");
        }
        alert("Adresse mise à jour avec succès !");
      } catch (error) {
        console.error("Erreur lors de la mise à jour:", error);
        alert("Échec de la mise à jour de l'adresse.");
      }
    },
    async updatePhoneNumber() {
      const adminId = this.getCookie('loginUser');
      try {
        const response = await fetch(`http://localhost:1937/api/admin/${adminId}/phone`, {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.phone) // Envoie le nouveau numéro de téléphone sous forme de chaîne de caractères
        });

        if (!response.ok) {
          throw new Error("Erreur lors de la mise à jour du numéro de téléphone");
        }
        alert("Numéro de téléphone mis à jour avec succès !");
      } catch (error) {
        console.error("Erreur lors de la mise à jour:", error);
        alert("Échec de la mise à jour du numéro de téléphone.");
      }
    },

    async updatePassword() {
      const adminId = this.getCookie('loginUser');
      if (this.newPassword !== this.confirmNewPassword) {
        alert("Les mots de passe ne correspondent pas.");
        return;
      }

      try {
        const response = await fetch(`http://localhost:1937/users/${adminId}/password?newPassword=${encodeURIComponent(this.newPassword)}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          }
        });
        if (response.ok){window.location.reload();}
        if (!response.ok) {
          throw new Error("Erreur lors de la mise à jour du mot de passe");
        }
        alert("Mot de passe mis à jour avec succès !");
      } catch (error) {
        console.error("Erreur lors de la mise à jour:", error);
        alert("Échec de la mise à jour du mot de passe.");
      }
    },
    getCookie(name) {
      const value = `; ${document.cookie}`;
      const parts = value.split(`; ${name}=`);
      if (parts.length === 2) return parts.pop().split(';').shift();
    }
  }
};
</script>
<style scoped>
.admin-settings {
  background-color: #f0f0f0;
  padding: 20px;
  border-radius: 5px;
  width: 600px;
  margin: auto;
  text-align: center;
}

.admin-settings h2 {
  background-color: #007bff;
  color: white;
  padding: 10px;
  border-radius: 5px 5px 0 0;
  margin-top: 0;
}

.admin-settings div {
  margin-bottom: 10px;
}

.admin-settings label {
  display: block;
  margin-bottom: 5px;
}

.admin-settings input {
  width: calc(100% - 20px);
  padding: 5px;
  margin-bottom: 10px;
  border-radius: 3px;
  border: 1px solid #ccc;
}

.admin-settings button {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.admin-settings button:hover {
  background-color: #0056b3;
}
</style>
