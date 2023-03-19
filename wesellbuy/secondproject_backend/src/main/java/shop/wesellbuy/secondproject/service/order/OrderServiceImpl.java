package shop.wesellbuy.secondproject.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.*;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.order.OrderJpaRepository;
import shop.wesellbuy.secondproject.repository.order.OrderSearchCond;
import shop.wesellbuy.secondproject.repository.orderitem.OrderItemJpaRepository;
import shop.wesellbuy.secondproject.repository.orderitem.OrderItemSearchCond;
import shop.wesellbuy.secondproject.web.order.OrderDetailForm;
import shop.wesellbuy.secondproject.web.order.OrderListForm;
import shop.wesellbuy.secondproject.web.order.OrderListFormForAdmin;
import shop.wesellbuy.secondproject.web.orderitem.OrderItemForm;
import shop.wesellbuy.secondproject.web.orderitem.OrderItemListForm;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Order Service 구현 클래스
 * writer : 이호진
 * init : 2023.02.03
 * updated by writer :
 * update :
 * description : Order Service 구현 메소드 모음
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer : 이호진
     * update : 2023.02.05
     * description : 주문 저장 V1
     *               == 주문하기
     *
     * comment : controller에서 list를 받을 때
     *
     * update : 파라미터 paidMoney 추가
     *          createOrder() 파라미터 padiMoney 추가
     */
    @Override
    @Transactional
    public int save(List<OrderItemForm> orderItemFormList, int memberNum, int paidMoney) {
        // 상품 생성 and 주문상품 만들기(재고 부족 예외 발생할 수 있음)
        List<OrderItem> orderItemList = orderItemFormList.stream()
                .map(oi -> makeOrderItem(oi))
                .collect(toList());
        // 회원 불러오기
        Member member = memberJpaRepository.findById(memberNum).orElseThrow();
        // 배달 생성
        Delivery delivery = Delivery.createDelivery(member);
        // 주문 생성(주문하기 == save)
        // list를 array로 변경하기
//        OrderItem[] orderItemArrys = orderItemList.stream().toArray(OrderItem[]::new);
        OrderItem[] OrderItemArr = orderItemList.toArray(new OrderItem[orderItemList.size()]);
        Order order = Order.createOrder(member, delivery, paidMoney, OrderItemArr);
        // 주문하기
        orderJpaRepository.save(order);

        return order.getNum();
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문상품(OrderItem) 만들기
     *
     * update : view에서 가격을 받는 것이 아니라
     *          상품에서 받는다.
     */
    private OrderItem makeOrderItem(OrderItemForm oi) {
        // 상품 불러오기
        Item item = itemJpaRepository.findById(oi.getItemNum()).orElseThrow();
        // 주문상품 만들기
        OrderItem orderItem = OrderItem.createOrderItem(oi.getQuantity(), item.getPrice(), item);

        return orderItem;
    }

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
     *           > controller에서 사용 안함(2/14)
     *
     */
    @Override
    @Transactional
    public int save(int memberNum, int paidMoney, OrderItemForm... orderItemForms) {
        // 상품 생성 and 주문상품 만들기(재고 부족 예외 발생할 수 있음)
        // OrderItem[] 만들기 v1
        OrderItem[] orderItems = Arrays.stream(orderItemForms)
                .map(oi -> makeOrderItem(oi))
                .collect(toList())
                .toArray(new OrderItem[orderItemForms.length]);

//        // OrderItem[] 만들기 v2
//        OrderItem[] orderItems = Arrays.stream(orderItemForms)
//                .map(oi -> makeOrderItem(oi))
//                .toArray(OrderItem[]::new);

        // 회원 불러오기
        Member member = memberJpaRepository.findById(memberNum).orElseThrow();
        // 배달 생성
        Delivery delivery = Delivery.createDelivery(member);
        // 주문 생성(주문하기 == save)
        Order order = Order.createOrder(member, delivery, paidMoney, orderItems);
        // 주문하기
        orderJpaRepository.save(order);

        return order.getNum();
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문 취소
     *               -> order status : O(ORDER) -> C(CANCEL)
     *
     * comment : repository에서 fetch join 사용할지 생각해보기
     */
    @Override
    @Transactional
    public void cancel(int num) {
        // order 불러오기
        Order order = orderJpaRepository.findById(num).orElseThrow();
        // 주문 취소하기
        order.cancel();

        log.info( "주문번호 : " + num + " 주문취소 완료");
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 주문 상세보기
     */
    @Override
    public OrderDetailForm watchDetail(int num) {
        // 주문 상세보기 불러오기
        Order order = orderJpaRepository.findDetailInfoById(num).orElseThrow();
        // OrderDetailForm으로 변경하기
        OrderDetailForm orderDetailForm = OrderDetailForm.create(order);

        return orderDetailForm;
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 회원이 주문한 주문 모두 불러오기 + by 회원(주문자)아이디
     */
    @Override
    public Page<OrderListForm> selectList(OrderSearchCond cond, Pageable pageable, String memberId) {
        // 조건에 회원 아이디 담기
        cond.addMemberId(memberId);
        // Page 불러오기
        Page<Order> orderList = orderJpaRepository.findAllInfo(cond, pageable);
        // OrderListForm으로 바꾸기
        Page<OrderListForm> result = orderList.map(o -> OrderListForm.create(o));

        return result;
    }



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
    @Override
    @Transactional
    public void changeDeliveryStatusForSeller(int num) {
        // order 불러오기
        Order order = orderJpaRepository.findById(num).orElseThrow();
        // delivery status를 변경하기
        order.changeDeliveryStatus();
        log.info( "주문번호 : '" + num + "' 배송중");
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 판매자가 판매된 상품(목록) 확인하기
     */
    @Override
    public Page<OrderItemListForm> selectOrderItemList(OrderItemSearchCond cond, Pageable pageable, int memberNum) {
        // OrderItemSearchCond에 memberNum 담기
        cond.addSellerNum(memberNum);
        // 판매자의 판매 상품 목록 불러오기
        Page<OrderItem> orderItemPage = orderItemJpaRepository.findAllInfo(cond, pageable);
        // OrderItemListForm으로 바꾸기
        Page<OrderItemListForm> result = orderItemPage.map(oi -> OrderItemListForm.create(oi));

        return result;
    }

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
    @Override
    @Transactional
    public void changeDeliveryStatusForDeliver(int num) {
        // order 불러오기
        Order order = orderJpaRepository.findById(num).orElseThrow();
        // order delivery를 변경하기
        order.changeDeliveryStatus();
        log.info( "주문번호 : '" + num + "' 배송완료");
    }

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
    @Override
    public Page<OrderListFormForAdmin> selectListForAdmin(OrderSearchCond cond, Pageable pageable) {
        // Page 불러오기
        Page<Order> orderList = orderJpaRepository.findAllInfoForAdmin(cond, pageable);
        // OrderListFormForAdmin으로 바꾸기
        Page<OrderListFormForAdmin> result = orderList.map(o -> OrderListFormForAdmin.create(o));

        return result;
    }
//    -------------------------methods using for admin, deliver end ----------------------------------

}
