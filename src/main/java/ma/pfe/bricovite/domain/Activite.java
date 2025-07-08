package ma.pfe.bricovite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Activite.
 */
@Entity
@Table(name = "activite")
public class Activite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_fr")
    private String nomFr;

    @Column(name = "nom_ar")
    private String nomAr;

    @Column(name = "categorie_fr")
    private String categorieFr;

    @Column(name = "categorie_ar")
    private String categorieAr;

    @OneToMany(mappedBy = "activite", fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "photos", "commentaires", "notes", "user", "commune", "activite" }, allowSetters = true)
    private Set<Annonce> annonces = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "activites" }, allowSetters = true)
    private Categorie categorie;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Activite nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomAr() {
        return this.nomAr;
    }

    public Activite nomAr(String nomAr) {
        this.setNomAr(nomAr);
        return this;
    }

    public void setNomAr(String nomAr) {
        this.nomAr = nomAr;
    }

    public String getCategorieFr() {
        return this.categorieFr;
    }

    public Activite categorieFr(String categorieFr) {
        this.setCategorieFr(categorieFr);
        return this;
    }

    public void setCategorieFr(String categorieFr) {
        this.categorieFr = categorieFr;
    }

    public String getCategorieAr() {
        return this.categorieAr;
    }

    public Activite categorieAr(String categorieAr) {
        this.setCategorieAr(categorieAr);
        return this;
    }

    public void setCategorieAr(String categorieAr) {
        this.categorieAr = categorieAr;
    }

    public Set<Annonce> getAnnonces() {
        return this.annonces;
    }

    public void setAnnonces(Set<Annonce> annonces) {
        if (this.annonces != null) {
            this.annonces.forEach(i -> i.setActivite(null));
        }
        if (annonces != null) {
            annonces.forEach(i -> i.setActivite(this));
        }
        this.annonces = annonces;
    }

    public Activite annonces(Set<Annonce> annonces) {
        this.setAnnonces(annonces);
        return this;
    }

    public Activite addAnnonce(Annonce annonce) {
        this.annonces.add(annonce);
        annonce.setActivite(this);
        return this;
    }

    public Activite removeAnnonce(Annonce annonce) {
        this.annonces.remove(annonce);
        annonce.setActivite(null);
        return this;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Activite categorie(Categorie categorie) {
        this.setCategorie(categorie);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activite)) {
            return false;
        }
        return id != null && id.equals(((Activite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activite{" +
            "id=" + getId() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomAr='" + getNomAr() + "'" +
            ", categorieFr='" + getCategorieFr() + "'" +
            ", categorieAr='" + getCategorieAr() + "'" +
            "}";
    }
}
