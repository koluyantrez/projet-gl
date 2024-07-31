document.addEventListener('DOMContentLoaded', () => {
    async function displayOfficeDetails() {
        const queryParams = new URLSearchParams(window.location.search);
        const officeName = queryParams.get('id');
        if (!officeName) return;

        const officeDetailDiv = document.getElementById('office-detail');
        const editLink = document.getElementById('edit-link');
        if (!officeDetailDiv || !editLink) return;

        try {
            const response = await fetch(`/api/offices/${officeName}`);
            if (!response.ok) {
                throw new Error('Network response was not ok.');
            }
            const office = await response.json();

            officeDetailDiv.innerHTML = `
                <h2>${office.name}</h2>
                <p>Emplacement: ${office.location}</p>
                <p>Disponible: ${office.available}</p>
                <p>Occupant: ${office.associatedPersonnel || 'Aucun'}</p>
                <p>Équipements: ${office.equipment.join(', ') || 'Aucun'}</p>
            `;

            editLink.href = `office-form.html?id=${office.name}`;

        } catch (error) {
            console.error('Error fetching office details:', error);
        }
    }

    displayOfficeDetails();
});
