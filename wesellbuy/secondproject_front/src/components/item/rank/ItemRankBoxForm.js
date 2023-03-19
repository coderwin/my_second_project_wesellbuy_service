import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react'
import { Table } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { ItemRankContext } from '../ItemRankForm';
import "../../../css/form.css";

/**
 * Item rank list box component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : 상품순위 목록 box component
 */
const ItemRankBoxForm = () => {
  
  /// 변수 모음
  // 외부의 변수 불러오기
  const navigation = useNavigate();// navigation

  /// 상태 모음
  const [datas, setDatas] = useState(null);// 상품순위 목록 데이터 상태 
  /// 메서드 모음
  
  // datas에 상품순위 목록에 담기
  async function inputDatas() {
    try {
      // 서버에서 상품 목록 불러오기
      const response = await getItemRankList();
      // 요청 성공
      console.log("요청 성공");
      // 요청 끝
      // datas에 담기
      setDatas(response.data.data);
    } catch(err) {
      // 요청 실패
      console.log("요청 실패");
      console.log(err);
    }
  }
  // 서버에서 상품 랭크(순위) 불러오기
  async function getItemRankList() {
    return await axios.get(
      "http://localhost:8080/items/rank/v1"
    );
  }
  // 상품명을 클릭했을 때
  function handleItemNameClick(e) {
    // id 불러오기
    const id = e.target.id;// 상품번호 불러오기
    // 상품 상세보기로 이동
    navigation(`/item/${id}`);
  }

  /// 처음 시작
  useEffect(() => {
    // 상품순위 목록에 담기
    inputDatas();
  }, []);

  /// view 모음
  let view = null;// 태그를 담아준다.
  // tbody에 들어갈 데이터 생성
  // 데이터가 있으면 생성한다
  if(datas) {
    // 데이터 만들기
    view = datas.map((data) => {
      return (
        <tr key={data.num}>
          <th>{data.rank}</th>
          <th id={data.num} onClick={handleItemNameClick} className="mousePointer">{data.name}</th>
          <th>{data.price}</th>
          <th>{data.likes}</th>
          <th>{data.hits}</th>
          <th>{data.memberId}</th>
        </tr>
      );
    });
  // 없으면 데이터가 존재하지 않는다고 알려주기
  } else {
    view = (
    <tr>
      <th colSpan={6}>
        상품이 없습니다.
      </th>
    </tr>);
  }

  return (
    <Table hover>
      <thead className="table-primary">
        <tr>
          <th>순위</th>
          <th>상품명</th>
          <th>가격</th>
          <th>좋아요수</th>
          <th>조회수</th>
          <th>판매자 아이디</th>
        </tr>
      </thead>
      <tbody>
        {view}
      </tbody>
    </Table>
  )
}

export default ItemRankBoxForm;