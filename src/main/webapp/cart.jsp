<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | Cart | Rampal Farm</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="<c:url value='/css/main.css'><c:param name="v" value="4"/></c:url>">
</head>
<body class="page-cart">
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="page-shell" id="main-content" role="main">
    <header class="page-headline">
        <p class="eyebrow">Rampal Farm</p>
        <h1>Shopping cart</h1>
        <p class="page-lead">Your basket</p>
    </header>
    <c:if test="${not empty flashSuccess}">
        <div class="message-banner success">${flashSuccess}</div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="message-banner error">${flashError}</div>
    </c:if>

    <section class="section-block">
        <div class="section-heading">
            <p class="eyebrow">Cart Summary</p>
            <h3>Your selected items</h3>
        </div>

        <c:choose>
            <c:when test="${empty cartItems}">
                <p>Your cart is empty. Go back to the home page and add some products.</p>
            </c:when>
            <c:otherwise>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>Product</th>
                            <th>Photo</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Subtotal</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${cartItems}" var="item">
                            <tr>
                                <td>${item.productName}</td>
                                <td>
                                    <div class="cart-thumb">
                                        <jsp:include page="/WEB-INF/jsp/product-thumb.jsp">
                                            <jsp:param name="name" value="${item.productName}"/>
                                            <jsp:param name="imageUrl" value="${item.displayImageUrl}"/>
                                            <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
                                            <jsp:param name="variant" value="${item.productId}"/>
                                            <jsp:param name="mode" value="thumb"/>
                                        </jsp:include>
                                    </div>
                                </td>
                                <td>NPR <fmt:formatNumber value="${item.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/update-cart" method="post" class="inline-form">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <input type="number" name="quantity" min="1" value="${item.quantity}" class="qty-input">
                                        <button type="submit" class="secondary-btn">Update</button>
                                    </form>
                                </td>
                                <td>NPR <fmt:formatNumber value="${item.subtotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/remove-from-cart" method="post">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <button type="submit" class="danger-btn">Remove</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="cart-total-box">
                    <h4>Total: NPR <fmt:formatNumber value="${cartTotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></h4>
                </div>
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedInUser}">
                        <div class="checkout-box">
                            <h4>Place Order</h4>
                            <p>Add your delivery address and submit the cart as an order.</p>
                            <form action="${pageContext.request.contextPath}/checkout" method="post" class="dashboard-form">
                                <label for="deliveryAddress">Delivery address</label>
                                <textarea id="deliveryAddress" name="deliveryAddress" rows="4" placeholder="Enter full delivery address" required></textarea>
                                <button type="submit" class="primary-btn">Place Order</button>
                            </form>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="checkout-box">
                            <h4>Ready to place your order?</h4>
                            <p>Login with your account or register first to continue to ordering.</p>
                            <div class="button-row">
                                <a class="primary-btn" href="${pageContext.request.contextPath}/login?redirect=/cart">Login</a>
                                <a class="secondary-btn" href="${pageContext.request.contextPath}/register?redirect=/cart">Register</a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </section>
</main>
<%@ include file="/WEB-INF/jsp/site-footer.jsp" %>
</body>
</html>
