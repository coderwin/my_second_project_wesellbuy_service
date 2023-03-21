package shop.wesellbuy.secondproject.domain.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -978062717L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBook book = new QBook("book");

    public final shop.wesellbuy.secondproject.domain.QItem _super;

    public final StringPath author = createString("author");

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

    public final StringPath publisher = createString("publisher");

    //inherited
    public final EnumPath<ItemStatus> status;

    //inherited
    public final NumberPath<Integer> stock;

    public QBook(String variable) {
        this(Book.class, forVariable(variable), INITS);
    }

    public QBook(Path<? extends Book> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBook(PathMetadata metadata, PathInits inits) {
        this(Book.class, metadata, inits);
    }

    public QBook(Class<? extends Book> type, PathMetadata metadata, PathInits inits) {
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

