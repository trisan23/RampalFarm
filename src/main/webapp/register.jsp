<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | Register | Rampal Farm</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="<c:url value='/css/main.css'><c:param name="v" value="4"/></c:url>">
    <link rel="stylesheet" href="<c:url value='/css/login_register.css'/>">
</head>
<body class="auth-page page-register">
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<main class="auth-shell" id="main-content" role="main">
    <section class="auth-copy">
        <p class="eyebrow">Rampal Farm</p>
        <h1>Create your account</h1>
        <p class="auth-copy-text">Register as a customer to use the Farm2Home system. The form includes validation and secure password handling.</p>
        <a class="ghost-link" href="${pageContext.request.contextPath}/home">Preview the website</a>
    </section>

    <section class="auth-card">
        <h2>Register</h2>
        <p class="auth-subtitle">Customer registration for the Farm2Home marketplace.</p>

        <c:if test="${not empty flashSuccess}">
            <div class="message success">${flashSuccess}</div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="message error">${flashError}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form">
            <input type="hidden" name="redirect" value="${redirect}">
            <label for="username">Full name</label>
            <input id="username" type="text" name="username" value="${username}" placeholder="Ram Sharma" required>

            <label for="email">Email</label>
            <input id="email" type="email" name="email" value="${email}" placeholder="ram@gmail.com" required>

            <label for="phoneNumber">Phone number</label>
            <input id="phoneNumber" type="text" name="phoneNumber" value="${phoneNumber}" placeholder="98XXXXXXXX" required>

            <label for="password">Password</label>
            <input id="password" type="password" name="password" placeholder="At least 8 chars, 1 uppercase, 1 number, 1 symbol" required>

            <label for="confirmPassword">Confirm password</label>
            <input id="confirmPassword" type="password" name="confirmPassword" placeholder="Retype your password" required>

            <button type="submit">Create Account</button>
        </form>

        <p class="auth-switch">Already registered? <a href="${pageContext.request.contextPath}/login?redirect=${redirect}">Log in here</a></p>
    </section>
</main>
<%@ include file="/WEB-INF/jsp/site-footer.jsp" %>
</body>
</html>
