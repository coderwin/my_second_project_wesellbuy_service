package shop.wesellbuy.secondproject.web.recommendation;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 추천합니다글 수정 form dto
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer : 이호진
 * update : 2023.02.13
 * description : 클라이언트가 보내온 추천합니다글 수정 정보를 담아둔다.
 *
 * update : > files 필드 추가
 *          > 생성자 추가(files 뺀)
 *          > addNum(Integer num) 추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class    RecommendationUpdateForm {

    private Integer num; // 게시글 번호
    @NotBlank(message = "비어있을 수 없습니다")
    private String itemName; // 추천받은 상품 이름
    @NotBlank(message = "비어있을 수 없습니다")
    private String sellerId; // 추천받은 판매자 이름
    @NotBlank(message = "비어있을 수 없습니다")
    private String content; // 추천 이유

    private List<MultipartFile> files;

    // 생성자
    public RecommendationUpdateForm(Integer num, String itemName, String sellerId, String content) {
        this.num = num;
        this.itemName = itemName;
        this.sellerId = sellerId;
        this.content = content;
    }

    // ** 비즈니스 메서드 ** //
    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : num 담기
     */
    public void addNum(Integer num) {
        this.num = num;
    }
}
