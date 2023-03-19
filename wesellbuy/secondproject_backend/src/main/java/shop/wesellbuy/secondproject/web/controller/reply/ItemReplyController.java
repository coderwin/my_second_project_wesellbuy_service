package shop.wesellbuy.secondproject.web.controller.reply;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.wesellbuy.secondproject.repository.reply.item.ItemReplySearchCond;
import shop.wesellbuy.secondproject.service.reply.item.ItemReplyService;
import shop.wesellbuy.secondproject.util.SessionConst;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberSessionForm;
import shop.wesellbuy.secondproject.web.reply.ReplyDetailForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;
import shop.wesellbuy.secondproject.web.reply.ReplyUpdateForm;
import shop.wesellbuy.secondproject.web.resultBox.Result;
import shop.wesellbuy.secondproject.web.resultBox.ResultV2;

/**
 * ItemReply Controller
 * writer : 이호진
 * init : 2023.02.13
 * updated by writer :
 * update :
 * description : ItemReply RestController 메소드 모음
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemReplyController {

    private final ItemReplyService itemReplyService;

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 댓글 저장
     */
    @PostMapping("/{boardNum}/replies")
    @ApiOperation(value = "상품 댓글 등록")
    public ResponseEntity<ResultV2<String>> save(@RequestBody ReplyForm form,
                                  @PathVariable int boardNum,
                                  @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberSessionForm sessionForm ) {
        log.info("form : {}", form);
        // 댓글 등록
        itemReplyService.save(form, sessionForm.getNum(), boardNum);
        // code 201 보내기
        String successMsg = "등록 성공";
        ResultV2<String> body = new ResultV2<>(successMsg, boardNum);

        return new ResponseEntity(body, HttpStatus.CREATED);
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 댓글 수정
     */
    @PutMapping("/{boardNum}/replies/{replyNum}")
    @ApiOperation(value = "상품 댓글 수정")
    public ResponseEntity<Result<String>> update(@RequestBody ReplyUpdateForm form,
                                                 @PathVariable int boardNum,
                                                 @PathVariable int replyNum) {
        log.info("resplyUpdateForm form 1 : {}", form);
        // replyNum 입력하기
        form.addNum(replyNum);
        log.info("resplyUpdateForm form 2 : {}", form);
        // 댓글 수정하기
        itemReplyService.update(form);
        // responseEntity body 생성
        String successMsg = "수정 완료";
        Result<String> body = new Result<>(successMsg);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 댓글 삭제
     */
    @DeleteMapping("/{boardNum}/replies/{replyNum}")
    @ApiOperation(value = "상품 댓글 삭제")
    public ResponseEntity<ResultV2<String>> delete(@PathVariable int replyNum, @PathVariable int boardNum) {
        // 댓글 삭제하기
        itemReplyService.delete(replyNum);
        log.info("댓글삭제 성공 -> item reply Num : {}", replyNum);
        // responseEntity body 생성
        String successMsg = "삭제 완료";
        ResultV2<String> body = new ResultV2<>(successMsg, boardNum);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    //    -------------------------methods using for admin start----------------------------------
    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 모두 불러오기
     *               -> admin이 사용
     *
     * comment : 쿼리스트링으로 넘어오는
     *           > 검색 데이터 memberId, content, createDate를 받고
     *           > 페이징 데이터 size, page를 받는다.
     */
    @GetMapping("/replies")
    @ApiOperation(value = "상품 댓글 목록")
    public Result<Page<ReplyDetailForm>> selectListForAdmin(ItemReplySearchCond cond, Pageable pageable) {
        log.info("ItemReplySearchCond cond : {}", cond);
        // 목록 검색하기
        Page<ReplyDetailForm> pageForm = itemReplyService.selectListForAdmin(cond, pageable);
        // Result 객체 생성하기
        Result<Page<ReplyDetailForm>> result = new Result(pageForm);

        return result;
    }

    //    -------------------------methods using for admin end----------------------------------






}
