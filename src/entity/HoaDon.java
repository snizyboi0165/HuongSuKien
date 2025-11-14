//Người làm: Nguyễn Cao Việt An
package entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

public class HoaDon {
	private String maHoaDon;
	private LocalDateTime ngayTao;
	private String trangThai;
	private NhanVien nhanVien;
	private Ban ban;
	private KhachHang khachHang;
	private Thue thue;
	private KhuyenMai khuyenMai;

	private List<ChiTietHoaDon> danhSachChiTiet;

	public HoaDon(String maHoaDon, LocalDateTime ngayTao, String trangThai, NhanVien nhanVien, Ban ban,
			KhachHang khachHang, Thue thue, KhuyenMai khuyenMai) {
		super();
		this.maHoaDon = maHoaDon;
		this.ngayTao = ngayTao;
		this.trangThai = trangThai;
		this.nhanVien = nhanVien;
		this.ban = ban;
		this.khachHang = khachHang;
		this.thue = thue;
		this.khuyenMai = khuyenMai;
		this.danhSachChiTiet = new ArrayList<>();
	}

	public HoaDon() {
		this.danhSachChiTiet = new ArrayList<>();
	}

	public HoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
		this.danhSachChiTiet = new ArrayList<>();
	}

	public void themChiTiet(ChiTietHoaDon ct) {
		for (ChiTietHoaDon chiTiet : danhSachChiTiet) {
			if(chiTiet.getSanPham().equals(ct.getSanPham())) {
				chiTiet.setSoLuong(chiTiet.getSoLuong() + ct.getSoLuong());
				return;
			}
		}
		this.danhSachChiTiet.add(ct);
		ct.setHoaDon(this);
	}

	public void apDungKhuyenMai(KhuyenMai km) {
		this.setKhuyenMai(km);
	}

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public LocalDateTime getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(LocalDateTime ngayTao) {
		this.ngayTao = ngayTao;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
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

	public Thue getThue() {
		return thue;
	}

	public void setThue(Thue thue) {
		this.thue = thue;
	}

	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}

	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}

	public List<ChiTietHoaDon> getDanhSachChiTiet() {
		return danhSachChiTiet;
	}

	public void setDanhSachChiTiet(List<ChiTietHoaDon> danhSachChiTiet) {
		this.danhSachChiTiet = danhSachChiTiet;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maHoaDon);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoaDon other = (HoaDon) obj;
		return Objects.equals(maHoaDon, other.maHoaDon);
	}

	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", ngayTao=" + ngayTao + ", trangThai=" + trangThai + ", nhanVien="
				+ nhanVien + ", ban=" + ban + ", khachHang=" + khachHang + ", thue=" + thue + ", khuyenMai=" + khuyenMai
				+ ", danhSachChiTiet=" + danhSachChiTiet + "]";
	}


}