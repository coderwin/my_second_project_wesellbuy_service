package shop.wesellbuy.secondproject.repository.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * search Member password by condition dto
 * writer : 이호진
 * init : 2023.01.17
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : 회원의 비밀번호를 찾을 때 필요한 dto
 *
 * update : @NoArgsConstructor추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchPwdCond {

    private String id; // 회원 아이디
    private String selfPhone; // 회원 휴대폰번호
    private String email; // 회원 이메일
}
