package loyaltycom.api_selos.domain.repositories;

import loyaltycom.api_selos.domain.dtos.TrocaPendenteDTO;
import loyaltycom.api_selos.domain.models.TrocaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrocaRepository extends JpaRepository<TrocaModel, Integer> {
    Optional<TrocaModel> findByCodTroca(String codTroca);

    @Query("SELECT COUNT(t) FROM TrocaModel t WHERE t.idCliente = :idCliente AND t.status = :status")
    Integer countByIdClienteAndStatus(@Param("idCliente") Integer idCliente, @Param("status") String status);

    @Query(value = """
            SELECT e.id_produto, e.cod_troca, e.qtd_selos, e.valor_restante, e.criado_em,
                   p.nome, p.img
            FROM tb_troca e
            JOIN tb_resgataveis p ON e.id_produto = p.id_produto
            WHERE e.id_cliente = :idCliente AND e.status = 'PENDENTE'
            ORDER BY e.criado_em DESC
            """, nativeQuery = true)
    Optional<List<TrocaPendenteDTO>> findPendentesComProduto(@Param("idCliente") Integer idCliente);

    @Query(value = "SELECT " +
            "t.id_produto, " +
            "t.cod_troca, " +
            "t.qtd_selos, " +
            "t.valor_restante, " +
            "t.criado_em, " +
            "r.nome, " +
            "r.img " +
            "FROM tb_troca t " +
            "JOIN tb_resgataveis r ON t.id_produto = r.id_produto " +
            "WHERE t.id_cliente = :idCliente " +
            "AND t.status = 'EXPIRADO' " +
            "ORDER BY t.criado_em DESC", nativeQuery = true)
    List<Object[]> buscarExpiradasRaw(@Param("idCliente") Integer idCliente);

    @Query(value = """
                SELECT 
                    r.cod_troca,
                    r.dt_movimento::text,
                    p.nome,
                    p.img,
                    r.qtd_selos,
                    r.vlr_liquido AS valor_restante
                FROM tb_resgates r
                JOIN tb_resgataveis p ON r.id_produto = p.id_produto
                WHERE r.id_cliente = :idCliente
                ORDER BY r.dt_movimento DESC
            """, nativeQuery = true)
    List<Object[]> buscarResgatesCliente(@Param("idCliente") Integer idCliente);

    boolean existsByCodTroca(String code);
}
