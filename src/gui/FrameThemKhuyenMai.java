//Người làm: Nguyễn Thành Đạt
package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;

public class FrameThemKhuyenMai extends JDialog implements ActionListener {
	
	private JPanel pnlMain;
	private JPanel pnlCenter;
    private JComboBox<String> cboTrangThaiTim;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtTenKM;
    private JTextField txtMaKM;
    private JTextField txtDieuKien;
    private JTextField txtNgayBatDau;
    private JTextField txtNgayKetThuc;
    private JTextField txtPhanTramGiam; 
    private JTextField txtMaKMTim;
    private JTextField txtTenKMTim;
    private JButton btnLamMoi;
    private JButton btnTim;
    private JButton btnThem;
    private JButton btnXoaTrang;
    private KhuyenMai_DAO khuyenMaiDAO;
    private List<KhuyenMai> allKhuyenMaiList;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    public FrameThemKhuyenMai(Frame owner) {
        super(owner, "Thêm Khuyến Mãi Mới", true); 
        setTitle("Thêm Khuyến Mãi Mới");
        setSize(1200, 800);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        
        khuyenMaiDAO = new KhuyenMai_DAO();
        allKhuyenMaiList = new ArrayList<>();


        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlMain.add(createFormPanel(), BorderLayout.NORTH);

        pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.add(createSearchPanel(), BorderLayout.NORTH);
        pnlCenter.add(createTablePanel(), BorderLayout.CENTER);

        pnlMain.add(pnlCenter, BorderLayout.CENTER);

        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoaTrang.addActionListener(this); 

        add(pnlMain);
        loadKhuyenMai(); 
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnLamMoi) {
            txtMaKMTim.setText("");
            txtTenKMTim.setText("");
            cboTrangThaiTim.setSelectedIndex(0);
            loadKhuyenMai();
        } else if (src == btnTim) {
            filterKhuyenMai();
        } else if (src == btnThem) {
            themKhuyenMai();
        } else if (src == btnXoaTrang) {
            xuLyXoaTrangForm();
        }
    }
    
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.white);
        btn.setBackground(new Color(139, 69, 19));
        return btn;
    }


    private JPanel createFormPanel() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd/MM/yyyy");
        
        String ngayBatDauMacDinh = sdfDateOnly.format(cal.getTime());
        
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        String ngayKetThucMacDinh = sdfDateOnly.format(cal.getTime());
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin khuyến mãi"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel inputContainer = new JPanel(new GridBagLayout());
        inputContainer.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        inputContainer.add(new JLabel("Mã khuyến mãi:"), gbc);
        txtMaKM = new JTextField(20);
        gbc.gridx = 1;
        inputContainer.add(txtMaKM, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputContainer.add(new JLabel("Ngày bắt đầu (dd/MM/yyyy):"), gbc);
        txtNgayBatDau = new JTextField(ngayBatDauMacDinh); 
        gbc.gridx = 1;
        inputContainer.add(txtNgayBatDau, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputContainer.add(new JLabel("Phần trăm giảm (%):"), gbc); 
        txtPhanTramGiam = new JTextField("0"); 
        gbc.gridx = 1;
        inputContainer.add(txtPhanTramGiam, gbc); 

        gbc.gridx = 2; gbc.gridy = 0;
        inputContainer.add(new JLabel("Tên khuyến mãi:"), gbc);
        txtTenKM = new JTextField(20);
        gbc.gridx = 3;
        inputContainer.add(txtTenKM, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        inputContainer.add(new JLabel("Ngày kết thúc (dd/MM/yyyy):"), gbc);
        txtNgayKetThuc = new JTextField(ngayKetThucMacDinh); 
        gbc.gridx = 3;
        inputContainer.add(txtNgayKetThuc, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        inputContainer.add(new JLabel("Điều kiện:"), gbc);
        txtDieuKien = new JTextField(20);
        gbc.gridx = 3;
        inputContainer.add(txtDieuKien, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnXoaTrang = createStyledButton("Xóa Trắng");
        btnThem = createStyledButton("Thêm");

        buttonPanel.add(btnXoaTrang);
        buttonPanel.add(btnThem);

        JPanel formContentPanel = new JPanel(new BorderLayout());
        formContentPanel.setBackground(Color.WHITE);
        formContentPanel.add(inputContainer, BorderLayout.CENTER);
        formContentPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(formContentPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private void xuLyXoaTrangForm() {
        txtMaKM.setText("");
        txtTenKM.setText("");
        txtDieuKien.setText("");
        txtPhanTramGiam.setText("0");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd/MM/yyyy");
        
        txtNgayBatDau.setText(sdfDateOnly.format(cal.getTime()));
        
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        txtNgayKetThuc.setText(sdfDateOnly.format(cal.getTime()));
    }


    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        panel.add(new JLabel("Mã khuyến mãi:"));
        txtMaKMTim = new JTextField(15);
        panel.add(txtMaKMTim);

        panel.add(new JLabel("Tên khuyến mãi:"));
        txtTenKMTim = new JTextField(15);
        panel.add(txtTenKMTim);

        panel.add(new JLabel("Trạng thái:"));
        cboTrangThaiTim = new JComboBox<>(new String[]{"Tất cả","Đang áp dụng", "Hết hạn", "Chưa áp dụng"});
        panel.add(cboTrangThaiTim);
        
        btnTim = new JButton("Tìm");
        btnLamMoi = new JButton("Làm mới");
        panel.add(btnTim);
        panel.add(btnLamMoi);

        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {
            "Mã khuyến mãi", "Tên khuyến mãi",
            "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái","Điều kiện"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        return new JScrollPane(table);
    }

    public void loadKhuyenMai() {
        tableModel.setRowCount(0);
        try {
            this.allKhuyenMaiList = khuyenMaiDAO.getAll();
            showKhuyenMaiOnTable(this.allKhuyenMaiList);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu khuyến mãi từ CSDL: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showKhuyenMaiOnTable(List<KhuyenMai> list) {
         tableModel.setRowCount(0);
         LocalDateTime now = LocalDateTime.now();
         
         for(KhuyenMai km : list) {
             String trangThai = getTrangThai(km.getNgayBatDau(), km.getNgayKetThuc(), now);
             
             tableModel.addRow(new Object[]{
                 km.getMaKhuyenMai(),
                 km.getTenKhuyenMai(),
                 km.getNgayBatDau() != null ? km.getNgayBatDau().format(dtf) : "N/A",
                 km.getNgayKetThuc() != null ? km.getNgayKetThuc().format(dtf) : "N/A",
                 trangThai,
                 km.getDieuKienApDung()
             });
         }
    }

    private String getTrangThai(LocalDateTime ngayBD, LocalDateTime ngayKT, LocalDateTime now) {
        if (ngayBD == null || ngayKT == null) return "Không xác định";
        
        if (now.isAfter(ngayKT)) {
            return "Hết hạn";
        } else if (now.isAfter(ngayBD) && now.isBefore(ngayKT)) {
            return "Đang áp dụng";
        } 
        return "Chưa áp dụng";
    }

    private void filterKhuyenMai() {
        String ma = txtMaKMTim.getText().trim().toLowerCase();
        String ten = txtTenKMTim.getText().trim().toLowerCase();
        String trangThaiChon = cboTrangThaiTim.getSelectedItem().toString();
        LocalDateTime now = LocalDateTime.now();

        List<KhuyenMai> filteredList = new java.util.ArrayList<>();
        
        for (KhuyenMai km : allKhuyenMaiList) {
            String maKM = km.getMaKhuyenMai().toLowerCase();
            String tenKM = km.getTenKhuyenMai().toLowerCase();
            String trangThaiThucTe = getTrangThai(km.getNgayBatDau(), km.getNgayKetThuc(), now);

            boolean matchMa = ma.isEmpty() || maKM.contains(ma);
            boolean matchTen = ten.isEmpty() || tenKM.contains(ten);
            boolean matchTT = trangThaiChon.equals("Tất cả") || trangThaiThucTe.equals(trangThaiChon);

            if (matchMa && matchTen && matchTT) {
                filteredList.add(km);
            }
        }
        
        showKhuyenMaiOnTable(filteredList);
    }
    
    private void themKhuyenMai() {
        String ma = txtMaKM.getText().trim();
        String ten = txtTenKM.getText().trim();
        String bdStr = txtNgayBatDau.getText().trim();
        String ktStr = txtNgayKetThuc.getText().trim();
        String dk = txtDieuKien.getText().trim();
        String phanTramStr = txtPhanTramGiam.getText().trim(); 

        if (ma.isEmpty() || ten.isEmpty() || bdStr.isEmpty() || ktStr.isEmpty() || phanTramStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        try {
            double phanTram = Double.parseDouble(phanTramStr) / 100.0;
            if (phanTram < 0 || phanTram > 1) {
                 JOptionPane.showMessageDialog(this, "Phần trăm giảm phải từ 0 đến 100!");
                 return;
            }
            
            Date dateBD = sdf.parse(bdStr);
            Date dateKT = sdf.parse(ktStr);
            
            LocalDateTime bd = dateBD.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime kt = dateKT.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(23).withMinute(59).withSecond(59);

            if (kt.isBefore(bd)) {
                 JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!");
                 return;
            }
            
            KhuyenMai kmMoi = new KhuyenMai(ma, ten, dk, phanTram, bd, kt); 
            
            boolean success = khuyenMaiDAO.themKhuyenMai(kmMoi); 
            
            if (success) {
                 JOptionPane.showMessageDialog(this, "Thêm thành công!");
                 loadKhuyenMai(); 
                 xuLyXoaTrangForm(); 
            } else {
                 JOptionPane.showMessageDialog(this, "Thêm thất bại (Mã KM đã tồn tại hoặc lỗi CSDL)!");
            }
            
        } catch (DateTimeParseException | ParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày hoặc phần trăm giảm không hợp lệ!");
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + e.getMessage());
             e.printStackTrace();
        }
    }
}