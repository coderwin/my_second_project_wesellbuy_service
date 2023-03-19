import React from 'react';
import { Carousel } from 'react-bootstrap';
import SaleImage1 from '../../../images/home/sale.jpg';
import SaleImage2 from '../../../images/home/sale2.jpg';
import SaleImage3 from '../../../images/home/sale3.jpg';
import '../../../css/bootstrapstyle.css';

const ItemHomeHeaderImageBox = () => {

  /// 변수 모음
  const srcs = [SaleImage3, SaleImage1, SaleImage2];
  const alts = ["sale3", "sale", "sale2"];
  const titles = ["", "", "wesellbuy"];

  /// view 모음

  return (
    <Carousel fade >
      {
        srcs.map((src, i) => {
          return (
            <Carousel.Item key={i}>
              <img
                className="d-block w-100"
                src={src}
                alt={alts[i]}
              />
              <Carousel.Caption>
                <h3>{titles[i]}</h3>
                <p></p>
              </Carousel.Caption>
            </Carousel.Item>
          );
        })
      }
    </Carousel>
  )
}

export default ItemHomeHeaderImageBox;