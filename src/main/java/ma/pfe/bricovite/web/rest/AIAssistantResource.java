package ma.pfe.bricovite.web.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ma.pfe.bricovite.service.AIService;
import ma.pfe.bricovite.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-assistance") // Correction: ai-assistant → ai-assistance
public class AIAssistantResource {

    private final Logger log = LoggerFactory.getLogger(AIAssistantResource.class); // Correction: AIAssistanceResource → AIAssistantResource

    /**
     * {@code POST  /api/ai-assistance/generate-title} : Generate a title based on description.
     *
     * @param request the description and other parameters
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the generated title
     */
    @PostMapping("/generate-title")
    public ResponseEntity<Map<String, String>> generateTitle(@RequestBody Map<String, String> request) {
        log.debug("REST request to generate title from description: {}", request.get("description"));

        // Simple title generation logic (replace with actual AI processing)
        String description = request.get("description");
        String title = generateTitleFromDescription(description);

        Map<String, String> response = new HashMap<>();
        response.put("title", title);

        return ResponseEntity.ok(response);
    }

    /**
     * {@code POST  /api/ai-assistance/enhance-description} : Enhance a description.
     *
     * @param request the description to enhance
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the enhanced description
     */
    @PostMapping("/enhance-description")
    public ResponseEntity<Map<String, String>> enhanceDescription(@RequestBody Map<String, String> request) {
        log.debug("REST request to enhance description: {}", request.get("description"));

        String description = request.get("description");
        String enhanced = enhanceDescriptionText(description);

        Map<String, String> response = new HashMap<>();
        response.put("enhancedDescription", enhanced);

        return ResponseEntity.ok(response);
    }

    /**
     * {@code POST  /api/ai-assistance/generate-keywords} : Generate keywords.
     *
     * @param request the description and service type
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of keywords
     */
    @PostMapping("/generate-keywords")
    public ResponseEntity<List<String>> generateKeywords(@RequestBody Map<String, Object> request) {
        log.debug("REST request to generate keywords from description: {}", request.get("description"));

        String description = (String) request.get("description");
        String serviceType = request.get("serviceType") != null ? (String) request.get("serviceType") : null;

        List<String> keywords = generateKeywordsFromDescription(description, serviceType);

        return ResponseEntity.ok(keywords);
    }

    /**
     * {@code POST  /api/ai-assistance/estimate-price} : Estimate a price.
     *
     * @param request the service details including description, category and activity
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the price estimation
     */
    @PostMapping("/estimate-price")
    public ResponseEntity<Map<String, Object>> estimatePrice(@RequestBody Map<String, Object> request) {
        log.debug("REST request to estimate price for description: {}", request.get("description"));

        String description = (String) request.get("description");
        Integer categorieId = request.get("categorieId") != null ? (Integer) request.get("categorieId") : null;
        Integer activiteId = request.get("activiteId") != null ? (Integer) request.get("activiteId") : null;

        Map<String, Object> priceEstimation = estimatePriceForService(description, categorieId, activiteId);

        return ResponseEntity.ok(priceEstimation);
    }

    // ======= Helper methods for AI simulation ========

    private String generateTitleFromDescription(String description) {
        // Simple logic to generate a title (replace with actual AI)
        if (description == null || description.isEmpty()) {
            return "Titre de l'annonce";
        }

        String[] words = description.split("\\s+");
        StringBuilder title = new StringBuilder();

        // Take first 5 words or less if description is short
        int wordCount = Math.min(words.length, 5);
        for (int i = 0; i < wordCount; i++) {
            title.append(words[i]).append(" ");
        }

        return title.toString().trim() + "...";
    }

    private String enhanceDescriptionText(String description) {
        // Simple logic to enhance a description (replace with actual AI)
        if (description == null || description.isEmpty()) {
            return "Description améliorée de l'annonce";
        }

        // Add some sentences to "enhance" the description
        return (
            description +
            "\n\nCe service est fourni par un professionnel qualifié. " +
            "Contactez-moi pour plus d'informations ou pour discuter de votre projet. " +
            "Satisfaction garantie et tarifs compétitifs."
        );
    }

    private List<String> generateKeywordsFromDescription(String description, String serviceType) {
        // Generate keywords based on description and service type
        if (description == null || description.isEmpty()) {
            return Arrays.asList("service", "qualité", "professionnel");
        }

        // Generic keywords list - customize for your domain
        List<String> keywords = Arrays.asList(
            "professionnel",
            "qualité",
            "service",
            "expérience",
            "expertise",
            "rapide",
            "efficace",
            "fiable",
            "abordable"
        );

        // In a real implementation, you would analyze the description to extract relevant keywords
        return keywords;
    }

    private Map<String, Object> estimatePriceForService(String description, Integer categorieId, Integer activiteId) {
        // Estimate a price for the service (replace with actual AI)
        Map<String, Object> priceInfo = new HashMap<>();

        // Default prices
        double minPrice = 50.0;
        double maxPrice = 150.0;

        // In a real implementation, you would analyze description and categories
        // to determine a more accurate price

        priceInfo.put("minPrice", minPrice);
        priceInfo.put("maxPrice", maxPrice);
        priceInfo.put("currency", "€");

        return priceInfo;
    }
}
