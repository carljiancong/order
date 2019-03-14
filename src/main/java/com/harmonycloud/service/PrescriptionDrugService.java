package com.harmonycloud.service;

import com.google.common.collect.Maps;
import com.harmonycloud.bo.PrescriptionDrugBo;
import com.harmonycloud.entity.PrescriptionDrug;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import com.harmonycloud.repository.PrescriptionDrugRepository;
import com.harmonycloud.result.CimsResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PrescriptionDrugService {
    @Autowired
    private PrescriptionDrugRepository prescriptionDrugRepository;

    /**
     * save prescription_drug
     *
     * @param prescriptionDrugList model
     * @param prescriptionId       prescriptionId
     * @return
     * @throws Exception
     */

    public CimsResponseWrapper<String> savePrescriptionDrug(List<PrescriptionDrug> prescriptionDrugList, Integer prescriptionId) throws Exception {

        Map<Integer, PrescriptionDrug> drugMap = Maps.newHashMap();

        for (int i = 0; i < prescriptionDrugList.size(); i++) {
            prescriptionDrugList.get(i).setPrescriptionId(prescriptionId);
            if (prescriptionDrugRepository.save(prescriptionDrugList.get(i)).getPrescriptionDrugId() == null) {
                throw new OrderException(ErrorMsgEnum.SAVE_ERROR.getMessage());
            }
        }
        return new CimsResponseWrapper<>(true, null, "Save success");
    }

    /**
     * saga:save prescription_drug rollback
     *
     * @param prescriptionId prescriptionId
     */
    public void savePrescriptionDrugCancel(Integer prescriptionId) {
        List<PrescriptionDrug> prescriptionDrugList = prescriptionDrugRepository.findByPrescriptionId(prescriptionId);
        for (int i = 0; i < prescriptionDrugList.size(); i++) {
            prescriptionDrugRepository.delete(prescriptionDrugList.get(i));
        }
    }

    /**
     * update prescription_drug
     *
     * @param prescriptionDrugBO model
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CimsResponseWrapper<String> updatePrescriptionDrug(PrescriptionDrugBo prescriptionDrugBO) throws Exception {
        List<PrescriptionDrug> oldPrescriptionDrugList = prescriptionDrugBO.getOldPrescriptionDrugList();
        List<PrescriptionDrug> newPrescriptionDrugList = prescriptionDrugBO.getNewPrescriptionDrugList();

        prescriptionDrugRepository.deleteAll(oldPrescriptionDrugList);

        for (int i = 0; i < newPrescriptionDrugList.size(); i++) {
            newPrescriptionDrugList.get(i).setPrescriptionId(oldPrescriptionDrugList.get(0).getPrescriptionId());
            if (prescriptionDrugRepository.save(newPrescriptionDrugList.get(i)).getPrescriptionDrugId() == null) {
                throw new OrderException(ErrorMsgEnum.UPDATE_ERROR.getMessage());
            }
        }
        return new CimsResponseWrapper<>(true, null, "Update success");
    }


    /**
     * saga:update Prescription_drug rollback
     *
     * @param prescriptionDrugBO model
     */
    public void updatePrescriptionDrugCancel(PrescriptionDrugBo prescriptionDrugBO) throws Exception {
        List<PrescriptionDrug> oldPrescriptionDrugList = prescriptionDrugBO.getOldPrescriptionDrugList();
        List<PrescriptionDrug> PrescriptionDrugList = prescriptionDrugRepository.findByPrescriptionId(oldPrescriptionDrugList.get(0).getPrescriptionId());
        prescriptionDrugRepository.deleteAll(PrescriptionDrugList);
        prescriptionDrugRepository.saveAll(oldPrescriptionDrugList);
    }

}
