package com.harmonycloud.repository;

import com.harmonycloud.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    Prescription findByEncounterId(Integer encounterId);
}
