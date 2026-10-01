<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><title>Đăng nhập</title></head><body>
<div class="row justify-content-center"><div class="col-md-5 col-lg-4">
    <div class="card shadow-sm"><div class="card-body p-4">
        <h1 class="h3 mb-3">Đăng nhập</h1>
        <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
        <c:if test="${not empty success}"><div class="alert alert-success"><c:out value="${success}"/></div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/login">
            <label class="form-label" for="username">Tên đăng nhập</label>
            <input id="username" class="form-control mb-3" name="username" value="<c:out value='${param.username}'/>" required autofocus>
            <label class="form-label" for="password">Mật khẩu</label>
            <input id="password" class="form-control mb-3" name="password" type="password" required>
            <button class="btn btn-primary w-100" type="submit">Đăng nhập</button>
        </form>
        <p class="mt-3 mb-0 text-center">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
    </div></div>
</div></div>
</body></html>
