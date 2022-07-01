package ma.pfe.bricovite.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Personne.class)
public abstract class Personne_ {

	public static volatile SingularAttribute<Personne, String> cnie;
	public static volatile SingularAttribute<Personne, String> tel;
	public static volatile SingularAttribute<Personne, Long> id;
	public static volatile SingularAttribute<Personne, User> user;

	public static final String CNIE = "cnie";
	public static final String TEL = "tel";
	public static final String ID = "id";
	public static final String USER = "user";

}

