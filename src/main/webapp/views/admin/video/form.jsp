<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head><title>${editing ? 'Cập nhật' : 'Thêm'} sản phẩm</title></head>
<body>
<div class="row justify-content-center">
    <div class="col-xl-8">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h1 class="h2 mb-0">${editing ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm'}</h1>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/videos">Quay lại</a>
        </div>
        <div class="card shadow-sm"><div class="card-body p-4">
            <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
            <form method="post" action="${pageContext.request.contextPath}/admin/video/save">
                <input type="hidden" name="mode" value="${editing ? 'edit' : 'create'}">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label" for="videoId">Mã video</label>
                        <input class="form-control" id="videoId" name="videoId" maxlength="50"
                               pattern="[A-Za-z0-9_-]+" value="<c:out value='${video.videoId}'/>" ${editing ? 'readonly' : ''} required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="categoryId">Category</label>
                        <select class="form-select" id="categoryId" name="categoryId" required>
                            <option value="">-- Chọn Category --</option>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.categoryId}" ${video.category.categoryId == category.categoryId ? 'selected' : ''}>
                                    <c:out value="${category.categoryname}"/>${category.status ? '' : ' (đang ẩn)'}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-12">
                        <label class="form-label" for="title">Tiêu đề</label>
                        <input class="form-control" id="title" name="title" maxlength="200" value="<c:out value='${video.title}'/>" required>
                    </div>
                    <div class="col-md-8">
                        <label class="form-label" for="poster">Poster (URL hoặc đường dẫn)</label>
                        <input class="form-control" id="poster" name="poster" maxlength="50" value="<c:out value='${video.poster}'/>">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label" for="views">Lượt xem</label>
                        <input class="form-control" id="views" name="views" type="number" min="0" value="${empty video.views ? 0 : video.views}" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="price">Giá bán (đ)</label>
                        <input class="form-control" id="price" name="price" type="number" min="0.01" max="9999999999999999.99" step="0.01" value="${video.price}" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="stock">Tồn kho</label>
                        <input class="form-control" id="stock" name="stock" type="number" min="0" max="2147483647" step="1" value="${video.stock}" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label" for="description">Mô tả</label>
                        <textarea class="form-control" id="description" name="description" rows="5" maxlength="500"><c:out value="${video.description}"/></textarea>
                    </div>
                    <div class="col-12">
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" id="active" name="active" ${!editing || video.active ? 'checked' : ''}>
                            <label class="form-check-label" for="active">Đang hoạt động</label>
                        </div>
                    </div>
                </div>
                <button class="btn btn-primary mt-4" type="submit">${editing ? 'Lưu thay đổi' : 'Thêm video'}</button>
            </form>
        </div></div>
    </div>
</div>
</body>
</html>
