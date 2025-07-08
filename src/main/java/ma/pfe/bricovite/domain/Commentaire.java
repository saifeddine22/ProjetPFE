package ma.pfe.bricovite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Commentaire.
 */
@Entity
@Table(name = "commentaire")
public class Commentaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "details")
    private String details;

    @Column(name = "date_commentaire")
    private Instant dateCommentaire;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "photos", "commentaires", "notes", "user", "commune", "activite" }, allowSetters = true)
    private Annonce annonce;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commentaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return this.details;
    }

    public Commentaire details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getDateCommentaire() {
        return this.dateCommentaire;
    }

    public Commentaire dateCommentaire(Instant dateCommentaire) {
        this.setDateCommentaire(dateCommentaire);
        return this;
    }

    public void setDateCommentaire(Instant dateCommentaire) {
        this.dateCommentaire = dateCommentaire;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Commentaire user(User user) {
        this.setUser(user);
        return this;
    }

    public Annonce getAnnonce() {
        return this.annonce;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public Commentaire annonce(Annonce annonce) {
        this.setAnnonce(annonce);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commentaire)) {
            return false;
        }
        return id != null && id.equals(((Commentaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commentaire{" +
            "id=" + getId() +
            ", details='" + getDetails() + "'" +
            ", dateCommentaire='" + getDateCommentaire() + "'" +
            "}";
    }
}
