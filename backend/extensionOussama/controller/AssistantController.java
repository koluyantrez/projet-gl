package com.genieLogiciel.Umons.backend.extensionOussama.controller;

import com.genieLogiciel.Umons.backend.extensionOussama.model.Assistant;
import com.genieLogiciel.Umons.backend.extensionOussama.service.AssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion des assistants.
 */
@RestController
@RequestMapping("/assistant")
@CrossOrigin("http://localhost:8080")
public class AssistantController {
    @Autowired
    private AssistantService assistantService;

    /**
     * Ajoute un nouvel assistant.
     *
     * @param newAssistant Le nouvel assistant à ajouter.
     * @return ResponseEntity contenant le message de succès ou d'erreur.
     */
    @PostMapping("/addNew")
    public ResponseEntity<String> addNewAssistant(@RequestBody Assistant newAssistant) {
        return assistantService.addNewAssistant(newAssistant);
    }
}
