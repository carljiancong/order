package com.harmonycloud.service;

import com.alibaba.fastjson.JSONObject;
import com.harmonycloud.bo.PrescriptionDrugBo;
import com.harmonycloud.entity.PrescriptionDrug;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import com.harmonycloud.repository.PrescriptionDrugRepository;
import com.harmonycloud.result.CimsResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionDrugService {
    @Autowired
    private PrescriptionDrugRepository prescriptionDrugRepository;
    @Autowired
    private RocketMqService rocketMqService;

    /**
     * save prescription_drug
     *
     * @param prescriptionDrugList model
     * @param prescriptionId       prescriptionId
     * @return
     * @throws Exception
     */

    public CimsResponseWrapper<String> savePrescriptionDrug(List<PrescriptionDrug> prescriptionDrugList, Integer prescriptionId) throws Exception {

        for (int i = 0; i < prescriptionDrugList.size(); i++) {
            prescriptionDrugList.get(i).setPrescriptionId(prescriptionId);
            if (prescriptionDrugRepository.save(prescriptionDrugList.get(i)).getPrescriptionDrugId() == null) {
                throw new OrderException(ErrorMsgEnum.SAVE_ERROR.getMessage());
            }
        }

        JSONObject json = new JSONObject();
        prescriptionDrugList.forEach(drug -> {
            if (json.containsKey(drug.getDrugId().toString())) {
                json.put(drug.getDrugId().toString(), json.getIntValue(drug.getDrugId().toString()) + 1);
            } else {
                json.put(drug.getDrugId().toString(), 1);
            }
        });
        //send message
        if (json.containsKey("314") || json.containsKey("316")) {
            rocketMqService.sendMsg("OrderTopic", "OrderPush", getInfo(json.getIntValue("314"), json.getIntValue("316")));
        }

        return new CimsResponseWrapper<String>(true, null, "Save success");
    }

    /**
     * saga:save prescription_drug rollback
     *
     * @param prescriptionId prescriptionId
     */
    public void savePrescriptionDrugCancel(Integer prescriptionId) throws Exception {
        List<PrescriptionDrug> prescriptionDrugList = prescriptionDrugRepository.findByPrescriptionId(prescriptionId);
        for (int i = 0; i < prescriptionDrugList.size(); i++) {
            prescriptionDrugRepository.delete(prescriptionDrugList.get(i));
        }
        JSONObject json = new JSONObject();
        prescriptionDrugList.forEach(drug -> {
            if (json.containsKey(drug.getDrugId().toString())) {
                json.put(drug.getDrugId().toString(), json.getIntValue(drug.getDrugId().toString()) + 1);
            } else {
                json.put(drug.getDrugId().toString(), 1);
            }
        });
        //send message
        if (json.containsKey("314") || json.containsKey("316")) {
            rocketMqService.sendMsg("OrderTopic", "OrderPush", "saga:" + getInfo(-json.getIntValue("314"), -json.getIntValue("316")));
        }
    }

    /**
     * update prescription_drug
     *
     * @param prescriptionDrugBo model
     * @return
     */
    public CimsResponseWrapper<String> updatePrescriptionDrug(PrescriptionDrugBo prescriptionDrugBo) throws Exception {
        List<PrescriptionDrug> oldPrescriptionDrugList = prescriptionDrugBo.getOldPrescriptionDrugList();
        List<PrescriptionDrug> newPrescriptionDrugList = prescriptionDrugBo.getNewPrescriptionDrugList();

        prescriptionDrugRepository.deleteAll(oldPrescriptionDrugList);

        for (int i = 0; i < newPrescriptionDrugList.size(); i++) {
            newPrescriptionDrugList.get(i).setPrescriptionId(oldPrescriptionDrugList.get(0).getPrescriptionId());
            if (prescriptionDrugRepository.save(newPrescriptionDrugList.get(i)).getPrescriptionDrugId() == null) {
                throw new OrderException(ErrorMsgEnum.UPDATE_ERROR.getMessage());
            }
        }

        JSONObject oldJson = new JSONObject();
        JSONObject newJson = new JSONObject();
        oldPrescriptionDrugList.forEach(drug -> {
            if (oldJson.containsKey(drug.getDrugId().toString())) {
                oldJson.put(drug.getDrugId().toString(), oldJson.getIntValue(drug.getDrugId().toString()) + 1);
            } else {
                oldJson.put(drug.getDrugId().toString(), 1);
            }
        });
        newPrescriptionDrugList.forEach(drug -> {
            if (newJson.containsKey(drug.getDrugId().toString())) {
                newJson.put(drug.getDrugId().toString(), newJson.getIntValue(drug.getDrugId().toString()) + 1);
            } else {
                newJson.put(drug.getDrugId().toString(), 1);
            }
        });

        //send message
        if (oldJson.containsKey("314") || oldJson.containsKey("316") || newJson.containsKey("314") || newJson.containsKey("316")) {
            rocketMqService.sendMsg("OrderTopicUpdate", "OrderPushUpdate", getInfo((newJson.getIntValue("314") - oldJson.getIntValue("314")), (newJson.getIntValue("316") - oldJson.getIntValue("316"))));
        }
        return new CimsResponseWrapper<String>(true, null, "Update  success");
    }


    /**
     * saga:update Prescription_drug rollback
     *
     * @param prescriptionDrugBo model
     */
    public void updatePrescriptionDrugCancel(PrescriptionDrugBo prescriptionDrugBo) throws Exception {
        List<PrescriptionDrug> oldPrescriptionDrugList = prescriptionDrugBo.getOldPrescriptionDrugList();
        List<PrescriptionDrug> PrescriptionDrugList = prescriptionDrugRepository.findByPrescriptionId(oldPrescriptionDrugList.get(0).getPrescriptionId());
        prescriptionDrugRepository.deleteAll(PrescriptionDrugList);
        prescriptionDrugRepository.saveAll(oldPrescriptionDrugList);

        JSONObject oldJson = new JSONObject();
        JSONObject newJson = new JSONObject();
        oldPrescriptionDrugList.forEach(drug -> {
            if (oldJson.containsKey(drug.getDrugId().toString())) {
                oldJson.put(drug.getDrugId().toString(), oldJson.getIntValue(drug.getDrugId().toString()) + 1);
            } else {
                oldJson.put(drug.getDrugId().toString(), 1);
            }
        });
        PrescriptionDrugList.forEach(drug -> {
            if (newJson.containsKey(drug.getDrugId().toString())) {
                newJson.put(drug.getDrugId().toString(), newJson.getIntValue(drug.getDrugId().toString()) + 1);
            } else {
                newJson.put(drug.getDrugId().toString(), 1);
            }
        });

        if (oldJson.containsKey("314") || oldJson.containsKey("316") || newJson.containsKey("314") || newJson.containsKey("316")) {
            rocketMqService.sendMsg("OrderTopicUpdate", "OrderPushUpdate", "saga:" + getInfo((oldJson.getIntValue("314") - newJson.getIntValue("314")), (oldJson.getIntValue("316") - newJson.getIntValue("316"))));
        }
    }

    private String getInfo(int juniorNum, int adultNum) {
        StringBuilder info = new StringBuilder();

        if (juniorNum != 0) {
            if (juniorNum > 0) {
                info.append(String.format("increase medication Engerix-B Junior x%d ", juniorNum));
            } else {
                info.append(String.format(" reduce medication Engerix-B Junior x%d", Math.abs(juniorNum)));

            }
            if (adultNum != 0) {
                info.append("and");
            }
        }

        if (adultNum != 0) {
            if (adultNum > 0) {
                info.append(String.format(" increase medication Engerix-B Adult x%d", adultNum));
            } else {
                info.append(String.format(" reduce medication Engerix-B Adult x%d", Math.abs(adultNum)));
            }
        }
        return info.toString();
    }


}
