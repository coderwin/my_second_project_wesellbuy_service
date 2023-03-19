package shop.wesellbuy.secondproject.web.item;

import lombok.Getter;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.common.PictureStatus;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;

import java.util.ArrayList;

/**
 * 상품 순위 상세보기 dto
 * writer : 이호진
 * init : 2023.02.02
 * updated by writer : 이호진
 * update : 2023.02.14
 * description : 서버로부터 받은 상품 순위 정보를 담아둔다.
 *               by 좋아요수
 *
 * update : ItemPicture -> ItemPictureForm으로 교체
 */
@Getter
public class ItemRankForm {

    private Integer num; // 상품 번호
    private String name; // 상품명
    private Integer price; // 가격
    private Integer hits; // 조회수
    private Long likes; // 좋아요수
    private Integer rank; // 순위
    private ItemPictureForm pictureForm; // 이미지 한장만

    // 있어야 하는지 생각해 볼 것들
    private String memberId; // 상품 등록 회원(판매자) 아이디

    // ** 생성자 ** //


    public ItemRankForm(Integer num, String name, Integer price, Integer hits, Long likes, ItemPictureForm pictureForm, String memberId) {
        this.num = num;
        this.name = name;
        this.price = price;
        this.hits = hits;
        this.likes = likes;
        this.rank = rank;
        this.pictureForm = pictureForm;
        this.memberId = memberId;
    }

    // ** 생성 메서드 ** //
    public static ItemRankForm create(Item item) {

        ItemRankForm form = new ItemRankForm(
                item.getNum(),
                item.getName(),
                item.getPrice(),
                item.getHits(),
                (long) item.getItemLikesList().size(),
                item.getItemPictureList().stream()
                        .filter(p -> p.getStatus().equals(PictureStatus.R))
                        .findFirst()
                        .map(p -> ItemPictureForm.create(p))
                        .orElse(null),
                item.getMember().getId()
        );

        return form;
    }

    /**
     * comment : n + 1문제 발생하지 않을까? picture 찾을 때
     *           -> test로 확인
     *              -> betch size 일어난다!
     *              -> n + 1문제 발생하지 않는다.
     */
    // Tuple을 사용시
    public static ItemRankForm create(Long likes, Item item, Member member) {

        ItemRankForm form = new ItemRankForm(
                item.getNum(),
                item.getName(),
                item.getPrice(),
                item.getHits(),
                likes,
                item.getItemPictureList().stream()
                        .filter(p -> p.getStatus().equals(PictureStatus.R))
                        .findFirst()
                        .map(p -> ItemPictureForm.create(p))
                        .orElse(null),
                member.getId()
        );

        return form;
    }

    // ** 비즈니스 메서드 ** //
    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 순위 입력.
     */
    public void addRank(Integer rank) {
        this.rank = rank;
    }
}
