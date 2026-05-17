package com.rampal.farm2home.utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {
    public static final String USER_SESSION_KEY = "loggedInUser";
    public static final String FLASH_SUCCESS = "flashSuccess";
    public static final String FLASH_ERROR = "flashError";

    public static void setAttribute(HttpServletRequest request, String key, Object value) {
        HttpSession session = request.getSession();
        session.setAttribute(key, value);
    }

    public static Object getAttribute(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(key);
        }
        return null;
    }

    public static void removeAttribute(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static void setFlashMessage(HttpServletRequest request, String key, String message) {
        setAttribute(request, key, message);
    }

    public static String consumeFlashMessage(HttpServletRequest request, String key) {
        Object value = getAttribute(request, key);
        if (value != null) {
            removeAttribute(request, key);
            return value.toString();
        }
        return null;
    }
}
