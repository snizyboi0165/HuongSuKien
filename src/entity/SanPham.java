//Người làm: Ngô Văn Thành
package entity;

import java.util.Objects;

public class SanPham {
	private String maSanPham;
    private String tenSanPham;
    private double giaBan;
    private String loai;
    private String moTa;
    private String trangThai;

	public SanPham(String maSanPham, String tenSanPham, double giaBan, String loai, String moTa, String trangThai) {
		super();
		this.maSanPham = maSanPham;
		this.tenSanPham = tenSanPham;
		this.giaBan = giaBan;
		this.loai = loai;
		this.moTa = moTa;
		this.trangThai = trangThai;
	}
	
	public SanPham() {
	}
	
	public SanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}
	
	public double getGiaBan() {
		return giaBan;
	}
	public void setGiaBan(double giaBan) {
		this.giaBan = giaBan;
	}
	public String getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}
	public String getTenSanPham() {
		return tenSanPham;
	}
	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public String getLoai() {
		return loai;
	}
	public void setLoai(String loai) {
		this.loai = loai;
	}
	
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(maSanPham);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SanPham other = (SanPham) obj;
		return Objects.equals(maSanPham, other.maSanPham);
	}
	
	@Override
	public String toString() {
		return "SanPham [maSanPham=" + maSanPham + ", tenSanPham=" + tenSanPham + ", moTa=" + moTa + ", loai=" + loai
				+ ", trangThai=" + trangThai + "]";
	}
}