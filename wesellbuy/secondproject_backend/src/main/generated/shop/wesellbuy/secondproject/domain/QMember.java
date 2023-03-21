package shop.wesellbuy.secondproject.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1948609947L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity _super = new shop.wesellbuy.secondproject.domain.common.QBaseDateColumnEntity(this);

    public final shop.wesellbuy.secondproject.domain.member.QAddress address;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final ListPath<CustomerService, QCustomerService> customerServiceList = this.<CustomerService, QCustomerService>createList("customerServiceList", CustomerService.class, QCustomerService.class, PathInits.DIRECT2);

    public final ListPath<shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply, shop.wesellbuy.secondproject.domain.reply.QCustomerServiceReply> customerServiceReplyList = this.<shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply, shop.wesellbuy.secondproject.domain.reply.QCustomerServiceReply>createList("customerServiceReplyList", shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply.class, shop.wesellbuy.secondproject.domain.reply.QCustomerServiceReply.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final StringPath id = createString("id");

    public final ListPath<Item, QItem> itemList = this.<Item, QItem>createList("itemList", Item.class, QItem.class, PathInits.DIRECT2);

    public final ListPath<shop.wesellbuy.secondproject.domain.reply.ItemReply, shop.wesellbuy.secondproject.domain.reply.QItemReply> ItemReplyList = this.<shop.wesellbuy.secondproject.domain.reply.ItemReply, shop.wesellbuy.secondproject.domain.reply.QItemReply>createList("ItemReplyList", shop.wesellbuy.secondproject.domain.reply.ItemReply.class, shop.wesellbuy.secondproject.domain.reply.QItemReply.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final ListPath<Order, QOrder> orderList = this.<Order, QOrder>createList("orderList", Order.class, QOrder.class, PathInits.DIRECT2);

    public final shop.wesellbuy.secondproject.domain.member.QPhone phones;

    public final StringPath pwd = createString("pwd");

    public final ListPath<Recommendation, QRecommendation> recommendationList = this.<Recommendation, QRecommendation>createList("recommendationList", Recommendation.class, QRecommendation.class, PathInits.DIRECT2);

    public final ListPath<shop.wesellbuy.secondproject.domain.reply.RecommendationReply, shop.wesellbuy.secondproject.domain.reply.QRecommendationReply> recommendationReplyList = this.<shop.wesellbuy.secondproject.domain.reply.RecommendationReply, shop.wesellbuy.secondproject.domain.reply.QRecommendationReply>createList("recommendationReplyList", shop.wesellbuy.secondproject.domain.reply.RecommendationReply.class, shop.wesellbuy.secondproject.domain.reply.QRecommendationReply.class, PathInits.DIRECT2);

    public final shop.wesellbuy.secondproject.domain.member.QSelfPicture selfPicture;

    public final EnumPath<shop.wesellbuy.secondproject.domain.member.MemberStatus> status = createEnum("status", shop.wesellbuy.secondproject.domain.member.MemberStatus.class);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new shop.wesellbuy.secondproject.domain.member.QAddress(forProperty("address")) : null;
        this.phones = inits.isInitialized("phones") ? new shop.wesellbuy.secondproject.domain.member.QPhone(forProperty("phones")) : null;
        this.selfPicture = inits.isInitialized("selfPicture") ? new shop.wesellbuy.secondproject.domain.member.QSelfPicture(forProperty("selfPicture"), inits.get("selfPicture")) : null;
    }

}

