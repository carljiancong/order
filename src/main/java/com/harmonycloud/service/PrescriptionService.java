package com.harmonycloud.service;

import com.harmonycloud.bo.PrescriptionDrugBo;
import com.harmonycloud.dto.DrugHistory;
import com.harmonycloud.bo.UserPrincipal;
import com.harmonycloud.entity.Prescription;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import com.harmonycloud.repository.PrescriptionRepository;
import com.harmonycloud.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PrescriptionService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionDrugService prescriptionDrugService;

    @Autowired
    HttpServletRequest request;


    /**
     * save prescription
     *
     * @param prescription model
     * @return prescrition
     * @throws Exception
     */
    public Prescription savePrescription(Prescription prescription) throws Exception {

        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        prescription.setCreateBy(userDetails.getUsername());
        prescription.setCreateDate(new Date());


        if (prescriptionRepository.save(prescription).getPrescriptionId() == null) {
            throw new OrderException(ErrorMsgEnum.SAVE_ERROR.getMessage());
        }

        return prescription;
    }

    /**
     * saga:save prescription rollback
     * @param prescription model
     * @return prescription
     * @throws Exception
     */

    public Prescription savePrescriptionCancel(Prescription prescription) throws Exception {


        Prescription oldPrescription = prescriptionRepository.findByEncounterId(prescription.getEncounterId());
        prescriptionRepository.delete(oldPrescription);

        return prescription;
    }

    /**
     * update prescription
     *
     * @param oldPrescription model
     * @return prescription
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public Prescription updatePrescription(Prescription oldPrescription) throws Exception {

        Prescription prescription = prescriptionRepository.findByEncounterId(oldPrescription.getEncounterId());
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        prescription.setCreateBy(userDetails.getUsername());
        prescription.setCreateDate(new Date());


        prescriptionRepository.save(prescription);

        return prescription;
    }

    /**
     * saga: update prescription rollback
     * @param prescription model
     * @return prescription
     * @throws Exception
     */

    public Prescription updatePrescriptionCancel(Prescription prescription) throws Exception {


        prescription.setPrescriptionId(prescriptionRepository.findByEncounterId(prescription.getEncounterId()).getPrescriptionId());
        prescriptionRepository.save(prescription);

        return prescription;

    }

    /**
     * get drug history
     *
     * @param patientId patientId
     * @return List
     * @throws Exception
     */
    public List<DrugHistory> listDrugHistory(Integer patientId) throws Exception {
        String msg = LogUtil.getRequest(request) + ", information='";

        List<Prescription> prescriptionList = prescriptionRepository.findByPatientId(patientId);
        if (!CollectionUtils.isEmpty(prescriptionList)) {
            List<DrugHistory> drugHistoryList = new ArrayList<>();
            for (Prescription prescription : prescriptionList) {
                List<PrescriptionDrugBo> prescriptionDrugBoList = prescriptionDrugService.listPrescriptionDrug(prescription.getPrescriptionId());
                DrugHistory drugHistory = new DrugHistory(prescription, prescriptionDrugBoList);
                drugHistoryList.add(drugHistory);
            }
            return drugHistoryList;
        } else {
            logger.info(msg + "the patient:{} has no prescription list", patientId);
            return null;
        }

    }


    /**
     * get patient prescription and prescription_drug in this encounter
     *
     * @param encounterId encounterId
     * @return DrugHistory
     * @throws Exception
     */
    public DrugHistory getPrescription(Integer encounterId) throws Exception {
        Prescription prescription = prescriptionRepository.findByEncounterId(encounterId);
        if (prescription != null) {
            List<PrescriptionDrugBo> prescriptionDrugBoList = prescriptionDrugService.listPrescriptionDrug(prescription.getPrescriptionId());
            DrugHistory drugHistory = new DrugHistory(prescription, prescriptionDrugBoList);
            return drugHistory;
        } else {
            return null;

        }
    }

}
