//Người làm: Nguyễn Cao Việt An
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Ban_DAO;

import entity.Ban;

public class FrameThanhToan extends JInternalFrame implements ActionListener {
    private JPanel pnlTables;
    private JButton btnThanhToan;
    private Ban_DAO ban_DAO;
    private FrameGiaoDienChinh mainFrame;

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
    
    private List<BanUI> danhSachBanUI = new ArrayList<>();
    private BanUI selectedBanUI = null;
	private JPanel pnlLeft;
	private JLabel lblTitle;

    public FrameThanhToan(FrameGiaoDienChinh parent) {
        this.mainFrame = parent;
    	
        setTitle("Thanh Toán Hóa Đơn");
    	setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setIconifiable(true);
        setLayout(new BorderLayout());
        
        ban_DAO = new Ban_DAO();

        pnlLeft = new JPanel();
        pnlLeft.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlLeft.setBackground(new Color(224, 224, 224));
        pnlLeft.setPreferredSize(new Dimension(200, 0));
        
        btnThanhToan = new JButton("Thanh Toán Bàn");
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThanhToan.setFocusPainted(false);
        btnThanhToan.addActionListener(this);
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setBackground(new Color(139, 69, 19));
        btnThanhToan.setPreferredSize(new Dimension(180, 50));
        btnThanhToan.setEnabled(false);
        pnlLeft.add(btnThanhToan);

        add(pnlLeft, BorderLayout.WEST);

        lblTitle = new JLabel("CHỌN BÀN CẦN THANH TOÁN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.ITALIC, 24));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(139, 69, 19)); 
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setPreferredSize(new Dimension(0, 50));
        add(lblTitle, BorderLayout.NORTH);

        pnlTables = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        pnlTables.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlTables.setBackground(Color.WHITE);
        
        loadTablesDangDung(); 
        
        add(new JScrollPane(pnlTables), BorderLayout.CENTER);
    }
    
    public void loadTablesDangDung() {
        pnlTables.removeAll();
        danhSachBanUI.clear();
        selectedBanUI = null;
        btnThanhToan.setEnabled(false);

        try {
            List<Ban> dsBanEntities = ban_DAO.getBanTheoTrangThai("Đang dùng");

            if (dsBanEntities.isEmpty()) {
                pnlTables.setLayout(new GridBagLayout());
                JLabel lblThongBao = new JLabel("Hiện không có bàn nào đang dùng.");
                lblThongBao.setFont(new Font("Segoe UI", Font.ITALIC, 18));
                lblThongBao.setForeground(Color.GRAY);
                pnlTables.add(lblThongBao);
            } else {
                pnlTables.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
                
                for (Ban ban : dsBanEntities) {
                    JPanel table = new JPanel(new BorderLayout());
                    table.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                    table.setBackground(new Color(240, 255, 255));
                    table.setPreferredSize(new Dimension(150, 160));

                    ImageIcon image = getTableIcon(ban.getTrangThai());
                    JLabel iconLabel = new JLabel(image, SwingConstants.CENTER); 
                    JLabel infoLabel = new JLabel(ban.getTenBan() + " - " + ban.getTrangThai(), SwingConstants.CENTER);
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
                    danhSachBanUI.add(banUI);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách bàn từ CSDL!");
        }
        
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
        selectedBanUI.panel.setBackground(new Color(230, 240, 255));
        btnThanhToan.setEnabled(true);
    }

    private static ImageIcon getTableIcon(String trangThai) {
        String path = (trangThai.equals("Đang dùng")) ? "/images/using_table.png" : "/images/a.png";
        try {
            ImageIcon rawIcon = new ImageIcon(FrameDatBan.class.getResource(path));
            Image scaledImg = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + path);
            return new ImageIcon();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThanhToan) {
            xuLyThanhToan();
        }
    }
    
    private void xuLyThanhToan() {
        if (selectedBanUI == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bàn để thanh toán.");
            return;
        }
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        FrameDialogChiTietThanhToan dialogCT = new FrameDialogChiTietThanhToan(parentFrame, selectedBanUI.ban);
        dialogCT.setVisible(true); 

        if (dialogCT.daThanhToanThanhCong()) {
            loadTablesDangDung();
            
            if (mainFrame != null) {
                mainFrame.refreshFrameDatBan();
            }
        }
    }
}