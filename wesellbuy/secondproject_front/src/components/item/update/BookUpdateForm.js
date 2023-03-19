import React, { useContext } from 'react'
import { Col, Form, Row } from 'react-bootstrap';
import { ItemUpdateContext } from '../ItemUpdateForm';

/**
 * Item save의 type=B component
 * writer : 이호진
 * init : 2023.03.09
 * updated by writer :
 * update :
 * description : ItemUpdateForm에서 type=B(Book)일 때 component
 */
const BookUpdateForm = () => {
  // 외부 변수, 상태, 메서드 불러오기
  const {data, errMsgs, handleDataChange} = useContext(ItemUpdateContext);

  return (
    <>
      {/* 저자 */}
      <Form.Group
        as={Row}
        className="mb-3"
      >
        <Form.Label column sm="2">
          AUTHOR <span className='important'>*</span>
        </Form.Label>
        <Col sm="10">
          <Form.Control
            type="text"
            name="author"
            value={data.author}
            onChange={handleDataChange}
          />
        </Col>
        {/* 에러 메시지 */}
        <Col className="error">
          {errMsgs.author}
        </Col>
      </Form.Group>
      {/* 출판사 */}
      <Form.Group
        as={Row}
        className="mb-3"
      >
        <Form.Label column sm="2">
          PUBLISHER <span className='important'>*</span>
        </Form.Label>
        <Col sm="10">
          <Form.Control
            type="text"
            name="publisher"
            value={data.publisher}
            onChange={handleDataChange}
          />
        </Col>
        {/* 에러 메시지 */}
        <Col className="error">
          {errMsgs.publisher}
        </Col>
      </Form.Group>
    </>
  )
}

export default BookUpdateForm;