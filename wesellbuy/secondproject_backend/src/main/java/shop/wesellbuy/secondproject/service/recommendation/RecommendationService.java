package shop.wesellbuy.secondproject.service.recommendation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.repository.recommendation.RecommendationSearchCond;
import shop.wesellbuy.secondproject.web.recommendation.*;

import java.io.IOException;
import java.util.List;

/**
 * Recommendation Service
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer :
 * update :
 * description : Recommendation Service 메소드 모음
 */
public interface RecommendationService {

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 저장
     */
    int save(RecommendationForm recommendationForm, List<MultipartFile> files, int memberNum) throws IOException;

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 상세보기
     */
    RecommendationDetailForm watchDetail(int num);

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 수정
     */
    void update(RecommendationUpdateForm recommendationUpdateForm,
                List<MultipartFile> files) throws IOException;

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 삭제
     *               -> status 상태를 변경한다(R -> D)
     */
    void delete(int num);

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 추천합니다글 이미지 삭제
     *               -> status 상태를 변경한다(R -> D)
     */
    void deletePicture(int recommendationNum, int pictureNum);

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 모두 불러오기
     *               -> status 상태(D)는 출력 안 한다.
     *
     * comment : list, count를 찾아서 다시 Page를 만들어야할까?
     *           아니면 Page<Recommendation> 상태에서 바로 만드는 방법 있을까?
     */
    Page<RecommendationListForm> selectList(RecommendationSearchCond cond, Pageable pageable);


    //    -------------------------methods using for admin start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 모두 불러오기
     *               -> status 상관 없이
     *               -> admin이 사용한다.
     */
    Page<RecommendationListForAdminForm> selectListForAdmin(RecommendationSearchCond cond, Pageable pageable);

    //    -------------------------methods using for admin end----------------------------------

}
