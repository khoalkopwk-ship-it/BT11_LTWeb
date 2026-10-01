<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <title>Sản phẩm</title>
    <style>.video-poster { aspect-ratio: 16 / 9; object-fit: cover; background: #e9ecef; }</style>
</head>
<body>
<div class="d-flex justify-content-between align-items-end mb-4">
    <div>
        <h1 class="h2 mb-1">Danh sách sản phẩm</h1>
        <p class="text-muted mb-0">Tổng cộng <c:out value="${totalItems}"/> video · 3 video/trang</p>
    </div>
</div>
<c:if test="${empty videos}"><div class="alert alert-info">Chưa có video đang hoạt động.</div></c:if>
<div class="row g-4">
    <c:forEach items="${videos}" var="video">
        <div class="col-md-6 col-lg-4">
            <article class="card h-100 shadow-sm">
                <c:choose>
                    <c:when test="${not empty video.poster}">
                        <img class="card-img-top video-poster" src="<c:out value='${video.poster}'/>" alt="Poster <c:out value='${video.title}'/>">
                    </c:when>
                    <c:otherwise>
                        <img class="card-img-top video-poster" src="${pageContext.request.contextPath}/assets/images/video-default.svg" alt="Chưa có poster">
                    </c:otherwise>
                </c:choose>
                <div class="card-body d-flex flex-column">
                    <h2 class="h5"><c:out value="${video.title}"/></h2>
                    <p class="text-muted"><c:out value="${video.category.categoryname}"/> · <c:out value="${video.views}"/> lượt xem</p>
                    <p><c:out value="${video.description}"/></p>
                    <c:url var="detailUrl" value="/videos"><c:param name="id" value="${video.videoId}"/></c:url>
                    <a class="btn btn-outline-primary mt-auto" href="${detailUrl}">Xem chi tiết</a>
                    <%@ include file="../common/product-purchase.jsp" %>
                </div>
            </article>
        </div>
    </c:forEach>
</div>

<c:if test="${totalPages > 1}">
    <nav class="mt-4" aria-label="Phân trang sản phẩm">
        <ul class="pagination justify-content-center">
            <c:url var="previousUrl" value="/videos"><c:param name="page" value="${page - 1}"/></c:url>
            <li class="page-item ${page == 1 ? 'disabled' : ''}">
                <a class="page-link" href="${previousUrl}" aria-label="Trang trước">&laquo;</a>
            </li>
            <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                <c:url var="pageUrl" value="/videos"><c:param name="page" value="${pageNumber}"/></c:url>
                <li class="page-item ${page == pageNumber ? 'active' : ''}">
                    <a class="page-link" href="${pageUrl}"><c:out value="${pageNumber}"/></a>
                </li>
            </c:forEach>
            <c:url var="nextUrl" value="/videos"><c:param name="page" value="${page + 1}"/></c:url>
            <li class="page-item ${page == totalPages ? 'disabled' : ''}">
                <a class="page-link" href="${nextUrl}" aria-label="Trang sau">&raquo;</a>
            </li>
        </ul>
    </nav>
</c:if>
</body>
</html>
