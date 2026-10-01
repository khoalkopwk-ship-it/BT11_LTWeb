<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!doctype html>
<html lang="vi">
<head><title>Quản lý sản phẩm</title></head>
<body>
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h1 class="h2 mb-1">Quản lý sản phẩm (Video)</h1>
        <p class="text-muted mb-0">Tổng cộng <c:out value="${totalItems}"/> video · 6 video/trang</p>
    </div>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/video/create">+ Thêm video</a>
</div>

<c:if test="${not empty success}"><div class="alert alert-success"><c:out value="${success}"/></div></c:if>
<c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>

<div class="card shadow-sm">
    <div class="table-responsive">
        <table class="table table-striped table-hover align-middle mb-0">
            <thead class="table-dark">
            <tr><th>Mã</th><th>Tiêu đề</th><th>Category</th><th>View</th><th>Giá (đ)</th><th>Tồn kho</th><th>Trạng thái</th><th class="text-end">Thao tác</th></tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty videos}">
                    <tr><td colspan="8" class="text-center py-4 text-muted">Chưa có video.</td></tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${videos}" var="video">
                        <tr>
                            <td><code><c:out value="${video.videoId}"/></code></td>
                            <td><c:out value="${video.title}"/></td>
                            <td><c:out value="${video.category.categoryname}" default="Chưa phân loại"/></td>
                            <td><c:out value="${video.views}"/></td>
                            <td class="text-nowrap"><fmt:formatNumber value="${video.price}" pattern="#,##0.##"/></td>
                            <td><c:out value="${video.stock}"/></td>
                            <td>
                                <span class="badge ${video.active ? 'text-bg-success' : 'text-bg-secondary'}">
                                    ${video.active ? 'Hoạt động' : 'Ẩn'}
                                </span>
                            </td>
                            <td class="text-end text-nowrap">
                                <c:url var="viewUrl" value="/admin/video/view"><c:param name="id" value="${video.videoId}"/></c:url>
                                <c:url var="editUrl" value="/admin/video/edit"><c:param name="id" value="${video.videoId}"/></c:url>
                                <a class="btn btn-sm btn-outline-secondary" href="${viewUrl}">Xem</a>
                                <a class="btn btn-sm btn-outline-primary" href="${editUrl}">Sửa</a>
                                <form class="d-inline" method="post" action="${pageContext.request.contextPath}/admin/video/delete"
                                      onsubmit="return confirm('Xóa video ${video.videoId}? Dữ liệu like/share liên quan cũng sẽ bị xóa.');">
                                    <input type="hidden" name="id" value="<c:out value='${video.videoId}'/>">
                                    <button class="btn btn-sm btn-outline-danger" type="submit">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>

<c:if test="${totalPages > 1}">
    <nav class="mt-4" aria-label="Phân trang quản lý video">
        <ul class="pagination justify-content-center">
            <c:url var="previousUrl" value="/admin/videos"><c:param name="page" value="${page - 1}"/></c:url>
            <li class="page-item ${page == 1 ? 'disabled' : ''}"><a class="page-link" href="${previousUrl}">&laquo;</a></li>
            <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                <c:url var="pageUrl" value="/admin/videos"><c:param name="page" value="${pageNumber}"/></c:url>
                <li class="page-item ${page == pageNumber ? 'active' : ''}"><a class="page-link" href="${pageUrl}">${pageNumber}</a></li>
            </c:forEach>
            <c:url var="nextUrl" value="/admin/videos"><c:param name="page" value="${page + 1}"/></c:url>
            <li class="page-item ${page == totalPages ? 'disabled' : ''}"><a class="page-link" href="${nextUrl}">&raquo;</a></li>
        </ul>
    </nav>
</c:if>
</body>
</html>
