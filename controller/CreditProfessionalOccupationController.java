package ng.optisoft.rosabon.credit.controller;

import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.credit.service.CrProfOccupationService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CreditProfessionalOccupationController {
    private final CrProfOccupationService profOccupationService;

    @GetMapping(value = ApiRoute.USERS + ApiRoute.GET_OCCUPATION)
    public ResponseEntity<?> getProfOccupation(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        return ResponseEntity.ok().body(profOccupationService.getAllPerfection(pageable));
    }
}
