package ma.pfe.bricovite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Province.
 */
@Entity
@Table(name = "province")
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code_reg")
    private Double codeReg;

    @Column(name = "code_prov")
    private Double codeProv;

    @Column(name = "nom_fr")
    private String nomFr;

    @Column(name = "nom_ar")
    private String nomAr;

    @Column(name = "region_fr")
    private String regionFr;

    @Column(name = "region_ar")
    private String regionAr;

    @Column(name = "geometry")
    private String geometry;

    @OneToMany(mappedBy = "province")
    @JsonIgnoreProperties(value = { "annonces", "province" }, allowSetters = true)
    private Set<Commune> communes = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "provinces" }, allowSetters = true)
    private Region region;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Province id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCodeReg() {
        return this.codeReg;
    }

    public Province codeReg(Double codeReg) {
        this.setCodeReg(codeReg);
        return this;
    }

    public void setCodeReg(Double codeReg) {
        this.codeReg = codeReg;
    }

    public Double getCodeProv() {
        return this.codeProv;
    }

    public Province codeProv(Double codeProv) {
        this.setCodeProv(codeProv);
        return this;
    }

    public void setCodeProv(Double codeProv) {
        this.codeProv = codeProv;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Province nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomAr() {
        return this.nomAr;
    }

    public Province nomAr(String nomAr) {
        this.setNomAr(nomAr);
        return this;
    }

    public void setNomAr(String nomAr) {
        this.nomAr = nomAr;
    }

    public String getRegionFr() {
        return this.regionFr;
    }

    public Province regionFr(String regionFr) {
        this.setRegionFr(regionFr);
        return this;
    }

    public void setRegionFr(String regionFr) {
        this.regionFr = regionFr;
    }

    public String getRegionAr() {
        return this.regionAr;
    }

    public Province regionAr(String regionAr) {
        this.setRegionAr(regionAr);
        return this;
    }

    public void setRegionAr(String regionAr) {
        this.regionAr = regionAr;
    }

    public String getGeometry() {
        return this.geometry;
    }

    public Province geometry(String geometry) {
        this.setGeometry(geometry);
        return this;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Set<Commune> getCommunes() {
        return this.communes;
    }

    public void setCommunes(Set<Commune> communes) {
        if (this.communes != null) {
            this.communes.forEach(i -> i.setProvince(null));
        }
        if (communes != null) {
            communes.forEach(i -> i.setProvince(this));
        }
        this.communes = communes;
    }

    public Province communes(Set<Commune> communes) {
        this.setCommunes(communes);
        return this;
    }

    public Province addCommune(Commune commune) {
        this.communes.add(commune);
        commune.setProvince(this);
        return this;
    }

    public Province removeCommune(Commune commune) {
        this.communes.remove(commune);
        commune.setProvince(null);
        return this;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Province region(Region region) {
        this.setRegion(region);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Province)) {
            return false;
        }
        return id != null && id.equals(((Province) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Province{" +
            "id=" + getId() +
            ", codeReg=" + getCodeReg() +
            ", codeProv=" + getCodeProv() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomAr='" + getNomAr() + "'" +
            ", regionFr='" + getRegionFr() + "'" +
            ", regionAr='" + getRegionAr() + "'" +
            ", geometry='" + getGeometry() + "'" +
            "}";
    }
}
