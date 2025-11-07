package loyaltycom.api_selos.domain.dtos;

import java.time.LocalDateTime;

public record TransacaoResponseDTO(LocalDateTime dtMovimento, Integer valor, String tipo) { }
