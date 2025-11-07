package loyaltycom.api_selos.domain.exceptions.personalized_exceptions;

public class ProductsAlreadyExistsException extends RuntimeException {
    public ProductsAlreadyExistsException(String message) {
        super(message);
    }
}
