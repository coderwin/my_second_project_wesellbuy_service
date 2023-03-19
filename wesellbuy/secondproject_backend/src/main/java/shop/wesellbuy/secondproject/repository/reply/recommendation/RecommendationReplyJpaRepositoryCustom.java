package shop.wesellbuy.secondproject.repository.reply.recommendation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;

/**
 * RecommendationReply Repository by using queryDsl
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : querydsl이용한 RecommendationReplyJpaRepository 모음
 */
public interface RecommendationReplyJpaRepositoryCustom {

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 모든 추찬합니다 댓글 찾기
     */
    Page<RecommendationReply> findAllInfo(RecommendationReplySearchCond recommendationReplySearchCond, Pageable pageable);
}
