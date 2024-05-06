package ng.optisoft.rosabon.credit.controller;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.service.CrCompanyInclusionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CreditCompanyInclusionController {
    private final CrCompanyInclusionService companyInclusionService;

    @GetMapping(value = ApiRoute.USERS + ApiRoute.GET_COMPANY_INCL)
    public ResponseEntity<?> getAllCompanyInclusion(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(companyInclusionService.getAllCompanyInclusion(pageable));
    }

    @GetMapping(value = ApiRoute.USERS + "/{uuid}" +ApiRoute.GET_INCLUSION)
    public ResponseEntity<?> getCompanyTotalWeightAndAvgScoreForAuditCrmAndLease(@PathVariable String uuid){
        return ResponseEntity.ok().body(companyInclusionService.calculateCompanyTotalWeightAndAvgScoreForAuditCrmAndLease(uuid));
    }
}
