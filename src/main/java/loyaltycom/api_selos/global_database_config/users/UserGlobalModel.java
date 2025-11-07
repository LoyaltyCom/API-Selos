package loyaltycom.api_selos.global_database_config.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_clientes_global")
public class UserGlobalModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 90, name = "id_cliente")
    private Integer idCliente;

    @Column(nullable = false, length = 90)
    private String nome;

    @Column(nullable = false, length = 100)
    private String email;

    @ColumnDefault("0")
    private Integer selos = 0;

    @Column(nullable = false, length = 90, name = "tenant_name")
    private String tenantName;

    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
