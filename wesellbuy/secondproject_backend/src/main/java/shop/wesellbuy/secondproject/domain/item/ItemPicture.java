package shop.wesellbuy.secondproject.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.common.BaseDateColumnEntity;
import shop.wesellbuy.secondproject.domain.common.PictureStatus;

/**
 * 상품 board 이미지 entity
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer : 이호진
 * update : 2023.02.02
 * description : 상품 게시판의 이미지 정보를 정의한다.
 *
 * update : status 추가
 */
@Entity
@Getter
public class ItemPicture extends BaseDateColumnEntity {

    @Id
    @GeneratedValue
    @Column(name = "itemPicture_id")
    private Integer num; // 이미지 번호
    @Column(length = 300)
    private String originalFileName; // 원본 파일 이름
    @Column(length = 300)
    private String storedFileName; // DB에 저장된 파일 이름
    @Column(length = 5)
    @Enumerated(EnumType.STRING)
    private PictureStatus status; // 이미지 상태
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_num")
    private Item item; // 회원 번호

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

    public void addItem(Item item) {
        this.item = item;
    }

//    // ** 연관관계 메서드 ** //
    // Item
//    public void addItem(Item item) {
//        this.item = item;
//        item.getItemPictureList().add(this);
//    }

    // ** 생성 메서드 ** //
    public static ItemPicture createItemPicture(String originalFileName, String storedFileName) {
        ItemPicture itemPicture = new ItemPicture();

        itemPicture.addOriginalFileName(originalFileName);
        itemPicture.addStoredFileName(storedFileName);
        itemPicture.addStatus(PictureStatus.R);
        return itemPicture;
    }

    // ** 비즈니스 메서드 ** //
    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : status를 수정(R -> D)
     */
    public void changeStatus() {
        this.status = PictureStatus.D;
    }
}
