document.addEventListener('DOMContentLoaded', function() {
    const buildingSelect = document.getElementById('building');
    const roomSelect = document.getElementById('desiredRoom');
    const reservationForm = document.getElementById('reservation-form');

    // Fonction pour charger les bâtiments
    function loadBuildings() {
        fetch('http://localhost:1937/api/buildings')
            .then(response => response.json())
            .then(buildings => {
                buildings.forEach(building => {
                    const option = document.createElement('option');
                    option.value = building.name;
                    option.textContent = building.name;
                    buildingSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Erreur lors de la récupération des bâtiments:', error));
    }

    // Fonction pour charger les salles en fonction du bâtiment sélectionné
    function loadRooms(buildingName) {
        roomSelect.innerHTML = '<option value="">Sélectionner une salle</option>'; // Réinitialiser les options de salle

        fetch(`http://localhost:1937/api/rooms/building/${buildingName}`)
            .then(response => response.json())
            .then(rooms => {
                rooms.forEach(room => {
                    if (room.type !== 'OFFICE') { // Assurez-vous que le type de salle est exclu
                        const option = document.createElement('option');
                        option.value = room.name;
                        option.textContent = room.name;
                        roomSelect.appendChild(option);
                    }
                });
            })
            .catch(error => console.error('Erreur lors de la récupération des salles:', error));
    }

    // Charger les bâtiments au chargement de la page
    loadBuildings();

    // Écouter les changements sur le sélecteur de bâtiment
    buildingSelect.addEventListener('change', function() {
        const selectedBuilding = buildingSelect.value;
        if (selectedBuilding) {
            loadRooms(selectedBuilding);
        } else {
            roomSelect.innerHTML = '<option value="">Sélectionner une salle</option>';
        }
    });

    // Fonction pour soumettre le formulaire de réservation
    function submitReservationForm(event) {
        event.preventDefault();

        const firstName = document.getElementById('firstName').value;
        const lastName = document.getElementById('lastName').value;
        const date = document.getElementById('date').value;
        const start = document.getElementById('start').value;
        const end = document.getElementById('end').value;
        const reason = document.getElementById('reason').value;
        const desiredRoom = document.getElementById('desiredRoom').value;

        // Validation basique
        if (!firstName || !lastName || !date || !start || !end || !reason || !desiredRoom) {
            alert('Veuillez remplir tous les champs.');
            return;
        }

        fetch('http://localhost:1937/api/roomReservation/addReservation', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ firstName, lastName, date, start, end, reason, desiredRoom })
        })
            .then(response => response.json())
            .then(data => {
                alert('Réservation envoyée avec succès');
                // Réinitialiser le formulaire ou rediriger si nécessaire
                reservationForm.reset();
            })
            .catch(error => {
                console.error('Erreur lors de l\'envoi de la réservation:', error);
                alert('Une erreur est survenue lors de l\'envoi de la réservation.');
            });
    }

    // Écouter la soumission du formulaire
    reservationForm.addEventListener('submit', submitReservationForm);
});
