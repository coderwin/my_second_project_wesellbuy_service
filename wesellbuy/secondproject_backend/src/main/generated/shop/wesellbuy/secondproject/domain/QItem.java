package shop.wesellbuy.secondproject.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -230479276L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity _super = new shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath dtype = createString("dtype");

    public final NumberPath<Integer> hits = createNumber("hits", Integer.class);

    public final ListPath<shop.wesellbuy.secondproject.domain.likes.ItemLikes, shop.wesellbuy.secondproject.domain.likes.QItemLikes> itemLikesList = this.<shop.wesellbuy.secondproject.domain.likes.ItemLikes, shop.wesellbuy.secondproject.domain.likes.QItemLikes>createList("itemLikesList", shop.wesellbuy.secondproject.domain.likes.ItemLikes.class, shop.wesellbuy.secondproject.domain.likes.QItemLikes.class, PathInits.DIRECT2);

    public final ListPath<shop.wesellbuy.secondproject.domain.item.ItemPicture, shop.wesellbuy.secondproject.domain.item.QItemPicture> itemPictureList = this.<shop.wesellbuy.secondproject.domain.item.ItemPicture, shop.wesellbuy.secondproject.domain.item.QItemPicture>createList("itemPictureList", shop.wesellbuy.secondproject.domain.item.ItemPicture.class, shop.wesellbuy.secondproject.domain.item.QItemPicture.class, PathInits.DIRECT2);

    public final ListPath<shop.wesellbuy.secondproject.domain.reply.ItemReply, shop.wesellbuy.secondproject.domain.reply.QItemReply> itemReplyList = this.<shop.wesellbuy.secondproject.domain.reply.ItemReply, shop.wesellbuy.secondproject.domain.reply.QItemReply>createList("itemReplyList", shop.wesellbuy.secondproject.domain.reply.ItemReply.class, shop.wesellbuy.secondproject.domain.reply.QItemReply.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QMember member;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final EnumPath<shop.wesellbuy.secondproject.domain.item.ItemStatus> status = createEnum("status", shop.wesellbuy.secondproject.domain.item.ItemStatus.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

