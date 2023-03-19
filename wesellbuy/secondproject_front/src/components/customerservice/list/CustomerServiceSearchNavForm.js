import React, { useContext } from 'react'
import { Button, Col, Container, Form, Nav, Navbar, Row } from 'react-bootstrap';
import { CustomerServiceListContext } from '../CustomerServiceListForm';

/**
 * CustomerService list search component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : 고객지원글 검색 component
 */
const CustomerServiceSearchNavForm = () => {

  /// 변수 모음
  // 외부의 변수 불러오기
  const {data, handleDataChange, handleSearchClick} = useContext(CustomerServiceListContext);
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
    <Navbar bg="light" expand="lg">
      <Container fluid>
        <Navbar.Brand href="#">Search</Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav>
          </Nav>
          {/* 신고한 회원아이디 search */}
          <Form className="d-flex">
            <Form.Control
              type="search"
              placeholder="신고한 회원아이디"
              className="me-2"
              aria-label="Search"
              name="reportedId"
              value={data.reportedId}
              onChange={handleDataChange}
            />
          </Form>
          <Form className="d-flex">
            {/* 등록 날짜 search */}
            {/* 작동할 지 모르겟군 -> 확인해보자 */}
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

export default CustomerServiceSearchNavForm;