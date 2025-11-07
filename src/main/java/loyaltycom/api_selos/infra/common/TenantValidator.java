package loyaltycom.api_selos.infra.common;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TenantValidator {

    private static final Set<String> VALID_TENANTS = Set.of("main", "main2", "alvorada", "big-mais", "patio-gourmet", "zona-sul");

    public boolean isValidTenant(String tenant) {
        return VALID_TENANTS.contains(tenant);
    }
}

