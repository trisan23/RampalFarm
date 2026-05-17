<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Farm2Home | User Management</title>
    <link rel="stylesheet" href="<c:url value='/css/main.css'/>">
</head>
<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="page-shell admin-two-column admin-two-column--users">
    <header class="page-headline page-headline--span-all">
        <p class="eyebrow">Farm2Home Admin</p>
        <h1>Manage users</h1>
        <p class="page-lead">Create staff or customer accounts, update profile details, and deactivate accounts when needed.</p>
    </header>
    <section class="editor-panel">
        <div class="panel-title-row">
            <div>
                <p class="eyebrow">User Form</p>
                <h2><c:choose><c:when test="${not empty editUser}">Edit user</c:when><c:otherwise>Add user</c:otherwise></c:choose></h2>
            </div>
            <c:if test="${not empty editUser}">
                <a class="secondary-btn" href="${pageContext.request.contextPath}/admin/users">Cancel</a>
            </c:if>
        </div>
        <c:if test="${not empty flashSuccess}">
            <div class="message-banner success">${flashSuccess}</div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="message-banner error">${flashError}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/users" method="post" class="dashboard-form">
            <input type="hidden" name="action" value="${not empty editUser ? 'update' : 'create'}">
            <c:if test="${not empty editUser}">
                <input type="hidden" name="userId" value="${editUser.userId}">
            </c:if>

            <label for="username">Full name</label>
            <input id="username" type="text" name="username" value="${editUser.username}" required>

            <label for="email">Email</label>
            <input id="email" type="email" name="email" value="${editUser.email}" required>

            <label for="phoneNumber">Phone number</label>
            <input id="phoneNumber" type="text" name="phoneNumber" value="${editUser.phoneNumber}" required>

            <label for="password">Password <span class="field-hint">${not empty editUser ? '(leave blank to keep current password)' : ''}</span></label>
            <input id="password" type="password" name="password" ${empty editUser ? 'required' : ''}>

            <label for="role">Role</label>
            <select id="role" name="role" required>
                <option value="customer" ${editUser.role eq 'customer' || empty editUser ? 'selected' : ''}>Customer</option>
                <option value="admin" ${editUser.role eq 'admin' ? 'selected' : ''}>Admin</option>
            </select>

            <label for="status">Status</label>
            <select id="status" name="status" required>
                <option value="active" ${editUser.status eq 'active' || empty editUser ? 'selected' : ''}>Active</option>
                <option value="inactive" ${editUser.status eq 'inactive' ? 'selected' : ''}>Inactive</option>
            </select>

            <button type="submit">${not empty editUser ? 'Update User' : 'Create User'}</button>
        </form>
    </section>

    <section class="listing-panel">
        <div class="panel-title-row">
            <div>
                <p class="eyebrow">Directory</p>
                <h2>Existing users</h2>
            </div>
            <span class="inline-badge">${users.size()} records</span>
        </div>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.phoneNumber}</td>
                        <td><span class="status-pill status-role">${user.role}</span></td>
                        <td><span class="status-pill status-${user.status}">${user.status}</span></td>
                        <td class="actions-cell">
                            <a class="table-link" href="${pageContext.request.contextPath}/admin/users?edit=${user.userId}">Edit</a>
                            <form action="${pageContext.request.contextPath}/admin/users" method="post">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="userId" value="${user.userId}">
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
