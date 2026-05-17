<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | ${product.productName}</title>
    <link rel="stylesheet" href="<c:url value='/css/main.css'><c:param name="v" value="4"/></c:url>">
</head>
<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<main class="page-shell" id="main-content" role="main">
    <section class="hero-panel product-detail-panel">
        <div class="product-detail-image">
            <jsp:include page="/WEB-INF/jsp/product-thumb.jsp">
                <jsp:param name="name" value="${product.productName}"/>
                <jsp:param name="imageUrl" value="${product.displayImageUrl}"/>
                <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
                <jsp:param name="variant" value="${product.productId}"/>
            </jsp:include>
        </div>
        <div class="product-detail-copy">
            <p class="eyebrow">${product.categoryName}</p>
            <h1>${product.productName}</h1>
            <p class="page-lead">${product.description}</p>
            <div class="product-detail-meta">
                <span>Available stock: ${product.stockQuantity}</span>
                <strong>NPR <fmt:formatNumber value="${product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/></strong>
            </div>
            <form action="${pageContext.request.contextPath}/add-to-cart" method="post" class="cart-form">
                <input type="hidden" name="productId" value="${product.productId}">
                <label class="qty-label" for="detail-qty-${product.productId}">Quantity</label>
                <input id="detail-qty-${product.productId}" type="number" name="quantity" min="1" max="${product.stockQuantity}" value="1" class="qty-input"
                       oninvalid="this.setCustomValidity(Number(this.value) > Number(this.max) ? 'Sorry we are out of stock' : '')"
                       oninput="this.setCustomValidity('')">
                <div class="button-row">
                    <button type="submit" name="redirect" value="/product?productId=${product.productId}" class="primary-btn">Add to Cart</button>
                    <a class="secondary-btn" href="${pageContext.request.contextPath}/home">Back to Products</a>
                </div>
            </form>
        </div>
    </section>
</main>
<%@ include file="/WEB-INF/jsp/site-footer.jsp" %>
</body>
</html>
