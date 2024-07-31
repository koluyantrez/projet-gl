document.addEventListener('DOMContentLoaded', function() {
    const addRoomForm = document.getElementById('addRoomForm');
    const filterForm = document.getElementById('filterForm');
    const roomList = document.getElementById('roomList');
    const associatedBuildingSelect = document.getElementById('associatedBuilding');
    const filterBuildingSelect = document.getElementById('filterBuilding');
    const filterTypeSelect = document.getElementById('filterType');

    // Initialize Select2 for equipment select field
    $('#equipement').select2({
        placeholder: 'Select equipment',
        allowClear: true
    });

    // Fetch buildings and populate the associatedBuilding and filterBuilding select options
    fetch('/api/buildings')
        .then(response => response.json())
        .then(buildings => {
            buildings.forEach(building => {
                const option = document.createElement('option');
                option.value = building.name;
                option.textContent = building.name;
                associatedBuildingSelect.appendChild(option);
                filterBuildingSelect.appendChild(option.cloneNode(true));
            });
            // Ensure "AllBuildings" option is present
            const allBuildingsOption = document.createElement('option');
            allBuildingsOption.value = 'AllBuildings';
            allBuildingsOption.textContent = 'All Buildings';
            filterBuildingSelect.appendChild(allBuildingsOption);
        })
        .catch(error => {
            console.error('Error fetching buildings:', error);
        });

    addRoomForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const associatedBuilding = document.getElementById('associatedBuilding').value;
        const type = document.getElementById('type').value;
        const capacity = document.getElementById('capacity').value;
        const status = document.getElementById('status').value;
        const equipement = Array.from(document.getElementById('equipement').selectedOptions).map(option => option.value);
        const available = document.getElementById('available').value === 'true';

        fetch('/api/rooms', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                associatedBuilding: associatedBuilding,
                type: type,
                capacity: capacity,
                status: status,
                equipement: equipement,
                available: available
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.message === "Room added successfully") {
                    addRoomForm.reset();
                    $('#equipement').val(null).trigger('change'); // Reset Select2 field
                    refreshRoomList(); // Refresh the room list
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

    filterForm.addEventListener('submit', function(event) {
        event.preventDefault();
        refreshRoomList();
    });

    function refreshRoomList() {
        const filterBuilding = filterBuildingSelect.value;
        const filterType = filterTypeSelect.value;

        roomList.innerHTML = ''; // Clear the existing room list

        let url;
        if (filterBuilding === 'AllBuildings') {
            url = `/api/rooms`; // Endpoint to get all rooms
        } else {
            url = `/api/rooms/filtre?building=${filterBuilding}&type=${filterType}`;
        }

        fetch(url)
            .then(response => response.json())
            .then(rooms => {
                rooms.forEach(room => {
                    showRoom(room);
                });
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function showRoom(room) {
        const col = document.createElement('div');
        col.className = 'col-md-4';

        const card = document.createElement('div');
        card.className = 'card';

        const cardBody = document.createElement('div');
        cardBody.className = 'card-body';

        const title = document.createElement('h5');
        title.className = 'card-title';
        title.textContent = room.name;

        const building = document.createElement('p');
        building.className = 'card-text';
        building.textContent = `Building: ${room.associatedBuilding}`;

        const type = document.createElement('p');
        type.className = 'card-text';
        type.textContent = `Type: ${room.type}`;

        const capacity = document.createElement('p');
        capacity.className = 'card-text';
        capacity.textContent = `Capacity: ${room.capacity}`;

        const status = document.createElement('p');
        status.className = 'card-text';
        status.textContent = `Status: ${room.status}`;

        const equipement = document.createElement('p');
        equipement.className = 'card-text';
        equipement.textContent = `Equipment: ${room.equipement.join(', ')}`;

        const updateStatusButton = document.createElement('button');
        updateStatusButton.className = 'btn btn-secondary';
        updateStatusButton.textContent = `Set Status to ${room.status === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE'}`;
        updateStatusButton.onclick = function() {
            const newStatus = room.status === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE';
            updateRoomStatus(room.name, newStatus)
                .then(updatedRoom => {
                    // Update the status text and button text directly
                    status.textContent = `Status: ${updatedRoom.status}`;
                    updateStatusButton.textContent = `Set Status to ${updatedRoom.status === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE'}`;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        };

        cardBody.appendChild(title);
        cardBody.appendChild(building);
        cardBody.appendChild(type);
        cardBody.appendChild(capacity);
        cardBody.appendChild(status);
        cardBody.appendChild(equipement);
        cardBody.appendChild(updateStatusButton);

        card.appendChild(cardBody);
        col.appendChild(card);

        roomList.appendChild(col);
    }

    function updateRoomStatus(roomName, newStatus) {
        return fetch(`/api/rooms/update/${roomName}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: newStatus })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            });
    }

    // Initial load of the room list
    refreshRoomList();
});
