<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="imgUrl" value="${param.imageUrl}"/>
<c:set var="pname" value="${param.name}"/>
<c:set var="ctx" value="${param.contextPath}"/>
<c:set var="mode" value="${param.mode}"/>
<c:set var="v" value="${empty param.variant ? 0 : param.variant}"/>
<c:set var="vMod" value="${v % 6}"/>
<c:set var="letter" value="?"/>
<c:if test="${not empty pname}">
    <c:set var="letter" value="${fn:toUpperCase(fn:substring(pname, 0, 1))}"/>
</c:if>
<c:set var="thumbClass" value="${mode eq 'thumb' ? ' product-visual-placeholder--thumb' : ''}"/>
<c:choose>
    <c:when test="${not empty imgUrl}">
        <c:url var="imageSrc" value="/${imgUrl}"/>
        <img src="${imageSrc}" alt="${fn:escapeXml(pname)}" loading="lazy">
    </c:when>
    <c:otherwise>
        <div class="product-visual-placeholder${thumbClass}" data-variant="${vMod}" role="img" aria-label="${fn:escapeXml(pname)}">
            <span class="product-visual-letter" aria-hidden="true"><c:out value="${letter}"/></span>
        </div>
    </c:otherwise>
</c:choose>
