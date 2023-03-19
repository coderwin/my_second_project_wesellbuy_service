package shop.wesellbuy.secondproject.domain.member;

import jakarta.persistence.*;
import lombok.*;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.common.BaseDateColumnEntity;

/**
 * 회원 이미지 정보
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 회원 이미지 정보를 정의한다.
 */
@Entity
@Getter
public class SelfPicture extends BaseDateColumnEntity {

    @Id @GeneratedValue
    @Column(name = "selfPicture_id")
    private Integer num; // 이미지 번호
    @Column(length = 300)
    private String originalFileName; // 원본 파일 이름
    @Column(length = 300)
    private String storedFileName; // DB에 저장된 파일 이름

    // cascade => 그림 수정시에도 member가 persist 되어야한다.
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "selfPicture", cascade = CascadeType.ALL)
    private Member member; // 회원 번호

    // ** setter ** //

    public void addOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void addStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    // ** 생성 메서드 ** //
    public static SelfPicture createSelfPicture(String originalFileName, String storedFileName) {
        SelfPicture selfPicture = new SelfPicture();

        selfPicture.addOriginalFileName(originalFileName);
        selfPicture.addStoredFileName(storedFileName);

        return selfPicture;
    }

}
