package shop.wesellbuy.secondproject.repository.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import shop.wesellbuy.secondproject.domain.Order;
import shop.wesellbuy.secondproject.domain.QOrder;
import shop.wesellbuy.secondproject.domain.delivery.DeliveryStatus;
import shop.wesellbuy.secondproject.domain.order.OrderStatus;
import shop.wesellbuy.secondproject.util.LocalDateParser;

import java.util.List;
import java.util.Optional;

import static shop.wesellbuy.secondproject.domain.QDelivery.delivery;
import static shop.wesellbuy.secondproject.domain.QMember.member;
import static shop.wesellbuy.secondproject.domain.QOrder.order;

/**
 * OrderJpaRepositoryCustom 구현
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : OrderJpaRepository 구현 모음 + 최적화 사용(fetch)
 */
@RequiredArgsConstructor
@Slf4j
public class OrderJpaRepositoryImpl implements OrderJpaRepositoryCustom{

    private final JPAQueryFactory query;

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update : 2023.02.04
     * description : 모든 주문 찾기 + fetchjoin
     *               -> 회원만 사용
     *               -> orderSearchCond에 회원아이디가 있어야 한다.
     */
    @Override
    public Page<Order> findAllInfo(OrderSearchCond orderSearchCond, Pageable pageable) {

        List<Order> result = query
                .selectFrom(order)
                .join(order.member, member).fetchJoin() // 주문한 회원 id
                .join(order.delivery, delivery).fetchJoin()
                .where(
                        member.id.eq(orderSearchCond.getMemberId()),
                        orderOrderStatusEq(orderSearchCond.getOrderStatus()),
                        orderDeliveryStatusEq(orderSearchCond.getDeliveryStatus()),
                        orderCreateDateBetween(orderSearchCond.getCreateDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.num.desc())
                .fetch();

        Long totalCount = query
                .select(order.count())
                .from(order)
                .where(
                        order.member.id.eq(orderSearchCond.getMemberId()),
                        orderOrderStatusEq(orderSearchCond.getOrderStatus()),
                        orderDeliveryStatusEq(orderSearchCond.getDeliveryStatus()),
                        orderCreateDateBetween(orderSearchCond.getCreateDate())
                )
                .fetchOne();

        return new PageImpl(result, pageable, totalCount);
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 주문 정보 검색 조건 eq by createDate
     */
    private BooleanExpression orderCreateDateBetween(String createDate) {
        if(StringUtils.hasText(createDate)) {
            // String을 LocalDateTime으로 바꾸기
            LocalDateParser localDateParser = new LocalDateParser(createDate);
            return order.createdDate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update : 2023.03.11
     * description : 주문 정보 검색 조건 eq by 배달 상태
     *
     * comment : 상태가 다른 값이면 null 말고 처리방법 없을까?
     *           -> null이라 모든 값이 나온다.
     *
     * update : > O(OVER) 배송취소 상태 추가
     */
    private BooleanExpression orderDeliveryStatusEq(String deliveryStatus) {
        if(StringUtils.hasText(deliveryStatus)) {
            // 배달 준비 상태
            if("R".equalsIgnoreCase(deliveryStatus)) {
                return order.delivery.status.eq(DeliveryStatus.R);
            // 배달중 상태
            } else if("T".equalsIgnoreCase(deliveryStatus)) {
                return order.delivery.status.eq(DeliveryStatus.T);
            // 배달완료 상태
            } else if("C".equalsIgnoreCase(deliveryStatus)) {
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
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 주문 정보 검색 조건 eq by 주문 상태
     *
     * comment : 상태가 다른 값이면 null 말고 처리방법 없을까?
     *           -> null이라 모든 값이 나온다.
     */
    private BooleanExpression orderOrderStatusEq(String orderStatus) {
        if(StringUtils.hasText(orderStatus)) {
            // 주문 상태
            if ("O".equalsIgnoreCase(orderStatus)) {
                return order.status.eq(OrderStatus.O);
            // 주문 취소 상태
            } else if ("C".equalsIgnoreCase(orderStatus)) {
                return order.status.eq(OrderStatus.C);
            }
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 주문 정보 검색 조건 eq by 주문 회원 아이디
     */
    private BooleanExpression orderIdEq(String memberId) {
        return StringUtils.hasText(memberId) ? order.member.id.eq(memberId) : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 주문 상세보기 + fetchjoin by order_num(id)
     *
     * comment : 왜 static으로 사용 못하지? order를
     */
    @Override
    public Optional<Order> findDetailInfoById(int num) {
        Order order = query
                .selectFrom(QOrder.order)
                .join(QOrder.order.member, member).fetchJoin()
                .join(QOrder.order.delivery, delivery).fetchJoin()
                .where(QOrder.order.num.eq(num))
                .fetchOne();

        return Optional.ofNullable(order);
    }

//    -------------------------methods using for admin start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update :
     * description : 모든 주문 찾기 + fetchjoin
     *               -> 관리자만 사용
     */
    @Override
    public Page<Order> findAllInfoForAdmin(OrderSearchCond orderSearchCond, Pageable pageable) {

        List<Order> result = query
                .selectFrom(order)
                .join(order.member, member).fetchJoin() // 주문한 회원 id
                .join(order.delivery, delivery).fetchJoin()
                .where(
                        orderIdEq(orderSearchCond.getMemberId()),
                        orderOrderStatusEq(orderSearchCond.getOrderStatus()),
                        orderDeliveryStatusEq(orderSearchCond.getDeliveryStatus()),
                        orderCreateDateBetween(orderSearchCond.getCreateDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.num.desc())
                .fetch();

        Long totalCount = query
                .select(order.count())
                .from(order)
                .where(
                        orderIdEq(orderSearchCond.getMemberId()),
                        orderOrderStatusEq(orderSearchCond.getOrderStatus()),
                        orderDeliveryStatusEq(orderSearchCond.getDeliveryStatus()),
                        orderCreateDateBetween(orderSearchCond.getCreateDate())
                )
                .fetchOne();

        return new PageImpl(result, pageable, totalCount);
    }

//    -------------------------methods using for admin end----------------------------------


}
