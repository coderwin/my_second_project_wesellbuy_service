package shop.wesellbuy.secondproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

//@Configuration
public class BaseEntityConfig {

    // Entity에 등록자(CreatedBy)/수정자(CreatedBy) 레코드 사용하기위헤서 bean 등록한다.
//    @Bean
//    public AuditorAware<String> InputAuditorProvider() {
//        // 세션에 있는 회원정보의 아이디 이용
//        return () -> Optional.of();
//
//    }
}
