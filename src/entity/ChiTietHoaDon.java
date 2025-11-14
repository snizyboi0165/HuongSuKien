//Người làm: Nguyễn Thành Đạt
package entity;

import java.util.Objects;

public class ChiTietHoaDon {
	private HoaDon hoaDon;
	private SanPham sanPham;
	private double donGia;
	private int soLuong;

	public ChiTietHoaDon(HoaDon hoaDon, SanPham sanPham, double donGia, int soLuong) {
		super();
		this.hoaDon = hoaDon;
		this.sanPham = sanPham;
		this.donGia = donGia;
		this.soLuong = soLuong;
	}

	public ChiTietHoaDon() {
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public SanPham getSanPham() {
		return sanPham;
	}

	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public double getThanhTien() {
		return this.donGia * this.soLuong;
	}

	@Override
	public int hashCode() {
		return Objects.hash(hoaDon, sanPham);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChiTietHoaDon other = (ChiTietHoaDon) obj;
		return Objects.equals(hoaDon, other.hoaDon) && Objects.equals(sanPham, other.sanPham);
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon [hoaDon=" + (hoaDon != null ? hoaDon.getMaHoaDon() : "null") + ", sanPham="
				+ (sanPham != null ? sanPham.getMaSanPham() : "null") + ", donGia=" + donGia + ", soLuong=" + soLuong
				+ "]";
	}
}