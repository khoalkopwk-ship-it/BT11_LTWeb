<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">EcoMart</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainMenu" aria-controls="mainMenu" aria-expanded="false" aria-label="Mở menu"><span class="navbar-toggler-icon"></span></button>
        <div id="mainMenu" class="collapse navbar-collapse">
            <div class="navbar-nav me-auto">
                <a class="nav-link" href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/videos">Sản phẩm</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/cart">Giỏ hàng <span class="badge text-bg-success">${empty sessionScope.cart ? 0 : sessionScope.cart.totalQuantity}</span></a>
                <a class="nav-link" href="${pageContext.request.contextPath}/orders">Lịch sử đặt hàng</a>
                <c:if test="${sessionScope.account.admin}">
                    <a class="nav-link text-warning" href="${pageContext.request.contextPath}/admin">Trang quản trị</a>
                </c:if>
            </div>
            <div class="navbar-nav">
                <c:choose>
                    <c:when test="${empty sessionScope.account}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                    </c:when>
                    <c:otherwise>
                        <span class="navbar-text me-3">Xin chào, <c:out value="${sessionScope.account.fullname}"/></span>
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
