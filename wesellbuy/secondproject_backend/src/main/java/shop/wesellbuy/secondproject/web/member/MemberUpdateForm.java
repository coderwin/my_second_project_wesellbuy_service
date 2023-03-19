package shop.wesellbuy.secondproject.web.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.util.ValidationOfPattern;

/**
 * 회원 수정 정보 form dto
 * writer : 이호진
 * init : 2023.01.26
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : 클라이언트가 보내온 회원 수정 정보를 담아둔다.
 *
 * update : > 비밀버호 확인 filed 추가
 *          > 생성자 추가
 *          > @NoArgsContructor 추가 : controller에서 데이터 받기 위해서
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberUpdateForm {

    @NotBlank(message = "필수 입력입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[?<>~!@#$%^&*_+-])[a-z\\d?<>~!@#$%^&*_+-]{8,21}$",
            message = "영소문자, 숫자, 특수기호 포함 8~21자리를 입력하셔야 합니다."
    )
    private String pwd; // 비밀번호
    @NotBlank(message = "필수 입력입니다.")
    private String pwdConfirm; // 비밀번호 확인
    @NotBlank(message = "필수 입력입니다.")
    @Pattern(regexp = "^\\w+@[a-zA-Z\\d]+\\.[a-zA-Z\\d]+(\\.[a-zA-Z\\d]+)?$",
            message = "이메일 형식에 맞지 않습니다."
    )
    private String email; // 이메일
    @NotBlank(message = "필수 입력입니다.")
    @Pattern(regexp = "^01(0|[6-9])\\d{4}\\d{4}$", message="\'-\'없이 숫자로만 입력해주세요.")
    private String selfPhone; // 휴대전화(필수)
    private String homePhone; // 집전화(선택)
    @NotBlank(message = "국적을 선택해주세요")
    private String country; // 나라 이름
    @NotBlank(message = "지역을 선택해주세요")
    private String city; // 지역 이름
    @NotBlank(message = "동/거리명을 입력해주세요")
    private String street; // 동
    @NotBlank(message = "상세주소를 입력해주세요")
    private String detail; // 상세주소
    @NotBlank(message = "우편번호를 입력해주세요")
    private String zipcode; // 우편보호

    private MultipartFile file; // 회원 이미지

    // 생성자
    public MemberUpdateForm(String pwd, String email, String selfPhone, String homePhone, String country, String city, String street, String detail, String zipcode, MultipartFile file) {
        this.pwd = pwd;
        this.email = email;
        this.selfPhone = selfPhone;
        this.homePhone = homePhone;
        this.country = country;
        this.city = city;
        this.street = street;
        this.detail = detail;
        this.zipcode = zipcode;
        this.file = file;
    }

    // ** 비즈니스 메서드 ** //
    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 회원 정보 수정 value 입력 오류 검사
     */
    public void validateUpdateValues(BindingResult bindingResult) {

        // 비밀번호 확인 오류
        String pwd = this.getPwd();
        String pwdConfirm = this.getPwdConfirm();
        if(StringUtils.hasText(pwd) && StringUtils.hasText(pwdConfirm)) {
            if(!pwd.equals(pwdConfirm)) {
                bindingResult.rejectValue("pwdConfirm", "failed", "비밀번호가 일치하지 않습니다.");
            }
        }

        // 파일 확장자 조사
        String patternFile = ".*(?<=\\.(jpg|JPG|png|PNG|jpeg|JPEG|gif|GIF))";
        if(getFile() != null) {
            ValidationOfPattern.validateValues(patternFile, this.getFile().getOriginalFilename(), bindingResult, "file", "failed", "jpg, jpeg, png, gif 파일만 가능합니다.");
        }
    }

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 파일을 넣기
     */
    public void addFile(MultipartFile file) {
        this.file = file;
    }
}
