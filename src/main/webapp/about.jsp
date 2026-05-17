<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | About Us</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css?v=7">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/about.css?v=2">
</head>
<body class="about-page">
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<main class="about-shell" id="main-content" role="main">
    <jsp:include page="/WEB-INF/jsp/about-content.jsp"/>
</main>
<%@ include file="/WEB-INF/jsp/site-footer.jsp" %>
</body>
</html>
