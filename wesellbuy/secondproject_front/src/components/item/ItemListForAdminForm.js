import axios from 'axios';
import React, { createContext, useContext, useEffect, useState } from 'react'
import { Col, Container, Row } from 'react-bootstrap';
import { CustomContext } from '../../App';
import PageButtonForm from '../common/pagebutton/PageButtonForm';
import Loding from '../Loding';
import ItemListBoxForAdminForm from './listForAdmin/ItemListBoxForAdminForm';
import ItemSearchNavForAdminForm from './listForAdmin/ItemSearchNavForAdminForm';

/**
 * Item list for admin component
 * writer : 이호진
 * init : 2023.03.28
 * updated by writer :
 * update :
 * description : 상품 목록 관리자용 component
 */
export const ItemListForAdminContext = createContext(null); // ItemListForAdminContext Context

const ItemListForAdminForm = () => {

  /// 변수 모음
  // 검색 데이터 default 변수
  const defaultData = {
    name: "",// 상품명
    memberId: "",// 판매자 아이디
    dtype: "",// 종류
    createData: "",// 상품 생성 날짜(shape : 0000-00-00) 
    size: 20,// 페이지 size
    page: 0// 페이지 번호
  }
  const {serverHost} = useContext(CustomContext);
  
  /// 상태 모음
  const [loding, setLoding] = useState(false);// 요청처리 상태
  const [data, setData] = useState(defaultData);// 검색 데이터 상태
  const [listDatas, setListDatas] = useState(null);// 데이터 상태(목록을 위한)
  const [totalPages, setTotalPages] = useState(0);// 상품 list의 전체페이지

  /// 메서드 모음
  
  // datas에 주문 목록에 담기
  async function inputListDatas() {
    // loding = true
    setLoding(true);
    try {
      // 서버에서 주문 목록 불러오기
      const {data} = await getItemList();
      // loding false
      setLoding(false);
      // 요청 성공
      // console.log("요청 성공");
      // Listdatas에 담기
      setListDatas(data.data.content);
      setTotalPages(data.data.totalPages);
    } catch(err) {
      // loding false
      setLoding(false);
      // 요청 실패
      // console.log("요청 실패");
      // console.log(err);
    }
  }
  // datas에 주문 목록에 담기 for search
  async function inputListDatasForSearch() {
    try {
      // 서버에서 주문 목록 불러오기
      const {data} = await getItemList();
      // loding false
      setLoding(false);
      // 요청 성공
      // console.log("요청 성공");
      // Listdatas에 담기
      setListDatas(data.data.content);
      setTotalPages(data.data.totalPages);
    } catch(err) {
      // loding false
      setLoding(false);
      // 요청 실패
      // console.log("요청 실패");
      // console.log(err);
    }
  }
  // 서버에서 모든 추천합니다글 불러오기
  async function getItemList() {
    return await axios.get(
      `${serverHost}:8080/items/admin`,
      {
        params: data,
        withCredentials: true
      }
    );
  }
  // 검색 데이터 바뀌면 data 변경한다
  function handleDataChange(e) {
    setData({
      ...data,
      [e.target.name]: e.target.value
    });
  }
  // page 데이터 바뀌면 data 변경한다
  function handlePageInDataChange(e) {
    // console.log(`${e.target.name} : ${e.target.id}`);
    setData((data) => {
      return {
      ...data,
      [e.target.name]: e.target.id
      }
    });
  }
  
  // 찾기(Search) 버튼 클릭 했을 때
    // listDatas에 담아주기
  async function handleSearchClick() {
    // 주문목록을 listDatas에 담기
    await inputListDatas();
  }

  /// 처음 시작
  useEffect(() => {
    // 주문 목록에 담기
    inputListDatas();
  }, []);
  // 검색할 때
  useEffect(() => {
    inputListDatasForSearch();
  }, [data]);

  /// view 모음

  // 요청 처리 view
  if(loding) return(<Loding />);

  return (
    <ItemListForAdminContext.Provider value={{data, handleDataChange, handleSearchClick, listDatas, totalPages}}>
      <Container className="body_text_center">
        <Row className="d-flex justify-content-center">
          <Col sm={10}>
            {/* 상품 찾기 Nav */}
            <Row>
              <Col md="12">
                {/* 위쪽 Nav - 검색 */}
                <ItemSearchNavForAdminForm />
              </Col>
            </Row>
            {/* 상품 목록 box */}
            <Row id="top">
              <Col md="12">
                {/* body - 상품 목록  */}
                <ItemListBoxForAdminForm />
              </Col>
            </Row>
            {/* footer - 페이지 버튼 */}
            <Row className="d-flex justify-content-center">
              <Col sm={3}>
                <PageButtonForm data={data} handleDataChange={handlePageInDataChange} totalPages={totalPages} />
              </Col>
            </Row>
            {/* 맨위로 이동하기 */}
            <Row className="footerFixed mousePointer body_text_right">
              <Col>
                <a href="#top">맨위로</a>
              </Col>
            </Row>
          </Col>
        </Row>
      </Container>
    </ItemListForAdminContext.Provider> 
  )
}

export default ItemListForAdminForm;