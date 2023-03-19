import React, { useContext } from 'react';
import { Button, Col, Form, Row } from 'react-bootstrap';
import Loding from '../../Loding';
import { SearchIdContext } from './IdSearchForm';

export const EmailForm = () => {

  // 외부 상태, 변수, 메서드 불러오기
  const {loding, handleDataChange, handleSearchIdSubmit, error, data} = useContext(SearchIdContext);

  // 작업 처리 중일 때 view
  if(loding) return (<Loding />);

  return (
    <Form onSubmit={handleSearchIdSubmit}>
      <Row className="d-flex justify-content-center">
        <Col sm={4}>
          <Form.Group className="mb-3">
              <Form.Label>이름</Form.Label>
              <Form.Control type="text" name="name" value={data.name} placeholder="Enter NAME" onChange={handleDataChange} />
          </Form.Group>
        </Col>
      </Row>
      <Row className="d-flex justify-content-center">
        <Col sm={4}>
          <Form.Group className="mb-3">
              <Form.Label>이메일</Form.Label>
              <Form.Control type="text" name="email" value={data.email} placeholder="이메일" onChange={handleDataChange} />
          </Form.Group>
        </Col>
      </Row>  

      {/* errorMsg box */}
      <Row className="d-flex justify-content-center">
        <Col sm={4}>
          <Form.Group>
            <Col className="error">
              {error}
            </Col>
          </Form.Group>
        </Col>
      </Row>
      <Row className="d-flex justify-content-center">
        <Col sm={4} className="d-grid gap-2">
          <Button variant="primary" type="submit" >
          찾기
          </Button>
        </Col>
      </Row>
    </Form>
  )
}

export default EmailForm;
