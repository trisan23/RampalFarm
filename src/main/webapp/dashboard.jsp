<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | User Dashboard</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="<c:url value='/css/main.css'><c:param name="v" value="7"/></c:url>">
    <link rel="stylesheet" href="<c:url value='/css/about.css'><c:param name="v" value="2"/></c:url>">
</head>
<body class="page-dashboard">
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="page-shell" id="main-content" role="main">
    <header class="page-headline">
        <p class="eyebrow">My Account</p>
        <h1>Welcome, ${sessionScope.loggedInUser.firstName}!</h1>
        <p class="page-lead">Manage your orders and learn more about our farm.</p>
    </header>

    <section class="hero-panel hero-panel--simple">
        <div class="hero-copy">
            <h2>Your Dashboard</h2>
            <p>From here you can access your order history, update your details, or browse our latest agricultural products. Thank you for supporting Farm2Home!</p>
            <div class="hero-actions">
                <a class="primary-btn" href="${pageContext.request.contextPath}/orders">View My Orders</a>
                <a class="secondary-btn" href="${pageContext.request.contextPath}/home">Browse Products</a>
            </div>
        </div>
    </section>

    <!-- Embedded About Us Section -->
    <div style="margin-top: 48px; padding-top: 32px; border-top: 1px solid var(--border);">
        <div class="section-heading" style="margin-bottom: 24px;">
            <p class="eyebrow">Farm2Home Story</p>
            <h3>About Us</h3>
        </div>
    </div>
    
    <jsp:include page="/WEB-INF/jsp/about-content.jsp"/>
</main>
<%@ include file="/WEB-INF/jsp/site-footer.jsp" %>
</body>
</html>
