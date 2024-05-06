package ng.optisoft.rosabon.credit.controller;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.enums.TargetMarketEnum;
import ng.optisoft.rosabon.credit.service.CrTargetMarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CreditTargetMarketController {
    private final CrTargetMarketService targetMarketService;

    @GetMapping(value = ApiRoute.USERS + ApiRoute.USER_TYPES)
    public ResponseEntity<?> getAllUserTypeForSignUp(){
        return ResponseEntity.ok().body(targetMarketService.getAllByTargetMarket(TargetMarketEnum.INDIVIDUAL.name()));
    }

    @GetMapping(value = ApiRoute.USERS + ApiRoute.BUSINESS_TYPE)
    public ResponseEntity<?> getAllBusinessTypeForSignUp(){
        return ResponseEntity.ok().body(targetMarketService.getAllByTargetMarket(TargetMarketEnum.CORPORATE.name()));
    }
}
