package com.harmonycloud.controller;

import com.harmonycloud.bo.PrescriptionDrugBo;
import com.harmonycloud.result.CimsResponseWrapper;
import com.harmonycloud.service.PrescriptionDrugService;
import com.harmonycloud.service.PrescriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class OrderController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionDrugService prescriptionDrugService;

    @PostMapping("/saveOrder")
    @ApiOperation(value = "save medication order by save", httpMethod = "POST")
    @ApiImplicitParam(name = "prescriptionDrugBo", value = "prescriptionDrugBo", dataType = "PrescriptionDrugBo")
    public CimsResponseWrapper<String> saveOrder(@RequestBody PrescriptionDrugBo prescriptionDrugBo) throws Exception {

        prescriptionService.savePrescription(prescriptionDrugBo);

        return new CimsResponseWrapper<String>(true, null, "Save success");
    }

    @PostMapping("/savePrescription")
    @ApiOperation(value = "save medication order by nest patient", httpMethod = "POST")
    @ApiImplicitParam(name = "prescriptionDrugBo", value = "prescriptionDrugBo", dataType = "PrescriptionDrugBo")
    @Compensable(compensationMethod = "savePrescriptionCancel", timeout = 10)
    public CimsResponseWrapper<String> savePrescription(@RequestBody PrescriptionDrugBo prescriptionDrugBo) throws Exception {

        prescriptionService.savePrescription(prescriptionDrugBo);

        return new CimsResponseWrapper<String>(true, null, "Save success");
    }

    public void savePrescriptionCancel(PrescriptionDrugBo prescriptionDrugBo) {
        prescriptionService.savePrescriptionCancel(prescriptionDrugBo);
    }

}
