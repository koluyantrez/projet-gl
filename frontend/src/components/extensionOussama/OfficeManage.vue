<template>
  <div>
    <!-- Header professionnel -->
    <header>
      <div class="header-content">
        <h1>Gestion des Bureaux</h1>
        <button class="back-btn" @click="goBack">Revenir en arrière</button>
      </div>
    </header>

    <table id="office-table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Nom</th>
        <th>Emplacement</th>
        <th>Personnel associé</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="office in offices" :key="office.id">
        <td>{{ office.id }}</td>
        <td>{{ office.name }}</td>
        <td>{{ office.location }}</td>
        <td>{{ office.associatedPersonnel || 'Aucun' }}</td>
        <td>
          <button class="assign-btn" @click="openAssignPersonnelModal(office)">
            {{ office.associatedPersonnel ? 'Modifier' : 'Attribuer' }}
          </button>
          <span v-if="office.pendingUpdate" class="pending-update">En attente de validation</span>
          <span v-if="office.pendingDeletion" class="pending-deletion">En attente de suppression</span>
        </td>
      </tr>
      </tbody>
    </table>

    <div id="assign-personnel-modal" v-if="showModal" @click.self="closeModal">
      <div class="modal-content">
        <span class="close-btn" @click="closeModal">&times;</span>
        <h2>Attribuer un personnel</h2>
        <input type="text" id="personnel-search" v-model="personnelSearchInput" @input="loadPersonnels" placeholder="Rechercher un personnel" />
        <ul id="personnel-suggestions">
          <li v-for="personnel in personnelSuggestions" :key="personnel.id" @click="selectPersonnel(personnel)">
            {{ personnel.name }}
          </li>
        </ul>
        <button id="assign-personnel-btn" @click="assignPersonnel">Attribuer</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      offices: [],
      showModal: false,
      personnelSearchInput: '',
      personnelSuggestions: [],
      currentOffice: null,
      selectedPersonnel: null,
    };
  },
  mounted() {
    this.loadOffices();
  },
  methods: {
    async loadOffices() {
      try {
        const response = await fetch('http://localhost:1937/api/offices');
        console.log(response);
        if (!response.ok) throw new Error('Erreur lors de la récupération des bureaux.');
        this.offices = await response.json();
      } catch (error) {
        console.error('Erreur:', error);
      }
    },
    openAssignPersonnelModal(office) {
      this.showModal = true;
      this.currentOffice = office;
      this.personnelSearchInput = '';
      this.personnelSuggestions = [];
      this.selectedPersonnel = null;
    },
    closeModal() {
      this.showModal = false;
      this.currentOffice = null;
    },
    async loadPersonnels() {
      try {
        const response = await fetch(`http://localhost:1937/api/personnel?query=${encodeURIComponent(this.personnelSearchInput)}`);
        if (!response.ok) throw new Error('Erreur lors de la récupération des personnels.');
        this.personnelSuggestions = await response.json();
      } catch (error) {
        console.error('Erreur:', error);
      }
    },
    selectPersonnel(personnel) {
      this.selectedPersonnel = personnel;
      this.personnelSearchInput = personnel.name;
      this.personnelSuggestions = [];
    },
    async assignPersonnel() {
      if (!this.selectedPersonnel) {
        alert('Veuillez sélectionner un personnel.');
        return;
      }

      try {
        const updateResponse = await fetch(`http://localhost:1937/api/offices/${encodeURIComponent(this.currentOffice.name)}/occupant?newOccupant=${encodeURIComponent(this.selectedPersonnel.name)}`, {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json'
          }
        });

        if (updateResponse.ok) {
          this.closeModal();
          this.loadOffices();
        } else {
          throw new Error('Erreur lors de l\'attribution du personnel.');
        }
      } catch (error) {
        console.error('Erreur:', error);
      }
    },
    goBack() {
      window.history.back();
    }
  }
};
</script>

<style scoped>
/* Header professionnel */
header {
  background-color: #004d99;
  color: white;
  padding: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 1rem;
}

.header-content h1 {
  margin: 0;
  font-size: 1.5rem;
  color: white;
}

.back-btn {
  background-color: #ff4d4d;
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

.back-btn:hover {
  background-color: #cc0000;
}
#assign-personnel-modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 30%;
}

.close-btn {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close-btn:hover,
.close-btn:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

.pending-update,
.pending-deletion {
  margin-left: 10px;
  color: orange;
}

#personnel-suggestions {
  list-style-type: none;
  padding: 0;
  margin: 0;
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
}

#personnel-suggestions li {
  padding: 10px;
  cursor: pointer;
}

#personnel-suggestions li:hover {
  background-color: #f0f0f0;
}

#assign-personnel-btn {
  background-color: #004d99;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

#assign-personnel-btn:hover {
  background-color: #003366;
}
/* Général */
body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
  margin: 0;
  padding: 0;
  background-color: #f4f4f4;
}

/* En-tête */
header {
  background-color: #003366;
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

header h1 {
  margin: 0;
  font-size: 1.75rem;
}

header a {
  color: white;
  text-decoration: none;
  font-weight: bold;
  margin-left: 1rem;
  font-size: 1rem;
}

header a:hover {
  text-decoration: underline;
}

/* Contenu principal */
main {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

/* Tableaux */
table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 2rem;
  background-color: white;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

table th, table td {
  padding: 1rem;
  border: 1px solid #ddd;
  text-align: left;
}

table th {
  background-color: #003366;
  color: white;
  font-size: 1.1rem;
}

table tr:nth-child(even) {
  background-color: #f9f9f9;
}

table tr:hover {
  background-color: #e0e0e0;
}

/* Formulaires */
form {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-top: 2rem;
}

form label {
  display: block;
  margin: 0.5rem 0;
}

form input[type="text"],
form input[type="hidden"] {
  width: calc(100% - 2rem);
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

form button {
  background-color: #003366;
  color: white;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  margin-top: 1rem;
}

form button:hover {
  background-color: #002244;
}

/* Détails du Bureau */
#office-detail {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* Boutons de suppression */
.delete-button {
  background-color: #ff4d4d;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #cc0000;
}

/* Notifications de suppression */
.pending-deletion {
  background-color: #ffeb3b;
  color: #333;
  padding: 0.5rem;
  border-radius: 4px;
  font-weight: bold;
}

/* Styles pour le formulaire d'attribution de personnel */
.assign-personnel-form {
  margin-top: 1rem;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.assign-personnel-form label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.assign-personnel-form select,
.assign-personnel-form button {
  display: block;
  width: 100%;
  margin-top: 0.5rem;
}

.assign-personnel-form select {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

.assign-personnel-form button {
  background-color: #003366;
  color: white;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  margin-top: 0.5rem;
}

.assign-personnel-form button:hover {
  background-color: #002244;
}

/* Visibilité mobile */
@media (max-width: 768px) {
  header {
    flex-direction: column;
    align-items: flex-start;
  }

  header a {
    margin-left: 0;
    margin-top: 0.5rem;
  }

  table th, table td {
    padding: 0.5rem;
  }

  form input[type="text"],
  form input[type="hidden"] {
    width: calc(100% - 1.5rem);
  }

  form button {
    width: auto;
  }
}

/* Styles pour la modal de suggestion */
.modal {
  display: none;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgb(0,0,0);
  background-color: rgba(0,0,0,0.4);
  padding-top: 60px;
}

.modal-content {
  background-color: #fefefe;
  margin: 5% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
  max-width: 600px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.close-btn {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close-btn:hover,
.close-btn:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

#personnel-search {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.suggestions-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
}

.suggestions-list li {
  padding: 10px;
  cursor: pointer;
}

.suggestions-list li:hover {
  background-color: #f0f0f0;
}

#assign-personnel-btn {
  background-color: #004d99;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

#assign-personnel-btn:hover {
  background-color: #003366;
}

</style>
