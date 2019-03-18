package com.harmonycloud.controller;

import com.harmonycloud.dto.DrugHistory;
import com.harmonycloud.dto.PrescriptionDto;
import com.harmonycloud.dto.PrescriptionDrugDto;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import com.harmonycloud.result.CimsResponseWrapper;
import com.harmonycloud.service.PrescriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class OrderController {

    @Autowired
    private PrescriptionService prescriptionService;


    /**
     * list drug history
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
        return prescriptionService.listDrugHistory(patientId);
    }

    /**
     * get patient prescription in this enocunter
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
        return prescriptionService.getPrescription(encounterId);
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
    public CimsResponseWrapper<String> saveOrder(@RequestBody PrescriptionDto dto) throws Exception {
        if (dto == null || dto.getPrescription().getPatientId() <= 0 || dto.getPrescription().getEncounterId() <= 0) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        prescriptionService.savePrescription(dto);

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
    public CimsResponseWrapper<String> updateOrder(@RequestBody PrescriptionDrugDto dto) throws Exception {
        if (dto == null || dto.getOldPrescriptionDrugList().get(0).getPrescriptionId() <= 0) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        prescriptionService.updatePrescription(dto);
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
    public CimsResponseWrapper<String> savePrescription(@RequestBody PrescriptionDto dto) throws Exception {
        if (dto == null || dto.getPrescription().getPatientId() <= 0 || dto.getPrescription().getEncounterId() <= 0) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        prescriptionService.savePrescription(dto);
        return new CimsResponseWrapper<>(true, null, "Save success");
    }

    /**
     * saga:save medication order rollback
     *
     * @param dto model
     */
    public void savePrescriptionCancel(PrescriptionDto dto) throws Exception {
        prescriptionService.savePrescriptionCancel(dto);
    }

    /**
     * update medication order by next patient button
     *
     * @param dto model
     * @return
     * @throws Exception
     */
    @PostMapping("/updatePrescription")
    @Compensable(compensationMethod = "updatePrescriptionDrugCancel", timeout = 5)
    public CimsResponseWrapper<String> updatePrescription(@RequestBody PrescriptionDrugDto dto) throws Exception {
        if (dto == null) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        prescriptionService.updatePrescription(dto);
        return new CimsResponseWrapper<>(true, null, "Update success");
    }

    /**
     * saga:update medication order rollback
     *
     * @param dto model
     * @throws Exception
     */
    public void updatePrescriptionDrugCancel(PrescriptionDrugDto dto) throws Exception {
        prescriptionService.updatePrescriptionCancel(dto);
    }

}
