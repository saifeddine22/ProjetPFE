package ma.pfe.bricovite.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Activite.class)
public abstract class Activite_ {

	public static volatile SingularAttribute<Activite, String> categorieFr;
	public static volatile SingularAttribute<Activite, Categorie> categorie;
	public static volatile SetAttribute<Activite, Annonce> annonces;
	public static volatile SingularAttribute<Activite, String> categorieAr;
	public static volatile SingularAttribute<Activite, String> nomFr;
	public static volatile SingularAttribute<Activite, Long> id;
	public static volatile SingularAttribute<Activite, String> nomAr;

	public static final String CATEGORIE_FR = "categorieFr";
	public static final String CATEGORIE = "categorie";
	public static final String ANNONCES = "annonces";
	public static final String CATEGORIE_AR = "categorieAr";
	public static final String NOM_FR = "nomFr";
	public static final String ID = "id";
	public static final String NOM_AR = "nomAr";

}

