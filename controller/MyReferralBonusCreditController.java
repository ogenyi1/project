package ng.optisoft.rosabon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.service.MyReferralBonusCreditService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MyReferralBonusCreditController {

    private final MyReferralBonusCreditService myReferralBonusCreditService;

    @GetMapping(ApiRoute.AUTH + ApiRoute.MY_REFERRAL_BONUS_CREDIT)
    public ResponseEntity<?> getMyReferralBonusCredit (Authentication auth) {
        return myReferralBonusCreditService.getMyReferralBonusCredit(auth);
    }

    @PostMapping(ApiRoute.AUTH + ApiRoute.MY_REFERRAL_BONUS_CREDIT + ApiRoute.REDEEM_BONUS)
    public ResponseEntity<?> redeemEarnedReferralBonus (Authentication auth) {
        return myReferralBonusCreditService.redeemEarnedReferralBonus(auth);
    }
}
