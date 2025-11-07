package loyaltycom.api_selos.domain.exceptions.personalized_exceptions;

public class CampaignExistException extends RuntimeException {
    public CampaignExistException(String message) {
        super(message);
    }
}
