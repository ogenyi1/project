package ng.optisoft.rosabon.controller;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.service.CreditUserService;
import ng.optisoft.rosabon.dto.SwitchPlatformDto;
import ng.optisoft.rosabon.dto.response.HttpResponseDto2;
import ng.optisoft.rosabon.service.PlatformSwitchingService;
import ng.optisoft.rosabon.service.UseraccountMngr;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CreditTreasuryController {
    private final PlatformSwitchingService platformSwitchingService;
    private  final UseraccountMngr useraccountMngr;

    private final CreditUserService creditUserService;

    @GetMapping(value = ApiRoute.USERS + ApiRoute.EXISTING_CASHBACKED)
    public ResponseEntity<?>  getUserExistingCashBackedLoan(Authentication auth) {

        return creditUserService.getCurrentUserCashBackedLoanDetail(auth);
    }
    @PostMapping(value = ApiRoute.USERS + "/switch-platform")
    public ResponseEntity<HttpResponseDto2> switchPlatform(Authentication authentication) {
        return platformSwitchingService.switchPlatform(authentication);
    }

    @PostMapping(value = ApiRoute.USERS + "/update-credit-user-type")
    public ResponseEntity<HttpResponseDto2> updateUserType(Authentication authentication,  @Valid @RequestBody SwitchPlatformDto dto) {
        return platformSwitchingService.updateUserType(authentication, dto);
    }

    @PostMapping(ApiRoute.USERS + "/get-treasury-funds-value")
    public ResponseEntity<HttpResponseDto2> getTreasuryFundsValue () {
        return useraccountMngr.getTreasuryFundsValue();
    }

    @PostMapping(ApiRoute.USERS + "/update-cash-backed-loan-status")
    public ResponseEntity<HttpResponseDto2> updateCashBackedLoanStatus () {
        return useraccountMngr.updateCashBackedLoanStatus();
    }


}
