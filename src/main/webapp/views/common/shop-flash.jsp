<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${not empty requestScope.shopSuccess}"><div class="alert alert-success" role="status"><c:out value="${requestScope.shopSuccess}"/></div></c:if>
<c:if test="${not empty requestScope.shopError}"><div class="alert alert-danger" role="alert"><c:out value="${requestScope.shopError}"/></div></c:if>
