package shop.wesellbuy.secondproject.repository.item;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.domain.Item;

import java.util.Optional;

/**
 * Item Repository by using queryDsl
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : querydsl이용한 ItemJpaRepository 모음
 */
public interface ItemJpaRepositoryCustom {
    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : 모든 추천합니다 게시글 찾기 + fetchjoin
     */
    Page<Item> findAllInfo(ItemSearchCond itemSearchCond, Pageable pageable);

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : 상품 상세보기 + fetchjoin by num(id)
     */
    Optional<Item> findDetailInfoById(int num);

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 상품 name & 판매자 id로 Item 검색
     */
    Optional<Item> findByNameAndSellerId(String itemName, String memberId);

//    -------------------------methods using for admin start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 모든 추천합니다 게시글 찾기 + fetchjoin
     *               -> admin에서 사용
     *
     * comment : test 해보기
     */
    Page<Item> findAllInfoForAdmin(ItemSearchCond itemSearchCond, Pageable pageable);

//    -------------------------methods using for admin end----------------------------------

}
