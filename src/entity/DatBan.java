//Người làm: Nguyễn Cao Việt An
package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class DatBan {
	private String maDatBan, trangThai, ghiChu;
	private String soDienThoai;
	private int soLuongNguoi;
	private LocalDateTime thoiGianDat;
	private Ban ban;
	private KhachHang khachHang;
	private NhanVien nhanVien;

	public DatBan(String maDatBan, String trangThai, String ghiChu, String soDienThoai, int soLuongNguoi,
			LocalDateTime thoiGianDat, Ban ban, KhachHang khachHang, NhanVien nhanVien) {
		super();
		this.maDatBan = maDatBan;
		this.trangThai = trangThai;
		this.ghiChu = ghiChu;
		this.soDienThoai = soDienThoai;
		this.soLuongNguoi = soLuongNguoi;
		this.thoiGianDat = thoiGianDat;
		this.ban = ban;
		this.khachHang = khachHang;
		this.nhanVien = nhanVien;
	}

	public DatBan() {
	}
	
	public DatBan(String maDatBan) {
		this.maDatBan = maDatBan;
	}

	public void xacNhanDatBan() {
		this.setTrangThai("Đã xác nhận");
	}
	
	public void huyDatBan() {
		this.setTrangThai("Đã hủy");
	}

	public String getMaDatBan() {
		return maDatBan;
	}

	public void setMaDatBan(String maDatBan) {
		this.maDatBan = maDatBan;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public int getSoLuongNguoi() {
		return soLuongNguoi;
	}

	public void setSoLuongNguoi(int soLuongNguoi) {
		this.soLuongNguoi = soLuongNguoi;
	}

	public LocalDateTime getThoiGianDat() {
		return thoiGianDat;
	}

	public void setThoiGianDat(LocalDateTime thoiGianDat) {
		this.thoiGianDat = thoiGianDat;
	}

	public Ban getBan() {
		return ban;
	}

	public void setBan(Ban ban) {
		this.ban = ban;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maDatBan);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatBan other = (DatBan) obj;
		return Objects.equals(maDatBan, other.maDatBan);
	}

	@Override
	public String toString() {
		return "DatBan [maDatBan=" + maDatBan + ", trangThai=" + trangThai + ", ghiChu=" + ghiChu + ", soDienThoai="
				+ soDienThoai + ", soLuongNguoi=" + soLuongNguoi + ", thoiGianDat=" + thoiGianDat + ", maBan=" + ban
				+ ", maKhachHang=" + khachHang + ", nhanVien=" + nhanVien + "]";
	}
}