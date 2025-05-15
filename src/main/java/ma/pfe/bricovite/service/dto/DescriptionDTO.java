package ma.pfe.bricovite.service.dto;

public class DescriptionDTO {

    private String description;
    private String serviceType;

    // Constructeurs
    public DescriptionDTO() {}

    public DescriptionDTO(String description, String serviceType) {
        this.description = description;
        this.serviceType = serviceType;
    }

    // Getters et setters
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
