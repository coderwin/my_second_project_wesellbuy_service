import React, { useContext } from 'react'
import { Nav } from 'react-bootstrap'
import { ItemListContext } from '../ItemListForm'

/**
 * Item list type 선택 component
 * writer : 이호진
 * init : 2023.03.09
 * updated by writer :
 * update :
 * description : 상품의 종류를 선택할 수 있는 component
 */
const ItemTypeNavForm = () => {
  
  /// 변수 모음
  // 외부의 변수 불러오기
  const {handleTypeNavClick} = useContext(ItemListContext);
  /// 상태 모음

  /// 메서드 모음
  
  /// view 모음
  
  return (
    <Nav className="flex-column side_nav">
      <Nav.Link id="" onClick={handleTypeNavClick}>전체</Nav.Link>
      <Nav.Link id="B" onClick={handleTypeNavClick}>책</Nav.Link>
      <Nav.Link id="F" onClick={handleTypeNavClick}>가구</Nav.Link>
      <Nav.Link id="HA" onClick={handleTypeNavClick}>가전제품</Nav.Link>
      <Nav.Link id="ITEM" onClick={handleTypeNavClick}>기타</Nav.Link>
    </Nav>
  )
}

export default ItemTypeNavForm