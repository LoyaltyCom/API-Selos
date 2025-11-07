package loyaltycom.api_selos.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loyaltycom.api_selos.infra.common.TenantValidator;
import loyaltycom.api_selos.infra.customers_routing_config.ClientContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtClientFilter extends OncePerRequestFilter {
    private final JwtSecretKeyProvider keyProvider;
    private final TenantValidator tenantValidator;

    public JwtClientFilter(JwtSecretKeyProvider keyProvider, TenantValidator tenantValidator) {
        this.keyProvider = keyProvider;
        this.tenantValidator = tenantValidator;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/token")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(keyProvider.getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String clientId = claims.get("clientId", String.class);
                if (clientId == null || clientId.isBlank()) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Claim 'clientId' ausente no token.");
                    return;
                }

                if (!tenantValidator.isValidTenant(clientId)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Requisição restrita, acesso negado");
                }

                if ((("GET".equalsIgnoreCase(request.getMethod()))
                        && isProtectedRoute(request.getRequestURI()))
                        && !"main".equals(clientId)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Requisição não autorizada");
                    return;
                }

                ClientContextHolder.setCurrentDatabase(clientId);
            } catch (JwtException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ausente ou malformado");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isProtectedRoute(String uri) {
        return uri.startsWith("/");
    }
}



