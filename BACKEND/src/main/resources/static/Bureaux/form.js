document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('office-form');
    if (form) {
        form.addEventListener('submit', async (event) => {
            event.preventDefault();

            const name = form.querySelector('#name').value;
            const location = form.querySelector('#location').value;
            const officeId = form.querySelector('#office-id').value;
            const method = officeId ? 'PUT' : 'POST';
            const url = officeId ? `/api/offices/${officeId}` : '/api/offices/add';

            try {
                const response = await fetch(url, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        name: name,
                        location: location
                    })
                });
                if (!response.ok) {
                    throw new Error('Network response was not ok.');
                }
                alert('Bureau enregistré avec succès');
                window.location.href = 'index.html';
            } catch (error) {
                console.error('Error saving office:', error);
            }
        });

        // Pré-remplir le formulaire si un ID est présent dans l'URL
        const queryParams = new URLSearchParams(window.location.search);
        const officeId = queryParams.get('id');
        if (officeId) {
            fetch(`/api/offices/${officeId}`)
                .then(response => response.json())
                .then(office => {
                    document.querySelector('#name').value = office.name;
                    document.querySelector('#location').value = office.location;
                    document.querySelector('#office-id').value = office.name;
                })
                .catch(error => console.error('Error fetching office:', error));
        }
    }
});
