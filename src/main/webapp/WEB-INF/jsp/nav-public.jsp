<%
    String selectedTheme = "light";
    jakarta.servlet.http.Cookie[] themeCookies = request.getCookies();
    if (themeCookies != null) {
        for (jakarta.servlet.http.Cookie cookie : themeCookies) {
            if ("theme".equals(cookie.getName()) && "dark".equalsIgnoreCase(cookie.getValue())) {
                selectedTheme = "dark";
                break;
            }
        }
    }
    pageContext.setAttribute("themeMode", selectedTheme);
%>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<c:set var="navCartCount" value="${empty cartCount ? 0 : cartCount}"/>
<script>document.body.classList.remove('theme-light','theme-dark');document.body.classList.add('theme-${themeMode}');</script>
<header class="site-topbar theme-${themeMode}">
    <div class="topbar-inner">
        <a class="brand-lockup" href="${cp}/home">
            <img class="brand-logo" src="${cp}/images/farm2home-logo.png" width="40" height="40" alt="Farm2Home Rampal Farm logo">
            <span class="brand-text">
                <span class="brand-name">Farm2Home</span>
                <span class="brand-tagline">Rampal Farm</span>
            </span>
        </a>
        <input type="checkbox" id="main-nav-toggle" class="nav-toggle">
        <label class="nav-burger" for="main-nav-toggle" aria-label="Open menu">
            <span class="nav-burger-lines"></span>
        </label>
        <nav class="primary-nav" aria-label="Main">
            <a class="nav-link" href="${cp}/home">Home</a>
            <a class="nav-link" href="${cp}/about.jsp">About</a>
            <a class="nav-link" href="${cp}/cart">Cart <span class="nav-pill">${navCartCount}</span></a>
            <c:if test="${not empty sessionScope.loggedInUser}">
                <a class="nav-link" href="${cp}/orders">Orders</a>
            </c:if>
            <c:if test="${empty sessionScope.loggedInUser}">
                <a class="nav-link" href="${cp}/register">Register</a>
                <a class="nav-link nav-link--emphasis" href="${cp}/login">Login</a>
            </c:if>
            <c:if test="${sessionScope.loggedInUser.role eq 'admin'}">
                <a class="nav-link" href="${cp}/admin/dashboard">Admin</a>
            </c:if>
            <c:if test="${not empty sessionScope.loggedInUser}">
                <a class="nav-link" href="${cp}/logout">Logout</a>
            </c:if>
            <form class="theme-switcher" action="${cp}/theme" method="post">
                <button type="submit" name="theme" value="light" class="theme-chip ${themeMode eq 'light' ? 'active' : ''}">Light</button>
                <button type="submit" name="theme" value="dark" class="theme-chip ${themeMode eq 'dark' ? 'active' : ''}">Dark</button>
            </form>
        </nav>
    </div>
</header>
