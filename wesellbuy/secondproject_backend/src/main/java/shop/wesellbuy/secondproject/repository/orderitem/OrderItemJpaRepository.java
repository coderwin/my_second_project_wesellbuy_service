package shop.wesellbuy.secondproject.repository.orderitem;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.wesellbuy.secondproject.domain.OrderItem;

/**
 * OrderItem Repository
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : OrderItem Repository by Spring Data Jpa
 */
public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Integer>, OrderItemJpaRepositoryCustom {
}
