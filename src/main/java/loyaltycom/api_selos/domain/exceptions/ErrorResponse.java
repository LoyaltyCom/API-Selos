package loyaltycom.api_selos.domain.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private Object details;

    public ErrorResponse(String message, Object details) {
        this.message = message;
        this.details = details;
    }
}

