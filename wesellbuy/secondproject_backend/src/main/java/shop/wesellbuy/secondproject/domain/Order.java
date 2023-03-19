package shop.wesellbuy.secondproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.common.BaseDateColumnEntity;
import shop.wesellbuy.secondproject.domain.delivery.DeliveryStatus;
import shop.wesellbuy.secondproject.domain.order.OrderStatus;
import shop.wesellbuy.secondproject.exception.order.NotChangeDeliveryStatusException;
import shop.wesellbuy.secondproject.exception.order.NotCorrectPaidMoneyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 회원주문 정보
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 회원 주문정보를 정의한다.
 */
@Entity
@Table(name = "orders")
@Getter
public class Order extends BaseDateColumnEntity {

    @Id @GeneratedValue
    @Column(name = "order_num")
    private Integer num; // 주문 번호
    @Column(length = 5)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status; // 주문 상태[주문 상태(ORDER)인지, 주문 취소 상태(CANCEL)]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num")
    private Member member;// 주문한 회원 num

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "delivery_num")
    private Delivery delivery; // 배달 정보

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItemList = new ArrayList<>(); // 주문 아이템 모음

    // ** setter ** //
    public void addOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    // ** 연관관계 메서드 ** //
    // Member
    public void addMember(Member member) {
        member.getOrderList().add(this);
        this.member = member;
    }

    // Delivery
    public void addDelivery(Delivery delivery) {
        delivery.addOrder(this);
        this.delivery = delivery;
    }

    // OrderItemList
    public void addOrderItems(OrderItem orderItem) {
        orderItem.addOrder(this);
        this.orderItemList.add(orderItem);
    }

    // ** 생성 메서드 ** //
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();

        order.addMember(member);
        order.addDelivery(delivery);
        // orderItem에 order 주입
        Arrays.stream(orderItems)
                .forEach(oi -> order.addOrderItems(oi));
        order.addOrderStatus(OrderStatus.O);

        return order;
    }

    /**
     * comment : Order 생성
     *           + 총 주문상품 가격과 사용자의 지불 금액 확인
     *
     *           > test 필요
     */
    public static Order createOrder(Member member, Delivery delivery, int paidMoney, OrderItem... orderItems) {
        Order order = new Order();

        order.addMember(member);
        order.addDelivery(delivery);
        // orderItem에 order 주입
        Arrays.stream(orderItems)
                .forEach(oi -> order.addOrderItems(oi));
        order.addOrderStatus(OrderStatus.O);

        // 총 주문상품 가격과 사용자의 지불 금액 확인
        checkPaidMoney(paidMoney, order);

        return order;
    }

    // ** 비즈니스 메서드 ** //

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 총 주문상품 가격과 사용자의 지불 금액 확인
     */
    private static void checkPaidMoney(int paidMoney, Order order) {
        // 총 주문상품 가격과 사용자의 지불 금액 확인
        if(paidMoney < order.getTotalPrice()) {
            String errMsg = "지불된 금액이 부족합니다.";
            throw new NotCorrectPaidMoneyException(errMsg);
        } else if(paidMoney > order.getTotalPrice()) {
            String errMsg = "지불된 금액이 주문 금액을 초과하였습니다.";
            throw new NotCorrectPaidMoneyException(errMsg);
        }
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : order status : R(READY) -> C(CANCEL)
     */
    public void changeStatus() {
        //배달 status가 배송중, 배송완료면 불가하다.
        // -> 예외 일어남
        delivery.cancel();
        // R -> C 로 변경
        this.status = OrderStatus.C;
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문 배송 상태를 확인한다.
     */
    public void changeDeliveryStatus() {
        // 주문취소 일 때
        if(status.equals(OrderStatus.C)) {
            String errMsg = "주문이 취소되어 배송상태 변경 불가합니다.";
            throw new NotChangeDeliveryStatusException(errMsg);
        }
        // 주문취소 안 되었을 때
        if(delivery.getStatus().equals(DeliveryStatus.R)) {
            delivery.changeStatusRT();
        } else if(delivery.getStatus().equals(DeliveryStatus.T)) {
            delivery.changeStatusTC();
        }
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 총 주문 가격
     */
    public int getTotalPrice() {
        // 주문상품 각각의 가격 가져오기
        List<Integer> eachItemTotalPrices = orderItemList.stream().map(oi -> oi.getTotalPrice())
                .collect(toList());
        // 전체 가격 만들기
        int totalPrice = 0;
        for(int price : eachItemTotalPrices) {
            totalPrice += price;
        }

        return totalPrice;
    }

    /**
     * writer : 이호진
     * init : 2023.02.05
     * updated by writer :
     * update :
     * description : 주문 취소 요청이 왔을 때
     *               -> orderStatus 변경(O -> C)
     *               -> -> item의 제고량에 orderItem의 주문수량만큼 늘리기
     */
    public void cancel() {

        // 주문 상태 변경
        changeStatus();
        // orderItem의 제고량을 뺀 만큼 채우기
        orderItemList.stream().forEach(
                oi -> oi.cancel()
        );
    }
}
