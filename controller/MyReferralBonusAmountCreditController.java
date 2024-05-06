package ng.optisoft.rosabon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dto.request.MyReferralBonusAmountCreditInDto;
import ng.optisoft.rosabon.service.MyReferralBonusAmountCreditService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MyReferralBonusAmountCreditController {

    private final MyReferralBonusAmountCreditService myReferralBonusAmountCreditService;

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
    @PostMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MY_REFERRAL_BONUS_AMOUNT_CREDIT + ApiRoute.CREATE)
    public ResponseEntity<?> createMyReferralBonusAmountCredit (@Valid @RequestBody MyReferralBonusAmountCreditInDto dto) {
        return myReferralBonusAmountCreditService.createMyReferralBonusAmountCredit(dto);
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
    @PutMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MY_REFERRAL_BONUS_AMOUNT_CREDIT + ApiRoute.UPDATE + "/{id}")
    public ResponseEntity<?> updateMyReferralBonusAmountCredit (@Valid @NotNull @PathVariable("id") Long id, @Valid @RequestBody MyReferralBonusAmountCreditInDto dto) {
        return myReferralBonusAmountCreditService.updateMyReferralBonusAmountCredit(id,dto);
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MY_REFERRAL_BONUS_AMOUNT_CREDIT + ApiRoute.DELETE + "/{id}")
    public ResponseEntity<?> deleteMyReferralBonusAmountCredit (@Valid @NotNull @PathVariable("id") Long id) {
        return myReferralBonusAmountCreditService.deleteMyReferralBonusAmountCredit(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MY_REFERRAL_BONUS_AMOUNT_CREDIT)
    public ResponseEntity<?> getAllMyReferralBonusAmountCredit ()  {
        return myReferralBonusAmountCreditService.getAllMyReferralBonusAmountCredit();
    }

    @PreAuthorize("permitAll()")
    @GetMapping(ApiRoute.AUTH + ApiRoute.MY_REFERRAL_BONUS_AMOUNT_CREDIT)
    public ResponseEntity<?> getAllActiveMyReferralBonusAmountCredit () {
        return myReferralBonusAmountCreditService.getAllActiveMyReferralBonusAmountCredit();
    }
}
