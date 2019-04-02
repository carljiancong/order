package com.harmonycloud.service;

import com.alibaba.fastjson.JSONObject;
import com.harmonycloud.bo.PrescriptionDrugBo;
import com.harmonycloud.bo.UserPrincipal;
import com.harmonycloud.config.OrderConfigurationProperties;
import com.harmonycloud.entity.PrescriptionDrug;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import com.harmonycloud.repository.PrescriptionDrugRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionDrugService {

    @Autowired
    private PrescriptionDrugRepository prescriptionDrugRepository;
    @Autowired
    private RocketMqService rocketMqService;
    @Autowired
    private SyncService syncService;

    @Autowired
    private OrderConfigurationProperties config;

    @Autowired
    private HttpServletRequest request;


    /**
     * save prescription_drug
     *
     * @param prescriptionDrugList List<PrescriptionDrug>
     * @param prescriptionId       prescriptionId
     * @throws Exception
     */

    public void savePrescriptionDrug(List<PrescriptionDrug> prescriptionDrugList, Integer prescriptionId) throws Exception {

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
            rocketMqService.sendMsg("OrderTopic", "OrderPush", getInfo(json.getIntValue("314"), json.getIntValue("316")), request);
        }
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
            rocketMqService.sendMsg("OrderTopic", "OrderPush", "saga:" + getInfo(-json.getIntValue("314"), -json.getIntValue("316")), request);
        }
    }

    /**
     * update prescription drug
     *
     * @param oldPrescriptionDrugList List<PrescriptionDrug>
     * @param newPrescriptionDrugList List<PrescriptionDrug>
     * @param prescriptionId          prescriptionId
     * @throws Exception
     */
    public void updatePrescriptionDrug(List<PrescriptionDrug> oldPrescriptionDrugList, List<PrescriptionDrug> newPrescriptionDrugList, Integer prescriptionId) throws Exception {

        JSONObject oldJson = new JSONObject();
        JSONObject newJson = new JSONObject();

        if (oldPrescriptionDrugList != null) {
            prescriptionDrugRepository.deleteAll(oldPrescriptionDrugList);
            oldPrescriptionDrugList.forEach(drug -> {
                if (oldJson.containsKey(drug.getDrugId().toString())) {
                    oldJson.put(drug.getDrugId().toString(), oldJson.getIntValue(drug.getDrugId().toString()) + 1);
                } else {
                    oldJson.put(drug.getDrugId().toString(), 1);
                }
            });
        }

        if (newPrescriptionDrugList != null) {
            for (int i = 0; i < newPrescriptionDrugList.size(); i++) {
                newPrescriptionDrugList.get(i).setPrescriptionId(prescriptionId);
                if (prescriptionDrugRepository.save(newPrescriptionDrugList.get(i)).getPrescriptionDrugId() == null) {
                    throw new OrderException(ErrorMsgEnum.UPDATE_ERROR.getMessage());
                }
            }
            newPrescriptionDrugList.forEach(drug -> {
                if (newJson.containsKey(drug.getDrugId().toString())) {
                    newJson.put(drug.getDrugId().toString(), newJson.getIntValue(drug.getDrugId().toString()) + 1);
                } else {
                    newJson.put(drug.getDrugId().toString(), 1);
                }
            });
        }


        //send message
        if (oldJson.containsKey("314") || oldJson.containsKey("316") || newJson.containsKey("314") || newJson.containsKey("316")) {
            rocketMqService.sendMsg("OrderTopic", "OrderPush",
                    getInfo((newJson.getIntValue("314") - oldJson.getIntValue("314")), (newJson.getIntValue("316") - oldJson.getIntValue("316"))), request);
        }
    }


    /**
     * saga:update Prescription_drug rollback
     *
     * @param oldPrescriptionDrugList List<prescriptionDrug>
     * @param prescriptionId          prescrptionId
     * @throws Exception
     */
    public void updatePrescriptionDrugCancel(List<PrescriptionDrug> oldPrescriptionDrugList, Integer prescriptionId) throws Exception {

        List<PrescriptionDrug> newPrescriptionDrugList = prescriptionDrugRepository.findByPrescriptionId(prescriptionId);
        JSONObject oldJson = new JSONObject();
        JSONObject newJson = new JSONObject();
        if (newPrescriptionDrugList != null) {
            prescriptionDrugRepository.deleteAll(newPrescriptionDrugList);
            newPrescriptionDrugList.forEach(drug -> {
                if (newJson.containsKey(drug.getDrugId().toString())) {
                    newJson.put(drug.getDrugId().toString(), newJson.getIntValue(drug.getDrugId().toString()) + 1);
                } else {
                    newJson.put(drug.getDrugId().toString(), 1);
                }
            });
        }
        if (oldPrescriptionDrugList.size() != 0) {
            prescriptionDrugRepository.saveAll(oldPrescriptionDrugList);
            oldPrescriptionDrugList.forEach(drug -> {
                if (oldJson.containsKey(drug.getDrugId().toString())) {
                    oldJson.put(drug.getDrugId().toString(), oldJson.getIntValue(drug.getDrugId().toString()) + 1);
                } else {
                    oldJson.put(drug.getDrugId().toString(), 1);
                }
            });
        }

        if (oldJson.containsKey("314") || oldJson.containsKey("316") || newJson.containsKey("314") || newJson.containsKey("316")) {
            rocketMqService.sendMsg("OrderTopic", "OrderPush",
                    "saga:" + getInfo((oldJson.getIntValue("314") - newJson.getIntValue("314")), (oldJson.getIntValue("316") - newJson.getIntValue("316"))), request);
        }
    }

    /**
     * get PrescriptionDrugBoList
     *
     * @param prescriptionId prescriptionId
     * @return List
     * @throws Exception
     */
    public List<PrescriptionDrugBo> listPrescriptionDrug(Integer prescriptionId) throws Exception {

        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String token = userDetails.getToken();
        List<PrescriptionDrug> prescriptionDrugList = prescriptionDrugRepository.findByPrescriptionId(prescriptionId);
        if (!CollectionUtils.isEmpty(prescriptionDrugList)) {
            List<PrescriptionDrugBo> prescriptionDrugBoList = new ArrayList<>();
            Integer[] drugIdList = new Integer[prescriptionDrugList.size()];


            for (int i = 0; i < prescriptionDrugList.size(); i++) {
                drugIdList[i] = prescriptionDrugList.get(i).getDrugId();
            }
            //get drug list
            Object test = syncService.save(config.getDrugUri(), token, drugIdList).getData();

            Map<Integer, Map> tmp = (Map<Integer, Map>) test;

            prescriptionDrugList.forEach(prescriptionDrug -> {
                Map<String, Object> drug = tmp.get(prescriptionDrug.getDrugId().toString());
                PrescriptionDrugBo prescriptionDrugBo = new PrescriptionDrugBo(prescriptionDrug.getPrescriptionDrugId(), prescriptionDrug.getDrugId(),
                        drug.get("tradeName").toString(), drug.get("ingredient").toString(), prescriptionDrug.getRegimenLine(), prescriptionId);
                prescriptionDrugBoList.add(prescriptionDrugBo);
            });
            return prescriptionDrugBoList;
        } else {
            return null;
        }

    }

    /**
     * msg format
     *
     * @param juniorNum medication junior number
     * @param adultNum  medication adult number
     * @return String
     */
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
