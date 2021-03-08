package spring.cloud.gateway.util;

import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import spring.cloud.gateway.exception.DefaultException;
import spring.cloud.gateway.exception.JwtExceptionType;

@Component
public class JWTUtil {

	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	private Boolean isTokenExpired(String token) {
		boolean invalid = false;
		final Date expiration = getExpirationDateFromToken(token);
		invalid = expiration.before(new Date());

		if (invalid) {
			throw new DefaultException(JwtExceptionType.ExpiredJwtException);
		}
		return true;
	}

	public Boolean validateToken(String token) {
		return isTokenExpired(token);
	}

}