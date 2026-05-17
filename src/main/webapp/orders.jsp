<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | Orders | Rampal Farm</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="<c:url value='/css/main.css'><c:param name="v" value="7"/></c:url>">
    <link rel="stylesheet" href="<c:url value='/css/about.css'><c:param name="v" value="2"/></c:url>">
</head>
<body class="page-orders">
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="page-shell" id="main-content" role="main">
    <header class="page-headline">
        <p class="eyebrow">Rampal Farm</p>
        <h1>${pageTitle}</h1>
    </header>
    <c:if test="${not empty flashSuccess}">
        <div class="message-banner success">${flashSuccess}</div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="message-banner error">${flashError}</div>
    </c:if>

    <section class="section-block">
        <div class="section-heading">
            <p class="eyebrow">Order Features</p>
            <h3>${pageTitle}</h3>
            <p>${pageSubtitle}</p>
        </div>

        <c:choose>
            <c:when test="${empty orders}">
                <p>No orders are available yet.</p>
            </c:when>
            <c:otherwise>
                <div class="order-list">
                    <c:forEach items="${orders}" var="order">
                        <article class="order-card">
                            <div class="order-header">
                                <div>
                                    <h4>Order #${order.orderId}</h4>
                                    <p>
                                        <c:if test="${sessionScope.loggedInUser.role eq 'admin'}">Customer: ${order.username}<br></c:if>
                                        Date: <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm"/>
                                    </p>
                                </div>
                                <div class="order-summary">
                                    <span class="status-pill status-${order.status}">${order.status}</span>
                                    <strong>NPR <fmt:formatNumber value="${order.totalAmount}" type="number" minFractionDigits="2" maxFractionDigits="2"/></strong>
                                </div>
                            </div>
                            <p><strong>Delivery address:</strong> ${order.deliveryAddress}</p>
                            <c:if test="${sessionScope.loggedInUser.role eq 'admin' && order.status eq 'pending'}">
                                <form action="${pageContext.request.contextPath}/orders" method="post" class="order-status-form">
                                    <input type="hidden" name="orderId" value="${order.orderId}">
                                    <span class="helper-text">Update this pending order:</span>
                                    <div class="button-row">
                                        <button type="submit" name="status" value="confirmed" class="primary-btn">Confirm Order</button>
                                        <button type="submit" name="status" value="cancelled" class="danger-btn">Cancel Order</button>
                                    </div>
                                </form>
                            </c:if>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                    <tr>
                                        <th>Photo</th>
                                        <th>Product</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Subtotal</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${order.orderDetails}" var="detail">
                                        <tr>
                                            <td>
                                                <div class="cart-thumb">
                                                    <jsp:include page="/WEB-INF/jsp/product-thumb.jsp">
                                                        <jsp:param name="name" value="${detail.productName}"/>
                                                        <jsp:param name="imageUrl" value="${detail.displayImageUrl}"/>
                                                        <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
                                                        <jsp:param name="variant" value="${detail.productId}"/>
                                                        <jsp:param name="mode" value="thumb"/>
                                                    </jsp:include>
                                                </div>
                                            </td>
                                            <td>${detail.productName}</td>
                                            <td>NPR <fmt:formatNumber value="${detail.priceAtPurchase}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                            <td>${detail.quantity}</td>
                                            <td>NPR <fmt:formatNumber value="${detail.subtotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
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
