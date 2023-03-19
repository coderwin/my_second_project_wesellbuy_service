import React, { useContext, useEffect, useState } from 'react'
import { Container, Nav, Navbar, NavDropdown } from 'react-bootstrap'
import { Link, useNavigate } from 'react-router-dom'
import '../../css/googlefont.css';
import '../../css/form.css';
import { CustomContext } from '../../App';


const HeaderNav = () => {

  // 이동하기 만들기
  const navigation = useNavigate();
  // 홈으로
  const handleHomeClick = () => {
    navigation("/");
  }
  // 추천합니다
  const handleRecommendationClick = () => {
    navigation("/recommendation/list");
  }
  // 고객지원
  const handleCustomerServiceClick = () => {
    navigation("/cs/new");
  }
  // 상품등록
  const handleItemSaveClick = () => {
    navigation("/item/new");
  }
  // 배달목록
  const handleDeliveryListClick = () => {
    navigation("/order/list/deliver");
  }

  /// 상태 모음
  const [memberInfo, setMemberInfo] = useState(null);
  // 외부 변수, 상태, 메서드 불러오기
  const {sessionForm} = useContext(CustomContext);

  /// 메서드 모음

  /// 처음 시작
    // 왜 로그인 유지되는지는 모르겠다
  useEffect(() => {
    // memberInfo 불러오기
    const getMemberInfo = () => {
        const key = "LOGIN_MEMBER";
        const memberInfo = JSON.parse(sessionStorage.getItem(key));
        // setMemberInfo에 담기
        setMemberInfo(memberInfo);
    }
    getMemberInfo();
  }, [sessionForm]);

  /// view 모음
  let view = null;
  // member의 아이디에 deliver로 시작하면 Nav 보이기
  const pattern = /^deliver[\w]*$/;
  if(memberInfo) {
    if(pattern.test(memberInfo.id)) {
      view = <Nav.Link onClick={handleDeliveryListClick}>배달목록</Nav.Link>;
    }
  }

  return (
    <Navbar bg="light" expand="lg">
      <Container>
      <Navbar.Brand className="googlefont mitr mousePointer" onClick={handleHomeClick} >WeSellBuy</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <NavDropdown title="상품" id="basic-nav-dropdown">
              <NavDropdown.Item onClick={handleItemSaveClick}>
                상품등록
              </NavDropdown.Item> 
            </NavDropdown>
            <Nav.Link onClick={handleRecommendationClick}>추천합니다</Nav.Link>
            <Nav.Link onClick={handleCustomerServiceClick}>고객지원</Nav.Link>
            {/* 배달목록 */}
            {view}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  )
}

export default HeaderNav;