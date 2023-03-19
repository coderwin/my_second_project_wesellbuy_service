package shop.wesellbuy.secondproject.web.member.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 비밀번호 찾기 dto
 * writer : 이호진
 * init : 2023.01.26
 * updated by writer :
 * update :
 * description : 회원이 찾은 비밀번호를 담아준다.
 */
@Getter @Setter
@AllArgsConstructor
public class LoginSearchPwdResultForm {

    private String id; // 아이디
    private String pwd; // 비밀번호
}
