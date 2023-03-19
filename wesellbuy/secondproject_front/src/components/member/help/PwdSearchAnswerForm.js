import React from 'react'
import { Button, Col, Container, ListGroup, ListGroupItem, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const PwdSearchAnswerForm = ({id, pwd}) => {

  // navigation 생성
  const navigation = useNavigate();
  /// 메서드 모음
  
  // 로그인으로 이동
  function handleLoginClick() {
    navigation("/login");
  }
  

  return (
    <Container>
      <ListGroup>
      <Row className="d-flex justify-content-center">
        <Col sm={3}>
          <ListGroupItem>
            {id}님의 아이디는
          </ListGroupItem>
        </Col>
      </Row>
      <Row className="d-flex justify-content-center">
        <Col sm={3}>
          <ListGroupItem>
            {pwd}
          </ListGroupItem>
        </Col>
      </Row>
      <Row className="d-flex justify-content-center">
        <Col sm={3} className="d-grid gap-2">
          <Button onClick={handleLoginClick} size="lg">로그인</Button>
        </Col>  
      </Row>
      </ListGroup>
    </Container>
  )
}

export default PwdSearchAnswerForm