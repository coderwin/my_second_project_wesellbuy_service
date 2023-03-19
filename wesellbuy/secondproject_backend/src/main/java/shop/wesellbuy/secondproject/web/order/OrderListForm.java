package shop.wesellbuy.secondproject.web.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.Order;
import shop.wesellbuy.secondproject.domain.delivery.DeliveryStatus;
import shop.wesellbuy.secondproject.domain.order.OrderStatus;

import java.time.LocalDateTime;

/**
 * 주문 목록 dto
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : 서버에서 보내온 주문 목록 정보를 담아둔다.
 *               -> for 회원(주문자)
 */
@Getter
@AllArgsConstructor
public class OrderListForm {

    private int num; // 주문번호
    private String orderStatus; // 주문 상태
    private String deliveryStatus; // 배송 상태
    private LocalDateTime createDate; // 주문 날짜

    // ** 생성 메서드 ** //
    public static OrderListForm create(Order order) {
        OrderListForm form = new OrderListForm(
                order.getNum(),
                order.getStatus().getOrderStatus(),
                order.getDelivery().getStatus().getDeliveryStatus(),
                order.getCreatedDate()
        );

        return form;
    }

    // ** 비즈니스 메서드 ** //
    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문 상태를 String으로 바꾸기
     */
    public String changeOrderStatusToString(OrderStatus status) {
//        if(status.equals(OrderStatus.O)) {
//            return "ORDER";
//        } else {
//            return "CANCEL";
//        }
        return status.getOrderStatus();
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 배송 상태를 String으로 바꾸기
     */
    public String changeDeliveryStatusToString(DeliveryStatus status) {
//        if(status.equals(DeliveryStatus.R)) {
//            return "ORDER";
//        } else if(status.equals(DeliveryStatus.T)) {
//            return "TRANSIT";
//        } else {
//            return "COMPLETE";
//        }
        return status.getDeliveryStatus();
    }
}
