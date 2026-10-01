<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!doctype html><html lang="vi"><head><title>Giỏ hàng - EcoMart</title></head><body>
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
    <div><h1 class="h2 mb-1">Giỏ hàng</h1><p class="text-muted mb-0">Kiểm tra sản phẩm và số lượng trước khi đặt COD.</p></div>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/videos">Tiếp tục mua sắm</a>
</div>
<c:choose>
    <c:when test="${empty summary.lines}">
        <div class="card shadow-sm"><div class="card-body text-center p-5">
            <h2 class="h4">Giỏ hàng đang trống</h2><p class="text-muted">Chọn sản phẩm để bắt đầu đơn hàng của bạn.</p>
            <a class="btn btn-success" href="${pageContext.request.contextPath}/videos">Xem sản phẩm</a>
        </div></div>
    </c:when>
    <c:otherwise>
        <p class="d-md-none small text-muted">Vuốt bảng sang trái để xem thành tiền và nút Xóa.</p>
        <div class="card shadow-sm mb-4"><div class="table-responsive">
            <table class="table align-middle mb-0"><thead class="table-light"><tr>
                <th>Sản phẩm</th><th class="text-end">Đơn giá</th><th style="min-width:230px">Số lượng</th><th class="text-end">Thành tiền</th><th></th>
            </tr></thead><tbody>
            <c:forEach items="${summary.lines}" var="line">
                <tr>
                    <td><div class="fw-semibold"><c:out value="${line.title}"/></div><small class="text-muted"><c:out value="${line.videoId}"/></small>
                        <c:if test="${not empty line.problem}"><div class="small text-danger mt-1"><c:out value="${line.problem}"/></div></c:if>
                    </td>
                    <td class="text-end text-nowrap"><fmt:formatNumber value="${line.price}" pattern="#,##0.##"/> đ</td>
                    <td>
                        <c:choose><c:when test="${line.maxQuantity > 0}">
                            <form class="d-flex gap-2" method="post" action="${pageContext.request.contextPath}/cart/update">
                                <input type="hidden" name="shopToken" value="${sessionScope.shopCsrfToken}">
                                <input type="hidden" name="videoId" value="<c:out value='${line.videoId}'/>">
                                <input class="form-control form-control-sm" style="max-width:90px" name="quantity" type="number" min="1"
                                       max="${line.maxQuantity}" value="${line.quantity}" step="1" aria-label="Số lượng ${line.videoId}" required>
                                <button class="btn btn-sm btn-outline-primary" type="submit">Cập nhật</button>
                            </form><small class="text-muted">Tối đa <c:out value="${line.maxQuantity}"/></small>
                        </c:when><c:otherwise><c:out value="${line.quantity}"/></c:otherwise></c:choose>
                    </td>
                    <td class="text-end text-nowrap"><fmt:formatNumber value="${line.lineTotal}" pattern="#,##0.##"/> đ</td>
                    <td><form method="post" action="${pageContext.request.contextPath}/cart/remove">
                        <input type="hidden" name="shopToken" value="${sessionScope.shopCsrfToken}">
                        <input type="hidden" name="videoId" value="<c:out value='${line.videoId}'/>">
                        <button class="btn btn-sm btn-outline-danger" type="submit" aria-label="Xóa ${line.videoId}">Xóa</button>
                    </form></td>
                </tr>
            </c:forEach></tbody></table>
        </div></div>
        <div class="row g-3">
            <div class="col-md-6"><form method="post" action="${pageContext.request.contextPath}/cart/clear">
                <input type="hidden" name="shopToken" value="${sessionScope.shopCsrfToken}">
                <button class="btn btn-outline-danger" type="submit">Xóa toàn bộ giỏ</button>
            </form><p class="small text-muted mt-3">Số lượng từ 1 đến 99 và không vượt tồn kho. Nhấn Cập nhật sau khi sửa.</p></div>
            <div class="col-md-6"><div class="card shadow-sm"><div class="card-body">
                <div class="d-flex justify-content-between mb-2"><span>Tổng số lượng</span><strong><c:out value="${summary.totalQuantity}"/></strong></div>
                <div class="d-flex justify-content-between mb-2"><span>Phí giao hàng</span><span>0 đ</span></div>
                <div class="d-flex justify-content-between border-top pt-3 mb-3"><strong>Tổng tiền</strong><strong class="text-success"><fmt:formatNumber value="${summary.totalAmount}" pattern="#,##0.##"/> đ</strong></div>
                <c:choose><c:when test="${summary.checkoutAllowed}"><a class="btn btn-success w-100" href="${pageContext.request.contextPath}/checkout">Đặt hàng COD</a></c:when>
                    <c:otherwise><button class="btn btn-secondary w-100" disabled>Sửa giỏ hàng để tiếp tục</button></c:otherwise></c:choose>
                <c:if test="${empty sessionScope.account}"><p class="small text-muted mt-2 mb-0">Bạn sẽ đăng nhập trước khi xác nhận đơn.</p></c:if>
            </div></div></div>
        </div>
    </c:otherwise>
</c:choose>
</body></html>
