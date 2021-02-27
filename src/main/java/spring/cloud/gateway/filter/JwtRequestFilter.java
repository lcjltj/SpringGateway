package spring.cloud.gateway.filter;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Order(value = -1) // Global Filter 실행 순서
@Slf4j
public class JwtRequestFilter implements GlobalFilter {

	@Bean
	public ErrorWebExceptionHandler myExceptionHandler() {
		return new JwtExceptionHandler();
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			
			throw new ExpiredJwtException(null, null, null);
			// String token =
			// exchange.getRequest().getHeaders().get("Authorization").get(0).substring(7);
		} catch (NullPointerException e) {
			log.warn("no token.");
			exchange.getResponse().setStatusCode(HttpStatus.valueOf(401));
			log.info("status code :" + exchange.getResponse().getStatusCode());
		} catch (ExpiredJwtException e) {
			return Mono.error(e);
		}
		return chain.filter(exchange);
	}
}
