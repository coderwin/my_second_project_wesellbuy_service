package shop.wesellbuy.secondproject.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.*;
import shop.wesellbuy.secondproject.domain.delivery.DeliveryStatus;
import shop.wesellbuy.secondproject.domain.item.Book;
import shop.wesellbuy.secondproject.domain.item.Furniture;
import shop.wesellbuy.secondproject.domain.item.HomeAppliances;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;
import shop.wesellbuy.secondproject.domain.order.OrderStatus;
import shop.wesellbuy.secondproject.exception.delivery.NotCancelOrderException;
import shop.wesellbuy.secondproject.exception.item.OverflowQuantityException;
import shop.wesellbuy.secondproject.exception.order.NotChangeDeliveryStatusException;
import shop.wesellbuy.secondproject.exception.order.NotCorrectPaidMoneyException;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.order.OrderJpaRepository;
import shop.wesellbuy.secondproject.repository.order.OrderSearchCond;
import shop.wesellbuy.secondproject.repository.orderitem.OrderItemJpaRepository;
import shop.wesellbuy.secondproject.repository.orderitem.OrderItemSearchCond;
import shop.wesellbuy.secondproject.service.order.OrderService;
import shop.wesellbuy.secondproject.web.item.BookForm;
import shop.wesellbuy.secondproject.web.item.FurnitureForm;
import shop.wesellbuy.secondproject.web.item.HomeAppliancesForm;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.order.OrderDetailForm;
import shop.wesellbuy.secondproject.web.order.OrderListForm;
import shop.wesellbuy.secondproject.web.order.OrderListFormForAdmin;
import shop.wesellbuy.secondproject.web.orderitem.OrderItemForm;
import shop.wesellbuy.secondproject.web.orderitem.OrderItemListForm;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Slf4j
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderJpaRepository orderJpaRepository;
    @Autowired
    OrderItemJpaRepository orderItemJpaRepository;
    @Autowired
    ItemJpaRepository itemJpaRepository;
    @Autowired
    MemberJpaRepository memberJpaRepository;

    private Member member; // test 사용 member
    private Member member2; // test 사용 member
    private Member member3; // test 사용 member

    private Delivery delivery; // test 사용 delivery
    private Delivery delivery2; // test 사용 delivery
    private Delivery delivery3; // test 사용 delivery

    Item item1; // test 사용 상품
    Item item2; // test 사용 상품
    Item item3; // test 사용 상품

    int number = 100; //
    int oCount; // member 주문수
    int oCount2; // member2 주문수
    int oCount3; // member3 주문수

    int iaCount = 0; // member(판매자) 상품주문수
    int iaCount2 = 0; // member(판매자) 상품주문수

    @BeforeEach
    public void init() {

        // 회원 생성
        MemberForm memberForm1 = new MemberForm("a", "a","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member = Member.createMember(memberForm1);
        MemberForm memberForm2 = new MemberForm("a", "b","123", "a@a", "01012341234", "0511231234", "korea12", "b", "h2", "h2", "123", null);
        Member member2 = Member.createMember(memberForm2);
        MemberForm memberForm3 = new MemberForm("b", "cd","123", "a@a", "01012341234", "0511231234", "korea13", "b", "h3", "h3", "123", null);
        Member member3 = Member.createMember(memberForm3);

        memberJpaRepository.save(member);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);

        this.member = member; // x 물품 판다
        this.member2 = member2; // y 물품 판다
        this.member3 = member3; // z 물품 판다
        // 상품 생성
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        BookForm bookFormTest1 = new BookForm(10000, 1000, "x", "x is...", itemPictureList, "ed", "ok");
        this.item1 = Book.createBook(bookFormTest1, member);
        FurnitureForm furnitureFormTest1 = new FurnitureForm(20000, 2000, "y", "y is...", itemPictureList, "ed");
        this.item2 = Furniture.createFurniture(furnitureFormTest1, member2);
        HomeAppliancesForm homeAppliancesFormTest1 = new HomeAppliancesForm(30000, 3000, "z", "z is...", itemPictureList, "ed2");
        this.item3 = HomeAppliances.createHomeAppliances(homeAppliancesFormTest1, member3);

        itemJpaRepository.save(item1);
        itemJpaRepository.save(item2);
        itemJpaRepository.save(item3);

        // 주문하기(주문 생성)
        int number = 100; //
        oCount = 0; // member 주문수
        oCount2 = 0; // member2 주문수
        oCount3 = 0; // member3 주문수

        iaCount = 0; // member(판매자) 상품주문수
        iaCount2 = 0; // member(판매자) 상품주문수

        for(int i = 0; i < number; i++) {
            if(i % 3 == 0) {

                OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1); // 1000 * 3 3000
                OrderItem orderItem4 = OrderItem.createOrderItem(3, item1.getPrice(), item1); // 1000 * 3 3000

                // item2 주문
                OrderItem orderItem2 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000
                OrderItem orderItem5 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000
                OrderItem orderItem7 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000

                // item3 주문
                OrderItem orderItem3 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000
                OrderItem orderItem6 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000



                Delivery delivery11 = Delivery.createDelivery(member);
                Delivery delivery12 = Delivery.createDelivery(member);
                Delivery delivery13 = Delivery.createDelivery(member);

                Order order1 = Order.createOrder(member, delivery11, 7000, orderItem1, orderItem2);  //          // 7000
                Order order5 = Order.createOrder(member, delivery12, 19000, orderItem4, orderItem5, orderItem3);  // 19000
                Order order7 = Order.createOrder(member, delivery13, 16000, orderItem7, orderItem6);              // 16000

                orderJpaRepository.save(order1);
                orderJpaRepository.save(order5);
                orderJpaRepository.save(order7);

                oCount += 3;
                iaCount += 2;

                // 배달 상태 -> 배달중
                order1.getDelivery().changeStatusRT();
                // 주문 취소
                order5.cancel();

            } else if(i % 3 == 1) {
                // item1 주문
                OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1); // 1000 * 3 3000
                OrderItem orderItem4 = OrderItem.createOrderItem(3, item1.getPrice(), item1); // 1000 * 3 3000
                // item2 주문
                OrderItem orderItem2 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000
                // item3 주문
                OrderItem orderItem3 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000
                OrderItem orderItem5 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000

                Delivery delivery21 = Delivery.createDelivery(member2);
                Delivery delivery22 = Delivery.createDelivery(member2);

                Order order2 = Order.createOrder(member2, delivery21, 15000, orderItem1, orderItem3);            // 15000
                Order order4 = Order.createOrder(member2, delivery22, 19000, orderItem4, orderItem2, orderItem5);// 19000

                orderJpaRepository.save(order2);
                orderJpaRepository.save(order4);

                oCount2 += 2;
                iaCount2 += 2;

                // 주문 취소
                order2.changeStatus();

            } else {

                OrderItem orderItem2 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000
                OrderItem orderItem3 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000

                Delivery delivery3 = Delivery.createDelivery(member3);

                Order order3 = Order.createOrder(member3, delivery3, 16000, orderItem2, orderItem3); // 16000

                orderJpaRepository.save(order3);

                oCount3 += 1;
            }
        }

    }

    /**
     * 주문 저장 확인 v1
     */
    @Test
//    @Rollback(value = false)
    public void 주문_저장_v1_확인() {
        // given
        // OrderItemFrom 생성 * 3
        OrderItemForm orderItemForm1 = new OrderItemForm(2, item1.getNum());
        OrderItemForm orderItemForm2 = new OrderItemForm(6, item1.getNum());
        OrderItemForm orderItemForm3 = new OrderItemForm(10, item2.getNum());
        // 지불금액 계산
        int paidMoneyO = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity();
        // 지불금액보다 많을 때
        int paidMoneyMoreThan = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity() + 1;
        // 지불금액보다 작을 때
        int paidMoneyLessThan = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity() - 1;

        List<OrderItemForm> orderItemFormList = new ArrayList<>();

        orderItemFormList.add(orderItemForm1);
        orderItemFormList.add(orderItemForm2);
        orderItemFormList.add(orderItemForm3);
        // when // then
        // 주문하기(주문 저장하기)
        // 지불금액 맞을 때
        int orderNum = orderService.save(orderItemFormList, member.getNum(), paidMoneyO);

        // Order 불러오기
        Order findOrder = orderJpaRepository.findById(orderNum).orElseThrow();

        assertThat(findOrder.getMember().getId()).isEqualTo(member.getId());
        assertThat(findOrder.getDelivery().getAddress().getCity()).isEqualTo(member.getAddress().getCity());
        assertThat(findOrder.getDelivery().getAddress().getCity()).isEqualTo(member.getAddress().getCity());
        assertThat(findOrder.getOrderItemList().size()).isEqualTo(orderItemFormList.size());

        // 지불금액보다 많을 때
        assertThrows(NotCorrectPaidMoneyException.class,
                () -> orderService.save(orderItemFormList, member.getNum(), paidMoneyMoreThan),
                () -> "1sdfas 지불된 금액이 주문 금액을 초과하였습니다."
                );

        // 지불금액보다 작을 때
        assertThrows(NotCorrectPaidMoneyException.class,
                () -> orderService.save(orderItemFormList, member.getNum(), paidMoneyLessThan),
                () -> "지불된 금액이 부족합니다."
        );

    }

    /**
     * 주문 수량 초과 에러 확인 v1
     */
    @Test
//    @Rollback(value = false)
    public void 주문수량_초과_에러_확인() {
        // given
        // OrderItemFrom 생성 * 3
        OrderItemForm orderItemForm1 = new OrderItemForm(10000, item1.getNum());
        OrderItemForm orderItemForm2 = new OrderItemForm(1, item1.getNum());
        OrderItemForm orderItemForm3 = new OrderItemForm(20000, item2.getNum());
        // 지불금액 계산
        int paidMoneyO = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity();
        // 지불금액보다 많을 때
        int paidMoneyMoreThan = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity() + 1;
        // 지불금액보다 작을 때
        int paidMoneyLessThan = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity() - 1;

        List<OrderItemForm> orderItemFormList = new ArrayList<>();

        orderItemFormList.add(orderItemForm1);
        orderItemFormList.add(orderItemForm2);
        orderItemFormList.add(orderItemForm3);
        // when // then
        // 주문하기(주문 저장하기)
        // 지불금액 맞을 때
//        orderService.save(orderItemFormList, member.getNum(), paidMoneyO);
        assertThrows(OverflowQuantityException.class, () -> orderService.save(orderItemFormList, member.getNum(), paidMoneyO));
//        fail(item1.getName() + "상품이 주문수량 초과 발생", NotCorrectPaidMoneyException.class);

        /// item2 제고가 0개 일 때

        // item2 제고 0으로 만들기
//        OrderItemForm orderItemForm5 = new OrderItemForm(20000, item2.getNum());
        List<OrderItemForm> orderItemFormList3 = new ArrayList<>();
//        orderItemFormList3.add(orderItemForm5);
        orderItemFormList3.add(orderItemForm3);
        orderService.save(orderItemFormList3, member.getNum(), item2.getPrice() * orderItemForm3.getQuantity());

        // -> 0개를 주문하면
        OrderItemForm orderItemForm4 = new OrderItemForm(0, item2.getNum());

        List<OrderItemForm> orderItemFormList2 = new ArrayList<>();
        orderItemFormList2.add(orderItemForm4);

        assertThrows(OverflowQuantityException.class, () -> orderService.save(orderItemFormList2, member.getNum(), paidMoneyO));
//        assertThrows(NotCorrectPaidMoneyException.class, () -> orderService.save(orderItemFormList2, member.getNum(), paidMoneyO));// 에러 발생
    }

    /**
     * 주문 저장 확인 v2
     */
    @Test
//    @Rollback(value = false)
    public void 주문_저장_v2_확인() {
        // given
        // OrderItemFrom 생성 * 3
        OrderItemForm orderItemForm1 = new OrderItemForm(2, item1.getNum());
        OrderItemForm orderItemForm2 = new OrderItemForm(6, item1.getNum());
        OrderItemForm orderItemForm3 = new OrderItemForm(10, item2.getNum());
        // 지불금액 계산
        int paidMoneyO = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity();
        // 지불금액보다 많을 때
        int paidMoneyMoreThan = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity() + 1;
        // 지불금액보다 작을 때
        int paidMoneyLessThan = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity() - 1;

        // when // then
        // 주문하기(주문 저장하기)
        // 지불금액 맞을 때
        int orderNum = orderService.save(member.getNum(), paidMoneyO, orderItemForm1, orderItemForm2, orderItemForm3);

        // Order 불러오기
        Order findOrder = orderJpaRepository.findById(orderNum).orElseThrow();

        assertThat(findOrder.getMember().getId()).isEqualTo(member.getId());
        assertThat(findOrder.getDelivery().getAddress().getCity()).isEqualTo(member.getAddress().getCity());
        assertThat(findOrder.getDelivery().getAddress().getCity()).isEqualTo(member.getAddress().getCity());
        assertThat(findOrder.getOrderItemList().size()).isEqualTo(3);

        // 지불금액보다 많을 때
        assertThrows(NotCorrectPaidMoneyException.class,
                () -> orderService.save(member.getNum(), paidMoneyMoreThan, orderItemForm1, orderItemForm2, orderItemForm3),
                () -> "1sdfas 지불된 금액이 주문 금액을 초과하였습니다."
        );

        // 지불금액보다 작을 때
        assertThrows(NotCorrectPaidMoneyException.class,
                () -> orderService.save(member.getNum(), paidMoneyLessThan, orderItemForm1, orderItemForm2, orderItemForm3),
                () -> "지불된 금액이 부족합니다."
        );

    }

    /**
     * 주문 수량 초과 에러 확인 v2
     */
    @Test
//    @Rollback(value = false)
    public void 주문수량_초과_에러_확인_v2() {
        // given
        // OrderItemFrom 생성 * 3
        OrderItemForm orderItemForm1 = new OrderItemForm(10000, item1.getNum());
        OrderItemForm orderItemForm2 = new OrderItemForm(1, item1.getNum());
        OrderItemForm orderItemForm3 = new OrderItemForm(20000, item2.getNum());
        // 지불금액 계산
        int paidMoneyO = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity();
        // 지불금액보다 많을 때
        int paidMoneyMoreThan = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity() + 1;
        // 지불금액보다 작을 때
        int paidMoneyLessThan = item1.getPrice() * orderItemForm1.getQuantity() + item1.getPrice() * orderItemForm2.getQuantity() + item2.getPrice() * orderItemForm3.getQuantity() - 1;

        // when // then
        // 주문하기(주문 저장하기)
        // 지불금액 맞을 때
//        orderService.save(orderItemFormList, member.getNum(), paidMoneyO);
        assertThrows(OverflowQuantityException.class, () -> orderService.save(member.getNum(), paidMoneyO, orderItemForm1, orderItemForm2, orderItemForm3));
//        fail(item1.getName() + "상품이 주문수량 초과 발생", NotCorrectPaidMoneyException.class);

        /// item2 제고가 0개 일 때

        // item2 제고 0으로 만들기
        orderService.save(member.getNum(), item2.getPrice() * orderItemForm3.getQuantity(), orderItemForm3);

        // -> 0개를 주문하면
        OrderItemForm orderItemForm4 = new OrderItemForm(0, item2.getNum());

        assertThrows(OverflowQuantityException.class, () -> orderService.save(member.getNum(), paidMoneyO, orderItemForm4));
//        assertThrows(NotCorrectPaidMoneyException.class, () -> orderService.save(member.getNum(), paidMoneyO, orderItemForm4));// 에러 발생
    }

    /**
     * 주문 취소 확인
     * -> order status : O(ORDER) -> C(CANCEL) 바뀌었는지 확인
     */
    @Test
    @Rollback(value = false)
    public void 주문_취소_확인() {
        // given
        // 상품 생성
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        BookForm bookFormTest1 = new BookForm(10000, 1000, "x", "x is...", itemPictureList, "ed", "ok");
        Item item1 = Book.createBook(bookFormTest1, member);

        itemJpaRepository.save(item1);

        // 배달 생성
        // delivery 생성
        Delivery delivery = Delivery.createDelivery(member);

        // 주문상품 생성
        OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1);
        OrderItem orderItem2 = OrderItem.createOrderItem(2, item1.getPrice(), item1);

        // 주문하기
        Order order = Order.createOrder(member,
                delivery,
                item1.getPrice() * orderItem1.getQuantity() + item1.getPrice() * orderItem2.getQuantity()
                , orderItem1, orderItem2);

        orderJpaRepository.save(order);

        log.info("주문 취소 전, item의 stock : {}", item1.getStock());

        // when
        // 주문 취소하기
        orderService.cancel(order.getNum());

        // 다시 order 불러오기
        em.flush();
        em.clear();

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.C);

        log.info("주문 취소 후, item의 stock : {}", item1.getStock());

    }

    /**
     * 주문 취소 예외 확인
     */
    @Test
//    @Rollback(value = false)
    public void 주문_취소_예외_확인() {
        // given
        // 상품 생성
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        BookForm bookFormTest1 = new BookForm(10000, 1000, "x", "x is...", itemPictureList, "ed", "ok");
        Item item1 = Book.createBook(bookFormTest1, member);

        itemJpaRepository.save(item1);

        // 배달 생성
        // delivery 생성
        Delivery delivery = Delivery.createDelivery(member);

        // 주문상품 생성
        OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1);
        OrderItem orderItem2 = OrderItem.createOrderItem(2, item1.getPrice(), item1);

        // 주문하기
        Order order = Order.createOrder(member,
                delivery,
                item1.getPrice() * orderItem1.getQuantity() + item1.getPrice() * orderItem2.getQuantity()
                , orderItem1, orderItem2);

        orderJpaRepository.save(order);

        // when // then
        // 배달준비 -> 배달중으로 바꾸기
        order.getDelivery().changeStatusRT();
        // 주문 취소하기
        assertThrows(NotCancelOrderException.class, () -> orderService.cancel(order.getNum()));
//        assertThrows(IllegalStateException.class, () -> orderService.cancel(order.getNum()));

        log.info("취소가 안 될 경우 v1 item의 stock : {}", item1.getStock());

        // 배달완료로 바꾸기
        order.getDelivery().changeStatusTC();
//        // 주문 취소하기
        assertThrows(NotCancelOrderException.class, () -> orderService.cancel(order.getNum()));
//        assertThrows(IllegalStateException.class, () -> orderService.cancel(order.getNum()));

        log.info("취소가 안 될 경우 v2 item의 stock : {}", item1.getStock());


    }

    /**
     * 주문 상품 상세보기
     */
    @Test
    @Rollback(value = false)
    public void 주문_상품_상세보기() {
        // given
        // 상품 생성
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        BookForm bookFormTest1 = new BookForm(10000, 1000, "x", "x is...", itemPictureList, "ed", "ok");
        Item item1 = Book.createBook(bookFormTest1, member);
        FurnitureForm furnitureFormTest1 = new FurnitureForm(20000, 2000, "y", "y is...", null, "ed");
        Item item2 = Furniture.createFurniture(furnitureFormTest1, member2);

        itemJpaRepository.save(item1);
        itemJpaRepository.save(item2);

        // 배달 생성
        // delivery 생성
        Delivery delivery = Delivery.createDelivery(member);

        // 주문상품 생성
        OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1);
        OrderItem orderItem2 = OrderItem.createOrderItem(2, item1.getPrice(), item1);
        OrderItem orderItem3 = OrderItem.createOrderItem(2, item2.getPrice(), item2);

        // 주문하기
        Order order = Order.createOrder(member,
                delivery,
                item1.getPrice() * orderItem1.getQuantity() + item1.getPrice() * orderItem2.getQuantity() + item2.getPrice() * orderItem3.getQuantity(),
                orderItem1, orderItem2, orderItem3);

        orderJpaRepository.save(order);
        // when
        // 주문 상세보기
        OrderDetailForm detailForm = orderService.watchDetail(order.getNum());

        // then
        assertThat(detailForm.getNum()).isEqualTo(order.getNum());
        assertThat(detailForm.getOrderStatus()).isEqualTo("ORDER");
        assertThat(detailForm.getId()).isEqualTo(order.getMember().getId());
        assertThat(detailForm.getMemberPhone().getSelfPhone()).isEqualTo(order.getMember().getPhones().getSelfPhone());
        assertThat(detailForm.getDeliveryStatus()).isEqualTo("READY");
        assertThat(detailForm.getOrderItemDetailList().stream()
                .map(oid -> oid.getNum())
                .collect(toList()))
                .containsExactly(orderItem1.getNum(), orderItem2.getNum(), orderItem3.getNum());
        assertThat(detailForm.getTotalPrice()).isEqualTo(order.getTotalPrice());

    }

    /**
     * 주문 배달 상태 처리 확인
     * -> 판매자일 때
     * -> R(READY) -> T(TRANSIT)
     */
    @Test
    @Rollback(value = false)
    public void 주문_배달_상태_처리_확인_For_Seller() {
        // given
        // 상품 생성
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        BookForm bookFormTest1 = new BookForm(10000, 1000, "x", "x is...", itemPictureList, "ed", "ok");
        Item item1 = Book.createBook(bookFormTest1, member);
        FurnitureForm furnitureFormTest1 = new FurnitureForm(20000, 2000, "y", "y is...", null, "ed");
        Item item2 = Furniture.createFurniture(furnitureFormTest1, member2);

        itemJpaRepository.save(item1);
        itemJpaRepository.save(item2);

        // 배달 생성
        // delivery 생성
        Delivery delivery = Delivery.createDelivery(member);

        // 주문상품 생성
        OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1);
        OrderItem orderItem2 = OrderItem.createOrderItem(2, item1.getPrice(), item1);
        OrderItem orderItem3 = OrderItem.createOrderItem(2, item2.getPrice(), item2);

        // 주문하기
        Order order = Order.createOrder(member,
                delivery,
                item1.getPrice() * orderItem1.getQuantity() + item1.getPrice() * orderItem2.getQuantity() + item2.getPrice() * orderItem3.getQuantity(),
                orderItem1, orderItem2, orderItem3);

        orderJpaRepository.save(order);

        // when
        // 판매자가 배송준비를 배송중으로 바꾸기
        orderService.changeDeliveryStatusForSeller(order.getNum());

        // then
        assertThat(order.getDelivery().getStatus()).isEqualTo(DeliveryStatus.T);

    }

    /**
     * 주문 배달 상태 처리 확인
     * -> 판매자일 때
     * -> 주문자가 취소했을 경우
     *    -> NotChangeDeliveryStatusException 예외 발생
     */
    @Test
//    @Rollback(value = false)
    public void 주문자가_주문취소했을_경우_주문_배달_상태_처리_확인_For_Seller() {
        // given
        // 상품 생성
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        BookForm bookFormTest1 = new BookForm(10000, 1000, "x", "x is...", itemPictureList, "ed", "ok");
        Item item1 = Book.createBook(bookFormTest1, member);
        FurnitureForm furnitureFormTest1 = new FurnitureForm(20000, 2000, "y", "y is...", null, "ed");
        Item item2 = Furniture.createFurniture(furnitureFormTest1, member2);

        itemJpaRepository.save(item1);
        itemJpaRepository.save(item2);

        // 배달 생성
        // delivery 생성
        Delivery delivery = Delivery.createDelivery(member);

        // 주문상품 생성
        OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1);
        OrderItem orderItem2 = OrderItem.createOrderItem(2, item1.getPrice(), item1);
        OrderItem orderItem3 = OrderItem.createOrderItem(2, item2.getPrice(), item2);

        // 주문하기
        Order order = Order.createOrder(member,
                delivery,
                item1.getPrice() * orderItem1.getQuantity() + item1.getPrice() * orderItem2.getQuantity() + item2.getPrice() * orderItem3.getQuantity(),
                orderItem1, orderItem2, orderItem3);

        orderJpaRepository.save(order);

        // when // then
        // 주문자가 취소하기
        orderService.cancel(order.getNum());

        // 판매자가 배송준비를 배송중으로 바꾸기
        assertThrows(NotChangeDeliveryStatusException.class, () -> orderService.changeDeliveryStatusForSeller(order.getNum()));

    }


    /**
     * 주문 배달 상태 처리 확인
     * -> 배달원일 때
     * -> T(TRANSIT) -> C(COMPLETE)
     */
    @Test
    @Rollback(value = false)
    public void 주문_배달_상태_처리_확인_For_Deliver() {
// given
        // 상품 생성
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        BookForm bookFormTest1 = new BookForm(10000, 1000, "x", "x is...", itemPictureList, "ed", "ok");
        Item item1 = Book.createBook(bookFormTest1, member);
        FurnitureForm furnitureFormTest1 = new FurnitureForm(20000, 2000, "y", "y is...", null, "ed");
        Item item2 = Furniture.createFurniture(furnitureFormTest1, member2);

        itemJpaRepository.save(item1);
        itemJpaRepository.save(item2);

        // 배달 생성
        // delivery 생성
        Delivery delivery = Delivery.createDelivery(member);

        // 주문상품 생성
        OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1);
        OrderItem orderItem2 = OrderItem.createOrderItem(2, item1.getPrice(), item1);
        OrderItem orderItem3 = OrderItem.createOrderItem(2, item2.getPrice(), item2);

        // 주문하기
        Order order = Order.createOrder(member,
                delivery,
                item1.getPrice() * orderItem1.getQuantity() + item1.getPrice() * orderItem2.getQuantity() + item2.getPrice() * orderItem3.getQuantity(),
                orderItem1, orderItem2, orderItem3);

        orderJpaRepository.save(order);

        // when
        // 판매자가 배송준비를 배송중으로 바꾸기
        orderService.changeDeliveryStatusForSeller(order.getNum());

        // 배달원이 배송중을 배송완료로 바꾸기
        orderService.changeDeliveryStatusForDeliver(order.getNum());

        // then
        assertThat(order.getDelivery().getStatus()).isEqualTo(DeliveryStatus.C);

    }


    /**
     * 주문 배달 상태 처리 확인
     * -> 배달원일 때
     * -> 주문자가 취소했을 경우
     *    -> NotChangeDeliveryStatusException 예외 발생
     *
     * -> 일어날 수 없는 경우의 로직이다!!
     */
//    @Test
////    @Rollback(value = false)
//    public void 주문자가_주문취소했을_경우_주문_배달_상태_처리_확인_For_Deliver() {
//        // given
//        // 상품 생성
//        // picture 생성
//        List<ItemPicture> itemPictureList = new ArrayList<>();
//        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
//        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));
//
//        BookForm bookFormTest1 = new BookForm(10000, 1000, "x", "x is...", itemPictureList, "ed", "ok");
//        Item item1 = Book.createBook(bookFormTest1, member);
//        FurnitureForm furnitureFormTest1 = new FurnitureForm(20000, 2000, "y", "y is...", null, "ed");
//        Item item2 = Furniture.createFurniture(furnitureFormTest1, member2);
//
//        itemJpaRepository.save(item1);
//        itemJpaRepository.save(item2);
//
//        // 배달 생성
//        // delivery 생성
//        Delivery delivery = Delivery.createDelivery(member);
//
//        // 주문상품 생성
//        OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1);
//        OrderItem orderItem2 = OrderItem.createOrderItem(2, item1.getPrice(), item1);
//        OrderItem orderItem3 = OrderItem.createOrderItem(2, item2.getPrice(), item2);
//
//        // 주문하기
//        Order order = Order.createOrder(member,
//                delivery,
//                item1.getPrice() * orderItem1.getQuantity() + item1.getPrice() * orderItem2.getQuantity() + item2.getPrice() * orderItem3.getQuantity(),
//                orderItem1, orderItem2, orderItem3);
//
//        orderJpaRepository.save(order);
//
//        // when // then
//        // 주문자가 취소하기
//        orderService.cancel(order.getNum());
//
//        // 이상한 root이다.
//        // 판매자가 배달상태 배송중으로 바꾸기
//        delivery.changeStatusRT();
//
//        // 배송자가 배송준비를 배송중으로 바꾸기
//        assertThrows(NotChangeDeliveryStatusException.class, () -> orderService.changeDeliveryStatusForSeller(order.getNum()));
//
//    }


    /**
     * 주문자의 주문한 상품 목록 확인
     */
    @Test
    @Rollback(value = false)
    public void 주문자의_판매된_상품_목록_확인() {
        // given
        // 페이지, 사이즈 정하기
        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-02-05";
        String otherDay = "2023-02-06";

        // when
        // 검색조건 생성
        // 주문자 member 일 때
        // 조건 0
        OrderSearchCond cond0 = new OrderSearchCond("", "", "", "");
        // 조건 1
        OrderSearchCond cond11 = new OrderSearchCond("", "o", "", "");
        OrderSearchCond cond12 = new OrderSearchCond("", "", "R", "");
        OrderSearchCond cond13 = new OrderSearchCond("", "", "", today);
        // 조건 2
        OrderSearchCond cond21 = new OrderSearchCond("", "o", "R", "");
        OrderSearchCond cond22 = new OrderSearchCond("", "", "t", today);
        OrderSearchCond cond23 = new OrderSearchCond("", "C", "", today);
        // 조건 3
        OrderSearchCond cond31 = new OrderSearchCond("", "o", "R", today);

        // 검색에 대한 result가 없는 경우
        OrderSearchCond cond14 = new OrderSearchCond("", "ab", "", "");
        OrderSearchCond cond24 = new OrderSearchCond("", "", "Rsdaf", otherDay);
        OrderSearchCond cond25 = new OrderSearchCond("", "sdfsdf", "", today);
        OrderSearchCond cond32 = new OrderSearchCond("", "o", "R", otherDay);

        // then
        // 주문 1개 삭제
        int remove = 100 / 3 + 1;
        // 결과 확인(개수로)
        // 조건 0
        orderTestResult(pageablePage0Size100, cond0, member.getId(), oCount);

        // 조건 1
        orderTestResult(pageablePage0Size10, cond11, member.getId(), oCount - remove);
        orderTestResult(pageablePage0Size10, cond12, member.getId(), oCount - remove);
        orderTestResult(pageablePage0Size10, cond13, member.getId(), oCount);

        // 조건 2
        orderTestResult(pageablePage0Size10, cond21, member.getId(), oCount - remove * 2);
        orderTestResult(pageablePage0Size10, cond22, member.getId(), oCount - remove * 2);
        orderTestResult(pageablePage0Size10, cond23, member.getId(), oCount - remove * 2);

        // 조건 3
        orderTestResult(pageablePage3Size5, cond31, member.getId(), oCount - remove * 2);


        // 검색에 대한 result가 없는 경우
        orderTestResult(pageablePage0Size10, cond14, member.getId(), oCount);
        orderTestResult(pageablePage0Size10, cond24, member.getId(), 0);
        orderTestResult(pageablePage0Size10, cond25, member.getId(), oCount);
        orderTestResult(pageablePage0Size10, cond32, member.getId(), 0);

    }

    /**
     * 주문자의_주문_상품_목록_확인의
     *  -> test 결과 확인
     */
    private void orderTestResult(Pageable pageable, OrderSearchCond cond, String memberId, int count) {
        // page 가져오기
        Page<OrderListForm> orderPage = orderService.selectList(cond, pageable, memberId);
        // 전체 개수로 확인
        assertThat(orderPage.getTotalElements()).isEqualTo(count);
    }


//    -------------------------methods using for user(seller) start ----------------------------------

    /**
     * 판매자의 판매된 상품 목록 확인
     */
    @Test
    @Rollback(value = false)
    public void 판매자의_판매된_상품_목록_확인() {
        // given
        // 페이지, 사이즈 정하기
        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-02-05";
        String otherDay = "2023-02-06";

        // when
        // when
        // 조건 생성
        // 판매자가 member 일 때
        // 조건 0
        OrderItemSearchCond cond0 = new OrderItemSearchCond(0, "", "", "", "");
        // 조건 1
        OrderItemSearchCond cond11 = new OrderItemSearchCond(0, "b", "", "", "");
        OrderItemSearchCond cond12 = new OrderItemSearchCond(0, "", "o", "", "");
        OrderItemSearchCond cond13 = new OrderItemSearchCond(0, "", "", "R", "");
        OrderItemSearchCond cond14 = new OrderItemSearchCond(0, "", "", "", today);
        // 조건 2
        OrderItemSearchCond cond21 = new OrderItemSearchCond(0, "a", "O", "", "");
        OrderItemSearchCond cond22 = new OrderItemSearchCond(0, "a", "", "R", "");
        OrderItemSearchCond cond23 = new OrderItemSearchCond(0, "a", "", "", otherDay);
        OrderItemSearchCond cond24 = new OrderItemSearchCond(0, "", "C", "R", "");
        OrderItemSearchCond cond25 = new OrderItemSearchCond(0, "", "c", "", today);
        OrderItemSearchCond cond26 = new OrderItemSearchCond(0, "", "", "t", today);
        // 배달 완료
        OrderItemSearchCond cond27 = new OrderItemSearchCond(0, "", "", "c", today);

        // 조건 3
        OrderItemSearchCond cond31 = new OrderItemSearchCond(0, "b", "o", "T", "");
        OrderItemSearchCond cond32 = new OrderItemSearchCond(0, "b", "O", "", today);
        OrderItemSearchCond cond33 = new OrderItemSearchCond(0, "a", "", "t", today);
        OrderItemSearchCond cond34 = new OrderItemSearchCond(0, "", "O", "R", today);
        // 주문 안 한 아이디
        OrderItemSearchCond cond36 = new OrderItemSearchCond(0, "cd", "O", "", today);

        // 조건 4
        OrderItemSearchCond cond41 = new OrderItemSearchCond(0, "a", "o", "T", today);

        // 조건 잘못 되었을 때
        OrderItemSearchCond cond1 = new OrderItemSearchCond(0, "ok12", "", "", "");
        OrderItemSearchCond cond15 = new OrderItemSearchCond(0, "", "asdf", "", today);
        OrderItemSearchCond cond28 = new OrderItemSearchCond(0, "", "asdf", "sdfas", "");
        OrderItemSearchCond cond35 = new OrderItemSearchCond(0, "", "O", "Tasd", today);
        OrderItemSearchCond cond42 = new OrderItemSearchCond(0, "aefe", "o", "T", today);


        // then
        // 판매자가 member 일 때  나머지 : 0 item1 C(CANCEL) 1개, T(TRANSIT) 1개,
        //                      나머지 : 1 item1 C(CANCEL) 1개
        // 조건 0
        sellerTestResult(cond0, pageablePage0Size10, iaCount + iaCount2);
        // 조건 1
        sellerTestResult(cond11, pageablePage1Size10, iaCount2);
        sellerTestResult(cond12, pageablePage1Size10, iaCount / 2 + iaCount2 / 2);
        sellerTestResult(cond13, pageablePage1Size10, iaCount / 2 + iaCount2);
        sellerTestResult(cond14, pageablePage1Size10, iaCount + iaCount2);
        // 조건 2
        sellerTestResult(cond21, pageablePage0Size100, iaCount / 2);
        sellerTestResult(cond22, pageablePage0Size100, iaCount / 2);
        sellerTestResult(cond23, pageablePage0Size100, 0);
        sellerTestResult(cond24, pageablePage0Size100, iaCount / 2 + iaCount2 / 2);
        sellerTestResult(cond25, pageablePage0Size100, iaCount / 2 + iaCount2 / 2);
        sellerTestResult(cond26, pageablePage0Size100, iaCount / 2);
        // 배달 완료
        sellerTestResult(cond27, pageablePage0Size100, 0);

        // 조건 3
        sellerTestResult(cond31, pageablePage0Size100, 0);
        sellerTestResult(cond32, pageablePage0Size100, iaCount2 / 2);
        sellerTestResult(cond33, pageablePage0Size100, iaCount / 2);
        sellerTestResult(cond34, pageablePage0Size100, iaCount2 / 2);
        // 주문 안 한 아이디
        sellerTestResult(cond36, pageablePage0Size100, 0);

        // 조건 4
        sellerTestResult(cond41, pageablePage2Size2, iaCount / 2);

        // 조건 잘못 되었을 때
        sellerTestResult(cond1, pageablePage0Size10, 0);
        sellerTestResult(cond15, pageablePage1Size10, iaCount + iaCount2); // status의 값이 달라질 때 null 처리 말고 있을까?
        sellerTestResult(cond28, pageablePage1Size10, iaCount + iaCount2); // status의 값이 달라질 때 null 처리 말고 있을까?
        sellerTestResult(cond35, pageablePage1Size10, iaCount / 2 + iaCount2 / 2); // status의 값이 달라질 때 null 처리 말고 있을까?
        sellerTestResult(cond42, pageablePage2Size2, 0); // status의 값이 달라질 때 null 처리 말고 있을까?
    }

    /**
     * 판매자의_판매된_상품_목록_확인의
     *  -> test 결과 확인
     */
    private void sellerTestResult(OrderItemSearchCond cond, Pageable pageable, int count) {
        // page 가져오기
        Page<OrderItemListForm> orderItemPage = orderService.selectOrderItemList(cond, pageable, member.getNum());
        // 전체 개수로 확인
        assertThat(orderItemPage.getTotalElements()).isEqualTo(count);
    }

//    -------------------------methods using for user(seller) end ----------------------------------


//    -------------------------methods using for admin, deliver start----------------------------------

    /**
     * 관리자, 배달원의 주문한 상품 목록 확인
     */
    @Test
    @Rollback(value = false)
    public void 관리자의_판매된_상품_목록_확인() {
        // given
        // 페이지, 사이즈 정하기
        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-02-05";
        String otherDay = "2023-02-06";

        // when
        // 검색조건 생성
        // 조건 0
        OrderSearchCond cond0 = new OrderSearchCond("", "", "", "");
        // 조건 1
        OrderSearchCond cond11 = new OrderSearchCond("cd", "", "", "");
        OrderSearchCond cond12 = new OrderSearchCond("", "o", "", "");
        OrderSearchCond cond13 = new OrderSearchCond("", "", "R", "");
        OrderSearchCond cond14 = new OrderSearchCond("", "", "", today);
        // 조건 2
        OrderSearchCond cond21 = new OrderSearchCond("b", "o", "", "");
        OrderSearchCond cond22 = new OrderSearchCond("b", "", "T", "");
        OrderSearchCond cond23 = new OrderSearchCond("b", "", "", today);
        OrderSearchCond cond24 = new OrderSearchCond("", "c", "R", "");
        OrderSearchCond cond25 = new OrderSearchCond("", "O", "", today);
        OrderSearchCond cond26 = new OrderSearchCond("", "", "R", today);
        // 조건 3
        OrderSearchCond cond31 = new OrderSearchCond("a", "o", "R", "");
        OrderSearchCond cond32 = new OrderSearchCond("a", "O", "", today);
        OrderSearchCond cond33 = new OrderSearchCond("a", "", "R", today);
        OrderSearchCond cond34 = new OrderSearchCond("", "o", "C", today);
        // 조건 4
        OrderSearchCond cond41 = new OrderSearchCond("a", "o", "R", today);

        // 검색에 대한 result가 없는 경우
        OrderSearchCond cond15 = new OrderSearchCond("", "ab", "", "");
        OrderSearchCond cond27 = new OrderSearchCond("", "", "Rsdaf", otherDay);
        OrderSearchCond cond35 = new OrderSearchCond("asd", "sdfsdf", "", today);
        OrderSearchCond cond42 = new OrderSearchCond("a", "o", "Rsdf", today);

        // then
        // 주문 1개 삭제
        int remove1 = 100 / 3 + 1;
        int remove2 = 100 / 3;
        int remove3 = 100 / 3;
        // 결과 확인(개수로)
        // 조건 0
        adminTestResult(pageablePage0Size100, cond0, oCount + oCount2 + oCount3);

        // 조건 1
        adminTestResult(pageablePage0Size10, cond11, oCount3);
        adminTestResult(pageablePage0Size10, cond12, oCount + oCount2 + oCount3 - remove1 - remove2);
        adminTestResult(pageablePage0Size10, cond13, oCount + oCount2 + oCount3 - remove1);
        adminTestResult(pageablePage0Size10, cond14, oCount + oCount2 + oCount3);

        // 조건 2
        adminTestResult(pageablePage0Size10, cond21, oCount2 - remove2);
        adminTestResult(pageablePage0Size10, cond22, 0);
        adminTestResult(pageablePage0Size10, cond23, oCount2);
        adminTestResult(pageablePage0Size10, cond24, oCount - remove1 - remove1 + oCount2 - remove2);
        adminTestResult(pageablePage0Size10, cond25, oCount + oCount2 + oCount3 - remove1 - remove2);
        adminTestResult(pageablePage0Size10, cond26, oCount + oCount2 + oCount3 - remove1);

        // 조건 3
        adminTestResult(pageablePage3Size5, cond31, oCount - remove1 - remove1);
        adminTestResult(pageablePage3Size5, cond32, oCount - remove1);
        adminTestResult(pageablePage3Size5, cond33, oCount - remove1);
        adminTestResult(pageablePage3Size5, cond34, 0);

        // 조건 4
        adminTestResult(pageablePage3Size5, cond41, oCount - remove1 * 2);

        // 검색에 대한 result가 없는 경우
        adminTestResult(pageablePage0Size10, cond15, oCount + oCount2 + oCount3);
        adminTestResult(pageablePage0Size10, cond27, 0);
        adminTestResult(pageablePage0Size10, cond35, 0);
        adminTestResult(pageablePage0Size10, cond42, oCount - remove1);

    }

    /**
     * 관리자, 배달원의_주문_상품_목록_확인의
     *  -> test 결과 확인
     */
    private void adminTestResult(Pageable pageable, OrderSearchCond cond, int count) {
        // page 가져오기
        Page<OrderListFormForAdmin> orderPage = orderService.selectListForAdmin(cond, pageable);
        // 전체 개수로 확인
        assertThat(orderPage.getTotalElements()).isEqualTo(count);
    }

//    -------------------------methods using for admin, deliver end ----------------------------------


}
