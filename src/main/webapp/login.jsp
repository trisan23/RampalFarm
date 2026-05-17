<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%
    String rememberedUser = request.getAttribute("savedEmail") != null ? request.getAttribute("savedEmail").toString() : "";
    boolean rememberChecked = false;
    Cookie[] loginCookies = request.getCookies();
    if (loginCookies != null) {
        for (Cookie cookie : loginCookies) {
            if ("rememberUser".equals(cookie.getName())) {
                rememberedUser = cookie.getValue();
                rememberChecked = rememberedUser != null && !rememberedUser.isBlank();
                break;
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | Login | Rampal Farm</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="<c:url value='/css/main.css'><c:param name="v" value="4"/></c:url>">
    <link rel="stylesheet" href="<c:url value='/css/login_register.css'/>">
</head>
<body class="auth-page page-login">
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<main class="auth-shell" id="main-content" role="main">
    <section class="auth-copy">
        <p class="eyebrow">Rampal Farm</p>
        <h1>Login to your account</h1>
        <p class="auth-copy-text">Use your account to access the Farm2Home system and continue working with the project features.</p>
        <a class="ghost-link" href="${pageContext.request.contextPath}/home">Back to home</a>
    </section>

    <section class="auth-card">
        <h2>Welcome back</h2>
        <p class="auth-subtitle">Use your email and password to continue.</p>

        <c:if test="${not empty flashSuccess}">
            <div class="message success">${flashSuccess}</div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="message error">${flashError}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form">
            <input type="hidden" name="redirect" value="${redirect}">
            <label for="email">Email</label>
            <input id="email" type="email" name="email" value="<%= rememberedUser %>" placeholder="farmer@farm2home.com" required>

            <label for="password">Password</label>
            <input id="password" type="password" name="password" placeholder="Enter your password" required>

            <label class="checkbox-row">
                <input type="checkbox" name="rememberMe" value="true" <%= rememberChecked ? "checked" : "" %>>
                <span>Remember Me</span>
            </label>

            <button type="submit">Log In</button>
        </form>

        <p class="auth-switch">New here? <a href="${pageContext.request.contextPath}/register?redirect=${redirect}">Create an account</a></p>
    </section>
</main>
<%@ include file="/WEB-INF/jsp/site-footer.jsp" %>
</body>
</html>
