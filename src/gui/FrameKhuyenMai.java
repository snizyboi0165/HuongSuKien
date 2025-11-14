//Người làm: Nguyễn Thành Đạt
package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.KhuyenMai_DAO;

import entity.KhuyenMai;

public class FrameKhuyenMai extends JInternalFrame implements ActionListener {

	private JPanel pnlKhuyenMai;
	private JComboBox<String> cboTrangThai;
	private KhuyenMai selectedKM;
	private KhuyenMai_DAO khuyenMaiDAO;
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnCapNhat;
	private JPanel pnlBoLoc;
	private JButton btnLamMoi;
	private JLabel lblTitle;
	private JPanel pnlNorth;
	private JPanel pnlSelectedCard;
	private JPanel pnlLeft;

	public FrameKhuyenMai() {
		setTitle("Quản lý khuyến mãi");
		setClosable(true);
		setMaximizable(true);
		setResizable(true);
		setIconifiable(true);

		setSize(1200, 700);
		setLocation(10, 10);
		setLayout(new BorderLayout());

		khuyenMaiDAO = new KhuyenMai_DAO();

		pnlLeft = new JPanel(new GridLayout(10, 1, 5, 5));
		pnlLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlLeft.setBackground(new Color(224, 224, 224));

		btnThem = createStyledButton("Thêm khuyến mãi (F4)");
		btnXoa = createStyledButton("Xóa khuyến mãi (F5)");
		btnCapNhat = createStyledButton("Sửa thông tin (F6)");

		pnlLeft.add(btnThem);
		pnlLeft.add(btnXoa);
		pnlLeft.add(btnCapNhat);

		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnCapNhat.addActionListener(this);
		add(pnlLeft, BorderLayout.WEST);

		pnlBoLoc = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlBoLoc.setBackground(Color.white);
		pnlBoLoc.add(new JLabel("Loại khuyến mãi:"));
		cboTrangThai = new JComboBox<>(new String[] { "Tất cả", "Đang áp dụng", "Hết hạn", "Chưa áp dụng" });
		pnlBoLoc.add(cboTrangThai);
		pnlBoLoc.add(btnLamMoi = new JButton("Làm mới"));
		btnLamMoi.addActionListener(this);

		lblTitle = new JLabel("DANH SÁCH KHUYẾN MÃI", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.ITALIC, 24));
		lblTitle.setOpaque(true);
		lblTitle.setBackground(new Color(139, 69, 19));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setPreferredSize(new Dimension(0, 50));

		pnlNorth = new JPanel(new BorderLayout());
		pnlNorth.add(lblTitle, BorderLayout.NORTH);
		pnlNorth.add(pnlBoLoc, BorderLayout.SOUTH);
		pnlNorth.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		add(pnlNorth, BorderLayout.NORTH);

		pnlKhuyenMai = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
		pnlKhuyenMai.setBorder(new EmptyBorder(15, 15, 15, 15));
		pnlKhuyenMai.setBackground(Color.white);
		add(new JScrollPane(pnlKhuyenMai), BorderLayout.CENTER);

		loadVouchers("Tất cả");
		
		cboTrangThai.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == btnLamMoi) {
			cboTrangThai.setSelectedIndex(0);
			loadVouchers("Tất cả");
		} else if (src == btnThem) {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            
            try {
                FrameThemKhuyenMai themKMDialog = new FrameThemKhuyenMai(parentFrame); 
                themKMDialog.setVisible(true);
                loadVouchers("Tất cả");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi: Không thể mở cửa sổ Thêm Khuyến Mãi. Chi tiết: " + ex.getMessage(), 
                    "Lỗi Mở Cửa Sổ", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
		} else if (src == btnXoa) {
			xoaKhuyenMai();
		} else if (src == btnCapNhat) {
			if (selectedKM == null) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần sửa!");
				return;
			}
			hienThiNhapLieu("Cập nhật Khuyến Mãi", selectedKM);
		} else if (src == cboTrangThai) {
			String selected = (String) cboTrangThai.getSelectedItem();
			loadVouchers(selected);
		}
	}

	private JButton createStyledButton(String text) {
		JButton btn = new JButton(text);
		btn.setFocusPainted(false);
		btn.setForeground(Color.white);
		btn.setBackground(new Color(139, 69, 19));
		return btn;
	}

	private void loadVouchers(String trangThaiChon) {
		pnlSelectedCard = null;
		selectedKM = null;

		pnlKhuyenMai.removeAll();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime now = LocalDateTime.now();

		try {
			List<KhuyenMai> ds = khuyenMaiDAO.getAll();

			for (KhuyenMai km : ds) {
				String trangThaiThucTe = "Chưa áp dụng";
				if (now.isAfter(km.getNgayKetThuc())) {
					trangThaiThucTe = "Hết hạn";
				} else if (now.isAfter(km.getNgayBatDau())) {
					trangThaiThucTe = "Đang áp dụng";
				}

				boolean hienThi = false;
				if (trangThaiChon.equals("Tất cả")) {
					hienThi = true;
				} else if (trangThaiChon.equals(trangThaiThucTe)) {
					hienThi = true;
				}

				if (hienThi) {
					JPanel card = createVoucherCard(km, dtf, trangThaiThucTe);
					pnlKhuyenMai.add(card);
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu khuyến mãi: " + e.getMessage(), "Lỗi CSDL",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		pnlKhuyenMai.revalidate();
		pnlKhuyenMai.repaint();
	}

	private JPanel createVoucherCard(KhuyenMai km, DateTimeFormatter dtf, String trangThai) {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(Color.white);
		card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		card.setPreferredSize(new Dimension(200, 250));
		card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JLabel lblTen = new JLabel(km.getTenKhuyenMai() + " (" + (km.getPhanTramGiam() * 100) + "%)",
				SwingConstants.CENTER);
		lblTen.setFont(new Font("Arial", Font.BOLD, 14));
		lblTen.setBorder(new EmptyBorder(5, 5, 5, 5));
		card.add(lblTen, BorderLayout.NORTH);

		JLabel lblAnh = new JLabel("<html><b>Mã: " + km.getMaKhuyenMai() + "</b></html>", SwingConstants.CENTER);
		card.add(lblAnh, BorderLayout.CENTER);

		JPanel info = new JPanel(new GridLayout(4, 1, 2, 2));
		info.setBorder(new EmptyBorder(5, 10, 5, 10));
		info.add(new JLabel(
				"<html>BĐ: " + (km.getNgayBatDau() != null ? km.getNgayBatDau().format(dtf) : "N/A") + "</html>"));
		info.add(new JLabel(
				"<html>KT: " + (km.getNgayKetThuc() != null ? km.getNgayKetThuc().format(dtf) : "N/A") + "</html>"));
		info.add(new JLabel("Điều kiện: " + km.getDieuKienApDung()));

		JLabel lblTrangThai = new JLabel("Trạng thái: " + trangThai);
		lblTrangThai.setForeground(trangThai.equals("Hết hạn") ? Color.RED
				: (trangThai.equals("Đang áp dụng") ? new Color(0, 128, 0) : Color.BLUE));
		info.add(lblTrangThai);

		card.add(info, BorderLayout.SOUTH);

		card.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					selectCard(card, km);
				}
			}
		});

		return card;
	}

	private void selectCard(JPanel card, KhuyenMai km) {
		if (pnlSelectedCard == card) {
			pnlSelectedCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			pnlSelectedCard = null;
			selectedKM = null;
			return;
		}

		if (pnlSelectedCard != null) {
			pnlSelectedCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}

		pnlSelectedCard = card;
		selectedKM = km;
		pnlSelectedCard.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
	}

	private void hienThiNhapLieu(String title, KhuyenMai km) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		JTextField txtMa = new JTextField(km != null ? km.getMaKhuyenMai() : "KM" + System.currentTimeMillis() % 10000);
		txtMa.setEditable(km == null);

		JTextField txtTen = new JTextField(km != null ? km.getTenKhuyenMai() : "");
		JTextField txtPhanTram = new JTextField(km != null ? String.valueOf(km.getPhanTramGiam() * 100) : "0");
		JTextField txtBatDau = new JTextField(
				km != null ? km.getNgayBatDau().format(dtf) : dtf.format(LocalDateTime.now()));
		JTextField txtKetThuc = new JTextField(
				km != null ? km.getNgayKetThuc().format(dtf) : dtf.format(LocalDateTime.now().plusDays(7)));
		JTextField txtDieuKien = new JTextField(km != null ? km.getDieuKienApDung() : "Không có");

		JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
		panel.add(new JLabel("Mã Khuyến Mãi:"));
		panel.add(txtMa);
		panel.add(new JLabel("Tên khuyến mãi:"));
		panel.add(txtTen);
		panel.add(new JLabel("Phần trăm giảm (%):"));
		panel.add(txtPhanTram);
		panel.add(new JLabel("Ngày Bắt Đầu (dd/MM/yyyy HH:mm):"));
		panel.add(txtBatDau);
		panel.add(new JLabel("Ngày Kết Thúc (dd/MM/yyyy HH:mm):"));
		panel.add(txtKetThuc);
		panel.add(new JLabel("Điều kiện:"));
		panel.add(txtDieuKien);

		int result = JOptionPane.showConfirmDialog(this, panel, title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			try {
				String ma = txtMa.getText().trim();
				String ten = txtTen.getText().trim();
				String dieuKien = txtDieuKien.getText().trim();

				if (ma.isEmpty() || ten.isEmpty()) {
					throw new IllegalArgumentException("Mã và Tên không được để trống.");
				}

				double phanTram = Double.parseDouble(txtPhanTram.getText().trim()) / 100.0;
				if (phanTram < 0 || phanTram > 1) {
					throw new NumberFormatException("Phần trăm giảm phải từ 0 đến 100.");
				}

				LocalDateTime ngayBD = LocalDateTime.parse(txtBatDau.getText().trim(), dtf);
				LocalDateTime ngayKT = LocalDateTime.parse(txtKetThuc.getText().trim(), dtf);

				if (ngayKT.isBefore(ngayBD)) {
					throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu.");
				}

				KhuyenMai kmMoi = new KhuyenMai(ma, ten, dieuKien, phanTram, ngayBD, ngayKT);

				boolean success = false;
				if (km == null) {
					success = khuyenMaiDAO.themKhuyenMai(kmMoi);
				} else {
					success = khuyenMaiDAO.capNhatKhuyenMai(kmMoi);
				}

				if (success) {
					JOptionPane.showMessageDialog(this, title + " thành công!");
					loadVouchers("Tất cả");
				} else {
					JOptionPane.showMessageDialog(this, title + " thất bại! (Mã có thể đã tồn tại)", "Lỗi CSDL",
							JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			} catch (DateTimeParseException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày giờ. Vui lòng dùng: dd/MM/yyyy HH:mm", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException | SQLException ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void xoaKhuyenMai() {
		if (selectedKM == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi muốn xoá!", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn xoá khuyến mãi: " + selectedKM.getTenKhuyenMai() + "?", "Xác nhận xoá",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				boolean success = khuyenMaiDAO.xoaKhuyenMai(selectedKM.getMaKhuyenMai());
				if (success) {
					JOptionPane.showMessageDialog(this, "Đã xoá khuyến mãi: " + selectedKM.getTenKhuyenMai());
					loadVouchers("Tất cả");
				} else {
					JOptionPane.showMessageDialog(this, "Xoá thất bại! (Có thể do KM đang được tham chiếu)", "Lỗi CSDL",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, "Lỗi CSDL khi xóa: " + e.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}