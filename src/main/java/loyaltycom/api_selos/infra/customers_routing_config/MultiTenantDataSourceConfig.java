package loyaltycom.api_selos.infra.customers_routing_config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiTenantDataSourceConfig {
    @Value("${pg.host}")
    private String PG_HOST;

    @Value("${pg.pass}")
    private String PG_PASS;

    @Value("${pg.user}")
    private String PG_USER;

    @Bean
    public DataSource dataSourceMain() {
        return DataSourceBuilder.create()
                .url(PG_HOST + "db_loyaltycom")
                .username(PG_USER)
                .password(PG_PASS)
                .build();
    }

    @Bean
    public DataSource dataSourceBigMais() {
        return DataSourceBuilder.create()
                .url(PG_HOST + "db_big_mais")
                .username(PG_USER)
                .password(PG_PASS)
                .build();
    }

    @Bean
    public DataSource dataSourceAlvorada() {
        return DataSourceBuilder.create()
                .url(PG_HOST + "db_alvorada")
                .username(PG_USER)
                .password(PG_PASS)
                .build();
    }

    @Bean
    public DataSource dataSourceRetroativo() {
        return DataSourceBuilder.create()
                .url(PG_HOST + "db_loyaltycom_retroativo")
                .username(PG_USER)
                .password(PG_PASS)
                .build();
    }

    @Bean
    public DataSource dataSourcePatioGourmet() {
        return DataSourceBuilder.create()
                .url(PG_HOST + "db_patio_gourmet")
                .username(PG_USER)
                .password(PG_PASS)
                .build();
    }

    @Bean
    public DataSource dataSourceZonaSul() {
        return DataSourceBuilder.create()
                .url(PG_HOST + "db_zona_sul")
                .username(PG_USER)
                .password(PG_PASS)
                .build();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource main = dataSourceMain();
        DataSource alvorada = dataSourceAlvorada();
        DataSource bigMais = dataSourceBigMais();
        DataSource patioGourmet = dataSourcePatioGourmet();
        DataSource zonaSul = dataSourceZonaSul();
        DataSource retroativo = dataSourceRetroativo();

        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("main", main);
        dataSources.put("alvorada", alvorada);
        dataSources.put("big-mais", bigMais);
        dataSources.put("patio-gourmet", patioGourmet);
        dataSources.put("zona-sul", zonaSul);
        dataSources.put("main2", retroativo);

        ClientRoutingDataSource routingDataSource = new ClientRoutingDataSource();
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(main);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }
}

