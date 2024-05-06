package ng.optisoft.rosabon.credit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.dto.request.CrGiftSpecialEarningInDto;
import ng.optisoft.rosabon.credit.service.CrRosabonSpecialEarningsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CreditRosabonSpecialEarningsController {

    private final CrRosabonSpecialEarningsService rosabonSpecialEarningsService;

    @GetMapping(ApiRoute.AUTH + ApiRoute.CREDIT + ApiRoute.ROSABON_SPECIAL_EARNINGS)
    public ResponseEntity<?> getCrRosabonSpecialEarnings (Authentication auth) {
        return rosabonSpecialEarningsService.getCrRosabonSpecialEarnings(auth);
    }

    @PostMapping(ApiRoute.AUTH + ApiRoute.CREDIT + ApiRoute.ROSABON_SPECIAL_EARNINGS + ApiRoute.REDEEM_BONUS)
    public ResponseEntity<?> redeemTotalEarnings (Authentication auth) {
        return rosabonSpecialEarningsService.redeemTotalEarnings(auth);
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
    @PostMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.CREDIT + ApiRoute.ROSABON_SPECIAL_EARNINGS + ApiRoute.GIFT)
    public ResponseEntity<?> giftSpecialEarning (@Valid @RequestBody CrGiftSpecialEarningInDto dto) {
        return rosabonSpecialEarningsService.giftSpecialEarning(dto);
    }
}
