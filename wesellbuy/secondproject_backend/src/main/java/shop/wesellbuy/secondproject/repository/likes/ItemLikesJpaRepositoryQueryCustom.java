package shop.wesellbuy.secondproject.repository.likes;

import com.querydsl.core.Tuple;

import java.util.List;

/**
 * ItemLikes Repository by using queryDsl
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : querydsl + select(dto)이용한 ItemLikesJpaRepository 모음
 */
public interface ItemLikesJpaRepositoryQueryCustom {

    /**
     * writer : 이호진
     * init : 2023.01.20
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : 모든 상품 좋아요 많은 순위 찾기 + select(dto)
     */
    List<ItemRankDto> findRankV2();

    /**
     * writer : 이호진
     * init : 2023.01.20
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : 모든 상품 좋아요 많은 순위 찾기 + query
     *
     * comment : rank() 함수를 사용할 수 없을까?
     */
//    public List<Tuple> findRankV3();
}
