package shop.wesellbuy.secondproject.repository.orderitem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.domain.OrderItem;

/**
 * OrderItem Repository by using queryDsl
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : querydsl이용한 OrderItemJpaRepository 모음
 */
public interface OrderItemJpaRepositoryCustom {

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
    Page<OrderItem> findAllInfo(OrderItemSearchCond cond, Pageable pageable);
}
