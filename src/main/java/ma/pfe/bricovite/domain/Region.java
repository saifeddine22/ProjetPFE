package ma.pfe.bricovite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Region.
 */
@Entity
@Table(name = "region")
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code_reg")
    private Double codeReg;

    @Column(name = "nom_fr")
    private String nomFr;

    @Column(name = "nom_ar")
    private String nomAr;

    @Column(name = "geometry")
    private String geometry;

    @OneToMany(mappedBy = "region")
    @JsonIgnoreProperties(value = { "communes", "region" }, allowSetters = true)
    private Set<Province> provinces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Region id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCodeReg() {
        return this.codeReg;
    }

    public Region codeReg(Double codeReg) {
        this.setCodeReg(codeReg);
        return this;
    }

    public void setCodeReg(Double codeReg) {
        this.codeReg = codeReg;
    }

    public String getNomFr() {
        return this.nomFr;
    }

    public Region nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
    }

    public String getNomAr() {
        return this.nomAr;
    }

    public Region nomAr(String nomAr) {
        this.setNomAr(nomAr);
        return this;
    }

    public void setNomAr(String nomAr) {
        this.nomAr = nomAr;
    }

    public String getGeometry() {
        return this.geometry;
    }

    public Region geometry(String geometry) {
        this.setGeometry(geometry);
        return this;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Set<Province> getProvinces() {
        return this.provinces;
    }

    public void setProvinces(Set<Province> provinces) {
        if (this.provinces != null) {
            this.provinces.forEach(i -> i.setRegion(null));
        }
        if (provinces != null) {
            provinces.forEach(i -> i.setRegion(this));
        }
        this.provinces = provinces;
    }

    public Region provinces(Set<Province> provinces) {
        this.setProvinces(provinces);
        return this;
    }

    public Region addProvince(Province province) {
        this.provinces.add(province);
        province.setRegion(this);
        return this;
    }

    public Region removeProvince(Province province) {
        this.provinces.remove(province);
        province.setRegion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Region)) {
            return false;
        }
        return id != null && id.equals(((Region) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Region{" +
            "id=" + getId() +
            ", codeReg=" + getCodeReg() +
            ", nomFr='" + getNomFr() + "'" +
            ", nomAr='" + getNomAr() + "'" +
            ", geometry='" + getGeometry() + "'" +
            "}";
    }
}
