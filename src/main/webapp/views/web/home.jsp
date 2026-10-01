<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <title>Trang chủ - Video theo Category</title>
    <style>
        .video-poster { aspect-ratio: 16 / 9; object-fit: cover; background: #e9ecef; }
        .category-section + .category-section { margin-top: 3rem; }
    </style>
</head>
<body>
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h1 class="h2 mb-1">Video theo Category</h1>
        <p class="text-muted mb-0">Mỗi Category hiển thị 3 video trên một trang.</p>
    </div>
</div>

<c:if test="${empty categoryPages}">
    <div class="alert alert-info">Chưa có Category đang hoạt động.</div>
</c:if>

<c:forEach items="${categoryPages}" var="group">
    <section class="category-section">
        <div class="d-flex align-items-center gap-2 border-bottom pb-2 mb-3">
            <h2 class="h4 mb-0"><c:out value="${group.category.categoryname}"/></h2>
            <span class="badge text-bg-primary"><c:out value="${group.videoCount}"/> video</span>
        </div>

        <c:choose>
            <c:when test="${empty group.videos}">
                <div class="alert alert-light border">Category này chưa có video đang hoạt động.</div>
            </c:when>
            <c:otherwise>
                <div class="row g-4">
                    <c:forEach items="${group.videos}" var="video">
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
                                    <h3 class="h5"><c:out value="${video.title}"/></h3>
                                    <dl class="row small mb-3">
                                        <dt class="col-5">Mã video:</dt><dd class="col-7"><c:out value="${video.videoId}"/></dd>
                                        <dt class="col-5">Category:</dt><dd class="col-7"><c:out value="${video.category.categoryname}"/></dd>
                                        <dt class="col-5">View:</dt><dd class="col-7"><c:out value="${video.views}"/></dd>
                                        <dt class="col-5">Share:</dt><dd class="col-7"><c:out value="${video.shareCount}"/></dd>
                                        <dt class="col-5">Like:</dt><dd class="col-7"><c:out value="${video.likeCount}"/></dd>
                                    </dl>
                                    <c:url var="detailUrl" value="/videos"><c:param name="id" value="${video.videoId}"/></c:url>
                                    <a class="btn btn-outline-primary mt-auto" href="${detailUrl}">Xem chi tiết</a>
                                    <%@ include file="../common/product-purchase.jsp" %>
                                </div>
                            </article>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <c:if test="${group.totalPages > 1}">
            <nav class="mt-4" aria-label="Phân trang ${group.category.categoryname}">
                <ul class="pagination justify-content-center mb-0">
                    <c:url var="previousUrl" value="/home">
                        <c:param name="page_${group.category.categoryId}" value="${group.currentPage - 1}"/>
                    </c:url>
                    <li class="page-item ${group.currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${previousUrl}" aria-label="Trang trước">&laquo;</a>
                    </li>
                    <c:forEach begin="1" end="${group.totalPages}" var="pageNumber">
                        <c:url var="pageUrl" value="/home">
                            <c:param name="page_${group.category.categoryId}" value="${pageNumber}"/>
                        </c:url>
                        <li class="page-item ${pageNumber == group.currentPage ? 'active' : ''}">
                            <a class="page-link" href="${pageUrl}"><c:out value="${pageNumber}"/></a>
                        </li>
                    </c:forEach>
                    <c:url var="nextUrl" value="/home">
                        <c:param name="page_${group.category.categoryId}" value="${group.currentPage + 1}"/>
                    </c:url>
                    <li class="page-item ${group.currentPage == group.totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="${nextUrl}" aria-label="Trang sau">&raquo;</a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </section>
</c:forEach>
</body>
</html>
