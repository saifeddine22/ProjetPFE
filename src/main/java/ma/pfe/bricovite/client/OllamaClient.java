package ma.pfe.bricovite.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OllamaClient {

    private final Logger log = LoggerFactory.getLogger(OllamaClient.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public OllamaClient(
        @Value("${ollama.api.url:http://localhost:11434}") String ollamaApiUrl,
        @Value("${ollama.api.model:gemma:2b}") String model
    ) {
        this.webClient = WebClient.builder().baseUrl(ollamaApiUrl).build();
        this.objectMapper = new ObjectMapper();
        this.model = model;
    }

    public String generateText(String prompt) {
        try {
            // Créer le corps de la requête pour Ollama
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", model);
            requestBody.put("prompt", prompt);
            // Paramètres facultatifs pour contrôler la génération
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 500);
            requestBody.put("stream", false); // Ne pas diffuser la réponse

            // Faire la requête HTTP à l'API Ollama
            String response = webClient
                .post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            // Analyser la réponse
            JsonNode jsonResponse = objectMapper.readTree(response);
            return jsonResponse.path("response").asText();
        } catch (Exception e) {
            log.error("Erreur lors de la génération de texte avec Ollama", e);
            return "Impossible de générer une suggestion. Veuillez réessayer plus tard.";
        }
    }
}
