<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<div class="mt-3">
    <p class="fs-5 fw-bold text-success mb-1"><fmt:formatNumber value="${video.price}" pattern="#,##0.##"/> đ</p>
    <p class="small text-muted mb-2">Còn <c:out value="${video.stock}"/> sản phẩm · Tối đa 99 cho mỗi sản phẩm</p>
    <c:choose>
        <c:when test="${video.purchasable}">
            <form class="d-flex align-items-end gap-2" method="post" action="${pageContext.request.contextPath}/cart/add">
                <input type="hidden" name="shopToken" value="${sessionScope.shopCsrfToken}">
                <input type="hidden" name="videoId" value="<c:out value='${video.videoId}'/>">
                <div>
                    <label class="form-label small mb-1" for="quantity-${video.videoId}">Số lượng</label>
                    <input class="form-control" style="width:5.5rem" id="quantity-${video.videoId}" name="quantity"
                           type="number" min="1" max="${video.maxOrderQuantity}" value="1" step="1" required>
                </div>
                <button class="btn btn-success flex-grow-1" type="submit">Thêm vào giỏ</button>
            </form>
        </c:when>
        <c:otherwise><span class="badge text-bg-secondary">Hết hàng hoặc ngừng bán</span></c:otherwise>
    </c:choose>
</div>
