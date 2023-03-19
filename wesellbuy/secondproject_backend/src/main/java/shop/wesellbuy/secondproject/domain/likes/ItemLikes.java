package shop.wesellbuy.secondproject.domain.likes;

import jakarta.persistence.*;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;

/**
 * 상품(board) 좋아요
 * writer : 이호진
 * init : 2023.01.15
 * updated by writer :
 * update :
 * description : 상품(게시판) '좋아요'를 정의한다.
 *
 * comment: '등록/수정날짜 넣을지 생각해보기'
 */
@Entity
@Getter
public class ItemLikes {

    @Id @GeneratedValue
    @Column(name = "itemLikes_num")
    private Integer num; // 좋아요 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num")
    private Member member; // 회원 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_num")
    private Item item; // 상품 번호

    // ** setter ** //

    public void addMember(Member member) {
        this.member = member;
    }


    // ** 연관관계 메서드 ** //

    // Item
    public void addItem(Item item) {
        this.item = item;
        item.getItemLikesList().add(this);
    }

    // ** 생성 메서드 ** //
    public static ItemLikes createItemLikes(Member member, Item item) {
        ItemLikes itemLikes = new ItemLikes();

        itemLikes.addMember(member);
        itemLikes.addItem(item);

        return itemLikes;
    }
}
