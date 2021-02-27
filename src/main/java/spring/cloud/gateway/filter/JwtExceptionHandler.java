package spring.cloud.gateway.filter;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.ExpiredJwtException;
import reactor.core.publisher.Mono;

public class JwtExceptionHandler implements ErrorWebExceptionHandler {
	private String errorCodeMaker(int errorCode, String errorMessage) {
		return "{\"errorCode\" : " + errorCode + ",\n" + 
				"\"errorMesages\" : " + errorMessage + " }";
	}

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		// TODO Auto-generated method stub

		if (ex.getClass() == ExpiredJwtException.class) {
			System.out.println("Hello");
		}
		
		byte[] bytes = errorCodeMaker(500,ex.getClass().toString()).getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
		return exchange.getResponse().writeWith(Mono.just(buffer));
	}

}
