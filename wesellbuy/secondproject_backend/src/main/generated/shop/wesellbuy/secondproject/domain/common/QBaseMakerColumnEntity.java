package shop.wesellbuy.secondproject.domain.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseMakerColumnEntity is a Querydsl query type for BaseMakerColumnEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseMakerColumnEntity extends EntityPathBase<BaseMakerColumnEntity> {

    private static final long serialVersionUID = -2051883350L;

    public static final QBaseMakerColumnEntity baseMakerColumnEntity = new QBaseMakerColumnEntity("baseMakerColumnEntity");

    public final StringPath createdBy = createString("createdBy");

    public final StringPath lastModifiedBy = createString("lastModifiedBy");

    public QBaseMakerColumnEntity(String variable) {
        super(BaseMakerColumnEntity.class, forVariable(variable));
    }

    public QBaseMakerColumnEntity(Path<? extends BaseMakerColumnEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseMakerColumnEntity(PathMetadata metadata) {
        super(BaseMakerColumnEntity.class, metadata);
    }

}

