package com.harmonycloud.entity;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "presciption_drug")
public class PrescriptionDrug {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @ApiModelProperty(name = "id", example = "1")
    private Integer PrescriptionDrugId;
    @Column(name = "drug_id")
    @ApiModelProperty(name = "药品id", example = "1")
    private Integer DrugId;
    @Column(name = "reginmen_line")
    @ApiModelProperty(name = "剂量")
    private String ReginmenLine;
    @Column(name = "prescripttion_id")
    @ApiModelProperty(name = "处方id")
    private Integer PrescriptionId;

    public PrescriptionDrug() {
    }

    public PrescriptionDrug(Integer drugId, String reginmenLine, Integer prescriptionId) {
        DrugId = drugId;
        ReginmenLine = reginmenLine;
        PrescriptionId = prescriptionId;
    }

    public Integer getPrescriptionDrugId() {
        return PrescriptionDrugId;
    }

    public void setPrescriptionDrugId(Integer prescriptionDrugId) {
        PrescriptionDrugId = prescriptionDrugId;
    }

    public Integer getDrugId() {
        return DrugId;
    }

    public void setDrugId(Integer drugId) {
        DrugId = drugId;
    }

    public String getReginmenLine() {
        return ReginmenLine;
    }

    public void setReginmenLine(String reginmenLine) {
        ReginmenLine = reginmenLine;
    }

    public Integer getPrescriptionId() {
        return PrescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        PrescriptionId = prescriptionId;
    }
}
