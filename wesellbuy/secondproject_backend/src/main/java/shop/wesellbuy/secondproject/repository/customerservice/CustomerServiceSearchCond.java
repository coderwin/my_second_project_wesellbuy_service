package shop.wesellbuy.secondproject.repository.customerservice;

import lombok.*;

/**
 * CustomerService findAll for condition dto
 * writer : 이호진
 * init : 2023.01.17
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : CustomerService finaAll에 사용되는 where 절의 조건 데이터 모음
 *
 * update : > @NoArgsConstructor 추가
 *          > addMemberId 추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CustomerServiceSearchCond {

    private String memberId; // 신고한 회원 아이디
    private String reportedId; // 신고당한 회원 아이디
    private String createDate; // create date

    // ** 비즈니스 메서드 ** //

    public void addMemberId(String memberId) {
        this.memberId = memberId;
    }
}
