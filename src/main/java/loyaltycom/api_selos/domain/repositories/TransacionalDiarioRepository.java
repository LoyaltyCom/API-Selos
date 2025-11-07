package loyaltycom.api_selos.domain.repositories;

import loyaltycom.api_selos.domain.models.TransacionalDiarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacionalDiarioRepository extends JpaRepository<TransacionalDiarioModel, Long> {
}
