package loyaltycom.api_selos.domain.dtos;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String userId;
    private String tenant;
    private String redirectUrl;
}
