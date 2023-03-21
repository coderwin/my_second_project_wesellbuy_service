package shop.wesellbuy.secondproject.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSelfPicture is a Querydsl query type for SelfPicture
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSelfPicture extends EntityPathBase<SelfPicture> {

    private static final long serialVersionUID = 1684412415L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSelfPicture selfPicture = new QSelfPicture("selfPicture");

    public final shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity _super = new shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final shop.wesellbuy.secondproject.domain.QMember member;

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final StringPath storedFileName = createString("storedFileName");

    public QSelfPicture(String variable) {
        this(SelfPicture.class, forVariable(variable), INITS);
    }

    public QSelfPicture(Path<? extends SelfPicture> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSelfPicture(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSelfPicture(PathMetadata metadata, PathInits inits) {
        this(SelfPicture.class, metadata, inits);
    }

    public QSelfPicture(Class<? extends SelfPicture> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new shop.wesellbuy.secondproject.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

