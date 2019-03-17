package com.harmonycloud.dto;

import com.harmonycloud.bo.PrescriptionDrugBo;
import com.harmonycloud.entity.Prescription;

import java.util.List;

public class DrugHistory {
    private Prescription prescription;
    private List<PrescriptionDrugBo> prescriptionDrugBoList;

    public DrugHistory() {
    }

    public DrugHistory(Prescription prescription, List<PrescriptionDrugBo> prescriptionDrugBoList) {
        this.prescription = prescription;
        this.prescriptionDrugBoList = prescriptionDrugBoList;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public List<PrescriptionDrugBo> getPrescriptionDrugBoList() {
        return prescriptionDrugBoList;
    }

    public void setPrescriptionDrugBoList(List<PrescriptionDrugBo> prescriptionDrugBoList) {
        this.prescriptionDrugBoList = prescriptionDrugBoList;
    }
}
