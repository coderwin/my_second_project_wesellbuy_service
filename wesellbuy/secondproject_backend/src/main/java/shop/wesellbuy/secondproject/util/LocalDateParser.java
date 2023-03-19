package shop.wesellbuy.secondproject.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * String의 date를 LocalDate로 변경
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : entity의 repository의 where에서 날짜 검색에 사용
 *               Stirng의 모양이 yyyyMMdd에서 사용
 */
public class LocalDateParser {

    private LocalDate searchDate;


    /**
     * comment : 원하는 type의 dateType을 localdateTime으로 바꿀 수 있다.
     *           -> if 조건으로 해결 가능하다.
     */
    public LocalDateParser(String currentDate) {
        String dateType = "yyyy-MM-dd"; // 날짜 type
        this.searchDate = LocalDate.parse(currentDate, DateTimeFormatter.ofPattern(dateType));
    }

    // 해당 날짜의 시작 시간(ex: 2020-02-02 00:00:00)
    public LocalDateTime startDate() {
        return this.searchDate.atStartOfDay();
    }

    // 해당 날짜의 끝 시간(ex: 2020-02-02 23:59:59)
    public LocalDateTime endDate() {
        return LocalDateTime.of(this.searchDate, LocalTime.of(23, 59, 59));
    }

    // localDate를 String으로 바꾸기
    public static String changeTypeOfDate(LocalDate birthday) {
        // yyyy-MM-dd 형식을 String으로 바꾸기
        String parseLocalDate = birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return parseLocalDate;
    }

}
