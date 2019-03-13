package com.harmonycloud.service;

import com.harmonycloud.bo.PrescriptionDrugBo;
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

    @Transactional(rollbackFor = Exception.class)
    public CimsResponseWrapper<Prescription> savePrescription(PrescriptionDrugBo prescriptionDrugBo) throws Exception {
        Prescription prescription = prescriptionDrugBo.getPrescription();
        List<PrescriptionDrug> prescriptionDrugList = prescriptionDrugBo.getPrescriptionDrugList();
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        Date date = new Date();
        Prescription prescription1 = new Prescription(prescription.getPatientId(), prescription.getEncounterId(), username, date);
        if (prescriptionRepository.save(prescription1).getPrescriptionId() == null) {
            throw new OrderException(ErrorMsgEnum.SAVE_ERROR.getMessage());
        } else {
            prescriptionDrugService.savePrescriptionDrug(prescriptionDrugList, prescription1.getPrescriptionId());
        }
        return new CimsResponseWrapper<>(true, null, prescription1);
    }

    @Transactional(rollbackFor = Exception.class)
    public void savePrescriptionCancel(PrescriptionDrugBo prescriptionDrugBo) {
        Prescription prescription = prescriptionDrugBo.getPrescription();
        List<PrescriptionDrug> prescriptionDrugList = prescriptionDrugBo.getPrescriptionDrugList();
        Prescription prescription1 = prescriptionRepository.findByEncounterId(prescription.getEncounterId());
        prescriptionRepository.delete(prescription);

        prescriptionDrugService.savePrescriptionDrugCancel(prescription1.getPrescriptionId());
    }


}
