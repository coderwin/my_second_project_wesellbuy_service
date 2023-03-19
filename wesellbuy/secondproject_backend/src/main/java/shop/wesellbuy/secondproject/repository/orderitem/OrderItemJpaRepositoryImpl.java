package shop.wesellbuy.secondproject.repository.orderitem;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import shop.wesellbuy.secondproject.domain.OrderItem;
import shop.wesellbuy.secondproject.domain.QMember;
import shop.wesellbuy.secondproject.domain.delivery.DeliveryStatus;
import shop.wesellbuy.secondproject.domain.order.OrderStatus;
import shop.wesellbuy.secondproject.util.LocalDateParser;

import java.util.List;

import static shop.wesellbuy.secondproject.domain.QDelivery.delivery;
import static shop.wesellbuy.secondproject.domain.QItem.item;
import static shop.wesellbuy.secondproject.domain.QMember.member;
import static shop.wesellbuy.secondproject.domain.QOrder.order;
import static shop.wesellbuy.secondproject.domain.QOrderItem.orderItem;

/**
 * OrderItemJpaRepositoryCustom 구현
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : OrderItemJpaRepository 구현 모음 + 최적화 사용(fetch)
 */
@RequiredArgsConstructor
@Slf4j
public class OrderItemJpaRepositoryImpl implements OrderItemJpaRepositoryCustom {

    private final JPAQueryFactory query;

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update : 2023.02.04
     * description : 모든 주문 찾기 + fetchjoin
     *               -> 회원(판매자)만 사용
     *               -> orderItemSearchCond에 회원아이디가 있어야 한다.
     *
     * comment : member가 두 개니까 구분위해 QMember 필요하지 않을까?
     *
     *           -> test 필요
     */
    @Override
    public Page<OrderItem> findAllInfo(OrderItemSearchCond cond, Pageable pageable) {
        // member가 두 개니까 구분위해 QMember 필요하지 않을까?
        QMember im = new QMember("im"); // item의 member

        // List 생성
        List<OrderItem> result = query
                .select(orderItem)
                .from(orderItem)
                .join(orderItem.item, item).fetchJoin()
                .join(item.member, im).fetchJoin()
                .join(orderItem.order, order).fetchJoin()
                .join(order.member, member).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .where(
//                        orderItem.item.member.num.eq(cond.getSellerNum()),
                        im.num.eq(cond.getSellerNum()),
                        orderIdEq(cond.getOrderId()),
                        orderOrderStatusEq(cond.getOrderStatus()),
                        orderDeliveryStatusEq(cond.getDeliveryStatus()),
                        orderItemCreateDateBetween(cond.getCreateDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderItem.num.desc())
                .fetch();

        // count 생성
        Long count = query
                .select(orderItem.count())
                .from(orderItem)
                .join(orderItem.item, item)
                .join(item.member, im)
                .join(orderItem.order, order)
//                .join(order.member, member)
//                .join(order.delivery, delivery)
                .where(
//                        orderItem.item.member.num.eq(cond.getSellerNum()),
                        im.num.eq(cond.getSellerNum()),
                        orderIdEq(cond.getOrderId()),
                        orderOrderStatusEq(cond.getOrderStatus()),
                        orderDeliveryStatusEq(cond.getDeliveryStatus()),
                        orderItemCreateDateBetween(cond.getCreateDate())
                )
                .fetchOne();

        // 페이지 생성
        return new PageImpl(result, pageable, count);
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문상품 정보 검색 조건 eq by createDate
     */
    private BooleanExpression orderItemCreateDateBetween(String createDate) {
        if(StringUtils.hasText(createDate)) {
            // String을 LocalDateTime으로 바꾸기
            LocalDateParser localDateParser = new LocalDateParser(createDate);
            return orderItem.createdDate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer : 이호진
     * update : 2023.03.11
     * description : 주문상품 정보 검색 조건 eq by 배달 상태
     *
     * comment : 상태가 다른 값이면 null 말고 처리방법 없을까?
     *           -> null이라 모든 값이 나온다.
     *
     * update : O(OVER) 배달취소 상태 추가
     */
    private BooleanExpression orderDeliveryStatusEq(String deliveryStatus) {
        if(StringUtils.hasText(deliveryStatus)) {
            // 배달 준비 상태
            if("R".equalsIgnoreCase(deliveryStatus)) {
//                return orderItem.order.delivery.status.eq(DeliveryStatus.R);
                return order.delivery.status.eq(DeliveryStatus.R);
                // 배달중 상태
            } else if("T".equalsIgnoreCase(deliveryStatus)) {
//                return orderItem.order.delivery.status.eq(DeliveryStatus.T);
                return order.delivery.status.eq(DeliveryStatus.T);
                // 배달완료 상태
            } else if("C".equalsIgnoreCase(deliveryStatus)) {
//                return orderItem.order.delivery.status.eq(DeliveryStatus.C);
                return order.delivery.status.eq(DeliveryStatus.C);
                // 배달취소 상태
            } else if("O".equalsIgnoreCase(deliveryStatus)) {
                return order.delivery.status.eq(DeliveryStatus.O);
            }
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문상품 정보 검색 조건 eq by 주문 상태
     *
     * comment : 상태가 다른 값이면 null 말고 처리방법 없을까?
     *           -> null이라 모든 값이 나온다.
     */
    private BooleanExpression orderOrderStatusEq(String orderStatus) {
        if(StringUtils.hasText(orderStatus)) {
            // 주문 상태
            if ("O".equalsIgnoreCase(orderStatus)) {
//                return orderItem.order.status.eq(OrderStatus.O);
                return order.status.eq(OrderStatus.O);
                // 주문 취소 상태
            } else if ("C".equalsIgnoreCase(orderStatus)) {
//                return orderItem.order.status.eq(OrderStatus.C);
                return order.status.eq(OrderStatus.C);
            }
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문상품 정보 검색 조건 eq by 주문 회원 아이디
     */
    private BooleanExpression orderIdEq(String orderId) {
//        return StringUtils.hasText(orderId) ? orderItem.order.member.id.eq(orderId) : null;
        return StringUtils.hasText(orderId) ? order.member.id.eq(orderId) : null;
    }



}
