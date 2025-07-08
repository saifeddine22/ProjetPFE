package ma.pfe.bricovite.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ma.pfe.bricovite.client.OllamaClient;
import ma.pfe.bricovite.repository.AnnonceRepository;
import ma.pfe.bricovite.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final Logger log = LoggerFactory.getLogger(AIService.class);
    private final OllamaClient ollamaClient;
    private final AnnonceRepository annonceRepository;

    public AIService(OllamaClient ollamaClient, AnnonceRepository annonceRepository) {
        this.ollamaClient = ollamaClient;
        this.annonceRepository = annonceRepository;
    }

    public EnhancedDescriptionDTO enhanceDescription(DescriptionDTO descriptionDTO) {
        String prompt =
            "Tu es un rédacteur professionnel d'annonces. Améliore cette description d'annonce pour un service de " +
            descriptionDTO.getServiceType() +
            " au Maroc. Rends-la plus attractive, professionnelle et détaillée, tout en restant concis " +
            "et en gardant le même sens. Utilise le français standard. Limite ta réponse à 250 mots maximum.\n\n" +
            "Description originale: " +
            descriptionDTO.getDescription();

        String enhancedDescription = ollamaClient.generateText(prompt);

        return new EnhancedDescriptionDTO(enhancedDescription);
    }

    public SuggestedTitleDTO suggestTitle(DescriptionDTO descriptionDTO) {
        String prompt =
            "Crée un titre accrocheur en français pour une annonce de service de " +
            descriptionDTO.getServiceType() +
            " basé sur cette description: " +
            descriptionDTO.getDescription() +
            ".\n\n" +
            "Le titre doit être court (maximum 10 mots), attrayant et contenir des mots-clés pertinents. " +
            "Donne uniquement le titre, sans explications ni guillemets.";

        String title = ollamaClient.generateText(prompt).trim();

        // Supprimer les guillemets s'ils sont présents
        if (title.startsWith("\"") && title.endsWith("\"")) {
            title = title.substring(1, title.length() - 1);
        }

        SuggestedTitleDTO dto = new SuggestedTitleDTO();
        dto.setTitle(title);
        return dto;
    }

    public PriceEstimationDTO estimatePrice(ServiceDetailsDTO serviceDetailsDTO) {
        String prompt =
            "Tu es un expert en services et travaux au Maroc. Estime une fourchette de prix raisonnable en Dirhams marocains (MAD) " +
            "pour ce service:\n\n" +
            "Type de service: " +
            serviceDetailsDTO.getServiceType() +
            "\n" +
            "Localisation: " +
            serviceDetailsDTO.getLocation() +
            "\n" +
            "Description: " +
            serviceDetailsDTO.getDescription() +
            "\n\n" +
            "Réponds UNIQUEMENT avec deux nombres séparés par un tiret, représentant le prix minimum et maximum. " +
            "Par exemple: 300-500";

        String priceRange = ollamaClient.generateText(prompt);

        // Utiliser une expression régulière pour extraire la fourchette de prix
        Pattern pattern = Pattern.compile("(\\d+)\\s*-\\s*(\\d+)");
        Matcher matcher = pattern.matcher(priceRange);

        double minPrice = 100; // Valeurs par défaut
        double maxPrice = 500;

        if (matcher.find()) {
            try {
                minPrice = Double.parseDouble(matcher.group(1));
                maxPrice = Double.parseDouble(matcher.group(2));
            } catch (NumberFormatException e) {
                log.error("Erreur lors de la conversion des prix", e);
            }
        }

        PriceEstimationDTO dto = new PriceEstimationDTO();
        dto.setMinPrice(minPrice);
        dto.setMaxPrice(maxPrice);
        dto.setCurrency("MAD");
        return dto;
    }

    // Méthode pour suggérer des mots-clés pertinents
    public List<String> suggestKeywords(DescriptionDTO descriptionDTO) {
        String prompt =
            "Suggère 5 mots-clés pertinents en français pour une annonce de service de " +
            descriptionDTO.getServiceType() +
            " basée sur cette description: " +
            descriptionDTO.getDescription() +
            ".\n\n" +
            "Réponds uniquement avec les mots-clés séparés par des virgules, sans phrases ni explications.";

        String keywordsResponse = ollamaClient.generateText(prompt);

        // Diviser la réponse en mots-clés individuels
        String[] keywords = keywordsResponse.split(",");
        List<String> keywordList = new ArrayList<>();

        for (String keyword : keywords) {
            String trimmed = keyword.trim();
            if (!trimmed.isEmpty()) {
                keywordList.add(trimmed);
            }
        }

        // Si aucun mot-clé n'a été trouvé ou si la réponse est mal formatée
        if (keywordList.isEmpty()) {
            log.warn("Aucun mot-clé n'a été extrait de la réponse: {}", keywordsResponse);
            // Ajouter quelques mots-clés par défaut basés sur le type de service
            keywordList.addAll(Arrays.asList(descriptionDTO.getServiceType(), "professionnel", "service", "Maroc", "qualité"));
        }

        return keywordList;
    }
}
