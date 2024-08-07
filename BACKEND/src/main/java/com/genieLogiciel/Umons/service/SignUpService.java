package com.genieLogiciel.Umons.backend.service;

import com.genieLogiciel.Umons.backend.model.SignUpRequest;
import com.genieLogiciel.Umons.backend.repository.SignUpRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignUpService {

    @Autowired
    private SignUpRequestRepository signUpRequestRepository;

    public void sendSignupRequest(SignUpRequest signUpRequest) {
        signUpRequestRepository.save(signUpRequest);
    }

    @Transactional
    public void deleteSignupRequest(Long id) {
        signUpRequestRepository.deleteById(id);
    }
}
