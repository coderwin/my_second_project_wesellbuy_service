import axios from 'axios';
import React, { createContext, useEffect, useState } from 'react';
import { Button, Col, Container, Row } from 'react-bootstrap';
import EmailForPwdForm from './EmailForPwdForm';
import PwdSearchAnswerForm from './PwdSearchAnswerForm';
import SelfphoneForPwdForm from './SelfphoneForPwdForm';

// 비밀번호 찾기 컨텍스트 생성
export const SearchPwdContext = createContext(null);

const PwdSearchForm = () => {

    // default value 모음
    const defaultData = {
        id: "",
        selfPhone: "",
        email: ""
    }

    /// 상태 모음
    const [loding, setLoding] = useState(false); // 로딩 상태
    const [error, setError] = useState(null); // 에러 상태
    const [data, setData] = useState(defaultData); 
    const [result, setResult] = useState(null);
    // 박스 만들기
    // searchBox, emailBox 만들기
    const [selfphoneBox, setSelfphoneBox] = useState(null);
    const [emailBox, setEmailBox] = useState(null);

    /// 메서드 모음
    // 휴대전화찾기 클릭
    function handleSelfphoneBtnClick() {
        // display none으로 고치기
        setSelfphoneBox(<SelfphoneForPwdForm />)
        setEmailBox(null);
    }
    // 이메일찾기 클릭
    function handleEmailBtnClick() {
        // display none으로 고치기
        setEmailBox(<EmailForPwdForm />);
        setSelfphoneBox(null);
    }
    // input 태그에 데이터 입력될 때 상태값 바꾸기
    const handleDataChange = (e) => {
        setData({
            ...data,
            [e.target.name]: e.target.value
        });
    }
    // 서버에 아이디 찾기 요청하기
    const findId = async () => {
        console.log("데이터 요청 중...");
        // 데이터 처리중
        setLoding(true);
        // json으로 바꾸기
        return await axios.get(
            "http://localhost:8080/members/find/pwd",
            {params: data}
        );
    }

    // 아이디 찾기 요청 결과 보여주기
    const handleSearchPwdSubmit = async (e) => {
        // 이벤트 실행 막기
        e.preventDefault();
        try {
            // 서버에 아이디 찾기 요청하기
            const resultData = await findId();
            // loding은 false
            setLoding(false);
            // 데이터 보여주기
            const {id, pwd} = resultData.data.data;// 유저 아이디 + 비밀번호
            // form으로 데이터 전달하기
            setResult(<PwdSearchAnswerForm id={id} pwd={pwd} />);

        } catch(error) {
            setLoding(false);
            // 에러 메시지 보내주기
            const errMsg = error.response.data.errMsg;
            setError(errMsg);
        }
    }

    // 처음은 휴대전화 찾기가 보인다
    useEffect(() => {
        setSelfphoneBox(<EmailForPwdForm />);
    }, []);

    // 서버 요청 성공 할 때 view
    if(result) {
        return (
            <>
                {result}
            </>
        );
    }

    return (
        <>
            <SearchPwdContext.Provider value={{loding, setLoding, handleDataChange, handleSearchPwdSubmit, error, data}}>
                <Container>
                    <Row className="d-flex justify-content-center">
                        <Col sm={2} className="mousePointer"  onClick={handleEmailBtnClick}>
                            <Button variant="outline-primary" size="sm">
                                이메일로찾기
                            </Button>
                        </Col>
                        <Col sm={2} className="mousePointer" onClick={handleSelfphoneBtnClick}>
                            <Button variant="outline-primary" size="sm">
                                휴대전화로찾기
                            </Button>
                        </Col>
                    </Row>
                    <Row className="d-flex justify-content-center">
                        <Col>
                            {selfphoneBox}
                            {emailBox}
                        </Col>    
                    </Row>
                </Container>
            </SearchPwdContext.Provider>
        </>
        
    )
}

export default PwdSearchForm;