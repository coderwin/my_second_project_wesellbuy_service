package shop.wesellbuy.secondproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.common.BaseDateColumnEntity;

/**
 * 주문 상품 정보
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 주문 상푸 정보를 정의한다.
 */
@Entity
@Getter
public class OrderItem extends BaseDateColumnEntity {

    @Id @GeneratedValue
    @Column(name = "orderItem_num")
    private Integer num; // 주문상품 번호
    private Integer quantity; // 주문 수량
    private Integer itemPrice; // 주문상품 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_num")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY) // orderItem create 시, cascade 설정해야하는지 생각해보기
    @JoinColumn(name = "item_num")
    private Item item;

    // ** setter ** //

    public void addQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void addItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    public void addItem(Item item) {
        this.item = item;
    }

    // ** 생성 메서드 ** //
    public static OrderItem createOrderItem(int quantity, int itemPrice, Item item) {
        OrderItem orderItem = new OrderItem();

        orderItem.addQuantity(quantity);
        orderItem.addItemPrice(itemPrice);
        orderItem.addItem(item);

        // item의 제고수량에서 주문수량을 빼준다.
        item.removeStock(quantity);

        return orderItem;
    }

    // ** 비즈니스 메서드 ** //

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문상품 총 가격 출력
     */
    public int getTotalPrice() {
        int totalPrice = quantity * itemPrice;

        return totalPrice;
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 주문 취소 요청이 왔을 때
     *               -> item의 제고량에 orderItem의 주문수량만큼 늘리기
     */
    public void cancel() {
        // item의 제고량에 orderItem의 주문수량만큼 늘리기
        item.restoreStock(quantity);
    }
}
