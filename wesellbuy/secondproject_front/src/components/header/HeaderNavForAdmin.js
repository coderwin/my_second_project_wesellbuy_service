import { useState } from "react";
import { Container, Nav, Navbar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const HeaderNavForAdmin = () => {

  // 이동하기 만들기
  const navigation = useNavigate();
  // 홈으로 === 회원목록
  const handleHomeClick = () => {
    navigation("/");
  }
  // 회원목록
  const handleMemberListClick = () => {
    navigation("/admin/member/list");
  }
  // 상품목록
  const handleItemListClick = () => {
    navigation("/admin/item/list");
  }
  // 주문목록
  const handleOrderListClick = () => {
    navigation("/admin/order/list");
  }
  // 추천합니다목록
  const handleRecommendationListClick = () => {
    navigation("/admin/recommendation/list");
  }
  // 고객지원목록
  const handleCustomerServiceListClick = () => {
    navigation("/admin/cs/list");
  }

  /// 상태 모음

  /// 메서드 모음

  /// view 모음

  return (
    <Navbar bg="light" expand="lg">
      <Container>
      <Navbar.Brand className="googlefont mitr mousePointer" onClick={handleHomeClick} >WeSellBuy</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            {/* 회원목록 */}
            <Nav.Link onClick={handleMemberListClick}>회원목록</Nav.Link>
            {/* 상품목록 */}
            <Nav.Link onClick={handleItemListClick}>상품목록</Nav.Link>
            {/* 주문목록 */}
            <Nav.Link onClick={handleOrderListClick}>주문목록</Nav.Link>
            {/* 추천합니다목록 */}
            <Nav.Link onClick={handleRecommendationListClick}>추천합니다목록</Nav.Link>
            {/* 고객지원목록 */}
            <Nav.Link onClick={handleCustomerServiceListClick}>고객지원목록</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  )
}

export default HeaderNavForAdmin;