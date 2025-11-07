package loyaltycom.api_selos.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrocaExpiradaDTO {
    private Long idProduto;
    private String codTroca;
    private Integer qtdSelos;
    private Double valorRestante;
    private LocalDateTime criadoEm;
    private String nome;
    private String img;
}
