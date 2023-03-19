package shop.wesellbuy.secondproject.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.domain.Order;

import java.util.Optional;

/**
 * Order Repository by using queryDsl
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : querydsl이용한 OrdersJpaRepository 모음
 */
public interface OrderJpaRepositoryCustom {

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update : 2023.02.04
     * description :
     *
     * update : 코드 변경
     *          -> member.id.eq(); 생김
     */
    Page<Order> findAllInfo(OrderSearchCond orderSearchCond, Pageable pageable);

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 주문 상세보기 + fetchjoin by num(id)
     */
    Optional<Order> findDetailInfoById(int num);

//    -------------------------methods using for admin start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update : 2023.02.04
     * description : 모든 주문 찾기 + fetchjoin
     *               -> 관리자만 사용
     *
     * update : method 이름 변경
     */
    Page<Order> findAllInfoForAdmin(OrderSearchCond orderSearchCond, Pageable pageable);

//    -------------------------methods using for admin end----------------------------------


}
