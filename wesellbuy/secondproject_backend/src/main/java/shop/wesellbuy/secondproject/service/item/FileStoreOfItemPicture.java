package shop.wesellbuy.secondproject.service.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;
import shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 상품 업로드 파일 저장
 * writer : 이호진
 * init : 2023.02.02
 * updated by writer :
 * update :
 * description : 상품 업로드 파일 저장에 사용
 */
@Component
@Slf4j
public class FileStoreOfItemPicture {

    @Value("${file.itemPicture.dir}")
    private String fileDir; // 파일 경로
    @Value("${directory.itemPicture.dir}")
    private String directoryDir; // 폴더 생성 경로

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 파일이 저장되는 fullName(폴더 경로 + 파일 이름) 설정
     */
    public String getFullPath(String filename) {

        return fileDir + filename;
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : form으로부터 받은 여러 개의 파일들 저장하기
     */
    public List<ItemPicture> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        // 파일 담을 list 생성
        List<ItemPicture> storeFileResult = new ArrayList<>();

        // list에 담기
        for(MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 하나의 파일을 해당 서버 컴퓨터의 경로에 저장하기
     */
    public ItemPicture storeFile(MultipartFile multipartFile) throws IOException {

        // 파일 없을 때
        if(multipartFile == null || multipartFile.isEmpty()) {
           return null;
        }
        // 원본 파일명 찾기
        String originalFileName = multipartFile.getOriginalFilename();
        // 서버 컴퓨터에 저장될 가짜?파일명 만들기
        String storeFileName = createStoreFileName(originalFileName);

        // 저장 폴더가 없으면 폴더 만들기
        makeFolder();

        // 파일을 해당 경로에 저장하기
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return ItemPicture.createItemPicture(originalFileName, storeFileName);
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 폴더 없을 시 폴더 생성하기
     */
    private void makeFolder() {
        log.info("makeFolder 작동 중...");
        File newDirectory = new File(this.directoryDir);

        // 폴더가 존재 안 하면 만들기
        if(!newDirectory.exists()) {
            log.info("폴더 생성시작");
            newDirectory.mkdirs();

            log.info("폴더 생성 끝");
        }
        // 현재 시스템 경로 확인
        log.info("현재 시스템 경로 확인 : {}", System.getProperty("user.dir")); // 프로젝트 최상위 폴더 확인
        // 파일 위치 확인
        log.info("파일 위치 확인 : {}", newDirectory.getPath()); // 이미지 저장 폴더 경로 확인

        log.info("makeFolder 작동 끝...");
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 서버 컴퓨터에 저장되는 가짜? 파일명 만들기
     *              -> 중복 파일명을 막기위해서
     */
    private String createStoreFileName(String originalFileName) {

        // 확장자를 찾기
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 파일의 확장자 찾기
     */
    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");// 파일명.확장자에서 확장자가 시작되는 앞을 찾기
        return originalFileName.substring(pos + 1);
    }




}
