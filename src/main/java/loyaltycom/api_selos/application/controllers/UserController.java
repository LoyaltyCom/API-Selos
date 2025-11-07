package loyaltycom.api_selos.application.controllers;

import jakarta.servlet.http.HttpServletRequest;
import loyaltycom.api_selos.application.services.UserService;
import loyaltycom.api_selos.domain.dtos.AtualizacaoSelosRequestDTO;
import loyaltycom.api_selos.domain.dtos.LoginResponseDTO;
import loyaltycom.api_selos.domain.dtos.TransacaoRequest;
import loyaltycom.api_selos.domain.dtos.UserRequestDTO;
import loyaltycom.api_selos.domain.exceptions.personalized_exceptions.UserAlreadyExistsException;
import loyaltycom.api_selos.infra.common.JwtUtil;
import loyaltycom.api_selos.infra.customers_routing_config.ClientContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${external.api.token}")
    private String externalToken;
    private final HttpServletRequest request;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(HttpServletRequest request, UserService userService, JwtUtil jwtUtil) {
        this.request = request;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login-register")
    public ResponseEntity<?> loginRegisterAndUpdateUser(@RequestBody UserRequestDTO userRequest) {
        try {
            LoginResponseDTO response = userService.loginOrRegisterAndUpdateUser(userRequest);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor.");
        }
    }

    @GetMapping("/{tenant}/{id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "tenant") String tenantName,
                                     @PathVariable(name = "id") Integer idCliente) {
        try {
            return ResponseEntity.ok(userService.getUser(idCliente, tenantName));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{tenant}")
    public ResponseEntity<?> atualizarQtdSelos(@PathVariable(name = "tenant") String tenant,
                                               @RequestBody AtualizacaoSelosRequestDTO requestDTO) {
        try {
            userService.atualizarSelosDoCliente(tenant, requestDTO);

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/transacao/{tenant}")
    public ResponseEntity<?> realizarTransacao(@PathVariable("tenant") String tenant,
                                               @RequestBody TransacaoRequest request) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            return ResponseEntity.ok(userService.executarAcao(request));
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
}

