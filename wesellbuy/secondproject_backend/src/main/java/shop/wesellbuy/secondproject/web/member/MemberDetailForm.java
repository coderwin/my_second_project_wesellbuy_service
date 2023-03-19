package shop.wesellbuy.secondproject.web.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.member.Address;
import shop.wesellbuy.secondproject.domain.member.MemberStatus;
import shop.wesellbuy.secondproject.domain.member.Phone;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;

import java.time.LocalDateTime;

/**
 * 회원 정보 상세보기 dto
 * writer : 이호진
 * init : 2023.01.26
 * updated by writer :
 * update :
 * description : 회원의 상세정보를 담아둔다.
 */
@Getter @Setter
@AllArgsConstructor
public class MemberDetailForm {

    private int num; // 회원 번호
    private String id; // 아이디
    private String name; // 이름
    private String email; // 이메일
    private Phone phones; // 휴대전화 + 집전화
    private Address address; // 주소
    private MemberStatus status; // 상태가 탈퇴면 lastModifiedDate의 시간이 탈퇴 날짜이다.
    private LocalDateTime createDate; // 회원 가입 날짜

    private SelfPictureForm selfPictureForm; // 이미지

    // ** 생성 메서드 ** //
    public static MemberDetailForm createMemberDetailForm(Member member) {

        // SelfPictureForm 생성하기
        SelfPictureForm selfPictureForm = null;
        if(member.getSelfPicture() != null) {
            selfPictureForm = new SelfPictureForm(member.getSelfPicture().getOriginalFileName(), member.getSelfPicture().getStoredFileName());
        }

        // MemberDetailForm 생성
        return new MemberDetailForm(
                member.getNum(),
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhones(),
                member.getAddress(),
                member.getStatus(),
                member.getCreatedDate(),
                selfPictureForm);
    }

}
