package shop.wesellbuy.secondproject.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 전화번호
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 회원 전화번호를 정의한다.
 */
@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phone {

    @Column(length = 13)
    private String selfPhone; // 휴대전화(필수)
    @Column(length = 13)
    private String homePhone; // 집전화(선택)


    // ** 생성 메서드  ** //
    public static Phone createPhone(String selfPhone, String homePhone) {
        Phone phone = new Phone(selfPhone, homePhone);
        return phone;
    }

}
