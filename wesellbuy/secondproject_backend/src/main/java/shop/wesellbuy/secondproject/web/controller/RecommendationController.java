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
import shop.wesellbuy.secondproject.repository.recommendation.RecommendationSearchCond;
import shop.wesellbuy.secondproject.service.recommendation.FileStoreOfRecommendationPicture;
import shop.wesellbuy.secondproject.service.recommendation.RecommendationService;
import shop.wesellbuy.secondproject.util.SessionConst;
import shop.wesellbuy.secondproject.util.ValidationOfPattern;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberSessionForm;
import shop.wesellbuy.secondproject.web.recommendation.*;
import shop.wesellbuy.secondproject.web.resultBox.Result;
import shop.wesellbuy.secondproject.web.resultBox.ResultV2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Recommendation Contoller
 * writer : 이호진
 * init : 2023.02.12
 * updated by writer :
 * update :
 * description : Recommendation RestController 메소드 모음
 */
@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final FileStoreOfRecommendationPicture fileStoreOfRecommendationPicture;

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 추천합니다글 저장
     */
    @PostMapping
    @ApiOperation(value = "추천합니다 게시글 등록")
    public ResponseEntity<?> save(@RequestPart("data") @Validated RecommendationForm form,
                     @RequestPart(name = "files", required = false) List<MultipartFile> files,
                     BindingResult bindingResult,
                     @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberSessionForm sessionForm
                     ) throws IOException {
        log.info("files : {}", files);
        log.info("RecommendationForm form : {}", form);

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
        // 게시글 등록
        int num = recommendationService.save(form, files, sessionForm.getNum());
        // code 201 보내기
        String successMsg = "게시글 등록 성공";
        ResultV2<String> body = new ResultV2(successMsg, num);

        return new ResponseEntity(body, HttpStatus.CREATED);
    }

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 추천합니다글 상세보기
     */
    @GetMapping("/{num}")
    @ApiOperation(value = "추천합니다 게시글 상세보기")
    public Result<RecommendationDetailForm> watchDetail(@PathVariable int num) {
        // 게시글 상세보기 불러오기
        RecommendationDetailForm form = recommendationService.watchDetail(num);
        // Result 생성
        Result<RecommendationDetailForm> result = new Result<>(form);

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 추천합니다글 수정
     */
    @PutMapping("/{num}")
    @ApiOperation(value = "추천합니다 게시글 수정")
    public ResponseEntity<?> update(@RequestPart("data") @Validated RecommendationUpdateForm form,
                                    @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                    BindingResult bindingResult,
                                    @PathVariable int num) throws IOException {
        log.info("files : {}", files);
        log.info("RecommendationForm form : {}", form);

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
            // files null 처리하기
        } else {
            files = new ArrayList<>();
        }
        // bindingResult에 에러 있는지 확인
        if(bindingResult.hasErrors()) {
            log.info("member update error : {}", bindingResult);

            return ValidatedErrorsMsg.makeValidatedErrorsContents(bindingResult);
        }
        /// 검증 통과
        // form에 num 담기
        form.addNum(num);
        // 수정하기
        recommendationService.update(form, files);
        // 수정 완료
        String successMsg = "수정 완료";
        // responseEntity body 생성
        ResultV2<String> body = new ResultV2<>(successMsg, form.getNum());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 추천합니다글 삭제
     */
    @DeleteMapping("/{num}")
    @ApiOperation("추천합니다 게시글 삭제")
    public ResponseEntity<Result<String>> delete(@PathVariable int num) {
        // 게시글 삭제
        recommendationService.delete(num);
        // responseEntity body 생성
        log.info("추천합니다글 삭제 성공 -> recommendation num : {}", num);
        String successMsg = "삭제 완료";
        Result<String> body = new Result<>(successMsg);

        return new ResponseEntity(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 추천합니다글 이미지 삭제
     */
    @DeleteMapping("/{num1}/pictures/{num2}")
    @ApiOperation("추천합니다 게시글 이미지 삭제")
    public ResponseEntity<ResultV2<String>> deletePicture(@PathVariable("num1") int boardNum, @PathVariable("num2") int pictureNum) {
        // 이미지 삭제
        recommendationService.deletePicture(boardNum, pictureNum);
        // responseEntity body 생성
        log.info("추천합니다글 이미지 삭제 성공 -> recommendation picture num : {}", pictureNum);
        String successMsg = "삭제 완료";
        ResultV2<String> body = new ResultV2<>(successMsg, boardNum);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 추천합니다글 모두 불러오기
     *               > 검색 데이터 itemName, sellerId, memberId, createDate를 받고
     *               > 페이징 데이터 size, page를 받는다.
     */
    @GetMapping
    @ApiOperation("추천합니다 게시글 목록")
    public Result<Page<RecommendationListForm>> selectList(RecommendationSearchCond cond, Pageable pageable) {
        log.info("RecommendationSearchCond : {}", cond);
        // 게시글 가져오기
        Page<RecommendationListForm> pageForm = recommendationService.selectList(cond, pageable);
        // Result 생성하기
        Result<Page<RecommendationListForm>> result = new Result<>(pageForm);

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 추천합니다글 이미지 파일 불러오기
     */
    @GetMapping("/images/{savedFileName}")
    @ApiOperation("추천합니다 게시글 이미지 파일 불러오기")
    public Resource showImage(@PathVariable String savedFileName) throws MalformedURLException {
        log.info("showImage -> savedFileName : {}", savedFileName);
        // 파일이 저장되어있는 root 불러오기
        return new UrlResource("file:" + fileStoreOfRecommendationPicture.getFullPath(savedFileName));
    }

    //   ------------------------------methods using for admin start --------------------------------
    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 추천합니다글 모두 불러오기
     *               > 검색 데이터 itemName, sellerId, memberId, createDate를 받고
     *               > 페이징 데이터 size, page를 받는다.
     *               -> status 사용
     *               -> admin이 사용한다.
     *
     */
    @GetMapping("/admin")
    @ApiOperation("추천합니다 게시글 목록 관리자용")
    public Result<Page<RecommendationListForAdminForm>> selectListForAdmin(RecommendationSearchCond cond, Pageable pageable) {
        // 게시글 가져오기
        Page<RecommendationListForAdminForm> pageForm = recommendationService.selectListForAdmin(cond, pageable);
        // Result 생성하기
        Result<Page<RecommendationListForAdminForm>> result = new Result<>(pageForm);

        return result;
    }

    //   ------------------------------methods using for admin end --------------------------------

}
