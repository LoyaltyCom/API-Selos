package loyaltycom.api_selos.application.services;

import jakarta.servlet.http.HttpServletRequest;
import loyaltycom.api_selos.domain.dtos.AtualizacaoSelosRequestDTO;
import loyaltycom.api_selos.domain.dtos.LoginResponseDTO;
import loyaltycom.api_selos.domain.dtos.UserRequestDTO;
import loyaltycom.api_selos.domain.models.UserModel;
import loyaltycom.api_selos.domain.repositories.TransacaoRepository;
import loyaltycom.api_selos.domain.repositories.UserRepository;
import loyaltycom.api_selos.global_database_config.users.UserGlobalModel;
import loyaltycom.api_selos.global_database_config.users.UserGlobalService;
import loyaltycom.api_selos.infra.common.FieldsValidator;
import loyaltycom.api_selos.infra.common.JwtUtil;
import loyaltycom.api_selos.infra.customers_routing_config.ClientContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    @Value("${external.api.token}")
    private String externalToken;

    @Value("${external.redirect.url}")
    private String redirectBaseUrl;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserGlobalService userGlobalService;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;
    private final FieldsValidator fieldsValidator;
    private final TransacaoRepository transacaoRepository;

    public UserService(UserRepository userRepository, UserGlobalService userGlobalService, JwtUtil jwtUtil,
                       HttpServletRequest request, FieldsValidator fieldsValidator,
                       TransacaoRepository transacaoRepository) {
        this.userRepository = userRepository;
        this.userGlobalService = userGlobalService;
        this.jwtUtil = jwtUtil;
        this.request = request;
        this.fieldsValidator = fieldsValidator;
        this.transacaoRepository = transacaoRepository;
    }

    public UserModel getUser(Integer userId, String tenantName) {
        try {
            ClientContextHolder.setCurrentDatabase(tenantName);

            logger.info("Tentou buscar o usuário no {} ", tenantName);

            return userRepository.findByIdCliente(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        } finally {
            ClientContextHolder.clear();
        }
    }

    public LoginResponseDTO login(String idCliente, String email) {
        Optional<UserModel> user = userRepository.findByIdCliente(Integer.valueOf(idCliente));

        if (user.isEmpty() || !user.get().getEmail().equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("Usuário inválido");
        }

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(idCliente);
        response.setTenant(jwtUtil.extractClientName(request));
        response.setRedirectUrl(redirectBaseUrl + "?idCliente=" + idCliente);

        return response;
    }

    public LoginResponseDTO loginOrRegisterAndUpdateUser(UserRequestDTO userRequest) {
        logger.info("Recebido no login-register: {} ", ClientContextHolder.getCurrentDatabase());
        fieldsValidator.validateFields(userRequest);

        Optional<UserModel> clientFounded = userRepository.findByIdCliente(Integer.valueOf(userRequest.idCliente()));

        if (clientFounded.isPresent()) {
            if (!clientFounded.get().getEmail().equalsIgnoreCase(userRequest.email())) {
                clientFounded.get().setAtualizadoEm(LocalDateTime.now());
                clientFounded.get().setEmail(userRequest.email());
                userRepository.save(clientFounded.get());
            }
            logger.info("Login id {} | {} ", userRequest.idCliente(), ClientContextHolder.getCurrentDatabase());
            return login(userRequest.idCliente(), userRequest.email());
        } else {
            String tenantName = jwtUtil.extractClientName(request);

            UserModel user = new UserModel();
            user.setIdCliente(Integer.valueOf(userRequest.idCliente()));
            user.setNome(userRequest.nome());
            user.setEmail(userRequest.email());
            user.setCriadoEm(LocalDateTime.now());
            user.setSelos(0);

            UserGlobalModel userGlobalModel = new UserGlobalModel();
            userGlobalModel.setIdCliente(Integer.valueOf(userRequest.idCliente()));
            userGlobalModel.setTenantName(tenantName);
            userGlobalModel.setNome(userRequest.nome());
            userGlobalModel.setEmail(userRequest.email());
            userGlobalModel.setCriadoEm(LocalDateTime.now());
            userGlobalModel.setSelos(0);

            logger.info("Novo cliente id {} | {} ", userRequest.idCliente(), ClientContextHolder.getCurrentDatabase());
            userRepository.save(user);
            userGlobalService.save(userGlobalModel);

            return login(userRequest.idCliente(), userRequest.email());
        }
    }

    public void atualizarSelosDoCliente(String tenant, AtualizacaoSelosRequestDTO dto) {
        try {
            ClientContextHolder.setCurrentDatabase(tenant);

            userRepository.atualizarSelos(dto.getIdCliente(), dto.getQtdSelos());
        } finally {
            ClientContextHolder.clear();
        }
    }
}


