package shop.wesellbuy.secondproject.repository.recommendation;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.wesellbuy.secondproject.domain.Recommendation;

/**
 * ecommendation Repository
 * writer : 이호진
 * init : 2023.01.18
 * updated by writer :
 * update :
 * description : Recommendation Repository by Spring Data Jpa
 */
public interface RecommendationJpaRepository
        extends JpaRepository<Recommendation, Integer>, RecommendationJpaRepositoryCustom {
}
