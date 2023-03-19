package shop.wesellbuy.secondproject.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.web.item.FurnitureForm;
import shop.wesellbuy.secondproject.web.item.HomeAppliancesForm;
import shop.wesellbuy.secondproject.web.item.ItemUpdateForm;

@Entity
@DiscriminatorValue("F")
@Getter
public class Furniture extends Item {

    @Column(length = 200)
    private String company;// 제조회사 이름

    // ** setter ** //
    public void addCompany(String company) {
        this.company = company;
    }

    // ** 생성 메서드 ** //
    // item controller 만들 때, 나중에 다시 생각
    public static Furniture createFurniture(FurnitureForm furnitureForm, Member member) {
        Furniture furniture = new Furniture();

        furniture.addStock(furnitureForm.getStock());
        furniture.addPrice(furnitureForm.getPrice());
        furniture.addName(furnitureForm.getName());
        furniture.addContent(furnitureForm.getContent());
        furniture.addStatus(ItemStatus.R);
        furniture.addMember(member);
        furniture.addCompany(furnitureForm.getCompany());
        // 각각의 itemPicture에 item 등록
        if(furnitureForm.getItemPictureList() != null) {
            furnitureForm.getItemPictureList().forEach((ip) -> furniture.addItemPictures(ip));
//            furnitureForm.getItemPictureList().forEach((ip) -> ip.addItem(furniture));
        }
        return furniture;
    }

    // ** 비즈니스 로직(메서드) ** //

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : Furniture type 상품 정보 수정
     */
    public void update(ItemUpdateForm updateForm) {
        this.addName(updateForm.getName());
        this.addStock(updateForm.getStock());
        this.addPrice(updateForm.getPrice());
        this.addContent(updateForm.getContent());
        // 사진 추가하기
        // 연관관계 생각
        if(updateForm.getItemPictureList() != null) {
            updateForm.getItemPictureList()
                    .forEach(p -> this.addItemPictures(p));
        }
        // book
        this.addCompany(updateForm.getCompany());

    }
}
