package com.rampal.farm2home.utilities;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

public class CookieUtil {

    private CookieUtil() {
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        addCookie(null, response, name, value, maxAge, true);
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response,
                                 String name, String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(request != null && request.isSecure());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> name.equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie == null ? null : cookie.getValue();
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        deleteCookie(null, response, name, true);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String name, boolean httpOnly) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(request != null && request.isSecure());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

