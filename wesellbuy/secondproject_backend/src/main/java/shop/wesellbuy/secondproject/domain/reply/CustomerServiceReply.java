package shop.wesellbuy.secondproject.domain.reply;

import jakarta.persistence.*;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.CustomerService;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.board.BoardStatus;
import shop.wesellbuy.secondproject.domain.common.BaseDateColumnEntity;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;
import shop.wesellbuy.secondproject.web.reply.ReplyUpdateForm;

/**
 * 고객지원 board 댓글
 * writer : 이호진
 * init : 2023.01.15
 * updated by writer :
 * update :
 * description : 고객지원 게시판 댓글 정의한다.
 */
@Entity
@Getter
public class CustomerServiceReply extends BaseDateColumnEntity {

    @Id @GeneratedValue
    @Column(name = "customerServiceReply_num")
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
    @JoinColumn(name = "customerService_num")
    private CustomerService customerService;

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
        member.getCustomerServiceReplyList().add(this);
    }

    // CustomerService
    public void addCustomerService(CustomerService customerService) {
        this.customerService = customerService;
        customerService.getCustomerServiceReplyList().add(this);
    }

    // ** 생성 메서드 ** //
    public static CustomerServiceReply createCustomerServiceReply(ReplyForm replyForm, Member member, CustomerService customerService) {

        CustomerServiceReply customerServiceReply = new CustomerServiceReply();

        customerServiceReply.addContent(replyForm.getContent());
        customerServiceReply.addStatus(ReplyStatus.R);
        customerServiceReply.addMember(member);
        customerServiceReply.addCustomerService(customerService);

        return customerServiceReply;
    }

    // ** 비즈니스 메서드 ** //
    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 고객지원글 댓글 정보 수정
     */
    public void updateCustomerServiceReply(ReplyUpdateForm updateReplyForm) {
        this.content = updateReplyForm.getContent();
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 상품 댓글 정보 삭제
     *               -> status를 R -> D로 변경
     */
    public void delete() {
        this.status = ReplyStatus.D;
    }
}
