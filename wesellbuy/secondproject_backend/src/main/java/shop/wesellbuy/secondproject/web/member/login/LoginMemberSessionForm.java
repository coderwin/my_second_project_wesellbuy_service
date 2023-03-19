package shop.wesellbuy.secondproject.web.member.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 정보 session 저장 dto
 * writer : 이호진
 * init : 2023.01.27
 * updated by writer :
 * update :
 * description : 로그인 성공 시, 일부 회원 정보를 담는다.
 */
@Getter @Setter
@AllArgsConstructor
public class LoginMemberSessionForm {

    private int num; // 회원 등록 번호
    private String id; // 회원 아이디
    private String name; // 회원 이름

    // ** 생성 메서드 ** //
    public static LoginMemberSessionForm createLoginMemberSessionForm(int num , String id, String name) {
        return new LoginMemberSessionForm(num, id, name);
    }

}
