package shop.wesellbuy.secondproject.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.wesellbuy.secondproject.exception.member.ExistingIdException;
import shop.wesellbuy.secondproject.service.member.MemberService;
import shop.wesellbuy.secondproject.web.controller.MemberController;
import shop.wesellbuy.secondproject.web.member.MemberOriginForm;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MemberController 단위 테스트
 *
 * comment - 예외를 테스트하고 싶을 때
 *           -> Controller 내부에서 발생하는 예외로 인해 ServletException으로 나타나는 예외를
 *           -> Controller에서 일어나는 예외로 확인할 수는 없을까?
 */
//@WebMvcTest(MemberController.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
public class MemberControllerUnitTest {

    @InjectMocks // 가짜 객체 주입
    MemberController memberController;

    @Mock // 가짜 객체 생성
    MemberService memberService;

    MockMvc mockMvc;// Http 호출 가능

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @DisplayName("회원 가입 성공")
    @Test
    public void 회원_가입_성공() throws Exception {
        // given
        // 결과값 만들기
        int response = 1;
        // Mock 객체(가짜 객체)의 결과값 만들기
        doReturn(response).when(memberService).join(any(MemberOriginForm.class));

        /// 클라이언트 data 만들기
        // MockFile 만들기
        String fileName = "testFile1"; // 파일명
        String contentType = "jpg"; // 파일 확장자/ 파일타입
        String originFileName = fileName + "." + contentType;
        String filePath = "src/test/resources/testImages/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath); // 첨부파일 읽어오기

        MockMultipartFile file = new MockMultipartFile("file", originFileName, contentType, fileInputStream);

        // == MemberOriginForm 만들기 그것을 json String으로 만들기
        MemberOriginForm memberOriginForm = new MemberOriginForm(
                "랄라",
                "number1",
                "asd123!@#",
                "asd123!@#",
                "number@naver2.com2",
                "01012341234",
                "021231234",
                "korea",
                "bu",
                "dong",
                "apart",
                "12345",
                null
        );
        // dto를 String json으로  만들기 그다음 byte[]/inputStream으로 만들기
        String body = new ObjectMapper().writeValueAsString(memberOriginForm);
        byte[] strJsonBody = body.getBytes(StandardCharsets.UTF_8);
        // dto를 바로 string으로
//        byte[] strJsonBody = memberOriginForm.toString().getBytes(StandardCharsets.UTF_8); (X)

//        MockMultipartFile jsonBody = new MockMultipartFile("image", "", "application/json", result);
        MockMultipartFile jsonBody = new MockMultipartFile(
                "memberOriginForm",
                "",
                "application/json",
                "{ \"id\": \"ok\", \"name\": \"ok\", \"pwd\": \"ads123!@#\", \"pwdConfirm\": \"ads123!@#\", \"email\": \"ok@ok.com\", \"country\": \"korea\", \"city\":\"bu1123\", \"street\": \"bu12\", \"detail\": \"bu1\", \"zipcode\": \"12345\", \"selfPhone\": \"01012341234\", \"homePhone\": \"021231234\"}".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile jsonBody2 = new MockMultipartFile(
                "memberOriginForm",
                "",
                "application/json",
                strJsonBody);
        // when
        // test 메서드 실행하기
        // O 성공
//        ResultActions resultActions = mockMvc.perform(
//                multipart("/members")
//                        .file(file)
//                        .file(jsonBody)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
        // O 성공
        ResultActions resultActions = mockMvc.perform(
                multipart("/members")
                        .file(file)
                        .file(jsonBody2)
                        .contentType("multipart/form-data")
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        // 결과 확인하기
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("data").value("회원가입 성공"))
                .andExpect(jsonPath("$.data").value("회원가입 성공"))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("회원 가입 실패V1_이미 존재하는 아이디")
    @Test
    public void 회원_가입_실패_이미_존재하는_아이디() throws Exception {
        // given
        // 회원 가입1
        String fileName = "testFile1"; // 파일명
        String contentType = "jpg"; // 파일 확장자/ 파일타입
        String originFileName = fileName + "." + contentType;
        String filePath = "src/test/resources/testImages/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath); // 첨부파일 읽어오기

        MockMultipartFile file = new MockMultipartFile("file", originFileName, contentType, fileInputStream);

        // == MemberOriginForm 만들기 그것을 json String으로 만들기
        MemberOriginForm memberOriginForm = new MemberOriginForm(
                "랄라",
                "number1",
                "asd123!@#",
                "asd123!@#",
                "number@naver2.com2",
                "01012341234",
                "021231234",
                "korea",
                "bu",
                "dong",
                "apart",
                "12345",
                file
        );
        // 회원1 저장
        memberService.join(memberOriginForm);
        // 아이디 중복 예외를 던진다.
        doThrow(new ExistingIdException("이미 사용중인 아이디")).when(memberService).join(any(MemberOriginForm.class));

        // 아이디 중복 회원
        String fileName2 = "testFile1"; // 파일명
        String contentType2 = "jpg"; // 파일 확장자/ 파일타입
        String originFileName2 = fileName2 + "." + contentType2;
        String filePath2 = "src/test/resources/testImages/" + fileName2 + "." + contentType2;
        FileInputStream fileInputStream2 = new FileInputStream(filePath); // 첨부파일 읽어오기

        MockMultipartFile file2 = new MockMultipartFile("file", originFileName2, contentType2, fileInputStream2);

        // == MemberOriginForm 만들기 그것을 json String으로 만들기
        MemberOriginForm memberOriginForm2 = new MemberOriginForm(
                "랄라",
                "number1",
                "asd123!@#",
                "asd123!@#",
                "number@naver2.com2",
                "01012341234",
                "021231234",
                "korea",
                "bu",
                "dong",
                "apart",
                "12345",
                null
        );
        // json string으로 만들기
        String body = new ObjectMapper().writeValueAsString(memberOriginForm2);
        log.info("jsonbody : {}", body);
        // inputStream으로 만들기
        byte[] strJsonBody = body.getBytes(StandardCharsets.UTF_8);
        // multipartFile 생성하기
        MockMultipartFile jsonBody = new MockMultipartFile(
                "data",
                "",
                "application/json",
                strJsonBody
        );
        // when // then
        // test V1
        // ExistingIdException에 의해 ServletException 발생
//        Assertions.assertThrows(ServletException.class, () -> {
//            mockMvc.perform(
//                    multipart("/members")
//                            .file(file2)
//                            .file(jsonBody)
//                            .contentType(MediaType.MULTIPART_FORM_DATA)
//                            .accept(MediaType.APPLICATION_JSON)
//            );
//        });
        // test V2
        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> {
                            mockMvc.perform(
                                    multipart("/members")
                                            .file(file2)
                                            .file(jsonBody)
                                            .contentType(MediaType.MULTIPART_FORM_DATA)
                                            .accept(MediaType.APPLICATION_JSON)
                            );
                        }
                )
                .hasCause(new ExistingIdException("이미 사용중인 아이디"));

//        // test V3 - perform() 수행 중 예외가 발생해 이까지 도달하지 못함
//        ResultActions resultActions = mockMvc.perform(
//                multipart("/members")
//                        .file(file2)
//                        .file(jsonBody)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // assert로 예외를 검사한다.
//        resultActions.andExpect(status().is4xxClientError())
//                .andExpect(
//                        (result) -> {
//                            assertTrue(result.getResolvedException().getClass().isAssignableFrom(ExistingIdException.class));
//                        }
//                );

//        // test V4
//        ResultActions resultActions = mockMvc.perform(
//                multipart("/members")
//                        .file(file2)
//                        .file(jsonBody)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .accept(MediaType.APPLICATION_JSON)
//        );

//
//        // then
//        // test V4 - perform() 수행 중 예외가 발생해 이까지 도달하지 못함
//        resultActions.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("code").value("Bad Reqeust"))
//                .andExpect(jsonPath("errMsg").value("이미 사용중인 아이디"))
//                .andExpect(jsonPath("$.errMsg").value("이미 사용중인 아이디"))
//                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("회원가입 검증 예외 발생")
    @Test
    public void 회원가입_검증_예외_발생() {
        // given

        // when

        // then
    }









}
