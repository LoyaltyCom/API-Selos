package loyaltycom.api_selos.application.controllers;

import loyaltycom.api_selos.application.services.TransacaoService;
import loyaltycom.api_selos.domain.dtos.TransacaoRequest;
import loyaltycom.api_selos.domain.dtos.TransacaoResponseDTO;
import loyaltycom.api_selos.infra.customers_routing_config.ClientContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes/{tenant}")
public class TransacaoController {
    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    public ResponseEntity<?> realizarTransacao(@PathVariable("tenant") String tenant,
                                               @RequestBody TransacaoRequest request) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            return ResponseEntity.ok(transacaoService.executarAcao(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    java.util.Map.of("error", "Erro interno: " + e.getMessage())
            );
        } finally {
            ClientContextHolder.clear();
        }
    }

    @GetMapping("/internas")
    public ResponseEntity<List<TransacaoResponseDTO>> buscarTransacoesInternas(@PathVariable("tenant") String tenant,
                                                                               @RequestParam(required = false) Integer idCliente) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            return ResponseEntity.ok(transacaoService.buscarTransacoesInternasDoUsuario(idCliente));
        } finally {
            ClientContextHolder.clear();
        }
    }

    @GetMapping("/diarias")
    public ResponseEntity<List<TransacaoResponseDTO>> buscarTransacoesDiariasDoUsuarioNoCliente(@PathVariable("tenant") String tenant,
                                                                                                @RequestParam(required = false) Integer idCliente) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            return ResponseEntity.ok(transacaoService.buscarTransacoesDiariasDoUsuarioNoCliente(idCliente));
        } finally {
            ClientContextHolder.clear();
        }
    }

}
