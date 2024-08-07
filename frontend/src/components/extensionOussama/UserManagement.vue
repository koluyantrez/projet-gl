<template>
  <div>
    <div class="container">
      <h1>Gestion des Utilisateurs</h1>
      <form @submit.prevent="filterUsers" id="filterForm">
        <input
            list="categories"
            v-model="selectedCategory"
            placeholder="Catégorie"
            class="filter-select"
        />
        <datalist id="categories">
          <option v-for="cat in categories" :key="cat" :value="cat">{{ formatOption(cat) }}</option>
        </datalist>

        <input
            list="departments"
            v-model="selectedDepartment"
            placeholder="Département"
            class="filter-select"
        />
        <datalist id="departments">
          <option v-for="dep in departments" :key="dep" :value="dep">{{ formatOption(dep) }}</option>
        </datalist>

        <button type="submit" class="filter-button">Rechercher</button>
      </form>
      <div class="user-list" id="personnelList">
        <div
            v-for="user in users"
            :key="user.matricule"
            class="user-card"
            @click="showDetails(user)"
        >
          <h2>{{ user.name }}</h2>
          <p class="user-category">Catégorie: {{ user.categorie }}</p>
          <p class="user-department">Département: {{ user.departement || 'Non spécifié' }}</p>
        </div>
      </div>
    </div>

    <div v-if="modalVisible" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <div class="modal-body">
          <div class="user-image-container">
            <img v-if="selectedUser.image" :src="'data:image/jpeg;base64,' + selectedUser.image" alt="Photo de l'utilisateur" class="user-image" />
          </div>
          <div class="user-info">
            <h2>Nom: {{ selectedUser.name || 'Non spécifié' }}</h2>
            <p>Adresse: {{ selectedUser.adresse || 'Non spécifiée' }}</p>
            <p>Catégorie: {{ selectedUser.categorie || 'Non spécifiée' }}</p>
            <p>Numéro: {{ selectedUser.numero || 'Non spécifié' }}</p>
            <p>Département: {{ selectedUser.departement || 'Non spécifié' }}</p>
            <p>Email: {{ selectedUser.email || 'Non spécifié' }}</p>
            <p>Matricule: {{ selectedUser.matricule || 'Non spécifié' }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      users: [],
      categories: [],
      departments: [],
      selectedCategory: '',
      selectedDepartment: '',
      modalVisible: false,
      selectedUser: {},
      image : null,
    };
  },
  created() {
    this.loadCategories();
    this.loadDepartments();
    this.loadPersonnel();
  },
  methods: {
    formatOption(value) {
      return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
    },
    filterUsers() {
      console.log('Filter button clicked');
      console.log('Selected Category:', this.selectedCategory);
      console.log('Selected Department:', this.selectedDepartment);
      this.loadPersonnel();
    },
    async loadPersonnel() {
      try {
        const category = this.selectedCategory;
        const department = this.selectedDepartment;
        let url = 'http://localhost:1937/api/personnel/search';

        const queryParams = [];
        if (category) queryParams.push(`category=${encodeURIComponent(category)}`);
        if (department) queryParams.push(`department=${encodeURIComponent(department)}`);
        if (queryParams.length > 0) {
          url += '?' + queryParams.join('&');
        }

        console.log('Fetching personnel with URL:', url);
        const response = await fetch(url);

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const personnel = await response.json();
        console.log('Fetched personnel:', personnel);
        this.users = personnel;
      } catch (error) {
        console.error('Error fetching personnel:', error);
      }
    },
    async loadCategories() {
      try {
        console.log('Fetching categories');
        const response = await fetch('http://localhost:1937/api/personnel/categories');

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const categories = await response.json();
        console.log('Fetched categories:', categories);
        this.categories = categories;
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    },
    async loadDepartments() {
      try {
        console.log('Fetching departments');
        const response = await fetch('http://localhost:1937/api/personnel/departments');

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const departments = await response.json();
        console.log('Fetched departments:', departments);
        this.departments = departments;
      } catch (error) {
        console.error('Error fetching departments:', error);
      }
    },
    async showDetails(user) {
      try {
        console.log('Fetching details for user:', user.matricule);
        const response = await fetch(`http://localhost:1937/users/${user.matricule}`);

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const personnel = await response.json();
        console.log('Fetched user details:', personnel);
        this.selectedUser = personnel;
        this.modalVisible = true;
      } catch (error) {
        console.error('Error fetching personnel details:', error);
      }
    },
    closeModal() {
      console.log('Closing modal');
      this.modalVisible = false;
    }
  }
};
</script>

<style scoped>
/* Your existing CSS styles */
</style>


<style scoped>
body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f7f9fc;
  margin: 0;
  padding: 0;
}

.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 25px;
  text-align: center;
}

h1 {
  margin-bottom: 35px;
  font-size: 2.5em;
  color: #333;
}

form {
  margin-bottom: 30px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.filter-select {
  padding: 10px;
  font-size: 1em;
  border-radius: 5px;
  border: 1px solid #ddd;
  background-color: #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.filter-button {
  padding: 10px 20px;
  font-size: 1em;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.filter-button:hover {
  background-color: #0056b3;
}

.user-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}

.user-card {
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  width: 300px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: box-shadow 0.3s, transform 0.3s;
}

.user-card:hover {
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
  transform: scale(1.02);
}

.user-category,
.user-department {
  font-size: 1em;
  color: #555;
}

.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
}

.modal-content {
  background-color: #fff;
  margin: 5% auto;
  padding: 20px;
  border: 1px solid #ddd;
  width: 80%;
  max-width: 600px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  position: relative;
}

.modal-body {
  display: flex;
  gap: 20px;
  align-items: flex-start;

}

.user-image-container {
  flex: 1;
}

.user-info {
  flex: 2;
}

.close {
  color: #888;
  float: right;
  font-size: 24px;
  font-weight: bold;
  cursor: pointer;
}

.close:hover {
  color: #333;
}

.modal-content h2 {
  margin-top: 0;
  font-size: 1.5em;
  color: #333;
}

.modal-content p {
  font-size: 1em;
  color: #666;
  margin: 5px 0;
}

.user-image {
  max-width: 100%;
  border-radius: 10px;
  border: 2px solid black;
}
</style>
