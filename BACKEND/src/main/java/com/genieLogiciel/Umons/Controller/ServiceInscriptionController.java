package com.genieLogiciel.Umons.Controller;

import com.genieLogiciel.Umons.model.ServiceInscription;

import com.genieLogiciel.Umons.model.Student;
import com.genieLogiciel.Umons.service.ServiceInscriptionService;
import com.genieLogiciel.Umons.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping( "/api/inscription")
public class ServiceInscriptionController {

    private ServiceInscriptionService serviceInscriptionService;
    private StudentService studentService;

    @Autowired
    public ServiceInscriptionController(ServiceInscriptionService serviceInscriptionService, StudentService studentService) {
        this.serviceInscriptionService = serviceInscriptionService;
        this.studentService = studentService;
    }

    @PostMapping("/addMember")
    public ResponseEntity<String> creer(@RequestBody ServiceInscription inscription){
        return serviceInscriptionService.addNewMember(inscription);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<ServiceInscription> rechercher(){
        return this.serviceInscriptionService.rechercher();
    }

    @GetMapping("/membreServiceInscription")
    public ServiceInscription lire(@RequestParam Integer matricule){
        return this.serviceInscriptionService.lire(matricule);
    }

    @DeleteMapping(path="{matricule}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> supprimer(@PathVariable int matricule){
        this.serviceInscriptionService.supprimer(matricule);
        return new ResponseEntity<>("ServiceInscription service member deleted successfully", HttpStatus.OK);
    }

    @PostMapping(path = "addStudent", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addStudent(@RequestBody Student student){
        this.serviceInscriptionService.addStudent(student);
        return new ResponseEntity<>("Student added successfully", HttpStatus.OK);
    }


}