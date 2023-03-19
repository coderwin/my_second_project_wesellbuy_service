import React from 'react'
import { Button, Col, Container, ListGroup, ListGroupItem, Row } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom';

const SearchButtons = () => {

    // navigation 생성
    const navigation = useNavigate();

    /// 메서드 모음
    // 아이디 찾기
    const handleSearchIdClick = () => {
        navigation("/help/search/id");
    }
    // 비밀번호 찾기
    const handleSearchPwdClick = () => {
        navigation("/help/search/pwd");
    }
    
    return (
        <Container className="body_text_center">
            <ListGroup>
                <ListGroupItem>
                <Row className="d-flex justify-content-center">
                    <Col sm={2}>
                        <Button onClick={handleSearchIdClick} variant="outline-primary" >아이디찾기</Button>
                    </Col>
                    <Col sm={2}>
                        <Button onClick={handleSearchPwdClick}variant="outline-primary">비밀번호찾기</Button>
                    </Col>
                </Row>
                </ListGroupItem>
            </ListGroup>
        </Container>
    );
}

export default SearchButtons