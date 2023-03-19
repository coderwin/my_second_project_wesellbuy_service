package shop.wesellbuy.secondproject.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.web.resultBox.Result;

@RestController
@Slf4j
public class testController {

    Member testMember = new Member("hello");

    @GetMapping("/hello")
    public Result hello() {
        log.info("{}", "hello");
        log.error("{}", "hello");
//        log.trace("{\"\"}", "hello");

//        String greeting = "hello";

        return new Result(testMember);
    }

    @GetMapping("/hello2")
    public String Hello2() {
        log.info("{}", "hello");
        log.error("{}", "hello");
        log.trace("{}", "hello"); // 안나옴
        String greeting = "hello";

        return greeting;
    }

}
