<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | Product Management</title>
    <link rel="stylesheet" href="<c:url value='/css/main.css'/>">
</head>
<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="page-shell admin-two-column">
    <header class="page-headline page-headline--span-all">
        <p class="eyebrow">Farm2Home Admin</p>
        <h1>Manage products</h1>
        <p class="page-lead">Update product pricing, stock, category mapping, and custom product imagery from one screen.</p>
    </header>
    <section class="editor-panel">
        <div class="panel-title-row">
            <div>
                <p class="eyebrow">Product Form</p>
                <h2><c:choose><c:when test="${not empty editProduct}">Edit product</c:when><c:otherwise>Add product</c:otherwise></c:choose></h2>
            </div>
            <c:if test="${not empty editProduct}">
                <a class="secondary-btn" href="${pageContext.request.contextPath}/admin/products">Cancel</a>
            </c:if>
        </div>
        <c:if test="${not empty flashSuccess}">
            <div class="message-banner success">${flashSuccess}</div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="message-banner error">${flashError}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/products" method="post" enctype="multipart/form-data" class="dashboard-form">
            <input type="hidden" name="action" value="${not empty editProduct ? 'update' : 'create'}">
            <c:if test="${not empty editProduct}">
                <input type="hidden" name="productId" value="${editProduct.productId}">
                <input type="hidden" name="existingImageUrl" value="${editProduct.imageUrl}">
            </c:if>

            <label for="productName">Product name</label>
            <input id="productName" type="text" name="productName" value="${editProduct.productName}" required>

            <label for="description">Description</label>
            <textarea id="description" name="description" rows="4">${editProduct.description}</textarea>

            <label for="stockQuantity">Stock quantity</label>
            <input id="stockQuantity" type="number" min="0" name="stockQuantity" value="${editProduct.stockQuantity}" required>

            <label for="price">Price (NPR)</label>
            <input id="price" type="text" name="price" value="${editProduct.price}" required>

            <label for="categoryId">Category</label>
            <select id="categoryId" name="categoryId">
                <option value="">No category</option>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.categoryId}" ${editProduct.categoryId eq category.categoryId ? 'selected' : ''}>${category.categoryName}</option>
                </c:forEach>
            </select>

            <label for="image">Product image</label>
            <input id="image" type="file" name="image" accept=".jpg,.jpeg,.png,.gif">

            <button type="submit">${not empty editProduct ? 'Update Product' : 'Create Product'}</button>
        </form>
    </section>

    <section class="listing-panel">
        <div class="panel-title-row">
            <div>
                <p class="eyebrow">Inventory</p>
                <h2>Existing products</h2>
            </div>
            <span class="inline-badge">${products.size()} records</span>
        </div>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Product</th>
                    <th>Category</th>
                    <th>Stock</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="product">
                    <tr>
                        <td>${product.productName}</td>
                        <td>${product.categoryName}</td>
                        <td>${product.stockQuantity}</td>
                        <td>NPR <fmt:formatNumber value="${product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td class="actions-cell">
                            <a class="table-link" href="${pageContext.request.contextPath}/admin/products?edit=${product.productId}">Edit</a>
                            <form action="${pageContext.request.contextPath}/admin/products" method="post">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="productId" value="${product.productId}">
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
