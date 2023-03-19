package shop.wesellbuy.secondproject.repository.likes;

import com.querydsl.core.Tuple;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.likes.ItemLikes;

import java.util.List;
import java.util.Optional;

/**
 * ItemLikes Repository by using queryDsl
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer : 이호진
 * update : 2023.03.14
 * description : querydsl이용한 ItemLikesJpaRepository 모음
 *
 * update : findByItemNumAndMemberNum 추가
 */
public interface ItemLikesJpaRepositoryCustom {

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : 모든 상품_좋아요 찾기 + fetchjoin
     *               ...ById == ...ByNum
     */
    List<ItemLikes> findAllInfoById(int memberNum);

    /**
     * writer : 이호진
     * init : 2023.03.14
     * updated by writer :
     * update :
     * description : 상품 좋아요 찾기 by itemNum and memberNum
     */
    Optional<ItemLikes> findByItemNumAndMemberNum(int itemNum, int memberNum);

    /**
     * writer : 이호진
     * init : 2023.01.20
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : 모든 상품_좋아요 or 좋아요 많은 순위 찾기
     *               -> item service에서 사용한다.
     */
    List<Tuple> findRank();

    /**
     * writer : 이호진
     * init : 2023.01.20
     * updated by writer : 이호진
     * update : 2023.02.02
     * description : 모든 상품 좋아요 많은 순위 찾기 V4
     *               -> List<Item> 반환
     *               -> fetchJoin 사용
     *
     *               -> itemLikes와 item은 fetch조인이 왜 안 될까?
     */
    List<Item> findRankV4();
}
