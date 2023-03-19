package shop.wesellbuy.secondproject.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JPA 사용 위한 bean 생성
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : JPA 사용 위한 bean 생성위한 config
 */
@Configuration
public class JPAConfig {

    @PersistenceContext
    private EntityManager em;

    /**
     * JPA 사용 위한 bean 생성
     * writer : 이호진
     * init : 2023.01.16
     * updated by writer :
     * update :
     * description : queryDSL 사용할 수 있는 bean 생성
     */
    @Bean
    public JPAQueryFactory createJPAQueryFactory() {
        return new JPAQueryFactory(em);
    }








}
