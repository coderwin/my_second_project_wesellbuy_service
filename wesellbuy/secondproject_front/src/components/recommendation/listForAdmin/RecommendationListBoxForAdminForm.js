import { useContext } from "react";
import { Table } from "react-bootstrap";
import { RecommendationListForAdminContext } from "../RecommendationListForAdminForm";
import RecommendationForAdminForm from "./RecommendationForAdminForm";

/**
 * Recommendation list box for admin component
 * writer : 이호진
 * init : 2023.03.28
 * updated by writer :
 * update :
 * description : 추천합니다 목록 box 관리자용 component
 */
const RecommendationListBoxForAdminForm = () => {
  
  /// 변수 모음
  // 외부의 변수 불러오기
  const {listDatas, data: searchCond, totalPages} = useContext(RecommendationListForAdminContext);

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
        <RecommendationForAdminForm 
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
          추천상품이 없습니다.
        </th>
      </tr>
    );
  }

  return (
    <Table hover>
      <thead className="table-primary">
        <tr>
          <th>순서</th>
          <th>추천상품</th>
          <th>판매자</th>
          <th>작성자</th>
          <th>상태</th>
          <th></th>{/* 삭제 버튼 */}
        </tr>
      </thead>
      <tbody>
        {view}
      </tbody>
    </Table>
  )
}

export default RecommendationListBoxForAdminForm;