package com.genieLogiciel.Umons.extensionEsteban.Service;

import com.genieLogiciel.Umons.extensionEsteban.Repository.PAERequestRepository;
import com.genieLogiciel.Umons.extensionEsteban.model.PAERequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PAERequestService {

    @Autowired
    private PAERequestRepository paeRequestRepository;

    public void saveRequest(PAERequest request) {
        paeRequestRepository.save(request);
    }

    public void acceptRequest(Long id) {
        PAERequest request = paeRequestRepository.findById(id).orElseThrow();
        request.setStatus("Accepted");
        paeRequestRepository.save(request);
    }

    public void rejectRequest(Long id) {
        PAERequest request = paeRequestRepository.findById(id).orElseThrow();
        request.setStatus("Rejected");
        paeRequestRepository.save(request);
    }
}
