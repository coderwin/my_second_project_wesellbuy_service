package shop.wesellbuy.secondproject.repository.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Recommendation findAll for condition dto
 * writer : 이호진
 * init : 2023.01.18
 * updated by writer :
 * update :
 * description : Recommendation finaAll에 사용되는 where 절의 조건 데이터 모음
 */
@Getter
@AllArgsConstructor
public class RecommendationSearchCond {

    private String itemName; // 추천받은 상품 이름
    private String sellerId; // 추천받은 판매자 이름
    private String memberId; // 작성자 아이디
    private String createDate; // 작성날짜
}
