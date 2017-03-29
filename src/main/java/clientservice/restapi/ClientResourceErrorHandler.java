package clientservice.restapi;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClientResourceErrorHandler {

	@ExceptionHandler(ClientResourceException.class)
	ResponseEntity<?> handleControllerException(ClientResourceException ex) {
		return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<?> handleControllerException(MethodArgumentNotValidException ex) {
		return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	ResponseEntity<?> handleControllerException(HttpServletRequest request, Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), INTERNAL_SERVER_ERROR);
	}
	
}
