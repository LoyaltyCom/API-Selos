package loyaltycom.api_selos.application.services;

import jakarta.transaction.Transactional;
import loyaltycom.api_selos.domain.dtos.TransacaoRequest;
import loyaltycom.api_selos.domain.dtos.TransacaoResponseDTO;
import loyaltycom.api_selos.domain.models.TransacaoModel;
import loyaltycom.api_selos.domain.models.TransacionalDiarioModel;
import loyaltycom.api_selos.domain.models.UserModel;
import loyaltycom.api_selos.domain.repositories.TransacaoRepository;
import loyaltycom.api_selos.domain.repositories.TransacionalDiarioRepository;
import loyaltycom.api_selos.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TransacaoService {
    private final UserRepository userRepository;
    private final TransacaoRepository transacaoRepository;
    private final TransacionalDiarioRepository transacionalDiarioRepository;

    public TransacaoService(UserRepository userRepository, TransacaoRepository transacaoRepository,
                            TransacionalDiarioRepository transacionalDiarioRepository) {
        this.userRepository = userRepository;
        this.transacaoRepository = transacaoRepository;
        this.transacionalDiarioRepository = transacionalDiarioRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public Map<String, Object> executarAcao(TransacaoRequest request) {
        String tipo = request.getTipo();
        Integer idCliente = request.getIdCliente();
        Integer quantidade = request.getQuantidade();
        Integer idDestino = request.getIdDestino();

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }

        UserModel remetente = userRepository.findByIdCliente(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Usuário remetente não encontrado."));

        if ("transfer".equalsIgnoreCase(tipo)) {
            if (idDestino == null || idDestino.equals(idCliente)) {
                throw new IllegalArgumentException("ID de destino inválido.");
            }

            UserModel destinatario = userRepository.findByIdCliente(idDestino)
                    .orElseThrow(() -> new IllegalArgumentException("Destinatário não encontrado."));

            if (remetente.getSelos() < quantidade) {
                throw new IllegalArgumentException("Saldo insuficiente para transferência.");
            }

            remetente.setSelos(remetente.getSelos() - quantidade);
            destinatario.setSelos(destinatario.getSelos() + quantidade);

            userRepository.save(remetente);
            userRepository.save(destinatario);

            transacaoRepository.save(new TransacaoModel(
                    null, quantidade, LocalDateTime.now(), "Transferência", remetente.getIdCliente(),
                    LocalDateTime.now(), LocalDateTime.now()
            ));

            transacaoRepository.save(new TransacaoModel(
                    null, quantidade, LocalDateTime.now(), "Recebimento", destinatario.getIdCliente(),
                    LocalDateTime.now(), LocalDateTime.now()
            ));

            return Map.of(
                    "message", "Transferência realizada com sucesso!",
                    "user", remetente
            );
        } else if ("deposit".equalsIgnoreCase(tipo)) {
            remetente.setSelos(remetente.getSelos() + quantidade);
            userRepository.save(remetente);

            transacaoRepository.save(new TransacaoModel(
                    null, quantidade, LocalDateTime.now(), "Depósito", remetente.getIdCliente(),
                    LocalDateTime.now(), LocalDateTime.now()
            ));

            return Map.of(
                    "message", "Depósito realizado com sucesso!",
                    "user", remetente
            );
        } else if ("resgate".equalsIgnoreCase(tipo)) {
            if (remetente.getSelos() < quantidade) {
                throw new IllegalArgumentException("Saldo insuficiente para resgate.");
            }

            remetente.setSelos(remetente.getSelos() - quantidade);
            userRepository.save(remetente);

            transacaoRepository.save(new TransacaoModel(
                    null, quantidade, LocalDateTime.now(), "Resgate", remetente.getIdCliente(),
                    LocalDateTime.now(), LocalDateTime.now()
            ));

            return Map.of(
                    "message", "Resgate realizado com sucesso!",
                    "user", remetente
            );
        }

        throw new IllegalArgumentException("Tipo de ação inválido.");
    }

    public List<TransacaoResponseDTO> buscarTransacoesInternasDoUsuario(Integer idCliente) {
        return transacaoRepository.findAllByIdCliente(idCliente);
    }

    public List<TransacaoResponseDTO> buscarTransacoesDiariasDoUsuarioNoCliente(Integer idCliente) {
        List<TransacionalDiarioModel> transacoesEncontradas = transacionalDiarioRepository.findAllByIdCliente(idCliente);

        List<TransacaoResponseDTO> transacoes = new ArrayList<>();

        for (TransacionalDiarioModel model : transacoesEncontradas) {
            String canalVenda = "Compra Fisíca";

            if (model.getCanalVenda().equalsIgnoreCase("ecommerce")){
                canalVenda = "Compra Online";
            }

            TransacaoResponseDTO dto = new TransacaoResponseDTO(model.getDtMovimento(), model.getQtdSeloTotal(), canalVenda);

            transacoes.add(dto);
        }

        return transacoes;
    }
}
