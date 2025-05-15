package ma.pfe.bricovite.service.dto;

public class ServiceDetailsDTO {

    private String serviceType;
    private String location;
    private String description;

    // Constructeurs
    public ServiceDetailsDTO() {}

    public ServiceDetailsDTO(String serviceType, String location, String description) {
        this.serviceType = serviceType;
        this.location = location;
        this.description = description;
    }

    // Getters et setters
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
