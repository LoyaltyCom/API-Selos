package loyaltycom.api_selos.domain.models.auxiliares;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PromocaoNvl2 {
    @Column(name = "novo_percentual_de_desconto_2")
    public Double novoPercentualDeDesconto2;

    @Column(name = "nova_qtd_selos_2")
    public Integer novaQtdSelos2;
}
