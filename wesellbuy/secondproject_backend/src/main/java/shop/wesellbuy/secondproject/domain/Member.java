package shop.wesellbuy.secondproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.common.BaseDateColumnEntity;
import shop.wesellbuy.secondproject.domain.member.Address;
import shop.wesellbuy.secondproject.domain.member.MemberStatus;
import shop.wesellbuy.secondproject.domain.member.Phone;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;
import shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply;
import shop.wesellbuy.secondproject.domain.reply.ItemReply;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.member.MemberUpdateForm;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 정보
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer : 이호진
 * update : 2023.02.08
 * description : 회원 정보를 정의한다.
 *
 * comment : selfPicture의 생명주기를 다시 생각해보기(about create and update)
 *           joinColumn 위치 생각해보기(member or self_picture)
 *
 * update : OneToMany 필드를 불러올 때 순서 정하기
 *          > @OrderBy 사용
 */
@Entity
@Getter
public class Member extends BaseDateColumnEntity {

    @Id @GeneratedValue
    @Column(name = "member_num")
    private Integer num; // 회원가입 번호
    @Column(length = 50)
    private String id; // 아이디
    @Column(length = 21)
    private String pwd; // 비밀번호
    @Column(length = 50)
    private String name; // 이름
    @Column(length = 50)
    private String email; // 이메일

    @Embedded
    private Phone phones; // 휴대전화 + 집전화
    @Embedded
    private Address address; // 주소
    @Column(length = 5)
    @Enumerated(value = EnumType.STRING)
    private MemberStatus status; // 상태가 탈퇴면 lastModifiedDate의 시간이 탈퇴 날짜이다.

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "selfPicture_id")
    private SelfPicture selfPicture; // 이미지
                                     // Member와 라이프사이클 같아서 cascade 사용

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>(); // 상품 주문 모음

    @OneToMany(mappedBy = "member")
    private List<CustomerService> customerServiceList = new ArrayList<>(); // 고객지원 작성글 모음

    @OneToMany(mappedBy = "member")
    private List<Item> itemList = new ArrayList<>(); // 회원 등록 상품 모음

    @OneToMany(mappedBy = "member")
    private List<Recommendation> recommendationList = new ArrayList<>(); // 회원 등록 추천합니다 모음

    @OneToMany(mappedBy = "member")
    @OrderBy(value = "num DESC")
    private List<CustomerServiceReply> customerServiceReplyList = new ArrayList<>(); // 고객지원 댓글 모음

    @OneToMany(mappedBy = "member")
    @OrderBy(value = "num DESC")
    private List<ItemReply> ItemReplyList = new ArrayList<>(); // 상품 댓글 모음

    @OneToMany(mappedBy = "member")
    @OrderBy(value = "num DESC")
    private List<RecommendationReply> recommendationReplyList = new ArrayList<>(); // 추천합니다 댓글 모음

    protected Member() {
    }

    public Member(String name) {
        this.name = name;
    }


    // ** setter  ** //

    public void addName(String name) {
        this.name = name;
    }

    public void addId(String id) {
        this.id = id;
    }

    public void addPwd(String pwd) {
        this.pwd = pwd;
    }

    public void addEmail(String email) {
        this.email = email;
    }

    public void addPhones(Phone phones) {
        this.phones = phones;
    }

    public void addAddress(Address address) {
        this.address = address;
    }

    public void addStatus(MemberStatus status) {
        this.status = status;
    }

    // ** 연관관계 메서드 ** //
    // 회원 id 입력
    public void addSelfPicture(SelfPicture selfPicture) {
        // selfPicture이 없을 때 설정
        if(selfPicture != null) {
            this.selfPicture = selfPicture;
            selfPicture.addMember(this);
        } else {
            this.selfPicture = null;
        }
    }

    // ** 생성 메서드 ** //
    public static Member createMember(MemberForm memberForm) {
        Member member = new Member();

        member.addId(memberForm.getId());
        member.addName(memberForm.getName());
        member.addPwd(memberForm.getPwd());
        member.addEmail(memberForm.getEmail());
        member.addPhones(Phone.createPhone(memberForm.getSelfPhone(), memberForm.getHomePhone()));
        member.addAddress(
                Address.createAddress(
                        memberForm.getCountry(),
                        memberForm.getCity(),
                        memberForm.getStreet(),
                        memberForm.getDetail(),
                        memberForm.getZipcode()
                ));
        member.addSelfPicture(memberForm.getSelfPicture());
        member.addStatus(MemberStatus.J); // 회원 가입 상태

        return member;
    }

    // ** 비즈니스(서비스) 로직(메서드) ** //
    /**
     * 회원 탈퇴 요청이 올 때 실행
     * @param member
     */
    public void withdrawMember(Member member) {
        changeMemberStatus(member);
    }

    /**
     * 회원 탈퇴 요청이 올 때,
     * 회원 상태 레코드 value - J -> W로 변경
     */
    private void changeMemberStatus(Member member) {
        member.status = MemberStatus.W;
    }

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer : 이호진
     * update : 2023.03.06
     * description : 회원 정보 수정
     *
     * update : > 'updateSelfPicture이 null이 아니면 실행하기' 수정
     */
    public void updateMember(MemberUpdateForm memberUpdateForm, SelfPicture updateSelfPicture) {
        addPwd(memberUpdateForm.getPwd());
        addEmail(memberUpdateForm.getEmail());
        addPhones(
                Phone.createPhone(memberUpdateForm.getSelfPhone(), memberUpdateForm.getHomePhone())
        );
        addAddress(
                Address.createAddress(memberUpdateForm.getCountry(), memberUpdateForm.getCity(), memberUpdateForm.getStreet(), memberUpdateForm.getDetail(), memberUpdateForm.getZipcode())
        );
        // updateSelfPicture이 null이 아니면 실행하기
        if(updateSelfPicture != null) {
            addSelfPicture(updateSelfPicture);
        }
    }
}
