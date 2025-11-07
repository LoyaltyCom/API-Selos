package loyaltycom.api_selos.infra.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import loyaltycom.api_selos.infra.security.JwtSecretKeyProvider;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final JwtSecretKeyProvider keyProvider;

    public JwtUtil(JwtSecretKeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    public String extractClientName(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Token JWT não fornecido");
        }

        String token = header.substring(7);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keyProvider.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String tenantId = claims.get("clientId", String.class);

        if (tenantId == null || tenantId.isBlank()) {
            throw new RuntimeException("Claim 'tenant_id' ausente ou vazio no JWT.");
        }

        return switch (tenantId) {
            case "nagumo" -> "Nagumo";
            case "festival" -> "Festival";
            default -> "LoyaltyCom";
        };
    }
}

