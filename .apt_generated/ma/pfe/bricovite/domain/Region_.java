package ma.pfe.bricovite.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Region.class)
public abstract class Region_ {

	public static volatile SetAttribute<Region, Province> provinces;
	public static volatile SingularAttribute<Region, Double> codeReg;
	public static volatile SingularAttribute<Region, String> nomFr;
	public static volatile SingularAttribute<Region, String> geometry;
	public static volatile SingularAttribute<Region, Long> id;
	public static volatile SingularAttribute<Region, String> nomAr;

	public static final String PROVINCES = "provinces";
	public static final String CODE_REG = "codeReg";
	public static final String NOM_FR = "nomFr";
	public static final String GEOMETRY = "geometry";
	public static final String ID = "id";
	public static final String NOM_AR = "nomAr";

}

