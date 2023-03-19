package shop.wesellbuy.secondproject.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.exception.ValidatedErrorsMsg;
import shop.wesellbuy.secondproject.repository.item.ItemSearchCond;
import shop.wesellbuy.secondproject.service.item.FileStoreOfItemPicture;
import shop.wesellbuy.secondproject.service.item.ItemService;
import shop.wesellbuy.secondproject.util.SessionConst;
import shop.wesellbuy.secondproject.util.ValidationOfPattern;
import shop.wesellbuy.secondproject.web.item.*;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberSessionForm;
import shop.wesellbuy.secondproject.web.resultBox.Result;
import shop.wesellbuy.secondproject.web.resultBox.ResultForItem;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Item Contoller
 * writer : 이호진
 * init : 2023.02.13
 * updated by writer :
 * update :
 * description : Item RestController 메소드 모음
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final FileStoreOfItemPicture fileStore;

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 상품 저장
     */
    @PostMapping
    @ApiOperation(value = "상품 등록")
    public ResponseEntity<?> save(@SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMemberSessionForm sessionForm,
                                  @RequestPart("data") @Validated ItemOriginalForm form,
                                  @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                  BindingResult bindingResult) throws IOException {
        // 데이터 검증하기
        if(files != null) {
            for(MultipartFile file : files) {
                // 파일 확장자 조사
                String patternFile = ".*(?<=\\.(jpg|JPG|png|PNG|jpeg|JPEG|gif|GIF))";
                if (file != null) {
                    ValidationOfPattern.validateValues(patternFile, file.getOriginalFilename(), bindingResult, "files", "failed", "jpg, jpeg, png, gif 파일만 가능합니다.");
                    // 1개라도 있으면 forEach 탈출
                    if(bindingResult.hasErrors()) {
                        break;
                    }
                }
            }
        // files 빈 List로 처리하기
        } else {
            files = new ArrayList<>();
        }
        // bindingResult에 에러 있는지 확인
        if(bindingResult.hasErrors()) {
            log.info("item save error : {}", bindingResult);

            return ValidatedErrorsMsg.makeValidatedErrorsContents(bindingResult);
        }
        /// 검증 통과
        // 상품 등록
        int itemNum = itemService.save(form, files, sessionForm.getNum());
        // code 201 보내기
        String successMsg = "상품 등록 성공";
        ResultForItem body = new ResultForItem(successMsg, itemNum);

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 상품 수정
     */
    @PutMapping("/{num}")
    @ApiOperation("상품 수정")
    public ResponseEntity<?> update(@RequestPart("data") @Validated ItemUpdateForm form,
                                    @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                    BindingResult bindingResult,
                                    @PathVariable int num) throws IOException {

        // 데이터 검증하기
        if(files != null) {
            for(MultipartFile file : files) {
                // 파일 확장자 조사
                String patternFile = ".*(?<=\\.(jpg|JPG|png|PNG|jpeg|JPEG|gif|GIF))";
                if (file != null) {
                    ValidationOfPattern.validateValues(patternFile, file.getOriginalFilename(), bindingResult, "files", "failed", "jpg, jpeg, png, gif 파일만 가능합니다.");
                    // 1개라도 있으면 forEach 탈출
                    if(bindingResult.hasErrors()) {
                        break;
                    }
                }
            }
            // files 빈 List로 처리하기
        } else {
            files = new ArrayList<>();
        }
        // bindingResult에 에러 있는지 확인
        if(bindingResult.hasErrors()) {
            log.info("recommendation save error : {}", bindingResult);

            return ValidatedErrorsMsg.makeValidatedErrorsContents(bindingResult);
        }
        /// 검증 통과
        // form에 num 담기
        form.addNum(num);
        // 수정하기
        itemService.update(form, files);
        // 수정 완료
        String successMsg = "수정 완료";
        // responseEntity body 생성
        ResultForItem body = new ResultForItem(successMsg, num);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 상품 삭제
     */
    @DeleteMapping("/{num}")
    @ApiOperation("상품 삭제")
    public ResponseEntity<Result<String>> delete(@PathVariable int num) {
        // 삭제하기
        itemService.delete(num);
        // responseEntity body 생성
        log.info("상품 삭제 성공 -> item num : {}", num);
        String successMsg = "삭제 완료";
        Result<String> body = new Result<>(successMsg);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 상품 상세보기
     */
    @GetMapping("/{num}")
    @ApiOperation(value = "상품 상세보기")
    public Result<ItemDetailForm> watchDetail(@PathVariable int num) {
        // 상품 상세보기 불러오기
        ItemDetailForm form = itemService.watchDetail(num);
        // Result 생성
        Result<ItemDetailForm> result = new Result<>(form);

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 이미지 삭제
     */
    @DeleteMapping("/{itemNum}/pictures/{pictureNum}")
    @ApiOperation(value = "상품 이미지 삭제")
    public ResponseEntity<ResultForItem<String>> deletePicture(@PathVariable int itemNum, @PathVariable int pictureNum) {
        // 이미지 삭제
        itemService.deletePicture(itemNum, pictureNum);
        // responseEntity body 생성
        log.info("상품 이미지 삭제 성공 -> item picture num : {}", pictureNum);
        String successMsg = "삭제 완료";
        ResultForItem<String> body = new ResultForItem<>(successMsg, itemNum);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 상품 순위 불러오기 V1
     *               -> 좋아요수가 높은 순으로
     */
    @GetMapping("/rank/v1")
    @ApiOperation("상품 순위 목록 V1")
    public Result<List<ItemRankForm>> selectRankV1() {
        // 상품 순위 불러오기
        List<ItemRankForm> formList = itemService.selectRank();
        // Result 생성
        Result<List<ItemRankForm>> result = new Result<>(formList);

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 상품 순위 불러오기 V2
     *               -> 좋아요수가 높은 순으로
     */
    @GetMapping("/rank/v2")
    @ApiOperation("상품 순위 목록 V2")
    public Result<List<ItemRankForm>> selectRankV2() {
        // 상품 순위 불러오기
        List<ItemRankForm> formList = itemService.selectRankV2();
        // Result 생성
        Result<List<ItemRankForm>> result = new Result<>(formList);

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer : 이호진
     * update : 2023.02.01
     * description : 상품 모두 불러오기
     *               > 검색 데이터 name(상품명), memberId(상품 등록 아이디), createDate(만든 날짜), dtype(상품 종류)를 받고
     *               > 페이징 데이터 size, page를 받는다.
     */
    @GetMapping
    @ApiOperation("상품 목록")
    public Result<Page<ItemListForm>> selectList(ItemSearchCond cond, Pageable pageable) {
        log.info("itemSearchCond cond : {}", cond);
        // 상품 불러오기
        Page<ItemListForm> pageForm = itemService.selectList(cond, pageable);
        // Result 생성하기
        Result<Page<ItemListForm>> result = new Result<>(pageForm);

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 상품 이미지 파일 불러오기
     */
    @GetMapping("/images/{savedFileName}")
    @ApiOperation("상품 이미지 파일 불러오기")
    public Resource showImage(@PathVariable String savedFileName) throws MalformedURLException {
        log.info("showImage -> savedFileName : {}", savedFileName);
        // 파일이 저장되어 있는 root 불러오기
        return new UrlResource("file:" + fileStore.getFullPath(savedFileName));
    }


    //    -------------------------methods using for admin start----------------------------------
    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 상품 모두 불러오기
     *               > 검색 데이터 itemName, sellerId, memberId, createDate를 받고
     *               > 페이징 데이터 size, page를 받는다.
     *               -> status 사용
     *               -> admin이 사용한다.
     */
    @GetMapping("/admin")
    @ApiOperation("상품 목록 관리자용")
    public Result<Page<ItemListFormForAdmin>> selectListForAdmin(ItemSearchCond cond, Pageable pageable) {
        // 상품 불러오기
        Page<ItemListFormForAdmin> pageForm = itemService.selectListForAdmin(cond, pageable);
        // Result 생성
        Result<Page<ItemListFormForAdmin>> result = new Result<>(pageForm);

        return result;
    }

    //    -------------------------methods using for admin end----------------------------------













}
