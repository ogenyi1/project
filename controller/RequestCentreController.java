package ng.optisoft.rosabon.controller;


import lombok.RequiredArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dto.request.CustomerRequestInDto;
import ng.optisoft.rosabon.dto.request.TreatCustomerRequestInDto;
import ng.optisoft.rosabon.model.Useraccount.Usage;
import ng.optisoft.rosabon.service.RequestCentreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
//@PreAuthorize("hasAnyAuthority('VIEW_REQUEST','TREAT_REQUEST','SUPER_ADMINISTRATOR')")
@PreAuthorize("hasAnyAuthority('SUPER_ADMINISTRATOR')")
public class RequestCentreController {

    private final RequestCentreService requestCentreService;

    @PostMapping(ApiRoute.AUTH  + ApiRoute.ADMIN + ApiRoute.REQUEST_CENTRE + ApiRoute.CREATE_CUSTOMER_REQUEST)
    public ResponseEntity<?> createCustomerRequest (Authentication auth,
                                                    @RequestBody @Valid CustomerRequestInDto customerRequestInDto,
                                                    @RequestParam(name="admin-usage", required = false) Usage adminUsage) {
        return requestCentreService.createCustomerRequest(auth,customerRequestInDto,adminUsage);
    }
    @PreAuthorize("hasAnyAuthority('VIEW_REQUEST')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REQUEST_CENTRE + ApiRoute.PENDING_REQUESTS)
    public ResponseEntity<?> getAllPendingRequests (Authentication auth,
                                                    @RequestParam(name="admin-usage", required = false) Usage adminUsage) {
        return requestCentreService.getAllPendingRequests(auth,adminUsage);
    }

    @PreAuthorize("hasAnyAuthority('TREAT_REQUEST')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REQUEST_CENTRE + ApiRoute.MY_QUEUE)
    public ResponseEntity<?> getAllRequestsRequiringMyApproval (Authentication auth,
                                                                @RequestParam(name="admin-usage", required = false) Usage adminUsage) {
        return requestCentreService.getAllRequestsRequiringMyApproval(auth,adminUsage);
    }

    @PreAuthorize("hasAnyAuthority('TREAT_REQUEST')")
    @PutMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REQUEST_CENTRE + ApiRoute.TREAT_REQUEST + "/{id}")
    public ResponseEntity<?> treatCustomerRequest (Authentication auth,
                                                   @PathVariable Long id,
                                                   @RequestBody @Valid TreatCustomerRequestInDto dto,
                                                   @RequestParam(name="admin-usage", required = false) Usage adminUsage) {
        return requestCentreService.treatCustomerRequest(auth,id,dto,adminUsage);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REQUEST','TREAT_REQUEST')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REQUEST_CENTRE + ApiRoute.PENDING_REQUESTS + ApiRoute.FIND_BY_PARAM)
    public ResponseEntity<?> findPendingRequestsByParam (Authentication auth, @RequestParam(value = "param") String param,
//                                                        @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
//                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
//                                                        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
//                                                        @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                                                        @RequestParam(name="admin-usage", required = false) Usage adminUsage) {
        return requestCentreService.findPendingRequestsByParam(auth,param,adminUsage);
//        return requestCentreService.findPendingRequestsByParam(auth,param,pageNo,pageSize,sortBy,sortDir,adminUsage);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REQUEST','TREAT_REQUEST')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REQUEST_CENTRE + ApiRoute.MY_QUEUE + ApiRoute.FIND_BY_PARAM)
    public ResponseEntity<?> findQueuedRequestsByParam (Authentication auth, @RequestParam(value = "param") String param,
//                                                        @RequestParam(value = "pageNo", defaultValue = "0",required = false) int pageNo,
//                                                        @RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize,
//                                                        @RequestParam(value = "sortBy", defaultValue = "id",required = false) String sortBy,
//                                                        @RequestParam(value = "sortDir", defaultValue = "desc",required = false) String sortDir,
                                                        @RequestParam(name="admin-usage", required = false) Usage adminUsage
    ) {
//        return requestCentreService.findQueuedRequestsByParam(auth,param,pageNo,pageSize,sortBy,sortDir,adminUsage);
        return requestCentreService.findQueuedRequestsByParam(auth,param,adminUsage);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMINISTRATOR')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REQUEST_CENTRE + ApiRoute.SEPARATOR + "all-requests")
    public ResponseEntity<?> getAllRequests (Authentication auth,
                                             @RequestParam(value = "pageNo", defaultValue = "0",required = false) int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize,
                                             @RequestParam(value = "sortBy", defaultValue = "id",required = false) String sortBy,
                                             @RequestParam(value = "sortDir", defaultValue = "desc",required = false) String sortDir,
                                             @RequestParam(name="admin-usage", required = false) Usage adminUsage) {
        return requestCentreService.getAllRequests(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                adminUsage
        );
    }

    @PreAuthorize("hasAnyAuthority('VIEW_REQUEST')")
    @GetMapping(ApiRoute.AUTH + ApiRoute.ADMIN + ApiRoute.REQUEST_CENTRE + ApiRoute.SEPARATOR + "get-all-approved-or-declined-requests")
    public ResponseEntity<?> getAllApprovedOrDeclinedRequests (Authentication auth,
                                             @RequestParam(value = "pageNo", defaultValue = "0",required = false) int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize,
                                             @RequestParam(value = "sortBy", defaultValue = "id",required = false) String sortBy,
                                             @RequestParam(value = "sortDir", defaultValue = "desc",required = false) String sortDir,
                                             @RequestParam(name="admin-usage", required = false) Usage adminUsage) {
        return requestCentreService.getAllApprovedOrDeclinedRequests(
                auth,
                pageNo,
                pageSize,
                sortBy,
                sortDir,
                adminUsage
        );
    }


}

