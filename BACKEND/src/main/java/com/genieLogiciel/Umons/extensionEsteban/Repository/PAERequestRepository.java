package com.genieLogiciel.Umons.extensionEsteban.Repository;

import com.genieLogiciel.Umons.extensionEsteban.model.PAERequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Pae request repository.
 */
public interface PAERequestRepository extends JpaRepository<PAERequest, Long> {
}