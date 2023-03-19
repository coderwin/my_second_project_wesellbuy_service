package shop.wesellbuy.secondproject.domain.recommendation;

import jakarta.persistence.*;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.Recommendation;
import shop.wesellbuy.secondproject.domain.common.BaseDateColumnEntity;
import shop.wesellbuy.secondproject.domain.common.PictureStatus;

/**
 * 추천합니다 board 이미지 entity
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer : 이호진
 * update : 2023.02.01
 * description : 추천합니다 게시판의 이미지 정보를 정의한다.
 *
 * update : status 추가
 */
@Entity
@Getter
public class RecommendationPicture extends BaseDateColumnEntity {

    @Id
    @GeneratedValue
    @Column(name = "RecommendationPicture_id")
    private Integer num; // 이미지 번호
    @Column(length = 300)
    private String originalFileName; // 원본 파일 이름
    @Column(length = 300)
    private String storedFileName; // DB에 저장된 파일 이름
    @Column(length = 5)
    @Enumerated(EnumType.STRING)
    private PictureStatus status; // 이미지 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_num")
    private Recommendation recommendation; // 추천합니다 번호

    // ** setter ** //

    public void addOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void addStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public void addStatus(PictureStatus status) {
        this.status = status;
    }

    // ** 연관관계 메서드 ** //
    public void addRecommendation(Recommendation recommendation) {

        this.recommendation = recommendation;
        recommendation.getRecommendationPictureList().add(this);
    }


    // ** 생성 메서드 ** //
    public static RecommendationPicture createRecommendationPicture(String originalFileName, String storedFileName) {
        RecommendationPicture recommendationPicture = new RecommendationPicture();

        recommendationPicture.addOriginalFileName(originalFileName);
        recommendationPicture.addStoredFileName(storedFileName);
        recommendationPicture.addStatus(PictureStatus.R);
        return recommendationPicture;
    }

    // ** 비즈니스 메서드 ** //
    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : status를 수정(R -> D)
     */
    public void changeStatus() {
        status = PictureStatus.D;
    }

}
