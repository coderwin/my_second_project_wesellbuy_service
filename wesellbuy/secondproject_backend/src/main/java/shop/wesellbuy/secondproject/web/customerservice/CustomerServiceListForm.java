package shop.wesellbuy.secondproject.web.customerservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.wesellbuy.secondproject.domain.CustomerService;

import java.time.LocalDateTime;

/**
 * 고객지원글 list dto
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer :
 * update :
 * description : 고객지원글 list를 담아둔다.
 */
@Getter @Setter
@AllArgsConstructor
public class CustomerServiceListForm {

    private Integer num; // 게시글 번호
    private String reportedId; // 신고된 회원 아이디
    private String memberId; // 회원 아이디(작성자)
    private LocalDateTime createDate; // 생성 날짜

    // ** 생성 메서드 ** //
    public static CustomerServiceListForm create(CustomerService customerService) {

        CustomerServiceListForm customerServiceListForm = new CustomerServiceListForm(
                customerService.getNum(),
                customerService.getReportedId(),
                customerService.getMember().getId(),
                customerService.getCreatedDate()
        );

        return customerServiceListForm;
    }
}
