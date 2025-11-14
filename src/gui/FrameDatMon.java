//Người làm: Nguyễn Thành Đạt
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
import dao.HoaDon_DAO;

import entity.Ban;
import entity.HoaDon;

public class FrameDatMon extends JInternalFrame implements ActionListener{

    private JPanel pnlTables;
    private JTextField txtBanSo;
    private Ban_DAO ban_DAO;
    private HoaDon_DAO hoaDonDAO;

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
	private JPanel pnlFilter;
	private JLabel lblTitle;
	private JPanel pnlNorth;
	private JLabel lblThongBao;
	private JPanel table;
	private JLabel iconLabel;
	private JLabel infoLabel;
	private JButton btnThemMon;
	private JButton btnTim;
	private JButton btnLamMoi;


    public FrameDatMon() {
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setIconifiable(true);
        setTitle("Gọi Món");
        setLayout(new BorderLayout());
        
        ban_DAO = new Ban_DAO();
        hoaDonDAO = new HoaDon_DAO();

        pnlLeft = new JPanel();
        pnlLeft.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); 
        pnlLeft.setBorder(new EmptyBorder(10,10,10,10));
        pnlLeft.setBackground(new Color(224,224,224));
        pnlLeft.setPreferredSize(new Dimension(200, 0)); 
        
        btnThemMon = new JButton("Đặt món");
        btnThemMon.addActionListener(this);
        btnThemMon.setForeground(Color.white);
        btnThemMon.setBackground(new Color(139, 69, 19));
        btnThemMon.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThemMon.setFocusPainted(false);
        btnThemMon.setPreferredSize(new Dimension(180, 50));
        btnThemMon.setEnabled(false);
        pnlLeft.add(btnThemMon);

        add(pnlLeft, BorderLayout.WEST);

        pnlFilter = new JPanel();
        pnlFilter.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlFilter.setBackground(Color.white);
        pnlFilter.add(new JLabel("Bàn số"));
        txtBanSo = new JTextField(6);
        pnlFilter.add(txtBanSo);
        btnTim = new JButton("Tìm");
        btnLamMoi = new JButton("Làm mới");
        pnlFilter.add(btnTim);
        pnlFilter.add(btnLamMoi);

        lblTitle = new JLabel("CHỌN BÀN ĐỂ THÊM MÓN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.ITALIC, 24));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(139, 69, 19)); 
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setPreferredSize(new Dimension(0, 50)); 

        pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.add(lblTitle, BorderLayout.NORTH);
        pnlNorth.add(pnlFilter, BorderLayout.SOUTH);

        add(pnlNorth, BorderLayout.NORTH);

        pnlTables = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        pnlTables.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlTables.setBackground(new Color(255,255,255));
        
        loadTablesDangDung(); 
        
        add(new JScrollPane(pnlTables), BorderLayout.CENTER);
        
        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);
    }

    private static ImageIcon getTableIcon(String trangThai) {
        String path = "/images/using_table.png";
        try {
            ImageIcon rawIcon = new ImageIcon(FrameDatMon.class.getResource(path));
            Image scaledImg = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + path);
            return new ImageIcon();
        }
    }

    private void loadTablesDangDung() {
        pnlTables.removeAll();
        danhSachBanUI.clear();
        selectedBanUI = null;
        btnThemMon.setEnabled(false);

        try {
            List<Ban> dsBanEntities = ban_DAO.getBanTheoTrangThai("Đang dùng");

            if (dsBanEntities.isEmpty()) {
                pnlTables.setLayout(new GridBagLayout()); 
                lblThongBao = new JLabel("Hiện không có bàn nào đang dùng.");
                lblThongBao.setFont(new Font("Segoe UI", Font.ITALIC, 18));
                lblThongBao.setForeground(Color.GRAY);
                pnlTables.add(lblThongBao);
            } else {
                pnlTables.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20)); 
                
                for (Ban ban : dsBanEntities) {
                    table = new JPanel(new BorderLayout());
                    table.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                    table.setBackground(new Color(240, 255, 255));
                    table.setPreferredSize(new Dimension(150, 160)); 

                    ImageIcon image = getTableIcon(ban.getTrangThai());
                    iconLabel = new JLabel(image, SwingConstants.CENTER); 
                    infoLabel = new JLabel(ban.getTenBan() + " - " + ban.getTrangThai(), SwingConstants.CENTER);
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
        selectedBanUI.panel.setBackground(new Color(255, 230, 230));
        btnThemMon.setEnabled(true);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if (src == btnTim) {
            filterTables();
        } else if (src == btnLamMoi) {
            txtBanSo.setText("");
            filterTables();
        } else if (src == btnThemMon) {
            xuLyThemMon();
        }
    }
    
    private void filterTables() {
        String banSoInput = txtBanSo.getText().trim(); 

        String tenBanCanTim = banSoInput;
        if (banSoInput.matches("\\d+")) { 
            tenBanCanTim = "Bàn " + banSoInput;
        }

        for (BanUI item : danhSachBanUI) {
            boolean nameMatch = banSoInput.isEmpty() || item.ban.getTenBan().equalsIgnoreCase(tenBanCanTim);
            item.panel.setVisible(nameMatch); 
        }
        
        pnlTables.revalidate();
        pnlTables.repaint();
    }
    
    private void xuLyThemMon() {
        if (selectedBanUI == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bàn để thêm món.");
            return;
        }
        
        Ban banChon = selectedBanUI.ban;
        
        try {
            HoaDon hoaDon = hoaDonDAO.findActiveByMaBan(banChon.getMaBan());
            
            if (hoaDon == null) {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi: Không tìm thấy hóa đơn 'Chưa thanh toán' nào cho " + banChon.getTenBan());
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