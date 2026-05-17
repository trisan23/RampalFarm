<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<c:set var="currentUser" value="${not empty sessionScope.user ? sessionScope.user : sessionScope.loggedInUser}"/>
<c:set var="currentRole" value="${not empty currentUser ? currentUser.role : ''}"/>
<c:set var="navCartCount" value="${empty cartCount ? 0 : cartCount}"/>

<header class="site-topbar ${currentRole eq 'admin' ? 'site-topbar--admin' : ''}">
    <div class="topbar-inner">
        <a class="brand-lockup" href="${currentRole eq 'admin' ? cp.concat('/admin/dashboard') : cp.concat('/home')}">
            <img class="brand-logo" src="${cp}/images/farm2home-logo.png" width="40" height="40" alt="Farm2Home logo">
            <span class="brand-text">
                <span class="brand-name">Farm2Home</span>
                <span class="brand-tagline">${currentRole eq 'admin' ? 'Admin Panel' : 'Rampal Farm'}</span>
            </span>
        </a>

        <input type="checkbox" id="main-nav-toggle" class="nav-toggle">
        <label class="nav-burger" for="main-nav-toggle" aria-label="Open menu">
            <span class="nav-burger-lines"></span>
        </label>

        <nav class="primary-nav" aria-label="Main Navigation">
            <c:choose>
                <c:when test="${currentRole eq 'admin'}">
                    <a class="nav-link" href="${cp}/admin/dashboard">Dashboard</a>
                    <a class="nav-link" href="${cp}/admin/products">Products</a>
                    <a class="nav-link" href="${cp}/orders">Orders</a>
                    <a class="nav-link" href="${cp}/admin/users">Users</a>
                    <a class="nav-link nav-link--emphasis" href="${cp}/logout">Logout</a>
                </c:when>
                <c:when test="${not empty currentUser}">
                    <a class="nav-link" href="${cp}/home">Home</a>
                    <a class="nav-link" href="${cp}/about.jsp">About Us</a>
                    <a class="nav-link" href="${cp}/home#products">Products</a>
                    <a class="nav-link" href="${cp}/cart">Cart <span class="nav-pill">${navCartCount}</span></a>
                    <a class="nav-link" href="${cp}/orders">Orders</a>
                    <a class="nav-link nav-link--emphasis" href="${cp}/logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a class="nav-link" href="${cp}/home">Home</a>
                    <a class="nav-link" href="${cp}/about.jsp">About Us</a>
                    <a class="nav-link" href="${cp}/home#products">Products</a>
                    <a class="nav-link nav-link--emphasis" href="${cp}/login">Login</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
