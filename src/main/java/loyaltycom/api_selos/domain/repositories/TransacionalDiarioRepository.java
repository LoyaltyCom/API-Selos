package loyaltycom.api_selos.domain.repositories;

import loyaltycom.api_selos.domain.dtos.TransacaoResponseDTO;
import loyaltycom.api_selos.domain.models.TransacionalDiarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacionalDiarioRepository extends JpaRepository<TransacionalDiarioModel, Long> {

    List<TransacionalDiarioModel> findAllByIdCliente(Integer idCliente);
}
