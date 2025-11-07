package loyaltycom.api_selos.domain.models.auxiliares;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PromocaoNvl1 {
    @Column(name = "novo_percentual_de_desconto")
    public Double novoPercentualDeDesconto;

    @Column(name = "nova_qtd_selos")
    public Integer novaQtdSelos;
}
