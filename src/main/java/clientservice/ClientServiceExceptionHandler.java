package clientservice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ClientServiceExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ClientServiceException.class)
	public ResponseEntity<?> handleControllerException(ClientServiceException ex) {
		return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<?> handleAccessDeniedException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<?> handleControllerException(HttpServletRequest request, Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), INTERNAL_SERVER_ERROR);
	}

}
