package com.genieLogiciel.Umons.extensionEsteban.Controller;

import com.genieLogiciel.Umons.extensionEsteban.Service.PAERequestService;
import com.genieLogiciel.Umons.extensionEsteban.Service.PeriodService;
import com.genieLogiciel.Umons.extensionEsteban.model.PAERequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Pae request controller.
 */
@RestController
@RequestMapping("/api/pae-requests")
public class PAERequestController {

    @Autowired
    private PAERequestService paeRequestService;

    @Autowired
    private PeriodService periodService;

    /**
     * Submit pae request response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<String> submitPAERequest(@RequestBody PAERequest request) {
//        if (!periodService.isWithinPeriod()) {
//            return ResponseEntity.status(403).body("La période de demande est fermée");
//        }
        paeRequestService.saveRequest(request);
        return ResponseEntity.ok("Demande soumise avec succès");
    }

    /**
     * Accept pae request response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @PostMapping("/{id}/accept")
    public ResponseEntity<String> acceptPAERequest(@PathVariable Long id) {
        paeRequestService.acceptRequest(id);
        return ResponseEntity.ok("Demande acceptée");
    }

    /**
     * Reject pae request response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectPAERequest(@PathVariable Long id) {
        paeRequestService.rejectRequest(id);
        return ResponseEntity.ok("Demande rejetée");
    }

    /**
     * Gets all pae requests.
     *
     * @return the all pae requests
     */
    @GetMapping
    public ResponseEntity<List<PAERequest>> getAllPAERequests() {
        List<PAERequest> requests = paeRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }
}
