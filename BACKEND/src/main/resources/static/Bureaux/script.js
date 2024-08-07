document.addEventListener("DOMContentLoaded", () => {
    const officeTableBody = document.querySelector('#office-table tbody');
    const assignPersonnelModal = document.getElementById('assign-personnel-modal');
    const assignPersonnelBtn = document.getElementById('assign-personnel-btn');
    const personnelSearchInput = document.getElementById('personnel-search');
    const personnelSuggestions = document.getElementById('personnel-suggestions');
    const closeBtn = document.querySelector('.close-btn');

    // Charger les bureaux
    const loadOffices = async () => {
        try {
            const response = await fetch('/api/offices');
            if (!response.ok) throw new Error('Erreur lors de la récupération des bureaux.');
            const offices = await response.json();
            displayOffices(offices);
        } catch (error) {
            console.error('Erreur:', error);
        }
    };

    // Afficher les bureaux dans le tableau
    const displayOffices = (offices) => {
        officeTableBody.innerHTML = '';
        offices.forEach(office => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${office.id}</td>
                <td>${office.name}</td>
                <td>${office.location}</td>
                <td>${office.associatedPersonnel || 'Aucun'}</td>
                <td>
                    <button class="assign-btn" data-office-name="${office.name}">
                        ${office.associatedPersonnel ? 'Modifier' : 'Attribuer'}
                    </button>
                    ${office.pendingUpdate ? '<span class="pending-update">En attente de validation</span>' : ''}
                    ${office.pendingDeletion ? '<span class="pending-deletion">En attente de suppression</span>' : ''}
                </td>
            `;
            officeTableBody.appendChild(tr);
        });
    };

    // Ouvrir la modale
    const openAssignPersonnelModal = (officeName) => {
        assignPersonnelModal.style.display = 'block';
        assignPersonnelBtn.dataset.officeName = officeName;
        loadPersonnels(); // Charge les personnels
    };

    // Fermer la modale
    closeBtn.addEventListener('click', () => {
        assignPersonnelModal.style.display = 'none';
    });

    window.onclick = (event) => {
        if (event.target == assignPersonnelModal) {
            assignPersonnelModal.style.display = 'none';
        }
    };

    // Charger les personnels pour suggestions
    const loadPersonnels = async (query = '') => {
        try {
            const response = await fetch(`/api/personnel?query=${encodeURIComponent(query)}`);
            if (!response.ok) throw new Error('Erreur lors de la récupération des personnels.');
            const personnels = await response.json();
            displaySuggestions(personnels);
        } catch (error) {
            console.error('Erreur:', error);
        }
    };

    // Afficher les suggestions
    const displaySuggestions = (personnels) => {
        personnelSuggestions.innerHTML = '';
        personnels.forEach(personnel => {
            const li = document.createElement('li');
            li.textContent = personnel.name;
            li.dataset.personnelId = personnel.id;
            li.addEventListener('click', () => {
                personnelSearchInput.value = personnel.name;
                personnelSuggestions.innerHTML = '';
            });
            personnelSuggestions.appendChild(li);
        });
    };

    // Rechercher les personnels au fur et à mesure de la saisie
    personnelSearchInput.addEventListener('input', () => {
        const query = personnelSearchInput.value;
        if (query.length > 1) {
            loadPersonnels(query);
        } else {
            personnelSuggestions.innerHTML = '';
        }
    });

    // Attribuer un personnel au bureau
    assignPersonnelBtn.addEventListener('click', async () => {
        const officeName = assignPersonnelBtn.dataset.officeName;
        const personnelName = personnelSearchInput.value;

        if (!personnelName) {
            alert('Veuillez sélectionner un personnel.');
            return;
        }

        try {
            const response = await fetch(`/api/personnel/name/${encodeURIComponent(personnelName)}`);
            if (!response.ok) throw new Error('Erreur lors de la récupération du personnel.');

            const personnel = await response.json();
            if (!personnel) {
                alert('Personnel non trouvé.');
                return;
            }

            // Envoyer la requête pour indiquer qu'une mise à jour est en attente de validation
            const updateResponse = await fetch(`/api/offices/${encodeURIComponent(officeName)}/occupant?newOccupant=${encodeURIComponent(personnel.name)}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (updateResponse.ok) {
                assignPersonnelModal.style.display = 'none';
                loadOffices(); // Recharger la liste des bureaux pour refléter l'état "en attente de validation"
            } else {
                throw new Error('Erreur lors de l\'attribution du personnel.');
            }
        } catch (error) {
            console.error('Erreur:', error);
        }
    });

    // Exemple d'ajout de gestionnaire pour ouvrir la modale
    officeTableBody.addEventListener('click', (event) => {
        if (event.target.classList.contains('assign-btn')) {
            const officeName = event.target.dataset.officeName;
            openAssignPersonnelModal(officeName);
        }
    });

    // Charger les bureaux au chargement de la page
    loadOffices();
});
