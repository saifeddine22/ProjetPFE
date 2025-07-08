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

    @Column(name = "code_reg")
    private Double codeReg;

    @Column(name = "code_prov")
    private Double codeProv;

    @Column(name = "province_fr")
    private String provinceFr;

    @Column(name = "province_ar")
    private String provinceAr;

    @Column(name = "region_fr")
    private String regionFr;

    @Column(name = "region_ar")
    private String regionAr;

    @Column(name = "cercle_fr")
    private String cercleFr;

    @Column(name = "code_cercle")
    private Double codeCercle;

    @Column(name = "com_fr")
    private String comFr;

    @Column(name = "code_com")
    private Double codeCom;

    @Column(name = "centre_fr")
    private String centreFr;

    @Column(name = "cod_ac")
    private Double codAc;

    @Column(name = "com_ar")
    private String comAr;

    @Column(name = "cc")
    private Double cc;

    @Column(name = "centre_ar")
    private String centreAr;

    @Column(name = "nom_ar")
    private String nomAr;

    @Column(name = "nom_fr")
    private String nomFr;

    @Column(name = "geometry")
    private String geometry;

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

    public Double getCodeReg() {
        return this.codeReg;
    }

    public Commune codeReg(Double codeReg) {
        this.setCodeReg(codeReg);
        return this;
    }

    public void setCodeReg(Double codeReg) {
        this.codeReg = codeReg;
    }

    public Double getCodeProv() {
        return this.codeProv;
    }

    public Commune codeProv(Double codeProv) {
        this.setCodeProv(codeProv);
        return this;
    }

    public void setCodeProv(Double codeProv) {
        this.codeProv = codeProv;
    }

    public String getProvinceFr() {
        return this.provinceFr;
    }

    public Commune provinceFr(String provinceFr) {
        this.setProvinceFr(provinceFr);
        return this;
    }

    public void setProvinceFr(String provinceFr) {
        this.provinceFr = provinceFr;
    }

    public String getProvinceAr() {
        return this.provinceAr;
    }

    public Commune provinceAr(String provinceAr) {
        this.setProvinceAr(provinceAr);
        return this;
    }

    public void setProvinceAr(String provinceAr) {
        this.provinceAr = provinceAr;
    }

    public String getRegionFr() {
        return this.regionFr;
    }

    public Commune regionFr(String regionFr) {
        this.setRegionFr(regionFr);
        return this;
    }

    public void setRegionFr(String regionFr) {
        this.regionFr = regionFr;
    }

    public String getRegionAr() {
        return this.regionAr;
    }

    public Commune regionAr(String regionAr) {
        this.setRegionAr(regionAr);
        return this;
    }

    public void setRegionAr(String regionAr) {
        this.regionAr = regionAr;
    }

    public String getCercleFr() {
        return this.cercleFr;
    }

    public Commune cercleFr(String cercleFr) {
        this.setCercleFr(cercleFr);
        return this;
    }

    public void setCercleFr(String cercleFr) {
        this.cercleFr = cercleFr;
    }

    public Double getCodeCercle() {
        return this.codeCercle;
    }

    public Commune codeCercle(Double codeCercle) {
        this.setCodeCercle(codeCercle);
        return this;
    }

    public void setCodeCercle(Double codeCercle) {
        this.codeCercle = codeCercle;
    }

    public String getComFr() {
        return this.comFr;
    }

    public Commune comFr(String comFr) {
        this.setComFr(comFr);
        return this;
    }

    public void setComFr(String comFr) {
        this.comFr = comFr;
    }

    public Double getCodeCom() {
        return this.codeCom;
    }

    public Commune codeCom(Double codeCom) {
        this.setCodeCom(codeCom);
        return this;
    }

    public void setCodeCom(Double codeCom) {
        this.codeCom = codeCom;
    }

    public String getCentreFr() {
        return this.centreFr;
    }

    public Commune centreFr(String centreFr) {
        this.setCentreFr(centreFr);
        return this;
    }

    public void setCentreFr(String centreFr) {
        this.centreFr = centreFr;
    }

    public Double getCodAc() {
        return this.codAc;
    }

    public Commune codAc(Double codAc) {
        this.setCodAc(codAc);
        return this;
    }

    public void setCodAc(Double codAc) {
        this.codAc = codAc;
    }

    public String getComAr() {
        return this.comAr;
    }

    public Commune comAr(String comAr) {
        this.setComAr(comAr);
        return this;
    }

    public void setComAr(String comAr) {
        this.comAr = comAr;
    }

    public Double getCc() {
        return this.cc;
    }

    public Commune cc(Double cc) {
        this.setCc(cc);
        return this;
    }

    public void setCc(Double cc) {
        this.cc = cc;
    }

    public String getCentreAr() {
        return this.centreAr;
    }

    public Commune centreAr(String centreAr) {
        this.setCentreAr(centreAr);
        return this;
    }

    public void setCentreAr(String centreAr) {
        this.centreAr = centreAr;
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

    public String getNomFr() {
        return this.nomFr;
    }

    public Commune nomFr(String nomFr) {
        this.setNomFr(nomFr);
        return this;
    }

    public void setNomFr(String nomFr) {
        this.nomFr = nomFr;
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
            ", codeReg=" + getCodeReg() +
            ", codeProv=" + getCodeProv() +
            ", provinceFr='" + getProvinceFr() + "'" +
            ", provinceAr='" + getProvinceAr() + "'" +
            ", regionFr='" + getRegionFr() + "'" +
            ", regionAr='" + getRegionAr() + "'" +
            ", cercleFr='" + getCercleFr() + "'" +
            ", codeCercle=" + getCodeCercle() +
            ", comFr='" + getComFr() + "'" +
            ", codeCom=" + getCodeCom() +
            ", centreFr='" + getCentreFr() + "'" +
            ", codAc=" + getCodAc() +
            ", comAr='" + getComAr() + "'" +
            ", cc=" + getCc() +
            ", centreAr='" + getCentreAr() + "'" +
            ", nomAr='" + getNomAr() + "'" +
            ", nomFr='" + getNomFr() + "'" +
            ", geometry='" + getGeometry() + "'" +
            "}";
    }
}
