package com.genieLogiciel.Umons.backend.repository;

import com.genieLogiciel.Umons.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
