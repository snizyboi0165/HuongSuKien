//Người làm: Nguyễn Cao Việt An
package entity;

import java.util.Objects;

public class Ban {
	private String maBan, tenBan, trangThai;

	public Ban(String maBan, String tenBan, String trangThai) {
		super();
		this.maBan = maBan;
		this.tenBan = tenBan;
		this.trangThai = trangThai;
	}

	public Ban() {
	}

	public Ban(String maBan) {
		this.maBan = maBan;
	}
	
	public void capNhatTrangThai(String trangThaiMoi) {
		this.setTrangThai(trangThaiMoi);
	}

	public String getMaBan() {
		return maBan;
	}

	public void setMaBan(String maBan) {
		this.maBan = maBan;
	}

	public String getTenBan() {
		return tenBan;
	}

	public void setTenBan(String tenBan) {
		this.tenBan = tenBan;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maBan);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ban other = (Ban) obj;
		return Objects.equals(maBan, other.maBan);
	}

	@Override
	public String toString() {
		return "Ban [maBan=" + maBan + ", tenBan=" + tenBan + ", trangThai=" + trangThai + "]";
	}

}