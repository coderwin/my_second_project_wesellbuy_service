package shop.wesellbuy.secondproject.domain.reply;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomerServiceReply is a Querydsl query type for CustomerServiceReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomerServiceReply extends EntityPathBase<CustomerServiceReply> {

    private static final long serialVersionUID = -205779656L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomerServiceReply customerServiceReply = new QCustomerServiceReply("customerServiceReply");

    public final shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity _super = new shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final shop.wesellbuy.secondproject.domain.QCustomerService customerService;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final shop.wesellbuy.secondproject.domain.QMember member;

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final EnumPath<ReplyStatus> status = createEnum("status", ReplyStatus.class);

    public QCustomerServiceReply(String variable) {
        this(CustomerServiceReply.class, forVariable(variable), INITS);
    }

    public QCustomerServiceReply(Path<? extends CustomerServiceReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomerServiceReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomerServiceReply(PathMetadata metadata, PathInits inits) {
        this(CustomerServiceReply.class, metadata, inits);
    }

    public QCustomerServiceReply(Class<? extends CustomerServiceReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.customerService = inits.isInitialized("customerService") ? new shop.wesellbuy.secondproject.domain.QCustomerService(forProperty("customerService"), inits.get("customerService")) : null;
        this.member = inits.isInitialized("member") ? new shop.wesellbuy.secondproject.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

