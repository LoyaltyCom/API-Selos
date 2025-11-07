package loyaltycom.api_selos.global_database_config.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGlobalRepository extends JpaRepository<UserGlobalModel, Long> {
    Optional<UserGlobalModel> findByIdCliente(Integer idCliente);

    boolean existsByIdClienteAndTenantNameIgnoreCase(Integer userId, String tenantName);

   Optional<UserGlobalModel> findByIdClienteAndTenantName(Integer userId, String tenantName);
}
