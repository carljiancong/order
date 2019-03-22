package com.harmonycloud.dto;

import com.harmonycloud.entity.Prescription;
import com.harmonycloud.entity.PrescriptionDrug;

import java.util.List;

public class PrescriptionDrugDto {

    private Prescription prescription;

    private List<PrescriptionDrug> oldPrescriptionDrugList;

    private List<PrescriptionDrug> newPrescriptionDrugList;

    public PrescriptionDrugDto() {
    }

    public PrescriptionDrugDto(Prescription prescription, List<PrescriptionDrug> oldPrescriptionDrugList, List<PrescriptionDrug> newPrescriptionDrugList) {
        this.prescription = prescription;
        this.oldPrescriptionDrugList = oldPrescriptionDrugList;
        this.newPrescriptionDrugList = newPrescriptionDrugList;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
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
