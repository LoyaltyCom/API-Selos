package loyaltycom.api_selos.infra.customers_routing_config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ClientRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return ClientContextHolder.getCurrentDatabase();
    }
}

