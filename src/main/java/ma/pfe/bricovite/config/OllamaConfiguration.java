package ma.pfe.bricovite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OllamaConfiguration {

    // Configuration supplémentaire si nécessaire

    @Bean
    public ExchangeStrategies exchangeStrategies() {
        // Augmenter la taille maximale du buffer pour les réponses plus grandes
        return ExchangeStrategies
            .builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // 16MB
            .build();
    }
}
