<c:set var="cp" value="${pageContext.request.contextPath}"/>
<footer class="site-footer">
    <div class="footer-inner">
        <div class="footer-brand">
            <a class="footer-brand-lockup" href="${cp}/home">
                <img class="footer-brand-logo" src="${cp}/images/farm2home-logo.png" width="48" height="48" alt="Farm2Home, Rampal Farm">
                <span class="footer-brand-text">
                    <span class="footer-brand-name">Farm2Home</span>
                    <span class="footer-brand-tagline">Rampal Farm</span>
                </span>
            </a>
            <p class="footer-desc">Fresh produce from Rampal Farm, straight to your door.</p>
        </div>
        <div class="footer-col">
            <p class="footer-heading">Shop</p>
            <ul class="footer-links">
                <li><a href="${cp}/home">Browse products</a></li>
                <li><a href="${cp}/home#products">Catalog</a></li>
                <li><a href="${cp}/cart">Cart</a></li>
            </ul>
        </div>
        <div class="footer-col footer-col-connect">
            <p class="footer-heading">Connect</p>
            <div class="footer-connect-lead">
                <img class="footer-connect-logo" src="${cp}/images/farm2home-logo.png" width="40" height="40" alt="">
                <span class="footer-connect-text">
                    <span class="footer-connect-name">Rampal Farm</span>
                    <span class="footer-connect-tagline">Farm2Home</span>
                </span>
            </div>
            <ul class="footer-links footer-social-list">
                <li>
                    <a class="footer-social" href="https://www.facebook.com/RampalFarm2Home" target="_blank" rel="noopener noreferrer">
                        <span class="footer-social-icon" aria-hidden="true">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24" fill="currentColor"><path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/></svg>
                        </span>
                        <span class="footer-social-label"> Rampal Farm2Home</span>
                    </a>
                </li>
                <li>
                    <a class="footer-social" href="https://www.instagram.com/RampalFarm2Home/" target="_blank" rel="noopener noreferrer">
                        <span class="footer-social-icon" aria-hidden="true">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24" fill="currentColor"><path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"/></svg>
                        </span>
                        <span class="footer-social-label"> @RampalFarm2Home</span>
                    </a>
                </li>
            </ul>
        </div>
        <div class="footer-col">
            <p class="footer-heading">Contact</p>
            <ul class="footer-links">
                <li><a href="tel:9860266956">9860266956</a></li>
                <li><a href="mailto:rampal23@gmail.com">rampal23@gmail.com</a></li>
            </ul>
        </div>
    </div>
    <p class="footer-legal">&copy; <c:out value="${pageContext.request.serverName}"/> &middot; Farm2Home &middot; Rampal Farm</p>
</footer>
