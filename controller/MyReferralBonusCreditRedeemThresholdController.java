package ng.optisoft.rosabon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dto.request.MyReferralBonusCreditRedeemThresholdInDto;
import ng.optisoft.rosabon.service.MyReferralBonusCreditRedeemThresholdService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MyReferralBonusCreditRedeemThresholdController {

    private final MyReferralBonusCreditRedeemThresholdService myReferralBonusCreditRedeemThresholdService;

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
    @PostMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MY_REFERRAL_BONUS_CREDIT_REDEEM_THRESHOLD + ApiRoute.CREATE)
    public ResponseEntity<?> createMyReferralBonusCreditRedeemThreshold (@Valid @RequestBody MyReferralBonusCreditRedeemThresholdInDto dto) {
        return myReferralBonusCreditRedeemThresholdService.createMyReferralBonusCreditRedeemThreshold(dto);
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
    @PutMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MY_REFERRAL_BONUS_CREDIT_REDEEM_THRESHOLD + ApiRoute.UPDATE+ "/{id}")
    public ResponseEntity<?> updateMyReferralBonusCreditRedeemThreshold (@Valid @NotNull @PathVariable("id") Long id, @Valid @RequestBody MyReferralBonusCreditRedeemThresholdInDto dto) {
        return myReferralBonusCreditRedeemThresholdService.updateMyReferralBonusCreditRedeemThreshold(id,dto);
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
    @DeleteMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MY_REFERRAL_BONUS_CREDIT_REDEEM_THRESHOLD + ApiRoute.DELETE+ "/{id}")
    public ResponseEntity<?> deleteMyReferralBonusCreditRedeemThreshold (@Valid @NotNull @PathVariable("id") Long id) {
        return myReferralBonusCreditRedeemThresholdService.deleteMyReferralBonusCreditRedeemThreshold(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.MY_REFERRAL_BONUS_CREDIT_REDEEM_THRESHOLD)
    public ResponseEntity<?> getAllMyReferralBonusCreditRedeemThreshold () {
        return myReferralBonusCreditRedeemThresholdService.getAllMyReferralBonusCreditRedeemThreshold();
    }

    @PreAuthorize("permitAll()")
    @GetMapping(ApiRoute.AUTH + ApiRoute.MY_REFERRAL_BONUS_CREDIT_REDEEM_THRESHOLD)
    public ResponseEntity<?> getAllActiveMyReferralBonusCreditRedeemThreshold () {
        return myReferralBonusCreditRedeemThresholdService.getAllActiveMyReferralBonusCreditRedeemThreshold();
    }
}
