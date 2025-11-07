package loyaltycom.api_selos.application.controllers;

import loyaltycom.api_selos.application.services.ResgataveisService;
import loyaltycom.api_selos.domain.models.ResgataveisModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resgataveis")
public class ResgataveisController {
    private final ResgataveisService resgataveisService;

    public ResgataveisController(ResgataveisService resgataveisService) {
        this.resgataveisService = resgataveisService;
    }

    @GetMapping("/buscar-todos/{tenant}")
    public ResponseEntity<List<ResgataveisModel>> getAll(@PathVariable("tenant") String tenant) {

        return ResponseEntity.ok(resgataveisService.findAll(tenant));
    }

    @GetMapping("buscar-produto/{id}/{tenant}")
    public ResponseEntity<ResgataveisModel> getById(@PathVariable("id") Integer id,
                                                    @PathVariable("tenant") String tenant) {

        return ResponseEntity.ok(resgataveisService.findById(id, tenant));
    }

    @PostMapping("/{tenant}")
    public ResponseEntity<ResgataveisModel> create(@PathVariable("tenant") String tenant,
                                                   @RequestBody ResgataveisModel produto) {

        return ResponseEntity.ok(resgataveisService.save(produto, tenant));
    }
}

