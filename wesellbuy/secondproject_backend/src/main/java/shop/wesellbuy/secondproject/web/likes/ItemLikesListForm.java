package shop.wesellbuy.secondproject.web.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.likes.ItemLikes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * itemLikes data 저장 dto
 * writer : 이호진
 * init : 2023.01.27
 * updated by writer :
 * update :
 * description : 모든 좋아요 num 정보 담는다.
 */
@Getter
@AllArgsConstructor
public class ItemLikesListForm {

    private int num; // 좋아요 번호

    // ** 생성 메서드 ** //
    public static ItemLikesListForm create(int num) {
        ItemLikesListForm itemLikesListForm = new ItemLikesListForm(num);

        return itemLikesListForm;
    }
}
