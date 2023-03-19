package shop.wesellbuy.secondproject.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import shop.wesellbuy.secondproject.domain.board.BoardStatus;
import shop.wesellbuy.secondproject.domain.common.BaseDateColumnEntity;
import shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;
import shop.wesellbuy.secondproject.web.recommendation.RecommendationForm;
import shop.wesellbuy.secondproject.web.recommendation.RecommendationUpdateForm;

import java.util.ArrayList;
import java.util.List;

/**
 * 추천합니다 board
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : 추천합니다 게시판을 정의한다.
 *
 * comment - create : 연관관계 메서드 수정(recommendationPicture)
 *                    -> FK가 있는 곳에서 연관관계 메서드 생성
 *
 *         - update : BaordStatus 추가
 *                    OneToMany 필드를 불러올 때 순서 정하기
 *                    > @OrderBy 사용
 *                    > @OrderBy(value = "num DESC")로 교체
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation extends BaseDateColumnEntity {

    @Id @GeneratedValue
    @Column(name = "recommendation_num")
    private Integer num;
    @Column(length = 200)
    private String itemName; // 추천받은 상품 이름
    @Column(length = 50)
    private String sellerId; // 추천받은 판매자 이름
    @Column(length = 10000)
    private String content; // 추천 이유
    @ColumnDefault("0")
    private Integer hits; // 조회수
    @Column(length = 5)
    @Enumerated(EnumType.STRING)
    private BoardStatus status; // 게시판 등록 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num")
    private Member member; // 회원 아이디

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.PERSIST) // 생명주기 같다
    private List<RecommendationPicture> recommendationPictureList = new ArrayList<>();

    @OneToMany(mappedBy = "recommendation")
    @OrderBy(value = "num DESC")
    private List<RecommendationReply> recommendationReplyList = new ArrayList<>(); // 댓글 모음

    // ** setter ** //
    public void addItemName(String itemName) {
        this.itemName = itemName;
    }

    public void addSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void addContent(String content) {
        this.content = content;
    }

    public void addStatus(BoardStatus status) {
        this.status = status;
    }

    // ** 연관관계 메서드 ** //
    // Member
    public void addMember(Member member) {
        this.member = member;
        member.getRecommendationList().add(this);
    }

    // RecommendationPicture
//    public void addRecommendationPicture(RecommendationPicture recommendationPicture) {
//        recommendationPicture.addRecommendation(this);
//        this.recommendationPictureList.add(recommendationPicture);
//    }

    // ** 생성 메서드 ** //
    public static Recommendation createRecommendation(RecommendationForm recommendationForm, Member member) {
        Recommendation recommendation = new Recommendation();

        recommendation.addItemName(recommendationForm.getItemName());
        recommendation.addSellerId(recommendationForm.getSellerId());
        recommendation.addContent(recommendationForm.getContent());
        recommendation.addStatus(BoardStatus.R);
        recommendation.addMember(member);

        // RecommendationPicture에 Recommendation 등록
        if(recommendationForm.getRecommendationPictureList() != null) {
            recommendationForm.getRecommendationPictureList()
                    .forEach((rp) -> rp.addRecommendation(recommendation));
        }
        return recommendation;
    }

    // ** 비즈니스(서비스) 로직(메서드) ** //

    /**
     * 조회수 default 정하기
     * - 조회수의 기본값을 db에 저장한다.
     */
    @PrePersist
    public void prePersistHits() {
        this.hits = this.hits == null ? 0 : this.hits;
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 조회수 1 증가시킨다.
     */
    public void changeHits() {
        this.hits += 1;
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 정보 수정
     */
    public void updateRecommendation(RecommendationUpdateForm updateForm, List<RecommendationPicture> pictures) {

        this.itemName = updateForm.getItemName();
        this.sellerId = updateForm.getSellerId();
        this.content = updateForm.getContent();
        // 사진 추가하기
        // RecommendationPicture에 Recommendation 등록
        // 연관관계 생각
        if(pictures != null) {
            pictures.forEach((p) -> p.addRecommendation(this));
        }
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 추천합니다글 상태 수정(R -> D)
     */
    public void changeStatus() {
        this.status = BoardStatus.D;
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 추천합니다글 이미지 삭제
     *               -> pictureSatus를 수정(R -> D)
     */
    public void deletePicture(int pictureNum) {
        // pictureNum에 맞는 pictures 불러오기
        RecommendationPicture findPicture = recommendationPictureList.stream()
                .filter(p -> p.getNum() == pictureNum)
                .findFirst()
                .orElseThrow();
        // picture 상태변경 하기
        findPicture.changeStatus();
    }
}
