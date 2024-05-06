package ng.optisoft.rosabon.credit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.externalapis.erp.BasicErpService;
import ng.optisoft.rosabon.credit.externalapis.erp.pojo.ERPProdGenericResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Author Olaleye Tosin
 * January 2024
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class CrErpUtilityController {

    private final BasicErpService basicErpService;

    @GetMapping(value = ApiRoute.USERS + ApiRoute.VENDORS)
    public List<ERPProdGenericResponse> getAllVendors() {
        return basicErpService.handleGetAllVendor();
    }

    @GetMapping(value = ApiRoute.USERS + ApiRoute.LEASE_BRAND)
    public List<ERPProdGenericResponse> getAllLeaseBrand() {
        return basicErpService.handleGetAllLeaseBrand();
    }

    //    @GetMapping(ApiRoute.USERS + "{productId}" + ApiRoute.LEASE_TYPES +"{yearOfManufacture}")
    @GetMapping("/lease-type" + ApiRoute.SEPARATOR + "{productId}" + ApiRoute.SEPARATOR + "{yearOfManufacture}")
    public String getLoanOrLeaseTypes(@PathVariable String productId, @PathVariable Integer yearOfManufacture) {
        return basicErpService.handleGetLoanOrLeaseType(productId, yearOfManufacture);
    }

    @GetMapping(ApiRoute.USERS + "{productId}" + ApiRoute.LEASE_STATUS)
    public List<ERPProdGenericResponse> getLeaseStatus(@PathVariable String productId) {
        return basicErpService.handleGetLeaseStatus(productId);
    }

    @GetMapping(ApiRoute.USERS + "{productId}" + ApiRoute.LEASE_PRODUCT_TYPE)
    public List<ERPProdGenericResponse> getLeaseProductType(@PathVariable String productId) {
        return basicErpService.handleLeaseProductType(productId);
    }

    @GetMapping("/years-of-vh-manufacture")
    public List<Integer> getVehicleYearOfManufactureInLast24Years() {
        return basicErpService.getVehicleYearOfManufacture();
    }

}
