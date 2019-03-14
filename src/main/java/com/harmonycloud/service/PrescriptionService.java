package com.harmonycloud.service;

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
    public CimsResponseWrapper<Prescription> savePrescription(PrescriptionDto prescriptionDto) throws Exception {
        Prescription prescription = prescriptionDto.getPrescription();
        List<PrescriptionDrug> prescriptionDrugList = prescriptionDto.getPrescriptionDrugList();
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        prescription.setCreateBy(userDetails.getUsername());
        prescription.setCreateDate(new Date());

        // save prescription
        if (prescriptionRepository.save(prescription).getPrescriptionId() == null) {
            throw new OrderException(ErrorMsgEnum.SAVE_ERROR.getMessage());
        }

        // save prescriptionDrug
        prescriptionDrugService.savePrescriptionDrug(prescriptionDrugList, prescription.getPrescriptionId());

        return new CimsResponseWrapper<>(true, null, prescription);
    }

    /**
     * saga:save prescription rollback
     *
     * @param prescriptionDto model
     */
    @Transactional(rollbackFor = Exception.class)
    public void savePrescriptionCancel(PrescriptionDto prescriptionDto) {
        //delete prescription
        Integer EncounterId = prescriptionDto.getPrescription().getEncounterId();
        Prescription prescription = prescriptionRepository.findByEncounterId(EncounterId);
        prescriptionRepository.delete(prescription);

        //delete prescriptionDrug
        prescriptionDrugService.savePrescriptionDrugCancel(prescription.getPrescriptionId());
    }


    public CimsResponseWrapper<List> listDrugHistory(Integer patientId) throws Exception {
        List<Prescription> prescriptionList = prescriptionRepository.findByPatientId(patientId);
        return new CimsResponseWrapper<List>(true, null, prescriptionList);
    }

}
