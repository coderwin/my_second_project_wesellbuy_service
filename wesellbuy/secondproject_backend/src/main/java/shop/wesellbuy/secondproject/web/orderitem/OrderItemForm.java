package shop.wesellbuy.secondproject.web.orderitem;

import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * 주문상품 dto
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : 클라이언트가 보내온 주문상품 정보를 담아둔다.
 *
 * update : 상품 가격 제거
 *          -> view에서 수정할 가능성이 있음
 *          > @NoArgsConstructor 추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class OrderItemForm {

    @Positive(message = "주문 수량이 맞지 않습니다")
    private int quantity; // 주문 수량
//    @Positive(message = "상품 가격이 맞지 않습니다")
//    private int price; // 상품 가격
    @Positive(message = "상품 번호가 맞지 않습니다")
    private int itemNum; // 상품 번호

}
