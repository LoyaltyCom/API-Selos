package loyaltycom.api_selos.application.services;

import loyaltycom.api_selos.domain.dtos.TrocaColecaoDTO;
import loyaltycom.api_selos.domain.dtos.TrocaExpiradaDTO;
import loyaltycom.api_selos.domain.dtos.TrocaPendenteDTO;
import loyaltycom.api_selos.domain.models.TrocaModel;
import loyaltycom.api_selos.domain.repositories.TransacaoRepository;
import loyaltycom.api_selos.domain.repositories.TrocaRepository;
import loyaltycom.api_selos.domain.repositories.UserRepository;
import loyaltycom.api_selos.infra.common.FieldsValidator;
import loyaltycom.api_selos.infra.customers_routing_config.ClientContextHolder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrocaService {
    private final TrocaRepository trocaRepository;
    private final FieldsValidator fieldsValidator;
    private final UserRepository userRepository;
    private final TransacaoRepository transacaoRepository;

    public TrocaService(TrocaRepository trocaRepository, FieldsValidator fieldsValidator, UserRepository userRepository,
                        TransacaoRepository transacaoRepository) {
        this.trocaRepository = trocaRepository;
        this.fieldsValidator = fieldsValidator;
        this.userRepository = userRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public TrocaModel save(String tenant, TrocaModel troca) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            fieldsValidator.validateFields(troca);
            return trocaRepository.save(troca);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            ClientContextHolder.clear();
        }
    }

    public Integer buscarTotalDeResgatesPendentes(Integer idCliente, String tenant) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            Integer trocasPendentesOp = trocaRepository.countByIdClienteAndStatus(idCliente, "PENDENTE");

            if (trocasPendentesOp > 0) {
                return trocasPendentesOp;
            } else
                return 0;
        } finally {
            ClientContextHolder.clear();
        }
    }

    @Transactional
    public TrocaModel expirarEReembolsar(String codTroca, String tenant) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            TrocaModel troca = trocaRepository.findByCodTroca(codTroca)
                    .orElseThrow(() -> new RuntimeException("Resgate não encontrado"));

            if (!"PENDENTE".equalsIgnoreCase(troca.getStatus())) {
                throw new RuntimeException("Resgate já expirado ou processado");
            }

            troca.setStatus("EXPIRADO");
            trocaRepository.save(troca);

            userRepository.reembolsarSelos(troca.getIdCliente(), troca.getQtdSelos());

            transacaoRepository.registrarTransacao(troca.getIdCliente(), "Estorno", troca.getQtdSelos());

            return troca;
        } finally {
            ClientContextHolder.clear();
        }
    }

    public List<TrocaPendenteDTO> buscarPendentes(Integer idCliente, String tenant) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            Optional<List<TrocaPendenteDTO>> trocasPendentes = trocaRepository.findPendentesComProduto(idCliente);

            return trocasPendentes.orElse(List.of());
        } finally {
            ClientContextHolder.clear();
        }
    }

    public List<TrocaExpiradaDTO> buscarTrocasExpiradas(Integer idCliente, String tenant) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);
            List<Object[]> results = trocaRepository.buscarExpiradasRaw(idCliente);

            return results.stream().map(row -> {
                TrocaExpiradaDTO dto = new TrocaExpiradaDTO();

                dto.setIdProduto(row[0] != null ? ((Number) row[0]).longValue() : null);
                dto.setCodTroca((String) row[1]);
                dto.setQtdSelos(row[2] != null ? ((Number) row[2]).intValue() : null);
                dto.setValorRestante(row[3] != null ? ((Number) row[3]).doubleValue() : null);

                if (row[4] != null) {
                    Object criadoEmObj = row[4];
                    if (criadoEmObj instanceof java.sql.Timestamp) {
                        dto.setCriadoEm(((java.sql.Timestamp) criadoEmObj).toLocalDateTime());
                    } else if (criadoEmObj instanceof java.time.Instant) {
                        dto.setCriadoEm(LocalDateTime.ofInstant(
                                (java.time.Instant) criadoEmObj,
                                ZoneId.systemDefault()
                        ));
                    } else if (criadoEmObj instanceof LocalDateTime) {
                        dto.setCriadoEm((LocalDateTime) criadoEmObj);
                    }
                }
                dto.setNome((String) row[5]);
                dto.setImg((String) row[6]);

                return dto;
            }).collect(Collectors.toList());

        } finally {
            ClientContextHolder.clear();
        }
    }

    public List<TrocaColecaoDTO> buscarColecaoPorCliente(Integer idCliente, String tenant) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);
            List<Object[]> results = trocaRepository.buscarResgatesCliente(idCliente);

            return results.stream().map(row -> {
                TrocaColecaoDTO dto = new TrocaColecaoDTO();
                dto.setCodTroca((String) row[0]);
                dto.setDtMovimento((String) row[1]);
                dto.setNome((String) row[2]);
                dto.setImg((String) row[3]);
                dto.setQtdSelos(row[4] != null ? ((Number) row[4]).intValue() : null);
                dto.setValorRestante(row[5] != null ? ((Number) row[5]).doubleValue() : null);
                return dto;
            }).toList();

        } finally {
            ClientContextHolder.clear();
        }
    }

    public String gerarCodigoUnico(String tenant){
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            String code;
            do {
                code = RandomStringUtils.random(6, "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789");
            } while (trocaRepository.existsByCodTroca(code));

            return code;
        } finally {
            ClientContextHolder.clear();
        }
    }
}
