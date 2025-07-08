package ma.pfe.bricovite.service.dto;

// Classe pour l'estimation de prix par l'IA
public class PriceEstimationDTO {

    private double minPrice;
    private double maxPrice;
    private String currency;

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
