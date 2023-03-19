import React, { useContext } from 'react'
import {  Col, ListGroup, ListGroupItem, Row, Table } from 'react-bootstrap';
import { OrderDetailContext } from '../OrderDetailForm';
import OrderItemList from './OrderItemList';

/**
 * Order detail 내용 component
 * writer : 이호진
 * init : 2023.03.12
 * updated by writer :
 * update :
 * description : 주문 상세보기 내용 box component
 */
const CustomerServiceDetailBoxForm = () => {

  /// 변수 모음
  const {data} = useContext(OrderDetailContext);// 외부의 변수, 상태, 메서드 불러오기
  const orderStatusValues = ["ORDER", "CANCEL"];// 주문상태 values 모음
  const orderStatusNames = ["주문", "취소"];// 주문상태 names 모음
  const deliveryStatusValues = ["READY", "TRANSIT", "COMPLETE", "OVER"];// 배달상태 values 모음
  const deliveryStatusNames = ["준비중", "배송중", "배송완료", "배송취소"];// 배달상태 names 모음

  /// 상태 모음

  /// 메서드 모음

  /// view 모음
  let orderStatusView = null;// 주문상태 출력 변수
  let deliveryStatusView = null;// 배달상태 출력 변수
  let addressBoxView = "";// 배송지 박스 출력 변수
  let orderItemsBoxView = "";// 주문상품 목록 출력 변수

  // orderStatusView 결정
  for(let i = 0; i < orderStatusValues.length; i++) {
    if(orderStatusValues[i] === data.orderStatus) {
      orderStatusView = orderStatusNames[i];
    }
  }

  // deliveryStatusView 결정
  for(let i = 0; i < deliveryStatusValues.length; i++) {
    if(deliveryStatusValues[i] === data.deliveryStatus) {
      deliveryStatusView = deliveryStatusNames[i];
    }
  }

  // addressBoxView 결정
  // 주소 있을 때
  if(data.address) {
    for(let key in data.address) {
      addressBoxView += data.address[key] + " ";
    }
  // 주소 없을 때  
  } else {
    addressBoxView = "주소 없음";
  }

  // orderItemsBoxView 결정
  // 주문상품이 있을 때
  if(data.orderItemDetailList) {
    orderItemsBoxView = data.orderItemDetailList.map((orderItem, i) => {
      return <OrderItemList key={i} orderItem={orderItem} num={i} />
    });
  } else {
    orderItemsBoxView = 
      <tr>
        <th>주문상품이 없습니다.</th>
      </tr>
  }

  return (
    <>
      <ListGroup as="ul">
        {/* 신고된 회원 아이디 */}
        <ListGroupItem className="list-group-item-dark">
          <Row>
            <Col md="6">주문번호</Col>
            <Col md="6">{data.num}</Col>
          </Row>
        </ListGroupItem>
        {/* 결제금액 */}
        <ListGroupItem >
          <Row>
            <Col md="6">결제금액(원)</Col>
            <Col md="6">{data.totalPrice}</Col>
          </Row>
        </ListGroupItem>
        {/* 주문상태 */}
        <ListGroupItem>
          <Row>
            <Col md="6">주문상태</Col>
            <Col md="6">{orderStatusView}</Col>
          </Row>
        </ListGroupItem>
        {/* 배달상태 */}
        {/* 작성자 */}
        <ListGroupItem >
          <Row>
            <Col md="6">회원아이디</Col>
            <Col md="6">{data.id}</Col>
          </Row>
        </ListGroupItem>
        <ListGroupItem>
          <Row>
            <Col md="6">배달상태</Col>
            <Col md="6">{deliveryStatusView}</Col>
          </Row>
        </ListGroupItem>
        {/* 주문 회원연락처 */}
        <ListGroupItem>
          <Row>
            <Col md="3">휴대전화</Col>
            <Col md="3">{data.memberPhone.selfPhone}</Col>
            <Col md="3">집전화</Col>
            <Col md="3">{data.memberPhone.homePhone}</Col>
          </Row>
        </ListGroupItem>
        {/* 배송주소 */}
        <ListGroupItem>
          <Row>
            <Col md="3">배송주소</Col>
            <Col md="9">{addressBoxView}</Col>
          </Row>
        </ListGroupItem>
        {/* 주문상품 정보 모음 */}
        <ListGroupItem>
          <Table hover>
            <thead className="table-primary">
              <tr>
                <th>순서</th>
                <th>상품명</th>
                <th>주문수량</th>
                <th>상품가격(원)</th>
              </tr>
            </thead>
            <tbody>
              {orderItemsBoxView}
            </tbody>
          </Table>
        </ListGroupItem>
      </ListGroup>
    </>
  )
}

export default CustomerServiceDetailBoxForm;