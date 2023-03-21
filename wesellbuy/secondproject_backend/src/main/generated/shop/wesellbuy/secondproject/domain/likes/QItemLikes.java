package shop.wesellbuy.secondproject.domain.likes;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemLikes is a Querydsl query type for ItemLikes
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemLikes extends EntityPathBase<ItemLikes> {

    private static final long serialVersionUID = 1844854486L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemLikes itemLikes = new QItemLikes("itemLikes");

    public final shop.wesellbuy.secondproject.domain.QItem item;

    public final shop.wesellbuy.secondproject.domain.QMember member;

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public QItemLikes(String variable) {
        this(ItemLikes.class, forVariable(variable), INITS);
    }

    public QItemLikes(Path<? extends ItemLikes> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemLikes(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemLikes(PathMetadata metadata, PathInits inits) {
        this(ItemLikes.class, metadata, inits);
    }

    public QItemLikes(Class<? extends ItemLikes> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new shop.wesellbuy.secondproject.domain.QItem(forProperty("item"), inits.get("item")) : null;
        this.member = inits.isInitialized("member") ? new shop.wesellbuy.secondproject.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

