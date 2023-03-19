package shop.wesellbuy.secondproject.web.member.login;

import lombok.*;

/**
 * 로그인 data 저장 dto
 * writer : 이호진
 * init : 2023.01.27
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : 로그인 시도할 때, data를 담는다.
 *               + '아이디기억' 여부도 확인
 *
 * update : @NoArgsConstructor 추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LoginMemberForm {

    private String id; // 아이디
    private String pwd; // 비밀번호
    private Boolean rememberId; // '아이디 기억' 체크 박스


}
