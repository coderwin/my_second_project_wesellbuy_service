package shop.wesellbuy.secondproject.web.customerservice;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 고객지원글 dto
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : 클라이언트에게서 받은 고객지원글 정보를 담아둔다.
 *
 * update : > @NoArgsConstructor 추가
 *          > @NotBlank 추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerServiceForm {

    @NotBlank(message = "신고 아이디를 입력해주세요")
    private String reportedId; // 신고된 회원 아이디
    @NotBlank(message = "신고 내용을 입력해주세요")
    private String content; // 신고 내용
}
