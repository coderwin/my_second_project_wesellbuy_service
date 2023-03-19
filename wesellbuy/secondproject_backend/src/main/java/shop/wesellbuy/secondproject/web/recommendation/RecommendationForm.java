package shop.wesellbuy.secondproject.web.recommendation;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture;

import java.util.ArrayList;
import java.util.List;

/**
 * 추천합니다글 저장 form dto
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : 클라이언트가 보내온 추천합니다글 저장 정보를 담아둔다.
 *
 * update : > files 필드 추가
 *          > 생성자 추가(files 뺀)
 *          > @NoArgsConstructor 추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RecommendationForm {

    @NotBlank(message = "비어있을 수 없습니다.")
    private String itemName; // 추천받은 상품 이름
    @NotBlank(message = "비어있을 수 없습니다.")
    private String sellerId; // 추천받은 판매자 아이디
    @NotBlank(message = "비어있을 수 없습니다.")
    private String content; // 추천 이유
    private List<RecommendationPicture> recommendationPictureList;
    private List<MultipartFile> files;

    // 생성자
    public RecommendationForm(String itemName, String sellerId, String content, List<RecommendationPicture> recommendationPictureList) {
        this.itemName = itemName;
        this.sellerId = sellerId;
        this.content = content;
        this.recommendationPictureList = recommendationPictureList;
    }


    // ** 비즈니스 로직 ** //

    public void addRecommendationPictureList(List<RecommendationPicture> recommendationPictureList) {
        this.recommendationPictureList = recommendationPictureList;
    }
}
