import React, { useContext} from 'react';
import { Col, ListGroup, ListGroupItem, Row } from 'react-bootstrap';
import { CustomerServiceDetailContext } from '../CustomerServiceDetailForm';
import '../../../css/googlefont.css';

/**
 * CustomerService detail 내용 component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : 고객지원글 상세보기 내용 box component
 */
const CustomerServiceDetailBoxForm = () => {

  /// 변수 모음
  const {data} = useContext(CustomerServiceDetailContext);// 외부의 변수, 상태, 메서드 불러오기

  /// 상태 모음

  /// 메서드 모음
  // 시간 날짜만 나오게 하기
  function printDate(datetime) {
    // 날짜시간 받아서 
      // T부분에서 cut
    const datetimeArr = datetime.split("T");
    return datetimeArr[0];
  }

  /// view 모음

  return (
    <>
      <ListGroup as="ul">
        {/* 신고된 회원 아이디 */}
        <ListGroupItem className="body_text_center">
          <Row>
            <Col md="3">신고된 회원 아이디</Col>
            <Col md="8">{data.reportedId}</Col>
          </Row>
        </ListGroupItem>
        {/* 작성자 */}
        <ListGroupItem className="body_text_center">
          <Row>
            <Col md="3">작성자</Col>
            <Col md="8">{data.memberId}</Col>
          </Row>
        </ListGroupItem>
        {/* 작성 날짜 */}
        <ListGroupItem className="body_text_center">
          <Row>
            <Col md="3">작성날짜</Col>
            <Col md="8">{printDate(data.createDate)}</Col>
          </Row>
        </ListGroupItem>
        {/* 신고 이유 */}
        <ListGroupItem className="originContent">
          {data.content}
        </ListGroupItem>
      </ListGroup>
    </>
  )
}

export default CustomerServiceDetailBoxForm;