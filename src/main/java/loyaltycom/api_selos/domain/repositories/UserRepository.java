package loyaltycom.api_selos.domain.repositories;

import loyaltycom.api_selos.domain.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByIdCliente(Integer idCliente);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tb_clientes SET selos = selos + :qtdSelos WHERE id_cliente = :idCliente", nativeQuery = true)
    void reembolsarSelos(Integer idCliente, Integer qtdSelos);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tb_clientes SET selos = :qtdSelos WHERE id_cliente = :idCliente", nativeQuery = true)
    void atualizarSelos(Integer idCliente, Integer qtdSelos);
}
