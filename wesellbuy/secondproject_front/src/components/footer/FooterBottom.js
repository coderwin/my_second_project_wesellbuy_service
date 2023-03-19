import React from 'react'
import { Col, Container, Image, ListGroup, ListGroupItem, Row } from 'react-bootstrap';
import githubImage from '../../images/footer/github.png';
import notionImage from '../../images/footer/notion.png';
import '../../css/form.css';

const FooterBottom = () => {
  return (
    <Container fluid="md" className="footer body_text_center">
        <Row>
            <Col className="align-self-center">
                <ListGroup>
                    <ListGroupItem className="emailBox border border-white">
                        📧 gommind@naver.com
                    </ListGroupItem>
                </ListGroup>
            </Col>
            <Col>
                <a href="https://github.com/coderwin">
                    <Image
                        src={githubImage}
                        alt="github_address"
                    />
                </a>
            </Col>
            <Col>
                <a href="https://puzzled-detail-b29.notion.site/48d0b511fa46449d80c3d8fcbcd254a6">
                    <Image
                        src={notionImage}
                        alt="notion_address"
                    />
                </a>
            </Col>     
        </Row>       
    </Container>
  )
}

export default FooterBottom