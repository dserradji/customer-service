package clientservice.restapi;

import org.springframework.http.HttpStatus;

public class ClientResourceException extends RuntimeException {

	private static final long serialVersionUID = 8631466448974323851L;

	private final HttpStatus httpStatus;
	private final String message;

	public ClientResourceException(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	
	public String getMessage() {
		return message;
	}
}
