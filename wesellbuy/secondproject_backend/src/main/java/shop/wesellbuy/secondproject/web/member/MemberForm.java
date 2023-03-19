package shop.wesellbuy.secondproject.web.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.wesellbuy.secondproject.domain.member.Address;
import shop.wesellbuy.secondproject.domain.member.Phone;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;

/**
 * 회원가입 dto
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 클라이언트에게서 받은 회원 정보를 담아둔다.
 */
@Getter @Setter
@AllArgsConstructor
public class MemberForm {

    private String name; // 이름
    private String id; // 아이디
    private String pwd; // 비밀번호
    private String email; // 이메일
    private String selfPhone; // 휴대전화(필수)
    private String homePhone; // 집전화(선택)
    private String country; // 나라 이름
    private String city; // 지역 이름
    private String street; // 동
    private String detail; // 상세주소
    private String zipcode; // 우편보호

    // member controller 만들 때, 나중에 다시 생각
    private SelfPicture selfPicture; // 이미지
}
