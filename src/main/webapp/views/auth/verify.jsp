<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html><head><title>Xác thực OTP</title></head><body>
<div class="row justify-content-center"><div class="col-md-5">
    <div class="card shadow-sm"><div class="card-body p-4">
        <h1 class="h3">Kích hoạt tài khoản</h1>
        <p class="text-muted">Nhập mã OTP 6 chữ số đã gửi tới email. Mã có hiệu lực trong 5 phút.</p>
        <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/verify">
            <input name="otp" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" class="form-control mb-3" required autofocus>
            <button class="btn btn-primary w-100" type="submit">Xác nhận</button>
        </form>
    </div></div>
</div></div>
</body></html>
