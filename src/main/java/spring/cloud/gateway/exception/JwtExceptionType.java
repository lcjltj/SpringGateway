package spring.cloud.gateway.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionType {
	
	NullPointerException("토큰이 존재 하지 않습니다.", 100),
	JwtException("잘못된 토큰 입니다.",200),
	ExpiredJwtException("만료된 토큰 입니다.", 300);
	
	private final String message;
	private final int errorCode;
}