import React, { useContext } from 'react'
import { Col, Form, Row } from 'react-bootstrap';
import { ItemUpdateContext } from '../ItemUpdateForm';

/**
 * Item save의 type=HA component
 * writer : 이호진
 * init : 2023.03.09
 * updated by writer :
 * update :
 * description : ItemUpdateForm에서 type=HA(HomeAppliance)일 때 component
 */
const HomeApplianceUpdateForm = () => {
  // 외부 변수, 상태, 메서드 불러오기
  const {data, errMsgs, handleDataChange} = useContext(ItemUpdateContext);

  return (
    <>
      {/* 제조회사 */}
      <Form.Group
        as={Row}
        className="mb-3"
      >
        <Form.Label column sm="2">
          COMPANY <span className='important'>*</span>
        </Form.Label>
        <Col sm="10">
          <Form.Control
            type="text"
            name="company"
            value={data.company}
            onChange={handleDataChange}
          />
        </Col>
        {/* 에러 메시지 */}
        <Col className="company">
          {errMsgs.author}
        </Col>
      </Form.Group>
    </>
  )
}

export default HomeApplianceUpdateForm;