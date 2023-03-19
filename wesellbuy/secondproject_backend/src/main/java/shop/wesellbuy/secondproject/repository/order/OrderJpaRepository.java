package shop.wesellbuy.secondproject.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.wesellbuy.secondproject.domain.Order;

/**
 * Order Repository
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : Order Repository by Spring Data Jpa
 */
public interface OrderJpaRepository extends JpaRepository<Order, Integer>, OrderJpaRepositoryCustom {
}
