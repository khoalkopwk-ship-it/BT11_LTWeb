<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><title>Đăng ký</title></head><body>
<div class="row justify-content-center"><div class="col-md-6 col-lg-5">
    <div class="card shadow-sm"><div class="card-body p-4">
        <h1 class="h3 mb-3">Đăng ký tài khoản</h1>
        <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/register">
            <label class="form-label" for="fullname">Họ tên</label>
            <input id="fullname" name="fullname" value="<c:out value='${param.fullname}'/>" class="form-control mb-3" required maxlength="50">
            <label class="form-label" for="username">Tên đăng nhập</label>
            <input id="username" name="username" value="<c:out value='${param.username}'/>" class="form-control mb-3" required minlength="3" maxlength="50">
            <label class="form-label" for="password">Mật khẩu</label>
            <input id="password" name="password" type="password" class="form-control mb-3" required minlength="6" maxlength="50">
            <label class="form-label" for="email">Email nhận OTP</label>
            <input id="email" name="email" type="email" value="<c:out value='${param.email}'/>" class="form-control mb-3" required maxlength="150">
            <label class="form-label" for="phone">Số điện thoại</label>
            <input id="phone" name="phone" value="<c:out value='${param.phone}'/>" class="form-control mb-3" maxlength="15">
            <button class="btn btn-success w-100" type="submit">Gửi mã OTP</button>
        </form>
    </div></div>
</div></div>
</body></html>
