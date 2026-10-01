<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="vi"><head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title" default="EcoMart"/></title>
    <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
    <sitemesh:write property="head"/>
</head><body class="bg-light d-flex flex-column min-vh-100">
<jsp:include page="/views/common/header.jsp"/>
<main class="container py-4 flex-grow-1"><jsp:include page="/views/common/shop-flash.jsp"/><sitemesh:write property="body"/></main>
<jsp:include page="/views/common/footer.jsp"/>
<script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/bootstrap.bundle.min.js"></script>
</body></html>
