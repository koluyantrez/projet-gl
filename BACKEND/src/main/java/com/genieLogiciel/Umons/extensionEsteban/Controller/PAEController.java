package com.genieLogiciel.Umons.extensionEsteban.Controller;

import com.genieLogiciel.Umons.extensionEsteban.model.Pae;
import com.genieLogiciel.Umons.extensionEsteban.Service.PAEService;
import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.repository.CoursRepository;
import com.genieLogiciel.Umons.model.Student;
import com.genieLogiciel.Umons.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;

/**
 * The type Pae controller.
 */
@RestController
@RequestMapping("/api/pae")
public class PAEController {

    @Autowired
    private PAEService paeService;

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Create pae response entity.
     *
     * @param pae the pae
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Pae> createPAE(@RequestBody Pae pae) {
        Pae createdPAE = paeService.createPAE(pae);
        return ResponseEntity.ok(createdPAE);
    }

    /**
     * Gets all pa es.
     *
     * @return the all pa es
     */
    @GetMapping
    public ResponseEntity<List<Pae>> getAllPAEs() {
        List<Pae> paes = paeService.getAllPAEs();
        return ResponseEntity.ok(paes);
    }

    /**
     * Gets pae by id.
     *
     * @param id the id
     * @return the pae by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pae> getPAEById(@PathVariable Long id) {
        Pae pae = paeService.getPAEById(id);
        return ResponseEntity.ok(pae);
    }

    /**
     * Delete pae response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePAE(@PathVariable Long id) {
        paeService.deletePAE(id);
        return noContent().build();
    }

    /**
     * Add course to pae response entity.
     *
     * @param paeId    the pae id
     * @param courseId the course id
     * @return the response entity
     */
    @PostMapping("/add/{paeId}/{courseId}")
    public ResponseEntity<Void> addCourseToPAE(@PathVariable Long paeId, @PathVariable Long courseId) {
        Pae pae = paeService.getPAEById(paeId);
        Cours course = coursRepository.findById(courseId).orElse(null);
        if (pae != null && course != null) {
            pae.getCourses().add(course);
            paeService.createPAE(pae);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Add pae to student response entity.
     *
     * @param matricule the matricule
     * @param pae       the pae
     * @return the response entity
     */
    @PostMapping("/add-to-student/{matricule}")
    public ResponseEntity<Pae> addPAEToStudent(@PathVariable Long matricule, @RequestBody Pae pae) {
        Student student = studentRepository.findById(matricule).orElse(null);
        if (student != null) {
            Pae createdPAE = paeService.addPAEToStudent(student, pae);
            return ResponseEntity.ok(createdPAE);
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * Add existing pae to student response entity.
     *
     * @param matricule the matricule
     * @param paeId     the pae id
     * @return the response entity
     */
    @PostMapping("/add-existing-to-student/{matricule}/{paeId}")
    public ResponseEntity<Pae> addExistingPAEToStudent(@PathVariable Long matricule, @PathVariable Long paeId) {
        Student student = studentRepository.findById(matricule).orElse(null);
        Pae pae = paeService.getPAEById(paeId);
        if (student != null && pae != null) {
            Pae updatedPAE = paeService.addExistingPAEToStudent(student, pae);
            return ResponseEntity.ok(updatedPAE);
        }
        return ResponseEntity.notFound().build();
    }
}
