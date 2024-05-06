package ng.optisoft.rosabon.config;

import lombok.extern.slf4j.Slf4j;
import ng.optisoft.rosabon.dto.GeneralResponse;
import ng.optisoft.rosabon.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

	@ExceptionHandler(value = { NotFoundException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public GeneralResponse misMatchErrorHandler(NotFoundException ex) {
		log.error("throwing NotFoundException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage());
	}

	@ExceptionHandler(value = { ForbiddenException.class })
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public GeneralResponse forbiddenErrorHandler(ForbiddenException ex) {
		log.error("throwing ForbiddenException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.FORBIDDEN.value(),
				ex.getLocalizedMessage());
	}
	
	@ExceptionHandler(value = { UnauthorizedException.class })
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public GeneralResponse unauthorizedErrorHandler(UnauthorizedException ex) {
		log.error("throwing UnauthorizedException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.UNAUTHORIZED.value(),
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public GeneralResponse handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex) {
		log.error("throwing MethodArgumentNotValidException at : {}", ex.getStackTrace()[0]);

		List<String> errorList = ex.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> fieldError.getDefaultMessage())
				.collect(Collectors.toList());

		return new GeneralResponse(HttpStatus.BAD_REQUEST.value(), errorList.get(0));

	}

	@ExceptionHandler(value = { EntityAlreadyExistsException.class })
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public GeneralResponse entityAlreadyExistsErrorHandler(
			EntityAlreadyExistsException ex) {
		log.error("throwing EntityAlreadyExistsException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.CONFLICT.value(), ex.getLocalizedMessage());
	}

	@ExceptionHandler(value = { EmailAlreadyExistsException.class })
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public GeneralResponse emailAlreadyExistsErrorHandler(EmailAlreadyExistsException ex) {
		log.error("throwing EmailAlreadyExistsException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.CONFLICT.value(), ex.getLocalizedMessage());
	}

	@ExceptionHandler(value = { IllegalArgumentException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public GeneralResponse illegalArgumentExceptionHandler(IllegalArgumentException ex) {
		log.error("throwing IllegalArgumentException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.BAD_REQUEST.value(),
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(value = { ConflictException.class })
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public GeneralResponse conflictExceptionHandler(ConflictException ex) {
		log.error("throwing ConflictException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.CONFLICT.value(), ex.getLocalizedMessage());
	}

	@ExceptionHandler(value = { BadRequestException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public GeneralResponse badRequestExceptionHandler(BadRequestException ex) {
		log.error("throwing BadRequestException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.BAD_REQUEST.value(),
				ex.getLocalizedMessage());
	}
	
	@ExceptionHandler(value = { BadCredentialException.class })
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	public GeneralResponse badCredentialExceptionHandler(BadCredentialException ex) {
		log.error("throwing BadCredentialException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.NOT_ACCEPTABLE.value(),
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(value = { InsufficientFundsException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public GeneralResponse insufficientFundsExceptionHandler(InsufficientFundsException ex) {
		log.error("throwing InsufficientFundsException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.BAD_REQUEST.value(),
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(value = { InsufficientWemaFundsException.class })
	@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
	public GeneralResponse insufficientWemaFundsExceptionHandler(InsufficientWemaFundsException ex) {
		log.error("throwing InsufficientWemaFundsException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.EXPECTATION_FAILED.value(),
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(PaymentVerificationException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public GeneralResponse paymentVerificationExceptionHandler(PaymentVerificationException ex) {
		log.error("throwing PaymentVerificationException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.CONFLICT.value(),
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(FailedPaymentException.class)
	@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
	public GeneralResponse failedPaymentExceptionHandler(FailedPaymentException ex) {
		log.error("throwing FailedPaymentException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.EXPECTATION_FAILED.value(),
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(PaymentGatewayException.class)
	@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
	public GeneralResponse paymentGatewayExceptionHandler(PaymentGatewayException ex) {
		log.error("throwing PaymentGatewayException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.EXPECTATION_FAILED.value(),
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(UnverifiedEmailException.class)
	@ResponseStatus(value = HttpStatus.PRECONDITION_REQUIRED)
	public GeneralResponse unverifiedEmailExceptionHandler(UnverifiedEmailException ex) {
		log.error("throwing UnverifiedEmailException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.PRECONDITION_REQUIRED.value(),
				ex.getLocalizedMessage());
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	@ResponseStatus(value = HttpStatus.PRECONDITION_REQUIRED)
	public GeneralResponse missingRequestHeaderExceptionHandler(MissingRequestHeaderException ex) {
		log.error("throwing MissingRequestHeaderException at : {}", ex.getStackTrace()[0]);
		return new GeneralResponse(HttpStatus.BAD_REQUEST.value(),
				ex.getLocalizedMessage());
	}

}
