package ng.optisoft.rosabon.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.constant.ApiRoute;
import ng.optisoft.rosabon.dto.HttpResponseDto;
import ng.optisoft.rosabon.dto.request.LoginDto;
import ng.optisoft.rosabon.dto.response.UserDetailsDto;
import ng.optisoft.rosabon.enums.PlatformType;
import ng.optisoft.rosabon.exception.BadRequestException;
import ng.optisoft.rosabon.model.GenericModuleBaseEntity;
import ng.optisoft.rosabon.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@Slf4j
public class AuthController {
    private AuthService authService;

    @PostMapping(value = ApiRoute.LOGIN)
    public ResponseEntity<?> login(@RequestBody @Valid @NotNull LoginDto loginDto, HttpServletRequest request) throws ExecutionException, InterruptedException {

        log.info("Header Version *** {}", request.getHeader("version"));

        log.info("I GOT INTO LOGIN");
        Enumeration<String> headerNames = request.getHeaderNames();
        List<String> headerNameList = new ArrayList<>();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerNameList.add(headerName);
        }

        log.info("Headers:  {}", headerNameList.toString());

        if(PlatformType.MOBILE.name().equals(loginDto.getPlatformType()) && GenericModuleBaseEntity.Platform.TREASURY.equals(loginDto.getPlatform())) {
            if(request.getHeader("version") == null){
                throw new BadRequestException("Oops... It's not your password, you are getting this error" +
                        " because you are on an older app version. " +
                        "Kindly update your app from the store to continue" +
                        " enjoying our enhanced features and improved security");
            }
        }

        UserDetailsDto dto = authService.login(loginDto);

        return ResponseEntity.accepted().body(dto);
    }

    @PostMapping(value = ApiRoute.AUTH + ApiRoute.LOGOUT)
    public HttpResponseDto logout(Authentication authentication) {


        return authService.logout(authentication);

    }

}
