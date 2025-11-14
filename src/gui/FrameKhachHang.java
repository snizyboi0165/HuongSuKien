//Người làm: Nguyễn Thành Đạt
package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.KhachHang_DAO;

import entity.KhachHang;

public class FrameKhachHang extends JInternalFrame implements ActionListener {
	
	private JPanel pnlLeft;
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnSua;
	private JPanel pnlFilter;
	private JLabel lblTitle;
	private JPanel pnlNorth;
	private JPanel pnlKH;
	private JTextField txtTim;
	private JButton btnTim;
	private JButton btnLamMoi;
	private JTable tblKH;
	private DefaultTableModel model;

	private KhachHang_DAO khachHangDAO;
	

	public FrameKhachHang() {
		setTitle("Quản lý Khách Hàng");
		setClosable(true);
		setMaximizable(true);
		setResizable(true);
		setIconifiable(true);
		
		setSize(1200, 700);
		setLayout(new BorderLayout());

		khachHangDAO = new KhachHang_DAO();

		pnlLeft = new JPanel();
		pnlLeft.setLayout(new GridLayout(10, 1, 5, 5));
		pnlLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnlLeft.setBackground(new Color(224, 224, 224));

		btnThem = createStyledButton("Thêm khách hàng (F4)");
		btnXoa = createStyledButton("Xóa khách hàng (F5)");
		btnSua = createStyledButton("Sửa thông tin (F6)");

		pnlLeft.add(btnThem);
		pnlLeft.add(btnXoa);
		pnlLeft.add(btnSua);

		add(pnlLeft, BorderLayout.WEST);

		pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlFilter.setBackground(Color.white);
		pnlFilter.add(new JLabel("Nhập mã Khách Hàng cần tìm:"));
		txtTim = new JTextField(6);
		pnlFilter.add(txtTim);
		btnTim = new JButton("Tìm");
		btnLamMoi = new JButton("Làm mới");
		pnlFilter.add(btnTim);
		pnlFilter.add(btnLamMoi);

		lblTitle = new JLabel("DANH SÁCH KHÁCH HÀNG", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.ITALIC, 24));
		lblTitle.setOpaque(true);
		lblTitle.setBackground(new Color(139, 69, 19));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setPreferredSize(new Dimension(0, 50));

		pnlNorth = new JPanel(new BorderLayout());
		pnlNorth.add(lblTitle, BorderLayout.NORTH);
		pnlNorth.add(pnlFilter, BorderLayout.SOUTH);
		add(pnlNorth, BorderLayout.NORTH);

		pnlKH = new JPanel(new BorderLayout());
		model = new DefaultTableModel(new Object[] { "Mã khách hàng", "Họ tên", "Số Điện thoại", "Điểm tích luỹ" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblKH = new JTable(model);
		tblKH.setRowHeight(25);
		tblKH.setFont(new Font("Arial", Font.PLAIN, 14));
		tblKH.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		JScrollPane scroll = new JScrollPane(tblKH);
		pnlKH.add(scroll, BorderLayout.CENTER);
		add(pnlKH, BorderLayout.CENTER);

		loadKhachHang();

		btnLamMoi.addActionListener(this);
		btnTim.addActionListener(this);
		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnSua.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == btnLamMoi) {
			txtTim.setText("");
			loadKhachHang();
		} else if (src == btnTim) {
			String ma = txtTim.getText().trim();
			timKhachHang(ma);
		} else if (src == btnThem) {
			themKhachHang();
		} else if (src == btnXoa) {
			xoaKhachHang();
		} else if (src == btnSua) {
			suaThongTin();
		}
	}

	private JButton createStyledButton(String text) {
		JButton btn = new JButton(text);
		btn.setFocusPainted(false);
		btn.setForeground(Color.white);
		btn.setBackground(new Color(139, 69, 19));
		return btn;
	}

	private void loadKhachHang() {
		model.setRowCount(0);
		try {
			List<KhachHang> ds = khachHangDAO.timKiemNangCao("", "", "");
			for (KhachHang kh : ds) {
				model.addRow(new Object[] { kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai(),
						kh.getDiemTichLuy() });
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu khách hàng: " + e.getMessage(), "Lỗi CSDL",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void timKhachHang(String ma) {
		if (ma.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng cần tìm!");
			return;
		}
		try {
			List<KhachHang> ds = khachHangDAO.timKiemNangCao(ma, "", "");
			model.setRowCount(0);

			if (ds.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có mã: " + ma);
				loadKhachHang();
			} else {
				for (KhachHang kh : ds) {
					model.addRow(new Object[] { kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai(),
							kh.getDiemTichLuy() });
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm khách hàng: " + e.getMessage(), "Lỗi CSDL",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void themKhachHang() {
		Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
		
		FrameThemSuaKhachHang dialog = new FrameThemSuaKhachHang(parentFrame, null);
		dialog.setVisible(true);
		
		if (dialog.isSuccess()) {
			loadKhachHang();
		}
	}
	
	private void xoaKhachHang() {
		int selectedRow = tblKH.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xoá!");
			return;
		}

		String maKH = (String) model.getValueAt(selectedRow, 0);
		int confirm = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc muốn xoá khách hàng " + maKH + "?\n(Không thể xóa nếu khách hàng đã có hóa đơn)",
				"Xác nhận", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				boolean success = khachHangDAO.xoaKhachHang(maKH);

				if (success) {
					loadKhachHang();
					JOptionPane.showMessageDialog(this, "Xoá khách hàng thành công!");
				} else {
					JOptionPane.showMessageDialog(this,
							"Xoá thất bại! (Kiểm tra xem khách hàng có hóa đơn liên quan không)", "Lỗi CSDL",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, "Lỗi CSDL khi xóa khách hàng: " + e.getMessage(), "Lỗi CSDL",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	private void suaThongTin() {
		int selectedRow = tblKH.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
			return;
		}

		String maCu = (String) model.getValueAt(selectedRow, 0);
		String tenCu = (String) model.getValueAt(selectedRow, 1);
		String sdtCu = (String) model.getValueAt(selectedRow, 2);
		int diemCu = (int) model.getValueAt(selectedRow, 3);
		
		KhachHang khachHangSua = new KhachHang(maCu, tenCu, diemCu, sdtCu);
		
		Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
		
		FrameThemSuaKhachHang dialog = new FrameThemSuaKhachHang(parentFrame, khachHangSua); 
		dialog.setVisible(true);

		if (dialog.isSuccess()) {
			loadKhachHang();
		}
	}

}