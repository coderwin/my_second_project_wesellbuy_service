package shop.wesellbuy.secondproject.web.controller.likes;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.wesellbuy.secondproject.service.likes.ItemLikesService;
import shop.wesellbuy.secondproject.util.SessionConst;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberSessionForm;
import shop.wesellbuy.secondproject.web.resultBox.Result;

import java.util.List;

/**
 * ItemLikes Controller
 * writer : 이호진
 * init : 2023.02.13
 * updated by writer : 이호진
 * update : 2023.03.14
 * description : ItemLikes RestController 메소드 모음
 *
 * update : delete 추가(파라미터 itemNum + sessionForm)
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemLikesController {

    private final ItemLikesService itemLikesService;

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 상품 좋아요 저장
     */
    @PostMapping("/{itemNum}/likes")
    @ApiOperation(value = "좋아요 등록")
    public ResponseEntity save(@SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMemberSessionForm sessionForm,
                               @PathVariable int itemNum) {
        // 좋아요 등록
        int likesNum = itemLikesService.save(itemNum, sessionForm.getNum());
        // code 201 보내기
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    /**
//     * writer : 이호진
//     * init : 2023.02.13
//     * updated by writer :
//     * update :
//     * description : 상품 좋아요 삭제
//     */
//    @DeleteMapping("/{itemNum}/likes/{num}")
//    @ApiOperation(value = "좋아요 삭제")
//    public ResponseEntity delete(@PathVariable int num) {
//        // 좋아요 삭제
//        itemLikesService.delete(num);
//        // responseEntity 생성
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    /**
     * writer : 이호진
     * init : 2023.03.14
     * updated by writer :
     * update :
     * description : 상품 좋아요 삭제
     */
    @DeleteMapping("/{itemNum}/likes")
    @ApiOperation(value = "좋아요 삭제")
    public ResponseEntity delete(@PathVariable int itemNum,
                                 @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMemberSessionForm sessionForm) {
        // 좋아요 삭제
        itemLikesService.delete(itemNum, sessionForm.getNum());
        // responseEntity 생성
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 로그인한 회원의 모든 상품 좋아요 불러오기 by memberNum
     *               > 좋아요 색깔 표시할지 말지 결정
     */
    @GetMapping("/likes")
    @ApiOperation(value = "좋아요 목록")
    public Result<List<Integer>> selectList(@SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMemberSessionForm sessionForm) {
        // 목록 검색하기
        List<Integer> itemNumList = itemLikesService.selectListForItemList(sessionForm.getNum());
        // Result 생성하기
        Result<List<Integer>> result = new Result(itemNumList);

        return result;
    }
}
