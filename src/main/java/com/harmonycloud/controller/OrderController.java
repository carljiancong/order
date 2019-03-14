package com.harmonycloud.controller;

import com.harmonycloud.dto.PrescriptionDto;
import com.harmonycloud.bo.PrescriptionDrugBo;
import com.harmonycloud.enums.ErrorMsgEnum;
import com.harmonycloud.exception.OrderException;
import com.harmonycloud.result.CimsResponseWrapper;
import com.harmonycloud.service.PrescriptionDrugService;
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

    @Autowired
    private PrescriptionDrugService prescriptionDrugService;


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
     * @param bo model
     * @return
     * @throws Exception
     */
    @PostMapping("/updateOrder")
    @ApiOperation(value = "update medication order by save", httpMethod = "POST")
    @ApiImplicitParam(name = "bo", value = "bo", dataType = "PrescriptionDrugBo")
    public CimsResponseWrapper<String> updateOrder(@RequestBody PrescriptionDrugBo bo) throws Exception {
        if (bo == null || bo.getOldPrescriptionDrugList().get(0).getPrescriptionId() <= 0) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        prescriptionDrugService.updatePrescriptionDrug(bo);
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
     * @param prescriptionDto model
     */
    public void savePrescriptionCancel(PrescriptionDto prescriptionDto) throws Exception {
        prescriptionService.savePrescriptionCancel(prescriptionDto);
    }

    /**
     * update medication order by next patient button
     *
     * @param bo model
     * @return
     * @throws Exception
     */
    @PostMapping("/updatePrescription")
    @Compensable(compensationMethod = "updatePrescriptionDrugCancel", timeout = 10)
    public CimsResponseWrapper<String> updatePrescription(@RequestBody PrescriptionDrugBo bo) throws Exception {
        if (bo == null || bo.getOldPrescriptionDrugList().get(0).getPrescriptionId() <= 0) {
            throw new OrderException(ErrorMsgEnum.PARAMETER_ERROR.getMessage());
        }
        prescriptionDrugService.updatePrescriptionDrug(bo);
        return new CimsResponseWrapper<>(true, null, "Update success");
    }

    /**
     * saga:update medication order rollback
     * @param bo model
     * @throws Exception
     */
    public void updatePrescriptionDrugCancel(PrescriptionDrugBo bo) throws Exception {
        prescriptionDrugService.updatePrescriptionDrugCancel(bo);
    }

}
