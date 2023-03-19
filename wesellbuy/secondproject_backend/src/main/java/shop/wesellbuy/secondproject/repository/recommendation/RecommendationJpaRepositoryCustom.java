package shop.wesellbuy.secondproject.repository.recommendation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.domain.Recommendation;

import java.util.Optional;

/**
 * Recommendation Repository by using queryDsl
 * writer : 이호진
 * init : 2023.01.18
 * updated by writer :
 * update :
 * description : querydsl이용한 RecommendationJpaRepository 모음
 */
public interface RecommendationJpaRepositoryCustom {

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : 모든 추천합니다 게시글 찾기 + fetchjoin
     *               -> admin에서 사용
     */
    Page<Recommendation> findAllInfoForAdmin(RecommendationSearchCond recommendationSearchCond, Pageable pageable);

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 모든 고객지원 게시글 찾기
     */
    Page<Recommendation> findAllInfo(RecommendationSearchCond customerServiceSearchCond, Pageable pageable);

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : 추천합니다 상세보기 + fetchjoin by num(id)
     */
    public Optional<Recommendation> findDetailInfoById(int num);
}
