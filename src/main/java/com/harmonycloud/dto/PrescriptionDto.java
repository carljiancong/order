package com.harmonycloud.dto;

import com.harmonycloud.entity.Prescription;
import com.harmonycloud.entity.PrescriptionDrug;

import java.util.List;

public class PrescriptionDto {

    private Prescription prescription;

    private List<PrescriptionDrug> prescriptionDrugList;

    public PrescriptionDto() {
    }

    public PrescriptionDto(Prescription prescription, List<PrescriptionDrug> prescriptionDrugList) {
        this.prescription = prescription;
        this.prescriptionDrugList = prescriptionDrugList;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public List<PrescriptionDrug> getPrescriptionDrugList() {
        return prescriptionDrugList;
    }

    public void setPrescriptionDrugList(List<PrescriptionDrug> prescriptionDrugList) {
        this.prescriptionDrugList = prescriptionDrugList;
    }
}
