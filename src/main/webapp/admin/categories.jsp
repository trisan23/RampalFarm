<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | Category Management</title>
    <link rel="stylesheet" href="<c:url value='/css/main.css'/>">
</head>
<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="page-shell admin-two-column">
    <header class="page-headline page-headline--span-all">
        <p class="eyebrow">Farm2Home Admin</p>
        <h1>Manage categories</h1>
        <p class="page-lead">Keep the storefront organized by grouping products into clean, reusable categories.</p>
    </header>
    <section class="editor-panel">
        <div class="panel-title-row">
            <div>
                <p class="eyebrow">Category Form</p>
                <h2><c:choose><c:when test="${not empty editCategory}">Edit category</c:when><c:otherwise>Add category</c:otherwise></c:choose></h2>
            </div>
            <c:if test="${not empty editCategory}">
                <a class="secondary-btn" href="${pageContext.request.contextPath}/admin/categories">Cancel</a>
            </c:if>
        </div>
        <c:if test="${not empty flashSuccess}">
            <div class="message-banner success">${flashSuccess}</div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="message-banner error">${flashError}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/categories" method="post" class="dashboard-form">
            <input type="hidden" name="action" value="${not empty editCategory ? 'update' : 'create'}">
            <c:if test="${not empty editCategory}">
                <input type="hidden" name="categoryId" value="${editCategory.categoryId}">
            </c:if>

            <label for="categoryName">Category name</label>
            <input id="categoryName" type="text" name="categoryName" value="${editCategory.categoryName}" required>

            <label for="description">Description</label>
            <textarea id="description" name="description" rows="4">${editCategory.description}</textarea>

            <button type="submit">${not empty editCategory ? 'Update Category' : 'Create Category'}</button>
        </form>
    </section>

    <section class="listing-panel">
        <div class="panel-title-row">
            <div>
                <p class="eyebrow">Catalog Structure</p>
                <h2>Existing categories</h2>
            </div>
            <span class="inline-badge">${categories.size()} records</span>
        </div>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${categories}" var="category">
                    <tr>
                        <td>${category.categoryName}</td>
                        <td>${category.description}</td>
                        <td class="actions-cell">
                            <a class="table-link" href="${pageContext.request.contextPath}/admin/categories?edit=${category.categoryId}">Edit</a>
                            <form action="${pageContext.request.contextPath}/admin/categories" method="post">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="categoryId" value="${category.categoryId}">
                                <button type="submit" class="danger-btn">Delete</button>
                            </form>
                        </td>
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
