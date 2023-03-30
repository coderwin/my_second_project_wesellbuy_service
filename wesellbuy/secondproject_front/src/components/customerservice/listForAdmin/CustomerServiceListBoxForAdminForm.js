import { useContext } from "react";
import { Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { CustomerServiceListForAdminContext } from "../CustomerServiceListForAdminForm";

/**
 * CustomerService list box for admin component
 * writer : 이호진
 * init : 2023.03.28
 * updated by writer :
 * update :
 * description : 고객지원글 목록 box 관리자용 component
 */
const CustomerServiceListBoxForAdminForm = () => {
  
  /// 변수 모음
  // 외부의 변수 불러오기
  const {listDatas} = useContext(CustomerServiceListForAdminContext);
  const navigation = useNavigate();// navigation

  /// 상태 모음

  /// 메서드 모음
  
  // 고객지원글을 클릭했을 때
  function handleItemNameClick(e) {
    // id 불러오기
    const id = e.target.id;// 고객지원글 번호
    // 고객지원글 상세보기로 이동
    navigation(`/cs/${id}`);
  }
  // 시간 날짜만 나오게 하기
  function printDate(datetime) {
    // 날짜시간 받아서 
      // T부분에서 cut
    const datetimeArr = datetime.split("T");
    return datetimeArr[0];
  }

  /// view 모음
  

  let view = null;// 태그를 담아준다.
  // tbody에 들어갈 데이터 생성
  // 데이터가 있으면 생성한다
  if(listDatas.length !== 0) {
    // 데이터 만들기
    view = listDatas.map((data) => {
      return (
        <tr key={data.num}>
          <th id={data.num} onClick={handleItemNameClick} className="mousePointer">{data.memberId}</th>
          <th id={data.num} onClick={handleItemNameClick} className="mousePointer">{data.reportedId}</th>
          <th>{printDate(data.createDate)}</th>
        </tr>
      );
    });

  // 없으면 데이터가 존재하지 않는다고 알려주기
  } else {
    view = (
    <tr>
      <th colSpan={3}>
        작성한 글이 없습니다.
      </th>
    </tr>);
  }

  return (
    <Table hover>
      <thead className="table-primary">
        <tr>
          <th>신고한 회원아이디</th>
          <th>신고당한 회원아이디</th>
          <th>작성날짜</th>
        </tr>
      </thead>
      <tbody>
        {view}
      </tbody>
    </Table>
  )
}

export default CustomerServiceListBoxForAdminForm;