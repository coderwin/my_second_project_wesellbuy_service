package shop.wesellbuy.secondproject.domain.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * date column EventListener
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : entity에서 생성된 날짜/수정된 날짜/생성한 회원 id/수정한 회원 id 의 column(레코드) 생성해준다.
 */
//@EntityListeners(AuditingEntityListener.class)
//@MappedSuperclass
public abstract class BaseMakerColumnEntityWithDate extends BaseDateColumnEntity {

    @CreatedBy
    private String CreatedBy;
    @LastModifiedBy
    private String lastModifiedBy;
}
