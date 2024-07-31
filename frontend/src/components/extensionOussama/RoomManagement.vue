<template>
  <div class="container mt-5">
    <h1>Room Management System</h1>

    <!-- Form to Add Room -->
    <form @submit.prevent="addRoom" class="mb-4">
      <h2>Add Room</h2>
      <div class="form-group">
        <label for="associatedBuilding">Associated Building:</label>
        <select v-model="newRoom.associatedBuilding" id="associatedBuilding" class="form-control" required>
          <option v-for="building in buildings" :key="building" :value="building">{{ building }}</option>
        </select>
      </div>
      <div class="form-group">
        <label for="type">Type:</label>
        <select v-model="newRoom.type" id="type" class="form-control" required>
          <option value="OFFICE">Office</option>
          <option value="CLASSROOM">Classroom</option>
          <option value="CONFERENCE_ROOM">Conference Room</option>
          <option value="LABORATORY">Laboratory</option>
        </select>
      </div>
      <div class="form-group">
        <label for="capacity">Capacity:</label>
        <input type="number" v-model="newRoom.capacity" id="capacity" class="form-control" required>
      </div>
      <div class="form-group">
        <label for="status">Status:</label>
        <select v-model="newRoom.status" id="status" class="form-control" required>
          <option value="AVAILABLE">Available</option>
          <option value="UNAVAILABLE">Unavailable</option>
          <option value="OCCUPIED">Occupied</option>
          <option value="RESERVED">Reserved</option>
          <option value="MAINTENANCE">Maintenance</option>
        </select>
      </div>
      <div class="form-group">
        <label for="equipement">Equipments:</label>
        <select v-model="newRoom.equipment" id="equipement" class="form-control" multiple>
          <option value="PROJECTOR">Projector</option>
          <option value="WHITEBOARD">Whiteboard</option>
          <option value="COMPUTER">Computer</option>
          <option value="SPEAKERS">Speakers</option>
          <option value="TELEPHONE">Telephone</option>
          <option value="OTHER">Other</option>
        </select>
      </div>
      <div class="form-group">
        <label for="available">Available:</label>
        <select v-model="newRoom.available" id="available" class="form-control">
          <option value="true">Yes</option>
          <option value="false">No</option>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">Add Room</button>
    </form>

    <hr>

    <!-- Form to Filter Rooms -->
    <h2>Filter Rooms</h2>
    <form @submit.prevent="filterRooms" class="mb-4">
      <div class="form-group">
        <label for="filterBuilding">Building:</label>
        <select v-model="filter.building" id="filterBuilding" class="form-control">
          <option value="">All Buildings</option>
          <option v-for="building in buildings" :key="building" :value="building">{{ building }}</option>
        </select>
      </div>
      <div class="form-group">
        <label for="filterType">Type:</label>
        <select v-model="filter.type" id="filterType" class="form-control">
          <option value="">All Types</option>
          <option value="OFFICE">Office</option>
          <option value="CLASSROOM">Classroom</option>
          <option value="CONFERENCE_ROOM">Conference Room</option>
          <option value="LABORATORY">Laboratory</option>
        </select>
      </div>
      <button type="submit" class="btn btn-secondary">Filter</button>
    </form>

    <hr>

    <h2>Rooms List</h2>
    <div class="row">
      <div v-for="room in rooms" :key="room.name" class="col-md-4">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">{{ room.name }}</h5>
            <p class="card-text">Building: {{ room.associatedBuilding }}</p>
            <p class="card-text">Type: {{ room.type }}</p>
            <p class="card-text">Capacity: {{ room.capacity }}</p>
            <p class="card-text">Status: {{ room.status }}</p>
            <p class="card-text">Equipment: {{ room.equipment ? room.equipment.join(', ') : 'None' }}</p>
            <button @click="toggleRoomStatus(room)" class="btn btn-secondary">
              Set Status to {{ room.status === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import $ from 'jquery';

export default {
  name: 'RoomManagement',
  setup() {
    const buildings = ref([]);
    const newRoom = ref({
      associatedBuilding: '',
      type: 'OFFICE',
      capacity: 0,
      status: 'AVAILABLE',
      equipment: [],
      available: 'true'
    });
    const filter = ref({
      building: '',
      type: ''
    });
    const rooms = ref([]);

    onMounted(() => {
      fetch('http://localhost:1937/api/buildings')
          .then(response => response.json())
          .then(data => {
            buildings.value = data;
            buildings.value.push('All Buildings');
            initializeSelect2();
          })
          .catch(error => console.error('Error fetching buildings:', error));

      fetchRooms();
    });

    const addRoom = () => {
      fetch('http://localhost:1937/api/rooms', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(newRoom.value)
      })
          .then(response => response.json())
          .then(data => {
            if (data.message === "Room added successfully") {
              newRoom.value = {
                associatedBuilding: '',
                type: 'OFFICE',
                capacity: 0,
                status: 'AVAILABLE',
                equipment: [],
                available: 'true'
              };
              fetchRooms();
            } else {
              alert(data.message);
            }
          })
          .catch(error => console.error('Error:', error));
    };

    const filterRooms = () => {
      fetchRooms();
    };

    const fetchRooms = () => {
      let url = `http://localhost:1937/api/rooms`;
      if (filter.value.building && filter.value.building !== 'All Buildings') {
        url += `?building=${filter.value.building}`;
      }
      if (filter.value.type) {
        url += url.includes('?') ? `&type=${filter.value.type}` : `?type=${filter.value.type}`;
      }

      fetch(url)
          .then(response => response.json())
          .then(data => {
            rooms.value = data;
          })
          .catch(error => console.error('Error fetching rooms:', error));
    };

    const toggleRoomStatus = (room) => {
      const newStatus = room.status === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE';
      fetch(`http://localhost:1937/api/rooms/update/${room.name}/status`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ status: newStatus })
      })
          .then(response => response.json())
          .then(updatedRoom => {
            room.status = updatedRoom.status;
          })
          .catch(error => console.error('Error:', error));
    };

    const initializeSelect2 = () => {
      setTimeout(() => {
        $('#associatedBuilding').select2();
        $('#filterBuilding').select2();
        $('#equipement').select2();
      }, 100); // Delay to ensure elements are rendered
    };

    return {
      buildings,
      newRoom,
      filter,
      rooms,
      addRoom,
      filterRooms,
      toggleRoomStatus
    };
  }
};
</script>

<style scoped>
.card {
  margin-bottom: 20px;
}
.card-body {
  text-align: left;
}
.navbar-text {
  margin-left: auto;
}
</style>
