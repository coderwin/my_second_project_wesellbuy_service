package shop.wesellbuy.secondproject.repository.reply.item;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.wesellbuy.secondproject.domain.reply.ItemReply;

/**
 * ItemReply Repository
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : ItemReply Repository by Spring Data Jpa
 */
public interface ItemReplyJpaRepository extends JpaRepository<ItemReply, Integer>, ItemReplyJpaRepositoryCustom {
}
