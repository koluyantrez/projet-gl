package com.genieLogiciel.Umons.auth.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service pour la gestion de l'authentification.
 */
@Service
public class AuthService {

    /**
     * Détermine le rôle de l'utilisateur en fonction de son adresse e-mail.
     *
     * @param email L'adresse e-mail de l'utilisateur.
     * @return Le rôle de l'utilisateur (assistant, professeur, étudiant, inscription ou inconnu).
     */
    public String determineUserRole(String email) {
        if (email.endsWith("@Illumis.assistant.ac.be")) {
            return "assistant";
        } else if (email.endsWith("@Illumis.professeur.ac.be")) {
            return "professeur";
        } else if (email.endsWith("@Illumis.student.ac.be")) {
            return "student";
        } else if (email.endsWith("@Illumis.inscription.ac.be")){
            return "inscription";
        } else if (email.endsWith("@Illumis.admin.ac.be")){
            return "administrateur";
        }
        else {
            return "inconnu";
        }
    }

    /**
     * Extrait le matricule à partir de l'adresse e-mail de l'utilisateur.
     *
     * @param email L'adresse e-mail de l'utilisateur.
     * @return Le matricule extrait de l'adresse e-mail.
     */
    public String extractMatriculeFromEmail(String email) {
        Pattern pattern = Pattern.compile("^(\\d+)@Illumis\\.(student|assistant|professeur|inscription|admin)\\.ac\\.be$");
        Matcher matcher = pattern.matcher(email);

        if (matcher.find()) {
            return matcher.group(1); // Le groupe 1 correspond au premier groupe capturant dans l'expression régulière
        } else {
            // Si aucun matricule n'est trouvé, retourner null
            return null;
        }
    }

}
