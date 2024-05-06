package ng.optisoft.rosabon.credit.controller;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.dto.EmployeeErpDTO;
import ng.optisoft.rosabon.credit.dto.EmployeeIndustryErpDTO;
import ng.optisoft.rosabon.credit.dto.ProductErpDTO;
import ng.optisoft.rosabon.credit.dto.request.PenalChargeReqDto;
import ng.optisoft.rosabon.credit.externalapis.erp.BasicErpService;
import ng.optisoft.rosabon.credit.service.CrErpEntitiesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Author Olaleye Tosin
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class CrErpProductMgtController {
    private final BasicErpService basicErpService;
    private final CrErpEntitiesService erpEntitiesService;

    /**
     *
     * GET MAPPING
     * @return
     */
    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_ERP_PRODUCTS)
    public ResponseEntity<List<ProductErpDTO>> getAllErpProducts(){
        return erpEntitiesService.getAllErpProducts();
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_ERP_EMPLOYERS)
    public ResponseEntity<List<EmployeeErpDTO>> getAllErpEmployers(){
        return erpEntitiesService.getAllErpEmployer();
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.GET_ERP_EMPLOYERS_INDUSTRY)
    public ResponseEntity<List<EmployeeIndustryErpDTO>> getAllErpEmployersIndustry(){
        return erpEntitiesService.getAllErpEmployerIndustry();
    }

    @GetMapping(value = ApiRoute.ADMIN + "/{id}" +  ApiRoute.CREATE_REPAYMENT_TYPE)
    public ResponseEntity<?> createRepaymentType(@PathVariable String id) {
        return basicErpService.handleGetErpRepaymentType(id);
    }
}
