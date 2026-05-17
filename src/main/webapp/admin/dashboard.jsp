<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | Admin Dashboard</title>
    <link rel="stylesheet" href="<c:url value='/css/main.css'/>">
</head>
<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="page-shell">
    <section class="hero-panel hero-panel--simple admin-hero-panel">
        <div class="admin-hero-copy">
            <p class="eyebrow">Farm2Home Admin</p>
            <img class="admin-dashboard-logo" src="${pageContext.request.contextPath}/images/farm2home-logo.png" width="72" height="72" alt="Farm2Home logo">
            <h1>Dashboard</h1>
            <p class="page-lead">Manage inventory, categories, customers, and order activity from one place.</p>
            <div class="hero-actions">
                <a class="primary-btn" href="${pageContext.request.contextPath}/admin/products">Manage Products</a>
                <a class="secondary-btn" href="${pageContext.request.contextPath}/admin/users">Manage Users</a>
            </div>
        </div>
    </section>
    <c:if test="${not empty flashSuccess}">
        <div class="message-banner success">${flashSuccess}</div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="message-banner error">${flashError}</div>
    </c:if>

    <section class="stats-grid">
        <article class="stat-card">
            <span>Total Users</span>
            <strong>${userCount}</strong>
        </article>
        <article class="stat-card">
            <span>Total Categories</span>
            <strong>${categoryCount}</strong>
        </article>
        <article class="stat-card">
            <span>Total Products</span>
            <strong>${productCount}</strong>
        </article>
        <article class="stat-card">
            <span>Total Orders</span>
            <strong>${orderCount}</strong>
        </article>
        <article class="stat-card">
            <span>Pending Orders</span>
            <strong>${pendingOrderCount}</strong>
        </article>
    </section>

    <section class="section-block">
        <div class="section-heading">
            <p class="eyebrow">Recent Products</p>
            <h3>Recent product list</h3>
        </div>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Stock</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${recentProducts}" var="product">
                    <tr>
                        <td>${product.productName}</td>
                        <td>${product.categoryName}</td>
                        <td>${product.stockQuantity}</td>
                        <td>NPR <fmt:formatNumber value="${product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <section class="section-block">
        <div class="section-heading">
            <p class="eyebrow">Recent Orders</p>
            <h3>Recent order activity</h3>
        </div>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Order</th>
                    <th>Customer</th>
                    <th>Status</th>
                    <th>Total</th>
                    <th>Delivery Address</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${recentOrders}" var="order">
                    <tr>
                        <td>#${order.orderId}</td>
                        <td>${order.username}</td>
                        <td><span class="status-pill status-${order.status}">${order.status}</span></td>
                        <td>NPR <fmt:formatNumber value="${order.totalAmount}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td>${order.deliveryAddress}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
</main>
<%@ include file="/WEB-INF/jsp/site-footer.jsp" %>
</body>
</html>
