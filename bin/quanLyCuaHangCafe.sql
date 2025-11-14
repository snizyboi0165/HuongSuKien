CREATE DATABASE quanly_Cafe;
GO

USE quanly_Cafe;
GO

CREATE TABLE ChucVu (
    MaChucVu VARCHAR(15) PRIMARY KEY,
    TenChucVu NVARCHAR(50) NOT NULL
);
GO

CREATE TABLE Ban (
    MaBan VARCHAR(15) PRIMARY KEY,
    TenBan NVARCHAR(50) NOT NULL,
    TrangThai NVARCHAR(50) NOT NULL
);
GO

CREATE TABLE KhachHang (
    MaKhachHang VARCHAR(15) PRIMARY KEY,
    TenKhachHang NVARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(15) UNIQUE NOT NULL,
    DiemTichLuy INT DEFAULT 0 NOT NULL
);
GO

CREATE TABLE Thue (
    MaThue VARCHAR(15) PRIMARY KEY,
    TenThue NVARCHAR(50) NOT NULL,
    TyLeThue FLOAT NOT NULL,
    MoTa NVARCHAR(255)
);
GO

CREATE TABLE KhuyenMai (
    MaKhuyenMai VARCHAR(15) PRIMARY KEY,
    TenKhuyenMai NVARCHAR(100) NOT NULL,
    PhanTramGiam FLOAT NOT NULL,
    NgayBatDau DATETIME NOT NULL,
    NgayKetThuc DATETIME NOT NULL,
    DieuKienApDung NVARCHAR(255)
);
GO

CREATE TABLE SanPham (
    MaSanPham VARCHAR(15) PRIMARY KEY,
    TenSanPham NVARCHAR(100) NOT NULL,
    GiaBan FLOAT NOT NULL,
    Loai NVARCHAR(50) NOT NULL,
    MoTa NVARCHAR(255),
    TrangThai NVARCHAR(50) NOT NULL
);
GO

CREATE TABLE NhanVien (
    MaNhanVien VARCHAR(15) PRIMARY KEY,
    HoTen NVARCHAR(101) NOT NULL,
    SoDienThoai VARCHAR(15) NOT NULL,
    Luong FLOAT NOT NULL,
    NgayVaoLam DATETIME NOT NULL,
    MaChucVu VARCHAR(15) NOT NULL,
    Email NVARCHAR(100) NOT NULL UNIQUE,
    FOREIGN KEY (MaChucVu) REFERENCES ChucVu(MaChucVu)
);
GO

CREATE TABLE TaiKhoan (
	MaTaiKhoan VARCHAR(50) PRIMARY KEY,
    TenDangNhap VARCHAR(50) UNIQUE NOT NULL, 
    MatKhau VARCHAR(255) NOT NULL, 
    MaNhanVien VARCHAR(15) UNIQUE NOT NULL, 
    MaChucVu VARCHAR(15) NOT NULL, 
    FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien),
    FOREIGN KEY (MaChucVu) REFERENCES ChucVu(MaChucVu)
);
GO

CREATE TABLE DatBan (
    MaDatBan VARCHAR(15) PRIMARY KEY,
    MaBan VARCHAR(15) NOT NULL,
    MaKhachHang VARCHAR(15),
    MaNhanVien VARCHAR(15) NOT NULL,
    SoDienThoai VARCHAR(15) NOT NULL,
    ThoiGianDat DATETIME NOT NULL,
    SoLuongNguoi INT NOT NULL,
    TrangThai NVARCHAR(50) NOT NULL,
    GhiChu NVARCHAR(255),
    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan),
    FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang),
    FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien)
);
GO

CREATE TABLE HoaDon (
    MaHoaDon VARCHAR(15) PRIMARY KEY,
    NgayTao DATETIME NOT NULL,
    TrangThai NVARCHAR(50) NOT NULL,
    MaNhanVien VARCHAR(15) NOT NULL,
    MaBan VARCHAR(15) NOT NULL,
    MaKhachHang VARCHAR(15),
    MaThue VARCHAR(15),
    MaKhuyenMai VARCHAR(15),
    FOREIGN KEY (MaNhanVien) REFERENCES NhanVien(MaNhanVien),
    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan),
    FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang),
    FOREIGN KEY (MaThue) REFERENCES Thue(MaThue),
    FOREIGN KEY (MaKhuyenMai) REFERENCES KhuyenMai(MaKhuyenMai)
);
GO

CREATE TABLE ChiTietHoaDon (
    MaHoaDon VARCHAR(15),
    MaSanPham VARCHAR(15),	
    DonGia FLOAT NOT NULL,
    SoLuong INT NOT NULL,
    PRIMARY KEY (MaHoaDon, MaSanPham),
    FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon),
    FOREIGN KEY (MaSanPham) REFERENCES SanPham(MaSanPham)
);
GO

CREATE TABLE ChiTietDatBan (
    MaDatBan VARCHAR(15),
    MaSanPham VARCHAR(15),
    SoLuong INT NOT NULL,
    DonGia FLOAT NOT NULL,
    GhiChu NVARCHAR(255),
    PRIMARY KEY (MaDatBan, MaSanPham),
    FOREIGN KEY (MaDatBan) REFERENCES DatBan(MaDatBan),
    FOREIGN KEY (MaSanPham) REFERENCES SanPham(MaSanPham)
);
GO

INSERT INTO ChucVu (MaChucVu, TenChucVu) VALUES
('QL', N'Quản lý'),
('NV', N'Nhân viên');
GO

INSERT INTO NhanVien (MaNhanVien, HoTen, SoDienThoai, Luong, NgayVaoLam, MaChucVu, Email) VALUES
('NV001', N'Nguyễn An', '0963107730', 15000000, '2023-03-20', 'NV', 'nv001an@gmail.com'),
('NV002', N'Phan Công', '123456789', 8000000, '2023-01-15', 'NV', 'nv002cong@gmail.com'),
('NV003', N'Trần Bình', '112233445', 7500000, '2023-04-01', 'NV', 'nv003binh@gmail.com'),
('NV004', N'Lê Cẩm', '223344556', 7800000, '2023-05-10', 'NV', 'nv004cam@gmail.com'),
('NV005', N'Hoàng Đức', '334455667', 8200000, '2023-06-15', 'NV', 'nv005duc@gmail.com'),
('NV006', N'Vũ Giang', '445566778', 7600000, '2023-07-20', 'NV', 'nv006giang@gmail.com'),
('NV007', N'Đỗ Huyền', '556677889', 7900000, '2023-08-01', 'NV', 'nv007huyen@gmail.com'),
('NV008', N'Phạm Khang', '667788990', 8100000, '2023-09-10', 'NV', 'nv008khang@gmail.com'),
('NV009', N'Bùi Lan', '778899001', 7700000, '2023-10-15', 'NV', 'nv009lan@gmail.com'),
('NV010', N'Ngô Minh', '889900112', 8300000, '2023-11-20', 'NV', 'nv010minh@gmail.com'),
('NV011', N'Dương Nga', '990011223', 7500000, '2023-12-01', 'NV', 'nv011nga@gmail.com'),
('NV012', N'Lý Phúc', '101122334', 8000000, '2024-01-10', 'NV', 'nv012phuc@gmail.com'),
('NV013', N'Mai Quyên', '121314151', 7800000, '2024-02-15', 'NV', 'nv013quyen@gmail.com'),
('NV014', N'Tạ Sơn', '131415161', 8200000, '2024-03-20', 'NV', 'nv014son@gmail.com'),
('NV015', N'Đinh Thắm', '141516171', 7600000, '2024-04-01', 'NV', 'nv015tham@gmail.com');
GO

INSERT INTO TaiKhoan (MaTaiKhoan, TenDangNhap, MatKhau, MaNhanVien, MaChucVu) VALUES
('TK001', 'nv001an', '123456', 'NV001', 'NV'),
('TK002', 'nv002cong', '123456', 'NV002', 'NV'),
('TK003', 'nv003binh', '123456', 'NV003', 'NV'),
('TK004', 'nv004cam', '123456', 'NV004', 'NV'),
('TK005', 'nv005duc', '123456', 'NV005', 'NV'),
('TK006', 'nv006giang', '123456', 'NV006', 'NV'),
('TK007', 'nv007huyen', '123456', 'NV007', 'NV'),
('TK008', 'nv008khang', '123456', 'NV008', 'NV'),
('TK009', 'nv009lan', '123456', 'NV009', 'NV'),
('TK010', 'nv010minh', '123456', 'NV010', 'NV'),
('TK011', 'nv011nga', '123456', 'NV011', 'NV'),
('TK0Note: Cột `Email` (trong `NhanVien`) và `DieuKienApDung` (trong `KhuyenMai`) bạn để `NULL` (cho phép rỗng). Nếu bạn muốn chúng là `NOT NULL` (bắt buộc), bạn cần sửa lại code Java để luôn truyền giá trị cho chúng.12', 'nv012phuc', '123456', 'NV012', 'NV'),
('TK013', 'nv013quyen', '123456', 'NV013', 'NV'),
('TK014', 'nv014son', '123456', 'NV014', 'NV'),
('TK015', 'nv015tham', '123456', 'NV015', 'NV');
GO

INSERT INTO Ban (MaBan, TenBan, TrangThai) VALUES
('B001', N'Bàn 1', N'Trống'),
('B002', N'Bàn 2', N'Đang dùng'),
('B003', N'Bàn 3', N'Trống'),
('B004', N'Bàn 4', N'Đang chờ nhận'),
('B005', N'Bàn 5', N'Trống'),
('B006', N'Bàn 6', N'Đang dùng'),
('B007', N'Bàn 7', N'Trống'),
('B008', N'Bàn 8', N'Trống'),
('B009', N'Bàn 9', N'Đang dùng'),
('B010', N'Bàn 10', N'Trống'),
('B011', N'Bàn 11', N'Đang chờ nhận'),
('B012', N'Bàn 12', N'Trống'),
('B0013', N'Bàn 13', N'Trống'),
('B014', N'Bàn 14', N'Đang dùng'),
('B015', N'Bàn 15', N'Trống');
GO

INSERT INTO KhachHang (MaKhachHang, TenKhachHang, SoDienThoai, DiemTichLuy) VALUES
('KH001', N'Trần Văn Nam', '0901234501', 150),
('KH002', N'Lê Thị Hoa', '0901234502', 50),
('KH003', N'Nguyễn Minh Tuấn', '0901234503', 200),
('KH004', N'Phạm Thị Lan', '0901234504', 0),
('KH005', N'Hoàng Văn An', '0901234505', 75),
('KH006', N'Vũ Ngọc Bích', '0901234506', 120),
('KH007', N'Đặng Minh Hải', '0901234507', 30),
('KH008', N'Bùi Thu Thảo', '0901234508', 400),
('KH009', N'Hồ Văn Trung', '0901234509', 20),
('KH010', N'Ngô Thị Kim', '0901234510', 0),
('KH011', N'Dương Văn Long', '0901234511', 500),
('KH012', N'Đỗ Thị Huệ', '0901234512', 10),
('KH013', N'Trịnh Văn Sơn', '0901234513', 0),
('KH014', N'Phan Thị Mai', '0901234514', 80),
('KH015', N'Lương Văn Dũng', '0901234515', 210),
('KH016', N'Võ Thị Thu', '0901234516', 0),
('KH017', N'Đinh Văn Nam', '0901234517', 55),
('KH018', N'Chu Thị Lý', '0901234518', 90),
('KH019', N'Nguyễn Anh Quân', '0901234519', 110),
('KH020', N'Hoàng Bảo Châu', '0901234520', 0),
('KH021', N'Hà Văn Long', '0901234521', 10),
('KH022', N'Tô Thị Cúc', '0901234522', 0),
('KH023', N'Nguyễn Bá Kiên', '0901234523', 50),
('KH024', N'Đinh Thùy Linh', '0901234524', 0),
('KH025', N'Lương Văn Việt', '0901234525', 100),
('KH026', N'Cao Thị Hạnh', '0901234526', 20),
('KH027', N'Võ Minh Chiến', '0901234527', 0),
('KH028', N'Phan Ngọc Anh', '0901234528', 70),
('KH029', N'Lê Quang Huy', '0901234529', 150),
('KH030', N'Bùi Thị Hồng', '0901234530', 30),
('KH031', N'Trần Đình Khôi', '0901234531', 0),
('KH032', N'Hoàng Lan Phương', '0901234532', 80),
('KH033', N'Ngô Văn Tuấn', '0901234533', 10),
('KH034', N'Dương Mai Trâm', '0901234534', 0),
('KH035', N'Mai Xuân Bách', '0901234535', 250),
('KH036', N'Lý Cẩm Vân', '0901234536', 50),
('KH037', N'Đào Trung Hiếu', '0901234537', 0),
('KH038', N'Châu Bảo Quyên', '0901234538', 120),
('KH039', N'Tạ Thanh Tùng', '0901234539', 0),
('KH040', N'Đỗ Thị Ngọc', '0901234540', 90);
GO

INSERT INTO SanPham (MaSanPham, TenSanPham, GiaBan, Loai, MoTa, TrangThai) VALUES
('CF001', N'Cà phê Đen Đá', 25000, N'Đồ uống', N'Đậm vị cà phê nguyên chất', N'Còn hàng'),
('CF002', N'Cà phê Sữa Đá', 30000, N'Đồ uống', N'Cà phê hòa quyện cùng sữa đặc', N'Còn hàng'),
('CF003', N'Cà phê Đen Nóng', 25000, N'Đồ uống', N'Pha phin truyền thống', N'Còn hàng'),
('CF004', N'Cà phê Sữa Nóng', 30000, N'Đồ uống', N'Cà phê nóng pha cùng sữa đặc', N'Còn hàng'),
('CF005', N'Bạc Xỉu', 32000, N'Đồ uống', N'Cà phê ít, sữa nhiều, vị béo ngậy', N'Còn hàng'),
('CF006', N'Cappuccino', 45000, N'Đồ uống', N'Phong cách Ý, hương vị đậm đà', N'Còn hàng'),
('CF007', N'Latte', 45000, N'Đồ uống', N'Sữa tươi đánh bọt mịn, vị nhẹ nhàng', N'Còn hàng'),
('CF008', N'Mocha', 48000, N'Đồ uống', N'Kết hợp giữa cà phê và socola', N'Còn hàng'),
('CF009', N'Espresso', 40000, N'Đồ uống', N'Cà phê Ý nguyên chất, vị mạnh', N'Còn hàng'),
('CF010', N'Macchiato Cà Phê', 42000, N'Đồ uống', N'Cà phê đen phủ kem sữa', N'Còn hàng'),

('TEA01', N'Trà Đào Cam Sả', 35000, N'Đồ uống', N'Thanh mát, mùi sả dịu nhẹ', N'Còn hàng'),
('TEA02', N'Trà Tắc Mật Ong', 30000, N'Đồ uống', N'Trà xanh pha tắc và mật ong', N'Còn hàng'),
('TEA03', N'Trà Vải', 35000, N'Đồ uống', N'Trà olong và vải ngọt nhẹ', N'Còn hàng'),
('TEA04', N'Trà Dâu Tây', 38000, N'Đồ uống', N'Trà trái cây tươi mát', N'Còn hàng'),
('TEA05', N'Trà Sen Vàng', 40000, N'Đồ uống', N'Trà xanh ủ với hương sen', N'Còn hàng'),
('TEA06', N'Trà Chanh Sả', 32000, N'Đồ uống', N'Trà chanh pha sả tươi, vị thanh', N'Còn hàng'),
('TEA07', N'Trà Xoài Nhiệt Đới', 40000, N'Đồ uống', N'Trà trái cây vị xoài và cam', N'Còn hàng'),
('TEA08', N'Trà Matcha Kem Cheese', 55000, N'Đồ uống', N'Matcha đậm vị phủ kem phô mai', N'Còn hàng'),
('TEA09', N'Trà Hoa Hồng', 38000, N'Đồ uống', N'Trà thảo mộc hương hoa hồng nhẹ nhàng', N'Còn hàng'),
('TEA10', N'Trà Lài Lạnh', 28000, N'Đồ uống', N'Trà lài truyền thống ướp lạnh', N'Còn hàng'),
('TEA11', N'Hồng Trà Sữa', 42000, N'Đồ uống', N'Hồng trà kết hợp sữa tươi, vị béo nhẹ', N'Còn hàng'),

('BLD01', N'Sinh tố Bơ', 45000, N'Đồ uống', N'Sinh tố bơ tươi nguyên chất', N'Còn hàng'),
('BLD02', N'Sinh tố Dâu', 42000, N'Đồ uống', N'Sinh tố trái cây tươi ngon', N'Còn hàng'),
('BLD03', N'Sinh tố Xoài', 42000, N'Đồ uống', N'Sinh tố xoài chín thơm ngọt', N'Còn hàng'),
('BLD04', N'Cookie Đá Xay', 48000, N'Đồ uống', N'Socola và kem tươi cookie', N'Còn hàng'),
('BLD05', N'Matcha Đá Xay', 48000, N'Đồ uống', N'Trà xanh Nhật Bản, vị béo nhẹ', N'Còn hàng'),
('BLD06', N'Socola Đá Xay', 48000, N'Đồ uống', N'Socola đậm đà cùng kem sữa', N'Còn hàng'),
('MLK01', N'Sữa Tươi Trân Châu Đường Đen', 42000, N'Đồ uống', N'Sữa tươi kết hợp trân châu đen dẻo', N'Còn hàng'),
('MLK02', N'Sữa Tươi Matcha', 40000, N'Đồ uống', N'Sữa tươi pha cùng bột matcha', N'Còn hàng'),
('SODA01', N'Soda Việt Quất', 35000, N'Đồ uống', N'Soda vị trái cây tươi mát', N'Còn hàng'),
('SODA02', N'Soda Dâu Tây', 35000, N'Đồ uống', N'Soda vị dâu tây tươi', N'Còn hàng'),
('SODA03', N'Soda Bạc Hà', 35000, N'Đồ uống', N'Mát lạnh vị bạc hà', N'Còn hàng'),

('SNK01', N'Hướng Dương', 15000, N'Đồ ăn nhẹ', N'Hạt hướng dương rang muối', N'Còn hàng'),
('SNK02', N'Khoai Tây Chiên', 25000, N'Đồ ăn nhẹ', N'Khoai tây chiên giòn', N'Còn hàng'),
('SNK03', N'Bánh Mì Bơ Tỏi', 20000, N'Đồ ăn nhẹ', N'Bánh mì giòn, phủ bơ tỏi thơm', N'Còn hàng'),
('SNK04', N'Bánh Flan', 18000, N'Đồ ăn nhẹ', N'Bánh flan trứng sữa mềm mịn', N'Còn hàng'),
('SNK05', N'Bánh Su Kem', 22000, N'Đồ ăn nhẹ', N'Nhân kem vani ngọt nhẹ', N'Còn hàng'),
('SNK06', N'Bánh Ngọt Phô Mai', 28000, N'Đồ ăn nhẹ', N'Bánh phô mai mềm tan', N'Còn hàng'),
('SNK07', N'Mít Sấy', 20000, N'Đồ ăn nhẹ', N'Trái mít sấy giòn tự nhiên', N'Còn hàng'),
('SNK08', N'Bánh Mì Trứng Muối', 35000, N'Đồ ăn nhẹ', N'Bánh mì kẹp trứng muối béo ngậy', N'Còn hàng'),
('SNK09', N'Rau Câu Dừa', 15000, N'Đồ ăn nhẹ', N'Rau câu vị dừa thơm mát', N'Còn hàng'),
('SNK10', N'Bánh Croissant Bơ', 25000, N'Đồ ăn nhẹ', N'Bánh sừng bò bơ thơm lừng', N'Còn hàng'),
('SNK11', N'Khoai Lang Kén', 28000, N'Đồ ăn nhẹ', N'Khoai lang kén chiên giòn', N'Còn hàng'),
('SNK12', N'Bánh Tiramisu', 48000, N'Đồ ăn nhẹ', N'Bánh Tiramisu kiểu Ý', N'Còn hàng');
GO

INSERT INTO DatBan (MaDatBan, MaBan, MaKhachHang, MaNhanVien, SoDienThoai, ThoiGianDat, SoLuongNguoi, TrangThai, GhiChu) VALUES
('DB001', 'B001', 'KH001', 'NV002', '0901234501', '2025-11-01 19:00:00', 4, N'Đã xác nhận', N'Bàn gần cửa sổ'),
('DB002', 'B003', 'KH002', 'NV003', '0901234502', '2025-11-01 20:00:00', 2, N'Đã xác nhận', NULL),
('DB003', 'B005', NULL, 'NV004', '0988111222', '2025-11-02 18:00:00', 2, N'Đã xác nhận', N'Khách vãng lai'),
('DB004', 'B007', 'KH003', 'NV005', '0901234503', '2025-11-02 18:30:00', 5, N'Đã xác nhận', NULL),
('DB005', 'B008', 'KH004', 'NV002', '0901234504', '2025-11-03 11:00:00', 2, N'Đã hủy', N'Khách báo bận'),
('DB006', 'B010', 'KH005', 'NV003', '0901234505', '2025-11-03 12:00:00', 3, N'Đã xác nhận', NULL),
('DB007', 'B012', 'KH006', 'NV006', '0901234506', '2025-11-04 09:00:00', 2, N'Đã nhận bàn', NULL),
('DB008', 'B013', 'KH007', 'NV007', '0901234507', '2025-11-04 19:00:00', 6, N'Đã xác nhận', N'Trang trí sinh nhật'),
('DB009', 'B015', 'KH008', 'NV008', '0901234508', '2025-11-05 19:30:00', 2, N'Đã xác nhận', NULL),
('DB010', 'B001', 'KH009', 'NV009', '0901234509', '2025-11-05 20:00:00', 2, N'Đã nhận bàn', NULL),
('DB011', 'B003', 'KH010', 'NV010', '0901234510', '2025-11-06 17:00:00', 1, N'Đã xác nhận', NULL),
('DB012', 'B005', 'KH011', 'NV011', '0901234511', '2025-11-06 18:00:00', 4, N'Đã xác nhận', N'Cần ghế trẻ em'),
('DB013', 'B007', 'KH012', 'NV012', '0901234512', '2025-11-07 10:00:00', 2, N'Đã hủy', NULL),
('DB014', 'B008', NULL, 'NV013', '0977333444', '2025-11-07 14:00:00', 3, N'Đã xác nhận', NULL),
('DB015', 'B010', 'KH013', 'NV014', '0901234513', '2025-11-08 19:00:00', 2, N'Đã nhận bàn', NULL),
('DB016', 'B012', 'KH014', 'NV015', '0901234514', '2025-11-08 19:15:00', 2, N'Đã xác nhận', NULL),
('DB017', 'B013', 'KH015', 'NV002', '0901234515', '2025-11-09 20:00:00', 8, N'Đã xác nhận', NULL),
('DB018', 'B015', 'KH016', 'NV003', '0901234516', '2025-11-09 20:00:00', 2, N'Đã xác nhận', NULL),
('DB019', 'B001', 'KH017', 'NV004', '0901234517', '2025-11-10 18:30:00', 2, N'Đã hủy', N'Khách đổi ý'),
('DB020', 'B003', 'KH018', 'NV005', '0901234518', '2025-11-10 19:00:00', 4, N'Đã xác nhận', NULL);
GO

INSERT INTO ChiTietDatBan (MaDatBan, MaSanPham, SoLuong, DonGia, GhiChu) VALUES
('DB001', 'CF002', 2, 30000, N'Ít đá'),
('DB001', 'TEA01', 1, 35000, NULL),
('DB002', 'CF001', 2, 25000, NULL),
('DB003', 'SNK01', 1, 15000, NULL),
('DB004', 'CF001', 2, 25000, N'1 ít đường, 1 không đường'),
('DB004', 'CF002', 1, 30000, NULL),
('DB004', 'SNK01', 2, 15000, NULL),
('DB006', 'TEA01', 1, 35000, N'Nóng'),
('DB007', 'CF001', 1, 25000, NULL),
('DB007', 'SNK01', 1, 15000, NULL),
('DB008', 'TEA01', 4, 35000, NULL),
('DB008', 'CF002', 2, 30000, N'Nhiều sữa'),
('DB008', 'SNK01', 3, 15000, NULL),
('DB009', 'CF002', 2, 30000, NULL),
('DB011', 'CF001', 1, 25000, N'Mang đi'),
('DB012', 'TEA01', 2, 35000, NULL),
('DB012', 'SNK01', 1, 15000, NULL),
('DB014', 'CF002', 1, 30000, NULL),
('DB017', 'TEA01', 2, 35000, N'Nhiều sả'),
('DB018', 'CF002', 1, 30000, NULL);
GO

INSERT INTO HoaDon (MaHoaDon, NgayTao, TrangThai, MaNhanVien, MaBan, MaKhachHang, MaThue, MaKhuyenMai) VALUES 
('HD001', '2025-10-30 20:00:00', N'Đã thanh toán', 'NV002', 'B002', 'KH001', NULL, NULL);
GO

INSERT INTO ChiTietHoaDon (MaHoaDon, MaSanPham, DonGia, SoLuong) VALUES
('HD001', 'CF001', 25000, 2),
('HD001', 'SNK01', 15000, 1);
GO

INSERT INTO HoaDon (MaHoaDon, NgayTao, TrangThai, MaNhanVien, MaBan, MaKhachHang, MaThue, MaKhuyenMai) VALUES 
('HD002', '2025-10-31 10:30:00', N'Đã thanh toán', 'NV003', 'B006', NULL, NULL, NULL);
GO

INSERT INTO ChiTietHoaDon (MaHoaDon, MaSanPham, DonGia, SoLuong) VALUES
('HD002', 'TEA01', 35000, 1);
GO

INSERT INTO Thue (MaThue, TenThue, TyLeThue, MoTa) VALUES
('VAT10', N'Thuế Giá Trị Gia Tăng', 0.10, N'Thuế tiêu thụ bắt buộc 10%');
GO

INSERT INTO KhuyenMai (MaKhuyenMai, TenKhuyenMai, PhanTramGiam, NgayBatDau, NgayKetThuc, DieuKienApDung) VALUES
('KM100DTL', N'Khuyến mãi 100 điểm tích lũy', 0.10, '2025-01-01 00:00:00', '2025-12-31 23:59:59', N'Khách hàng trên 100 điểm tích lũy'),
('KM200DTL', N'Khuyến mãi 200 điểm tích lũy', 0.20, '2025-01-01 00:00:00', '2025-12-31 23:59:59', N'Khách hàng trên 200 điểm tích lũy');
GO