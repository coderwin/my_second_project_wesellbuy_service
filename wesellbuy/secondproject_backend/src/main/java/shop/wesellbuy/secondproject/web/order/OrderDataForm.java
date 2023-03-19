package shop.wesellbuy.secondproject.web.order;

import jakarta.validation.constraints.Positive;
import lombok.*;
import shop.wesellbuy.secondproject.web.orderitem.OrderItemForm;

import java.util.List;

/**
 * 주문 데이터 모음 dto
 * writer : 이호진
 * init : 2023.02.14
 * updated by writer :
 * update :
 * description : 클라이언트가 보낸 데이터를 담아두는 dto
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class OrderDataForm {

    private List<OrderItemForm> data; // 주문 상품 모음
    @Positive(message = "지불금액이 맞지 않습니다")
    private int paidMoney;// 지불 금액
}
