package shop.wesellbuy.secondproject.web.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * SelfPicture dto
 * writer : 이호진
 * init : 2023.01.26
 * updated by writer :
 * update :
 * description : 서버에서 받은 회원 이미지 정보를 담아둔다.
 */
@Getter @Setter
@AllArgsConstructor
public class SelfPictureForm {

    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // DB에 저장된 파일 이름
}
