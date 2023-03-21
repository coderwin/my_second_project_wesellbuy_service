package shop.wesellbuy.secondproject.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomerService is a Querydsl query type for CustomerService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomerService extends EntityPathBase<CustomerService> {

    private static final long serialVersionUID = -164047082L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomerService customerService = new QCustomerService("customerService");

    public final shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity _super = new shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final ListPath<shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply, shop.wesellbuy.secondproject.domain.reply.QCustomerServiceReply> customerServiceReplyList = this.<shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply, shop.wesellbuy.secondproject.domain.reply.QCustomerServiceReply>createList("customerServiceReplyList", shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply.class, shop.wesellbuy.secondproject.domain.reply.QCustomerServiceReply.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QMember member;

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final StringPath reportedId = createString("reportedId");

    public QCustomerService(String variable) {
        this(CustomerService.class, forVariable(variable), INITS);
    }

    public QCustomerService(Path<? extends CustomerService> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomerService(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomerService(PathMetadata metadata, PathInits inits) {
        this(CustomerService.class, metadata, inits);
    }

    public QCustomerService(Class<? extends CustomerService> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

