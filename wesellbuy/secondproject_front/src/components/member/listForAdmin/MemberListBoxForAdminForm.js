import { useContext } from "react";
import { Table } from "react-bootstrap";
import { MemberListForAdminContext } from "../MemberListForAdminForm";
import MemberForAdminForm from "./MemberForAdminForm";

/**
 * Member list box for admin component
 * writer : 이호진
 * init : 2023.03.28
 * updated by writer :
 * update :
 * description : 회원 목록 box 관리자용 component
 */
const MemberListBoxForAdminForm = () => {
  
  /// 변수 모음
  // 외부의 변수 불러오기
  const {listDatas, data: searchCond, totalPages} = useContext(MemberListForAdminContext);

  /// 상태 모음

  /// 메서드 모음

  /// view 모음
  let view = null;// 태그를 담아준다.
  // tbody에 들어갈 데이터 생성
  // 데이터가 있으면 생성한다
  if(listDatas) {
    // 데이터 만들기
    view = listDatas.map((data, i) => {
      return (
        <MemberForAdminForm 
          key={i} 
          data={data} 
          numPosition={i}
          datasLength={listDatas.length} 
          totalPages={totalPages} 
          searchCond={searchCond} 
        />
      );
    });
  // 없으면 데이터가 존재하지 않는다고 알려주기
  } else {
    view = (
      <tr>
        <th>
          회원이 없습니다.
        </th>
      </tr>
    );
  }

  return (
    <Table hover>
      <thead className="table-primary">
        <tr>
          <th>순서</th>
          <th>아이디</th>
          <th>이름</th>
          <th>이메일</th>
          <th>휴대전화</th>
          <th>집전화</th>
          <th>주소</th>
          <th>가입날짜</th>
          <th>가입상태</th>
          <th></th>{/* 탈퇴 버튼 */}
        </tr>
      </thead>
      <tbody>
        {view}
      </tbody>
    </Table>
  )
}

export default MemberListBoxForAdminForm;