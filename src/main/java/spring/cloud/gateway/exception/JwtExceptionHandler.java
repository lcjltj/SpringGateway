package spring.cloud.gateway.exception;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtExceptionHandler implements ErrorWebExceptionHandler {
	
	@Autowired
	Environment environment;
	
	private String makeResponse(int errorCode, String message) {
		return new StringBuilder()
				.append("{\n")
				.append("    \"errorCode\" : ")
				.append(errorCode)
				.append("\n    \"message\" : ")
				.append(message)
				.append("\n}").toString();
	}

	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		int errorCode = 0;
		String message = null;
		
		if (ex.getClass() == DefaultException.class) {
			errorCode = ((DefaultException) ex).getErrorCode();
			message = ((DefaultException) ex).getMessage();
		}
		
		byte[] bytes = makeResponse(errorCode,message).getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
		
		return exchange.getResponse().writeWith(Mono.just(buffer));
	}
}
