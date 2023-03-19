import React, { createContext, useState } from 'react'
import { Col, Container, ListGroup, Row } from 'react-bootstrap'
import Loding from '../Loding';
import ItemRankBoxForm from './rank/ItemRankBoxForm';
import ItemRankSearchNavForm from './rank/ItemRankSearchNavForm'

/**
 * Item rank component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : 상품순위 목록 component
 */
export const ItemRankContext = createContext(null); // ItemRank Context

const ItemRankForm = () => {

  /// 변수 모음
  
  /// 상태 모음
  const [loding, setLoding] = useState(false);// 요청처리 상태
  /// 메서드 모음
  // 상품 순위 찾기 클릭했을 때
  // 상품의 위치를 찾아준다.
  function handleSearchClick(e) {
    // 이벤트 막기
    e.preventDefault();
    
    const value = e.target.rankName.value; // 클라이언트가 찾는 순위
    // 숫자만 출력하기
    const rankNum = getOnlyNumbmers(value);
    // a 태그 생성
    const Atag = document.createElement("a");
    // href 속성 생성
    Atag.setAttribute("href", `#${rankNum}`);
    // onClick 속성 생성
    // 바로 함수 실행하기 => 자동 클릭하기
    Atag.click();
  }
  // 숫자만 가져오기
  function getOnlyNumbmers(value) {
    // pattern 만든다
    // 모든 숫자를 제외한 문자만을 찾기 
    const pattern = /[^0-9]/g;
    // 숫자만 추출하기
    // pattern에 걸린 것은 ""로 처리하기
    const result = value.replace(pattern, "");
    console.log(`result : ${result}`);

    return result;
  }

  /// view 모음

  // 요청 처리 view
  if(loding) return(<Loding />);

  return (
    <ItemRankContext.Provider value={{handleSearchClick, setLoding}}>
      <Container className="body_text_center">
        <Row className="d-flex justify-content-center">
          <Col sm={10}>
            {/* 순위 찾기 Nav */}
            <Row>
              <Col md="12">
                {/* 위쪽 Nav - 검색 */}
                <ItemRankSearchNavForm />
              </Col>
            </Row>
            {/* 순위 목록 box */}
            <Row id="top">
              <Col md="12">
                {/* body - 상품 목록  */}
                <ItemRankBoxForm />
              </Col>
            </Row>
            {/* 맨위로 이동하기 */}
            <Row className="footerFixed mousePointer body_text_right">
              <Col>
                <a href="#top">맨위로</a>
              </Col>
            </Row>
          </Col>
        </Row>
      </Container>
    </ItemRankContext.Provider> 
  )
}

export default ItemRankForm