//Người làm: Nguyễn Thành Đạt
package entity;

public class ChiTietDatBan {
    private String maDatBan;
    private String maSanPham;
    private int soLuong;
    private double donGia;
    private String ghiChu;

    public ChiTietDatBan() {}

    public ChiTietDatBan(String maDatBan, String maSanPham, int soLuong, double donGia, String ghiChu) {
        this.maDatBan = maDatBan;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
    }

    public String getMaDatBan() {
        return maDatBan;
    }

    public void setMaDatBan(String maDatBan) {
        this.maDatBan = maDatBan;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public double tinhThanhTien() {
        return soLuong * donGia;
    }

    @Override
    public String toString() {
        return "ChiTietDatBan {" +
                "maDatBan='" + maDatBan + '\'' +
                ", maSanPham='" + maSanPham + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", ghiChu='" + ghiChu + '\'' +
                ", thanhTien=" + tinhThanhTien() +
                '}';
    }
}
