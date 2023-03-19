package shop.wesellbuy.secondproject.repository.member;

import lombok.*;

/**
 * search Member id by condition dto
 * writer : 이호진
 * init : 2023.01.17
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : 회원의 아이디를 찾을 때 필요한 dto
 *
 * update : @NoArgsConstructor추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberSearchIdCond {

    private String name; // 회원 이름
    private String selfPhone; // 회원 휴대폰번호
    private String email; // 회원 이메일

}
