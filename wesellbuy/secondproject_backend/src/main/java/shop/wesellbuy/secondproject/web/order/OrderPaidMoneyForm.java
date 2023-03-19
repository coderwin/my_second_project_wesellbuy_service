package shop.wesellbuy.secondproject.web.order;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문상품 지불금액 dto
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : 클라이언트가 보내온 주문상품 지불금액 정보를 담아둔다.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderPaidMoneyForm {

    @Positive(message = "지불금액이 맞지 않습니다")
    private int paidMoney; // 지불 금액
}
