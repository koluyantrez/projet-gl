package com.genieLogiciel.Umons.extensionEsteban.Controller;

import com.genieLogiciel.Umons.extensionEsteban.model.Pae;
import com.genieLogiciel.Umons.extensionEsteban.Service.PAEService;
import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.repository.CoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/api/pae")
public class PAEController {

    @Autowired
    private PAEService paeService;

    @Autowired
    private CoursRepository coursRepository;

    @PostMapping
    public ResponseEntity<Pae> createPAE(@RequestBody Pae pae) {
        Pae createdPAE = paeService.createPAE(pae);
        return ResponseEntity.ok(createdPAE);
    }

    @GetMapping
    public ResponseEntity<List<Pae>> getAllPAEs() {
        List<Pae> paes = paeService.getAllPAEs();
        return ResponseEntity.ok(paes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pae> getPAEById(@PathVariable Long id) {
        Pae pae = paeService.getPAEById(id);
        return ResponseEntity.ok(pae);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePAE(@PathVariable Long id) {
        paeService.deletePAE(id);
        return noContent().build();
    }

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
}
