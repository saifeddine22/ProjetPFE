package ma.pfe.bricovite.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Commentaire.class)
public abstract class Commentaire_ {

	public static volatile SingularAttribute<Commentaire, Annonce> annonce;
	public static volatile SingularAttribute<Commentaire, Instant> dateCommentaire;
	public static volatile SingularAttribute<Commentaire, String> details;
	public static volatile SingularAttribute<Commentaire, Long> id;
	public static volatile SingularAttribute<Commentaire, User> user;

	public static final String ANNONCE = "annonce";
	public static final String DATE_COMMENTAIRE = "dateCommentaire";
	public static final String DETAILS = "details";
	public static final String ID = "id";
	public static final String USER = "user";

}

