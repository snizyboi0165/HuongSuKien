//Người làm: Nguyễn Cao Việt An
package gui;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import connectDB.DatabaseConnection;

public class FrameLogin extends JFrame implements ActionListener {

	private JTextField usernameField;
	private JPasswordField passwordField;
	private ImageIcon loginImg;
	private JLabel lblLogin;
	private JButton btnLogin;
	private JLabel lblSubTitle;
	private JButton btnForgotPassword;
	private JButton btnExit;
	private JPanel pnlMain;
	private JPanel pnlLeft;
	private JPanel pnlRight;
	private JPanel pnlUsername;
	private JPanel pnlPassword;
	private JPanel pnlLinks;
	
	private TaiKhoan_DAO taiKhoan_DAO;
	private NhanVien_DAO nhanVien_DAO;
	
	public FrameLogin() {
		setTitle("Đăng Nhập Hệ Thống Quản Lý Quán Cafe");
		try {
			DatabaseConnection.getInstance().getConnection();
			taiKhoan_DAO = new TaiKhoan_DAO();
			nhanVien_DAO = new NhanVien_DAO();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Không thể kết nối đến Cơ sở dữ liệu. Vui lòng kiểm tra lại!");
		}

		pnlMain = new JPanel(new GridLayout(1, 2));

		try {
			URL imageUrl = getClass().getResource("/images/cafe_login.jpg");

			if (imageUrl != null) {
				loginImg = new ImageIcon(imageUrl);
			} else {
				System.out.println("Không tìm thấy ảnh!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		pnlLeft = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (loginImg != null) {
					Image image = loginImg.getImage();
					g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
				}
			}
		};
		pnlLeft.setLayout(new BorderLayout());
		pnlLeft.setBackground(new Color(255, 102, 0));
		pnlMain.add(pnlLeft);

		pnlRight = new JPanel();
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
		pnlRight.setBackground(Color.white);
		pnlRight.setBorder(new EmptyBorder(40, 50, 40, 50));

		lblLogin = new JLabel("ĐĂNG NHẬP");
		lblLogin.setFont(new Font("Monsterrat", Font.BOLD, 30));
		lblLogin.setForeground(Color.black);
		lblLogin.setAlignmentX(CENTER_ALIGNMENT);

		lblSubTitle = new JLabel("Vui lòng đăng nhập để tiếp tục");
		lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblSubTitle.setForeground(new Color(102, 102, 102));
		lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		usernameField = new JTextField();
		usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		usernameField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		pnlUsername = createInputPanel("Tài khoản", "/images/username_icon.png", usernameField);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		passwordField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(5, 0, 5, 5)));
		pnlPassword = createInputPanel("Mật khẩu", "/images/password_icon.png", passwordField);

		final Color mainButtonColor = new Color(139, 69, 19);
		btnLogin = new JButton("ĐĂNG NHẬP") {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				if (getModel().isRollover()) {
					g2.setColor(mainButtonColor.brighter());
				} else {
					g2.setColor(mainButtonColor);
				}
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
				g2.dispose();
				super.paintComponent(g);
			}
		};
		btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBorderPainted(false);
		btnLogin.setFocusPainted(false);
		btnLogin.setContentAreaFilled(false);
		btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLogin.setMaximumSize(new Dimension(250, 40));

		pnlRight.add(lblLogin);
		pnlRight.add(lblSubTitle);
		pnlRight.add(Box.createVerticalStrut(40));
		pnlRight.add(pnlUsername);
		pnlRight.add(Box.createVerticalStrut(20));
		pnlRight.add(pnlPassword);
		pnlRight.add(Box.createVerticalStrut(20));
		pnlRight.add(btnLogin);

		pnlLinks = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		pnlLinks.setBackground(Color.WHITE);

		btnForgotPassword = new JButton("Quên mật khẩu?");
		btnForgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnForgotPassword.setForeground(Color.BLUE);
		btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnForgotPassword.setBorderPainted(false);
		btnForgotPassword.setFocusPainted(false);
		btnForgotPassword.setContentAreaFilled(false);

		btnExit = new JButton("Thoát");
		btnExit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnExit.setForeground(Color.RED);
		btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnExit.setBorderPainted(false);
		btnExit.setFocusPainted(false);
		btnExit.setContentAreaFilled(false);

		pnlLinks.add(btnForgotPassword);
		pnlLinks.add(btnExit);

		pnlRight.add(Box.createVerticalStrut(15));
		pnlRight.add(pnlLinks);

		pnlMain.add(pnlRight);
		add(pnlMain);

		btnLogin.addActionListener(this);
		btnForgotPassword.addActionListener(this);
		btnExit.addActionListener(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(950, 500);
		setLocationRelativeTo(null);
		setResizable(false);

		this.getRootPane().setDefaultButton(btnLogin);
		SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnLogin)) {
			String username = usernameField.getText().trim();
			String password = new String(passwordField.getPassword());

			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
				return;
			}

			boolean loginSuccess = false;
			String employeeFullName = null;
			TaiKhoan taiKhoan = null;

			try {
				taiKhoan = taiKhoan_DAO.kiemTraDangNhap(username, password);

				if (taiKhoan != null) {
					employeeFullName = nhanVien_DAO.getTenNhanVien(taiKhoan.getMaNhanVien());

					if (employeeFullName != null) {
						taiKhoan.setHoTenNhanVien(employeeFullName);
						loginSuccess = true;
					} else {
						JOptionPane.showMessageDialog(this,
								"Không tìm thấy thông tin nhân viên tương ứng với tài khoản.");
						loginSuccess = false;
						System.err.println("Lỗi: Không tìm thấy nhân viên với MaNhanVien: " + taiKhoan.getMaNhanVien());
					}
				} else {
					System.out.println("Sai tên đăng nhập hoặc mật khẩu.");
				}

			} catch (SQLException ex) {
				System.err.println("Lỗi truy vấn CSDL: " + ex.getMessage());
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this,
						"Lỗi kết nối hoặc truy vấn cơ sở dữ liệu.");
			}

			if (loginSuccess && taiKhoan != null) {
				JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
				FrameGiaoDienChinh mainFrame = new FrameGiaoDienChinh(taiKhoan);
				mainFrame.setVisible(true);
				this.dispose();
			} else if (!loginSuccess && !username.isEmpty() && !password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng.");
				passwordField.setText("");
				passwordField.requestFocus();
			}
		} else if (o.equals(btnExit)) {
			System.exit(0);
		} else if (o.equals(btnForgotPassword)) {
			xuLyQuenMatKhau();
		}
	}
    
    private void xuLyQuenMatKhau() {
        JTextField txtUsername = new JTextField(20);
        
        JPanel pnlInput = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pnlInput.add(new JLabel("Tên đăng nhập:"));
        pnlInput.add(txtUsername);
        
        int result = JOptionPane.showConfirmDialog(
            this, pnlInput, 
            "Khôi phục mật khẩu", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String username = txtUsername.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!");
                return;
            }
            
            try {
				boolean tonTai = taiKhoan_DAO.kiemTraTonTai(username);
				
				if (tonTai) {
					JOptionPane.showMessageDialog(this, 
						"Hệ thống đã gửi hướng dẫn khôi phục mật khẩu tới email liên kết với tài khoản '" + username + "'");
				} else {
					JOptionPane.showMessageDialog(this, 
						"Tên đăng nhập '" + username + "' không tồn tại trong hệ thống.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra cơ sở dữ liệu: " + e.getMessage());
			}
        }
    }

	private JPanel createInputPanel(String labelText, String iconPath, JComponent inputField) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 70));

		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.X_AXIS));
		pn.setOpaque(false);
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Segoe UI", Font.BOLD, 14));
		label.setForeground(new Color(51, 51, 51));

		pn.add(label);

		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.X_AXIS));
		inputContainer.setOpaque(false);

		try {
			URL iconUrl = getClass().getResource(iconPath);
			if (iconUrl != null) {
				ImageIcon icon = new ImageIcon(iconUrl);
				Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
				icon = new ImageIcon(img);
				JLabel iconLabel = new JLabel(icon);
				iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
				inputContainer.add(iconLabel);
			} else {
				System.err.println("Không tìm thấy icon tại: " + iconPath);
			}
		} catch (Exception e) {
			System.err.println("Lỗi khi tải icon: " + iconPath + " - " + e.getMessage());
			e.printStackTrace();
		}

		inputContainer.add(inputField);

		panel.add(pn);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(inputContainer);

		return panel;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() ->new FrameLogin().setVisible(true));
	}
}