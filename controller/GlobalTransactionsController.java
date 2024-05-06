package ng.optisoft.rosabon.controller;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.service.GlobalTransactnMngr;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.model.GlobalTransaction;
import ng.optisoft.rosabon.model.Useraccount;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@AllArgsConstructor
public class GlobalTransactionsController {

    private GlobalTransactnMngr globalTransactnMngr;
    private UseraccountDao useraccountDao;

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.TRANSACTIONS + ApiRoute.ALL)
    public ResponseEntity<?> getTransactionsBy(Authentication auth,
                                          @RequestParam(required = false) String search,
                                          @RequestParam(required = false) GlobalTransaction.PaymentStatus paymentStatus,
                                          @RequestParam(required = false) GlobalTransaction.ValueStatus valueStatus,
                                          @RequestParam(required = false) Long useraccountId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int limit) {

        Useraccount payer = null;
        if (useraccountId != null && useraccountId > 0) {
            payer = useraccountDao.findById(useraccountId).orElseThrow(
                    () -> new NotFoundException("User not found"));
        }

        return ResponseEntity.ok(
                globalTransactnMngr.get(search, paymentStatus, valueStatus, payer, page, limit));
    }
}
