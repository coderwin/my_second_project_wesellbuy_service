package shop.wesellbuy.secondproject.web.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;

import java.util.ArrayList;
import java.util.List;

/**
 * Item original dto
 * writer : 이호진
 * init : 2023.02.02
 * updated by writer : 이호진
 * update : 2023.02.13
 * description : 클라이언트에게서 받은 상품 수정 정보를 담아둔다.
 * update : @NoArgsConstructor 추가
 *          @NotBlank, @Pattern 추가
 *          files 필드 추가
 *          addNum(Integer num) 메서드 추가
 *          @Pattern(Integer type) 삭제 -> @Positive 추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemUpdateForm {

    private Integer num; // 상품 번호
    @NotBlank(message = "필수 입력입니다.")
    private String name; // 상품명
    @Positive(message = "양수만 입력 가능합니다.")
    private Integer stock; // 제고 수량
    @Positive(message = "양수만 입력 가능합니다.")
    private Integer price; // 가격
    @NotBlank(message = "필수 입력입니다.")
    private String content; // 설명
    @NotBlank(message = "필수 입력입니다.")
    @Pattern(regexp = "^B|HA|F|ITEM$", message = "책, 가구, 가전제품, 기타만 선택 가능합니다.")
    private String type; // 상풍종류 설정
    private List<ItemPicture> itemPictureList = new ArrayList<>(); // 상품 이미지 모음
    private List<MultipartFile> files; // bindingResult에 사용

    // Book에 필요
    private String author; // 저자
    private String publisher; // 출판사

    // Furniture, HomeAppliances에 필요
    private String company;// 제조회사 이름

    // 생성자
    public ItemUpdateForm(Integer num, String name, Integer stock, Integer price, String content, String type, List<ItemPicture> itemPictureList, String author, String publisher, String company) {
        this.num = num;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.content = content;
        this.type = type;
        this.itemPictureList = itemPictureList;
        this.author = author;
        this.publisher = publisher;
        this.company = company;
    }


    // ** 비즈니스 메서드 **//

    /**
     * 상품 이미지 list 담기
     */
    public void addItemPictureList(List<ItemPicture> itemPictureList) {
        this.itemPictureList = itemPictureList;
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : num 담기
     */
    public void addNum(Integer num) {
        this.num = num;
    }
}
