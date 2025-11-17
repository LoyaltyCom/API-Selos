package loyaltycom.api_selos.application.controllers;

import loyaltycom.api_selos.application.services.TrocaService;
import loyaltycom.api_selos.domain.dtos.TrocaColecaoDTO;
import loyaltycom.api_selos.domain.dtos.TrocaExpiradaDTO;
import loyaltycom.api_selos.domain.dtos.TrocaPendenteDTO;
import loyaltycom.api_selos.domain.models.TrocaModel;
import loyaltycom.api_selos.infra.customers_routing_config.ClientContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/troca")
public class TrocaController {
    private TrocaService trocaService;

    public TrocaController(TrocaService trocaService) {
        this.trocaService = trocaService;
    }

    @PostMapping("/{tenant}")
    public ResponseEntity<TrocaModel> create(@PathVariable("tenant") String tenant,
                                             @RequestBody TrocaModel troca) {

        return ResponseEntity.ok(trocaService.save(tenant, troca));
    }

    @PatchMapping("/expirar-e-reembolsar/{codTroca}/{tenant}")
    public ResponseEntity<TrocaModel> expirarEReembolsar(@PathVariable("codTroca") String codTroca,
                                                         @PathVariable("tenant") String tenant) {

        ClientContextHolder.setCurrentDatabase(tenant);
        return ResponseEntity.ok(trocaService.expirarEReembolsar(codTroca, tenant));
    }

    @GetMapping("/soma-pendente/{idCliente}/{tenant}")
    public ResponseEntity<Integer> resgatesPendentes(@PathVariable("idCliente") Integer idCliente,
                                                     @PathVariable("tenant") String tenant) {

        Integer trocasPendentes = trocaService.buscarTotalDeResgatesPendentes(idCliente, tenant);
        if (trocasPendentes > 0) {
            return ResponseEntity.ok(trocasPendentes);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/troca-pendente-detalhada/{idCliente}/{tenant}")
    public ResponseEntity<List<TrocaPendenteDTO>> getPendentes(@PathVariable("idCliente") Integer idCliente,
                                                               @PathVariable("tenant") String tenant) {

        List<TrocaPendenteDTO> pendentes = trocaService.buscarPendentes(idCliente, tenant);

        if (pendentes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pendentes);
    }

    @GetMapping("/expiradas/{idCliente}/{tenant}")
    public ResponseEntity<Page<TrocaExpiradaDTO>> buscarExpiradas(@PathVariable Integer idCliente,
                                                                  @PathVariable String tenant,
                                                                  Pageable pageable) {

        Page<TrocaExpiradaDTO> expiradas = trocaService.buscarTrocasExpiradas(idCliente, tenant, pageable);

        if (expiradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(expiradas);
    }


    @GetMapping("/colecao/{idCliente}/{tenant}")
    public ResponseEntity<List<TrocaColecaoDTO>> buscarColecao(@PathVariable("idCliente") Integer idCliente,
                                                               @PathVariable("tenant") String tenant) {

        return ResponseEntity.ok(trocaService.buscarColecaoPorCliente(idCliente, tenant));
    }

    @GetMapping("/gerar-codigo/{tenant}")
    public ResponseEntity<?> gerarCodigoUnico(@PathVariable("tenant") String tenant) {

        return ResponseEntity.ok(trocaService.gerarCodigoUnico(tenant));
    }
}
