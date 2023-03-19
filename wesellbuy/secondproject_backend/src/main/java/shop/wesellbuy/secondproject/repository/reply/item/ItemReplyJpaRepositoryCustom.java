package shop.wesellbuy.secondproject.repository.reply.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.domain.reply.ItemReply;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;
import shop.wesellbuy.secondproject.repository.reply.recommendation.RecommendationReplySearchCond;

/**
 * ItemReplyReply Repository by using queryDsl
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : querydsl이용한 ItemReplyJpaRepository 모음
 */
public interface ItemReplyJpaRepositoryCustom {

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 모든 상품 댓글 찾기
     */
    Page<ItemReply> findAllInfo(ItemReplySearchCond itemReplySearchCond, Pageable pageable);

}
