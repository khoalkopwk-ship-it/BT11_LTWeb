<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><title>Trang chủ quản trị</title></head><body>
<div class="p-5 bg-white rounded-3 shadow-sm">
    <h1>Trang chủ Admin</h1>
    <p class="lead">Xin chào <strong><c:out value="${sessionScope.account.fullname}"/></strong>. Bạn đã đăng nhập với quyền quản trị.</p>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/videos">Quản lý Video</a>
</div>
</body></html>
