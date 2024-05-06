package ng.optisoft.rosabon.controller;

import lombok.AllArgsConstructor;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dao.RoleDao;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.GeneralResponse;
import ng.optisoft.rosabon.dto.ResetPasswordDto;
import ng.optisoft.rosabon.dto.request.*;
import ng.optisoft.rosabon.dto.response.UseraccountDto;
import ng.optisoft.rosabon.enums.RoleConstant;
import ng.optisoft.rosabon.exception.NotFoundException;
import ng.optisoft.rosabon.model.Role;
import ng.optisoft.rosabon.model.Useraccount;
import ng.optisoft.rosabon.model.Useraccount.Usage;
import ng.optisoft.rosabon.service.UseraccountMngr;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class UserController {

    private UseraccountMngr useraccountMngr;
    private UseraccountDao useraccountDao;
    private RoleDao roleDao;


    @PostMapping(value = ApiRoute.USERS)
    public ResponseEntity<?> registerUser(@RequestBody @NotNull @Valid RegisterUserInDto dto) {
        UseraccountDto acct = useraccountMngr.registerUser(dto);

        return new ResponseEntity<>(acct, HttpStatus.CREATED);
    }

    @PutMapping(value = ApiRoute.USERS + ApiRoute.UPDATE_BVN)
    public ResponseEntity<?> updateBvn(@RequestBody UpdateBvnInDto updateBvnInDto) {
        useraccountMngr.updateBvn(updateBvnInDto);
        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(),
                "Bvn has been updated"));
    }

    @PostMapping(value = ApiRoute.USERS + "/{email}" + ApiRoute.RESEND_EMAIL_VERIFICATION_CODE)
    public ResponseEntity<?> resendEmailVerificationCode(@PathVariable String email) {
        useraccountMngr.resendEmailVerificationCode(email);

        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(),
                "Email verification code has been sent to " + email));
    }

    @PostMapping(value = ApiRoute.USERS + ApiRoute.VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid VerifyEmailInDto dto) {

        useraccountMngr.verifyEmail(dto);

        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(),
                "Email successfully verified"));
    }

    @PostMapping(value = ApiRoute.USERS + "/{email}" + ApiRoute.FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {

        useraccountMngr.forgotPassword(email);

        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(),
                "Password change request has been processed. Please check your email for further details"));
    }

    @PostMapping(value = ApiRoute.USERS + ApiRoute.RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto dto) {

        useraccountMngr.resetPassword(dto);

        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(),
                "Password reset Successful"));
    }
    
    @PostMapping(value = ApiRoute.USERS + ApiRoute.RESET_PASSWORD2)
    public ResponseEntity<?> resetPassword2(@RequestBody ResetPasswordDto dto) {

        useraccountMngr.resetPassword(dto);

        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(),
                "Password reset Successful"));
    }

    @PostMapping(value = ApiRoute.USERS + ApiRoute.UNLOCK_USER)
    @PreAuthorize("hasAnyAuthority('RESET_ADMIN_USER', 'SUPER_ADMINISTRATOR')")
    public ResponseEntity<?> unlockUser(@RequestParam String email, @RequestParam Usage usage) {

        useraccountMngr.unlockUser(email, usage);

        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(),
                "Message have been sent to the given email for further instructions on reset password"));
    }

    @PostMapping(value = ApiRoute.AUTH + ApiRoute.USERS + ApiRoute.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(Authentication auth,
                                            @RequestBody @Valid ChangePasswordInDto dto) {
        return ResponseEntity.ok(useraccountMngr.changePassword(useraccountDao.findByEmail(auth.getName()), dto));
    }

    @GetMapping(value = ApiRoute.AUTH + ApiRoute.USERS + ApiRoute.GET_KYC_DETAILS)
    public ResponseEntity<?> getUserKYC(Authentication auth) {
        Useraccount acct = useraccountDao.findByEmail(auth.getName());
        if (acct == null)
            throw new NotFoundException("User account details not found");
        return ResponseEntity.ok(useraccountMngr.getOneUser(acct));
    }

    @PutMapping(value = ApiRoute.AUTH + ApiRoute.USERS)
    public ResponseEntity<?> userKYCUpdate(Authentication auth,
                                           @RequestBody @NotNull @Valid UpdateUserInDto dto) {
        Useraccount acct = useraccountDao.findByEmail(auth.getName());
        if (acct == null)
            throw new NotFoundException("User account details not found");

        acct = useraccountMngr.updateUser(acct, dto); //rre

        return new ResponseEntity<>(useraccountMngr.getOneUser(acct), HttpStatus.OK);
    }

    @PutMapping(value = ApiRoute.AUTH + ApiRoute.USERS + ApiRoute.UPDATE_FIRST_LAST_NAME)
    public ResponseEntity<?> userKYCUpdateFirstAndLastName(Authentication auth, @RequestBody @NotNull @Valid UpdateUserFirstLastNameInDto dto) {
        Useraccount acct = useraccountDao.findByEmail(auth.getName());
        if (acct == null)
            throw new NotFoundException("User account details not found");
        acct = useraccountMngr.updateUserFirstLastName(acct, dto);

        return new ResponseEntity<>(useraccountMngr.getOneUser(acct), HttpStatus.OK);
    }

    @GetMapping(value = ApiRoute.AUTH + ApiRoute.USERS)
    public ResponseEntity<?> getUserDetails(Authentication auth) {
        Useraccount acct = useraccountDao.findByEmail(auth.getName());
        if (acct == null)
            throw new NotFoundException("User account details not found");

        return ResponseEntity.ok(useraccountMngr.getOneUser(acct));
    }

    @GetMapping(value = ApiRoute.AUTH + "/{email}" + ApiRoute.USERS)
    public ResponseEntity<?> getUserDetailsByEmail(Authentication auth, @PathVariable String email) {

        Useraccount acct = useraccountDao.findByEmail(email);
        if (acct == null)
            throw new NotFoundException("Email not found");

        return ResponseEntity.ok(useraccountMngr.getOneUser(acct));
    }

    @GetMapping(value = ApiRoute.AUTH + ApiRoute.USERS + ApiRoute.STATUS_LIST)
    public ResponseEntity<?> getListOfStatusesForUser(Authentication auth) {

        List<Useraccount.Status> statusLIst = Arrays.asList(Useraccount.Status.values());

        return ResponseEntity.ok(statusLIst);
    }

    @PostMapping(value = ApiRoute.ADMIN + ApiRoute.ADMIN_USERS)
    public ResponseEntity<?> registerAdmin(Authentication auth,
                                           @RequestBody @Valid RegisterAdminInDto dto) {

        Useraccount acct = useraccountMngr.registerAdmin(dto, useraccountDao.findByEmail(auth.getName()));

        return new ResponseEntity<>(new GeneralResponse(HttpStatus.CREATED.value(),
                "Account created"), HttpStatus.CREATED);
    }


    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.NON_ADMIN_USERS)
    public ResponseEntity<?> getAllNonAdminUsers(Authentication auth,
                                                 @RequestParam(required = false) String search,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int limit) {

        List<Role> roles = new ArrayList<>();
        roles.add(roleDao.findByName(RoleConstant.INDIVIDUAL_USER).orElse(null));
        roles.add(roleDao.findByName(RoleConstant.COMPANY).orElse(null));

        return ResponseEntity.ok(useraccountMngr.getAllUsers(search, roles, page, limit));
    }


    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.ADMIN_USERS)
    public ResponseEntity<?> getAllAdminUsers(Authentication auth,
                                              @RequestParam(required = false) String search,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int limit) {

        List<Role> roles = new ArrayList<>();
        roles.add(roleDao.findByName(RoleConstant.ADMINISTRATOR).orElse(null));
        roles.add(roleDao.findByName(RoleConstant.SUPER_ADMINISTRATOR).orElse(null));

        return ResponseEntity.ok(useraccountMngr.getAllUsers(search, roles, page, limit));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.USERS + ApiRoute.BY_NAME)
    public ResponseEntity<?> searchForUserByName(Authentication auth,
                                                 @RequestParam String name,
                                                 @RequestParam RoleConstant roleName,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int limit) {

        return ResponseEntity.ok(useraccountMngr.searchForUserByName(name, roleName, page, limit));
    }

    @PutMapping(value = ApiRoute.ADMIN + ApiRoute.USERS + ApiRoute.UPDATE_STATUS)
    public ResponseEntity<?> updateUserStatus(Authentication auth,
                                              @RequestBody @Valid UpdateUserStatusInDto dto) {


        useraccountMngr.updateUserStatus(dto, useraccountDao.findByEmail(auth.getName()));
        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(),
                "Status updated"));
    }


    @GetMapping(value = ApiRoute.USERS + ApiRoute.VERIFY_EMAIL + "/{email}" + "/{code}")
    public RedirectView verifyEmail(@PathVariable String email, @PathVariable String code) {


        Useraccount acct = useraccountDao.findByEmail(email);
        if (acct == null)
            throw new NotFoundException("Email not found");

        return useraccountMngr.verifyEmail(email, code);

//        return ResponseEntity.ok(new GeneralResponse(HttpStatus.OK.value(),
//                "Email successfully verified"));
//        if (GenericModuleBaseEntity.Platform.CREDIT.name().equals(acct.getPlatform().name())) return "Congratulations. Your email has been verified successfully "
//                + "<a href=\"https://rosabon-credit.optisoft.com.ng\">click this link to proceed to login</a>";
//        else return "Congratulations. Your email has been verified successfully. Proceed to login.";
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.USERS + ApiRoute.GET_BY_ADMIN_REF_CODE)
    public ResponseEntity<?> getAllUsersUnderAdmin(Authentication auth,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(useraccountMngr.getAllUsersUnderAdmin(auth, page, limit));
    }

    @GetMapping(value = ApiRoute.ADMIN + ApiRoute.USERS + ApiRoute.FIND_BY_PARAM)
    public ResponseEntity<?> findByParam (Authentication auth,@RequestParam(value = "param") String param,
                                          @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir) {
        return useraccountMngr.findAllByParam(auth,param,pageNo,pageSize,sortBy,sortDir);
    }
    //get all pending user requests
    //get all pending user requests for specific admin

    @PostMapping(value = ApiRoute.AUTH + ApiRoute.USERS + ApiRoute.SEND_OTP)
    public ResponseEntity<?> sendOtpToUser(Authentication auth, @RequestBody @Valid SendOtpInDto dto) {
        Useraccount useraccount = useraccountDao.findByEmail(auth.getName());

        useraccountMngr.sendOtpToUser(useraccount,dto);

        return ResponseEntity.ok("An OTP has been sent to your registered email address, the OTP will expire after 10 mins");
    }
}
