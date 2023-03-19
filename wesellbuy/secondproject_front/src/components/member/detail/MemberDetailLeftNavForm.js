import React from 'react'
import { Nav } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom';

/**
 * member detail nav component
 * writer : 이호진
 * init : 2023.03.13
 * updated by writer :
 * update :
 * description : 나의 상세정보, 주문 , 판매 확인 페이지로 이동할 수 있는 component
 */
const MemberDetailLeftNavForm = () => {
  
  /// 변수 모음
  // 외부의 변수 불러오기
  const navigation = useNavigate();// navigation
  /// 상태 모음

  /// 메서드 모음
  // 나의 상세보기로 이동
  function handleMyDetailClick() {
    navigation("/mydetail");
  }
  // 나의 주문보기로 이동
  function handleMyOrderClick() {
    navigation("/order/list");
  }
  // 나의 판매된 상품 보기로 이동
  function handleMySoldItemsClick() {
    navigation("/order/list/seller");
  }
  // 나의(내가 쓴) 고객지원 요청글 보기로 이동
  function handleMyCustomerServiceListClick() {
    navigation("/cs/list");
  }
  // 상품 등록으로 이동
  function handleNewItemClick() {
    navigation("/item/new");
  }
  // 추천합니다 등록으로 이동
  function handleNewRecommendationClick() {
    navigation("/recommendation/new");
  }
  
  /// view 모음
  
  return (
    <Nav className="flex-column">
      <Nav.Link onClick={handleMyDetailClick}>나의 정보</Nav.Link>
      <Nav.Link onClick={handleMyOrderClick}>나의 주문</Nav.Link>
      <Nav.Link onClick={handleMySoldItemsClick}>판매된 상품</Nav.Link>
      <Nav.Link onClick={handleMyCustomerServiceListClick}>나의 고객지원</Nav.Link>
      <Nav.Link onClick={handleNewItemClick}>상품등록</Nav.Link>
      <Nav.Link onClick={handleNewRecommendationClick}>추천합니다 작성</Nav.Link>
    </Nav>
  )
}

export default MemberDetailLeftNavForm;