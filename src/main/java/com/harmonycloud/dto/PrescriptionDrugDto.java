package com.harmonycloud.dto;

import com.harmonycloud.entity.Prescription;
import com.harmonycloud.entity.PrescriptionDrug;

import java.util.List;

public class PrescriptionDrugDto {

    private Prescription Prescription;

    private List<PrescriptionDrug> oldPrescriptionDrugList;

    private List<PrescriptionDrug> newPrescriptionDrugList;

    public PrescriptionDrugDto() {
    }

    public PrescriptionDrugDto(com.harmonycloud.entity.Prescription prescription, List<PrescriptionDrug> oldPrescriptionDrugList, List<PrescriptionDrug> newPrescriptionDrugList) {
        Prescription = prescription;
        this.oldPrescriptionDrugList = oldPrescriptionDrugList;
        this.newPrescriptionDrugList = newPrescriptionDrugList;
    }

    public com.harmonycloud.entity.Prescription getPrescription() {
        return Prescription;
    }

    public void setPrescription(com.harmonycloud.entity.Prescription prescription) {
        Prescription = prescription;
    }

    public List<PrescriptionDrug> getOldPrescriptionDrugList() {
        return oldPrescriptionDrugList;
    }

    public void setOldPrescriptionDrugList(List<PrescriptionDrug> oldPrescriptionDrugList) {
        this.oldPrescriptionDrugList = oldPrescriptionDrugList;
    }

    public List<PrescriptionDrug> getNewPrescriptionDrugList() {
        return newPrescriptionDrugList;
    }

    public void setNewPrescriptionDrugList(List<PrescriptionDrug> newPrescriptionDrugList) {
        this.newPrescriptionDrugList = newPrescriptionDrugList;
    }
}
