package spring.cloud.gateway.exception;

import lombok.Getter;

@Getter
public class DefaultException extends RuntimeException {

	private static final long serialVersionUID = -1182546868146990489L;
	private final int errorCode;

	public DefaultException(JwtExceptionType jwtExceptionType) {
		super(jwtExceptionType.getMessage());
		this.errorCode = jwtExceptionType.getErrorCode();
	}
}