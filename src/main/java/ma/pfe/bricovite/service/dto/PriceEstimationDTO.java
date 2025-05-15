package ma.pfe.bricovite.service.dto;

public class PriceEstimationDTO {

    private Double minPrice;
    private Double maxPrice;
    private String currency;

    // Constructeurs
    public PriceEstimationDTO() {}

    public PriceEstimationDTO(Double minPrice, Double maxPrice, String currency) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.currency = currency;
    }

    // Getters et setters
    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
