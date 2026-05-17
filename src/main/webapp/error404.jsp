<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page not found | Farm2Home</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="<c:url value='/css/main.css'><c:param name="v" value="4"/></c:url>">
</head>
<body class="page-error">
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="page-shell" role="main">
    <section class="hero-panel hero-panel--simple">
        <div class="hero-copy">
            <p class="eyebrow">404</p>
            <h2>That page isn’t on the farm map.</h2>
            <p>The address may be wrong or the page was moved. Head back to the shop to keep browsing fresh produce from Rampal Farm.</p>
            <div class="hero-actions">
                <a class="primary-btn" href="<c:url value='/home'/>">Back to home</a>
                <button type="button" class="secondary-btn" onclick="history.back()">Go back</button>
            </div>
        </div>
    </section>
</main>
<%@ include file="/WEB-INF/jsp/site-footer.jsp" %>
</body>
</html>
