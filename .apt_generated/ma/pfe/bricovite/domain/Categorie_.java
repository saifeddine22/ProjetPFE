package ma.pfe.bricovite.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Categorie.class)
public abstract class Categorie_ {

	public static volatile SingularAttribute<Categorie, String> nomFr;
	public static volatile SetAttribute<Categorie, Activite> activites;
	public static volatile SingularAttribute<Categorie, Long> id;
	public static volatile SingularAttribute<Categorie, String> nomAr;

	public static final String NOM_FR = "nomFr";
	public static final String ACTIVITES = "activites";
	public static final String ID = "id";
	public static final String NOM_AR = "nomAr";

}

