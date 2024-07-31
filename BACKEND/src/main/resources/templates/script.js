document.addEventListener('DOMContentLoaded', function() {
    // Cache DOM elements
    const sidebarLinks = document.querySelectorAll('#sidebar .nav-link');
    const mainContent = document.getElementById('mainContent');

    // Function to load content dynamically
    function loadContent(url) {
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(html => {
                mainContent.innerHTML = html;
                mainContent.style.opacity = 0;
                setTimeout(() => {
                    mainContent.style.opacity = 1;
                }, 100);
            })
            .catch(error => {
                console.error('Error loading content:', error);
                mainContent.innerHTML = '<p>Erreur lors du chargement du contenu.</p>';
            });
    }

    // Event listener for sidebar links
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault();

            // Remove 'active' class from all links
            sidebarLinks.forEach(link => link.classList.remove('active'));

            // Add 'active' class to clicked link
            this.classList.add('active');

            // Determine the content to load based on the link id
            let url;
            switch (this.id) {
                case 'overview':
                    url = '/overview';
                    break;
                case 'manageRooms':
                    url = '/static/salles/index.html';
                    break;
                case 'manageBuildings':
                    url = '/static/batiment/index.html';
                    break;
                case 'manageUsers':
                    url = '/static/utilisateurs/index.html';
                    break;
                case 'manageOffices':
                    url = '/static/bureaux/index.html';
                    break;
                case 'settings':
                    url = '/parametre/index.html';
                    break;
                case 'logout':
                    url = '/logout';
                    window.location.href = url;
                    return;
                default:
                    url = '/overview';
                    break;
            }

            // Load the content for the selected section
            loadContent(url);
        });
    });
});
