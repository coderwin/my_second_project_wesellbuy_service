package shop.wesellbuy.secondproject.web.orderitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.OrderItem;

/**
 * 주문상품 상세보기 dto
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : 서버가 보내온 주문상품 정보를 담아둔다.
 */
@Getter
@AllArgsConstructor
public class OrderItemDetailForm {

    private Integer num; // 주문상품 번호 // 필요 없을 것 같은데
    private Integer quantity; // 주문 수량
    private Integer itemPrice; // 주문상품 가격
    private Integer itemNum; // 상품 번호
    private String itemName; // 상품명
//    private String sellerId; // 판매자 아이디
                                // 상품 상세보기에서 확인하면 되지 않을까?

    // ** 생성 메서드 ** //
    public static OrderItemDetailForm create(OrderItem orderItem) {
        OrderItemDetailForm form = new OrderItemDetailForm(
                orderItem.getNum(),
                orderItem.getQuantity(),
                orderItem.getItemPrice(),
                orderItem.getItem().getNum(),
                orderItem.getItem().getName()
        );

        return form;
    }
}
