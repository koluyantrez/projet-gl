package com.genieLogiciel.Umons.backend.repository;

import com.genieLogiciel.Umons.backend.model.SignUpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpRequestRepository extends JpaRepository<SignUpRequest, Long> {
}
