<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <title><c:out value="${video.title}"/> - Chi tiết video</title>
    <style>.detail-poster { width: 100%; aspect-ratio: 16 / 9; object-fit: cover; background: #e9ecef; }</style>
</head>
<body>
<c:url var="backUrl" value="/home"/>
<a class="btn btn-link px-0 mb-3" href="${backUrl}">&larr; Quay lại trang chủ</a>
<article class="card shadow-sm overflow-hidden">
    <div class="row g-0">
        <div class="col-lg-6">
            <c:choose>
                <c:when test="${not empty video.poster}">
                    <img class="detail-poster" src="<c:out value='${video.poster}'/>" alt="Poster <c:out value='${video.title}'/>">
                </c:when>
                <c:otherwise>
                    <img class="detail-poster" src="${pageContext.request.contextPath}/assets/images/video-default.svg" alt="Chưa có poster">
                </c:otherwise>
            </c:choose>
        </div>
        <div class="col-lg-6">
            <div class="card-body p-4">
                <h1 class="h2 mb-4"><c:out value="${video.title}"/></h1>
                <dl class="row mb-0">
                    <dt class="col-sm-5">Mã video:</dt><dd class="col-sm-7"><c:out value="${video.videoId}"/></dd>
                    <dt class="col-sm-5">Category name:</dt><dd class="col-sm-7"><c:out value="${video.category.categoryname}"/></dd>
                    <dt class="col-sm-5">View:</dt><dd class="col-sm-7"><c:out value="${video.views}"/></dd>
                    <dt class="col-sm-5">Share:</dt><dd class="col-sm-7"><c:out value="${video.shareCount}"/></dd>
                    <dt class="col-sm-5">Like:</dt><dd class="col-sm-7"><c:out value="${video.likeCount}"/></dd>
                </dl>
                <%@ include file="../common/product-purchase.jsp" %>
            </div>
        </div>
    </div>
    <div class="card-body border-top p-4">
        <h2 class="h5">Mô tả</h2>
        <p class="mb-0"><c:out value="${video.description}" default="Chưa có mô tả."/></p>
    </div>
</article>
</body>
</html>
