<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!doctype html>
<html lang="vi">
<head><title>Chi tiết sản phẩm - <c:out value="${video.videoId}"/></title></head>
<body>
<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="h2 mb-0">Chi tiết sản phẩm</h1>
    <div>
        <c:url var="editUrl" value="/admin/video/edit"><c:param name="id" value="${video.videoId}"/></c:url>
        <a class="btn btn-primary" href="${editUrl}">Sửa</a>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/videos">Danh sách</a>
    </div>
</div>
<div class="card shadow-sm"><div class="card-body p-4">
    <div class="row g-4">
        <div class="col-lg-5">
            <c:choose>
                <c:when test="${not empty video.poster}"><img class="img-fluid rounded" src="<c:out value='${video.poster}'/>" alt="Poster"></c:when>
                <c:otherwise><img class="img-fluid rounded" src="${pageContext.request.contextPath}/assets/images/video-default.svg" alt="Chưa có poster"></c:otherwise>
            </c:choose>
        </div>
        <div class="col-lg-7">
            <h2 class="h3"><c:out value="${video.title}"/></h2>
            <dl class="row">
                <dt class="col-sm-4">Mã video</dt><dd class="col-sm-8"><c:out value="${video.videoId}"/></dd>
                <dt class="col-sm-4">Category</dt><dd class="col-sm-8"><c:out value="${video.category.categoryname}"/></dd>
                <dt class="col-sm-4">Lượt xem</dt><dd class="col-sm-8"><c:out value="${video.views}"/></dd>
                <dt class="col-sm-4">Giá bán</dt><dd class="col-sm-8"><fmt:formatNumber value="${video.price}" pattern="#,##0.##"/> đ</dd>
                <dt class="col-sm-4">Tồn kho</dt><dd class="col-sm-8"><c:out value="${video.stock}"/></dd>
                <dt class="col-sm-4">Lượt share</dt><dd class="col-sm-8"><c:out value="${video.shareCount}"/></dd>
                <dt class="col-sm-4">Lượt like</dt><dd class="col-sm-8"><c:out value="${video.likeCount}"/></dd>
                <dt class="col-sm-4">Trạng thái</dt><dd class="col-sm-8">${video.active ? 'Hoạt động' : 'Ẩn'}</dd>
                <dt class="col-sm-4">Mô tả</dt><dd class="col-sm-8"><c:out value="${video.description}" default="Chưa có mô tả."/></dd>
            </dl>
        </div>
    </div>
</div></div>
</body>
</html>
