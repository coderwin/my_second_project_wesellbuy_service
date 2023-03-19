package shop.wesellbuy.secondproject.web.member.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 회원 아이디 찾기 dto
 * writer : 이호진
 * init : 2023.01.26
 * updated by writer :
 * update :
 * description : 회원이 찾은 아이디를 담아준다.
 */
@Getter @Setter
@AllArgsConstructor
public class LoginSearchIdResultForm {
    private String name; // 이름
    private List<String> ids; // 회원 가입 아이디 모음
}
