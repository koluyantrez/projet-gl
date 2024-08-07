<template>
  <div>
    <h1>Building Management System</h1>

    <div class="row">
      <div class="col-md-6">
        <h2>Add Building</h2>
        <form id="addBuildingForm" @submit.prevent="addBuilding">
          <div class="form-group">
            <label for="name">Name:</label>
            <input type="text" class="form-control" id="name" v-model="newBuilding.name" />
          </div>
          <div class="form-group">
            <label for="adresse">Address:</label>
            <input type="text" class="form-control" id="adresse" v-model="newBuilding.adresse" />
          </div>
          <div class="form-group">
            <label for="available">Available:</label>
            <select class="form-control" id="available" v-model="newBuilding.available">
              <option value="true">Yes</option>
              <option value="false">No</option>
            </select>
          </div>
          <button type="submit" class="btn btn-primary">Add Building</button>
        </form>
      </div>

      <div class="col-md-6">
        <h2>Search Building</h2>
        <div class="form-group">
          <label for="searchName">Enter building name:</label>
          <input type="text" class="form-control" id="searchName" v-model="searchName" />
          <button @click="searchBuilding" class="btn btn-primary">Search</button>
        </div>
        <div id="searchResult"></div>
      </div>
    </div>

    <h2>Buildings List</h2>
    <div class="row" id="buildingList">
      <div class="col-md-4" v-for="building in buildings" :key="building.name">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">{{ building.name }}</h5>
            <p class="card-text">Address: {{ building.adresse }}</p>
            <p class="card-text">Available: {{ building.available ? 'Yes' : 'No' }}</p>
            <p class="card-text">Number of Rooms: {{ building.numberOfRooms }}</p>
            <p class="card-text">Number of Offices: {{ building.numbersOfOffices }}</p>
            <button class="btn btn-secondary" @click="toggleAvailability(building)">
              Set Available to {{ !building.available }}
            </button>
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
      newBuilding: {
        name: '',
        adresse: '',
        available: 'true',
      },
      buildings: [],
      searchName: '',
    };
  },
  mounted() {
    this.fetchBuildings();
  },
  methods: {
    fetchBuildings() {
      fetch('http://localhost:1937/api/buildings')
          .then((response) => response.json())
          .then((buildings) => {
            this.buildings = buildings;
          })
          .catch((error) => {
            console.error('Error:', error);
          });
    },
    addBuilding() {
      fetch('http://localhost:1937/api/buildings', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          name: this.newBuilding.name,
          adresse: this.newBuilding.adresse,
          available: this.newBuilding.available === 'true',
          numberOfRooms: 0,
          numbersOfOffices: 0,
          allRooms: [],
          allOffices: [],
        }),
      })
          .then((response) => response.json())
          .then((data) => {
            this.buildings.push(data);
            this.newBuilding = {
              name: '',
              adresse: '',
              available: 'true',
            };
          })
          .catch((error) => {
            console.error('Error:', error);
          });
    },
    searchBuilding() {
      if (this.searchName) {
        fetch(`http://localhost:1937/api/buildings/name/${this.searchName}`)
            .then((response) => response.json())
            .then((building) => {
              if (building) {
                this.showSearchResult(building);
              } else {
                this.$el.querySelector('#searchResult').textContent = 'Building not found';
              }
            })
            .catch((error) => {
              console.error('Error:', error);
              this.$el.querySelector('#searchResult').textContent = 'Building not found';
            });
      }
    },
    toggleAvailability(building) {
      this.updateAvailability(building.name, !building.available)
          .then((updatedBuilding) => {
            building.available = updatedBuilding.available;
          })
          .catch((error) => {
            console.error('Error:', error);
          });
    },
    updateAvailability(buildingName, availability) {
      return fetch(`http://localhost:1937/api/buildings/${buildingName}/availability`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(availability),
      }).then((response) => response.json());
    },
    showSearchResult(building) {
      this.$el.querySelector('#searchResult').innerHTML = `
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">${building.name}</h5>
            <p class="card-text">Address: ${building.adresse}</p>
            <p class="card-text">Available: ${building.available ? 'Yes' : 'No'}</p>
            <p class="card-text">Number of Rooms: ${building.numberOfRooms}</p>
            <p class="card-text">Number of Offices: ${building.numbersOfOffices}</p>
          </div>
        </div>
      `;
    },
  },
};
</script>

<style>
.card {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

#searchResult {
  margin-top: 20px;
}

#buildingList {
  margin-top: 20px;
}
</style>