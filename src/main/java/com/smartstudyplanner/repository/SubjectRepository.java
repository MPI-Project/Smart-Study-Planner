package com.smartstudyplanner.repository;

import com.smartstudyplanner.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}