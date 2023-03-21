package shop.wesellbuy.secondproject.domain.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHomeAppliances is a Querydsl query type for HomeAppliances
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHomeAppliances extends EntityPathBase<HomeAppliances> {

    private static final long serialVersionUID = 56251839L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHomeAppliances homeAppliances = new QHomeAppliances("homeAppliances");

    public final shop.wesellbuy.secondproject.domain.QItem _super;

    public final StringPath company = createString("company");

    //inherited
    public final StringPath content;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    //inherited
    public final StringPath dtype;

    //inherited
    public final NumberPath<Integer> hits;

    //inherited
    public final ListPath<shop.wesellbuy.secondproject.domain.likes.ItemLikes, shop.wesellbuy.secondproject.domain.likes.QItemLikes> itemLikesList;

    //inherited
    public final ListPath<ItemPicture, QItemPicture> itemPictureList;

    //inherited
    public final ListPath<shop.wesellbuy.secondproject.domain.reply.ItemReply, shop.wesellbuy.secondproject.domain.reply.QItemReply> itemReplyList;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    // inherited
    public final shop.wesellbuy.secondproject.domain.QMember member;

    //inherited
    public final StringPath name;

    //inherited
    public final NumberPath<Integer> num;

    //inherited
    public final NumberPath<Integer> price;

    //inherited
    public final EnumPath<ItemStatus> status;

    //inherited
    public final NumberPath<Integer> stock;

    public QHomeAppliances(String variable) {
        this(HomeAppliances.class, forVariable(variable), INITS);
    }

    public QHomeAppliances(Path<? extends HomeAppliances> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHomeAppliances(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHomeAppliances(PathMetadata metadata, PathInits inits) {
        this(HomeAppliances.class, metadata, inits);
    }

    public QHomeAppliances(Class<? extends HomeAppliances> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new shop.wesellbuy.secondproject.domain.QItem(type, metadata, inits);
        this.content = _super.content;
        this.createdDate = _super.createdDate;
        this.dtype = _super.dtype;
        this.hits = _super.hits;
        this.itemLikesList = _super.itemLikesList;
        this.itemPictureList = _super.itemPictureList;
        this.itemReplyList = _super.itemReplyList;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.member = _super.member;
        this.name = _super.name;
        this.num = _super.num;
        this.price = _super.price;
        this.status = _super.status;
        this.stock = _super.stock;
    }

}

