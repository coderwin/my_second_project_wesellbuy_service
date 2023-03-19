import React, { useContext, useEffect, useState } from 'react'
import { Button, Col, Form, ListGroup, Row } from 'react-bootstrap'
import { useNavigate, useParams } from 'react-router-dom';
import { CustomContext } from '../../../App';

/**
 * reply writing component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : 댓글 등록/작성 component
 */
const ReplyRecommendationWritingForm = ({saveReply}) => {

  /// 변수 모음
  const defaultData = {
    content: ""// 댓글 내용
  }
  // URI에서 상품번호 받아오기
  const {num: boardNum} = useParams();
  // navigation
  const navigation = useNavigate();
  const {setLoding: setAllLoding} = useContext(CustomContext);

  /// 상태 모음
  const [data, setData] = useState(defaultData);// 데이터 상태
  const [login, setLogin] = useState(false);// 로그인 상태
  const [memberInfo, setMemberInfo] = useState(null);// 로그인 사용자 정보 상태

  /// 메서드 모음
  // data의 속성 데이터 변경
  function handleDataChange(e) {
    setData({
      ...data,
      [e.target.name]: e.target.value
    });
  }
  // 로그인 사용자가 댓글 등록 버튼 클릭했을 때
  async function handleSaveClick() {
    setAllLoding(true);// 전체 loding
    try {
      // 서버로 댓글 등록 요청 하기
      const response = await saveReply(boardNum, data);
      // 요청 성공
      setAllLoding(false);
      console.log("요청 성공");
      alert(response.data.data);
      // 데이터는 비우기
      setData(
        defaultData
      );
    } catch(err) {
      // 요청 실패
      setAllLoding(false);
      console.log("요청 실패");
      console.log(err);
    }
  }
  // replyFormList에서 reply 등록하기 => 사용 안 함(보류중)
    // 서버 저장시 view로 reply 번호 보내주는 것도 생각하기
  function handleSaveRepliesChange(data, replyNum) {
    // reply의 속성을 모두 갖춘 reply객체 만들기
    const reply = {
      ...data,
      createDate: "",
      num: replyNum,
      memberId: "" 
    }
    // // replies에 추가하기
    // setReplies([
    //   reply,
    //   ...replies
    // ]);
  }

  // 비로그인 사용자가 댓글 등록 버튼 클릭했을 때
  function handleSaveByNoLoginClick() {
    alert("로그인 후 이용해 주세요");
    return;
  }

  // 처음 시작
  useEffect(() => {
    // sessionStorage에 key="LOGIN_MEMBER" 있는지 확인
    const key = "LOGIN_MEMBER";
    const memberData = JSON.parse(sessionStorage.getItem(key));
    // 데이터가 없으면
    if(memberData === null || memberData === undefined) {
      // login 상태는 false
      setLogin(false);
    } else {
      // login 상태 true
      setLogin(true);
      // memberInfo에 memberData 대입
      setMemberInfo(memberData);
    }
  }, []);

  /// view 모음
  // 로그인 사용자 일 때 -> login=ture
  const loginUserView = (
    <>
      <ListGroup.Item>
        <Row>
          {/* 작성자 아이디 */}
          <Col sm="2" className="align-self-center">
            {memberInfo !== null ? memberInfo.id : ""}
          </Col>
          {/* 내용 */}
          <Col sm="10">
            <Form.Control
            as="textarea"
            name="content"
            rows={5}
            value={data.content}
            placeholder="댓글을 입력하세요"
            onChange={handleDataChange}
            />
          </Col>
        </Row> 
      </ListGroup.Item>
      {/* 버튼 box */}
      <ListGroup.Item>
        <Row className="d-flex justify-content-center">
          <Col sm={3} className="d-grid gap-2" >
            <Button variant="outline-primary" onClick={handleSaveClick}>등록</Button>
          </Col>
        </Row>
      </ListGroup.Item>
    </>
  );
  // 비로그인 사용자 일 때 -> login=false
  const noLoginUserView = (
    <>
      <ListGroup.Item>
        <Row>
          {/* 작성자 아이디 */}
          <Col sm="3" className="align-self-center">
            <span>비어있음</span>
          </Col>
          <Col sm="9">
            {/* 내용 */}
            <Form.Control
              as="textarea"
              name="content"
              rows={10}
              value={data.content}
              placeholder="댓글을 입력하세요"
              onChange={handleDataChange}
            />
          </Col>
        </Row>
      </ListGroup.Item>
      {/* 버튼 box */}
      <ListGroup.Item>
        <Row className="d-flex justify-content-center">
          <Col sm={3} className="d-grid gap-2" >
            <Button onClick={handleSaveByNoLoginClick}>등록</Button>
          </Col>
        </Row>
      </ListGroup.Item>
    </>
  );
  // view 모양 정하기
  let view = ""; // 뷰의 모양을 정한다.
  // mode에 따른 view 정하기
  if(login) {
    view = loginUserView;
  } else {
    view = noLoginUserView;
  }

  return (
    <ListGroup>
      {view}
    </ListGroup>
  )
}

export default ReplyRecommendationWritingForm;