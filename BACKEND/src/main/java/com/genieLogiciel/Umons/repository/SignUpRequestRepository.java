package com.genieLogiciel.Umons.repository;

import com.genieLogiciel.Umons.model.SignUpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpRequestRepository extends JpaRepository<SignUpRequest, Long> {

}