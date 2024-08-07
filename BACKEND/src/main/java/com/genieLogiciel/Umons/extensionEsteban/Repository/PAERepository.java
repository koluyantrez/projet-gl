package com.genieLogiciel.Umons.extensionEsteban.Repository;

import com.genieLogiciel.Umons.extensionEsteban.model.Pae;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PAERepository extends JpaRepository<Pae, Long> {
}
