package shop.wesellbuy.secondproject;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;
import shop.wesellbuy.secondproject.web.member.MemberForm;

/**
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : Test를 위한 데이터 생성
 *               - test에만 사용
 *
 * comment : class 생성없이 EntityManager를 사용하면 왜 Transaction에 걸릴까? EntityManager가 왜 생성안 될까?
 */
@Profile("local")
//@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInit {

    private final InitMemberRepository initMemberRepository;

    @PostConstruct
    public void init() {
        log.info("test member date 생성 시작");
        // member repository test 데이터
        initMemberRepository.initForMember();
    }

    @Component
    static class InitMemberRepository {
        @PersistenceContext
        EntityManager em;

        /**
         * member를 위한 test 데이터
         */
        @Transactional // repository test에 사용한다.
        public void initForMember() {

            SelfPicture testSelfPicture = SelfPicture.createSelfPicture("a", "a");
            SelfPicture testSelfPicture2 = SelfPicture.createSelfPicture("a", "a");
            SelfPicture testSelfPicture3 = SelfPicture.createSelfPicture("a", "a");

            MemberForm memberForm1 = new MemberForm("a", "ab", "123", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", "123", testSelfPicture);
            MemberForm memberForm2 = new MemberForm("b", "b","123", "b@b", "01012341234", "0511231234", "korea", "b", "h", "h","123", testSelfPicture2);
            MemberForm memberForm3 = new MemberForm("c", "c","123", "c@c", "01012341234", "0511231234", "korea", "b", "h", "h", "123",testSelfPicture3);

            Member member1 = Member.createMember(memberForm1);
            Member member2 = Member.createMember(memberForm2);
            Member member3 = Member.createMember(memberForm3);

            // 회원정보 저장
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
        }
    }


}
