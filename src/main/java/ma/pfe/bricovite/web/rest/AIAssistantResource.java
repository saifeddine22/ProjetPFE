package ma.pfe.bricovite.web.rest;

import java.util.List;
import java.util.Map;
import ma.pfe.bricovite.service.AIService;
import ma.pfe.bricovite.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-assistance")
public class AIAssistantResource {

    private final Logger log = LoggerFactory.getLogger(AIAssistantResource.class);
    private final AIService aiService;

    public AIAssistantResource(AIService aiService) {
        this.aiService = aiService;
    }

    /**
     * {@code POST  /api/ai-assistance/generate-title} : Generate a title based on description.
     *
     * @param request the description and other parameters
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the generated title
     */
    @PostMapping("/generate-title")
    public ResponseEntity<SuggestedTitleDTO> generateTitle(@RequestBody Map<String, String> request) {
        log.debug("REST request to generate title from description: {}", request.get("description"));

        String description = request.get("description");
        String serviceType = request.getOrDefault("serviceType", "");

        DescriptionDTO descriptionDTO = new DescriptionDTO();
        descriptionDTO.setDescription(description);
        descriptionDTO.setServiceType(serviceType);

        SuggestedTitleDTO result = aiService.suggestTitle(descriptionDTO);

        return ResponseEntity.ok(result);
    }

    /**
     * {@code POST  /api/ai-assistance/enhance-description} : Enhance a description.
     *
     * @param request the description to enhance
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the enhanced description
     */
    @PostMapping("/enhance-description")
    public ResponseEntity<EnhancedDescriptionDTO> enhanceDescription(@RequestBody Map<String, String> request) {
        log.debug("REST request to enhance description: {}", request.get("description"));

        String description = request.get("description");
        String serviceType = request.getOrDefault("serviceType", "");

        DescriptionDTO descriptionDTO = new DescriptionDTO();
        descriptionDTO.setDescription(description);
        descriptionDTO.setServiceType(serviceType);

        EnhancedDescriptionDTO result = aiService.enhanceDescription(descriptionDTO);

        return ResponseEntity.ok(result);
    }

    /**
     * {@code POST  /api/ai-assistance/generate-keywords} : Generate keywords.
     *
     * @param request the description and service type
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of keywords
     */
    @PostMapping("/generate-keywords")
    public ResponseEntity<List<String>> generateKeywords(@RequestBody KeywordSuggestionRequestDTO request) {
        log.debug("REST request to generate keywords from description: {}", request.getDescription());

        DescriptionDTO descriptionDTO = new DescriptionDTO();
        descriptionDTO.setDescription(request.getDescription());
        descriptionDTO.setServiceType(request.getServiceType());

        List<String> keywords = aiService.suggestKeywords(descriptionDTO);

        return ResponseEntity.ok(keywords);
    }

    /**
     * {@code POST  /api/ai-assistance/estimate-price} : Estimate a price.
     *
     * @param serviceDetails the service details including description, category and activity
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the price estimation
     */
    @PostMapping("/estimate-price")
    public ResponseEntity<PriceEstimationDTO> estimatePrice(@RequestBody ServiceDetailsDTO serviceDetails) {
        log.debug("REST request to estimate price for description: {}", serviceDetails.getDescription());

        PriceEstimationDTO priceEstimation = aiService.estimatePrice(serviceDetails);

        return ResponseEntity.ok(priceEstimation);
    }
}
