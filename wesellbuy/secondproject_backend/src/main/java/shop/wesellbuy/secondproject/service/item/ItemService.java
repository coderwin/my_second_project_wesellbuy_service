package shop.wesellbuy.secondproject.service.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.repository.item.ItemSearchCond;
import shop.wesellbuy.secondproject.web.item.*;

import java.io.IOException;
import java.util.List;

/**
 * Item Service
 * writer : 이호진
 * init : 2023.02.02
 * updated by writer :
 * update :
 * description : Item Service 메소드 모음
 */
public interface ItemService {

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 저장
     *               -> 상품이름 중복 상관 없음
     */
    int save(ItemOriginalForm itemOriginalForm, List<MultipartFile> files, int memberNum) throws IOException;

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 수정
     *
     * comment : 강제 type 변환 말고 다른 방법이 있을까?
     */
    void update(ItemUpdateForm itemUpdateForm, List<MultipartFile> files) throws IOException;

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 삭제
     *               -> status 상태를 변경한다(R -> D)
     */
    void delete(int num);

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 상세보기
     */
    ItemDetailForm watchDetail(int num);

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 이미지 삭제
     *               -> status 상태를 변경한다(R -> D)
     *
     * comment : 확인해보기
     *           -> betch size 작동하는가?
     */
    void deletePicture(int itemNum, int pictureNum);

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 순위 불러오기
     *               -> 좋아요수가 높은 순으로
     *
     * comment : repository에서 select(item)에 문제가 있을 것으로 예상()
     *           -> Dto로 해결해볼 것을 생각
     *           -> Dto 말고는 없을까?
     */
    List<ItemRankForm> selectRank();

    /**
     * writer : 이호진
     * init : 2023.02.03
     * updated by writer :
     * update :
     * description : 상품 순위 불러오기 V2
     *               -> 좋아요수가 높은 순으로
     *               -> Tuple 이용
     *
     * comment : Qclass를 써도 될까?(서비스에서?)
     *           -> 안 쓰고 순위대로 불러오는 방법이 있을까?
     *           -> DTO로 조회하는 것 밖에 없나?
     *              -> DTO로 조회시, 사진 list는 어떻게 할 것인가?
     *                  -> N + 1문제 발생하지 않나?
     *                  -> 불러와지지도 않을까?
     */
    List<ItemRankForm> selectRankV2();

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer : 이호진
     * update : 2023.02.01
     * description : 추천합니다글 모두 불러오기
     *               -> status 상태(D)는 출력 안 한다.
     */
    Page<ItemListForm> selectList(ItemSearchCond cond, Pageable pageable);

//    -------------------------methods using for admin start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 모두 불러오기
     *               -> status 사용
     *               -> admin이 사용한다.
     */
    Page<ItemListFormForAdmin> selectListForAdmin(ItemSearchCond cond, Pageable pageable);

//    -------------------------methods using for admin end----------------------------------

}
