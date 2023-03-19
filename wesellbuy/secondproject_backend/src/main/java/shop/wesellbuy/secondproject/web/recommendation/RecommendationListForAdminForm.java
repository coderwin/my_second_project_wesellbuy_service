package shop.wesellbuy.secondproject.web.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.Recommendation;
import shop.wesellbuy.secondproject.domain.board.BoardStatus;

/**
 * 추천합니다글 상세보기 dto
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer :
 * update :
 * description : 추천합니다글 상세정보를 담아둔다. for admin
 *
 * comment : 이미지 넣을지 생각해보기(list)
 */
@Getter
@AllArgsConstructor
public class RecommendationListForAdminForm {

    private Integer num; // 게시글 번호
    private String itemName; // 추천받은 상품 이름
    private String sellerId; // 추천받은 판매자 이름
    private String content; // 추천 이유
    private Integer hits; // 조회수
    private String memberId; // 작성자 아이디
    private BoardStatus status; // 게시글 상태

    // 이미지 넣을지 생각해보기
//    private RecommendationPictureForm picture;// 이미지

    // ** 생성 메서드 ** //
    public static RecommendationListForAdminForm create(Recommendation recommendation) {

        RecommendationListForAdminForm recommendationListForAdminForm = new RecommendationListForAdminForm(
                recommendation.getNum(),
                recommendation.getItemName(),
                recommendation.getSellerId(),
                recommendation.getContent(),
                recommendation.getHits(),
                recommendation.getMember().getId(),
                recommendation.getStatus()
        );

        return recommendationListForAdminForm;
    }
}
