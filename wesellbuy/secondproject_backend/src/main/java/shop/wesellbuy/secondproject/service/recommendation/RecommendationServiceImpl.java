package shop.wesellbuy.secondproject.service.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.Recommendation;
import shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture;
import shop.wesellbuy.secondproject.exception.recommendation.NotExistingItemException;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.recommendation.RecommendationJpaRepository;
import shop.wesellbuy.secondproject.repository.recommendation.RecommendationSearchCond;
import shop.wesellbuy.secondproject.web.recommendation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * RecommendationService Service 구현 클래스
 * writer : 이호진
 * init : 2023.02.01
 * updated by writer :
 * update :
 * description : RecommendationService Service 구현 메소드 모음
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationJpaRepository recommendationJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final FileStoreOfRecommendationPicture fileStore; // 이미지 저장

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 저장
     */
    @Override
    @Transactional
    public int save(RecommendationForm recommendationForm, List<MultipartFile> files, int memberNum) throws IOException {
        // 추천받은 상품 & 판매자 있는지 확인
        checkItemNameAndSellerId(recommendationForm.getItemName(), recommendationForm.getSellerId());
        // 추천합니다 사진 서버 컴퓨터에 저장하기
        List<RecommendationPicture> recommendationPictureList = fileStore.storeFiles(files);
        // 생성된 사진 list recommendationForm에 담기
        recommendationForm.addRecommendationPictureList(recommendationPictureList);
        // 회원 불러오기
        Member member = memberJpaRepository.findById(memberNum).orElseThrow();
        // 추천합니다글 생성
        Recommendation recommendation = Recommendation.createRecommendation(recommendationForm, member);
        // 추천합니다글 저장
        recommendationJpaRepository.save(recommendation);

        return recommendation.getNum();
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 상품 & 판매자가 존재하는지 확인한다.
     */
    private void checkItemNameAndSellerId(String itemName, String memberId) {
        // 추천받은 상품 있는지 확인
        String errMsg = "추천 상품 또는 판매자를 잘못 입력하셨습니다.";
        itemJpaRepository.findByNameAndSellerId(itemName, memberId)
                .orElseThrow(() -> new NotExistingItemException(errMsg));
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 상세보기
     */
    @Override
    @Transactional
    public RecommendationDetailForm watchDetail(int num) {
        // 조회수 1 증가 시킨다.
        updateHits(num);
        // 추천합니다글 불러오기
        Recommendation recommendation = recommendationJpaRepository.findDetailInfoById(num).orElseThrow();
        // RecommendationDetailForm으로 변경하기
        return RecommendationDetailForm.create(recommendation);
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 조회수 1 증가
     *               -> 상세보기 클릭시 실행된다.
     */
    private void updateHits(int num) {
        // 추천합니다글 불러오기
        Recommendation recommendation = recommendationJpaRepository.findById(num).orElseThrow();
        // 조회수 1 증가 시키기
        recommendation.changeHits();
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 수정
     */
    @Override
    @Transactional
    public void update(RecommendationUpdateForm recommendationUpdateForm,
                       List<MultipartFile> files) throws IOException {
        // 추천받은 상품 & 판매자 있는지 확인
        checkItemNameAndSellerId(recommendationUpdateForm.getItemName(), recommendationUpdateForm.getSellerId());
        // 추천합니다글 불러오기
        Recommendation recommendation = recommendationJpaRepository.findById(recommendationUpdateForm.getNum()).orElseThrow();
        // 이미지 서버 컴퓨터에 저장하기
        List<RecommendationPicture> newPictures = fileStore.storeFiles(files);
        // 수정하기
        recommendation.updateRecommendation(recommendationUpdateForm, newPictures);
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 삭제
     *               -> status 상태를 변경한다(R -> D)
     */
    @Override
    @Transactional
    public void delete(int num) {
        // 추천합니다글 불러오기
        Recommendation recommendation = recommendationJpaRepository.findById(num).orElseThrow();
        // 추천합니다글 상태 변경(R -> D)
        recommendation.changeStatus();
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 추천합니다글 이미지 삭제
     *               -> status 상태를 변경한다(R -> D)
     */
    @Override
    @Transactional
    public void deletePicture(int recommendationNum, int pictureNum) {
        // 추천합니다글 불러오기
        Recommendation recommendation = recommendationJpaRepository.findById(recommendationNum).orElseThrow();
        // picture를 삭제한다.
        recommendation.deletePicture(pictureNum);
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer : 이호진
     * update : 2023.02.01
     * description : 추천합니다글 모두 불러오기
     *               -> status 상태(D)는 출력 안 한다.
     *
     * comment : list, count를 찾아서 다시 Page를 만들어야할까?
     *           아니면 Page<Recommendation> 상태에서 바로 만드는 방법 있을까?
     *
     *           --> db의 where절에 status = R 조건 추가로 해결
     */
    @Override
    public Page<RecommendationListForm> selectList(RecommendationSearchCond cond, Pageable pageable) {
        // 조건에 맞는 추천합니다글 불러오기
        Page<Recommendation> recommendationList = recommendationJpaRepository.findAllInfo(cond, pageable);
        // RecommendationListForm에 정보 담기
        // status가 D(Delete)인 건 출력 안한다.
        // list 만들기
        Page<RecommendationListForm> result = recommendationList.map(r -> RecommendationListForm.create(r));

        return result;
    }

//    -------------------------methods using for admin start----------------------------------
    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 모두 불러오기
     *               -> status 사용
     *               -> admin이 사용한다.
     */
    @Override
    public Page<RecommendationListForAdminForm> selectListForAdmin(RecommendationSearchCond cond, Pageable pageable) {
        // 조건에 맞는 추천합니다글 불러오기
        Page<Recommendation> recommendationList = recommendationJpaRepository.findAllInfoForAdmin(cond, pageable);
        // RecommendationListForm에 정보 담기
        Page<RecommendationListForAdminForm> result = recommendationList.map(r -> RecommendationListForAdminForm.create(r));

        return result;
    }

//    -------------------------methods using for admin end----------------------------------



}
