package ng.optisoft.rosabon.credit.controller;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.dto.request.WalletBonusReqDto;
import ng.optisoft.rosabon.credit.service.CBonusSettingService;
import ng.optisoft.rosabon.credit.service.CrRosaBonusScheduleService;
import ng.optisoft.rosabon.dto.GeneralResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
public class CreditBonusSettingController {

    private final CBonusSettingService cBonusSettingService;

    private final CrRosaBonusScheduleService crRosaBonusScheduleService;


    //GET
    @GetMapping(value = ApiRoute.ADMIN +ApiRoute.GET_ALL_BONUS_SCHEDULE)
    public ResponseEntity<?> getAllBonusSettings(@PageableDefault(sort = "id", direction = ASC) Pageable pageable){
        var result = cBonusSettingService.getAllBonusSetting(pageable);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.UPLOAD_BONUS_SCHEDULE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadBonusSchedule(@RequestParam("csvFile")
                                                 MultipartFile csvFile) {

        String url = crRosaBonusScheduleService.uploadBonusSchedule(csvFile);

        cBonusSettingService.saveBonusScheduleAndBeneficiary(url);

        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(), url));
    }

    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADD_BONUS_SET_UP)
    public ResponseEntity<?> setBonus(@RequestBody @Valid WalletBonusReqDto walletBonusReqDto) {

        return ResponseEntity.ok(cBonusSettingService.configureBonus(walletBonusReqDto));
    }
}
