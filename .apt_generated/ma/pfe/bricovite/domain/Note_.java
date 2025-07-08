package ma.pfe.bricovite.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Note.class)
public abstract class Note_ {

	public static volatile SingularAttribute<Note, Annonce> annonce;
	public static volatile SingularAttribute<Note, Integer> valeur;
	public static volatile SingularAttribute<Note, Long> id;

	public static final String ANNONCE = "annonce";
	public static final String VALEUR = "valeur";
	public static final String ID = "id";

}

