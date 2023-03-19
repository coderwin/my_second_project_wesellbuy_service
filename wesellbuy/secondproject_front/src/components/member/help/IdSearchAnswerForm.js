import React from 'react'
import { Button, Col, Container, ListGroup, ListGroupItem, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const IdSearchAnswerForm = ({name, ids}) => {

  // navigation 생성
  const navigation = useNavigate();
  /// 메서드 모음
  // 비밀번호 찾기로 이동
  function handleSearchIdClick() {
    navigation("/help/search/pwd");
  }
  // 로그인으로 이동
  function handleLoginClick() {
    navigation("/login");
  }
  
  const result = ids.map((id, num) => {
    return (
      <Row key={num} className="d-flex justify-content-center">
        <Col sm={4}>
          <ListGroupItem>
          {id}
          </ListGroupItem>
        </Col>
      </Row>
    );
  });
  return (
    <Container>
      <ListGroup>   
        <Row className="d-flex justify-content-center">
          <Col sm={4}>
            <ListGroupItem>
              {name}님의 아이디는
            </ListGroupItem>
          </Col>
        </Row>
        {result}
        <Row className="d-flex justify-content-center">
          <Col sm={2}>
            <Button onClick={handleSearchIdClick}>비밀번호찾기</Button>
          </Col>
          <Col sm={2}>
            <Button onClick={handleLoginClick}>로그인</Button>
          </Col>  
        </Row>
      </ListGroup>
    </Container>
  )
}

export default IdSearchAnswerForm