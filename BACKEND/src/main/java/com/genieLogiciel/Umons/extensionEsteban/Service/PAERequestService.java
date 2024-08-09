package com.genieLogiciel.Umons.extensionEsteban.Service;

import com.genieLogiciel.Umons.extensionEsteban.Repository.PAERequestRepository;
import com.genieLogiciel.Umons.extensionEsteban.model.PAERequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Pae request service.
 */
@Service
public class PAERequestService {

    @Autowired
    private PAERequestRepository paeRequestRepository;

    /**
     * Save request.
     *
     * @param request the request
     */
    public void saveRequest(PAERequest request) {
        paeRequestRepository.save(request);
    }

    /**
     * Accept request.
     *
     * @param id the id
     */
    public void acceptRequest(Long id) {
        PAERequest request = paeRequestRepository.findById(id).orElseThrow();
        request.setStatus("Accepted");
        paeRequestRepository.save(request);
    }

    /**
     * Reject request.
     *
     * @param id the id
     */
    public void rejectRequest(Long id) {
        PAERequest request = paeRequestRepository.findById(id).orElseThrow();
        request.setStatus("Rejected");
        paeRequestRepository.save(request);
    }

    /**
     * Gets all requests.
     *
     * @return the all requests
     */
    public List<PAERequest> getAllRequests() {
        return paeRequestRepository.findAll();
    }
}
