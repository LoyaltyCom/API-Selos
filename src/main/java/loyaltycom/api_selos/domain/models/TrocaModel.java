package loyaltycom.api_selos.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_troca")
public class TrocaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String canal;

    @Column(name = "cod_troca")
    private String codTroca;

    @Column(name = "id_produto")
    private Integer idProduto;

    @Column(name = "id_cliente")
    private Integer idCliente;

    private String status;

    @Column(name = "qtd_selos")
    private Integer qtdSelos;

    @Column(name = "valor_restante")
    private Double valorRestante;

    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
}
