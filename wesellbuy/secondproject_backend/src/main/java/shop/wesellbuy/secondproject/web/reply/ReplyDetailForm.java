package shop.wesellbuy.secondproject.web.reply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.wesellbuy.secondproject.domain.Recommendation;
import shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply;
import shop.wesellbuy.secondproject.domain.reply.ItemReply;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;
import shop.wesellbuy.secondproject.domain.reply.ReplyStatus;

import java.time.LocalDateTime;

/**
 * board 댓글 상세보기 dto
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer :
 * update :
 * description : 서버에 있는 댓글 내용 정보를 담아둔다.
 */
@Getter
@Setter
@AllArgsConstructor
public class ReplyDetailForm {

    private Integer num; // 댓글 번호
    private String content; // 내용
    private String memberId; // 댓글 작성자 아이디
    private LocalDateTime createDate; // 작성 날짜
    private ReplyStatus status; // 댓글 상태
    // admmin 이용
    private Integer boardNum; // 게시글 번호


    // ** 생성자 ** //
    public ReplyDetailForm(Integer num, String content, String memberId, LocalDateTime createDate) {
        this.num = num;
        this.content = content;
        this.memberId = memberId;
        this.createDate = createDate;
    }

    // ** 생성 메서드 ** //
    // n + 1문제 발생하는데(reply에서 member를 조회하는 구나)
    //  -> test에서 확인 결과
    //      -> n + 1 문제 발생 안함(in query로 해결)

    /**
     * CustomerServiceDetailForm -> Reply에 사용
     */
    public static ReplyDetailForm createReplyDetailForm(CustomerServiceReply reply) {

        ReplyDetailForm replyDetailForm = new ReplyDetailForm(
                reply.getNum(),
                reply.getContent(),
                reply.getMember().getId(),
                reply.getCreatedDate()
                );

        return replyDetailForm;
    }

    /**
     * RecommendationDetailForm -> Reply에 사용
     */
    public static ReplyDetailForm createReplyDetailForm(RecommendationReply reply) {
        ReplyDetailForm replyDetailForm = new ReplyDetailForm(
                reply.getNum(),
                reply.getContent(),
                reply.getMember().getId(),
                reply.getCreatedDate()
        );

        return replyDetailForm;
    }

    /**
     * ItemDetailForm -> Reply에 사용
     */
    public static ReplyDetailForm createReplyDetailForm(ItemReply reply) {
        ReplyDetailForm replyDetailForm = new ReplyDetailForm(
                reply.getNum(),
                reply.getContent(),
                reply.getMember().getId(),
                reply.getCreatedDate()
        );

        return replyDetailForm;
    }

//    -------------------------methods using for admin start----------------------------------

    /**
     * ItemReply에서 사용
     */
    public static ReplyDetailForm create(ItemReply reply) {

        ReplyDetailForm replyDetailForm = new ReplyDetailForm(
                reply.getNum(),
                reply.getContent(),
                reply.getMember().getId(),
                reply.getCreatedDate(),
                reply.getStatus(),
                reply.getItem().getNum()
        );

        return replyDetailForm;
    }

    /**
     * CustomerServiceReply에서 사용
     */
    public static ReplyDetailForm create(CustomerServiceReply reply) {

        ReplyDetailForm replyDetailForm = new ReplyDetailForm(
                reply.getNum(),
                reply.getContent(),
                reply.getMember().getId(),
                reply.getCreatedDate(),
                reply.getStatus(),
                reply.getCustomerService().getNum()
        );

        return replyDetailForm;
    }

    /**
     * RecommendationReply에서 사용
     */
    public static ReplyDetailForm create(RecommendationReply reply) {

        ReplyDetailForm replyDetailForm = new ReplyDetailForm(
                reply.getNum(),
                reply.getContent(),
                reply.getMember().getId(),
                reply.getCreatedDate(),
                reply.getStatus(),
                reply.getRecommendation().getNum()
        );

        return replyDetailForm;
    }


//    -------------------------methods using for admin end----------------------------------

}
