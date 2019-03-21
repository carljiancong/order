package com.harmonycloud.controller;

import com.harmonycloud.dto.DrugHistory;
import com.harmonycloud.dto.PrescriptionDto;
import com.harmonycloud.dto.PrescriptionDrugDto;
import com.harmonycloud.entity.Prescription;
import com.harmonycloud.entity.PrescriptionDrug;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import com.harmonycloud.result.CimsResponseWrapper;
import com.harmonycloud.service.PrescriptionDrugService;
import com.harmonycloud.service.PrescriptionService;
import com.harmonycloud.util.LogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api
@RestController
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private PrescriptionDrugService prescriptionDrugService;

    @Autowired
    private HttpServletRequest request;

    /**
     * list drug history by patientId
     *
     * @param patientId patientId
     * @return
     * @throws Exception
     */
    @GetMapping("/drugHistory")
    @ApiOperation(value = "patient drug history", httpMethod = "GET")
    @ApiImplicitParam(name = "patientId", value = "patientId", paramType = "query", dataType = "Integer")
    public CimsResponseWrapper<List> listDrugHistory(@RequestParam("patientId") Integer patientId) throws Exception {
        if (patientId == null || patientId <= 0) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        List<DrugHistory> drugHistoryList = prescriptionService.listDrugHistory(patientId);
        return new CimsResponseWrapper<>(true, null, drugHistoryList);

    }

    /**
     * get patient prescription by encounterId
     *
     * @param encounterId
     * @return
     * @throws Exception
     */
    @GetMapping("/getPrescription")
    @ApiOperation(value = "get prescription  by enocunterId", httpMethod = "GET")
    @ApiImplicitParam(name = "encounterId", value = "encounterId", paramType = "query", dataType = "Integer")
    public CimsResponseWrapper<DrugHistory> getPrescription(@RequestParam("encounterId") Integer encounterId) throws Exception {
        if (encounterId == null || encounterId <= 0) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        DrugHistory drugHistory = prescriptionService.getPrescription(encounterId);

        return new CimsResponseWrapper<>(true, null, drugHistory);
    }

    /**
     * save medication order by save button
     *
     * @param dto model
     * @return
     * @throws Exception
     */
    @PostMapping("/saveOrder")
    @ApiOperation(value = "save medication order by save", httpMethod = "POST")
    @ApiImplicitParam(name = "dto", value = "dto", dataType = "PrescriptionDto")
    @Transactional(rollbackFor = Throwable.class)
    public CimsResponseWrapper<String> saveOrder(@RequestBody PrescriptionDto dto) throws Exception {
        String msg = LogUtil.getRequest(request) + ", information='";

        if (dto == null || dto.getPrescription().getPatientId() == null || dto.getPrescription().getPatientId() <= 0
                || dto.getPrescription().getEncounterId() == null || dto.getPrescription().getEncounterId() <= 0) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        //save prescription
        Prescription prescription = prescriptionService.savePrescription(dto.getPrescription());
        logger.info(msg + "Save prescription success '");

        //save prescription drug
        List<PrescriptionDrug> prescriptionDrugList = dto.getPrescriptionDrugList();
        if (!CollectionUtils.isEmpty(prescriptionDrugList)) {
            prescriptionDrugService.savePrescriptionDrug(prescriptionDrugList, prescription.getPrescriptionId());
        }
        logger.info(msg + "Save prescription drug success '");

        return new CimsResponseWrapper<>(true, null, "Save success");
    }

    /**
     * update medication order by save button
     *
     * @param dto model
     * @return
     * @throws Exception
     */
    @PostMapping("/updateOrder")
    @ApiOperation(value = "update medication order by save", httpMethod = "POST")
    @ApiImplicitParam(name = "dto", value = "dto", dataType = "PrescriptionDrugDto")
    @Transactional(rollbackFor = Throwable.class)
    public CimsResponseWrapper<String> updateOrder(@RequestBody PrescriptionDrugDto dto) throws Exception {
        String msg = LogUtil.getRequest(request) + ", information='";
        if (dto == null) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        //update prescription
        Prescription prescription = prescriptionService.updatePrescription(dto.getPrescription());
        logger.info(msg + "Update prescription success '");

        // update prescription_drug
        if (dto.getOldPrescriptionDrugList() == null && dto.getNewPrescriptionDrugList() == null) {
            return new CimsResponseWrapper<>(true, null, "Update success");
        }

        prescriptionDrugService.updatePrescriptionDrug(dto.getOldPrescriptionDrugList(), dto.getNewPrescriptionDrugList(), prescription.getPrescriptionId());
        logger.info(msg + "Update prescription drug success '");

        return new CimsResponseWrapper<>(true, null, "Update success");
    }


    /**
     * save medication order by next patient button
     *
     * @param dto model
     * @return
     * @throws Exception
     */
    @PostMapping(path = "/savePrescription", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Compensable(compensationMethod = "savePrescriptionCancel", timeout = 10)
    @Transactional(rollbackFor = Throwable.class)
    public CimsResponseWrapper<String> savePrescription(@RequestBody PrescriptionDto dto) throws Exception {
        String msg = LogUtil.getRequest(request) + ", information='";

        if (dto == null || dto.getPrescription().getPatientId() == null || dto.getPrescription().getPatientId() <= 0
                || dto.getPrescription().getEncounterId() == null || dto.getPrescription().getEncounterId() <= 0) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        //save prescription
        Prescription prescription = prescriptionService.savePrescription(dto.getPrescription());
        logger.info(msg + "Save prescription success '");

        //save prescription drug
        List<PrescriptionDrug> prescriptionDrugList = dto.getPrescriptionDrugList();
        if (!CollectionUtils.isEmpty(prescriptionDrugList)) {
            prescriptionDrugService.savePrescriptionDrug(prescriptionDrugList, prescription.getPrescriptionId());
        }
        logger.info(msg + "Save prescription drug success '");
        return new CimsResponseWrapper<>(true, null, "Save success");
    }

    /**
     * saga:save medication order rollback
     *
     * @param dto model
     */
    public void savePrescriptionCancel(PrescriptionDto dto) throws Exception {
        String msg = LogUtil.getRequest(request) + ", information='";
        logger.info(msg + "sage ----> save prescription cancel");
        //save precription cancel
        Prescription prescription = prescriptionService.savePrescriptionCancel(dto.getPrescription());

        logger.info(msg + "sage ----> save prescription drug cancel");
        //delete prescriptionDrug
        if (dto.getPrescriptionDrugList() == null) {
            prescriptionDrugService.savePrescriptionDrugCancel(prescription.getPrescriptionId());
        }
    }

    /**
     * update medication order by next patient button
     *
     * @param dto model
     * @return
     * @throws Exception
     */
    @PostMapping("/updatePrescription")
    @Compensable(compensationMethod = "updatePrescriptionDrugCancel", timeout = 10)
    @Transactional(rollbackFor = Throwable.class)
    public CimsResponseWrapper<String> updatePrescription(@RequestBody PrescriptionDrugDto dto) throws Exception {
        String msg = LogUtil.getRequest(request) + ", information='";

        if (dto == null) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        //update prescription
        Prescription prescription = prescriptionService.updatePrescription(dto.getPrescription());
        logger.info(msg + "Update prescription success '");

        if (dto.getOldPrescriptionDrugList() == null && dto.getNewPrescriptionDrugList() == null) {
            return new CimsResponseWrapper<>(true, null, "Update success");
        }
        // update prescription_drug
        prescriptionDrugService.updatePrescriptionDrug(dto.getOldPrescriptionDrugList(), dto.getNewPrescriptionDrugList(), prescription.getPrescriptionId());
        logger.info(msg + "Update prescription drug success '");

        return new CimsResponseWrapper<>(true, null, "Update success");
    }

    /**
     * saga:update medication order rollback
     *
     * @param dto model
     * @throws Exception
     */
    public void updatePrescriptionDrugCancel(PrescriptionDrugDto dto) throws Exception {
        String msg = LogUtil.getRequest(request) + ", information='";
        logger.info(msg + "sage ----> Update prescription cancel");
        Prescription prescription = prescriptionService.updatePrescriptionCancel(dto.getPrescription());

        if (dto.getOldPrescriptionDrugList() != null || dto.getNewPrescriptionDrugList() != null) {
            logger.info(msg + "sage ----> Update prescription drug cancel");
            prescriptionDrugService.updatePrescriptionDrugCancel(dto.getOldPrescriptionDrugList(), prescription.getPrescriptionId());
        }
    }

}
