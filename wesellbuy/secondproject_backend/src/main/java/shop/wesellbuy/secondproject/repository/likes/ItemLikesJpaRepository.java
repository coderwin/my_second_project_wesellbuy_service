package shop.wesellbuy.secondproject.repository.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.wesellbuy.secondproject.domain.likes.ItemLikes;

/**
 * ItemLikes Repository
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : ItemLikes Repository by Spring Data Jpa
 */
public interface ItemLikesJpaRepository extends JpaRepository<ItemLikes, Integer>, ItemLikesJpaRepositoryCustom, ItemLikesJpaRepositoryQueryCustom {
}
