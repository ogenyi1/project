package ng.optisoft.rosabon.credit.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@Aspect
@Component
@Slf4j
public class   LoggingAdvice {

//	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	@Pointcut(value="execution(* com.stanbic.service.collectionextendedcompositeservice.controllers.*.*(..) )")
//	public void pointCut() {}
	
	
	@Around("@annotation(ng.optisoft.rosabon.credit.annotations.LogMethod)")

	public Object applicationLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
//		String methodName = pjp.getSignature().getName();
//		//String className = pjp.getTarget().getClass().toString();
//		Object[] array = pjp.getArgs();
//		logger.info("Method invoked : "+methodName+"()"+" Request : {}", array);
//		Object object = pjp.proceed();
//		logger.info("Method invoked : "+methodName+"()"+" Response : {}", object);
//		return object;
		long startTime = System.nanoTime();
		String methodName = proceedingJoinPoint.getSignature().getName();
		log.info("proceedingJoinPoint kind : {}", proceedingJoinPoint.getKind());
		Object[] request = proceedingJoinPoint.getArgs();
		HttpServletRequest servletRequest = RequestContextHolder.getRequestAttributes() != null ?
				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest() : null;
		String url = servletRequest != null ? servletRequest.getRequestURL().toString() : null;
		log.info("URL : " + url);
		String httpMethod = servletRequest != null ? servletRequest.getMethod() : null;
		log.info("Http Method : " + httpMethod);
		log.info("Method invoked : " + methodName + "()");

		if (("GET").equals(httpMethod)) log.info("Request : " + Arrays.toString(request));
		else if (request.length == 1) log.info("Request : " + request[0]);
		else if (request.length == 2) log.info("Request : " + request[1]);
		else if (request.length > 2) {
			Object[] subarray = Arrays.copyOfRange(request, 3, request.length);
			log.info("Request : " + Arrays.toString(subarray));
		}
		Object response = proceedingJoinPoint.proceed();
		log.info("Method invoked : " + methodName + "()");
		log.info("Response : {}", response);
		long endTime = System.nanoTime();
		long executionTimeNano = endTime - startTime;
		double executionTimeInSeconds = executionTimeNano / 1_000_000_000.00;
		log.info("executionTimeInSeconds :{}",executionTimeInSeconds);
		return response;
	}
	
}
