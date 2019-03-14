package com.harmonycloud.service;

import com.harmonycloud.bo.PrescriptionDrugBo;
import com.harmonycloud.bo.PrescriptionBo;
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
     * @param prescriptionBo model
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public CimsResponseWrapper<String> savePrescription(PrescriptionBo prescriptionBo) throws Exception {
        Prescription prescription = prescriptionBo.getPrescription();
        List<PrescriptionDrug> prescriptionDrugList = prescriptionBo.getPrescriptionDrugList();
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        prescription.setCreateBy(userDetails.getUsername());
        prescription.setCreateDate(new Date());

        // save prescription
        if (prescriptionRepository.save(prescription).getPrescriptionId() == null) {
            throw new OrderException(ErrorMsgEnum.SAVE_ERROR.getMessage());
        }

        // save prescriptionDrug
        prescriptionDrugService.savePrescriptionDrug(prescriptionDrugList, prescription.getPrescriptionId());

        return new CimsResponseWrapper<>(true, null, "Save success");
    }

    /**
     * saga:save prescription rollback
     *
     * @param prescriptionBo model
     */
    @Transactional(rollbackFor = Exception.class)
    public void savePrescriptionCancel(PrescriptionBo prescriptionBo) throws Exception{
        //delete prescription
        Integer EncounterId = prescriptionBo.getPrescription().getEncounterId();
        Prescription prescription = prescriptionRepository.findByEncounterId(EncounterId);
        prescriptionRepository.delete(prescription);

        //delete prescriptionDrug
        prescriptionDrugService.savePrescriptionDrugCancel(prescription.getPrescriptionId());
    }

    /**
     * update prescription
     *
     * @param prescriptionDrugBo
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public CimsResponseWrapper<String> updatePrescription(PrescriptionDrugBo prescriptionDrugBo) throws Exception {
        Prescription prescription = prescriptionDrugBo.getOldPrescription();
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        prescription.setCreateBy(userDetails.getUsername());
        prescription.setCreateDate(new Date());

        // update prescription
        prescriptionRepository.save(prescription);
        // update prescription_drug
        prescriptionDrugService.updatePrescriptionDrug(prescriptionDrugBo);

        return new CimsResponseWrapper<String>(true, null, "Update  success");
    }

    public void updatePrescriptionCancel(PrescriptionDrugBo prescriptionDrugBo) throws Exception {
        Prescription prescription = prescriptionDrugBo.getOldPrescription();

        prescriptionRepository.save(prescription);

        prescriptionDrugService.updatePrescriptionDrugCancel(prescriptionDrugBo);

    }

    public CimsResponseWrapper<List> listDrugHistory(Integer patientId) throws Exception {
        List<Prescription> prescriptionList = prescriptionRepository.findByPatientId(patientId);
        return new CimsResponseWrapper<List>(true, null, prescriptionList);
    }

}
