package shop.wesellbuy.secondproject.repository.reply.recommendation;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;

/**
 * RecommendationReply Repository
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : RecommendationReply Repository by Spring Data Jpa
 */
public interface RecommendationReplyJpaRepository extends JpaRepository<RecommendationReply, Integer>, RecommendationReplyJpaRepositoryCustom {
}
