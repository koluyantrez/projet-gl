<template>
  <div>
    <!-- Header professionnel -->
    <header>
      <div class="header-content">
        <h1>Room Management System</h1>
        <button class="back-btn" @click="goBack">Revenir en arrière</button>
      </div>
    </header>

    <div class="container">
      <h2>Add Room</h2>
      <form @submit.prevent="addRoom">
        <div class="form-group">
          <label for="associatedBuilding">Associated Building:</label>
          <input
              type="text"
              id="associatedBuilding"
              v-model="newRoom.associatedBuilding"
              @input="onBuildingInput"
              @blur="hideSuggestions"
              placeholder="Type to search for building"
              autocomplete="off"
          />
          <ul v-if="showSuggestions && filteredBuildings.length" class="suggestions">
            <li
                v-for="building in filteredBuildings"
                :key="building"
                @mousedown.prevent="selectBuilding(building)"
            >
              {{ building }}
            </li>
          </ul>
        </div>
        <div class="form-group">
          <label for="type">Type:</label>
          <input
              type="text"
              id="type"
              v-model="newRoom.type"
              @input="onTypeInput"
              @blur="hideTypeSuggestions"
              placeholder="Type to search for room type"
              autocomplete="off"
          />
          <ul v-if="showTypeSuggestions && filteredTypes.length" class="suggestions">
            <li
                v-for="type in filteredTypes"
                :key="type"
                @mousedown.prevent="selectType(type)"
            >
              {{ type }}
            </li>
          </ul>
        </div>
        <div class="form-group">
          <label for="capacity">Capacity:</label>
          <input type="text" id="capacity" v-model="newRoom.capacity" placeholder="Enter capacity">
        </div>
        <div class="form-group">
          <label for="status">Status:</label>
          <select id="status" v-model="newRoom.status">
            <option value="AVAILABLE">Available</option>
            <option value="UNAVAILABLE">Unavailable</option>
            <option value="OCCUPIED">Occupied</option>
            <option value="RESERVED">Reserved</option>
            <option value="MAINTENANCE">Maintenance</option>
          </select>
        </div>
        <div class="form-group">
          <label for="equipement">Equipment:</label>
          <select id="equipement" v-model="newRoom.equipement" multiple>
            <option v-for="item in equipmentTypes" :key="item" :value="item">
              {{ item }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label for="available">Available:</label>
          <select id="available" v-model="newRoom.available">
            <option value="yes">Yes</option>
            <option value="no">No</option>
          </select>
        </div>
        <button type="submit">Add Room</button>
      </form>

      <h2>Filter Rooms</h2>
      <form @submit.prevent="filterRooms">
        <div class="form-group">
          <label for="filterBuilding">Building:</label>
          <input
              type="text"
              id="filterBuilding"
              v-model="filter.associatedBuilding"
              @input="onFilterBuildingInput"
              @blur="hideFilterBuildingSuggestions"
              placeholder="Type to search for building"
              autocomplete="off"
          />
          <ul v-if="showFilterBuildingSuggestions && filteredBuildings.length" class="suggestions">
            <li
                v-for="building in filteredBuildings"
                :key="building"
                @mousedown.prevent="selectFilterBuilding(building)"
            >
              {{ building }}
            </li>
          </ul>
        </div>
        <div class="form-group">
          <label for="filterType">Type:</label>
          <input
              type="text"
              id="filterType"
              v-model="filter.type"
              @input="onFilterTypeInput"
              @blur="hideFilterTypeSuggestions"
              placeholder="Type to search for room type"
              autocomplete="off"
          />
          <ul v-if="showFilterTypeSuggestions && filteredTypes.length" class="suggestions">
            <li
                v-for="type in filteredTypes"
                :key="type"
                @mousedown.prevent="selectFilterType(type)"
            >
              {{ type }}
            </li>
          </ul>
        </div>
        <button type="submit">Filter</button>
      </form>

      <div id="roomList" class="row">
        <div v-for="room in rooms" :key="room.name" class="col-md-3">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title">{{ room.name }}</h5>
              <p class="card-text">Building: {{ room.associatedBuilding }}</p>
              <p class="card-text">Type: {{ room.type }}</p>
              <p class="card-text">Capacity: {{ room.capacity }}</p>
              <p class="card-text">Status: {{ room.status }}</p>
              <p class="card-text">Equipment: {{ room.equipement ? room.equipement.join(', ') : 'None' }}</p>
              <button @click="updateRoomStatus(room)">
                Set Status to {{ room.status === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE' }}
              </button>
            </div>
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
      newRoom: {
        associatedBuilding: '',
        type: 'OFFICE',
        capacity: '',
        status: 'AVAILABLE',
        equipement: [],
        available: 'yes',
      },
      filter: {
        associatedBuilding: '',
        type: '',
      },
      rooms: [],
      buildings: [],
      types : ['OFFICE', 'classroom', 'Laboratory', 'Workshop', 'Studio', 'Auditorium', 'Library', 'Gym', 'Cafeteria', 'Meeting Room', 'Computer Lab', 'Counseling Room'],
      equipmentTypes: [
        'PROJECTOR',
        'WHITEBOARD',
        'COMPUTER',
        'SPEAKERS',
        'TELEPHONE',
        'OTHER'
      ],
      showSuggestions: false,
      filteredBuildings: [],
      showTypeSuggestions: false,
      filteredTypes: [],
      showFilterBuildingSuggestions: false,
      showFilterTypeSuggestions: false,
    };
  },
  methods: {
    async addRoom() {
      try {
        const response = await fetch('http://localhost:1937/api/rooms', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            associatedBuilding: this.newRoom.associatedBuilding,
            type: this.newRoom.type,
            capacity: this.newRoom.capacity,
            status: this.newRoom.status,
            equipement: this.newRoom.equipement,
            available: this.newRoom.available === 'yes'
          })
        });
        const data = await response.json();
        if (data.message === "Room added successfully") {
          this.newRoom = {
            associatedBuilding: '',
            type: 'OFFICE',
            capacity: '',
            status: 'AVAILABLE',
            equipement: [],
            available: 'yes',
          };
          this.refreshRoomList();
        } else {
          alert(data.message);
        }
      } catch (error) {
        console.error('Error:', error);
      }
    },
    onBuildingInput() {
      this.showSuggestions = true;
      this.filteredBuildings = this.buildings.filter(building =>
          building.toLowerCase().includes(this.newRoom.associatedBuilding.toLowerCase())
      );
    },
    hideSuggestions() {
      setTimeout(() => {
        this.showSuggestions = false;
      }, 200);
    },
    selectBuilding(building) {
      this.newRoom.associatedBuilding = building;
      this.showSuggestions = false;
    },
    onTypeInput() {
      this.showTypeSuggestions = true;
      this.filteredTypes = this.types.filter(type =>
          type.toLowerCase().includes(this.newRoom.type.toLowerCase())
      );
    },
    hideTypeSuggestions() {
      setTimeout(() => {
        this.showTypeSuggestions = false;
      }, 200);
    },
    selectType(type) {
      this.newRoom.type = type;
      this.showTypeSuggestions = false;
    },
    onFilterBuildingInput() {
      this.showFilterBuildingSuggestions = true;
      this.filteredBuildings = this.buildings.filter(building =>
          building.toLowerCase().includes(this.filter.associatedBuilding.toLowerCase())
      );
    },
    hideFilterBuildingSuggestions() {
      setTimeout(() => {
        this.showFilterBuildingSuggestions = false;
      }, 200);
    },
    selectFilterBuilding(building) {
      this.filter.associatedBuilding = building;
      this.showFilterBuildingSuggestions = false;
    },
    onFilterTypeInput() {
      this.showFilterTypeSuggestions = true;
      this.filteredTypes = this.types.filter(type =>
          type.toLowerCase().includes(this.filter.type.toLowerCase())
      );
    },
    hideFilterTypeSuggestions() {
      setTimeout(() => {
        this.showFilterTypeSuggestions = false;
      }, 200);
    },
    selectFilterType(type) {
      this.filter.type = type;
      this.showFilterTypeSuggestions = false;
    },
    async filterRooms() {
      try {
        const query = [];
        if (this.filter.associatedBuilding) {
          query.push(`building=${encodeURIComponent(this.filter.associatedBuilding)}`);
        }
        if (this.filter.type) {
          query.push(`type=${encodeURIComponent(this.filter.type)}`);
        }
        const url = `http://localhost:1937/api/rooms/filtre?${query.join('&')}`;
        const response = await fetch(url);
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        this.rooms = data;
      } catch (error) {
        console.error('Error:', error);
      }
    },
    async updateRoomStatus(room) {
      try {
        const newStatus = room.status === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE';
        const response = await fetch(`http://localhost:1937/api/rooms/update/${room.name}/status`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({status: newStatus})
        });
        if (response.ok) {
          this.refreshRoomList();
        } else {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
      } catch (error) {
        console.error('Error:', error);
      }
    },
    async refreshRoomList() {
      try {
        const response = await fetch('http://localhost:1937/api/rooms');
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        this.rooms = data;
      } catch (error) {
        console.error('Error:', error);
      }
    },
    async fetchBuildings() {
      try {
        const response = await fetch('http://localhost:1937/api/buildings');
        const buildings = await response.json();
        this.buildings = buildings.map(b => b.name);
      } catch (error) {
        console.error('Error fetching buildings:', error);
      }
    },
    goBack() {
      window.history.back();
    },
  },
  mounted() {
    this.fetchBuildings();
    this.refreshRoomList();
  }
};
</script>

<style>
/* CSS Styles */

h1 {
  color: black;
  font-size: 30px;
}

body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
}

header {
  background-color: #007bff;
  color: #fff;
  padding: 10px;
  text-align: left;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.back-btn {
  background-color: #fff;
  color: #007bff;
  border: 1px solid #007bff;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 4px;
}

.container {
  max-width: 1100px;
  margin: 20px auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

input, select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  background-color: #007bff;
  color: #fff;
  border: none;
  padding: 10px 20px;
  margin: 10px;
  cursor: pointer;
  border-radius: 30px;
}

.suggestions {
  list-style-type: none;
  padding: 0;
  margin: 0;
  border: 1px solid #ccc;
  border-radius: 4px;
  max-height: 150px;
  overflow-y: auto;
}

.suggestions li {
  padding: 8px;
  cursor: pointer;
}

.suggestions li:hover {
  background-color: #f0f0f0;
}
</style>
