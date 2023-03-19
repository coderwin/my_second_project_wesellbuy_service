package shop.wesellbuy.secondproject.service.likes;

import shop.wesellbuy.secondproject.web.likes.ItemLikesListForm;

import java.util.List;

/**
 * ItemLikes Service
 * writer : 이호진
 * init : 2023.02.01
 * updated by writer : 이호진
 * update : 2023.03.14
 * description : ItemLikes Service 메소드 모음
 *
 * update : 상품 좋아요 삭제 by itemNum and memberNum 추가
 */
public interface ItemLikesService {

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 상품 좋아요 저장
     */
    int save(int itemNum, int memberNum);

//    /**
//     * writer : 이호진
//     * init : 2023.02.01
//     * updated by writer :
//     * update :
//     * description : 상품 좋아요 삭제
//     */
//    void delete(int num);

    /**
     * writer : 이호진
     * init : 2023.03.14
     * updated by writer :
     * update :
     * description : 상품 좋아요 삭제 by itemNum and memberNum
     */
    void delete(int itemNum, int MemberNum);

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 모든 상품 좋아요 불러오기 by memberNum
     */
    List<ItemLikesListForm> selectList(int memberNum);

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 로그인한 회원의 모든 상품 좋아요 불러오기 by memberNum
     *               > 좋아요 색깔 표시할지 말지 결정
     */
    List<Integer> selectListForItemList(int memberNum);
}
