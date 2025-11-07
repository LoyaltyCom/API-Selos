package loyaltycom.api_selos.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrocaColecaoDTO {
    private String codTroca;
    private String dtMovimento;
    private String nome;
    private String img;
    private Integer qtdSelos;
    private Double valorRestante;
}

