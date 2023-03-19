package shop.wesellbuy.secondproject.domain.reply;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.CustomerService;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.Recommendation;
import shop.wesellbuy.secondproject.domain.common.BaseDateColumnEntity;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;
import shop.wesellbuy.secondproject.web.reply.ReplyUpdateForm;

/**
 * 추천합니다 board 댓글
 * writer : 이호진
 * init : 2023.01.15
 * updated by writer :
 * update :
 * description : 추천합니다. 게시판 댓글 정의한다.
 */
@Entity
@Getter
public class RecommendationReply extends BaseDateColumnEntity {

    @Id
    @GeneratedValue
    @Column(name = "recommendationReply_num")
    private Integer num; // 댓글 번호
    @Column(length = 10000)
    private String content; // 내용
    @Column(length = 5)
    @Enumerated(value = EnumType.STRING)
    private ReplyStatus status; // 게시판 댓글 상태[REGISTER/DELETE]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_num")
    private Recommendation recommendation;

    // ** setter ** //
    public void addContent(String content) {
        this.content = content;
    }

    public void addStatus(ReplyStatus status) {
        this.status = status;
    }

    // ** 연관관계 메서드 ** //
    // Member
    public void addMember(Member member) {
        this.member = member;
        member.getRecommendationReplyList().add(this);
    }

    // Recommendation
    public void addRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
        recommendation.getRecommendationReplyList().add(this);
    }

    // ** 생성 메서드 ** //
    public static RecommendationReply createRecommendationReply(ReplyForm replyForm, Member member, Recommendation Recommendation) {

        RecommendationReply recommendationReply = new RecommendationReply();

        recommendationReply.addContent(replyForm.getContent());
        recommendationReply.addStatus(ReplyStatus.R);
        recommendationReply.addMember(member);
        recommendationReply.addRecommendation(Recommendation);

        return recommendationReply;
    }

    // ** 비즈니스 메서드 ** //
    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 추천합니다글 댓글 정보 수정
     */
    public void updateRecommendationReply(ReplyUpdateForm updateReplyForm) {
        this.content = updateReplyForm.getContent();
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 삭제
     *               status를 변경한다.(R -> D)
     */
    public void delete() {
        this.status = ReplyStatus.D;
    }
}
