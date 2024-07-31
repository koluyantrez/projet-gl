const adminId = 1; // ID de l'administrateur (à ajuster selon votre application)

async function updateAddress() {
    const address = document.getElementById('address').value;
    try {
        const response = await fetch(`/api/admin/${adminId}/address`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ address })
        });

        if (!response.ok) {
            throw new Error("Erreur lors de la mise à jour de l'adresse");
        }
        alert("Adresse mise à jour avec succès !");
    } catch (error) {
        console.error("Erreur lors de la mise à jour:", error);
        alert("Échec de la mise à jour de l'adresse.");
    }
}

async function updatePhoneNumber() {
    const phone = document.getElementById('phone').value;
    try {
        const response = await fetch(`/api/admin/${adminId}/phone`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ phone })
        });

        if (!response.ok) {
            throw new Error("Erreur lors de la mise à jour du numéro de téléphone");
        }
        alert("Numéro de téléphone mis à jour avec succès !");
    } catch (error) {
        console.error("Erreur lors de la mise à jour:", error);
        alert("Échec de la mise à jour du numéro de téléphone.");
    }
}

async function updatePassword() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (password !== confirmPassword) {
        alert("Les mots de passe ne correspondent pas.");
        return;
    }

    try {
        const response = await fetch(`/api/admin/${adminId}/password`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ password })
        });

        if (!response.ok) {
            throw new Error("Erreur lors de la mise à jour du mot de passe");
        }
        alert("Mot de passe mis à jour avec succès !");
    } catch (error) {
        console.error("Erreur lors de la mise à jour:", error);
        alert("Échec de la mise à jour du mot de passe.");
    }
}
