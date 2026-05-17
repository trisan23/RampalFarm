<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | Rampal Farm</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="<c:url value='/css/main.css'><c:param name="v" value="7"/></c:url>">
</head>
<body class="page-home">
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="page-shell" id="main-content" role="main">
    <section class="hero-panel">
        <div class="hero-copy">
            <p class="eyebrow">Fresh from Rampal Farm</p>
            <h2>Fresh vegetables, fruits, dairy, grains, and spices delivered from farm to home.</h2>
            <p>Browse categories, add products to your cart, and place orders with delivery details. Guests can explore the catalog, but cart and ordering now require login or quick registration.</p>
            <div class="hero-actions">
                <a class="primary-btn" href="${pageContext.request.contextPath}/home#products">Browse Products</a>
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedInUser}">
                        <a class="secondary-btn" href="${pageContext.request.contextPath}/orders">View Orders</a>
                    </c:when>
                    <c:otherwise>
                        <a class="secondary-btn" href="${pageContext.request.contextPath}/login">Sign In to Order</a>
                    </c:otherwise>
                </c:choose>
            </div>
            <c:if test="${not empty lastLoginEmail}">
                <p class="helper-text">Last remembered email: <strong>${lastLoginEmail}</strong></p>
            </c:if>
        </div>
        <div class="hero-metrics">
            <article class="metric-card">
                <span class="metric-label">Categories</span>
                <strong class="metric-value">${categoryCount}</strong>
            </article>
            <article class="metric-card">
                <span class="metric-label">Products Available</span>
                <strong class="metric-value">${productCount}</strong>
            </article>
            <article class="metric-card">
                <span class="metric-label">Cart Items</span>
                <strong class="metric-value">${cartCount}</strong>
            </article>
        </div>
    </section>

    <c:if test="${not empty flashSuccess}">
        <div class="message-banner success">${flashSuccess}</div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="message-banner error">${flashError}</div>
    </c:if>

    <section class="section-block">
        <div class="section-heading">
            <p class="eyebrow">Browse by Category</p>
            <h3>Product categories</h3>
        </div>
        <div class="category-grid">
            <a class="category-card category-link ${empty selectedCategoryId ? 'selected' : ''}" href="${pageContext.request.contextPath}/home#products">
                <h4>All Products</h4>
                <p>Show every available item from every category.</p>
            </a>
            <c:forEach items="${categories}" var="category">
                <a class="category-card category-link ${selectedCategoryId eq category.categoryId ? 'selected' : ''}"
                   href="${pageContext.request.contextPath}/home?categoryId=${category.categoryId}#products">
                    <h4>${category.categoryName}</h4>
                    <p>${category.description}</p>
                </a>
            </c:forEach>
        </div>
    </section>

    <section class="section-block" id="products">
        <div class="section-heading">
            <p class="eyebrow">Featured Produce</p>
            <h3>
                <c:choose>
                    <c:when test="${not empty selectedCategoryName}">${selectedCategoryName} products</c:when>
                    <c:otherwise>Available products</c:otherwise>
                </c:choose>
            </h3>
        </div>
        <c:choose>
            <c:when test="${empty products}">
                <p>No products are available for this category right now.</p>
            </c:when>
            <c:otherwise>
                <div class="product-grid">
                    <c:forEach items="${products}" var="product">
                        <article class="product-card">
                            <div class="product-image">
                                <jsp:include page="/WEB-INF/jsp/product-thumb.jsp">
                                    <jsp:param name="name" value="${product.productName}"/>
                                    <jsp:param name="imageUrl" value="${product.displayImageUrl}"/>
                                    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
                                    <jsp:param name="variant" value="${product.productId}"/>
                                </jsp:include>
                            </div>
                            <div class="product-content">
                                <div class="product-topline">
                                    <span>${product.categoryName}</span>
                                    <span>Stock: ${product.stockQuantity}</span>
                                </div>
                                <h4>${product.productName}</h4>
                                <p>${product.description}</p>
                                <strong>NPR <fmt:formatNumber value="${product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/></strong>
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user or not empty sessionScope.loggedInUser}">
                                        <form action="${pageContext.request.contextPath}/add-to-cart" method="post" class="cart-form">
                                            <input type="hidden" name="productId" value="${product.productId}">
                                            <label class="qty-label" for="qty-${product.productId}">Quantity</label>
                                            <input id="qty-${product.productId}" type="number" name="quantity" min="1" max="${product.stockQuantity}" value="1" class="qty-input"
                                                   oninvalid="this.setCustomValidity(Number(this.value) > Number(this.max) ? 'Sorry we are out of stock' : '')"
                                                   oninput="this.setCustomValidity('')">
                                            <div class="button-row">
                                                <button type="submit" name="redirect" value="/home" class="primary-btn">Add to Cart</button>
                                                <button type="submit" name="redirect" value="/cart" class="secondary-btn">Place Order</button>
                                            </div>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="button-row">
                                            <a class="primary-btn" href="${pageContext.request.contextPath}/login?redirect=/home">Add to Cart</a>
                                            <a class="secondary-btn" href="${pageContext.request.contextPath}/login?redirect=/cart">Place Order</a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </article>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>
<%@ include file="/WEB-INF/jsp/site-footer.jsp" %>
</body>
</html>
