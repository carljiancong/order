package com.harmonycloud.bo;

import com.harmonycloud.entity.PrescriptionDrug;

import java.util.List;

public class PrescriptionDrugBo {
    private List<PrescriptionDrug> oldPrescriptionDrugList;
    private List<PrescriptionDrug> newPrescriptionDrugList;

    public PrescriptionDrugBo() {
    }

    public PrescriptionDrugBo(List<PrescriptionDrug> oldPrescriptionDrugList, List<PrescriptionDrug> newPrescriptionDrugList) {
        this.oldPrescriptionDrugList = oldPrescriptionDrugList;
        this.newPrescriptionDrugList = newPrescriptionDrugList;
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
