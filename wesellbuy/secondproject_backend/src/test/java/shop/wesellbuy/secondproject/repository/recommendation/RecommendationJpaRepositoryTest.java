package shop.wesellbuy.secondproject.repository.recommendation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.Recommendation;
import shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.recommendation.RecommendationForm;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class RecommendationJpaRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    RecommendationJpaRepository rJRepository;

    private Member member; // test 사용 member
    private Member member2; // test 사용 member
    private Member member3; // test 사용 member
    private Member member4; // test 사용 member

    int rCount;
    int r2Count;
    int r3Count;

    // test에 필요한 객체 생성
    @BeforeEach
    public void init() {

        // 회원 생성
        MemberForm memberForm1 = new MemberForm("a", "a", "123","a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member = Member.createMember(memberForm1);
        MemberForm memberForm2 = new MemberForm("a", "b","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member2 = Member.createMember(memberForm2);
        MemberForm memberForm3 = new MemberForm("b", "cd","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member3 = Member.createMember(memberForm3);
        MemberForm memberForm4 = new MemberForm("b", "cde","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member4 = Member.createMember(memberForm4);

        em.persist(member);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        this.member = member; // x 물품 판다
        this.member2 = member2; // y 물품 판다
        this.member3 = member3; // z 물품 판다
        this.member4 = member4; // e 물품 판다

        // 추천합니다 객체 저장
        int number = 100;
        Recommendation recommendation = null;

        // 사진 모음
        List<RecommendationPicture> rpList = new ArrayList<>();
        rpList.add(RecommendationPicture.createRecommendationPicture("x", "x"));
        rpList.add(RecommendationPicture.createRecommendationPicture("y", "y"));
        for(int i = 0; i < number; i++) {

            if(i % 3 == 0) {
                RecommendationForm recommendationForm = new RecommendationForm("y1", "b", "ok", rpList);
                recommendation = Recommendation.createRecommendation(recommendationForm, member);
                this.rCount += 1;
            } else if(i % 3 == 1) {
                RecommendationForm recommendationForm = new RecommendationForm("z1", "c", "ok2", null);
                recommendation = Recommendation.createRecommendation(recommendationForm, member2);
                this.r2Count += 1;
            } else {
                RecommendationForm recommendationForm = new RecommendationForm("x1", "a", "ok3", rpList);
                recommendation = Recommendation.createRecommendation(recommendationForm, member3);
                this.r3Count += 1;
            }
            em.persist(recommendation);
        }
    }

    @Test
//    @Rollback(value = false)
    public void 추천합니다_글_저장_add_상세보기() {
        // given
        List<RecommendationPicture> rpList = new ArrayList<>();
        rpList.add(RecommendationPicture.createRecommendationPicture("e", "e"));
        rpList.add(RecommendationPicture.createRecommendationPicture("e1", "e1"));

        RecommendationForm recommendationForm = new RecommendationForm("y1", "y", "ok", rpList);
        Recommendation recommendation = Recommendation.createRecommendation(recommendationForm, member);
        // 저장 확인
        rJRepository.save(recommendation);
        // when
        Recommendation findRecommendation = rJRepository.findDetailInfoById(recommendation.getNum()).orElseThrow();

        // then
        // 상세보기 확인
        assertThat(findRecommendation).isEqualTo(recommendation);
    }

    /**
     * 추천합니다글_모두_가져오기_확인
     * admin이 사용하는
     */
    @Test
//    @Rollback(value = false)
    public void 추천합니다글_모두_가져오기_for_admin_By_조건_페이징() {
        // given
        // 페이지, 사이즈 정하기
//        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-01-27";
        String otherDay = "2023-01-28";

        // 검색 조건 생성
        // 조건 1
        // id : a, b, cd
//        RecommendationSearchCond cond1 = new RecommendationSearchCond("y1", null, null, null);
//        RecommendationSearchCond cond2 = new RecommendationSearchCond(null, "b", null, null);
//        RecommendationSearchCond cond3 = new RecommendationSearchCond(null, null, "a", null);
//        RecommendationSearchCond cond4 = new RecommendationSearchCond(null, null, null, today);
        // 조건 2
//        RecommendationSearchCond cond21 = new RecommendationSearchCond("y1", "b", null, null);
//        RecommendationSearchCond cond22 = new RecommendationSearchCond("y1", null, "a", null);
//        RecommendationSearchCond cond23 = new RecommendationSearchCond("y1", null, null, today);
//        RecommendationSearchCond cond24 = new RecommendationSearchCond(null, "b", "a", null);
//        RecommendationSearchCond cond25 = new RecommendationSearchCond(null, "b", null, today);
//        RecommendationSearchCond cond26 = new RecommendationSearchCond(null, null, "a", today);
        // 조건 3
//        RecommendationSearchCond cond31 = new RecommendationSearchCond("y1", "b", "a", null);
//        RecommendationSearchCond cond32 = new RecommendationSearchCond("y1", "b", null, today);
//        RecommendationSearchCond cond33 = new RecommendationSearchCond("y1", null, "a", today);
//        RecommendationSearchCond cond34 = new RecommendationSearchCond(null, "b", "a", today);
        // 조건 4
        RecommendationSearchCond cond45 = new RecommendationSearchCond("y1", "b", "a", today);


        // 검색 값이 없는 경우
//        RecommendationSearchCond cond5 = new RecommendationSearchCond(null, "hi", null, null);
//        RecommendationSearchCond cond27 = new RecommendationSearchCond(null, null, "cde", today);
//        RecommendationSearchCond cond35 = new RecommendationSearchCond(null, "cd", "b", otherDay);
        RecommendationSearchCond cond46 = new RecommendationSearchCond("y1", "b", "a", otherDay);


        // when
        // then
        // 조건 1
//        log.info("rCount : {}", rCount);
//        testResultForAdmin(pageablePage0Size10, cond1, rCount);
//        testResultForAdmin(pageablePage0Size10, cond2, rCount);
//        testResultForAdmin(pageablePage0Size10, cond3, rCount);
//        testResultForAdmin(pageablePage0Size10, cond4, rCount + r2Count + r3Count);
        // 조건 2
//        testResultForAdmin(pageablePage1Size10, cond21, rCount);
//        testResultForAdmin(pageablePage1Size10, cond22, rCount);
//        testResultForAdmin(pageablePage1Size10, cond23, rCount);
//        testResultForAdmin(pageablePage1Size10, cond24, rCount);
//        testResultForAdmin(pageablePage1Size10, cond25, rCount);
//        testResultForAdmin(pageablePage1Size10, cond26, rCount);
        // 조건 3
//        testResultForAdmin(pageablePage2Size2, cond31, rCount);
//        testResultForAdmin(pageablePage2Size2, cond32, rCount);
//        testResultForAdmin(pageablePage2Size2, cond33, rCount);
//        testResultForAdmin(pageablePage2Size2, cond34, rCount);

        // 조건 4
         testResultForAdmin(pageablePage1Size10, cond45, rCount);


        // 검색 값이 없는 경우
//        testResultForAdmin(pageablePage0Size10, cond5, 0);
//        testResultForAdmin(pageablePage0Size10, cond27, 0);
//        testResultForAdmin(pageablePage0Size10, cond35, 0);
        testResultForAdmin(pageablePage0Size10, cond46, 0);


    }

    // findAllInfo test result
    private void testResultForAdmin(Pageable pageable, RecommendationSearchCond cond, int count) {
        // when
        Page<Recommendation> result = rJRepository.findAllInfoForAdmin(cond, pageable);
        // then
        assertThat(result.getTotalElements()).isEqualTo(count);
    }

    /**
     * 추천합니다글_모두_가져오기_확인
     */
    @Test
    @Rollback(value = false)
    public void 추천합니다글_모두_가져오기_By_조건_페이징() {
        // given
        // 삭제된(status R -> D된 게시글 만들기)
        // 게시글 생성
        int r4Count = 30;
        for(int i = 0; i < r4Count; i++) {
            RecommendationForm recommendationForm = new RecommendationForm("e1", "cde", "ok~", new ArrayList<>());
            Recommendation recommendation = Recommendation.createRecommendation(recommendationForm, member4);
            // 저장 확인
            rJRepository.save(recommendation);
            // 삭제하기
            recommendation.changeStatus(); // status: R -> D
        }

        // 페이지, 사이즈 정하기
//        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-02-01";
        String otherDay = "2023-02-02";

        // 검색 조건 생성
        // 조건 0
        RecommendationSearchCond cond0 = new RecommendationSearchCond("", "", "", "");

        // 조건 1
        // id : a, b, cd
        RecommendationSearchCond cond1 = new RecommendationSearchCond("y1", null, null, null);
        RecommendationSearchCond cond2 = new RecommendationSearchCond(null, "b", null, null);
        RecommendationSearchCond cond3 = new RecommendationSearchCond(null, null, "a", null);
        RecommendationSearchCond cond4 = new RecommendationSearchCond(null, null, null, today);
        // 조건 2
        RecommendationSearchCond cond21 = new RecommendationSearchCond("y1", "b", null, null);
        RecommendationSearchCond cond22 = new RecommendationSearchCond("y1", null, "a", null);
        RecommendationSearchCond cond23 = new RecommendationSearchCond("y1", null, null, today);
        RecommendationSearchCond cond24 = new RecommendationSearchCond(null, "b", "a", null);
        RecommendationSearchCond cond25 = new RecommendationSearchCond(null, "b", null, today);
        RecommendationSearchCond cond26 = new RecommendationSearchCond(null, null, "a", today);
        // 조건 3
        RecommendationSearchCond cond31 = new RecommendationSearchCond("y1", "b", "a", null);
        RecommendationSearchCond cond32 = new RecommendationSearchCond("y1", "b", null, today);
        RecommendationSearchCond cond33 = new RecommendationSearchCond("y1", null, "a", today);
        RecommendationSearchCond cond34 = new RecommendationSearchCond(null, "b", "a", today);
        // 조건 4
        RecommendationSearchCond cond45 = new RecommendationSearchCond("y1", "b", "a", today);


        // 검색 값이 없는 경우
        RecommendationSearchCond cond5 = new RecommendationSearchCond(null, "hi", null, null);
        RecommendationSearchCond cond27 = new RecommendationSearchCond(null, null, "cde", today);
        RecommendationSearchCond cond35 = new RecommendationSearchCond(null, "cd", "b", otherDay);
        RecommendationSearchCond cond46 = new RecommendationSearchCond("y1", "b", "a", otherDay);


        // when
        // then
        // 조건 0
        testResult(pageablePage0Size10, cond0, rCount + r2Count +  r3Count);
        // 조건 1
        log.info("rCount : {}", rCount);
        testResult(pageablePage0Size10, cond1, rCount);
        testResult(pageablePage0Size10, cond2, rCount);
        testResult(pageablePage0Size10, cond3, rCount);
        testResult(pageablePage0Size10, cond4, rCount + r2Count + r3Count);
        // 조건 2
        testResult(pageablePage1Size10, cond21, rCount);
        testResult(pageablePage1Size10, cond22, rCount);
        testResult(pageablePage1Size10, cond23, rCount);
        testResult(pageablePage1Size10, cond24, rCount);
        testResult(pageablePage1Size10, cond25, rCount);
        testResult(pageablePage1Size10, cond26, rCount);
        // 조건 3
        testResult(pageablePage2Size2, cond31, rCount);
        testResult(pageablePage2Size2, cond32, rCount);
        testResult(pageablePage2Size2, cond33, rCount);
        testResult(pageablePage2Size2, cond34, rCount);

        // 조건 4
        testResult(pageablePage1Size10, cond45, rCount);


        // 검색 값이 없는 경우
        testResult(pageablePage0Size10, cond5, 0);
        testResult(pageablePage0Size10, cond27, 0);
        testResult(pageablePage0Size10, cond35, 0);
        testResult(pageablePage0Size10, cond46, 0);


    }

    // findAllInfo test result
    private void testResult(Pageable pageable, RecommendationSearchCond cond, int count) {
        // when
        Page<Recommendation> result = rJRepository.findAllInfo(cond, pageable);
        // then
        assertThat(result.getTotalElements()).isEqualTo(count);
    }


}
