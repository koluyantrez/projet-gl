# Projet de Génie Logiciel 2023/2024

Projet du groupe 4

## Membres actifs

- **210447 | Razanajao Aina** : Publication scientifique
- **220191 | Vanduynslager Estebane** : Gestion du PAE
- **223073 | Hakik Oussama** : Gestion des locaux


## Démarrage du projet

**Prérequis**

Avant de commencer, assurez-vous de disposer des éléments suivants :

- Avoir installé Java 21.
- Avoir installé Node.js et npm (npm install ou npm install --force). 
- Avoir téléchargé le code source du projet.



1. Ouvrez une invite de commande.
2. Naviguez vers le dossier racine du projet.
3. Modifiez les chemins d'accès dans le fichier `demarrage.bat` en fonction de votre environnement.
4. Exécutez le fichier `demarrage.bat`.

### Configuration du fichier `demarrage.bat`

Le fichier `demarrage.bat` contient les commandes nécessaires au démarrage du projet. Vous devrez peut-être modifier les chemins d'accès pour qu'ils correspondent à votre environnement.

- Ligne 1 : `@echo off` - Désactive l'affichage des commandes dans la console.
- Ligne 2 : `rem` Démarre le serveur Spring Boot - Commentaire explicatif.
- Ligne 3 : `start cmd /c "cd <chemin_vers_le_projet> && "gradlew bootRun"` - Lance le serveur Spring Boot dans une nouvelle fenêtre de console.

- Ligne 5 : `rem` Démarre le serveur frontend - Commentaire explicatif.
- Ligne 6 : `start cmd /c "cd <chemin_vers_le_frontend> && npm run serve"` - Lance le serveur frontend dans une nouvelle fenêtre de console.

### Accès à l'application

- L'application backend sera accessible à l'adresse `http://localhost:1937`.
- L'application frontend sera accessible à l'adresse `http://localhost:8080`.

### Remarques

- Si vous rencontrez des problèmes lors du démarrage du projet, vérifiez que vous avez bien installé tous les prérequis et que les chemins d'accès dans le fichier `demarrage.bat` sont corrects.
- Pour lancer seulement le backend, utilisez la commande `gradlew bootrun`.
- Pour lancer seulement le frontend, utilisez la commande `npm run serve`.
- Pour exécuter les tests unitaires, utilisez la commande `gradlew test`.
