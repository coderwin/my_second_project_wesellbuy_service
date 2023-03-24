import React, { useContext } from 'react'
import { Button, Col, Container, Form, Nav, Navbar, Row } from 'react-bootstrap';
import { ItemListContext } from '../ItemListForm';

/**
 * Item list search component
 * writer : 이호진
 * init : 2023.03.09
 * updated by writer :
 * update :
 * description : 상품 검색 component
 */
const ItemSearchNavForm = () => {

  /// 변수 모음
  // 외부의 변수 불러오기
  const {data, handleDataChange, handleSearchClick} = useContext(ItemListContext);
  // type에 들어가는 상품종류 모음
  const dtypeValues = ["", "B", "F", "HA", "ITEM"];
  const dtypeNames = ["선택", "책", "가구", "가전제품", "기타"];
  // input 달력(Datapicker) 환경설정 변수
  const dateOptions = {
    alloInvalid: true,
    maxDate: new Date(),// 오늘 날짜로 설정
    minDate: "2000-01-02",
    formatYear: 'yy'
  }
  // 페이지 sizeValues 배열 변수
  const sizeValues = [10, 15, 20, 30];
  /// 상태 모음

  /// 메서드 모음

  /// view 모음


  return (
    <Navbar expand="lg" className="search_navbar">
      <Container fluid>
        <Navbar.Brand href="#">Search</Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav>
          </Nav>
          {/* 상품명 search */}
          <Form className="d-flex">
            <Form.Control
              type="search"
              placeholder="상품명"
              className="me-2"
              aria-label="Search"
              name="name"
              value={data.name}
              onChange={handleDataChange}
            />
          </Form>
          {/* 상품 등록 아이디 search */}
          <Form className="d-flex">
            <Form.Control
              type="search"
              placeholder="판매자 아이디"
              className="me-2"
              aria-label="Search"
              name="memberId"
              value={data.memberId}
              onChange={handleDataChange}
            />
          </Form>
          {/* 등록 날짜 search */}
            {/* 작동할 지 모르겟군 -> 확인해보자 */}
          <Form className="d-flex">
            <Form.Control
              type="date"
              datepicker-popup=""
              ng-model="dt"
              is-open="opened"
              min="2023-03-09"
              max="2030-12-30"
              datepicer-options="dateOptions"
              className="me-2"
              aria-label="Search"
              name="createDate"
              value={data.createDate}
              onChange={handleDataChange}
            />
          </Form>
          {/* 상품 종류 search */}
          <Form className="d-flex">
            <Form.Select
                as={Col}
                sm="5" 
                name="dtype"
                value={data.dtype}
                onChange={handleDataChange}
              >
                {
                  dtypeValues.map((value, i) => {
                    return (value === data.dtype ? 
                    <option key={i} value={value}>{dtypeNames[i]}</option> 
                    : <option key={i} value={value}>{dtypeNames[i]}</option>);
                  })
                }
            </Form.Select>
            <Button type="button" onClick={handleSearchClick}>Search</Button>
          </Form>  
        </Navbar.Collapse>
        {/* 페이지 사이즈 설정 */}
        <Row>
          <Col>
            <Form className="d-flex">
              <Form.Select
                  as={Col}
                  sm="5" 
                  name="size" 
                  value={data.size}
                  onChange={handleDataChange}
                >
                  {
                    sizeValues.map((value, i) => {
                      return (value === data.size ? 
                      <option key={i} value={value}>{value}</option> 
                      : <option key={i} value={value}>{value}</option>);
                    })
                  }
              </Form.Select>
            </Form>
          </Col>
        </Row>
      </Container>
    </Navbar>
  )
}

export default ItemSearchNavForm;