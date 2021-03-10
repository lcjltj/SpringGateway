package spring.cloud.gateway.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionType {
	
	NullPointerException("토큰이 존재 하지 않습니다.", 100),
	JwtException("잘못된 토큰 입니다.",200),
	ExpiredJwtException("만료된 토큰 입니다.", 300),
	NOT_ALLOW_REQUEST("외부 요청으로 사용이 불가능합니다.",400);
	
	private final String message;
	private final int errorCode;
}