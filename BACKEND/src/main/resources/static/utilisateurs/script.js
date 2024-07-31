document.addEventListener('DOMContentLoaded', function() {
    const filterForm = document.getElementById('filterForm');
    const personnelList = document.getElementById('personnelList');
    const categorySelect = document.getElementById('searchCategory');
    const departmentSelect = document.getElementById('searchDepartment');

    // Load all personnel, categories, and departments initially
    loadPersonnel();
    loadCategories();
    loadDepartments();

    // Filter form submission
    filterForm.addEventListener('submit', function(event) {
        event.preventDefault();
        loadPersonnel();
    });

    // Fetch personnel data based on department and category
    function loadPersonnel() {
        const category = categorySelect.value;
        const department = departmentSelect.value;

        let url = '/api/personnel/search';

        // Construct URL based on filter criteria
        const queryParams = [];
        if (category) queryParams.push(`category=${encodeURIComponent(category)}`);
        if (department) queryParams.push(`department=${encodeURIComponent(department)}`);

        if (queryParams.length > 0) {
            url += '?' + queryParams.join('&');
        }

        fetch(url)
            .then(response => response.json())
            .then(personnel => {
                personnelList.innerHTML = '';
                personnel.forEach(p => {
                    const card = document.createElement('div');
                    card.className = 'col-md-4 mb-3';
                    card.innerHTML = `
                        <div class="card" data-id="${p.matricule}">
                            <div class="card-body">
                                <h5 class="card-title">${p.name}</h5>
                                <p class="card-text"><strong>Catégorie:</strong> ${p.categorie}</p>
                                <p class="card-text"><strong>Département:</strong> ${p.departement}</p>
                            </div>
                        </div>
                    `;
                    card.addEventListener('click', function() {
                        showPersonnelDetails(p.matricule);
                    });
                    personnelList.appendChild(card);
                });
            })
            .catch(error => {
                console.error('Error fetching personnel:', error);
            });
    }

    // Load categories for the filter dropdown
    function loadCategories() {
        fetch('/api/personnel/categories')
            .then(response => response.json())
            .then(categories => {
                categories.forEach(cat => {
                    const option = document.createElement('option');
                    option.value = cat;
                    option.textContent = cat.charAt(0).toUpperCase() + cat.slice(1).toLowerCase();
                    categorySelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('Error fetching categories:', error);
            });
    }

    // Load departments for the filter dropdown
    function loadDepartments() {
        fetch('/api/personnel/departments')
            .then(response => response.json())
            .then(departments => {
                departments.forEach(dep => {
                    const option = document.createElement('option');
                    option.value = dep;
                    option.textContent = dep.charAt(0).toUpperCase() + dep.slice(1).toLowerCase();
                    departmentSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('Error fetching departments:', error);
            });
    }

    // Show personnel details
    function showPersonnelDetails(id) {
        fetch(`/api/personnel/matricule/${id}`)
            .then(response => response.json())
            .then(personnel => {
                document.getElementById('detailName').textContent = personnel.name || 'Non spécifié';
                document.getElementById('detailImage').src = personnel.image || 'default-image.jpg';
                document.getElementById('detailAddress').textContent = personnel.adresse || 'Non spécifié';
                document.getElementById('detailCategory').textContent = personnel.categorie || 'Non spécifié';
                document.getElementById('detailNumber').textContent = personnel.numero || 'Non spécifié';
                document.getElementById('detailDepartment').textContent = personnel.departement || 'Non spécifié';
                document.getElementById('detailEmail').textContent = personnel.email || 'Non spécifié';
                document.getElementById('detailMatricule').textContent = personnel.matricule || 'Non spécifié';

                $('#personnelDetails').modal('show'); // Show the modal using Bootstrap
            })
            .catch(error => {
                console.error('Error fetching personnel details:', error);
            });
    }
});
