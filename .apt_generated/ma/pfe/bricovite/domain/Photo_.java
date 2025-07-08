package ma.pfe.bricovite.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Photo.class)
public abstract class Photo_ {

	public static volatile SingularAttribute<Photo, byte[]> image;
	public static volatile SingularAttribute<Photo, Annonce> annonce;
	public static volatile SingularAttribute<Photo, String> libelle;
	public static volatile SingularAttribute<Photo, String> imageContentType;
	public static volatile SingularAttribute<Photo, Long> id;
	public static volatile SingularAttribute<Photo, String> url;

	public static final String IMAGE = "image";
	public static final String ANNONCE = "annonce";
	public static final String LIBELLE = "libelle";
	public static final String IMAGE_CONTENT_TYPE = "imageContentType";
	public static final String ID = "id";
	public static final String URL = "url";

}

