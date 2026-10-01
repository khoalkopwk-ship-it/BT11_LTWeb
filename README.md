> Bắt buộc cấu hình biến môi trường DB và mail theo [CAU_HINH_RIENG.md](CAU_HINH_RIENG.md) trước khi chạy.

# EcoMart — DE03_24110253

Dự án kiểm tra quá trình đã được bổ sung giỏ hàng, đặt hàng COD và lịch sử đơn theo 8 trạng thái.
Giữ công nghệ và kiến trúc bài gốc: **Java 17, Servlet 6.1, JSP 4, JSTL 3, JPA/Hibernate 6.6.1,
SQL Server, SiteMesh 3, Maven WAR**. Dự án này **không phải Spring Boot**.

## Chạy nhanh

1. Mở SQL Server Management Studio, mở `database.sql` bằng UTF-8 và Execute toàn bộ file **một lần**.
   File tạo database `DE03_LTW`, các bảng gốc, giá/tồn kho, bảng đơn hàng và dữ liệu mẫu.
2. Kiểm tra kết nối SQL Server trong `src/main/resources/META-INF/persistence.xml`.
   Mặc định giữ cấu hình từ bài gốc: máy local, cổng 1433, database `DE03_LTW`, tài khoản `sa`.
   Thay mật khẩu hoặc cổng nếu SQL Server của bạn khác. Có thể dùng các biến môi trường
   `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` thay vì sửa file.
3. Dùng JDK 17 trở lên và Maven:

   ```powershell
   mvn clean package
   ```

4. Dừng Tomcat 11, thay deployment cũ bằng `target/ROOT.war` trong thư mục `webapps`, rồi khởi động lại.
   Bản ZIP cũng kèm `dist/ROOT.war` đã build để chép trực tiếp nếu muốn chạy ngay.
5. Mở `http://localhost:8080/home`.

Nếu Tomcat chạy cổng khác, thay `8080`. Nếu đổi tên WAR, thêm context tương ứng vào URL.
Không cần SMTP để đăng nhập bằng các tài khoản demo hoặc thử giỏ hàng/COD/lịch sử đơn.
Bootstrap 5.3.3 đã được lưu trong `assets/vendor/bootstrap` để giao diện hoạt động khi không có Internet.

## Tài khoản demo

| Tài khoản | Mật khẩu | Mục đích |
|---|---|---|
| `admin` | `123456` | CRUD sản phẩm, chỉnh giá và tồn kho |
| `user01` | `123456` | Mua hàng, 8 đơn mẫu đủ 8 trạng thái |
| `user02` | `123456` | Kiểm tra lịch sử riêng của tài khoản khác |

Các tài khoản mẫu được tạo khi chưa có; script không đổi mật khẩu tài khoản đã tồn tại.

## Các trang chính

| URL | Chức năng |
|---|---|
| `/home` | Trang chủ, phân trang video theo Category, thêm sản phẩm vào giỏ |
| `/videos` | Danh sách sản phẩm, thêm vào giỏ |
| `/videos?id=SP005` | Sản phẩm mẫu còn 3 chiếc, thử giới hạn số lượng |
| `/videos?id=SP006` | Sản phẩm mẫu hết hàng |
| `/cart` | Xem giỏ, sửa số lượng, xóa một mục hoặc xóa toàn bộ |
| `/checkout` | Nhập thông tin nhận hàng, xác nhận COD; cần đăng nhập |
| `/orders` | Lịch sử đơn của tài khoản hiện tại; 5 đơn/trang |
| `/orders?status=CONFIRMED` | Lọc đơn đã xác nhận |
| `/orders/detail?id=1` | Chi tiết đơn, chỉ chủ đơn được xem |
| `/admin/videos` | CRUD sản phẩm, 6 sản phẩm/trang, có giá và tồn kho |

## Tài liệu và database

- **`BO_SUNG_GIO_HANG_COD_LICH_SU.md`**: mô tả phần đã bổ sung, luồng xử lý, file liên quan và các bước demo/kiểm thử.
- `database.sql`: file đầy đủ, dùng để tạo hoặc bổ sung database và dữ liệu mẫu trong một lần chạy.
- `database/02_cart_cod_orders.sql`: migration riêng nếu đã chạy database.sql của bài gốc. Không cần chạy lại sau file đầy đủ.
- `database/03_demo_change_status.sql`: lệnh thay đổi trạng thái để quan sát trên trang lịch sử.
- `KIEM_THU.md`: kết quả kiểm tra và phạm vi đã kiểm chứng.

Giỏ hàng dùng session nên đăng xuất/hết session sẽ xóa giỏ; các đơn đã đặt được lưu trong database.
Chỉ hỗ trợ COD, phí giao hàng của bài demo là 0 đ. Việc đổi trạng thái trực tiếp trong SQL chỉ phục vụ hiển thị/lọc;
không tự động thu tiền, hoàn tiền hoặc hoàn tồn kho.

Chức năng đăng ký OTP gốc vẫn giữ cấu hình trong `EmailUtil`; nếu cần thử đăng ký, sử dụng cấu hình SMTP phù hợp.
