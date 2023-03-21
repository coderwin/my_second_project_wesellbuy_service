package shop.wesellbuy.secondproject.domain.reply;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecommendationReply is a Querydsl query type for RecommendationReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommendationReply extends EntityPathBase<RecommendationReply> {

    private static final long serialVersionUID = 1450461452L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecommendationReply recommendationReply = new QRecommendationReply("recommendationReply");

    public final shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity _super = new shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final shop.wesellbuy.secondproject.domain.QMember member;

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final shop.wesellbuy.secondproject.domain.QRecommendation recommendation;

    public final EnumPath<ReplyStatus> status = createEnum("status", ReplyStatus.class);

    public QRecommendationReply(String variable) {
        this(RecommendationReply.class, forVariable(variable), INITS);
    }

    public QRecommendationReply(Path<? extends RecommendationReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecommendationReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecommendationReply(PathMetadata metadata, PathInits inits) {
        this(RecommendationReply.class, metadata, inits);
    }

    public QRecommendationReply(Class<? extends RecommendationReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new shop.wesellbuy.secondproject.domain.QMember(forProperty("member"), inits.get("member")) : null;
        this.recommendation = inits.isInitialized("recommendation") ? new shop.wesellbuy.secondproject.domain.QRecommendation(forProperty("recommendation"), inits.get("recommendation")) : null;
    }

}

