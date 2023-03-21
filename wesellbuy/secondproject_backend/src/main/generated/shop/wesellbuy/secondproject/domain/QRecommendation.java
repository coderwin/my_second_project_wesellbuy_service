package shop.wesellbuy.secondproject.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecommendation is a Querydsl query type for Recommendation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommendation extends EntityPathBase<Recommendation> {

    private static final long serialVersionUID = 935629242L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecommendation recommendation = new QRecommendation("recommendation");

    public final shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity _super = new shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Integer> hits = createNumber("hits", Integer.class);

    public final StringPath itemName = createString("itemName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QMember member;

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final ListPath<shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture, shop.wesellbuy.secondproject.domain.recommendation.QRecommendationPicture> recommendationPictureList = this.<shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture, shop.wesellbuy.secondproject.domain.recommendation.QRecommendationPicture>createList("recommendationPictureList", shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture.class, shop.wesellbuy.secondproject.domain.recommendation.QRecommendationPicture.class, PathInits.DIRECT2);

    public final ListPath<shop.wesellbuy.secondproject.domain.reply.RecommendationReply, shop.wesellbuy.secondproject.domain.reply.QRecommendationReply> recommendationReplyList = this.<shop.wesellbuy.secondproject.domain.reply.RecommendationReply, shop.wesellbuy.secondproject.domain.reply.QRecommendationReply>createList("recommendationReplyList", shop.wesellbuy.secondproject.domain.reply.RecommendationReply.class, shop.wesellbuy.secondproject.domain.reply.QRecommendationReply.class, PathInits.DIRECT2);

    public final StringPath sellerId = createString("sellerId");

    public final EnumPath<shop.wesellbuy.secondproject.domain.board.BoardStatus> status = createEnum("status", shop.wesellbuy.secondproject.domain.board.BoardStatus.class);

    public QRecommendation(String variable) {
        this(Recommendation.class, forVariable(variable), INITS);
    }

    public QRecommendation(Path<? extends Recommendation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecommendation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecommendation(PathMetadata metadata, PathInits inits) {
        this(Recommendation.class, metadata, inits);
    }

    public QRecommendation(Class<? extends Recommendation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

