package shop.wesellbuy.secondproject.domain.recommendation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecommendationPicture is a Querydsl query type for RecommendationPicture
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommendationPicture extends EntityPathBase<RecommendationPicture> {

    private static final long serialVersionUID = 21516689L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecommendationPicture recommendationPicture = new QRecommendationPicture("recommendationPicture");

    public final shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity _super = new shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final shop.wesellbuy.secondproject.domain.QRecommendation recommendation;

    public final EnumPath<shop.wesellbuy.secondproject.domain.common.PictureStatus> status = createEnum("status", shop.wesellbuy.secondproject.domain.common.PictureStatus.class);

    public final StringPath storedFileName = createString("storedFileName");

    public QRecommendationPicture(String variable) {
        this(RecommendationPicture.class, forVariable(variable), INITS);
    }

    public QRecommendationPicture(Path<? extends RecommendationPicture> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecommendationPicture(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecommendationPicture(PathMetadata metadata, PathInits inits) {
        this(RecommendationPicture.class, metadata, inits);
    }

    public QRecommendationPicture(Class<? extends RecommendationPicture> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recommendation = inits.isInitialized("recommendation") ? new shop.wesellbuy.secondproject.domain.QRecommendation(forProperty("recommendation"), inits.get("recommendation")) : null;
    }

}

