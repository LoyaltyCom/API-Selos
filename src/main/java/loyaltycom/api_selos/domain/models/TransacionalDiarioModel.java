package loyaltycom.api_selos.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Hidden
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_transacional_diario")
public class TransacionalDiarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "dt_movimento")
    private LocalDate dtMovimento;

    @Column(name = "id_loja")
    private Long idLoja;

    @Column(name = "nome_loja")
    private String nomeLoja;

    @Column(name = "id_pdv")
    private Long idPDV;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "num_cupom")
    private Long numCupom;

    @Column(name = "canal_dvenda")
    private String canalVenda;

    @Column(name = "vlr_venda")
    private Double vlrVenda;

    @Column(name = "qtd_produto")
    private Integer qtdProduto;

    @Column(name = "id_operador")
    private Long idOperador;

    @Column(name = "qtd_selo_total")
    private Integer qtdSeloTotal;

    @Column(name = "qtd_selo_aceito")
    private Integer qtdSeloAceito;

    @Column(name = "qtd_selo_rejeitado")
    private Integer qtdSeloRejeitado;

    @Column(name = "qtd_selo_acelerador")
    private Integer qtdSeloAcelerador;
}
