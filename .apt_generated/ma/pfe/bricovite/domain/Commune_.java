package ma.pfe.bricovite.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Commune.class)
public abstract class Commune_ {

	public static volatile SingularAttribute<Commune, Double> cc;
	public static volatile SingularAttribute<Commune, Double> codeReg;
	public static volatile SingularAttribute<Commune, String> provinceAr;
	public static volatile SingularAttribute<Commune, String> comFr;
	public static volatile SingularAttribute<Commune, Double> codeCercle;
	public static volatile SingularAttribute<Commune, String> cercleFr;
	public static volatile SingularAttribute<Commune, String> centreFr;
	public static volatile SingularAttribute<Commune, Double> codeProv;
	public static volatile SingularAttribute<Commune, Province> province;
	public static volatile SingularAttribute<Commune, String> comAr;
	public static volatile SetAttribute<Commune, Annonce> annonces;
	public static volatile SingularAttribute<Commune, Double> codeCom;
	public static volatile SingularAttribute<Commune, String> provinceFr;
	public static volatile SingularAttribute<Commune, String> regionFr;
	public static volatile SingularAttribute<Commune, String> centreAr;
	public static volatile SingularAttribute<Commune, String> nomFr;
	public static volatile SingularAttribute<Commune, String> geometry;
	public static volatile SingularAttribute<Commune, Long> id;
	public static volatile SingularAttribute<Commune, Double> codAc;
	public static volatile SingularAttribute<Commune, String> regionAr;
	public static volatile SingularAttribute<Commune, String> nomAr;

	public static final String CC = "cc";
	public static final String CODE_REG = "codeReg";
	public static final String PROVINCE_AR = "provinceAr";
	public static final String COM_FR = "comFr";
	public static final String CODE_CERCLE = "codeCercle";
	public static final String CERCLE_FR = "cercleFr";
	public static final String CENTRE_FR = "centreFr";
	public static final String CODE_PROV = "codeProv";
	public static final String PROVINCE = "province";
	public static final String COM_AR = "comAr";
	public static final String ANNONCES = "annonces";
	public static final String CODE_COM = "codeCom";
	public static final String PROVINCE_FR = "provinceFr";
	public static final String REGION_FR = "regionFr";
	public static final String CENTRE_AR = "centreAr";
	public static final String NOM_FR = "nomFr";
	public static final String GEOMETRY = "geometry";
	public static final String ID = "id";
	public static final String COD_AC = "codAc";
	public static final String REGION_AR = "regionAr";
	public static final String NOM_AR = "nomAr";

}

