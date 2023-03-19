package shop.wesellbuy.secondproject.repository.reply.recommendation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;
import shop.wesellbuy.secondproject.repository.recommendation.RecommendationJpaRepository;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.recommendation.RecommendationForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class RecommendationReplyRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    RecommendationReplyJpaRepository recommendationReplyJpaRepository;

    @Autowired
    RecommendationJpaRepository recommendationJpaRepository;

    Member member; // 등록 회원
    Member member2; // 등록 회원
    Member member3; // 등록 회원

    Recommendation recommendation; // 등록 추천합니다 글
    Recommendation recommendation2; // 등록 추천합니다 글
    Recommendation recommendation3; // 등록 추천합니다 글

    // create member/customerService object
    @BeforeEach
    public void init() {
        // 회원 정보 저장
        MemberForm memberForm1 = new MemberForm("a", "a","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm2 = new MemberForm("b", "b","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm3 = new MemberForm("c", "c","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member = Member.createMember(memberForm1);
        Member member2 = Member.createMember(memberForm2);
        Member member3 = Member.createMember(memberForm3);

        em.persist(member);
        em.persist(member2);
        em.persist(member3);

        this.member = member;
        this.member2 = member2;
        this.member3 = member3;

        // 추천합니다 글 저장
        RecommendationForm recommendationForm = new RecommendationForm("y11", "c", "ok", null);
        recommendation = Recommendation.createRecommendation(recommendationForm, member);
        RecommendationForm recommendationForm2 = new RecommendationForm("y12", "a", "ok", null);
        recommendation2 = Recommendation.createRecommendation(recommendationForm2, member);
        RecommendationForm recommendationForm3 = new RecommendationForm("y13", "b", "ok", null);
        recommendation3 = Recommendation.createRecommendation(recommendationForm3, member);

        recommendationJpaRepository.save(recommendation);
        recommendationJpaRepository.save(recommendation2);
        recommendationJpaRepository.save(recommendation3);
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : save Test
     */
    @Test
    @Rollback(false)
    public void 고객지원글저장() {
        // given
        ReplyForm replyForm = new ReplyForm("hello3");
        RecommendationReply recommendationReply = RecommendationReply.createRecommendationReply(replyForm, member, recommendation);

        recommendationReplyJpaRepository.save(recommendationReply);
        // when
        RecommendationReply findRecommendationReply = recommendationReplyJpaRepository.findById(recommendationReply.getNum()).orElseThrow();

        // then
        Assertions.assertThat(findRecommendationReply).isEqualTo(recommendationReply);

    }

    @Test
    @Rollback(value = false)
    public void 상품_모두_가져오기_By_조건_페이징() {
        // given
        // 100개의 더미 파일 만들기
        Long count = 100L;
        int rCount = 0; // 게시글 개수 member의
        int r2Count = 0; // 게시글 개수 member2의
        int r3Count = 0; // 게시글 개수 member3의

        RecommendationReply reply = null;
        for (int i = 0; i < count; i++) {

            if (i % 3 == 0) {
                ReplyForm replyForm = new ReplyForm("hello11");
                reply = RecommendationReply.createRecommendationReply(replyForm, member, recommendation);
                rCount += 1;
            } else if (i % 3 == 1) {
                ReplyForm replyForm = new ReplyForm("hello22");
                reply = RecommendationReply.createRecommendationReply(replyForm, member2, recommendation);
                r2Count += 1;
            } else {
                ReplyForm replyForm = new ReplyForm("hello33");
                reply = RecommendationReply.createRecommendationReply(replyForm, member3, recommendation);
                r3Count += 1;
            }

            em.persist(reply);
        }

        // 페이지, 사이즈 정하기
//        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-01-27";
        String otherDay = "2023-01-28";

        // when
        // 검색조건 생성
        // 조건 1
        RecommendationReplySearchCond cond1 = new RecommendationReplySearchCond("b", null, null);
        RecommendationReplySearchCond cond2 = new RecommendationReplySearchCond(null, "ll", null);
        RecommendationReplySearchCond cond3 = new RecommendationReplySearchCond(null, null, today);
        // 조건2
        RecommendationReplySearchCond cond21 = new RecommendationReplySearchCond("a", "ll", null);
        RecommendationReplySearchCond cond22 = new RecommendationReplySearchCond("a", null, today);
        RecommendationReplySearchCond cond23 = new RecommendationReplySearchCond("", "ll", today);
        // 조건3
        RecommendationReplySearchCond cond31 = new RecommendationReplySearchCond("c", "ll", today);


        // 맞지 않는 조건
        RecommendationReplySearchCond cond4 = new RecommendationReplySearchCond(null, "hew", null);
        RecommendationReplySearchCond cond24 = new RecommendationReplySearchCond("a", "hew", "");
        RecommendationReplySearchCond cond32 = new RecommendationReplySearchCond("a", "hew", otherDay);


        // then
        // 조건1
        testResult(pageablePage1Size10, cond1, r2Count);
        testResult(pageablePage1Size10, cond2, rCount + r2Count + r3Count);
        testResult(pageablePage1Size10, cond3, rCount + r2Count + r3Count);
        // 조건2
        testResult(pageablePage1Size10, cond21, rCount);
        testResult(pageablePage1Size10, cond22, rCount);
        testResult(pageablePage1Size10, cond23, rCount + r2Count + r3Count);
        // 조건3
        testResult(pageablePage1Size10, cond31, r3Count);


        // 맞지 않는 조건
        testResult(pageablePage1Size10, cond4, 0);
        testResult(pageablePage1Size10, cond24, 0);
        testResult(pageablePage1Size10, cond32, 0);


    }

    private void testResult(Pageable pageable, RecommendationReplySearchCond cond, int count) {
        Page<RecommendationReply> result = recommendationReplyJpaRepository.findAllInfo(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }
}
