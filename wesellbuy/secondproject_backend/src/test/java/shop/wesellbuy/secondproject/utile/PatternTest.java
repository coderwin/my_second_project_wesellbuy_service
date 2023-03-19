package shop.wesellbuy.secondproject.utile;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

// 사용중인 pattern(정규표현식)을 검증한다.
public class PatternTest {

    @DisplayName("상품 저장 데이터 검증 패턴 테스트")
    @Test
    public void 상품_저장_데이터_검증_패턴_테스트() {
        // given
        String pattern1 = "^\\d+$"; // 숫자만 가능
        String pattern2 = "^B|HA|F|ITEM$"; // 이 단어만 가능
        // when
        String value1 = "123asdf";
        String value2 = "asdf!@2";
        String value3 = "01235";

        boolean result1 = Pattern.matches(pattern1, value1);
        boolean result2 = Pattern.matches(pattern1, value2);
        boolean result3 = Pattern.matches(pattern1, value3);

        String value21 = "asdf";
        String value22 = "HA";
        String value23 = "ITEM";
        String value24 = "ITEM2";
        String value25 = "B";
        String value26 = "BD";
        String value27 = "H";

        boolean result21 = Pattern.matches(pattern2, value21);
        boolean result22 = Pattern.matches(pattern2, value22);
        boolean result23 = Pattern.matches(pattern2, value23);
        boolean result24 = Pattern.matches(pattern2, value24);
        boolean result25 = Pattern.matches(pattern2, value25);
        boolean result26 = Pattern.matches(pattern2, value26);
        boolean result27 = Pattern.matches(pattern2, value27);

        // then
        assertThat(result1).isFalse();
        assertThat(result2).isFalse();
        assertThat(result3).isTrue();

        assertThat(result21).isFalse();
        assertThat(result22).isTrue();
        assertThat(result23).isTrue();
        assertThat(result24).isFalse();
        assertThat(result25).isTrue();
        assertThat(result26).isFalse();
        assertThat(result27).isFalse();
    }

    @DisplayName("배달원 체크 인터셉터에서 사용자 아이디 검증 테스트")
    @Test
    public void 배달원체크_사용자_아이디_검증_테스트() {
        // given
        String pattern1 = "^deliver\\w*$";// 사용 패턴2

        // 여러가지 아이디
        String id1 = "he12deliver";
        String id2 = "deliver123";
        String id3 = "Deliver1234";
        String id4 = "deliver";

        // when
        boolean result1 = id1.matches(pattern1);
        boolean result2 = id2.matches(pattern1);
        boolean result3 = id3.matches(pattern1);
        boolean result4 = id4.matches(pattern1);

        // then
        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
        assertThat(result4).isTrue();
    }

    @DisplayName("관리자 체크 인터셉터에서 사용자 아이디 검증 테스트")
    @Test
    public void 관리자체크_사용자_아이디_검증_테스트() {
        // given
        String pattern1 = "^admin\\w*$";// 사용 패턴2

        // 여러가지 아이디
        String id1 = "he12admin12 **&";
        String id2 = "admin123";
        String id3 = "Admin1234";
        String id4 = "admsdin";
        String id5 = "admin";

        // when
        boolean result1 = id1.matches(pattern1);
        boolean result2 = id2.matches(pattern1);
        boolean result3 = id3.matches(pattern1);
        boolean result4 = id4.matches(pattern1);
        boolean result5 = id5.matches(pattern1);

        // then
        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
        assertThat(result4).isFalse();
        assertThat(result5).isTrue();
    }
}
