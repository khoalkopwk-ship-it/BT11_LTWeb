<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!doctype html><html lang="vi"><head><title>Lịch sử đặt hàng - EcoMart</title></head><body>
<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
    <div><h1 class="h2 mb-1">Lịch sử đặt hàng</h1><p class="text-muted mb-0">Theo dõi các đơn hàng của bạn theo trạng thái.</p></div>
    <a class="btn btn-outline-success" href="${pageContext.request.contextPath}/videos">Mua thêm sản phẩm</a>
</div>
<nav class="d-flex flex-wrap gap-2 mb-4" aria-label="Lọc trạng thái đơn hàng">
    <a class="btn btn-sm ${empty selectedStatus ? 'btn-primary' : 'btn-outline-primary'}" href="${pageContext.request.contextPath}/orders">Tất cả (<c:out value="${allItems}"/>)</a>
    <c:forEach items="${statuses}" var="status">
        <c:url var="filterUrl" value="/orders"><c:param name="status" value="${status.name}"/></c:url>
        <a class="btn btn-sm ${selectedStatus == status.name ? 'btn-primary' : 'btn-outline-primary'}" href="${filterUrl}"><c:out value="${status.label}"/> (<c:out value="${statusCounts[status.name]}"/>)</a>
    </c:forEach>
</nav>
<p class="small text-muted">Có <c:out value="${totalItems}"/> đơn phù hợp · Trang <c:out value="${page}"/>/<c:out value="${totalPages}"/></p>
<p class="d-md-none small text-muted">Vuốt bảng sang trái để xem trạng thái, tổng tiền và nút Chi tiết.</p>
<div class="card shadow-sm"><div class="table-responsive">
    <table class="table align-middle mb-0"><thead class="table-light"><tr><th>Mã đơn</th><th>Ngày đặt</th><th>Người nhận</th><th>Trạng thái</th><th class="text-end">Tổng tiền</th><th></th></tr></thead><tbody>
    <c:choose><c:when test="${empty orders}"><tr><td colspan="6" class="text-center text-muted py-5">Chưa có đơn hàng ở trạng thái này.</td></tr></c:when>
        <c:otherwise><c:forEach items="${orders}" var="order">
            <tr><td class="fw-semibold">#<c:out value="${order.orderId}"/></td>
                <td class="text-nowrap"><c:out value="${order.createdAtText}"/></td><td><c:out value="${order.receiverName}"/></td>
                <td><span class="badge text-bg-${order.status.badge}"><c:out value="${order.status.label}"/></span></td>
                <td class="text-end text-nowrap"><fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.##"/> đ</td>
                <td><c:url var="detailUrl" value="/orders/detail"><c:param name="id" value="${order.orderId}"/></c:url><a class="btn btn-sm btn-outline-primary text-nowrap" href="${detailUrl}">Chi tiết</a></td>
            </tr>
        </c:forEach></c:otherwise>
    </c:choose></tbody></table>
</div></div>
<c:if test="${totalPages > 1}"><nav class="mt-4" aria-label="Phân trang đơn hàng"><ul class="pagination justify-content-center flex-wrap">
    <c:forEach begin="1" end="${totalPages}" var="number">
        <c:url var="pageUrl" value="/orders"><c:param name="page" value="${number}"/><c:param name="status" value="${selectedStatus}"/></c:url>
        <li class="page-item ${number == page ? 'active' : ''}"><a class="page-link" href="${pageUrl}"><c:out value="${number}"/></a></li>
    </c:forEach>
</ul></nav></c:if>
</body></html>
