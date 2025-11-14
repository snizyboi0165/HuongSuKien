//Người làm: Nguyễn Cao Việt An
package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import entity.TaiKhoan;
import dao.TaiKhoan_DAO;

public class FrameTaiKhoan extends JInternalFrame implements ActionListener {

	private JLabel lblTitle;
	private JPanel pnlMain;
	private JPanel pnlForm;
	private JPanel pnlPassword;
	private JPanel pnlButton;
	private JTextField txtTenDangNhap;
	private JTextField txtHoTen;
	private JTextField txtChucVu;

	private JPasswordField passCu;
	private JPasswordField passMoi;
	private JPasswordField passXacNhan;
	private JButton btnCapNhatMatKhau;
	private JButton btnThoat;

	private TaiKhoan taiKhoan;
	private TaiKhoan_DAO taiKhoanDAO;

	public FrameTaiKhoan(TaiKhoan account) {
		setTitle("Quản lý tài khoản");
		setClosable(true);
		setMaximizable(true);
		setResizable(true);
		setIconifiable(true);

		this.taiKhoan = account;
		this.taiKhoanDAO = new TaiKhoan_DAO();

		pnlMain = new JPanel();
		pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
		pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));

		lblTitle = new JLabel("Thông tin tài khoản");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		pnlForm = new JPanel(new GridLayout(0, 2, 10, 10));
		pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin cá nhân"));
		pnlForm.setMaximumSize(new Dimension(500, 150));

		txtTenDangNhap = new JTextField();
		txtTenDangNhap.setEditable(false);
		txtHoTen = new JTextField();
		txtHoTen.setEditable(false);
		txtChucVu = new JTextField();
		txtChucVu.setEditable(false);

		pnlForm.add(new JLabel("Tên đăng nhập:"));
		pnlForm.add(txtTenDangNhap);
		pnlForm.add(new JLabel("Họ và tên:"));
		pnlForm.add(txtHoTen);
		pnlForm.add(new JLabel("Chức vụ:"));
		pnlForm.add(txtChucVu);

		pnlPassword = new JPanel(new GridLayout(0, 2, 10, 10));
		pnlPassword.setBorder(BorderFactory.createTitledBorder("Đổi mật khẩu"));
		pnlPassword.setMaximumSize(new Dimension(500, 150));

		passCu = new JPasswordField();
		passMoi = new JPasswordField();
		passXacNhan = new JPasswordField();

		pnlPassword.add(new JLabel("Mật khẩu cũ:"));
		pnlPassword.add(passCu);
		pnlPassword.add(new JLabel("Mật khẩu mới:"));
		pnlPassword.add(passMoi);
		pnlPassword.add(new JLabel("Xác nhận mật khẩu mới:"));
		pnlPassword.add(passXacNhan);

		pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnCapNhatMatKhau = new JButton("Cập nhật mật khẩu");
		btnThoat = new JButton("Thoát");

		pnlButton.add(btnCapNhatMatKhau);
		pnlButton.add(btnThoat);

		pnlMain.add(lblTitle);
		pnlMain.add(Box.createVerticalStrut(20));
		pnlMain.add(pnlForm);
		pnlMain.add(Box.createVerticalStrut(20));
		pnlMain.add(pnlPassword);
		pnlMain.add(Box.createVerticalStrut(20));
		pnlMain.add(pnlButton);

		add(pnlMain);

		btnCapNhatMatKhau.addActionListener(this);
		btnThoat.addActionListener(this);

		loadDuLieu();
	}

	private void loadDuLieu() {
		if (taiKhoan != null) {
			txtTenDangNhap.setText(taiKhoan.getTenDangNhap());
			txtHoTen.setText(taiKhoan.getHoTenNhanVien());

			String chucVu = "Không xác định";
			if (taiKhoan.getMaChucVu() != null) {
				switch (taiKhoan.getMaChucVu()) {
				case "QL":
					chucVu = "Quản lý";
					break;
				case "NV":
					chucVu = "Nhân viên";
					break;
				default:
					chucVu = taiKhoan.getMaChucVu();
				}
			}
			txtChucVu.setText(chucVu);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == btnThoat) {
			this.dispose();
		} else if (src == btnCapNhatMatKhau) {
			xuLyDoiMatKhau();
		}
	}

	private void xuLyDoiMatKhau() {
		String matKhauCu = new String(passCu.getPassword());
		String matKhauMoi = new String(passMoi.getPassword());
		String xacNhan = new String(passXacNhan.getPassword());

		if (matKhauCu.isEmpty() || matKhauMoi.isEmpty() || xacNhan.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường mật khẩu.");
			return;
		}

		if (!matKhauMoi.equals(xacNhan)) {
			JOptionPane.showMessageDialog(this, "Mật khẩu mới và mật khẩu xác nhận không khớp.");
			passMoi.requestFocus();
			return;
		}

		if (matKhauMoi.equals(matKhauCu)) {
			JOptionPane.showMessageDialog(this, "Mật khẩu mới phải khác mật khẩu cũ.");
			passMoi.requestFocus();
			return;
		}

		try {
			TaiKhoan tkKiemTra = taiKhoanDAO.kiemTraDangNhap(taiKhoan.getTenDangNhap(), matKhauCu);
			if (tkKiemTra == null) {
				JOptionPane.showMessageDialog(this, "Mật khẩu cũ không chính xác.");
				passCu.requestFocus();
				return;
			}

			boolean success = taiKhoanDAO.doiMatKhau(taiKhoan.getTenDangNhap(), matKhauMoi);
			if (success) {
				JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
				passCu.setText("");
				passMoi.setText("");
				passXacNhan.setText("");
			} else {
				JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại. Vui lòng thử lại.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
		}
	}
}