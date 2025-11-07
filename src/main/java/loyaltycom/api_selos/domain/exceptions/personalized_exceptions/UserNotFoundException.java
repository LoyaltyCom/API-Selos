package loyaltycom.api_selos.domain.exceptions.personalized_exceptions;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
