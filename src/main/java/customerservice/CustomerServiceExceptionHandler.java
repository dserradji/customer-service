package customerservice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handles exceptions that are not already handled in ResponseEntityExceptionHandler class
 * 
 */
@RestControllerAdvice
public class CustomerServiceExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomerServiceException.class)
	public ResponseEntity<?> handleCustomerServiceException(CustomerServiceException ex) {
		return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<?> handleAccessDeniedException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<?> handleException(HttpServletRequest request, Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), INTERNAL_SERVER_ERROR);
	}

}
