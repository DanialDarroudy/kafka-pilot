package at.uibk.dps.coordinator.core.di;

import at.uibk.dps.coordinator.module.configuration.broker.config.BrokerConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class CoreDependencyInstaller {
    private final BrokerConfig config;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public AdminClient kafkaAdminClient(){
        var properties = new Properties();
        properties.put("bootstrap.servers", config.getBootstrapServers());
        return AdminClient.create(properties);
    }
}
