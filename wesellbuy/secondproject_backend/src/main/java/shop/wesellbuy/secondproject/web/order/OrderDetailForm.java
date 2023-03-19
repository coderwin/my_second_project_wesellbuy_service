package shop.wesellbuy.secondproject.web.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.Order;
import shop.wesellbuy.secondproject.domain.member.Address;
import shop.wesellbuy.secondproject.domain.member.Phone;
import shop.wesellbuy.secondproject.domain.order.OrderStatus;
import shop.wesellbuy.secondproject.web.orderitem.OrderItemDetailForm;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 주문 상세보기 dto
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : 주문 상세정보를 담아둔다.
 */
@Getter
@AllArgsConstructor
public class OrderDetailForm {

    private Integer num; // 주문 번호
    private String orderStatus; // 주문 상태
    private String id; // 주문한 회원 id
    private Phone memberPhone; // 주문한 회원 연락처
    private Address address; // 주문한 회원 주소
    private String deliveryStatus; // 배달 상태
    private List<OrderItemDetailForm> orderItemDetailList; // 주문상품 정보
    private Integer totalPrice; // 전체 주문 가격

    // ** 생성 메서드 ** //
    public static OrderDetailForm create(Order order) {
        OrderDetailForm form = new OrderDetailForm(
                order.getNum(),
                order.getStatus().getOrderStatus(),
                order.getMember().getId(),
                order.getMember().getPhones(),
                order.getDelivery().getAddress(),
                order.getDelivery().getStatus().getDeliveryStatus(),
                order.getOrderItemList().stream()
                        .map(oi -> OrderItemDetailForm.create(oi))
                        .collect(toList()),
                order.getTotalPrice()
        );

        return form;
    }
}
