    package ng.optisoft.rosabon.controller;
    
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import ng.optisoft.rosabon.constant.ApiRoute;
    import ng.optisoft.rosabon.dto.request.USSDCodeInDto;
    import ng.optisoft.rosabon.service.USSDCodeService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;
    
    import javax.validation.Valid;
    import javax.validation.constraints.NotNull;
    
    @RestController
    @RequiredArgsConstructor
    @Slf4j
    public class USSDCodeController {
    
        private final USSDCodeService ussdCodeService;
    
        @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
        @PostMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.USSD_CODES + ApiRoute.CREATE)
        public ResponseEntity<?> createUSSDCode (@Valid @RequestBody USSDCodeInDto dto) {
            return ussdCodeService.createUSSDCode(dto);
        }
    
        @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
        @PutMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.USSD_CODES + ApiRoute.UPDATE + "/{id}")
        public ResponseEntity<?> updateUSSDCode (@Valid @NotNull @PathVariable("id") Long id, @Valid @RequestBody USSDCodeInDto dto) {
            return ussdCodeService.updateUSSDCode(id,dto);
        }
    
        @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
        @DeleteMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.USSD_CODES + ApiRoute.DELETE + "/{id}")
        public ResponseEntity<?> deleteUSSDCode (@Valid @NotNull @PathVariable("id") Long id) {
            return ussdCodeService.deleteUSSDCode(id);
        }
    
        @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
        @GetMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.USSD_CODES)
        public ResponseEntity<?> getAllUssdCodes () {
            return ussdCodeService.getAllUssdCodes();
        }
    
        @PreAuthorize("permitAll()")
        @GetMapping(ApiRoute.AUTH + ApiRoute.USSD_CODES)
        public ResponseEntity<?> getAllActiveUssdCodes () {
            return ussdCodeService.getAllActiveUssdCodes();
        }
    
        @PreAuthorize("permitAll()")
        @GetMapping(ApiRoute.AUTH + ApiRoute.USSD_CODES + ApiRoute.FIND_BY_PARAM)
        public ResponseEntity<?> findAllByParam(@RequestParam(value = "param") String param) {
            return ussdCodeService.findAllByParam(param);
        }
    }
