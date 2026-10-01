<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!doctype html><html lang="vi"><head><title>Đơn hàng #${order.orderId} - EcoMart</title></head><body>
<a class="btn btn-link px-0 mb-3" href="${pageContext.request.contextPath}/orders">&larr; Lịch sử đặt hàng</a>
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
    <div><h1 class="h2 mb-1">Đơn hàng #<c:out value="${order.orderId}"/></h1><p class="text-muted mb-0">Đặt lúc <c:out value="${order.createdAtText}"/></p></div>
    <span class="badge text-bg-${order.status.badge} fs-6"><c:out value="${order.status.label}"/></span>
</div>
<div class="row g-4 mb-4">
    <div class="col-md-7"><div class="card h-100 shadow-sm"><div class="card-body">
        <h2 class="h5">Thông tin nhận hàng</h2><p class="fw-semibold mb-1"><c:out value="${order.receiverName}"/></p>
        <p class="mb-1"><c:out value="${order.phone}"/></p><p class="mb-0" style="white-space:pre-wrap;overflow-wrap:anywhere"><c:out value="${order.address}"/></p>
        <c:if test="${not empty order.note}"><p class="small text-muted mt-3 mb-0" style="white-space:pre-wrap;overflow-wrap:anywhere">Ghi chú: <c:out value="${order.note}"/></p></c:if>
    </div></div></div>
    <div class="col-md-5"><div class="card h-100 shadow-sm"><div class="card-body">
        <h2 class="h5">Thanh toán</h2><p>Thanh toán khi nhận hàng (COD)</p><p class="small text-muted mb-0">Số tiền theo đơn: <strong><fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.##"/> đ</strong>.</p>
    </div></div></div>
</div>
<div class="card shadow-sm"><div class="table-responsive"><table class="table align-middle mb-0">
    <thead class="table-light"><tr><th>Sản phẩm</th><th class="text-end">Đơn giá lúc đặt</th><th class="text-center">Số lượng</th><th class="text-end">Thành tiền</th></tr></thead><tbody>
    <c:forEach items="${order.items}" var="item"><tr>
        <td><div class="fw-semibold"><c:out value="${item.productTitle}"/></div><small class="text-muted"><c:out value="${item.video.videoId}"/></small></td>
        <td class="text-end text-nowrap"><fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.##"/> đ</td><td class="text-center"><c:out value="${item.quantity}"/></td>
        <td class="text-end text-nowrap"><fmt:formatNumber value="${item.lineTotal}" pattern="#,##0.##"/> đ</td>
    </tr></c:forEach></tbody>
    <tfoot><tr><th colspan="3" class="text-end">Phí giao hàng</th><td class="text-end">0 đ</td></tr>
        <tr><th colspan="3" class="text-end">Tổng tiền</th><th class="text-end text-success text-nowrap"><fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.##"/> đ</th></tr></tfoot>
</table></div></div>
</body></html>
