<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!doctype html><html lang="vi"><head><title>Đặt hàng COD - EcoMart</title></head><body>
<h1 class="h2 mb-1">Đặt hàng COD</h1><p class="text-muted mb-4">Kiểm tra thông tin nhận hàng. Thanh toán bằng tiền mặt khi nhận hàng.</p>
<form method="post" action="${pageContext.request.contextPath}/checkout">
    <input type="hidden" name="shopToken" value="${sessionScope.shopCsrfToken}">
    <input type="hidden" name="checkoutToken" value="${sessionScope.checkoutToken}">
    <div class="row g-4">
        <div class="col-lg-7"><div class="card shadow-sm"><div class="card-body p-4">
            <h2 class="h5 mb-3">Thông tin nhận hàng</h2>
            <div class="mb-3"><label class="form-label" for="receiverName">Tên người nhận</label>
                <input class="form-control" id="receiverName" name="receiverName" minlength="2" maxlength="100" value="<c:out value='${form.receiverName}'/>" autocomplete="name" required></div>
            <div class="mb-3"><label class="form-label" for="phone">Số điện thoại</label>
                <input class="form-control" id="phone" name="phone" type="tel" inputmode="numeric" pattern="0[0-9]{9}" maxlength="10" value="<c:out value='${form.phone}'/>" placeholder="0901234567" autocomplete="tel" required>
                <div class="form-text">10 chữ số, bắt đầu bằng 0.</div></div>
            <div class="mb-3"><label class="form-label" for="address">Địa chỉ nhận hàng</label>
                <textarea class="form-control" id="address" name="address" rows="3" minlength="10" maxlength="500" autocomplete="street-address" required><c:out value="${form.address}"/></textarea></div>
            <div class="mb-4"><label class="form-label" for="note">Ghi chú (không bắt buộc)</label>
                <textarea class="form-control" id="note" name="note" rows="2" maxlength="500"><c:out value="${form.note}"/></textarea></div>
            <h2 class="h5 mb-3">Phương thức thanh toán</h2>
            <div class="form-check p-3 ps-5 border rounded bg-light">
                <input class="form-check-input" type="radio" id="cod" name="paymentMethod" value="COD" checked required>
                <label class="form-check-label" for="cod">Thanh toán khi nhận hàng (COD)</label>
            </div>
        </div></div></div>
        <div class="col-lg-5"><div class="card shadow-sm"><div class="card-body p-4">
            <h2 class="h5 mb-3">Đơn hàng của bạn</h2>
            <c:forEach items="${summary.lines}" var="line">
                <div class="d-flex justify-content-between gap-3 border-bottom py-3">
                    <div><div class="fw-semibold"><c:out value="${line.title}"/></div><small class="text-muted"><c:out value="${line.quantity}"/> × <fmt:formatNumber value="${line.price}" pattern="#,##0.##"/> đ</small></div>
                    <span class="text-nowrap"><fmt:formatNumber value="${line.lineTotal}" pattern="#,##0.##"/> đ</span>
                </div>
            </c:forEach>
            <div class="d-flex justify-content-between mt-3"><span>Phí giao hàng</span><span>0 đ</span></div>
            <div class="d-flex justify-content-between my-3 fs-5"><strong>Tổng tiền</strong><strong class="text-success"><fmt:formatNumber value="${summary.totalAmount}" pattern="#,##0.##"/> đ</strong></div>
            <button class="btn btn-success w-100" type="submit">Xác nhận đặt hàng COD</button>
            <a class="btn btn-link w-100 mt-2" href="${pageContext.request.contextPath}/cart">Quay lại sửa giỏ hàng</a>
            <p class="small text-muted mb-0">Đơn được tạo ở trạng thái Đơn hàng mới sau khi xác nhận.</p>
        </div></div></div>
    </div>
</form>
</body></html>
