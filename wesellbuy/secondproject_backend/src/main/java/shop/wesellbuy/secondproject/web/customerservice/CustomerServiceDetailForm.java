package shop.wesellbuy.secondproject.web.customerservice;

import lombok.Getter;
import lombok.Setter;
import shop.wesellbuy.secondproject.domain.CustomerService;
import shop.wesellbuy.secondproject.domain.reply.ReplyStatus;
import shop.wesellbuy.secondproject.web.reply.ReplyDetailForm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 고객지원글 상세보기 dto
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : 고객지원글 상세정보를 담아둔다.
 *
 * update : 댓글이 없을 때 if() 조건 추가
 */
@Getter @Setter
public class CustomerServiceDetailForm {

    private Integer num; // 게시글 번호
    private String reportedId; // 신고된 회원 아이디
    private String content; // 신고 내용
    private String memberId; // 회원 아이디
    private LocalDateTime createDate; // 작성 날짜
    private List<ReplyDetailForm> replyList = new ArrayList<>(); // 댓글 모음

    // ** 생성 메서드 ** //
    public static CustomerServiceDetailForm createCustomerServiceDetailForm(CustomerService customerService) {
        // 댓글 가져와서 ReplyDetailForm으로 만들어주기
        CustomerServiceDetailForm customerServiceDetailForm = new CustomerServiceDetailForm();

        customerServiceDetailForm.setNum(customerService.getNum());
        customerServiceDetailForm.setReportedId(customerService.getReportedId());
        customerServiceDetailForm.setContent(customerService.getContent());
        customerServiceDetailForm.setMemberId(customerService.getMember().getId());
        customerServiceDetailForm.setCreateDate(customerService.getCreatedDate());
        // 댓글을 replyList에 넣어주기
        customerServiceDetailForm.setReplyList(
                customerService.getCustomerServiceReplyList().stream()
                        .filter(r -> r.getStatus().equals(ReplyStatus.R))
                        .map(r -> ReplyDetailForm.createReplyDetailForm(r))
                        .collect(toList()));


        return customerServiceDetailForm;
    }
}
