package com.harmonycloud.service;

import com.harmonycloud.entity.PrescriptionDrug;
import com.harmonycloud.repository.PrescriptionDrugRepository;
import com.harmonycloud.result.CimsResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionDrugService {
    @Autowired
    private PrescriptionDrugRepository prescriptionDrugRepository;

    public CimsResponseWrapper<PrescriptionDrug> savePrescriptionDrug(List<PrescriptionDrug> prescriptionDrugList, Integer prescriptionId) throws Exception {

        for (int i = 0; i < prescriptionDrugList.size(); i++) {
            prescriptionDrugList.get(i).setPrescriptionId(prescriptionId);
            prescriptionDrugRepository.save(prescriptionDrugList.get(i));
        }
        return new CimsResponseWrapper<>(true, null, null);
    }

    public void savePrescriptionDrugCancel(Integer prescriptionId) {

        List<PrescriptionDrug> prescriptionDrugList = prescriptionDrugRepository.findByPrescriptionId(prescriptionId);
        for (int i = 0; i < prescriptionDrugList.size(); i++) {
            prescriptionDrugRepository.delete(prescriptionDrugList.get(i));
        }
    }

}
