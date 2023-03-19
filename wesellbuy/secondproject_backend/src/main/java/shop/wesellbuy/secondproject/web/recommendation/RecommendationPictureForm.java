package shop.wesellbuy.secondproject.web.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture;

/**
 * RecommendationPicture dto
 * writer : 이호진
 * init : 2023.01.26
 * updated by writer :
 * update :
 * description : 서버에서 받은 추천합니다글 이미지 정보를 담아둔다.
 */
@Getter
@AllArgsConstructor
public class RecommendationPictureForm {

    private Integer num; // 파일 번호
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // DB에 저장된 파일 이름

    // ** 생성 메서드 **//
    public static RecommendationPictureForm create(RecommendationPicture recommendationPicture) {
        RecommendationPictureForm recommendationDetailForm = new RecommendationPictureForm(
                recommendationPicture.getNum(),
                recommendationPicture.getOriginalFileName(),
                recommendationPicture.getStoredFileName()
        );

        return recommendationDetailForm;
    }
}
