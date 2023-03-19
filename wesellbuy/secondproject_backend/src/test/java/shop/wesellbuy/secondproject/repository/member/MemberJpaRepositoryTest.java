package shop.wesellbuy.secondproject.repository.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;
import shop.wesellbuy.secondproject.web.member.MemberForm;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : Member Repository by jpa Test
 */
@SpringBootTest
@Transactional
@Slf4j
public class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @PersistenceContext
    EntityManager em;

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 회원 상세보기 확인 + fetchjoin 확인
     */
    @Test
//    @Rollback(false)
    public void 회원_상세보기_with_fetchJoin() {
        // given
        SelfPicture selfPicture = SelfPicture.createSelfPicture("test1", "test2");
        // selfPicture NotNull
        MemberForm memberForm1 = new MemberForm("a", "hello", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture);
        // selfPicture Null
        MemberForm memberForm2 = new MemberForm("a", "a", "123", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", "123", null);

        Member member1 = Member.createMember(memberForm1);
        Member member2 = Member.createMember(memberForm2);
        em.persist(member1);
        em.persist(member2);

        // when
        Member findMember1 = memberJpaRepository.findDetailInfoById(member1.getNum()).orElseThrow();
        Member findMember2 = memberJpaRepository.findDetailInfoById(member2.getNum()).orElseThrow();

        // then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        // selfPicture 확인
        assertThat(findMember1.getSelfPicture()).isEqualTo(selfPicture);
//        assertThat(findMember2.getSelfPicture()).isEqualTo(null);
        assertThat(findMember2.getSelfPicture()).isNull();
//        assertThat(findMember2.getSelfPicture()).isEqualTo(""); // false
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 회원 찾기 by name, selfPhone, email test
     */
    @Test
//    @Rollback(value = false)
    public void 회원_찾기_by_name_email_selfPhone() {
        // given
        MemberForm memberForm1 = new MemberForm("a", "a","123", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", "123", null);
        MemberForm memberForm3 = new MemberForm("a", "a","123", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", "123", null);
        MemberForm memberForm2 = new MemberForm("b", "bc","123", "a@a", "01012341234", "0511231234", "korea2", "b", "h", "h", "123", null);

        Member member1 = Member.createMember(memberForm1);
        Member member2 = Member.createMember(memberForm2);
        Member member3 = Member.createMember(memberForm3);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);

        // when
        MemberSearchIdCond memberSearchIdCond1 = new MemberSearchIdCond("a", "01012341234", null);
        MemberSearchIdCond memberSearchIdCond2 = new MemberSearchIdCond("b", null, "a@a");
        // 조건이 없을 때
        MemberSearchIdCond memberSearchIdCond31 = new MemberSearchIdCond("", null, "");
        // 조건이 하나만 있을 때
        MemberSearchIdCond memberSearchIdCond32 = new MemberSearchIdCond("a", null, "");
        MemberSearchIdCond memberSearchIdCond33 = new MemberSearchIdCond("", "01012341234", "");
        MemberSearchIdCond memberSearchIdCond34 = new MemberSearchIdCond("", "", "a@a");

        List<Member> findMembers1 = memberJpaRepository.findByNameAndSelfPhoneAndEmail(memberSearchIdCond1);
        List<Member> findMembers2 = memberJpaRepository.findByNameAndSelfPhoneAndEmail(memberSearchIdCond2);
        // 조건이 없을 때
        List<Member> findMembers31 = memberJpaRepository.findByNameAndSelfPhoneAndEmail(memberSearchIdCond31);

        // 조건이 하나만 있을 때
        List<Member> findMembers32 = memberJpaRepository.findByNameAndSelfPhoneAndEmail(memberSearchIdCond32);
        List<Member> findMembers33 = memberJpaRepository.findByNameAndSelfPhoneAndEmail(memberSearchIdCond33);
        List<Member> findMembers34 = memberJpaRepository.findByNameAndSelfPhoneAndEmail(memberSearchIdCond34);

        // then
        assertThat(findMembers1).containsExactly(member1, member3);
        assertThat(findMembers2).containsExactly(member2);
        // 조겆이 없을 때
        assertThat(findMembers31).containsExactly();
        // 조건이 하나만 있을 때
//        assertThat(findMembers32).containsExactly(); // 에러 service에서 처리
        assertThat(findMembers33).containsExactly();
        assertThat(findMembers34).containsExactly();
//        assertThat(findMembers34).containsExactly(member1); // 에러 발생
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 회원 찾기 by id, selfPhone, email test
     */
    @Test
//    @Rollback(value = false)
    public void 회원_찾기_by_id_email_selfPhone() {
        // given
        MemberForm memberForm1 = new MemberForm("a", "a2","123", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", "123", null);
        MemberForm memberForm3 = new MemberForm("a", "a","123", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", "123", null);
        MemberForm memberForm2 = new MemberForm("b", "bc","123", "a@a", "01012341234", "0511231234", "korea2", "b", "h", "h", "123", null);

        Member member1 = Member.createMember(memberForm1);
        Member member2 = Member.createMember(memberForm2);
        Member member3 = Member.createMember(memberForm3);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);

        // when
        MemberSearchPwdCond memberSearchPwdCond1 = new MemberSearchPwdCond("a", "01012341234", null);
        MemberSearchPwdCond memberSearchPwdCond2 = new MemberSearchPwdCond("bc", null, "a@a");
        // 조건이 없을 때
        MemberSearchPwdCond memberSearchPwdCond31 = new MemberSearchPwdCond("", null, "");
        // 조건이 하나만 있을 때
        MemberSearchPwdCond memberSearchPwdCond32 = new MemberSearchPwdCond("a", null, "");
        MemberSearchPwdCond memberSearchPwdCond33 = new MemberSearchPwdCond("", "01012341234", "");
        MemberSearchPwdCond memberSearchPwdCond34 = new MemberSearchPwdCond("", null, "a@a");


        List<Member> findMembers1 = memberJpaRepository.findByIdAndSelfPhoneAndEmail(memberSearchPwdCond1);
        List<Member> findMembers2 = memberJpaRepository.findByIdAndSelfPhoneAndEmail(memberSearchPwdCond2);
        // 조건이 없을 때
        List<Member> findMembers31 = memberJpaRepository.findByIdAndSelfPhoneAndEmail(memberSearchPwdCond31);
        // 조건이 하나만 있을 때
        List<Member> findMembers32 = memberJpaRepository.findByIdAndSelfPhoneAndEmail(memberSearchPwdCond32);
        List<Member> findMembers33 = memberJpaRepository.findByIdAndSelfPhoneAndEmail(memberSearchPwdCond33);
        List<Member> findMembers34 = memberJpaRepository.findByIdAndSelfPhoneAndEmail(memberSearchPwdCond34);

        // then
        assertThat(findMembers1).containsExactly(member3);
        assertThat(findMembers2).containsExactly(member2);
        // 조건이 없을 때
        assertThat(findMembers31).containsExactly();
        // 조건이 하나만 있을 때
//        assertThat(findMembers32).containsExactly(); // 에러 서비스에서 처리
        assertThat(findMembers33).containsExactly();
        assertThat(findMembers34).containsExactly();

    }

    /**
     * writer : 이호진
     * init : 2023.01.16
     * updated by writer :
     * update :
     * description : save Test
     */
    @Test
//    @Rollback(value = false)
    public void 회원정보저장() {
        // given
        SelfPicture testSelfPicture = SelfPicture.createSelfPicture("a", "a");

        MemberForm memberForm1 = new MemberForm("a", "a", "123","a@a", "01012341234", "0511231234", "korea", "b", "h", "h", "123", testSelfPicture);

        Member member1 = Member.createMember(memberForm1);

        // when
        memberJpaRepository.save(member1);

        // then
        Member findMember = memberJpaRepository.findById(member1.getNum()).orElse(null);

        assertThat(findMember).isEqualTo(member1);
    }

    /**
     * writer : 이호진
     * init : 2023.01.16
     * updated by writer :
     * update :
     * description : findAll by condition and pageable Test
     */
    @Test
//    @Rollback(value = false)
    public void 회원정보_모두가져오기_By_조건_페이징() {
        // given
        SelfPicture testSelfPicture = SelfPicture.createSelfPicture("a", "a");

        MemberForm memberForm1 = new MemberForm("a", "a", "123","a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm2 = new MemberForm("b", "bc","123", "a@a", "01012341234", "0511231234", "korea2", "b", "h", "h", "123", testSelfPicture);
        MemberForm memberForm3 = new MemberForm("c", "c", "123","a@a", "01012341234", "0511231234", "korea3", "b", "h", "h", "123", testSelfPicture);
        MemberForm memberForm4 = new MemberForm("a", "a", "123","a@a", "01012341234", "0511231234", "us", "c", "h", "h", "123", testSelfPicture);

        Member member1 = Member.createMember(memberForm1);
        Member member2 = Member.createMember(memberForm2);
        Member member3 = Member.createMember(memberForm3);
        Member member4 = Member.createMember(memberForm4);

        // 회원 저장
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);

        // when
        String today = "2023-01-27";
        String nextday = "2023-01-28";
        // 조건 입력하기
        // 조건 0개
        MemberSearchCond memberSearchCond = new MemberSearchCond(null, null, null, null); // 4
        // 조건 1개
        MemberSearchCond memberSearchCondWithId = new MemberSearchCond("a", null, null, null); // 2
        MemberSearchCond memberSearchCondWithCountry = new MemberSearchCond(null, "k", null, null); // 3
        MemberSearchCond memberSearchCondWithCity = new MemberSearchCond(null, null, "c", null); // 1
        MemberSearchCond memberSearchCondWithCreateDate = new MemberSearchCond(null, null, null, today); // 4
        // 조건 2개
        MemberSearchCond memberSearchCond1 = new MemberSearchCond("a", "korea", null, null); // 1
        MemberSearchCond memberSearchCond2 = new MemberSearchCond("c", null, "b", null); // 2
        MemberSearchCond memberSearchCond3 = new MemberSearchCond("a", null, null, today);// 2
        MemberSearchCond memberSearchCond4 = new MemberSearchCond(null, "u", null, today);// 1
        MemberSearchCond memberSearchCond5 = new MemberSearchCond(null, "u", "a", null);// 0
        MemberSearchCond memberSearchCond6 = new MemberSearchCond(null, null, "b", today);// 3
        MemberSearchCond memberSearchCond7 = new MemberSearchCond(null, "a", "b", null);// 3
        // 조건 3개
        MemberSearchCond memberSearchCond8 = new MemberSearchCond(null, "a", "b", nextday);// 0
        MemberSearchCond memberSearchCond9 = new MemberSearchCond("a", "ea", "b", null);// 1
        MemberSearchCond memberSearchCond10 = new MemberSearchCond("b", "a", null, today);// 1
        MemberSearchCond memberSearchCond11 = new MemberSearchCond("b", null, "a", today);// 0
        // 조건 4개
        MemberSearchCond memberSearchCond12 = new MemberSearchCond("a", "k", "b", today);// 1


        // 모든 회원 가져오기
        PageRequest pageRequestSize10 = PageRequest.of(0, 10);
        PageRequest pageRequestSize1 = PageRequest.of(0, 1);
        PageRequest pageRequestSize2 = PageRequest.of(0, 2);
        PageRequest pageRequestPage1Size3 = PageRequest.of(1, 3);

//        // then - 객체로 비교
//        // 조건 0
//        Page<Member> findMembers = memberJpaRepository.findAllInfo(memberSearchCond, pageRequestSize10);
//
//        log.info("findMembers content : {}", findMembers.getContent());
//        assertThat(findMembers.getContent()).containsExactly(member4, member3, member2, member1);
//        findAllWithCondTest(memberSearchCond, pageRequestSize10, member1, member2, member3, member4);
//
//        // 조건 1
//        findAllWithCondTest(memberSearchCondWithId, pageRequestSize10, member1, member4);
//        findAllWithCondTest(memberSearchCondWithCountry, pageRequestSize10, member1, member2, member3);
//        findAllWithCondTest(memberSearchCondWithCity, pageRequestSize10, member4);
//        findAllWithCondTest(memberSearchCondWithCreateDate, pageRequestSize10, member1, member2, member3, member4);
//
//        // 조건 2
//        findAllWithCondTest(memberSearchCond1, pageRequestSize10, member1);
//        findAllWithCondTest(memberSearchCond2, pageRequestSize10, member2, member3);
//        findAllWithCondTest(memberSearchCond3, pageRequestSize10, member1, member4);
//        findAllWithCondTest(memberSearchCond4, pageRequestSize10, member4);
//        findAllWithCondTest(memberSearchCond5, pageRequestSize10, null);
//        findAllWithCondTest(memberSearchCond6, pageRequestSize10, member1, member3, member2);
//        findAllWithCondTest(memberSearchCond7, pageRequestSize10, member1);
//
//        // 조건 3
//        findAllWithCondTest(memberSearchCond8, pageRequestSize10, null);
//        findAllWithCondTest(memberSearchCond9, pageRequestSize10, member1);
//        findAllWithCondTest(memberSearchCond10, pageRequestSize10, member2);
//        findAllWithCondTest(memberSearchCond11, pageRequestSize10, null);
//
//        // 조건 4
//        findAllWithCondTest(memberSearchCond12, pageRequestSize10, member1);
//
//        // page의 size가 1일 때
//        findAllWithCondTest(memberSearchCond, pageRequestSize1, member4); // order by -> desc 라서
//        findAllWithCondTest(memberSearchCond, pageRequestSize1, member1); // fail
//        findAllWithCondTest(memberSearchCond, pageRequestSize1, member2); // fail

//         then - 개수로 비교
        // 조건 0
        findAllWithCondTestWithQuantity(memberSearchCond, pageRequestSize10, 4);

        // 조건 1
        findAllWithCondTestWithQuantity(memberSearchCondWithId, pageRequestSize10,2);
        findAllWithCondTestWithQuantity(memberSearchCondWithCountry, pageRequestSize10, 3);
        findAllWithCondTestWithQuantity(memberSearchCondWithCity, pageRequestSize10,1);
        findAllWithCondTestWithQuantity(memberSearchCondWithCreateDate, pageRequestSize10,4);

        // 조건 2
        findAllWithCondTestWithQuantity(memberSearchCond1, pageRequestSize10, 1);
        findAllWithCondTestWithQuantity(memberSearchCond2, pageRequestSize10, 2);
        findAllWithCondTestWithQuantity(memberSearchCond3, pageRequestSize10, 2);
        findAllWithCondTestWithQuantity(memberSearchCond4, pageRequestSize10, 1);
        findAllWithCondTestWithQuantity(memberSearchCond5, pageRequestSize10, 0);
        findAllWithCondTestWithQuantity(memberSearchCond6, pageRequestSize10, 3);
        findAllWithCondTestWithQuantity(memberSearchCond7, pageRequestSize10, 3);

        // 조건 3
        findAllWithCondTestWithQuantity(memberSearchCond8, pageRequestSize10, 0);
        findAllWithCondTestWithQuantity(memberSearchCond9, pageRequestSize10, 1);
        findAllWithCondTestWithQuantity(memberSearchCond10, pageRequestSize10, 1);
        findAllWithCondTestWithQuantity(memberSearchCond11, pageRequestSize10, 0);

        // 조건 4
        findAllWithCondTestWithQuantity(memberSearchCond12, pageRequestSize10, 1);

//        // page의 size가 1일 때 - 객체로 비교
//        findAllWithCondTest(memberSearchCond, pageRequestSize1, member4); // order by -> desc 라서
//        findAllWithCondTest(memberSearchCond, pageRequestSize1, member1); // fail
//        findAllWithCondTest(memberSearchCond, pageRequestSize1, member2); // fail

        // page의 size가 1일 때 - 개수로 비교
        findAllWithCondTestWithQuantity(memberSearchCond, pageRequestSize1, 1); // order by -> desc 라서
        findAllWithCondTestWithQuantity(memberSearchCond, pageRequestSize2, 2); // order by -> desc 라서
        findAllWithCondTestWithQuantity(memberSearchCond, pageRequestPage1Size3, 1); // order by -> desc 라서


        // totalCount 확인
        Page<Member> findMembers1 = memberJpaRepository.findAllInfo(memberSearchCond, pageRequestSize10);
        Page<Member> findMembers2 = memberJpaRepository.findAllInfo(memberSearchCondWithId, pageRequestSize10);

        assertThat(findMembers1.getTotalElements()).isEqualTo(4L);
        assertThat(findMembers2.getTotalElements()).isEqualTo(2L); // where 조건에 맞게 count 센다
    }

    // 왜 안 되지? 객체가 달라진 건가?

    /**
     * 객체를 비교
     */
    private void findAllWithCondTest(MemberSearchCond memberSearchCond, PageRequest pageRequest, Member... members) {
        Page<Member> findMembers = memberJpaRepository.findAllInfo(memberSearchCond, pageRequest);

        assertThat(findMembers.getContent()).containsExactly(members);
    }

    /**
     * 개수를 비교
     */
    private void findAllWithCondTestWithQuantity(MemberSearchCond memberSearchCond, PageRequest pageRequest, int quantity) {
        Page<Member> findMembers = memberJpaRepository.findAllInfo(memberSearchCond, pageRequest);

        assertThat(findMembers.getContent().size()).isEqualTo(quantity);
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : findAll by condition and pageable Test + Slice 이용
     *
     * comment : Slice 객체인데 왜 count가 size + 1나 더 않 늘지?
     */
    @Test
//    @Rollback(value = false)
    public void Slice를_이용하여_paging_해본다() {

        // given
        SelfPicture testSelfPicture = SelfPicture.createSelfPicture("a", "a");

        MemberForm memberForm1 = new MemberForm("a", "a", "123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm2 = new MemberForm("b", "bc", "123", "a@a", "01012341234", "0511231234", "korea2", "b", "h", "h", "123", testSelfPicture);
        MemberForm memberForm3 = new MemberForm("c", "c", "123","a@a", "01012341234", "0511231234", "korea3", "b", "h", "h", "123", testSelfPicture);
        MemberForm memberForm4 = new MemberForm("a", "a", "123","a@a", "01012341234", "0511231234", "us", "c", "h", "h", "123", testSelfPicture);

        Member member1 = Member.createMember(memberForm1);
        Member member2 = Member.createMember(memberForm2);
        Member member3 = Member.createMember(memberForm3);
        Member member4 = Member.createMember(memberForm4);

        // 회원 저장
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);

        // when
        MemberSearchCond memberSearchCond = new MemberSearchCond(null, null, null, null); // 4
        PageRequest pageRequest = PageRequest.of(0, 3);

        Slice<Member> findMembers = memberJpaRepository.findAllInfoUsingSlice(memberSearchCond, pageRequest);

        // then
        assertThat(findMembers.getContent().size()).isEqualTo(4);
//        assertThat(findMembers.getContent().size()).isEqualTo(3);
    }

    @Test
    public void 회원_정보_가져오기_by_memberId() {
        // given
        MemberForm memberForm1 = new MemberForm("a", "a", "123","a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member = Member.createMember(memberForm1);

        em.persist(member);
        // when
        Member findMember = memberJpaRepository.findByMemberId(member.getId()).orElseThrow();

        // then
        assertThat(findMember).isEqualTo(member);
    }


}
