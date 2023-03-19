package shop.wesellbuy.secondproject.web.orderitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.OrderItem;
import shop.wesellbuy.secondproject.domain.member.Address;
import shop.wesellbuy.secondproject.domain.member.Phone;

/**
 * 주문상품 목록 dto
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer : 이호진
 * update : 2023.03.12
 * description : 서버에서 보내온 판매된 상품의 주문상품 정보를 담아둔다.
 *               -> 판매자가 자신이 올린 상품의 판매된 정보를 확인한다.
 *
 * update : > orderNum(주문번호) 추가
 */
@Getter
@AllArgsConstructor
public class OrderItemListForm {

    private int orderItemNum; // 판매된 상품 번호
    private int quantity; // 주문 수량
    private String orderStatus; // 주문 상태
    private String deliveryStatus; // 배달 상태
    private String memberId; // 주문한 회원 아이디
    private String memberName; // 회원 이름
    private Phone memberPhone; // 주문한 회원 연락처
    private Address address; // 배송지
    private int itemNum; // 상품 번호
    private int totalCount; // 주문 총가격
    private int orderNum; // 주문 번호

    // ** 생성 메서드 ** //
    public static OrderItemListForm create(OrderItem orderItem) {
        OrderItemListForm form = new OrderItemListForm(
                orderItem.getNum(),
                orderItem.getQuantity(),
                orderItem.getOrder().getStatus().getOrderStatus(),
                orderItem.getOrder().getDelivery().getStatus().getDeliveryStatus(),
                orderItem.getOrder().getMember().getId(),
                orderItem.getOrder().getMember().getName(),
                orderItem.getOrder().getMember().getPhones(),
                orderItem.getOrder().getDelivery().getAddress(),
                orderItem.getItem().getNum(),
                orderItem.getTotalPrice(),
                orderItem.getOrder().getNum()
        );

        return form;
    }

}
