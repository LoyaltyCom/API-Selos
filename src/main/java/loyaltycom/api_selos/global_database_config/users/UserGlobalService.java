package loyaltycom.api_selos.global_database_config.users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserGlobalService {

    private final UserGlobalRepository userGlobalRepository;

    public UserGlobalService(UserGlobalRepository userGlobalRepository) {
        this.userGlobalRepository = userGlobalRepository;
    }

    @Transactional("centralTransactionManager")
    public void save(UserGlobalModel userGlobalModel) {
        userGlobalRepository.save(userGlobalModel);
    }

    @Transactional("centralTransactionManager")
    public boolean existsByUserIdAndTenant(Integer userId, String tenantName) {
        return userGlobalRepository.existsByIdClienteAndTenantNameIgnoreCase(userId, tenantName);
    }
}
