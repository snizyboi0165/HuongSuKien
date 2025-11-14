//Người làm: Nguyễn Thành Đạt
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;

import entity.Ban;

public class FrameDialogDatBan extends JDialog implements ActionListener {

	private JTextField txtMaBan;
	private JTextField txtTenKH;
	private JTextField txtSDT;
	private JTextField txtSoLuongNguoi;
	private JComboBox<String> cboGio;
	private JComboBox<String> cboPhut;
	private JDateChooser dateChooserNgayNhan;
	private JButton btnHuy;
	private JButton btnLamMoi;
	private JButton btnThem;
	private boolean isConfirmed = false;
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
	private JPanel pnlHeader;
	private JLabel lblTitle;
	private JPanel pnlForm;
	private JLabel lblTenKH;
	private JLabel lblSDT;
	private JLabel lblGioNhan;
	private JPanel pnlButtons;
	private JPanel pnlLeft;
	private JPanel pnlRight;
	private JPanel pnlTime;

	public FrameDialogDatBan(Frame parent, Ban ban, String trangThaiMoi, boolean lockCurrentTime) {
		super(parent, trangThaiMoi.equals("Đang dùng") ? "Đặt Bàn Ngay" : "Đặt Bàn Chờ", true);
		setSize(550, 400);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());

		pnlHeader = new JPanel();
		pnlHeader.setBackground(new Color(69, 201, 199));
		pnlHeader.setPreferredSize(new Dimension(0, 50));

		lblTitle = new JLabel(trangThaiMoi.equals("Đang dùng") ? "Đặt Bàn Ngay" : "Đặt Bàn Chờ");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitle.setForeground(Color.WHITE);
		pnlHeader.add(lblTitle);
		add(pnlHeader, BorderLayout.NORTH);

		pnlForm = new JPanel(new GridBagLayout());
		pnlForm.setBorder(new EmptyBorder(10, 20, 10, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		JLabel lblMaBan = new JLabel("Bàn:");
		lblMaBan.setFont(labelFont);
		pnlForm.add(lblMaBan, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		txtMaBan = new JTextField();
		txtMaBan.setText(ban.getTenBan());
		txtMaBan.setEditable(false);
		txtMaBan.setBackground(Color.LIGHT_GRAY);
		pnlForm.add(txtMaBan, gbc);

		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		lblTenKH = new JLabel("Tên khách hàng:");
		lblTenKH.setFont(labelFont);
		lblTenKH.requestFocus();
		pnlForm.add(lblTenKH, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		txtTenKH = new JTextField();
		pnlForm.add(txtTenKH, gbc);

		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		lblSDT = new JLabel("Số Điện Thoại:");
		lblSDT.setFont(labelFont);
		pnlForm.add(lblSDT, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		txtSDT = new JTextField();
		pnlForm.add(txtSDT, gbc);

		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		lblGioNhan = new JLabel("Giờ nhận:");
		lblGioNhan.setFont(labelFont);
		pnlForm.add(lblGioNhan, gbc);

		pnlTime = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		cboGio = new JComboBox<>();
		for (int h = 6; h <= 21; h++) {
			cboGio.addItem(String.format("%02d", h));
		}
		pnlTime.add(cboGio);
		pnlTime.add(new JLabel(":"));
		String[] minutes = new String[60];
		for (int i = 0; i < 60; i++) {
			minutes[i] = String.format("%02d", i);
		}
		cboPhut = new JComboBox<>(minutes);
		pnlTime.add(cboPhut);
		pnlTime.add(Box.createHorizontalStrut(10));
		dateChooserNgayNhan = new JDateChooser();
		dateChooserNgayNhan.setDateFormatString("dd/MM/yyyy");
		dateChooserNgayNhan.setDate(new Date());
		dateChooserNgayNhan.setPreferredSize(new Dimension(120, cboGio.getPreferredSize().height));
		pnlTime.add(dateChooserNgayNhan);

		if (lockCurrentTime) {
			Calendar now = Calendar.getInstance();
			int hour = now.get(Calendar.HOUR_OF_DAY);
			int minute = now.get(Calendar.MINUTE);

			String phutGanNhat = "00";
			for (int i = cboPhut.getItemCount() - 1; i >= 0; i--) {
				if (minute >= Integer.parseInt(cboPhut.getItemAt(i))) {
					phutGanNhat = cboPhut.getItemAt(i);
					break;
				}
			}

			dateChooserNgayNhan.setDate(now.getTime());
			cboGio.setSelectedItem(String.format("%02d", hour));
			cboPhut.setSelectedItem(phutGanNhat);

			cboGio.setEnabled(false);
			cboPhut.setEnabled(false);
			dateChooserNgayNhan.setEnabled(false);
		}

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		pnlForm.add(pnlTime, gbc);

		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		JLabel lblSoLuongNguoi = new JLabel("Số lượng người:");
		lblSoLuongNguoi.setFont(labelFont);
		pnlForm.add(lblSoLuongNguoi, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		txtSoLuongNguoi = new JTextField("");
		pnlForm.add(txtSoLuongNguoi, gbc);

		add(pnlForm, BorderLayout.CENTER);

		pnlButtons = new JPanel(new BorderLayout(10, 10));
		pnlButtons.setBorder(new EmptyBorder(0, 10, 10, 10));
		pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnHuy = createStyledButton("Hủy", new Color(220, 53, 69), "/images/cancel.png");
		pnlLeft.add(btnHuy);
		pnlButtons.add(pnlLeft, BorderLayout.WEST);
		pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnLamMoi = createStyledButton("Làm mới", new Color(40, 167, 69), "/images/refresh.png");
		btnThem = createStyledButton("Thêm", new Color(0, 123, 255), "/images/add_user.png");
		pnlRight.add(btnLamMoi);
		pnlRight.add(btnThem);
		pnlButtons.add(pnlRight, BorderLayout.EAST);
		add(pnlButtons, BorderLayout.SOUTH);

		btnHuy.addActionListener(this);
		btnLamMoi.addActionListener(this);
		btnThem.addActionListener(this);

		if (lockCurrentTime) {
			btnLamMoi.setEnabled(false);
		}
	}

	private JButton createStyledButton(String text, Color bgColor, String iconPath) {
		JButton button = new JButton(text);
		button.setBackground(bgColor);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		try {
			URL url = getClass().getResource(iconPath);
			if (url != null) {
				ImageIcon icon = new ImageIcon(url);
				Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
				button.setIcon(new ImageIcon(img));
			}
		} catch (Exception e) {
			System.err.println("Icon not found: " + iconPath);
		}
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == btnThem) {
			if (txtTenKH.getText().trim().isEmpty() || txtSDT.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên và Số Điện Thoại khách hàng.", "Thiếu thông tin",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (dateChooserNgayNhan.getDate() == null) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nhận bàn.", "Thiếu thông tin",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			try {
				int soLuong = Integer.parseInt(txtSoLuongNguoi.getText().trim());
				if (soLuong <= 0) {
					JOptionPane.showMessageDialog(this, "Số lượng người phải là số dương.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
					txtSoLuongNguoi.requestFocus();
					return;
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Số lượng người phải là một con số.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
				txtSoLuongNguoi.requestFocus();
				return;
			}

			isConfirmed = true;
			this.dispose();

		} else if (src == btnHuy) {
			isConfirmed = false;
			this.dispose();

		} else if (src == btnLamMoi) {
			txtTenKH.setText("");
			txtSDT.setText("");
			txtSoLuongNguoi.setText("");
			cboGio.setSelectedIndex(0);
			cboPhut.setSelectedIndex(0);
			dateChooserNgayNhan.setDate(new Date());
		}
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public String getTenKhachHang() {
		return txtTenKH.getText().trim();
	}

	public String getSDT() {
		return txtSDT.getText().trim();
	}
	
	public int getSoLuongNguoi() {
        try {
            return Integer.parseInt(txtSoLuongNguoi.getText().trim());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

	public String getGioNhan() {
		Date selectedDate = dateChooserNgayNhan.getDate();
		if (selectedDate == null) {
			return null;
		}

		String gio = (String) cboGio.getSelectedItem();
		String phut = (String) cboPhut.getSelectedItem();

		Calendar cal = Calendar.getInstance();
		cal.setTime(selectedDate);
		try {
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(gio));
			cal.set(Calendar.MINUTE, Integer.parseInt(phut));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		} catch (NumberFormatException e) {
			System.err.println("Error parsing selected time: " + gio + ":" + phut);
			return dateTimeFormat.format(selectedDate);
		}

		return dateTimeFormat.format(cal.getTime());
	}
}