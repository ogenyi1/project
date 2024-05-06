package ng.optisoft.rosabon.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.dao.UseraccountDao;
import ng.optisoft.rosabon.dto.AuthCountDto;
import ng.optisoft.rosabon.dto.GeneralResponse;
import ng.optisoft.rosabon.model.Useraccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class AuthenticationEventListener {

	@Autowired private UseraccountDao useraccountDao;

//	@ExceptionHandler(UnauthorizedException.class) 
//	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
		try {
			log.info("AuthenticationEventListener : authenticationFailed");
			String username = (String) event.getAuthentication().getPrincipal();

//        event.getException().
			log.info("Username: " + username);
			Useraccount user = useraccountDao.findByEmail(username);
			if (user != null && (user.getUsage().equals(Useraccount.Usage.ADMIN) || user.getUsage().equals(Useraccount.Usage.ADMIN_TREASURY)
					|| user.getUsage().equals(Useraccount.Usage.USER_BOTH))) {
				user.setFailedAttempt(user.getFailedAttempt() + 1);
				user = useraccountDao.save(user);

				if (user.getFailedAttempt() >= 5) {
					user.setStatus(Useraccount.Status.LOCKED);
					user.setLockTime(LocalDateTime.now());
				}
//	        	  else {
//	        	  user.setFailedAttempt(user.getFailedAttempt() + 1);
//	          }
			}
//	          useraccountDao.save(user);
			log.info("username: " + username);
			AuthCountDto dto = new AuthCountDto(
					"Incorrect email and password combination. Please click on forgot password link to reset your password",
					user != null ? user.getLoginCount() : 0,
					user != null ? user.getFailedAttempt() : 0);

//	      	throw new UnauthorizedException("" + dto);

			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
					.currentRequestAttributes())
					.getResponse();
//	      	HttpServletResponse response = (HttpServletResponse) request.getAsyncContext().getResponse();
			if (response == null)
				System.out.println("response object is null");
			if (response != null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				ServletOutputStream out = response.getOutputStream();
				new ObjectMapper()
						.writeValue(
								out,
								new GeneralResponse(
										HttpServletResponse.SC_UNAUTHORIZED,
										"Incorrect email and password combination. Please click on forgot password link to reset your password",
										dto
								)
						);
				out.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
