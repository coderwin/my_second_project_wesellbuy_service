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
 * description : entity에서 생성한 회원 id/수정한 회원 id column(레코드) 생성해준다.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseMakerColumnEntity {

    @CreatedBy
    private String createdBy; // 만든 회원 id
    @LastModifiedBy
    private String lastModifiedBy; // 마지막으로 수정한 회원원 id
}
