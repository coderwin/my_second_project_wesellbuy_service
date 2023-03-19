package shop.wesellbuy.secondproject.repository.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Member findAll for condition dto
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : Member finaAll에 사용되는 where 절의 조건 데이터 모음
 */
@Getter
@AllArgsConstructor
public class MemberSearchCond {

    private String id; // 아이디
    private String country; // 나라 이름
    private String city; // 도시 이름
    private String createDate; // 아이디 생성 날짜
}
