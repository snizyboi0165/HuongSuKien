//Người làm: Nguyễn Cao Việt An
package entity;

import java.util.Objects;

public class KhachHang {
	private String maKhachHang, tenKhachHang;
	private int diemTichLuy;
	private String soDienThoai;

	public KhachHang(String maKhachHang, String tenKhachHang, int diemTichLuy, String soDienThoai) {
		super();
		this.maKhachHang = maKhachHang;
		this.tenKhachHang = tenKhachHang;
		this.diemTichLuy = diemTichLuy;
		this.soDienThoai = soDienThoai;
	}

	public KhachHang() {
	}

	public KhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}

	public void congDiemTichLuy(int soDiem) {
		if(soDiem > 0) {
			this.diemTichLuy += soDiem;
		}
	}
	
	public String getMaKhachHang() {
		return maKhachHang;
	}

	public void setMaKhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}

	public String getTenKhachHang() {
		return tenKhachHang;
	}

	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}

	public int getDiemTichLuy() {
		return diemTichLuy;
	}

	public void setDiemTichLuy(int diemTichLuy) {
		this.diemTichLuy = diemTichLuy;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maKhachHang);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(maKhachHang, other.maKhachHang);
	}

	@Override
	public String toString() {
		return "KhachHang [maKhachHang=" + maKhachHang + ", tenKhachHang=" + tenKhachHang + ", diemTichLuy="
				+ diemTichLuy + ", soDienThoai=" + soDienThoai + "]";
	}
}