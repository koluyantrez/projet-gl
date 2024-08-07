document.addEventListener('DOMContentLoaded', function() {
    let currentPage = 1;
    const itemsPerPage = 10;

    loadReservations(currentPage, itemsPerPage);

    document.getElementById('prev-btn').addEventListener('click', function() {
        if (currentPage > 1) {
            currentPage--;
            loadReservations(currentPage, itemsPerPage);
        }
    });

    document.getElementById('next-btn').addEventListener('click', function() {
        currentPage++;
        loadReservations(currentPage, itemsPerPage);
    });
});

function loadReservations(page, limit) {
    fetch(`http://localhost:1937/api/reservations?page=${page}&limit=${limit}`)
        .then(response => response.json())
        .then(data => {
            const reservationsList = document.getElementById('reservations-list');
            reservationsList.innerHTML = '';
            data.forEach(reservation => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <td>${reservation.firstName} ${reservation.lastName}</td>
                    <td>${new Date(reservation.date).toLocaleDateString()}</td>
                    <td>${reservation.start}</td>
                    <td>${reservation.desiredRoom}</td>
                    <td>${reservation.reservationStatus}</td>
                    <td>${reservation.reservationStatus === 'PENDING' ? '<button class="modify-btn" onclick="modifyReservation(' + reservation.id + ')">Modifier</button>' : ''}</td>
                `;
                reservationsList.appendChild(row);
            });
        })
        .catch(error => console.error('Erreur:', error));
}

function modifyReservation(id) {
    // Ajouter la logique pour modifier la réservation
}
