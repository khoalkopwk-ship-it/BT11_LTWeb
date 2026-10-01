# EcoMart — DE03_24110253

Project source reviewed against De thi Qua trinh 03.
Required architecture:
- Servlet + JPA + JSP
- MVC / Service / DAO
- SiteMesh User/Admin decorators
- OTP register, session login/logout
- Video CRUD pagination
- Video detail
- Home category video pagination
- Category video count

1. Mở `http://localhost:8080/home`.

## Tài khoản demo

| Tài khoản | Mật khẩu | Mục đích |
|---|---|---|
| `admin` | `123456` | CRUD sản phẩm, chỉnh giá và tồn kho |
| `user01` | `123456` | Mua hàng, 8 đơn mẫu đủ 8 trạng thái |
| `user02` | `123456` | Kiểm tra lịch sử riêng của tài khoản khác |

Các tài khoản mẫu được tạo khi chưa có; script không đổi mật khẩu tài khoản đã tồn tại.

## Cấu hình môi trường

```powershell
$env:DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=DE03_LTW;encrypt=false"
$env:DB_USERNAME = "sa"
$env:DB_PASSWORD = "<mat-khau-sql-moi>"
$env:MAIL_USERNAME = "<email-cua-ban>"
$env:MAIL_PASSWORD = "<app-password-moi>"
$env:MAIL_HOST = "smtp.gmail.com"
$env:MAIL_PORT = "587"
# Chạy Tomcat từ chính cửa sổ này, khi server chưa chạy:
& "H:\IDE\IntellijIDEA\apache-tomcat-11.0.25\bin\catalina.bat" run
```
