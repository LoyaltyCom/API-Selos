package loyaltycom.api_selos.application.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import loyaltycom.api_selos.domain.models.ResgataveisModel;
import loyaltycom.api_selos.domain.repositories.ResgataveisRepository;
import loyaltycom.api_selos.infra.customers_routing_config.ClientContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResgataveisService {
    private final ResgataveisRepository resgataveisRepository;

    public List<ResgataveisModel> findAll(String tenant) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            return resgataveisRepository.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            ClientContextHolder.clear();
        }
    }

    public ResgataveisModel findById(Integer id, String tenant) {
        ClientContextHolder.setCurrentDatabase(tenant);
        return resgataveisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
    }

    public ResgataveisModel save(ResgataveisModel produto, String tenant) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            return resgataveisRepository.save(produto);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            ClientContextHolder.clear();
        }
    }
}

