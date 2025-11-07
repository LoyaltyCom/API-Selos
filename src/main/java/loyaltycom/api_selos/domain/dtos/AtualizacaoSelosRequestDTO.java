package loyaltycom.api_selos.domain.dtos;

import lombok.Data;

@Data
public class AtualizacaoSelosRequestDTO {
    private Integer idCliente;
    private Integer qtdSelos;
}
