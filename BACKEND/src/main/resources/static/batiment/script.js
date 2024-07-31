document.addEventListener('DOMContentLoaded', function() {
    const addBuildingForm = document.getElementById('addBuildingForm');
    const buildingList = document.getElementById('buildingList');
    const searchButton = document.getElementById('searchButton');
    const searchName = document.getElementById('searchName');
    const searchResult = document.getElementById('searchResult');

    addBuildingForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const name = document.getElementById('name').value;
        const adresse = document.getElementById('adresse').value;
        const available = document.getElementById('available').value === 'true';

        fetch('/api/buildings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: name,
                adresse: adresse,
                available: available,
                numberOfRooms: 0,
                numbersOfOffices: 0,
                allRooms: [],
                allOffices: []
            })
        })
            .then(response => response.json())
            .then(data => {
                showBuilding(data);
            })
            .catch(error => {
                console.error('Error:', error);
            });

        addBuildingForm.reset();
    });

    fetch('/api/buildings')
        .then(response => response.json())
        .then(buildings => {
            buildings.forEach(building => {
                showBuilding(building);
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });

    searchButton.addEventListener('click', function() {
        const name = searchName.value;
        if (name) {
            fetch(`/api/buildings/name/${name}`)
                .then(response => response.json())
                .then(building => {
                    if (building) {
                        showSearchResult(building);
                    } else {
                        searchResult.textContent = 'Building not found';
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    searchResult.textContent = 'Building not found';
                });
        }
    });

    function showBuilding(building) {
        const col = document.createElement('div');
        col.className = 'col-md-4';

        const card = document.createElement('div');
        card.className = 'card';

        const cardBody = document.createElement('div');
        cardBody.className = 'card-body';

        const title = document.createElement('h5');
        title.className = 'card-title';
        title.textContent = building.name;

        const address = document.createElement('p');
        address.className = 'card-text';
        address.textContent = `Address: ${building.adresse}`;

        const availability = document.createElement('p');
        availability.className = 'card-text';
        availability.textContent = `Available: ${building.available ? 'Yes' : 'No'}`;

        const rooms = document.createElement('p');
        rooms.className = 'card-text';
        rooms.textContent = `Number of Rooms: ${building.numberOfRooms}`;

        const offices = document.createElement('p');
        offices.className = 'card-text';
        offices.textContent = `Number of Offices: ${building.numbersOfOffices}`;

        const toggleButton = document.createElement('button');
        toggleButton.className = 'btn btn-secondary';
        toggleButton.textContent = `Set Available to ${!building.available}`;
        toggleButton.onclick = function() {
            updateAvailability(building.name, !building.available)
                .then(updatedBuilding => {
                    availability.textContent = `Available: ${updatedBuilding.available ? 'Yes' : 'No'}`;
                    toggleButton.textContent = `Set Available to ${!updatedBuilding.available}`;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        };

        cardBody.appendChild(title);
        cardBody.appendChild(address);
        cardBody.appendChild(availability);
        cardBody.appendChild(rooms);
        cardBody.appendChild(offices);
        cardBody.appendChild(toggleButton);

        card.appendChild(cardBody);
        col.appendChild(card);
        buildingList.appendChild(col);
    }

    function showSearchResult(building) {
        searchResult.innerHTML = `
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
    }

    function updateAvailability(buildingName, availability) {
        return fetch(`/api/buildings/${buildingName}/availability`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(availability)
        })
            .then(response => response.json());
    }
});
