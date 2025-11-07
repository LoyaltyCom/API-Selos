package loyaltycom.api_selos.domain.repositories;

import loyaltycom.api_selos.domain.models.TransacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TransacaoRepository extends JpaRepository<TransacaoModel, Integer> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tb_transacoes (id_cliente, tipo, valor, dt_movimento) VALUES (:idCliente, :tipo, :valor, NOW())", nativeQuery = true)
    void registrarTransacao(Integer idCliente, String tipo, Integer valor);
}
