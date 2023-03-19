package shop.wesellbuy.secondproject.web.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import shop.wesellbuy.secondproject.domain.Recommendation;
import shop.wesellbuy.secondproject.domain.common.PictureStatus;
import shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;
import shop.wesellbuy.secondproject.domain.reply.ReplyStatus;
import shop.wesellbuy.secondproject.web.reply.ReplyDetailForm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 추천합니다글 상세보기 dto
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer :
 * update :
 * description : 추천합니다글 상세정보를 담아둔다.
 */
@Getter
@AllArgsConstructor
public class RecommendationDetailForm {

    private Integer num; // 게시글 번호
    private String itemName; // 추천받은 상품 이름
    private String sellerId; // 추천받은 판매자 이름
    private String content; // 추천 이유
    private Integer hits; // 조회수
    private String memberId; // 작정사 id
    private LocalDateTime createDate; // 작성 날짜
    private List<RecommendationPictureForm> recommendationPictureFormList; // 게시글 이미지 모음
    private List<ReplyDetailForm> replyFormList; // 게시글 댓글 모음

    // ** 생성 메서드 ** //
    public static RecommendationDetailForm create(Recommendation recommendation) {
        RecommendationDetailForm recommendationDetailForm = new RecommendationDetailForm(
                recommendation.getNum(),
                recommendation.getItemName(),
                recommendation.getSellerId(),
                recommendation.getContent(),
                recommendation.getHits(),
                recommendation.getMember().getId(),
                recommendation.getCreatedDate(),
                recommendation.getRecommendationPictureList().stream()
                        .filter(p -> p.getStatus().equals(PictureStatus.R))
                        .map(p -> RecommendationPictureForm.create(p))
                        .collect(toList()),
                recommendation.getRecommendationReplyList().stream()
                        .filter(r -> r.getStatus().equals(ReplyStatus.R))
                        .map(r -> ReplyDetailForm.createReplyDetailForm(r))
                        .collect(toList())
        );

        return recommendationDetailForm;
    }
}
