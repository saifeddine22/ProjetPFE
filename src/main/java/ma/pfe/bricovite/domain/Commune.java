package ma.pfe.bricovite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Commune.
 */
@Entity
@Table(name = "commune")
public class Commune implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "nom_ar")
    private String nomAr;

    @Column(name = "geometry")
    private String geometry;

    @Column(name = "attachement")
    private String attachement;

    @OneToMany(mappedBy = "commune")
    @JsonIgnoreProperties(value = { "photos", "commentaires", "notes", "user", "commune", "activite" }, allowSetters = true)
    private Set<Annonce> annonces = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "communes", "region" }, allowSetters = true)
    private Province province;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commune id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Commune nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomAr() {
        return this.nomAr;
    }

    public Commune nomAr(String nomAr) {
        this.setNomAr(nomAr);
        return this;
    }

    public void setNomAr(String nomAr) {
        this.nomAr = nomAr;
    }

    public String getGeometry() {
        return this.geometry;
    }

    public Commune geometry(String geometry) {
        this.setGeometry(geometry);
        return this;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getAttachement() {
        return this.attachement;
    }

    public Commune attachement(String attachement) {
        this.setAttachement(attachement);
        return this;
    }

    public void setAttachement(String attachement) {
        this.attachement = attachement;
    }

    public Set<Annonce> getAnnonces() {
        return this.annonces;
    }

    public void setAnnonces(Set<Annonce> annonces) {
        if (this.annonces != null) {
            this.annonces.forEach(i -> i.setCommune(null));
        }
        if (annonces != null) {
            annonces.forEach(i -> i.setCommune(this));
        }
        this.annonces = annonces;
    }

    public Commune annonces(Set<Annonce> annonces) {
        this.setAnnonces(annonces);
        return this;
    }

    public Commune addAnnonce(Annonce annonce) {
        this.annonces.add(annonce);
        annonce.setCommune(this);
        return this;
    }

    public Commune removeAnnonce(Annonce annonce) {
        this.annonces.remove(annonce);
        annonce.setCommune(null);
        return this;
    }

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Commune province(Province province) {
        this.setProvince(province);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commune)) {
            return false;
        }
        return id != null && id.equals(((Commune) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commune{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", nomAr='" + getNomAr() + "'" +
            ", geometry='" + getGeometry() + "'" +
            ", attachement='" + getAttachement() + "'" +
            "}";
    }
}
