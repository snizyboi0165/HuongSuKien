//Người làm: Nguyễn Thành Đạt
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Ban_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.DatBan_DAO;
import dao.ChiTietDatBan_DAO;
import dao.ChiTietHoaDon_DAO;

import entity.Ban;
import entity.KhachHang;
import entity.DatBan;
import entity.HoaDon;
import entity.TaiKhoan;

public class FrameDatBan extends JInternalFrame implements ActionListener {

	private JPanel pnlTables;
	private JComboBox<String> cboTrangThai;
	private JTextField txtBanSo;

	private Ban_DAO ban_DAO;
	private HoaDon_DAO hoaDonDAO;
	private KhachHang_DAO khachHangDAO;
	private DatBan_DAO datBanDAO;
	private ChiTietDatBan_DAO ctDatBanDAO;
	private ChiTietHoaDon_DAO ctHoaDonDAO;
	private TaiKhoan taiKhoanDangNhap;

	private class BanUI {
		Ban ban;
		JPanel panel;
		JLabel infoLabel;
		JLabel iconLabel;

		public BanUI(Ban ban, JPanel panel, JLabel infoLabel, JLabel iconLabel) {
			this.ban = ban;
			this.panel = panel;
			this.infoLabel = infoLabel;
			this.iconLabel = iconLabel;
		}
	}

	private List<BanUI> danhSachBan = new ArrayList<>();
	private BanUI selectedBanUI = null;
	private JPanel pnlLeft;
	private JButton btnDatBanNgay;
	private JButton btnDatBanCho;
	private JButton btnNhanBanCho;
	private JButton btnHuyBanCho;
	private JPanel pnlFilter;
	private JButton btnTim;
	private JButton btnLamMoi;
	private JLabel lblTitle;
	private InputMap inputMap;
	private ActionMap actionMap;

	public FrameDatBan(TaiKhoan account) {
		this.taiKhoanDangNhap = account;

		setClosable(true);
		setMaximizable(true);
		setResizable(true);
		setIconifiable(true);
		setTitle("Đặt Bàn");

		setLayout(new BorderLayout());

		ban_DAO = new Ban_DAO();
		hoaDonDAO = new HoaDon_DAO();
		khachHangDAO = new KhachHang_DAO();
		datBanDAO = new DatBan_DAO();
		ctDatBanDAO = new ChiTietDatBan_DAO();
		ctHoaDonDAO = new ChiTietHoaDon_DAO();

		pnlLeft = new JPanel();
		pnlLeft.setLayout(new GridLayout(10, 1, 5, 5));
		pnlLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlLeft.setBackground(new Color(224, 224, 224));

		btnDatBanNgay = createStyledButton("Đặt bàn ngay (F4)");
		btnDatBanCho = createStyledButton("Đặt bàn chờ (F5)");
		btnNhanBanCho = createStyledButton("Nhận bàn chờ (F6)");
		btnHuyBanCho = createStyledButton("Hủy bàn chờ (F7)");

		pnlLeft.add(btnDatBanNgay);
		pnlLeft.add(btnDatBanCho);
		pnlLeft.add(btnNhanBanCho);
		pnlLeft.add(btnHuyBanCho);

		btnDatBanNgay.addActionListener(this);
		btnDatBanCho.addActionListener(this);
		btnNhanBanCho.addActionListener(this);
		btnHuyBanCho.addActionListener(this);

		inputMap = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		actionMap = this.getActionMap();

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "datBanNgay");
		actionMap.put("datBanNgay", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnDatBanNgay.doClick();
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "datBanCho");
		actionMap.put("datBanCho", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnDatBanCho.doClick();
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "nhanBanCho");
		actionMap.put("nhanBanCho", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnNhanBanCho.doClick();
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "huyBanCho");
		actionMap.put("huyBanCho", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnHuyBanCho.doClick();
			}
		});
		add(pnlLeft, BorderLayout.WEST);

		pnlFilter = new JPanel();
		pnlFilter.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnlFilter.setBackground(Color.white);

		pnlFilter.add(new JLabel("Khu vực:"));
		cboTrangThai = new JComboBox<>(new String[] { "Tất cả", "Trống", "Đang dùng", "Đang chờ nhận" });
		pnlFilter.add(cboTrangThai);

		pnlFilter.add(new JLabel("Bàn số"));
		txtBanSo = new JTextField(6);
		pnlFilter.add(txtBanSo);

		btnTim = new JButton("Tìm");
		btnLamMoi = new JButton("Làm mới");
		pnlFilter.add(btnTim);
		pnlFilter.add(btnLamMoi);

		lblTitle = new JLabel("DANH SÁCH BÀN", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.ITALIC, 24));
		lblTitle.setOpaque(true);
		lblTitle.setBackground(new Color(139, 69, 19));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setPreferredSize(new Dimension(0, 50));

		JPanel pnlNorth = new JPanel(new BorderLayout());
		pnlNorth.add(lblTitle, BorderLayout.NORTH);
		pnlNorth.add(pnlFilter, BorderLayout.SOUTH);

		add(pnlNorth, BorderLayout.NORTH);

		pnlTables = new JPanel(new GridLayout(3, 5, 20, 20));
		pnlTables.setBorder(new EmptyBorder(15, 15, 15, 15));
		pnlTables.setBackground(new Color(255, 255, 255));

		loadTables();

		add(new JScrollPane(pnlTables), BorderLayout.CENTER);

		cboTrangThai.addActionListener(this);
		btnTim.addActionListener(this);
		btnLamMoi.addActionListener(this);
	}

	private JButton createStyledButton(String text) {
		JButton btn = new JButton(text);
		btn.setFocusPainted(false);
		btn.setForeground(Color.white);
		btn.setBackground(new Color(139, 69, 19));
		return btn;
	}

	private static ImageIcon getTableIcon(String trangThai) {
		String path = switch (trangThai) {
		case "Trống" -> "/images/empty_table.png";
		case "Đang dùng" -> "/images/using_table.png";
		case "Đang chờ nhận" -> "/images/waiting_table.png";
		default -> "/images/a.png";
		};

		try {
			ImageIcon rawIcon = new ImageIcon(FrameDatBan.class.getResource(path));
			Image scaledImg = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			return new ImageIcon(scaledImg);
		} catch (Exception e) {
			System.err.println("Không tìm thấy ảnh: " + path);
			return new ImageIcon();
		}
	}

	public void loadTables() {
		pnlTables.removeAll();
		danhSachBan.clear();

		try {
			List<Ban> dsBanEntities = ban_DAO.getAllBan();

			for (Ban ban : dsBanEntities) {
				String tableStatus = ban.getTrangThai();
				String tenBan = ban.getTenBan();
				ImageIcon image = getTableIcon(tableStatus);

				JPanel table = new JPanel(new BorderLayout());
				table.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
				table.setBackground(new Color(240, 255, 255));

				JLabel iconLabel = new JLabel(image, SwingConstants.CENTER);
				JLabel infoLabel = new JLabel(tenBan + " - " + tableStatus, SwingConstants.CENTER);
				infoLabel.setFont(new Font("Arial", Font.BOLD, 14));

				table.add(iconLabel, BorderLayout.CENTER);
				table.add(infoLabel, BorderLayout.SOUTH);

				BanUI banUI = new BanUI(ban, table, infoLabel, iconLabel);

				table.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						selectTable(banUI);
					}
				});

				pnlTables.add(table);
				danhSachBan.add(banUI);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách bàn từ CSDL!");
		}

		filterTables();

		pnlTables.revalidate();
		pnlTables.repaint();
	}

	private void selectTable(BanUI banUI) {
		if (selectedBanUI != null) {
			selectedBanUI.panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
			selectedBanUI.panel.setBackground(new Color(240, 255, 255));
		}
		selectedBanUI = banUI;
		selectedBanUI.panel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		selectedBanUI.panel.setBackground(new Color(255, 230, 230));
	}

	private void filterTables() {
		String selectedStatus = (String) cboTrangThai.getSelectedItem();
		String banSoInput = txtBanSo.getText().trim();

		String tenBanCanTim = banSoInput;
		if (banSoInput.matches("\\d+")) {
			tenBanCanTim = "Bàn " + banSoInput;
		}

		for (BanUI item : danhSachBan) {
			boolean statusMatch = selectedStatus.equals("Tất cả") || item.ban.getTrangThai().equals(selectedStatus);
			boolean nameMatch = banSoInput.isEmpty() || item.ban.getTenBan().equalsIgnoreCase(tenBanCanTim);
			item.panel.setVisible(statusMatch && nameMatch);
		}

		pnlTables.revalidate();
		pnlTables.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == cboTrangThai || src == btnTim) {
			filterTables();
		} else if (src == btnLamMoi) {
			cboTrangThai.setSelectedItem("Tất cả");
			txtBanSo.setText("");
			filterTables();
		}

		String command = e.getActionCommand();
		if (command.startsWith("Đặt bàn ngay")) {
			xuLyDatBanNgay();
		} else if (command.startsWith("Đặt bàn chờ")) {
			xuLyDatBanCho();
		} else if (command.startsWith("Nhận bàn chờ")) {
			xuLyNhanBanCho();
		} else if (command.startsWith("Hủy bàn chờ")) {
			xuLyHuyBan();
		}
	}

	private void xuLyDatBanNgay() {
		if (selectedBanUI == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một bàn trước.");
			return;
		}
		Ban banChon = selectedBanUI.ban;
		if (!banChon.getTrangThai().equals("Trống")) {
			JOptionPane.showMessageDialog(this,
					"Chỉ có thể đặt bàn đang [Trống]. Bàn này đang [" + banChon.getTrangThai() + "].");
			return;
		}

		FrameDialogDatBan dialog = new FrameDialogDatBan((Frame) SwingUtilities.getWindowAncestor(this), banChon,
				"Đang dùng", true);
		dialog.setVisible(true);

		if (dialog.isConfirmed()) {
			String tenKH = dialog.getTenKhachHang();
			String sdt = dialog.getSDT();
			String maKH = null;

			try {
				KhachHang kh = khachHangDAO.findBySDT(sdt);

				if (kh == null) {
					int confirmTaoKH = JOptionPane.showConfirmDialog(
							this, "Khách hàng với SĐT " + sdt
									+ " chưa tồn tại.\nBạn có muốn tạo khách hàng mới với tên '" + tenKH + "' không?",
							"Tạo khách hàng mới?", JOptionPane.YES_NO_OPTION);

					if (confirmTaoKH == JOptionPane.YES_OPTION) {
						KhachHang khMoi = khachHangDAO.taoKhachHangMoi(tenKH, sdt);
						if (khMoi != null) {
							maKH = khMoi.getMaKhachHang();
							JOptionPane.showMessageDialog(this, "Đã tạo khách hàng mới: " + maKH);
						} else {
							JOptionPane.showMessageDialog(this, "Tạo khách hàng mới thất bại!");
							return;
						}
					}
				}else if (kh != null) {
					maKH = kh.getMaKhachHang();
				}
				boolean taoHDSuccess = hoaDonDAO.taoHoaDonMoi(banChon.getMaBan(), taiKhoanDangNhap.getMaNhanVien(),
						maKH);

				if (taoHDSuccess) {
					capNhatTrangThaiBan(banChon, "Đang dùng");
					moCuaSoDatMon(banChon.getMaBan());
				} else {
					JOptionPane.showMessageDialog(this, "Không thể tạo hóa đơn mới cho bàn.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Lỗi SQL khi tạo hóa đơn: " + e.getMessage());
			}
		}
	}

	private void xuLyDatBanCho() {
		if (selectedBanUI == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một bàn trước.");
			return;
		}
		Ban banChon = selectedBanUI.ban;
		if (!banChon.getTrangThai().equals("Trống")) {
			JOptionPane.showMessageDialog(this, "Chỉ có thể đặt bàn đang [Trống].");
			return;
		}

		FrameDialogDatBan dialog = new FrameDialogDatBan((Frame) SwingUtilities.getWindowAncestor(this), banChon,
				"Đang chờ nhận", false);
		dialog.setVisible(true);

		if (dialog.isConfirmed()) {
			String tenKH = dialog.getTenKhachHang();
			String sdt = dialog.getSDT();
			String gioNhan = dialog.getGioNhan();
			int soLuongNguoi = dialog.getSoLuongNguoi();
			String maKH = null;

			try {
				KhachHang kh = khachHangDAO.findBySDT(sdt);

				if (kh == null) {
					int confirmTaoKH = JOptionPane.showConfirmDialog(
							this, "Khách hàng với SĐT " + sdt
									+ " chưa tồn tại.\nBạn có muốn tạo khách hàng mới với tên '" + tenKH + "' không?",
							"Tạo khách hàng mới?", JOptionPane.YES_NO_OPTION);

					if (confirmTaoKH == JOptionPane.YES_OPTION) {
						KhachHang khMoi = khachHangDAO.taoKhachHangMoi(tenKH, sdt);
						if (khMoi != null) {
							maKH = khMoi.getMaKhachHang();
							JOptionPane.showMessageDialog(this, "Đã tạo khách hàng mới: " + maKH);
						} else {
							JOptionPane.showMessageDialog(this, "Tạo khách hàng mới thất bại!", "Lỗi CSDL",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				} else {
					maKH = kh.getMaKhachHang();
					if (!kh.getTenKhachHang().equalsIgnoreCase(tenKH)) {
						JOptionPane.showMessageDialog(this, "SĐT này đã được đăng ký cho: " + kh.getTenKhachHang()
								+ "\nPhiếu đặt bàn sẽ được gán cho khách hàng này.");
					}
				}

				String maDatBanMoi = datBanDAO.taoDatBan(banChon.getMaBan(), maKH,
						taiKhoanDangNhap.getMaNhanVien(), sdt, gioNhan, soLuongNguoi, "");

				if (maDatBanMoi != null) {
					capNhatTrangThaiBan(banChon, "Đang chờ nhận");
					JOptionPane.showMessageDialog(this, banChon.getTenBan() + " đã chuyển sang [Đang chờ nhận].");

					int confirmDatMon = JOptionPane.showConfirmDialog(this,
							"Bạn có muốn thêm món cho phiếu đặt bàn này ngay không?", "Đặt món trước",
							JOptionPane.YES_NO_OPTION);
					if (confirmDatMon == JOptionPane.YES_NO_OPTION) {
						Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
						FrameDialogDatMon dialogDatMon = new FrameDialogDatMon(parentFrame, maDatBanMoi, true);
						dialogDatMon.setVisible(true);
					}
				} else {
					JOptionPane.showMessageDialog(this, "Tạo phiếu đặt bàn thất bại!");
				}

			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Lỗi SQL khi xử lý đặt bàn chờ: " + e.getMessage());
			}
		}
	}

	private void xuLyNhanBanCho() {
		if (selectedBanUI == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một bàn trước.");
			return;
		}
		Ban banChon = selectedBanUI.ban;
		if (!banChon.getTrangThai().equals("Đang chờ nhận")) {
			JOptionPane.showMessageDialog(this, "Chỉ có thể nhận bàn đang [Đang chờ nhận].");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Xác nhận nhận " + banChon.getTenBan() + " và chuyển sang [Đang dùng]?", "Xác nhận",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				DatBan datBan = datBanDAO.getDatBanByMaBan(banChon.getMaBan(), "Đã xác nhận");
				if (datBan == null) {
					JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy phiếu đặt bàn 'Đã xác nhận' cho bàn này.");
					capNhatTrangThaiBan(banChon, "Trống");
					return;
				}
				
				String maKH = (datBan.getKhachHang() != null) ? datBan.getKhachHang().getMaKhachHang() : null;

				boolean taoHDSuccess = hoaDonDAO.taoHoaDonMoi(banChon.getMaBan(), taiKhoanDangNhap.getMaNhanVien(), maKH);

				if (taoHDSuccess) {
					HoaDon hoaDonMoi = hoaDonDAO.findActiveByMaBan(banChon.getMaBan());
					if (hoaDonMoi == null) {
						JOptionPane.showMessageDialog(this, "Lỗi: Không thể tìm thấy hóa đơn vừa tạo.");
						return;
					}
					
					List<Object[]> chiTietDatTruoc = ctDatBanDAO.findAllChiTietByMaDatBan(datBan.getMaDatBan());
					
					for (Object[] item : chiTietDatTruoc) {
						String maSP = (String) item[0];
						int soLuong = (int) item[1];
						double donGia = (double) item[2];
						ctHoaDonDAO.themHoacCapNhatChiTiet(hoaDonMoi.getMaHoaDon(), maSP, soLuong, donGia);
					}
					
					capNhatTrangThaiBan(banChon, "Đang dùng");
					datBanDAO.updateTrangThaiDatBan(datBan.getMaDatBan(), "Đã nhận bàn");
					
					JOptionPane.showMessageDialog(this, "Đã nhận bàn và chuyển các món đặt trước vào hóa đơn.");
					moCuaSoDatMon(banChon.getMaBan());
				} else {
					JOptionPane.showMessageDialog(this, "Không thể tạo hóa đơn mới cho bàn.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Lỗi SQL khi nhận bàn chờ: " + e.getMessage());
			}
		}
	}

	private void xuLyHuyBan() {
		if (selectedBanUI == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một bàn trước.");
			return;
		}
		Ban banChon = selectedBanUI.ban;
		if (banChon.getTrangThai().equals("Trống")) {
			JOptionPane.showMessageDialog(this, "Bàn này đã [Trống].");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Xác nhận HỦY đặt/sử dụng và chuyển " + banChon.getTenBan() + " về [Trống]?", "Xác nhận",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			capNhatTrangThaiBan(banChon, "Trống");
			JOptionPane.showMessageDialog(this, banChon.getTenBan() + " đã được trả về [Trống].");
		}
	}

	private void capNhatTrangThaiBan(Ban banChon, String trangThaiMoi) {
		try {
			boolean success = ban_DAO.updateTrangThaiBan(banChon.getMaBan(), trangThaiMoi);

			if (success) {
				banChon.setTrangThai(trangThaiMoi);
				selectedBanUI.infoLabel.setText(banChon.getTenBan() + " - " + trangThaiMoi);
				selectedBanUI.iconLabel.setIcon(getTableIcon(trangThaiMoi));

				if (selectedBanUI != null) {
					selectedBanUI.panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
					selectedBanUI.panel.setBackground(new Color(240, 255, 255));
					selectedBanUI = null;
				}

				filterTables();
			} else {
				JOptionPane.showMessageDialog(this, "Không thể cập nhật trạng thái bàn trong CSDL.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi truy vấn CSDL: " + e.getMessage());
		}
	}

	private void moCuaSoDatMon(String maBan) {
		try {
			HoaDon hoaDon = hoaDonDAO.findActiveByMaBan(maBan);

			if (hoaDon == null) {
				JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy hóa đơn 'Chưa thanh toán' nào cho " + maBan);
				return;
			}

			Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
			FrameDialogDatMon dialogDatMon = new FrameDialogDatMon(parentFrame, hoaDon.getMaHoaDon());
			dialogDatMon.setVisible(true);

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi CSDL khi tìm hóa đơn: " + e.getMessage());
		}
	}
}