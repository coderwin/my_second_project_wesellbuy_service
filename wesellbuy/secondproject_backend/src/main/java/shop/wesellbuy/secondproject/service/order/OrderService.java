package shop.wesellbuy.secondproject.service.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.repository.order.OrderSearchCond;
import shop.wesellbuy.secondproject.repository.orderitem.OrderItemSearchCond;
import shop.wesellbuy.secondproject.web.order.OrderDetailForm;
import shop.wesellbuy.secondproject.web.order.OrderListForm;
import shop.wesellbuy.secondproject.web.order.OrderListFormForAdmin;
import shop.wesellbuy.secondproject.web.orderitem.OrderItemForm;
import shop.wesellbuy.secondproject.web.orderitem.OrderItemListForm;

import java.util.List;

/**
 * Order Service
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : Order Service 메소드 모음
 */
public interface OrderService {

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer : 이호진
     * update : 2024.02.05
     * description : 주문 저장 V1
     *               == 주문하기
     *
     * comment : controller에서 list를 받을 때
     *
     * update : 파라미터 paidMoney 추가
     *          createOrder() 파라미터 padiMoney 추가
     */
    int save(List<OrderItemForm> orderItemFormList, int memberNum, int paidMoney);

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문 저장 V2
     *               == 주문하기
     *
     *
     * comment : controller에서 array를 받을 때
     *           > paidMoney : 주문자가 지불한 가격
     */
    int save(int memberNum, int paidMoney, OrderItemForm... orderItemForms);

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문 취소
     *               -> order status : O(ORDER) -> C(CANCEL)
     */
    void cancel(int num);

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 주문 상세보기
     */
    OrderDetailForm watchDetail(int num);

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 회원이 주문한 주문 모두 불러오기 + by 회원(주문자)아이디
     */
    Page<OrderListForm> selectList(OrderSearchCond cond, Pageable pageable, String memberId);


//    -------------------------methods using for user(seller) start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 판매자가 주문의 배달상태를 배송중으로 변경
     *               배달 상태 변경
     *               -> delivery status : R(READY) -> T(TRANSIT)
     */
    void changeDeliveryStatusForSeller(int num);

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 판매자가 판매된 상품(목록) 확인하기
     */
    Page<OrderItemListForm> selectOrderItemList(OrderItemSearchCond cond, Pageable pageable, int memberNum);


//    -------------------------methods using for user(seller) end ----------------------------------

//    -------------------------methods using for deliver start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 배달원이 주문의 배달상태를 배송중으로 변경
     *               배달 상태 변경
     *               -> delivery status : T(TRANSIT) -> C(COMPLETE)
     *
     *               changeDeliveryStatusForSeller 코드는 같은데
     *               log가 다르다.(controller에서 처리)
     *               하나로 합쳐도 되지 않을까?
     */
    void changeDeliveryStatusForDeliver(int num);


//    -------------------------methods using for deliver end ----------------------------------

//    -------------------------methods using for admin, deliver start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 회원이 주문한 주문 모두 불러오기 + by 회원(주문자)아이디
     *               -> for admin
     *               -> for deliver
     */
    Page<OrderListFormForAdmin> selectListForAdmin(OrderSearchCond cond, Pageable pageable);

//    -------------------------methods using for admin, deliver end ----------------------------------

}
