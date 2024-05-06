package ng.optisoft.rosabon.controller;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.PaymentGatewayDao;
import ng.optisoft.rosabon.enums.EntityStatus;
import ng.optisoft.rosabon.mapper.PaymentGatewayMapper;
import ng.optisoft.rosabon.model.PaymentGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(ApiRoute.AUTH)
public class PaymentGatewayContoller {

    private PaymentGatewayDao paymentGatewayDao;

    @GetMapping(value =  ApiRoute.PAYMEMNT_GATEWAYS)
    public ResponseEntity<?> getPaymentGateways(Authentication auth) {

        List<PaymentGateway> gateways = paymentGatewayDao.findAllByEntityStatus(EntityStatus.ACTIVE);
        return new ResponseEntity<>(PaymentGatewayMapper.mapToDtoList(gateways), HttpStatus.OK);
    }
}
