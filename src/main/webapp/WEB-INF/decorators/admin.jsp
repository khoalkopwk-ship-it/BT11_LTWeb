<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="vi"><head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title" default="Quản trị EcoMart"/></title>
    <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
    <sitemesh:write property="head"/>
</head><body class="bg-body-tertiary d-flex flex-column min-vh-100">
<nav class="navbar navbar-dark bg-primary"><div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/admin">EcoMart Admin</a>
    <div class="d-flex gap-3"><a class="text-white text-decoration-none" href="${pageContext.request.contextPath}/admin/videos">Quản lý sản phẩm</a><a class="text-white text-decoration-none" href="${pageContext.request.contextPath}/home">Trang người dùng</a><a class="text-white text-decoration-none" href="${pageContext.request.contextPath}/logout">Đăng xuất</a></div>
</div></nav>
<main class="container py-4 flex-grow-1"><sitemesh:write property="body"/></main>
<jsp:include page="/views/common/footer.jsp"/>
<script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/bootstrap.bundle.min.js"></script>
</body></html>
