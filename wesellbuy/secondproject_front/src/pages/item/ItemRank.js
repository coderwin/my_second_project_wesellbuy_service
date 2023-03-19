import React from 'react';
import {Route, Routes} from 'react-router-dom';
import ItemRankForm from '../../components/item/ItemRankForm';

/**
 * Item rank page
 * writer : 이호진
 * init : 2023.02.21
 * updated by writer :
 * update :
 * description : 상품순위 목록 page
 */
function ItemRank() {

    return (
      <>
        <ItemRankForm />
      </>
    );
}

export default ItemRank;