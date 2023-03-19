package shop.wesellbuy.secondproject.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

/**
 * Cookie 사용 필요 도구 모음
 * writer : 이호진
 * init : 2023.01.27
 * updated by writer :
 * update :
 * description : 쿠키의 생성, 삭제 등을 다룬다
 *               + 쿠키에 사용된는 이름을 모아둔다
 */
public class CookieManager {

    public static final String REMEMBER_ID = "REMEMBER_ID";// 로그인 시, 아이디 기억에 사용되는 쿠키 이름

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 쿠키 생성
     *
     * comment : maxAge = 86400 (24시간)
     */
    public static void makeCookie(String cookieName, String value, HttpServletResponse response, int maxAge) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(maxAge);
        // 쿠키 생성
        response.addCookie(cookie);
    }

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 쿠키 찾기
     */
    public static Cookie findCookie(String cookieName, HttpServletRequest request) {
        // 쿠키가 없을 때
        if(request.getCookies() == null) {
            return null;
        }

        // 쿠키가 있으면
        Cookie[] cookies = request.getCookies();

        return Arrays.stream(cookies)
                .filter((cookie) ->
                    cookie.getName().equals(cookieName))
                .findFirst()
                .orElse(null);
    }

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 쿠키 만료
     */
    public static void expireCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
