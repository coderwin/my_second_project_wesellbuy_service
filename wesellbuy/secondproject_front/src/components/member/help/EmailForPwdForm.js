import React, { useContext } from 'react';
import { Button, Col, Form, Row } from 'react-bootstrap';
import Loding from '../../Loding';
import { SearchPwdContext } from './PwdSearchForm';

export const EmailForPwdForm = () => {

  // 외부 상태, 변수, 메서드 불러오기
  const {loding, handleDataChange, handleSearchPwdSubmit, error, data} = useContext(SearchPwdContext);

  // 작업 처리 중일 때 view
  if(loding) return (<Loding />);

  return (
    <Form onSubmit={handleSearchPwdSubmit}>
      <Row className="d-flex justify-content-center">
        <Col sm={4}>
          <Form.Group className="mb-3" controlId="formBasicEmail">
              <Form.Label>아이디</Form.Label>
              <Form.Control type="text" placeholder="Enter ID" name="id" value={data.id} onChange={handleDataChange} />
          </Form.Group>
        </Col>
      </Row>

      <Row className="d-flex justify-content-center">
        <Col sm={4}>
          <Form.Group className="mb-3" controlId="formBasicPassword">
              <Form.Label>이메일</Form.Label>
              <Form.Control type="text" placeholder="이메일" name="email" value={data.email} onChange={handleDataChange} />
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
          <Button variant="primary" type="submit">
          찾기
          </Button>
        </Col>
      </Row>

        
    </Form>
  )
}

export default EmailForPwdForm;
