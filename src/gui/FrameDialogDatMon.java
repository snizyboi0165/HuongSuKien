//Người làm: Nguyễn Thành Đạt
package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import entity.SanPham;

import dao.SanPham_DAO; 
import dao.ChiTietHoaDon_DAO;
import dao.ChiTietDatBan_DAO;



public class FrameDialogDatMon extends JDialog implements ActionListener {

    private JPanel pnlItem;
    private List<SanPham> dsSanPhamFull;
	private JPanel pnlLeft;
	private JPanel pnlFilter;
	private JLabel lblTitle;
	private JPanel pnlNorth;
	private JPanel orderNote;
	private JLabel lblNote; 
    private JPanel pnlOrder;
    private JComboBox<String> cboTrangThai;
    private JTextField txtMon;
    private JButton btnTim; 
    private JButton btnLamMoi; 
    private JButton btnDatMon; 
    private JButton btnGiamSL; 
    private JButton btnXacNhan;
    private JTable tblSanPham;
    private JTable tblOrder;
    private DefaultTableModel modelItem;
    private DefaultTableModel modelOrder;
    private JTextArea txtGhiChu;
    
    private String currentMaHoaDon;
    private String currentMaDatBan;
    
    private SanPham_DAO sanPhamDAO; 
    private ChiTietHoaDon_DAO ctHoaDonDAO;
    private ChiTietDatBan_DAO ctDatBanDAO;
    
   
    public FrameDialogDatMon(Frame parent, String maHoaDon) {
        super(parent, "Gọi Món cho Hóa Đơn: " + maHoaDon, true); 
        this.currentMaHoaDon = maHoaDon;
        this.currentMaDatBan = null;
        commonInit();
    }
    
    public FrameDialogDatMon(Frame parent, String maDatBan, boolean isDatBan) {
        super(parent, "Đặt Món Trước cho Phiếu: " + maDatBan, true); 
        this.currentMaDatBan = maDatBan;
        this.currentMaHoaDon = null;
        commonInit();
    }
    
    private void commonInit() {
        setSize(1200, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        sanPhamDAO = new SanPham_DAO();
        ctHoaDonDAO = new ChiTietHoaDon_DAO();
        ctDatBanDAO = new ChiTietDatBan_DAO();
        dsSanPhamFull = new ArrayList<>();

        pnlLeft = new JPanel();
        pnlLeft.setLayout(new GridLayout(10, 1, 5, 5));
        pnlLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlLeft.setBackground(new Color(224, 224, 224));
        
        btnDatMon = createStyledButton("Đặt món (F4)");
        btnGiamSL = createStyledButton("Giảm số lượng (F5)");
        btnXacNhan = createStyledButton("Xác nhận (Enter)");
        
        pnlLeft.add(btnDatMon);
        pnlLeft.add(btnGiamSL);
        pnlLeft.add(btnXacNhan);
        
        getContentPane().add(pnlLeft, BorderLayout.WEST);

        pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlFilter.setBackground(Color.white);

        pnlFilter.add(new JLabel("Loại:"));
        cboTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đồ uống", "Đồ ăn nhẹ"}); 
        pnlFilter.add(cboTrangThai);

        pnlFilter.add(new JLabel("Nhập mã/tên SP:"));
        txtMon = new JTextField(15);
        pnlFilter.add(txtMon);

        btnTim = new JButton("Tìm");
        btnLamMoi = new JButton("Làm mới");
        pnlFilter.add(btnTim);
        pnlFilter.add(btnLamMoi);

        lblTitle = new JLabel("DANH SÁCH SẢN PHẨM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.ITALIC, 24));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(139, 69, 19));
        lblTitle.setForeground(Color.WHITE);

        pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.add(lblTitle, BorderLayout.NORTH);
        pnlNorth.add(pnlFilter, BorderLayout.SOUTH);
        getContentPane().add(pnlNorth, BorderLayout.NORTH);

        pnlItem = new JPanel(new BorderLayout());
        pnlItem.setBorder(BorderFactory.createTitledBorder("Chọn món"));
        pnlItem.setBackground(Color.white);

        modelItem = new DefaultTableModel(new Object[]{"Mã SP", "Tên SP", "Giá Bán", "Loại"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSanPham = new JTable(modelItem);
        setupTable(tblSanPham);

        JScrollPane scroll_Items = new JScrollPane(tblSanPham);
        pnlItem.add(scroll_Items, BorderLayout.CENTER);
        
        String orderTitle = (currentMaHoaDon != null) ? "Hóa đơn hiện tại" : "Các món đặt trước";
        
        pnlOrder = new JPanel(new BorderLayout());
        pnlOrder.setBorder(BorderFactory.createTitledBorder(orderTitle));
        pnlOrder.setBackground(Color.white);
        
        modelOrder = new DefaultTableModel(new Object[]{"Mã SP", "Tên SP", "Số Lượng", "Đơn Giá", "Thành Tiền"}, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblOrder = new JTable(modelOrder);
        setupTable(tblOrder);
        TableColumnModel tcmOrder = tblOrder.getColumnModel();
        tcmOrder.getColumn(0).setPreferredWidth(50);
        tcmOrder.getColumn(1).setPreferredWidth(200);
        tcmOrder.getColumn(2).setPreferredWidth(50);
        tcmOrder.getColumn(3).setPreferredWidth(80);
        tcmOrder.getColumn(4).setPreferredWidth(100);
        
        JScrollPane scroll_Order = new JScrollPane(tblOrder);
        
        orderNote = new JPanel(new BorderLayout());
        orderNote.setPreferredSize(new Dimension(250, 0));
        lblNote = new JLabel("Ghi chú (cho món đang chọn):");
        lblNote.setFont(new Font("Arial", Font.BOLD, 14));
        orderNote.add(lblNote, BorderLayout.NORTH);
        txtGhiChu = new JTextArea();
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        txtGhiChu.setFont(new Font("Arial", Font.PLAIN, 14));
        orderNote.add(new JScrollPane(txtGhiChu), BorderLayout.CENTER); 
        pnlOrder.add(orderNote, BorderLayout.EAST);
        pnlOrder.add(scroll_Order,BorderLayout.CENTER);
        
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlItem, pnlOrder);
        split.setDividerLocation(300);
        split.setResizeWeight(0.5);
        getContentPane().add(split, BorderLayout.CENTER);

        cboTrangThai.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnTim.addActionListener(this);
        btnDatMon.addActionListener(this);
        btnGiamSL.addActionListener(this);
        btnXacNhan.addActionListener(this);

        setupKeyBindings();
        loadAllItemsFromDB();
        showItemsOnTable(dsSanPhamFull);
    }
    
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.white);
        btn.setBackground(new Color(139, 69, 19));
        return btn;
    }
    private void setupTable(JTable table) {
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    private void setupKeyBindings() {
        InputMap inputMap = ((JPanel)getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = ((JPanel)getContentPane()).getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "datMon");
        actionMap.put("datMon", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { btnDatMon.doClick(); }
        });
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "giamSL");
        actionMap.put("giamSL", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { btnGiamSL.doClick(); }
        });
        this.getRootPane().setDefaultButton(btnXacNhan);
    }

    private void loadAllItemsFromDB() {
        try {
            this.dsSanPhamFull = sanPhamDAO.getSanPhamConHang();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách sản phẩm: " + e.getMessage(), "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
            this.dsSanPhamFull = new ArrayList<>();
        }
    }

    private void showItemsOnTable(List<SanPham> list) {
        modelItem.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,##0 VNĐ");
        if(list == null) return;
        
        for (SanPham sp : list) {
            modelItem.addRow(new Object[]{
                sp.getMaSanPham(), 
                sp.getTenSanPham(), 
                df.format(sp.getGiaBan()),
                sp.getLoai()
            });
        }
    }
    
    private void addOrder() {
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một món trong danh sách sản phẩm trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maSP = modelItem.getValueAt(selectedRow, 0).toString();

        SanPham spChon = null;
        for (SanPham sp : dsSanPhamFull) {
            if (sp.getMaSanPham().equals(maSP)) {
                spChon = sp;
                break;
            }
        }
        
        if (spChon == null) {
            JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy sản phẩm đã chọn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tenSP = spChon.getTenSanPham();
        double donGia = spChon.getGiaBan();
        DecimalFormat df = new DecimalFormat("#,##0 VNĐ");

        for (int i = 0; i < modelOrder.getRowCount(); i++) {
            if (modelOrder.getValueAt(i, 0).toString().equals(maSP)) {
                int soLuong = (int) modelOrder.getValueAt(i, 2);
                soLuong++;
                modelOrder.setValueAt(soLuong, i, 2);
                modelOrder.setValueAt(df.format(soLuong * donGia), i, 4);
                return;
            }
        }

        modelOrder.addRow(new Object[]{maSP, tenSP, 1, df.format(donGia), df.format(donGia)});
    }

    private void removeOrder() {
        int selectedRow = tblOrder.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món muốn giảm số lượng trong hóa đơn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maSP = modelOrder.getValueAt(selectedRow, 0).toString();
        int soLuong = (int) modelOrder.getValueAt(selectedRow, 2);
        
        double donGia = 0;
        for (SanPham sp : dsSanPhamFull) {
            if (sp.getMaSanPham().equals(maSP)) {
                donGia = sp.getGiaBan();
                break;
            }
        }
        
        if (soLuong > 1) {
            soLuong--;
            modelOrder.setValueAt(soLuong, selectedRow, 2);
            DecimalFormat df = new DecimalFormat("#,##0 VNĐ");
            modelOrder.setValueAt(df.format(soLuong * donGia), selectedRow, 4);
        } else {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Số lượng hiện tại là 1.\nBạn có muốn xóa món này khỏi danh sách order?",
                    "Xác nhận xóa món",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                modelOrder.removeRow(selectedRow);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if (src == cboTrangThai) {
            String loai = cboTrangThai.getSelectedItem().toString();
            if (loai.equalsIgnoreCase("Tất cả")) {
                showItemsOnTable(dsSanPhamFull);
            } else {
                List<SanPham> dsDaLoc = new ArrayList<>();
                for (SanPham sp : dsSanPhamFull) {
                    if (sp.getLoai().equalsIgnoreCase(loai)) {
                        dsDaLoc.add(sp);
                    }
                }
                showItemsOnTable(dsDaLoc);
            }
            
        } else if (src == btnLamMoi) {
            txtMon.setText("");
            cboTrangThai.setSelectedIndex(0);
            showItemsOnTable(dsSanPhamFull);
            
        } else if (src == btnTim) {
            String keyword = txtMon.getText().trim().toLowerCase();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hoặc tên sản phẩm cần tìm!");
                return;
            }
            List<SanPham> dsDaLoc = new ArrayList<>();
            for (SanPham sp : dsSanPhamFull) {
                if (sp.getMaSanPham().toLowerCase().contains(keyword) || 
                    sp.getTenSanPham().toLowerCase().contains(keyword)) {
                    dsDaLoc.add(sp);
                }
            }
            showItemsOnTable(dsDaLoc);
            if(dsDaLoc.isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào khớp: " + keyword);
            }
        }
        else if (e.getSource() == btnDatMon) {
            addOrder();
        }
        else if(e.getSource() == btnGiamSL) {
        	removeOrder();
        }
        else if(e.getSource() == btnXacNhan) {
            handleXacNhan();
        }
    }

    private void handleXacNhan() {
        if (modelOrder.getRowCount() == 0) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Danh sách đang trống. Bạn có chắc muốn đóng cửa sổ?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
            }
            return;
        }

        try {
            for (int i = 0; i < modelOrder.getRowCount(); i++) {
                String maSP = modelOrder.getValueAt(i, 0).toString();
                int soLuong = (int) modelOrder.getValueAt(i, 2);
                
                double donGia = 0;
                for (SanPham sp : dsSanPhamFull) {
                    if (sp.getMaSanPham().equals(maSP)) {
                        donGia = sp.getGiaBan();
                        break;
                    }
                }

				if (this.currentMaHoaDon != null) {
                	ctHoaDonDAO.themHoacCapNhatChiTiet(this.currentMaHoaDon, maSP, soLuong, donGia);
				} else if (this.currentMaDatBan != null) {
					ctDatBanDAO.themHoacCapNhatChiTiet(this.currentMaDatBan, maSP, soLuong, donGia);
				}
            }
            
            if (this.currentMaHoaDon != null) {
            	JOptionPane.showMessageDialog(this, "Đã cập nhật món thành công cho hóa đơn: " + this.currentMaHoaDon);
            } else if (this.currentMaDatBan != null) {
            	JOptionPane.showMessageDialog(this, "Đã đặt món trước thành công cho phiếu: " + this.currentMaDatBan);
            }
            this.dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu chi tiết: " + e.getMessage(), "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
        }
    }
}