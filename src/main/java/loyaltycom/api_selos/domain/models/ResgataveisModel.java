package loyaltycom.api_selos.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import loyaltycom.api_selos.domain.models.auxiliares.PromocaoNvl1;
import loyaltycom.api_selos.domain.models.auxiliares.PromocaoNvl2;

import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_resgataveis")
public class ResgataveisModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_produto")
    private Integer idProduto;

    @Column(name = "id_produto_cliente")
    private Integer idProdutoCliente;

    private String nome;

    private String img;

    private Double preco;

    @Column(name = "desconto_nvl_1")
    private Double descontoNvl1;

    @Column(name = "qtd_selos_nvl_1")
    private Integer qtdSelosNvl1;

    @Column(name = "desconto_nvl_2")
    private Double descontoNvl2;

    @Column(name = "qtd_selos_nvl_2")
    private Integer qtdSelosNvl2;

    @Column(name = "id_campanha")
    private Integer idCampanha;

    @Embedded
    private PromocaoNvl1 promocaoNvl1;

    @Embedded
    private PromocaoNvl2 promocaoNvl2;
}
