package clientservice.restapi;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.valueOf;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ClientResourceErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ClientResourceException.class)
	ResponseEntity<?> handleControllerException(ClientResourceException ex) {
		return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<?> handleControllerException(HttpServletRequest request, Exception ex) {
		
		final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		final HttpStatus status = (statusCode == null ? INTERNAL_SERVER_ERROR : valueOf(statusCode));
		
		return new ResponseEntity<>(ex.getCause().getMessage(), status);
	}
}
