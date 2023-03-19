import axios from 'axios';
import React, { useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { Nav } from 'react-bootstrap'
import { json, useNavigate } from 'react-router-dom';
import { CustomContext } from '../../App';
import Loding from '../Loding';

const HeaderTop = () => {

    // 외부 변수, 상태, 메서드 불러오기
    const {sessionForm, changeLoding} = useContext(CustomContext);
    const navigation = useNavigate();

    // 상태 모음
    // 로딩/작업진행중 상태
    const [loding, setLoding] = useState(false); 
    const [memberInfo, setMemberInfo] = useState(null);

    /// 메서드 모음
    // /login으로 가기
    const handleLoginClick = () => {
        navigation("/login");
    }
    // /join으로 가기
    const handleJoinClick = () => {
        navigation("/join");
    }
    // /mydetail로 가기
    const handleMydetailClick = () => {
        navigation("/mydetail");
    }
    // 장바구니 가기
    const handleOrderClick = () => {
        navigation("/order/new");
    }
    // 서버에 로그아웃 요청하기
    async function logout() {
        // loding 상태 true
        changeLoding(true);
        // 서버 로그아웃 하기
        const {data} = await axios.post(
            "http://localhost:8080/members/logout"
        );
        return data;
    }
    // 로그아웃 하기
    const handleLogoutClick = async () => {
        try {
            // 서버에 로그아웃 요청
            const result = await logout();
            // loding false로 바꾸기
            changeLoding(false);
            // 로그아웃 성공 메시지 출력
            console.log(result.data);
            // session 아이디 제거하기 - 서버 연결 전 임시로
            sessionStorage.clear();
            // 홈으로
            navigation("/");
            // sessionForm 값 초기화하기
            setMemberInfo(null);
            
        } catch(error) {
            console.log(error);
            const errMsg = "로그아웃 처리중 에러 발생";
            alert(errMsg);
        }
    }

    /// 처음 시작
    // // 왜 로그인 유지되는지는 모르겠다
    // // 로그인이 되면서 app.js에서부터 다시 랜더링 되기 때문이다.
    // // 로그인시에는 body만 변한다/렌더링되기에 아무일이 안 일어난다.
    useEffect(() => {
        const getMemberInfo = () => {
            const key = "LOGIN_MEMBER";
            const memberInfo = JSON.parse(sessionStorage.getItem(key));
            // setMemberInfo에 담기
            setMemberInfo(memberInfo);
        }
        getMemberInfo();
    }, [sessionForm]);

    /// view 모음

    // 테그 담는 box
    let resultBox = null;
    // 로그인 상태에 따라 테그 바꾸기
    // 비로그인
    if(!memberInfo) {
        resultBox = (
            <Nav className="justify-content-end" activeKey="/home">
                <Nav.Item>
                    <Nav.Link onClick={handleLoginClick}>로그인</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link onClick={handleJoinClick}>회원가입</Nav.Link>
                </Nav.Item>
            </Nav>
        );  
    // 로그인
    } else {
        resultBox = (
            <Nav className="justify-content-end" activeKey="/home">
                <Nav.Item>
                    <Nav.Link>{memberInfo.id}님 반갑습니다</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link onClick={handleMydetailClick}>내정보</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link onClick={handleOrderClick}>장바구니</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link onClick={handleLogoutClick}>로그아웃</Nav.Link>
                </Nav.Item>
            </Nav>
        );
    }

    // Q. 계속 렌더링되는 게 맞나?
    // Q. 렌더링 되는 것이 아니라 여기로 오는 건가?
    // console.log("로그인 상태")
    
    // 작업 진행 상태 일 때
    if(loding) return (<Loding />);

    return (
        <>
            {resultBox}
        </>  
    );
}

export default HeaderTop;