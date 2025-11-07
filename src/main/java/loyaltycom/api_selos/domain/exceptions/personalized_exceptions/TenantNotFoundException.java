package loyaltycom.api_selos.domain.exceptions.personalized_exceptions;

public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(String message) {
        super(message);
    }
}
