package com.genieLogiciel.Umons.backend.auth.controller;

import com.genieLogiciel.Umons.backend.auth.LoginRequest;
import com.genieLogiciel.Umons.backend.auth.service.AuthService;
import com.genieLogiciel.Umons.backend.extensionOussama.service.AdministrateurService;
import com.genieLogiciel.Umons.backend.service.ProfesseurService;
import com.genieLogiciel.Umons.backend.service.ServiceInscriptionService;
import com.genieLogiciel.Umons.backend.service.StudentService;
import com.genieLogiciel.Umons.backend.extensionOussama.service.AssistantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion de l'authentification.
 */
@RestController
@CrossOrigin("http://localhost:8080")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private AssistantService assistantService;
    @Autowired private StudentService studentService;
    @Autowired private ProfesseurService professeurService;
    @Autowired private ServiceInscriptionService serviceInscriptionService;
    @Autowired private AdministrateurService administrateurService;

    /**
     * Gère la requête de connexion.
     *
     * @param loginData Les données de connexion.
     * @param session La session HTTP.
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginData , HttpSession session) {

        // Vérifie l'email pour déterminer le rôle de l'utilisateur
        String email = loginData.getEmail();
        String password = loginData.getPassword();
        String role = authService.determineUserRole(email);

        // En fonction du rôle, appelle le service approprié pour gérer la connexion
        if (role.equals("assistant")) {
            // Appeler le service pour la connexion de l'assistant
            ResponseEntity<String> response = assistantService.login(email, password);
            // Gérer les différents cas de retour de la méthode login
            if (response.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<>("Assistant login successful" , HttpStatus.OK);
            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new ResponseEntity<>("Invalid password" , HttpStatus.UNAUTHORIZED);
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>("Assistant not found" , HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Unknown error" , HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (role.equals("professeur")) {
            // Appeler le service pour la connexion du professeur
            ResponseEntity<String> response = professeurService.login(email, password);
            // Gérer les différents cas de retour de la méthode login
            if (response.getStatusCode() == HttpStatus.OK) {
                String matricule = authService.extractMatriculeFromEmail(email);
                session.setAttribute("matricule" , matricule);
                return new ResponseEntity<>("professeur login successful" , HttpStatus.OK);
            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new ResponseEntity<>("Invalid password" , HttpStatus.UNAUTHORIZED);
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>("professeur not found" , HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Unknown error" , HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (role.equals("student")) {
            // Appeler le service pour la connexion de l'étudiant
            ResponseEntity<String> response = studentService.login(email, password);
            // Gérer les différents cas de retour de la méthode login
            if (response.getStatusCode() == HttpStatus.OK) {
                String matricule = authService.extractMatriculeFromEmail(email);
                session.setAttribute("matricule" , matricule);
                return new ResponseEntity<>("student login successful" , HttpStatus.OK);
            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new ResponseEntity<>("Invalid password" , HttpStatus.UNAUTHORIZED);
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>("student not found" , HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Unknown error" , HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (role.equals("inscription")) {
            // Appeler le service pour la connexion du membre du service d'inscription
            ResponseEntity<String> response = serviceInscriptionService.login(email, password);
            // Gérer les différents cas de retour de la méthode login
            if (response.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<>("membre SI login successful" , HttpStatus.OK);
            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new ResponseEntity<>("Invalid password" , HttpStatus.UNAUTHORIZED);
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>("membre SI not found" , HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Unknown error" , HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (role.equals("administrateur")){

            // Appeler le service pour la connexion de l'admin
            ResponseEntity<String> response = administrateurService.login(email, password);
            // Gérer les différents cas de retour de la méthode login
            if (response.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<>("admin login successful" , HttpStatus.OK);
            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new ResponseEntity<>("Invalid password" , HttpStatus.UNAUTHORIZED);
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>("admin not found" , HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Unknown error" , HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>("unknown user" , HttpStatus.UNAUTHORIZED);
    }

    /**
     * Récupère les données de la session.
     *
     * @param session La session HTTP.
     * @return ResponseEntity contenant les données de session.
     */
    @GetMapping("/session/data")
    public ResponseEntity<String> getSessionData (HttpSession session){
        String matricule  = (String) session.getAttribute("matricuel");
        return new ResponseEntity<>(matricule , HttpStatus.OK);
    }
}
