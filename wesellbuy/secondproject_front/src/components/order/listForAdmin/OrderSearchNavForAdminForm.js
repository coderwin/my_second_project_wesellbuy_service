import { useContext } from "react";
import { Button, Col, Container, Form, Nav, Navbar, Row } from "react-bootstrap";
import { OrderListForAdminContext } from "../OrderListForAdminForm";

/**
 * Order list search for amdin component
 * writer : 이호진
 * init : 2023.03.28
 * updated by writer :
 * update :
 * description : 주문 검색 관리자용 component
 */
const OrderSearchNavForAdminForm = () => {

    /// 변수 모음
    // 외부의 변수 불러오기
    const {data, handleDataChange, handleSearchClick} = useContext(OrderListForAdminContext);
    // input 달력(Datapicker) 환경설정 변수
    const dateOptions = {
      alloInvalid: true,
      maxDate: new Date(),// 오늘 날짜로 설정
      minDate: "2000-01-02",
      formatYear: 'yy'
    }
    const sizeValues = [20, 30, 40]; // 페이지 sizeValues 배열 변수
    const orderStatusValues = ["", "O", "C"];// 주문상태 values 모음
    const orderStatusNames = ["선택", "주문", "취소"];// 주문상태 names 모음
    const deliveryStatusValues = ["", "R", "T", "C", "O"];// 배달상태 values 모음
    const deliveryStatusNames = ["선택","준비중", "배송중", "배송완료", "배송취소"];// 배달상태 names 모음
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
            {/* 주문자 아이디 */}
            <Form className="d-flex">
                <Form.Control
                type="search"
                placeholder="주문자 아이디"
                className="me-2"
                aria-label="Search"
                name="memberId"
                value={data.memberId}
                onChange={handleDataChange}
                />
            </Form>
            {/* 주문 상태 search */}
            <Form className="d-flex">
              <Form.Select
                  as={Col}
                  sm="5" 
                  name="orderStatus"
                  value={data.orderStatus} 
                  onChange={handleDataChange}
                >
                  {
                    orderStatusValues.map((value, i) => {
                      return (value === data.orderStatus ? 
                      <option key={i} value={value}>{orderStatusNames[i]}</option> 
                      : <option key={i} value={value}>{orderStatusNames[i]}</option>);
                    })
                  }
              </Form.Select>
            </Form>
            {/* 배달 상태 search */}
            <Form className="d-flex">
              <Form.Select
                  as={Col}
                  sm="5" 
                  name="deliveryStatus"
                  value={data.deliveryStatus}
                  onChange={handleDataChange}
                >
                  {
                    deliveryStatusValues.map((value, i) => {
                      return (value === data.deliveryStatus ? 
                      <option key={i} value={value}>{deliveryStatusNames[i]}</option> 
                      : <option key={i} value={value}>{deliveryStatusNames[i]}</option>);
                    })
                  }
              </Form.Select>
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
  
  export default OrderSearchNavForAdminForm;