package ma.pfe.bricovite.service.dto;

// Classe pour la description de l'annonce
public class DescriptionDTO {

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
