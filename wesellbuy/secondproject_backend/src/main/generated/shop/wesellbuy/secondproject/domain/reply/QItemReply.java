package shop.wesellbuy.secondproject.domain.reply;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemReply is a Querydsl query type for ItemReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemReply extends EntityPathBase<ItemReply> {

    private static final long serialVersionUID = 1572682674L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemReply itemReply = new QItemReply("itemReply");

    public final shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity _super = new shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final shop.wesellbuy.secondproject.domain.QItem item;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final shop.wesellbuy.secondproject.domain.QMember member;

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final EnumPath<ReplyStatus> status = createEnum("status", ReplyStatus.class);

    public QItemReply(String variable) {
        this(ItemReply.class, forVariable(variable), INITS);
    }

    public QItemReply(Path<? extends ItemReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemReply(PathMetadata metadata, PathInits inits) {
        this(ItemReply.class, metadata, inits);
    }

    public QItemReply(Class<? extends ItemReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new shop.wesellbuy.secondproject.domain.QItem(forProperty("item"), inits.get("item")) : null;
        this.member = inits.isInitialized("member") ? new shop.wesellbuy.secondproject.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

