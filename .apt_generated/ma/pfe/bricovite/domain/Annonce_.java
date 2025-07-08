package ma.pfe.bricovite.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Annonce.class)
public abstract class Annonce_ {

	public static volatile SetAttribute<Annonce, Note> notes;
	public static volatile SingularAttribute<Annonce, String> titre;
	public static volatile SingularAttribute<Annonce, Commune> commune;
	public static volatile SingularAttribute<Annonce, Double> latitude;
	public static volatile SingularAttribute<Annonce, String> description;
	public static volatile SetAttribute<Annonce, Photo> photos;
	public static volatile SingularAttribute<Annonce, Activite> activite;
	public static volatile SingularAttribute<Annonce, String> adresse;
	public static volatile SetAttribute<Annonce, Commentaire> commentaires;
	public static volatile SingularAttribute<Annonce, Long> id;
	public static volatile SingularAttribute<Annonce, Instant> dateAnnonce;
	public static volatile SingularAttribute<Annonce, User> user;
	public static volatile SingularAttribute<Annonce, Boolean> status;
	public static volatile SingularAttribute<Annonce, Double> longitude;

	public static final String NOTES = "notes";
	public static final String TITRE = "titre";
	public static final String COMMUNE = "commune";
	public static final String LATITUDE = "latitude";
	public static final String DESCRIPTION = "description";
	public static final String PHOTOS = "photos";
	public static final String ACTIVITE = "activite";
	public static final String ADRESSE = "adresse";
	public static final String COMMENTAIRES = "commentaires";
	public static final String ID = "id";
	public static final String DATE_ANNONCE = "dateAnnonce";
	public static final String USER = "user";
	public static final String STATUS = "status";
	public static final String LONGITUDE = "longitude";

}

