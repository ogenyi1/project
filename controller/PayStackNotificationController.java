package ng.optisoft.rosabon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.service.GlobalTransactnMngr;
import ng.optisoft.rosabon.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin
@AllArgsConstructor
public class PayStackNotificationController {

    private GlobalTransactnMngr globalTransactnMngr;

    private PaymentService paymentService;

    @PostMapping(value = ApiRoute.PAYSTACK + ApiRoute.GET_WEB_HOOK_NOTIFICATION)
    public ResponseEntity<?> getWebHookNotification(@RequestBody String request, @RequestHeader("x-paystack-signature") String signature) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        globalTransactnMngr.updateFundsTransferStatus(request, signature);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/payment/callback")
    public RedirectView processPayStackCallBack(@RequestParam("trxref") String reference, HttpServletResponse response){
//        var res = paymentService.verifyPaymentForPaystack(reference);

        return paymentService.verifyPaymentForPaystack(reference);
    }

    @RequestMapping(value = "/forwardedWithParams", method = RequestMethod.GET)

    public RedirectView forwardedWithParams(
            final RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addAttribute("param1", request.getAttribute("param1"));
        redirectAttributes.addAttribute("param2", request.getAttribute("param2"));

        redirectAttributes.addAttribute("attribute", "forwardedWithParams");
        return new RedirectView("redirectedUrl");
    }
}
