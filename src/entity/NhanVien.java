//Người làm: Nguyễn Cao Việt An
package entity;

import java.util.Objects;
import java.time.LocalDateTime;

public class NhanVien {
	private String maNhanVien;
	private String hoTen;
	private double luong;
	private String soDienThoai;
	private LocalDateTime ngayVaoLam;
	private ChucVu chucVu;

	public NhanVien(String maNhanVien, String hoTen, double luong, String soDienThoai, LocalDateTime ngayVaoLam,
			ChucVu chucVu) {
		super();
		this.maNhanVien = maNhanVien;
		this.hoTen = hoTen;
		this.luong = luong;
		this.soDienThoai = soDienThoai;
		this.ngayVaoLam = ngayVaoLam;
		this.chucVu = chucVu;
	}

	public NhanVien() {
	}

	public NhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public double getLuong() {
		return luong;
	}

	public void setLuong(double luong) {
		this.luong = luong;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public LocalDateTime getNgayVaoLam() {
		return ngayVaoLam;
	}

	public void setNgayVaoLam(LocalDateTime ngayVaoLam) {
		this.ngayVaoLam = ngayVaoLam;
	}

	public ChucVu getChucVu() {
		return chucVu;
	}

	public void setChucVu(ChucVu chucVu) {
		this.chucVu = chucVu;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNhanVien);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNhanVien, other.maNhanVien);
	}

	@Override
	public String toString() {
		return "NhanVien [maNhanVien=" + maNhanVien + ", hoTen=" + hoTen + ", luong=" + luong + ", soDienThoai="
				+ soDienThoai + ", ngayVaoLam=" + ngayVaoLam + ", chucVu=" + chucVu + "]";
	}

	
}