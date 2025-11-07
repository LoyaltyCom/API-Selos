package loyaltycom.api_selos.infra.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtGenerator {

    @Value("${api.security.token.secret}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] decodedKey = java.util.Base64.getDecoder().decode(secret);
        secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String gerarToken(String clientId) {
        return Jwts.builder()
                .claim("clientId", clientId)
                .setSubject("cliente-" + clientId)
                .setIssuedAt(new Date())
                //.setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24)) //24hrs de expiração
                //.setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365)) //1 ano de expiração
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 150)) // 5 meses
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}

