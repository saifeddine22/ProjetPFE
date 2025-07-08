package ma.pfe.bricovite.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Province.class)
public abstract class Province_ {

	public static volatile SingularAttribute<Province, Double> codeReg;
	public static volatile SingularAttribute<Province, Double> codeProv;
	public static volatile SingularAttribute<Province, String> nomFr;
	public static volatile SingularAttribute<Province, String> regionFr;
	public static volatile SingularAttribute<Province, String> geometry;
	public static volatile SingularAttribute<Province, Long> id;
	public static volatile SetAttribute<Province, Commune> communes;
	public static volatile SingularAttribute<Province, Region> region;
	public static volatile SingularAttribute<Province, String> nomAr;
	public static volatile SingularAttribute<Province, String> regionAr;

	public static final String CODE_REG = "codeReg";
	public static final String CODE_PROV = "codeProv";
	public static final String NOM_FR = "nomFr";
	public static final String REGION_FR = "regionFr";
	public static final String GEOMETRY = "geometry";
	public static final String ID = "id";
	public static final String COMMUNES = "communes";
	public static final String REGION = "region";
	public static final String NOM_AR = "nomAr";
	public static final String REGION_AR = "regionAr";

}

