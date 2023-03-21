package shop.wesellbuy.secondproject.domain.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemPicture is a Querydsl query type for ItemPicture
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemPicture extends EntityPathBase<ItemPicture> {

    private static final long serialVersionUID = 488691089L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemPicture itemPicture = new QItemPicture("itemPicture");

    public final shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity _super = new shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final shop.wesellbuy.secondproject.domain.QItem item;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final EnumPath<shop.wesellbuy.secondproject.domain.common.PictureStatus> status = createEnum("status", shop.wesellbuy.secondproject.domain.common.PictureStatus.class);

    public final StringPath storedFileName = createString("storedFileName");

    public QItemPicture(String variable) {
        this(ItemPicture.class, forVariable(variable), INITS);
    }

    public QItemPicture(Path<? extends ItemPicture> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemPicture(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemPicture(PathMetadata metadata, PathInits inits) {
        this(ItemPicture.class, metadata, inits);
    }

    public QItemPicture(Class<? extends ItemPicture> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new shop.wesellbuy.secondproject.domain.QItem(forProperty("item"), inits.get("item")) : null;
    }

}

