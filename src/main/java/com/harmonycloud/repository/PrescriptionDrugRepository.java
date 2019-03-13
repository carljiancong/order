package com.harmonycloud.repository;

import com.harmonycloud.entity.PrescriptionDrug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionDrugRepository extends JpaRepository<PrescriptionDrug,Integer> {
    List<PrescriptionDrug> findByPrescriptionId(Integer prescriptionId);
}
