package ma.pfe.bricovite.service.dto;

// Classe pour la demande de suggestions de mots-clés
public class KeywordSuggestionRequestDTO {

    private String description;
    private String serviceType;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
