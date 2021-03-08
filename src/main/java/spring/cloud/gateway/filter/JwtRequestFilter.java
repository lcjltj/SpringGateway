package spring.cloud.gateway.filter;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import spring.cloud.gateway.exception.DefaultException;
import spring.cloud.gateway.exception.JwtExceptionHandler;
import spring.cloud.gateway.exception.JwtExceptionType;
import spring.cloud.gateway.util.JWTUtil;

@Component
@Order(value = -1) // Global Filter 실행 순서
@RequiredArgsConstructor
public class JwtRequestFilter implements GlobalFilter {

	private final JWTUtil jwtUtil;

	@Bean
	public ErrorWebExceptionHandler myExceptionHandler() {
		return new JwtExceptionHandler();
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			ServerHttpRequest request = exchange.getRequest();
			String path = request.getPath().value();

			if (path.contains("/auth/") || path.contains("/admin/")) { // auth / admin 토큰 확인
				String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

				if (authHeader == null) {
					throw new DefaultException(JwtExceptionType.NullPointerException);
				} else if (authHeader != null && authHeader.startsWith("Bearer")) {
					String accessToken = authHeader.split(" ")[1];
					jwtUtil.validateToken(accessToken); // 만료된 token시 Exception
				} else {
					throw new DefaultException(JwtExceptionType.JwtException);
				}
			}
			
			return chain.filter(exchange);
		} catch (ExpiredJwtException e) {
			throw new DefaultException(JwtExceptionType.ExpiredJwtException);
		} catch (JwtException e) {
			throw new DefaultException(JwtExceptionType.JwtException);
		}
	}
}
