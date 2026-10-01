IF DB_ID(N'DE03_LTW') IS NULL
    CREATE DATABASE DE03_LTW;
GO

USE DE03_LTW;
GO

IF OBJECT_ID(N'dbo.Users', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Users
    (
        Username NVARCHAR(50) NOT NULL PRIMARY KEY,
        Password NVARCHAR(50) NULL,
        Phone NVARCHAR(15) NULL,
        Fullname NVARCHAR(50) NULL,
        Email NVARCHAR(150) NULL,
        Admin BIT NULL,
        Active BIT NULL,
        Images NVARCHAR(500) NULL
    );
END;
GO

IF OBJECT_ID(N'dbo.Category', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Category
    (
        CategoryId INT IDENTITY(1,1) PRIMARY KEY,
        Categoryname NVARCHAR(100) NULL,
        Categorycode NVARCHAR(100) NULL,
        Images NVARCHAR(500) NULL,
        Status BIT NULL
    );
END;
GO

IF OBJECT_ID(N'dbo.Videos', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Videos
    (
        VideoId NVARCHAR(50) NOT NULL PRIMARY KEY,
        Title NVARCHAR(200) NULL,
        Poster NVARCHAR(50) NULL,
        Views INT NULL,
        Description NVARCHAR(500) NULL,
        Active BIT NULL,
        CategoryId INT NULL,
        CONSTRAINT FK_Videos_Category FOREIGN KEY (CategoryId) REFERENCES dbo.Category(CategoryId)
    );
END;
GO

IF OBJECT_ID(N'dbo.Favorites', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Favorites
    (
        FavoriteId INT IDENTITY(1,1) PRIMARY KEY,
        LikedDate DATE NULL,
        VideoId NVARCHAR(50) NULL,
        Username NVARCHAR(50) NULL,
        CONSTRAINT FK_Favorites_Video FOREIGN KEY (VideoId) REFERENCES dbo.Videos(VideoId),
        CONSTRAINT FK_Favorites_User FOREIGN KEY (Username) REFERENCES dbo.Users(Username)
    );
END;
GO

IF OBJECT_ID(N'dbo.Shares', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Shares
    (
        ShareId INT IDENTITY(1,1) PRIMARY KEY,
        Emails NVARCHAR(50) NULL,
        SharedDate DATE NULL,
        Username NVARCHAR(50) NULL,
        VideoId NVARCHAR(50) NULL,
        CONSTRAINT FK_Shares_User FOREIGN KEY (Username) REFERENCES dbo.Users(Username),
        CONSTRAINT FK_Shares_Video FOREIGN KEY (VideoId) REFERENCES dbo.Videos(VideoId)
    );
END;
GO

-- Tài khoản mẫu: admin / 123456.
IF NOT EXISTS (SELECT 1 FROM dbo.Users WHERE Username = N'admin')
    INSERT dbo.Users (Username, Password, Fullname, Email, Admin, Active)
    VALUES (N'admin', N'123456', N'Quản trị viên', N'admin@ecomart.local', 1, 1);
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE Categorycode = 'SANPHAM')
    INSERT dbo.Category (Categoryname, Categorycode, Status) VALUES (N'Sản phẩm', 'SANPHAM', 1);
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE Categorycode = 'CONGNGHE')
    INSERT dbo.Category (Categoryname, Categorycode, Status) VALUES (N'Công nghệ xanh', 'CONGNGHE', 1);
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE Categorycode = 'HUONGDAN')
    INSERT dbo.Category (Categoryname, Categorycode, Status) VALUES (N'Hướng dẫn', 'HUONGDAN', 1);
GO

DECLARE @CategoryId INT = (SELECT TOP 1 CategoryId FROM dbo.Category WHERE Categorycode = 'SANPHAM');
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'SP001')
BEGIN
    INSERT dbo.Videos (VideoId, Title, Views, Description, Active, CategoryId) VALUES
    ('SP001', N'Sản phẩm EcoMart 01', 120, N'Sản phẩm thân thiện với môi trường.', 1, @CategoryId),
    ('SP002', N'Sản phẩm EcoMart 02', 85, N'Lựa chọn tiết kiệm cho gia đình.', 1, @CategoryId);
END;
GO

DECLARE @SanPhamId INT = (SELECT TOP 1 CategoryId FROM dbo.Category WHERE Categorycode = 'SANPHAM');
DECLARE @CongNgheId INT = (SELECT TOP 1 CategoryId FROM dbo.Category WHERE Categorycode = 'CONGNGHE');
DECLARE @HuongDanId INT = (SELECT TOP 1 CategoryId FROM dbo.Category WHERE Categorycode = 'HUONGDAN');

IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'SP003')
    INSERT dbo.Videos (VideoId, Title, Poster, Views, Description, Active, CategoryId)
    VALUES ('SP003', N'Bình nước tái sử dụng', NULL, 76, N'Giảm chai nhựa dùng một lần.', 1, @SanPhamId);
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'SP004')
    INSERT dbo.Videos (VideoId, Title, Poster, Views, Description, Active, CategoryId)
    VALUES ('SP004', N'Túi vải EcoMart', NULL, 61, N'Túi vải bền, nhẹ và thân thiện môi trường.', 1, @SanPhamId);
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'CN001')
    INSERT dbo.Videos (VideoId, Title, Poster, Views, Description, Active, CategoryId)
    VALUES ('CN001', N'Năng lượng mặt trời', NULL, 210, N'Giới thiệu giải pháp năng lượng sạch.', 1, @CongNgheId);
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'CN002')
    INSERT dbo.Videos (VideoId, Title, Poster, Views, Description, Active, CategoryId)
    VALUES ('CN002', N'Tái chế thông minh', NULL, 145, N'Ứng dụng công nghệ trong phân loại rác.', 1, @CongNgheId);
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'CN003')
    INSERT dbo.Videos (VideoId, Title, Poster, Views, Description, Active, CategoryId)
    VALUES ('CN003', N'Nhà ở tiết kiệm điện', NULL, 98, N'Các thiết bị giúp tối ưu điện năng.', 1, @CongNgheId);
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'CN004')
    INSERT dbo.Videos (VideoId, Title, Poster, Views, Description, Active, CategoryId)
    VALUES ('CN004', N'Giao thông xanh', NULL, 130, N'Xu hướng phương tiện giảm phát thải.', 1, @CongNgheId);
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'HD001')
    INSERT dbo.Videos (VideoId, Title, Poster, Views, Description, Active, CategoryId)
    VALUES ('HD001', N'Phân loại rác tại nhà', NULL, 180, N'Hướng dẫn phân loại rác theo từng nhóm.', 1, @HuongDanId);
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'HD002')
    INSERT dbo.Videos (VideoId, Title, Poster, Views, Description, Active, CategoryId)
    VALUES ('HD002', N'Tái sử dụng đồ cũ', NULL, 90, N'Biến vật dụng cũ thành sản phẩm hữu ích.', 1, @HuongDanId);
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'HD003')
    INSERT dbo.Videos (VideoId, Title, Poster, Views, Description, Active, CategoryId)
    VALUES ('HD003', N'Mua sắm không rác thải', NULL, 115, N'Chuẩn bị cho một lần mua sắm xanh.', 1, @HuongDanId);
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'HD004')
    INSERT dbo.Videos (VideoId, Title, Poster, Views, Description, Active, CategoryId)
    VALUES ('HD004', N'Tiết kiệm nước', NULL, 88, N'Những thay đổi nhỏ giúp giảm lượng nước sử dụng.', 1, @HuongDanId);

-- Đồng bộ lại chuỗi Unicode nếu script từng được chạy bằng sqlcmd sai code page.
UPDATE dbo.Category SET Categoryname = N'Sản phẩm' WHERE Categorycode = 'SANPHAM';
UPDATE dbo.Category SET Categoryname = N'Công nghệ xanh' WHERE Categorycode = 'CONGNGHE';
UPDATE dbo.Category SET Categoryname = N'Hướng dẫn' WHERE Categorycode = 'HUONGDAN';

UPDATE dbo.Videos SET Title = N'Bình nước tái sử dụng', Description = N'Giảm chai nhựa dùng một lần.' WHERE VideoId = 'SP003';
UPDATE dbo.Videos SET Title = N'Túi vải EcoMart', Description = N'Túi vải bền, nhẹ và thân thiện môi trường.' WHERE VideoId = 'SP004';
UPDATE dbo.Videos SET Title = N'Năng lượng mặt trời', Description = N'Giới thiệu giải pháp năng lượng sạch.' WHERE VideoId = 'CN001';
UPDATE dbo.Videos SET Title = N'Tái chế thông minh', Description = N'Ứng dụng công nghệ trong phân loại rác.' WHERE VideoId = 'CN002';
UPDATE dbo.Videos SET Title = N'Nhà ở tiết kiệm điện', Description = N'Các thiết bị giúp tối ưu điện năng.' WHERE VideoId = 'CN003';
UPDATE dbo.Videos SET Title = N'Giao thông xanh', Description = N'Xu hướng phương tiện giảm phát thải.' WHERE VideoId = 'CN004';
UPDATE dbo.Videos SET Title = N'Phân loại rác tại nhà', Description = N'Hướng dẫn phân loại rác theo từng nhóm.' WHERE VideoId = 'HD001';
UPDATE dbo.Videos SET Title = N'Tái sử dụng đồ cũ', Description = N'Biến vật dụng cũ thành sản phẩm hữu ích.' WHERE VideoId = 'HD002';
UPDATE dbo.Videos SET Title = N'Mua sắm không rác thải', Description = N'Chuẩn bị cho một lần mua sắm xanh.' WHERE VideoId = 'HD003';
UPDATE dbo.Videos SET Title = N'Tiết kiệm nước', Description = N'Những thay đổi nhỏ giúp giảm lượng nước sử dụng.' WHERE VideoId = 'HD004';
GO

-- Dữ liệu tương tác mẫu để kiểm tra số Share/Like động trên giao diện.
IF NOT EXISTS (SELECT 1 FROM dbo.Users WHERE Username = N'user01')
    INSERT dbo.Users (Username, Password, Fullname, Email, Admin, Active)
    VALUES (N'user01', N'123456', N'Người dùng mẫu', N'user01@ecomart.local', 0, 1);
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Favorites WHERE VideoId = 'SP001' AND Username = N'user01')
    INSERT dbo.Favorites (LikedDate, VideoId, Username) VALUES (CAST(GETDATE() AS DATE), 'SP001', N'user01');
IF NOT EXISTS (SELECT 1 FROM dbo.Shares WHERE VideoId = 'SP001' AND Username = N'user01')
    INSERT dbo.Shares (Emails, SharedDate, Username, VideoId)
    VALUES (N'friend@ecomart.local', CAST(GETDATE() AS DATE), N'user01', 'SP001');
GO

-- ====================================================
-- GIỎ HÀNG, COD VÀ LỊCH SỬ ĐƠN HÀNG
-- ====================================================
-- Mở rộng dự án gốc. Có thể chạy riêng nếu đã chạy database.sql bản cũ.
-- database.sql ở thư mục gốc đã chứa toàn bộ nội dung này ở cuối file.
USE DE03_LTW;
GO
SET XACT_ABORT ON;
GO

IF COL_LENGTH(N'dbo.Videos', N'Price') IS NULL
    ALTER TABLE dbo.Videos ADD Price DECIMAL(18,2) NOT NULL CONSTRAINT DF_Videos_Price DEFAULT (0);
GO
IF COL_LENGTH(N'dbo.Videos', N'Stock') IS NULL
    ALTER TABLE dbo.Videos ADD Stock INT NOT NULL CONSTRAINT DF_Videos_Stock DEFAULT (20);
GO
-- Chỉ cấp giá cho dữ liệu chưa có giá; chạy lại không khôi phục tồn kho đã bán.
UPDATE dbo.Videos
SET Price = CASE VideoId
    WHEN 'SP001' THEN 120000 WHEN 'SP002' THEN 95000
    WHEN 'SP003' THEN 89000 WHEN 'SP004' THEN 59000
    WHEN 'CN001' THEN 250000 WHEN 'CN002' THEN 180000
    WHEN 'CN003' THEN 320000 WHEN 'CN004' THEN 200000
    WHEN 'HD001' THEN 45000 WHEN 'HD002' THEN 65000
    WHEN 'HD003' THEN 75000 WHEN 'HD004' THEN 55000
    ELSE 100000 END
WHERE Price = 0;
GO
IF NOT EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CHK_Videos_Price')
    ALTER TABLE dbo.Videos ADD CONSTRAINT CHK_Videos_Price CHECK (Price >= 0);
IF NOT EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CHK_Videos_Stock')
    ALTER TABLE dbo.Videos ADD CONSTRAINT CHK_Videos_Stock CHECK (Stock >= 0);
GO

-- Sản phẩm tồn kho thấp và hết hàng để thử giới hạn số lượng trên giao diện.
DECLARE @CategoryId INT = (SELECT TOP (1) CategoryId FROM dbo.Category WHERE Categorycode = 'SANPHAM');
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'SP005')
    INSERT dbo.Videos (VideoId, Title, Views, Description, Active, CategoryId, Price, Stock)
    VALUES ('SP005', N'Hộp đựng thực phẩm EcoMart', 12, N'Sản phẩm mẫu chỉ còn 3 chiếc để thử giới hạn số lượng.', 1, @CategoryId, 79000, 3);
IF NOT EXISTS (SELECT 1 FROM dbo.Videos WHERE VideoId = 'SP006')
    INSERT dbo.Videos (VideoId, Title, Views, Description, Active, CategoryId, Price, Stock)
    VALUES ('SP006', N'Bộ ống hút EcoMart', 5, N'Sản phẩm mẫu đã hết hàng.', 1, @CategoryId, 39000, 0);
GO

IF OBJECT_ID(N'dbo.Orders', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Orders
    (
        OrderId BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT PK_Orders PRIMARY KEY,
        Username NVARCHAR(50) NOT NULL,
        ReceiverName NVARCHAR(100) NOT NULL,
        Phone NVARCHAR(15) NOT NULL,
        Address NVARCHAR(500) NOT NULL,
        Note NVARCHAR(500) NULL,
        PaymentMethod NVARCHAR(10) NOT NULL CONSTRAINT DF_Orders_PaymentMethod DEFAULT N'COD',
        Status NVARCHAR(20) NOT NULL CONSTRAINT DF_Orders_Status DEFAULT N'NEW',
        CreatedAt DATETIME2 NOT NULL CONSTRAINT DF_Orders_CreatedAt DEFAULT SYSDATETIME(),
        TotalAmount DECIMAL(18,2) NOT NULL,
        CheckoutToken NVARCHAR(36) NOT NULL,
        CONSTRAINT FK_Orders_Users FOREIGN KEY (Username) REFERENCES dbo.Users(Username),
        CONSTRAINT UQ_Orders_CheckoutToken UNIQUE (CheckoutToken),
        CONSTRAINT CHK_Orders_PaymentMethod CHECK (PaymentMethod = N'COD'),
        CONSTRAINT CHK_Orders_Status CHECK (Status IN
            (N'NEW', N'CONFIRMED', N'PREPARING', N'SHIPPING', N'DELIVERING', N'DELIVERED', N'CANCELLED', N'RETURNED')),
        CONSTRAINT CHK_Orders_TotalAmount CHECK (TotalAmount > 0)
    );
END;
GO
IF OBJECT_ID(N'dbo.OrderItems', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.OrderItems
    (
        OrderItemId BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT PK_OrderItems PRIMARY KEY,
        OrderId BIGINT NOT NULL,
        VideoId NVARCHAR(50) NOT NULL,
        ProductTitle NVARCHAR(200) NOT NULL,
        UnitPrice DECIMAL(18,2) NOT NULL,
        Quantity INT NOT NULL,
        CONSTRAINT FK_OrderItems_Orders FOREIGN KEY (OrderId) REFERENCES dbo.Orders(OrderId),
        CONSTRAINT FK_OrderItems_Videos FOREIGN KEY (VideoId) REFERENCES dbo.Videos(VideoId),
        CONSTRAINT UQ_OrderItems_Order_Video UNIQUE (OrderId, VideoId),
        CONSTRAINT CHK_OrderItems_UnitPrice CHECK (UnitPrice > 0),
        CONSTRAINT CHK_OrderItems_Quantity CHECK (Quantity BETWEEN 1 AND 99)
    );
END;
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE object_id = OBJECT_ID(N'dbo.Orders') AND name = N'IX_Orders_User_Status_Date')
    CREATE INDEX IX_Orders_User_Status_Date ON dbo.Orders (Username, Status, CreatedAt DESC);
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Users WHERE Username = N'user02')
    INSERT dbo.Users (Username, Password, Phone, Fullname, Email, Admin, Active)
    VALUES (N'user02', N'123456', N'0912345678', N'Người dùng thứ hai', N'user02@ecomart.local', 0, 1);
UPDATE dbo.Users SET Phone = N'0901234567' WHERE Username = N'user01' AND (Phone IS NULL OR Phone = N'');
GO

-- Lịch sử mẫu: user01 có 8 đơn, đủ 8 trạng thái và đủ để demo phân trang 5 đơn/trang.
-- Stock đã khai báo là tồn kho hiện có; các đơn mẫu là lịch sử trước khi demo.
-- Không reset trạng thái/tên/giá chi tiết của đơn đã tồn tại khi chạy lại script.
BEGIN TRANSACTION;
DECLARE @Statuses TABLE (No INT, Status NVARCHAR(20));
INSERT @Statuses VALUES
    (1,N'NEW'), (2,N'CONFIRMED'), (3,N'PREPARING'), (4,N'SHIPPING'),
    (5,N'DELIVERING'), (6,N'DELIVERED'), (7,N'CANCELLED'), (8,N'RETURNED');
DECLARE @No INT = 1;
DECLARE @Status NVARCHAR(20);
DECLARE @Token NVARCHAR(36);
DECLARE @OrderId BIGINT;
DECLARE @Total DECIMAL(18,2) = (SELECT SUM(Price) FROM dbo.Videos WHERE VideoId IN ('SP003','SP004'));
WHILE @No <= 8
BEGIN
    SELECT @Status = Status FROM @Statuses WHERE No = @No;
    SET @Token = N'00000000-0000-0000-0000-00000000000' + CAST(@No AS NVARCHAR(1));
    IF NOT EXISTS (SELECT 1 FROM dbo.Orders WHERE CheckoutToken = @Token)
    BEGIN
        INSERT dbo.Orders (Username, ReceiverName, Phone, Address, Note, PaymentMethod, Status, CreatedAt, TotalAmount, CheckoutToken)
        VALUES (N'user01', N'Người dùng mẫu', N'0901234567', N'01 Võ Văn Ngân, Thủ Đức, TP. Hồ Chí Minh',
                N'Đơn mẫu dùng kiểm tra bộ lọc trạng thái.', N'COD', @Status, DATEADD(DAY, -@No, SYSDATETIME()), @Total, @Token);
        SET @OrderId = CAST(SCOPE_IDENTITY() AS BIGINT);
        INSERT dbo.OrderItems (OrderId, VideoId, ProductTitle, UnitPrice, Quantity)
            SELECT @OrderId, VideoId, Title, Price, 1 FROM dbo.Videos WHERE VideoId IN ('SP003','SP004');
    END;
    SET @No += 1;
END;
-- Đơn thuộc tài khoản khác, dùng kiểm tra việc không xem được đơn của người khác.
IF NOT EXISTS (SELECT 1 FROM dbo.Orders WHERE CheckoutToken = N'00000000-0000-0000-0000-000000000009')
BEGIN
    INSERT dbo.Orders (Username, ReceiverName, Phone, Address, Note, PaymentMethod, Status, TotalAmount, CheckoutToken)
        SELECT N'user02', N'Người dùng thứ hai', N'0912345678', N'02 Võ Văn Ngân, Thủ Đức, TP. Hồ Chí Minh',
               N'Đơn thuộc user02.', N'COD', N'NEW', Price, N'00000000-0000-0000-0000-000000000009'
        FROM dbo.Videos WHERE VideoId = 'SP001';
    SET @OrderId = CAST(SCOPE_IDENTITY() AS BIGINT);
    INSERT dbo.OrderItems (OrderId, VideoId, ProductTitle, UnitPrice, Quantity)
        SELECT @OrderId, VideoId, Title, Price, 1 FROM dbo.Videos WHERE VideoId = 'SP001';
END;
COMMIT TRANSACTION;
GO
SELECT Username, Status, COUNT(*) AS SoDon FROM dbo.Orders GROUP BY Username, Status ORDER BY Username, Status;
GO
