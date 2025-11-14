//Người làm: Nguyễn Thành Đạt
package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class KhuyenMai {
	private String maKhuyenMai, tenKhuyenMai, dieuKienApDung;
	private double phanTramGiam;
	private LocalDateTime ngayBatDau, ngayKetThuc;

	public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, String dieuKienApDung, double phanTramGiam,
			LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
		super();
		this.maKhuyenMai = maKhuyenMai;
		this.tenKhuyenMai = tenKhuyenMai;
		this.dieuKienApDung = dieuKienApDung;
		this.phanTramGiam = phanTramGiam;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
	}

	public KhuyenMai() {
	}

	public KhuyenMai(String maKhuyenMai) {
		this.maKhuyenMai = maKhuyenMai;
	}

	public double tinhGiaSauKhuyenMai(double tongTien) {
		if (kiemTraHieuLuc()) {
			return tongTien * (1 - this.phanTramGiam);
		}
		return tongTien;
	}
	
	public boolean kiemTraHieuLuc() {
		LocalDateTime now = LocalDateTime.now();
		return now.isAfter(ngayBatDau) && now.isBefore(ngayKetThuc);
	}

	public String getMaKhuyenMai() {
		return maKhuyenMai;
	}

	public void setMaKhuyenMai(String maKhuyenMai) {
		this.maKhuyenMai = maKhuyenMai;
	}

	public String getTenKhuyenMai() {
		return tenKhuyenMai;
	}

	public void setTenKhuyenMai(String tenKhuyenMai) {
		this.tenKhuyenMai = tenKhuyenMai;
	}

	public String getDieuKienApDung() {
		return dieuKienApDung;
	}

	public void setDieuKienApDung(String dieuKienApDung) {
		this.dieuKienApDung = dieuKienApDung;
	}

	public double getPhanTramGiam() {
		return phanTramGiam;
	}

	public void setPhanTramGiam(double phanTramGiam) {
		this.phanTramGiam = phanTramGiam;
	}

	public LocalDateTime getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(LocalDateTime ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public LocalDateTime getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maKhuyenMai);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhuyenMai other = (KhuyenMai) obj;
		return Objects.equals(maKhuyenMai, other.maKhuyenMai);
	}

	@Override
	public String toString() {
		return "KhuyenMai [maKhuyenMai=" + maKhuyenMai + ", tenKhuyenMai=" + tenKhuyenMai + ", dieuKienApDung="
				+ dieuKienApDung + ", phanTramGiam=" + phanTramGiam + ", ngayBatDau=" + ngayBatDau + ", ngayKetThuc="
				+ ngayKetThuc + "]";
	}
}