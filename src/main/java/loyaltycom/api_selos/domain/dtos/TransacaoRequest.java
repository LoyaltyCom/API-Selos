package loyaltycom.api_selos.domain.dtos;

import lombok.Data;

@Data
public class TransacaoRequest {
    private Integer idCliente;
    private Integer idDestino;
    private String tipo;
    private Integer quantidade;
}
