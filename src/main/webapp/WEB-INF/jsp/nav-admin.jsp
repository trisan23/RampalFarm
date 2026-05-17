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
<script>document.body.classList.remove('theme-light','theme-dark');document.body.classList.add('theme-${themeMode}');</script>
<header class="site-topbar site-topbar--admin theme-${themeMode}">
    <div class="topbar-inner">
        <a class="brand-lockup" href="${cp}/admin/dashboard">
            <img class="brand-logo" src="${cp}/images/farm2home-logo.png" width="40" height="40" alt="Farm2Home Admin logo">
            <span class="brand-text">
                <span class="brand-name">Farm2Home</span>
                <span class="brand-tagline">Admin</span>
            </span>
        </a>
        <input type="checkbox" id="admin-nav-toggle" class="nav-toggle">
        <label class="nav-burger" for="admin-nav-toggle" aria-label="Open menu">
            <span class="nav-burger-lines"></span>
        </label>
        <nav class="primary-nav" aria-label="Admin">
            <a class="nav-link" href="${cp}/admin/dashboard">Dashboard</a>
            <a class="nav-link" href="${cp}/admin/users">Users</a>
            <a class="nav-link" href="${cp}/admin/categories">Categories</a>
            <a class="nav-link" href="${cp}/admin/products">Products</a>
            <a class="nav-link" href="${cp}/orders">Orders</a>
            <a class="nav-link" href="${cp}/home">Storefront</a>
            <a class="nav-link" href="${cp}/logout">Logout</a>
            <form class="theme-switcher" action="${cp}/theme" method="post">
                <button type="submit" name="theme" value="light" class="theme-chip ${themeMode eq 'light' ? 'active' : ''}">Light</button>
                <button type="submit" name="theme" value="dark" class="theme-chip ${themeMode eq 'dark' ? 'active' : ''}">Dark</button>
            </form>
        </nav>
    </div>
</header>
