//Người làm: Nguyễn Thành Đạt
package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

import dao.KhachHang_DAO;
import entity.KhachHang;

public class FrameThemSuaKhachHang extends JDialog implements ActionListener {
	private JPanel pnlMain;
    private JTextField txtTenKH;
    private JTextField txtMaKH;
    private JTextField txtSDT;
    private JTextField txtDiem;
    private JButton btnThem;
    private JButton btnHuy;
    private JButton btnXoaTrang; 

    private KhachHang_DAO khachHangDAO;
    private boolean isSuccess = false; 
    private boolean isEditMode = false;
    
    private KhachHang khachHangHienTai;

    public FrameThemSuaKhachHang(Frame owner, KhachHang khachHangToEdit) {
        super(owner, khachHangToEdit != null ? "Sửa Thông Tin Khách Hàng" : "Thêm Khách Hàng Mới", true);
        this.isEditMode = (khachHangToEdit != null);
        this.khachHangHienTai=khachHangToEdit;
        
        setSize(550, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        
        khachHangDAO = new KhachHang_DAO();

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlMain.add(createFormPanel(), BorderLayout.NORTH);
        pnlMain.add(createButtonPanel(), BorderLayout.SOUTH);

        add(pnlMain);

        if (isEditMode) {
            loadDataToForm(khachHangToEdit);
        } else {
            SwingUtilities.invokeLater(() -> txtTenKH.requestFocusInWindow());
        }
    }
   
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if (src == btnThem) {
            xuLyLuuDuLieu();
        } else if (src == btnHuy) {
            this.dispose();
        } else if (src == btnXoaTrang) {
            xuLyXoaTrangForm();
        }
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.white);
        btn.setBackground(bgColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return btn;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin khách hàng"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel inputContainer = new JPanel(new GridBagLayout());
        inputContainer.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        inputContainer.add(new JLabel("Mã khách hàng:"), gbc);
        txtMaKH = new JTextField(isEditMode ? "" : "Mã tự động tạo");
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        inputContainer.add(txtMaKH, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputContainer.add(new JLabel("Họ tên:"), gbc);
        txtTenKH = new JTextField(20);
        gbc.gridx = 1;
        inputContainer.add(txtTenKH, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputContainer.add(new JLabel("Số điện thoại:"), gbc);
        txtSDT = new JTextField(20);
        gbc.gridx = 1;
        inputContainer.add(txtSDT, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        inputContainer.add(new JLabel("Điểm tích luỹ:"), gbc);
        txtDiem = new JTextField("0");
        txtDiem.setEditable(isEditMode);
        txtDiem.setBackground(isEditMode ? Color.WHITE : new Color(240, 240, 240));
        gbc.gridx = 1;
        inputContainer.add(txtDiem, gbc);
        
        panel.add(inputContainer, BorderLayout.CENTER);
        return panel;
    }

    private void loadDataToForm(KhachHang kh) {
        txtMaKH.setText(kh.getMaKhachHang());
        txtTenKH.setText(kh.getTenKhachHang());
        txtSDT.setText(kh.getSoDienThoai());
        txtDiem.setText(String.valueOf(kh.getDiemTichLuy()));
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnHuy = createStyledButton("Hủy", new Color(220, 53, 69));
        btnXoaTrang = createStyledButton("Xóa Trắng", new Color(108, 117, 125));
        
        btnThem = createStyledButton(isEditMode ? "Cập Nhật" : "Thêm", new Color(40, 167, 69));
        
        btnHuy.addActionListener(this);
        btnXoaTrang.addActionListener(this); 
        btnThem.addActionListener(this);

        buttonPanel.add(btnHuy);
        if (!isEditMode) {
             buttonPanel.add(btnXoaTrang); 
        }
        buttonPanel.add(btnThem);
        return buttonPanel;
    }
    
    private void xuLyXoaTrangForm() {
        txtTenKH.setText("");
        txtSDT.setText("");
        txtTenKH.requestFocusInWindow();
    }
    
    private void xuLyLuuDuLieu() {
        String ten = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String diemStr = txtDiem.getText().trim();

        if (ten.isEmpty() || sdt.isEmpty() || diemStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Họ tên và Số điện thoại!");
            return;
        }

        try {
            int diem = Integer.parseInt(diemStr);
            KhachHang khMoi = null;
            boolean success = false;
            String maKH = txtMaKH.getText().trim();
            
            if (isEditMode) {
                khMoi = new KhachHang(maKH, ten, diem, sdt);
                success = khachHangDAO.capNhatKhachHang(khMoi);
            } else {
                khMoi = khachHangDAO.taoKhachHangMoi(ten, sdt); 
                success = (khMoi != null); 
            }

            if (success) {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Cập nhật" : "Thêm") + " khách hàng thành công!");
                this.isSuccess = true;
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Cập nhật" : "Thêm") + " thất bại! (SĐT đã tồn tại hoặc lỗi CSDL)");
            }
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số nguyên hợp lệ!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi CSDL khi lưu dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }
}