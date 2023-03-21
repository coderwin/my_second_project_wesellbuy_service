package shop.wesellbuy.secondproject.domain.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseDateColumnEntity is a Querydsl query type for BaseDateColumnEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseDateColumnEntity extends EntityPathBase<BaseDateColumnEntity> {

    private static final long serialVersionUID = 1357310618L;

    public static final QBaseDateColumnEntity baseDateColumnEntity = new QBaseDateColumnEntity("baseDateColumnEntity");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = createDateTime("lastModifiedDate", java.time.LocalDateTime.class);

    public QBaseDateColumnEntity(String variable) {
        super(BaseDateColumnEntity.class, forVariable(variable));
    }

    public QBaseDateColumnEntity(Path<? extends BaseDateColumnEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseDateColumnEntity(PathMetadata metadata) {
        super(BaseDateColumnEntity.class, metadata);
    }

}

