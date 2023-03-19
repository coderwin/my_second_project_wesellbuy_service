package shop.wesellbuy.secondproject.web.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.common.PictureStatus;
import shop.wesellbuy.secondproject.domain.item.Book;
import shop.wesellbuy.secondproject.domain.item.Furniture;
import shop.wesellbuy.secondproject.domain.item.HomeAppliances;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;
import shop.wesellbuy.secondproject.domain.reply.ReplyStatus;
import shop.wesellbuy.secondproject.web.reply.ReplyDetailForm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 상품 상세보기 dto
 * writer : 이호진
 * init : 2023.02.02
 * updated by writer :
 * update :
 * description : 서버에서 받은 상품 상세정보를 담아둔다.
 */
@Getter
@AllArgsConstructor
public class ItemDetailForm {

    private Integer num; // 상품 번호
    private String name; // 상품명
    private Integer stock; // 제고 수량
    private Integer price; // 가격
    private String content; // 설명
    private String type; // 상풍종류 설정
    private Integer hits; // 조회수
    private String memberId; // 상품 등록(판매자) 아이디
    private Integer likes; // 좋아요수
    private List<ItemPictureForm> pictureForms; // 이미지 모음
    private List<ReplyDetailForm> replyFormList; // 댓글 모음

    // Book에 필요
    private String author; // 저자
    private String publisher; // 출판사

    // Furniture, HomeAppliances에 필요
    private String company;// 제조회사 이름

    // ** 생성 메서드 ** //
    // 기타(Item)
    public static ItemDetailForm create(Item item) {
        ItemDetailForm form = new ItemDetailForm(
                item.getNum(),
                item.getName(),
                item.getStock(),
                item.getPrice(),
                item.getContent(),
                item.getDtype(),
                item.getHits(),
                item.getMember().getId(),
                item.getItemLikesList().size(),
                item.getItemPictureList().stream()
                        .filter(p -> p.getStatus().equals(PictureStatus.R))
                        .map(p -> ItemPictureForm.create(p))
                        .collect(toList()),
                item.getItemReplyList().stream()
                        .filter(r -> r.getStatus().equals(ReplyStatus.R))
                        .map(r -> ReplyDetailForm.createReplyDetailForm(r))
                        .collect(toList()),
                "",
                "",
                ""
        );

        return form;
    }

    // Book
    public static ItemDetailForm create(Book book) {
        ItemDetailForm form = new ItemDetailForm(
                book.getNum(),
                book.getName(),
                book.getStock(),
                book.getPrice(),
                book.getContent(),
                book.getDtype(),
                book.getHits(),
                book.getMember().getId(),
                book.getItemLikesList().size(),
                book.getItemPictureList().stream()
                        .filter(p -> p.getStatus().equals(PictureStatus.R))
                        .map(p -> ItemPictureForm.create(p))
                        .collect(toList()),
                book.getItemReplyList().stream()
                        .filter(r -> r.getStatus().equals(ReplyStatus.R))
                        .map(r -> ReplyDetailForm.createReplyDetailForm(r))
                        .collect(toList()),
                book.getAuthor(),
                book.getPublisher(),
                ""
        );

        return form;
    }

    // Furniture
    public static ItemDetailForm create(Furniture furniture) {
        ItemDetailForm form = new ItemDetailForm(
                furniture.getNum(),
                furniture.getName(),
                furniture.getStock(),
                furniture.getPrice(),
                furniture.getContent(),
                furniture.getDtype(),
                furniture.getHits(),
                furniture.getMember().getId(),
                furniture.getItemLikesList().size(),
                furniture.getItemPictureList().stream()
                        .filter(p -> p.getStatus().equals(PictureStatus.R))
                        .map(p -> ItemPictureForm.create(p))
                        .collect(toList()),
                furniture.getItemReplyList().stream()
                        .filter(r -> r.getStatus().equals(ReplyStatus.R))
                        .map(r -> ReplyDetailForm.createReplyDetailForm(r))
                        .collect(toList()),
                "",
                "",
                furniture.getCompany()
        );

        return form;
    }

    // HomeAppliances
    public static ItemDetailForm create(HomeAppliances homeAppliances) {
        ItemDetailForm form = new ItemDetailForm(
                homeAppliances.getNum(),
                homeAppliances.getName(),
                homeAppliances.getStock(),
                homeAppliances.getPrice(),
                homeAppliances.getContent(),
                homeAppliances.getDtype(),
                homeAppliances.getHits(),
                homeAppliances.getMember().getId(),
                homeAppliances.getItemLikesList().size(),
                homeAppliances.getItemPictureList().stream()
                        .filter(p -> p.getStatus().equals(PictureStatus.R))
                        .map(p -> ItemPictureForm.create(p))
                        .collect(toList()),
                homeAppliances.getItemReplyList().stream()
                        .filter(r -> r.getStatus().equals(ReplyStatus.R))
                        .map(r -> ReplyDetailForm.createReplyDetailForm(r))
                        .collect(toList()),
                "",
                "",
                homeAppliances.getCompany()
        );

        return form;
    }
}
