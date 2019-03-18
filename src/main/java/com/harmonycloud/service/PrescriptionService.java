package com.harmonycloud.service;

import com.harmonycloud.bo.PrescriptionDrugBo;
import com.harmonycloud.dto.DrugHistory;
import com.harmonycloud.dto.PrescriptionDrugDto;
import com.harmonycloud.dto.PrescriptionDto;
import com.harmonycloud.bo.UserPrincipal;
import com.harmonycloud.entity.Prescription;
import com.harmonycloud.entity.PrescriptionDrug;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import com.harmonycloud.repository.PrescriptionRepository;
import com.harmonycloud.result.CimsResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionDrugService prescriptionDrugService;

    /**
     * save prescription
     *
     * @param prescriptionDto model
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public CimsResponseWrapper<String> savePrescription(PrescriptionDto prescriptionDto) throws Exception {
        Prescription oldPrescription = prescriptionRepository.findByEncounterId(prescriptionDto.getPrescription().getEncounterId());
        Prescription prescription = prescriptionDto.getPrescription();
        List<PrescriptionDrug> prescriptionDrugList = prescriptionDto.getPrescriptionDrugList();
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        prescription.setCreateBy(userDetails.getUsername());
        prescription.setCreateDate(new Date());

        // save prescription
        if (prescriptionRepository.save(prescription).getPrescriptionId() == null) {
            throw new OrderException(ErrorMsgEnum.SAVE_ERROR.getMessage());
        }
        if (prescriptionDrugList.size() != 0) {
            // save prescriptionDrug
            prescriptionDrugService.savePrescriptionDrug(prescriptionDrugList, prescription.getPrescriptionId());
        }

        return new CimsResponseWrapper<>(true, null, "Save success");
    }

    /**
     * saga:save prescription rollback
     *
     * @param prescriptionDto model
     */
    @Transactional(rollbackFor = Exception.class)
    public void savePrescriptionCancel(PrescriptionDto prescriptionDto) throws Exception {
        //delete prescription
        Integer EncounterId = prescriptionDto.getPrescription().getEncounterId();
        Prescription prescription = prescriptionRepository.findByEncounterId(EncounterId);
        prescriptionRepository.delete(prescription);

        //delete prescriptionDrug
        List<PrescriptionDrug> prescriptionDrugList = prescriptionDto.getPrescriptionDrugList();
        if (prescriptionDrugList.size() != 0) {
            prescriptionDrugService.savePrescriptionDrugCancel(prescription.getPrescriptionId());
        }
    }

    /**
     * update prescription
     *
     * @param prescriptionDrugDto
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public CimsResponseWrapper<String> updatePrescription(PrescriptionDrugDto prescriptionDrugDto) throws Exception {

        Prescription prescription = prescriptionRepository.findByEncounterId(prescriptionDrugDto.getOldPrescription().getEncounterId());
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        prescription.setCreateBy(userDetails.getUsername());
        prescription.setCreateDate(new Date());

        // update prescription
        prescriptionRepository.save(prescription);
        // update prescription_drug
        if (prescriptionDrugDto.getNewPrescriptionDrugList().size() != 0) {
            prescriptionDrugService.updatePrescriptionDrug(prescriptionDrugDto,prescription.getPrescriptionId());
        }

        return new CimsResponseWrapper<String>(true, null, "Update  success");
    }

    /**
     * saga: update prescription rollback
     *
     * @param prescriptionDrugDto
     * @throws Exception
     */
    public void updatePrescriptionCancel(PrescriptionDrugDto prescriptionDrugDto) throws Exception {

        Prescription prescription = prescriptionDrugDto.getOldPrescription();
        prescription.setPrescriptionId(prescriptionRepository.findByEncounterId(prescriptionDrugDto.getOldPrescription().getEncounterId()).getPrescriptionId());

        prescriptionRepository.save(prescription);
        if (prescriptionDrugDto.getNewPrescriptionDrugList().size() != 0) {
            prescriptionDrugService.updatePrescriptionDrugCancel(prescriptionDrugDto,prescription.getPrescriptionId());
        }

    }

    /**
     * get drug history
     *
     * @param patientId patientId
     * @return
     * @throws Exception
     */
    public CimsResponseWrapper<List> listDrugHistory(Integer patientId) throws Exception {
        List<Prescription> prescriptionList = prescriptionRepository.findByPatientId(patientId);
        if (prescriptionList.size() != 0){
            List<DrugHistory> drugHistoryList = new ArrayList<>();
            for (int i = 0; i < prescriptionList.size(); i++) {
                List<PrescriptionDrugBo> prescriptionDrugBoList = prescriptionDrugService.listPrescriptionDrug(prescriptionList.get(i).getPrescriptionId());
                DrugHistory drugHistory = new DrugHistory(prescriptionList.get(i), prescriptionDrugBoList);
                drugHistoryList.add(drugHistory);
            }

            return new CimsResponseWrapper<List>(true, null, drugHistoryList);
        }
        else {
            return new CimsResponseWrapper<List>(true, null, null);
        }

    }


    /**
     * get patient prescription and prescription_drug in this encounter
     *
     * @param encounterId encounterId
     * @return
     * @throws Exception
     */
    public CimsResponseWrapper<DrugHistory> getPrescription(Integer encounterId) throws Exception {
        Prescription prescription = prescriptionRepository.findByEncounterId(encounterId);
        if (prescription != null) {
            List<PrescriptionDrugBo> prescriptionDrugBoList = prescriptionDrugService.listPrescriptionDrug(prescription.getPrescriptionId());
            DrugHistory drugHistory = new DrugHistory(prescription, prescriptionDrugBoList);
            return new CimsResponseWrapper<DrugHistory>(true, null, drugHistory);
        }
        else {
            return new CimsResponseWrapper<DrugHistory>(true, null, null);

        }
    }

}
