import React from 'react';
import { Pagination } from 'react-bootstrap';


/**
 * Item list pageButton component
 * writer : 이호진
 * init : 2023.03.09
 * updated by writer : 이호진
 * update : 2023.03.13
 * description : 목록 페이지 버튼 component
 *                > props
 *                  - data: 검색 데이터 모음 prop
 *                  - handleDataChange: data value 바꾸는 메서드 prop
 *                  - totalPages: 전체페이지 prop
 * 
 * comment : value가 잘 작동할지 모르겠다
 *           id로 바꿔야 될 수도
 * 
 * update : result = null, undefined -> result = []
 */
const PageButtonForm = ({data, handleDataChange, totalPages}) => {

  /// 변수 모음

  //// 상태 모음

  //// 메서드 모음

  //// view 모음
  /// page Number view 만들기
  // page Number 만들기 필요 변수
  const startPage = 1;// 시작 페이지 -> 사용 안 함
  const endPage = totalPages;// 끝 페이지
  const showedRangeOfPage = 5; // 페이지가 보이는 범위
  const nowPage = Number(data.page); // 현재 페이지
  const helperPage = data.page; // 페이지 번호를 만들 때 필요한 변수 -> 사용 안 함
  // 페이지 만드는 메서드
  function createPages() {
    // 출력 결과 담는 그릇 변수
    let result = [];
    const resultNextDirectionNum = Math.ceil(nowPage + 1 / showedRangeOfPage); // 페이징박스를 하나의 페이지로 보는 numbe
    const endDirectionNum = Math.ceil(endPage / showedRangeOfPage);// 마지막 페이지의 페이지 위치 number

    /// endPage 0 일 때 출력물 없음
    if(endPage === 0) {
      // console.log(`endPage : ${endPage}`);  
      return result;
    }
    /// 게시글 1이상일 때
    // endPage 1 일 때
    if(endPage === 1) {
      // console.log(`endPage : ${endPage}`);
      result.push(<Pagination.Item active name="page" key={nowPage} id={nowPage}>{nowPage + 1}</Pagination.Item>);
      return result;
    }

    // 1 < endPage 아닐 때
    const firstPageInPagingBox = Math.floor((nowPage) / showedRangeOfPage); // 페이징박스에서 첫번째 페이지 number
    let nextPagingBox = 0; // 페이징박스로 이동 변수

    if(firstPageInPagingBox !== 0) {
      nextPagingBox = (firstPageInPagingBox - 1) * showedRangeOfPage + 1;
    } else {
      nextPagingBox = firstPageInPagingBox * showedRangeOfPage + 1;
    }

    // showedRangeOfPage와 같거나 작을 때
    if(nowPage > showedRangeOfPage) {
      // 이전 페이징 박스로 이동
      result.push(<Pagination.Prev key={nextPagingBox} name="page" id={nextPagingBox} onClick={handleDataChange}/>);
    }

    // 중간 부분(페이지 번호가 나오는 부분)
    let stopLine = 1; // 반복문 제어 변수;
    let countPage = firstPageInPagingBox * showedRangeOfPage + 1;// 페이지 번호가 보이게 도와주는 변수

    // showedRangeOfPage만큼 반복한다.
    for(stopLine = 1; stopLine <= showedRangeOfPage; stopLine++) {
      // helperPage가 endPage보다 크면 나가기
      if(countPage > endPage) {
        break;
      }

      // 현재 페이지는 다르게 표현
      if(countPage === nowPage + 1) {
        result.push(<Pagination.Item key={countPage} name="page" active>{countPage}</Pagination.Item>);
      } else {
        result.push(<Pagination.Item key={countPage} name="page" id={countPage - 1} onClick={handleDataChange}>{countPage}</Pagination.Item>);
      }

      // 페이지 번호 1씩 증가시키기
      countPage += 1;
    }

    // 끝 부분
    if(resultNextDirectionNum < endDirectionNum) {  
      // 다음 페이징박스로 이동 >
      if(nowPage <= showedRangeOfPage) {
        result.push(<Pagination.Next key={(nextPagingBox + showedRangeOfPage * 1) - 1} id={(nextPagingBox + showedRangeOfPage * 1) - 1} onClick={handleDataChange} />);
      } else {
        result.push(<Pagination.Next key={(nextPagingBox + showedRangeOfPage * 2) - 1} id={(nextPagingBox + showedRangeOfPage * 2) - 1} onClick={handleDataChange} />);
      }
    }

    return result;
  }

  return (
    <Pagination>
      {createPages()}
    </Pagination>
  )
}

export default PageButtonForm