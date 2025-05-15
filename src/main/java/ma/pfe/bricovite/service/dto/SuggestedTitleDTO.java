package ma.pfe.bricovite.service.dto;

public class SuggestedTitleDTO {

    private String title;

    // Constructeurs
    public SuggestedTitleDTO() {}

    public SuggestedTitleDTO(String title) {
        this.title = title;
    }

    // Getters et setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
