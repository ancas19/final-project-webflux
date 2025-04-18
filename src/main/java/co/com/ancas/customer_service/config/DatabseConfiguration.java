package co.com.ancas.customer_service.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(basePackages = "co.com.ancas.customer_servvice.repositories")
public class DatabseConfiguration {

    @Value("${postgres.port}")
    private Integer port;
    @Value("${postgres.host}")
    private String host;
    @Value("${postgres.user}")
    private String username;
    @Value("${postgres.pass}")
    private String password;
    @Value("${postgres.database}")
    private String database;
    @Value("${postgres.pool.initialSize:10}")
    private Integer initialSize;
    @Value("${postgres.pool.maxSize:100}")
    private Integer maxSize;

    @Bean
    public ConnectionFactory defaultConnectionFactory() {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, host)
                .option(PORT, port)
                .option(DATABASE, database)
                .option(USER, username)
                .option(PASSWORD, password)
                .build();

        return ConnectionFactories.get(options);
    }

    @Bean
    @Primary
    public ConnectionFactory pooledConnectionFactory() {
        ConnectionPoolConfiguration poolConfig = ConnectionPoolConfiguration.builder(defaultConnectionFactory())
                .initialSize(initialSize)
                .maxSize(maxSize)
                .maxIdleTime(Duration.ofMinutes(30))
                .build();

        return new ConnectionPool(poolConfig);
    }

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }

    @Bean
    @Primary
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}
