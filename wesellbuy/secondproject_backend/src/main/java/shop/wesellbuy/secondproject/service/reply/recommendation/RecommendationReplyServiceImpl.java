package shop.wesellbuy.secondproject.service.reply.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.Recommendation;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.recommendation.RecommendationJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.recommendation.RecommendationReplyJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.recommendation.RecommendationReplySearchCond;
import shop.wesellbuy.secondproject.web.reply.ReplyDetailForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;
import shop.wesellbuy.secondproject.web.reply.ReplyUpdateForm;

/**
 * RecommendationReply Service 구현 클래스
 * writer : 이호진
 * init : 2023.02.01
 * updated by writer :
 * update :
 * description : RecommendationReply Service 구현 메소드 모음
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RecommendationReplyServiceImpl implements RecommendationReplyService {

    private final MemberJpaRepository memberJpaRepository;
    private final RecommendationJpaRepository recommendationJpaRepository;
    private final RecommendationReplyJpaRepository recommendationReplyJpaRepository;

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 저장
     */
    @Override
    @Transactional
    public int save(ReplyForm replyForm, int memberNum, int recommendationNum) {
        // member 불러오기
        Member member = memberJpaRepository.findById(memberNum).orElseThrow();
        // item 불러오기
        Recommendation recommendation = recommendationJpaRepository.findById(recommendationNum).orElseThrow();
        // 댓글 생성
        RecommendationReply recommendationReply = RecommendationReply.createRecommendationReply(replyForm, member, recommendation);
        // 댓글 저장하기
        recommendationReplyJpaRepository.save(recommendationReply);

        return recommendationReply.getNum();
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 수정
     */
    @Override
    @Transactional
    public void update(ReplyUpdateForm updateReplyForm) {
        // 댓글 불러오기
        RecommendationReply findRecommendationReply = recommendationReplyJpaRepository.findById(updateReplyForm.getNum()).orElseThrow();
        // 댓글 수정
        findRecommendationReply.updateRecommendationReply(updateReplyForm);
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 삭제
     *               status를 변경한다.(R -> D)
     */
    @Override
    @Transactional
    public void delete(int num) {
        // 댓글 불러오기
        RecommendationReply recommendationReply = recommendationReplyJpaRepository.findById(num).orElseThrow();
        // 댓글 삭제하기
        // status를 변경한다.(R -> D)
        recommendationReply.delete();
    }

//    -------------------------methods using for admin start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 모두 불러오기
     *               -> admin이 사용
     */
    @Override
    public Page<ReplyDetailForm> selectListForAdmin(RecommendationReplySearchCond cond, Pageable pageable) {
        // 모두 불러오기
        Page<RecommendationReply> recommendationReplyPage = recommendationReplyJpaRepository.findAllInfo(cond, pageable);
        // Page<ReplyDetailForm> 객체로 만들기
        // 모든 상태 포함
        Page<ReplyDetailForm> result = recommendationReplyPage.map(r -> ReplyDetailForm.create(r));

        return result;
    }

//    -------------------------methods using for admin admin----------------------------------







}
