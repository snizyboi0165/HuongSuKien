//Người làm: Ngô Văn Thành
package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

import dao.SanPham_DAO;
import entity.SanPham;

public class FrameThemSuaSanPham extends JDialog implements ActionListener {
	
	private JPanel pnlMain;
    private JTextField txtMaSP;
    private JTextField txtTenSP;
    private JTextField txtGiaBan;
    private JTextField txtMoTa;
    private JComboBox<String> cboLoai;
    private JComboBox<String> cboTrangThai;
    private JButton btnLuu;
    private JButton btnHuy;
    private JButton btnXoaTrang; 

    private SanPham_DAO sanPhamDAO;
    private boolean isSuccess = false; 
    private boolean isEditMode = false;
    private SanPham sanPhamHienTai;
	 

    public FrameThemSuaSanPham(Frame owner, SanPham sanPhamToEdit) {
        super(owner, sanPhamToEdit != null ? "Sửa Thông Tin Sản Phẩm" : "Thêm Sản Phẩm Mới", true);
        
        this.isEditMode = (sanPhamToEdit != null);
        this.sanPhamHienTai = sanPhamToEdit;
        
        setSize(750, 450); 
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        
        sanPhamDAO = new SanPham_DAO();

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlMain.add(createProductFormPanel(), BorderLayout.NORTH); 
        pnlMain.add(createButtonPanel(), BorderLayout.SOUTH);

        add(pnlMain);
        
        if (isEditMode) {
            loadDataToForm(sanPhamToEdit);
        } else {
            SwingUtilities.invokeLater(() -> {
                 txtMaSP.requestFocusInWindow();
            });
        }
    }
  
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if (src == btnLuu) {
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
        btn.setPreferredSize(new Dimension(120, 40));
        return btn;
    }

    private JPanel createProductFormPanel() { 
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(isEditMode ? "Thông tin sản phẩm" : "Thêm sản phẩm mới"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel inputContainer = new JPanel(new GridBagLayout());
        inputContainer.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.weightx = 0.5;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        inputContainer.add(new JLabel("Mã Sản Phẩm:"), gbc);
        txtMaSP = new JTextField(isEditMode ? "" : "");
        txtMaSP.setEditable(!isEditMode); 
        txtMaSP.setBackground(isEditMode ? new Color(240, 240, 240) : Color.WHITE);
        gbc.gridx = 1; gbc.weightx = 0.5;
        inputContainer.add(txtMaSP, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        inputContainer.add(new JLabel("Tên món:"), gbc);
        txtTenSP = new JTextField(20);
        gbc.gridx = 3; gbc.weightx = 0.5;
        inputContainer.add(txtTenSP, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        inputContainer.add(new JLabel("Loại món:"), gbc);
        cboLoai = new JComboBox<>(new String[]{"Đồ uống", "Đồ ăn nhẹ"});
        cboLoai.setBackground(Color.WHITE);
        gbc.gridx = 1; gbc.weightx = 0.5;
        inputContainer.add(cboLoai, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0; 
        inputContainer.add(new JLabel("Giá Bán (VNĐ):"), gbc);
        txtGiaBan = new JTextField(20);
        gbc.gridx = 3; gbc.weightx = 0.5;
        inputContainer.add(txtGiaBan, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        inputContainer.add(new JLabel("Trạng thái:"), gbc);
        cboTrangThai = new JComboBox<>(new String[]{"Còn hàng", "Hết hàng"});
        cboTrangThai.setBackground(Color.WHITE);
        gbc.gridx = 1; gbc.weightx = 0.5;
        inputContainer.add(cboTrangThai, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        inputContainer.add(new JLabel("Mô tả:"), gbc);
        txtMoTa = new JTextField(20);
        gbc.gridx = 3; gbc.weightx = 0.5;
        inputContainer.add(txtMoTa, gbc);

        JPanel formContentPanel = new JPanel(new BorderLayout());
        formContentPanel.setBackground(Color.WHITE);
        formContentPanel.add(inputContainer, BorderLayout.CENTER);
        
        panel.add(formContentPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private void loadDataToForm(SanPham sp) {
        txtMaSP.setText(sp.getMaSanPham());
        txtTenSP.setText(sp.getTenSanPham());
        txtGiaBan.setText(String.valueOf(sp.getGiaBan()));
        txtMoTa.setText(sp.getMoTa() != null ? sp.getMoTa() : "");
        
        cboLoai.setSelectedItem(sp.getLoai());
        cboTrangThai.setSelectedItem(sp.getTrangThai());
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnHuy = createStyledButton("Hủy", new Color(220, 53, 69));
        btnXoaTrang = createStyledButton("Xóa Trắng", new Color(108, 117, 125));
        
        btnLuu = createStyledButton(isEditMode ? "Cập Nhật" : "Lưu", new Color(40, 167, 69));
        
        btnHuy.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnLuu.addActionListener(this);

        buttonPanel.add(btnHuy);
        if (!isEditMode) {
             buttonPanel.add(btnXoaTrang); 
        }
        buttonPanel.add(btnLuu);
        return buttonPanel;
    }
    
    private void xuLyXoaTrangForm() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtGiaBan.setText("");
        txtMoTa.setText("");
        cboLoai.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        
        txtMaSP.requestFocusInWindow();
    }
    
    private void xuLyLuuDuLieu() {
        String ma = txtMaSP.getText().trim();
        String ten = txtTenSP.getText().trim();
        String giaStr = txtGiaBan.getText().trim();
        String moTa = txtMoTa.getText().trim();
        String loai = cboLoai.getSelectedItem().toString();
        String trangThai = cboTrangThai.getSelectedItem().toString();

        if (ma.isEmpty() || ten.isEmpty() || giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã, Tên và Giá bán!");
            return;
        }

        try {
            double giaBan = Double.parseDouble(giaStr);
            if (giaBan <= 0) {
                 JOptionPane.showMessageDialog(this, "Giá bán phải lớn hơn 0!");
                 return;
            }
            
            SanPham spMoi = new SanPham(ma, ten, giaBan, loai, moTa.isEmpty() ? null : moTa, trangThai);
            boolean success = false;
            
            if (isEditMode) {
                success = sanPhamDAO.capNhatSanPham(spMoi);
            } else {
                success = sanPhamDAO.themSanPham(spMoi);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Cập nhật" : "Thêm") + " sản phẩm thành công!");
                this.isSuccess = true;
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, (isEditMode ? "Cập nhật" : "Thêm") + " thất bại! (Mã SP có thể đã tồn tại)");
            }
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Giá bán phải là số hợp lệ!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi CSDL khi lưu dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }

}