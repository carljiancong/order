package com.harmonycloud.repository;

import com.harmonycloud.entity.Prescription;
import oracle.jdbc.proxy.annotation.Pre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    Prescription findByEncounterId(Integer encounterId);
    List<Prescription> findByPatientId(Integer patientId);
}
