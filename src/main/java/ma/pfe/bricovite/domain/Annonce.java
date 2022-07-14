package ma.pfe.bricovite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Annonce.
 */
@Entity
@Table(name = "annonce")
public class Annonce implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "date_annonce")
    private Instant dateAnnonce;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @OneToMany(mappedBy = "annonce", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties(value = { "annonce" }, allowSetters = true)
    private Set<Photo> photos = new HashSet<>();

    @OneToMany(mappedBy = "annonce", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties(value = { "annonce" }, allowSetters = true)
    private Set<Commentaire> commentaires = new HashSet<>();

    @OneToMany(mappedBy = "annonce", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties(value = { "annonce" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "annonces", "province" }, allowSetters = true)
    private Commune commune;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "annonces", "categorie" }, allowSetters = true)
    private Activite activite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Annonce id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Annonce titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public Annonce description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Annonce adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Annonce status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Instant getDateAnnonce() {
        return this.dateAnnonce;
    }

    public Annonce dateAnnonce(Instant dateAnnonce) {
        this.setDateAnnonce(dateAnnonce);
        return this;
    }

    public void setDateAnnonce(Instant dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Annonce latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Annonce longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Set<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<Photo> photos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setAnnonce(null));
        }
        if (photos != null) {
            photos.forEach(i -> i.setAnnonce(this));
        }
        this.photos = photos;
    }

    public Annonce photos(Set<Photo> photos) {
        this.setPhotos(photos);
        return this;
    }

    public Annonce addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setAnnonce(this);
        return this;
    }

    public Annonce removePhoto(Photo photo) {
        this.photos.remove(photo);
        photo.setAnnonce(null);
        return this;
    }

    public Set<Commentaire> getCommentaires() {
        return this.commentaires;
    }

    public void setCommentaires(Set<Commentaire> commentaires) {
        if (this.commentaires != null) {
            this.commentaires.forEach(i -> i.setAnnonce(null));
        }
        if (commentaires != null) {
            commentaires.forEach(i -> i.setAnnonce(this));
        }
        this.commentaires = commentaires;
    }

    public Annonce commentaires(Set<Commentaire> commentaires) {
        this.setCommentaires(commentaires);
        return this;
    }

    public Annonce addCommentaire(Commentaire commentaire) {
        this.commentaires.add(commentaire);
        commentaire.setAnnonce(this);
        return this;
    }

    public Annonce removeCommentaire(Commentaire commentaire) {
        this.commentaires.remove(commentaire);
        commentaire.setAnnonce(null);
        return this;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setAnnonce(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setAnnonce(this));
        }
        this.notes = notes;
    }

    public Annonce notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public Annonce addNote(Note note) {
        this.notes.add(note);
        note.setAnnonce(this);
        return this;
    }

    public Annonce removeNote(Note note) {
        this.notes.remove(note);
        note.setAnnonce(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Annonce user(User user) {
        this.setUser(user);
        return this;
    }

    public Commune getCommune() {
        return this.commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Annonce commune(Commune commune) {
        this.setCommune(commune);
        return this;
    }

    public Activite getActivite() {
        return this.activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public Annonce activite(Activite activite) {
        this.setActivite(activite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Annonce)) {
            return false;
        }
        return id != null && id.equals(((Annonce) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Annonce{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", status='" + getStatus() + "'" +
            ", dateAnnonce='" + getDateAnnonce() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            "}";
    }
}
