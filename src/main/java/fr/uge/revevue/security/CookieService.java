package fr.uge.revevue.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service
public class CookieService {
    private final static String PATH_COOKIE = "/";
    private final static boolean HTTP_ONLY = true;
    @Value("${cookie.expiration.time.authentication}")
    private int COOKIE_EXPIRATION_TIME;
    @Value("${cookie.expiration.time.refresh}")
    private int COOKIE_EXPIRATION_REFRESH_TIME;
    public CookieService(){}

    public void addCookie(String name, String value, int age, HttpServletResponse response){
        var cookie = new Cookie(name, value);
        cookie.setPath(PATH_COOKIE);
        cookie.setHttpOnly(HTTP_ONLY);
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }

    public Cookie findCookie(String name, HttpServletRequest request){
        var cookies = request.getCookies();
        if (cookies != null){
            for (var cookie:cookies){
                if (cookie.getName().equals(name)){
                    return cookie;
                }
            }
        }
        return null;
    }

    public void removeCookie(String name, HttpServletResponse response){
        var removeCookie = new Cookie(name, null);
        removeCookie.setPath(PATH_COOKIE);
        removeCookie.setMaxAge(0);
        response.addCookie(removeCookie);
    }

    public void addAllCookies(Map<String, String> tokens, HttpServletResponse response){
        addCookie("bearer", tokens.get("bearer"), COOKIE_EXPIRATION_TIME, response);
        addCookie("refresh", tokens.get("refresh"), COOKIE_EXPIRATION_REFRESH_TIME, response);
    }
}


