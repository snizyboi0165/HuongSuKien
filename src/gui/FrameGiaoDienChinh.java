//Người làm: Nguyễn Cao Việt An
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.net.URL;

import entity.TaiKhoan;

public class FrameGiaoDienChinh extends JFrame {

    private JDesktopPane desktopPane;
    private JMenu menuHeThong;       
    private JMenu menuDanhMuc;       
    private JMenu menuXuLy;          
    private JMenu menuThongKe;       
    private JMenu menuTimKiem;
    private JMenuBar menuBar;
    private TaiKhoan loggedInAccount;
    private String loggedInUserName;
	private JLabel lblUser;
    private JMenuItem logoutItem;
    private JMenuItem itemTimKiemNangCao;
	private JMenuItem itemTrangChu;
	private JMenuItem itemTaiKhoan;
	private JMenuItem itemQLSP;
	private JMenuItem itemQLKH;
	private JMenuItem itemQLHD;
	private JMenuItem itemQLKM;
	private JMenuItem itemDatBan;
	private JMenuItem itemDatMon;
	private JMenuItem itemThanhToan;
	private JMenuItem itemTKNgay;
	private JMenuItem itemTKThang;


    public FrameGiaoDienChinh(TaiKhoan account) {
        if (account == null) {
            account = new TaiKhoan("TEST", "User Mặc Định", "NV_TEST", "NV");
            account.setHoTenNhanVien("User Mặc Định");
        }
        this.loggedInAccount = account;
        
        String userName = account.getHoTenNhanVien();
        this.loggedInUserName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : account.getTenDangNhap();

        setTitle("Phần Mềm Quản Lý Quán Cafe - Xin chào: " + this.loggedInUserName);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);

        setJMenuBar(createMenuBar());

        desktopPane = createDesktopPane();
        setContentPane(desktopPane);
    }

    private JDesktopPane createDesktopPane() {
         return new JDesktopPane() {
            private Image backgroundImage;
            {
                try {
                    URL url = getClass().getResource("/images/background-cafe.png");
                    if (url != null) {
                        this.backgroundImage = new ImageIcon(url).getImage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
    }
    
    private JMenuBar createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(getWidth(), 40));
        menuBar.setBackground(Color.white);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        
        menuHeThong = createStyledMenu("Hệ Thống");
        menuHeThong.setIcon(createIcon("/images/system.png", 24, 24));
        
        itemTrangChu = createStyledMenuItem("Trang chủ", "/images/home.png");
        itemTaiKhoan = createStyledMenuItem("Tài khoản", "/images/account.png");
        
        menuHeThong.add(itemTrangChu);
        menuHeThong.add(itemTaiKhoan);
        menuHeThong.addSeparator();

        logoutItem = createStyledMenuItem("Đăng Xuất", "/images/logout.png");
        logoutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        for(ActionListener al : logoutItem.getActionListeners()) {
             logoutItem.removeActionListener(al);
        }
        logoutItem.addActionListener(e -> handleLogout());
        menuHeThong.add(logoutItem);

        menuDanhMuc = createStyledMenu("Danh Mục");
        menuDanhMuc.setIcon(createIcon("/images/quan_ly.png", 24, 24));
        
        itemQLSP = createStyledMenuItem("Sản Phẩm", "/images/coffee_cup.png");
        itemQLKH = createStyledMenuItem("Khách Hàng", "/images/khach_hang.png");
        itemQLHD = createStyledMenuItem("Hóa Đơn", "/images/rece.png");
        itemQLKM = createStyledMenuItem("Khuyến Mãi", "/images/percent.png");
        
        menuDanhMuc.add(itemQLSP);
        menuDanhMuc.add(itemQLKH);
        menuDanhMuc.add(itemQLHD);
        menuDanhMuc.add(itemQLKM);

        menuXuLy = createStyledMenu("Xử Lý");
        menuXuLy.setIcon(createIcon("/images/xu_ly.png", 24, 24));
        
        itemDatBan = createStyledMenuItem("Đặt Bàn", "/images/dat_ban.png");
        itemDatMon = createStyledMenuItem("Gọi Món", "/images/order.png");
        itemThanhToan = createStyledMenuItem("Thanh Toán", "/images/thanh_toan.png");
        
        menuXuLy.add(itemDatBan);
        menuXuLy.add(itemDatMon);
        menuXuLy.add(itemThanhToan);

        menuTimKiem = createStyledMenu("Tìm Kiếm");
        menuTimKiem.setIcon(createIcon("/images/search.png", 24, 24));
        itemTimKiemNangCao = createStyledMenuItem("Tra Cứu Tổng Hợp", "/images/search.png");
        menuTimKiem.add(itemTimKiemNangCao);
        
        menuThongKe = createStyledMenu("Thống Kê");
        menuThongKe.setIcon(createIcon("/images/thong_ke.png", 24, 24));
        
        itemTKNgay = createStyledMenuItem("Theo Ngày", "/images/icon-doanh-thu.png");
        itemTKThang = createStyledMenuItem("Theo Tháng", "/images/icon-doanh-thu.png");
        
        menuThongKe.add(itemTKNgay);
        menuThongKe.add(itemTKThang);
        
        
        menuBar.add(menuHeThong);
        menuBar.add(menuDanhMuc);
        menuBar.add(menuXuLy);
        menuBar.add(menuTimKiem);
        menuBar.add(menuThongKe);

        menuBar.add(Box.createHorizontalGlue());
        lblUser = new JLabel(" " + loggedInUserName + "  ");
        lblUser.setForeground(Color.BLACK);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ImageIcon userIcon = createIcon("/images/user.png", 24, 24);
        if (userIcon != null) {
        	lblUser.setIcon(userIcon);
        }
        menuBar.add(lblUser);

        return menuBar;
    }

    private JMenu createStyledMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setForeground(Color.BLACK);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return menu;
    }

    private JMenuItem createStyledMenuItem(String title, String iconPath) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menuItem.setBackground(Color.WHITE);
        menuItem.setForeground(Color.BLACK);
        ImageIcon icon = createIcon(iconPath, 20, 20);
        if (icon != null) {
            menuItem.setIcon(icon);
        }
        
        menuItem.addActionListener(e -> openInternalFrame(title));
        
        return menuItem;
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            new FrameLogin().setVisible(true); 
        }
    }
    
    private void openInternalFrame(String title) {
        if (title.equals("Trang chủ")) {
            for (JInternalFrame frame : desktopPane.getAllFrames()) {
                frame.dispose();
            }
            return; 
        }

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame.getTitle() != null && frame.getTitle().equals(title)) {
                try {
                    frame.setSelected(true); 
                    if (frame.isIcon()) { 
                        frame.setIcon(false);
                    }
                    frame.moveToFront();
                } catch (PropertyVetoException e) {
                     e.printStackTrace();
                }
                return; 
            }
        }

        JInternalFrame internalFrame = null;
        switch (title) {
            case "Tài khoản":
                 internalFrame = new FrameTaiKhoan(loggedInAccount);
                 break;
            case "Sản Phẩm":
                internalFrame = new FrameSanPham();
                break;
            case "Khách Hàng":
                internalFrame = new FrameKhachHang();
                break;
            case "Hóa Đơn":
                internalFrame = new FrameHoaDon();
                break;
            case "Khuyến Mãi":
                internalFrame = new FrameKhuyenMai();
				break;
            case "Đặt Bàn":
                 internalFrame = new FrameDatBan(loggedInAccount);
                 break;
            case "Gọi Món":
                internalFrame = new FrameDatMon();
                break;
            case "Thanh Toán":
                internalFrame = new FrameThanhToan(this);
                break;
            case "Tra Cứu Tổng Hợp":
                 internalFrame = new FrameTimKiem();
                 break;
            case "Theo Ngày":
                internalFrame = new FrameThongKeTheoNgay();
                break;
            case "Theo Tháng":
                internalFrame = new FrameThongKeTheoThang();
                break;

            default: 
                break;
        }

         if (internalFrame != null) {
            desktopPane.add(internalFrame);
            try {
                 internalFrame.setMaximum(true);
                 internalFrame.setSelected(true);
            } catch (PropertyVetoException e) {
                 e.printStackTrace();
            }
            internalFrame.setVisible(true);
            internalFrame.moveToFront();
        }
    }

     private ImageIcon createIcon(String path, int width, int height) {
        URL url = getClass().getResource(path);
        if (url != null) {
             try {
                ImageIcon originalIcon = new ImageIcon(url);
                Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } catch (Exception e) {
                 e.printStackTrace();
                return null; 
            }
        }
        return null; 
    }
     
 	public void refreshFrameDatBan() {
		for (JInternalFrame frame : desktopPane.getAllFrames()) {
			if (frame instanceof FrameDatBan) {
				((FrameDatBan) frame).loadTables();
				break; 
			}
		}
	}
}