package shop.wesellbuy.secondproject.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.wesellbuy.secondproject.domain.Item;

import java.util.Optional;

/**
 * Item Repository
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : Item Repository by Spring Data Jpa
 */
public interface ItemJpaRepository extends JpaRepository<Item, Integer>,  ItemJpaRepositoryCustom{

}
