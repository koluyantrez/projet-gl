<template>
  <div id="dashboard">
    <div class="sidebar">
      <h2>Admin Dashboard</h2>
      <ul>
        <li><router-link to="/dashboardAdmin"><i class="fas fa-home"></i> Tableau de bord</router-link></li>
        <li><router-link to="/roomsManagement"><i class="fas fa-door-open"></i> Gestion des Salles</router-link></li>
        <li><router-link to="/buildingManage"><i class="fas fa-building"></i> Gestion des Bâtiments</router-link></li>
        <li><router-link to="/userManage"><i class="fas fa-users"></i> Gestion des Utilisateurs</router-link></li>
        <li><router-link to="/officeManage"><i class="fas fa-briefcase"></i> Gestion des Bureaux</router-link></li>
        <li><router-link to="/settings"><i class="fas fa-cog"></i> Paramètres</router-link></li>
        <li><router-link to="/" class="logout"><i class="fas fa-sign-out-alt"></i> Déconnexion</router-link></li>
      </ul>
    </div>
    <div class="content">
      <div class="header">
        <img :src="adminPhoto" alt="Photo" class="photo" v-if="adminPhoto">
        <span style="color: black">{{ name }}</span>
      </div>
      <div class="contacts">
        <h3>Contacts des Services</h3>
        <ul>
          <li>Secrétariat: <a href="mailto:secretaire@exemple.com">secretaire@exemple.com</a></li>
          <li>Service d'inscription: <a href="mailto:inscription@exemple.com">inscription@exemple.com</a></li>
          <li>Service technique: <a href="mailto:technique@exemple.com">technique@exemple.com</a></li>
        </ul>
      </div>
      <div class="welcome">
        <h2>Bienvenue, {{ name }}!</h2>
        <p>Choisissez une option dans le menu à gauche pour commencer.</p>
      </div>
    </div>
  </div>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: 'DashboardAdmin',
  data() {
    return {
      name: '',
      adminPhoto: ''
    };
  },
  created() {
    const adminMatricule = Cookies.get('matriculeAdmin'); // Assurez-vous que le matricule est stocké dans les cookies lors de la connexion
    if (adminMatricule) {
      this.fetchAdminInfo(adminMatricule);
    }
  },
  methods: {
    async fetchAdminInfo(id) {
      try {
        // Fetch admin details and image
        const response = await fetch(`http://localhost:1937/api/admin/${id}`);
        if (!response.ok) throw new Error(`Erreur HTTP: ${response.status}`);
        const admin = await response.json();
        this.name = admin.name;

        // Convert image base64 to object URL
        if (admin.image) {
          this.adminPhoto = `data:image/jpeg;base64,${admin.image}`;
        }
      } catch (error) {
        console.error('Erreur lors de la récupération des informations de l\'admin :', error);
      }
    }
  }
};
</script>

<style scoped>
#dashboard {
  display: flex;
}
.sidebar {
  width: 250px;
  background: #2d2d2d;
  color: #fff;
  padding: 20px;
  height: 100vh;
}
.sidebar h2 {
  color: #fff;
  text-align: center;
  margin-bottom: 20px;
}
.sidebar ul {
  list-style: none;
  padding: 0;
}
.sidebar li {
  margin: 15px 0;
}
.sidebar a {
  color: #fff;
  text-decoration: none;
  display: flex;
  align-items: center;
}
.sidebar a i {
  margin-right: 10px;
}
.logout {
  color: red;
}
.content {
  flex: 1;
  padding: 20px;
}
.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  background-color: white;

}
.header .photo {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}
.contacts {
  margin: 20px 0;
  background: #f0f0f0;
  padding: 10px;
  border-radius: 5px;
}
.contacts h3 {
  margin-top: 0;
  color: #fff;
  background-color: #00bcd4;
  padding: 10px;
  border-radius: 5px 5px 0 0;
}
.contacts ul {
  list-style: none;
  padding: 0;
}
.contacts li {
  margin: 5px 0;
}
.welcome {
  margin: 20px 0;
}
</style>
