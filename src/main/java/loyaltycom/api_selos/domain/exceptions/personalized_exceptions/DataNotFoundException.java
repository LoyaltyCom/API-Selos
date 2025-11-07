package loyaltycom.api_selos.domain.exceptions.personalized_exceptions;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
