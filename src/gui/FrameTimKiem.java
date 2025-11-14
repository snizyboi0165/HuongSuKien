//Người làm: Nguyễn Cao Việt An
package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import dao.KhachHang_DAO;
import dao.HoaDon_DAO;
import entity.KhachHang;

public class FrameTimKiem extends JInternalFrame implements ActionListener {
    private JTextField txtMa;
    private JTextField txtHoTen;
    private JTextField txtSDT;
    private JTextField txtSanPham;
    private JCheckBox chkKhachHang;
    private JCheckBox chkHoaDon;
    private JButton btnSearch;
    private JButton btnLamMoi; 
	private JPanel pnlMain;
	private JLabel lblTitle;
	private JPanel btnPanel;
    private JTabbedPane tabbedResults;
    private DefaultTableModel modelKhachHang;
    private DefaultTableModel modelHoaDon;

    private KhachHang_DAO khachHangDAO;
    private HoaDon_DAO hoaDonDAO;


    public FrameTimKiem() {
        setTitle("Tìm Kiếm");
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setIconifiable(true);

        khachHangDAO = new KhachHang_DAO();
        hoaDonDAO = new HoaDon_DAO();

        pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        lblTitle = new JLabel("TÌM KIẾM NÂNG CAO", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.ITALIC, 24));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(139, 69, 19)); 
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setPreferredSize(new Dimension(0, 50));
        
        pnlMain.add(createSearchFormPanel(), BorderLayout.NORTH);
        pnlMain.add(createResultsTabbedPane(), BorderLayout.CENTER);

        btnSearch.addActionListener(this);
        btnLamMoi.addActionListener(this);

        this.getRootPane().setDefaultButton(btnSearch);
        
        setLayout(new BorderLayout());
        add(lblTitle, BorderLayout.NORTH);
        add(pnlMain, BorderLayout.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if (src == btnSearch) {
            handleSearch();
        } else if (src == btnLamMoi) {
        	lamMoi();
        }
    }
    

    private JPanel createSearchFormPanel() {
        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        TitledBorder mainBorder = BorderFactory.createTitledBorder("Nhập thông tin tìm kiếm");
        mainBorder.setTitleFont(labelFont);
        pnlForm.setBorder(mainBorder);

        txtMa = new JTextField();
        pnlForm.add(createFieldRow("Mã:", txtMa));
        pnlForm.add(Box.createVerticalStrut(10)); 

        txtHoTen = new JTextField();
        pnlForm.add(createFieldRow("Họ Tên:", txtHoTen));
        pnlForm.add(Box.createVerticalStrut(10));
        
        txtSDT = new JTextField();
        pnlForm.add(createFieldRow("Số điện thoại:", txtSDT));
        pnlForm.add(Box.createVerticalStrut(10));

        txtSanPham = new JTextField();
        pnlForm.add(createFieldRow("Sản phẩm đã mua:", txtSanPham));
        pnlForm.add(Box.createVerticalStrut(10));

        JPanel pnlCheck = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        TitledBorder checkBorder = BorderFactory.createTitledBorder("Tìm trong các mục");
        checkBorder.setTitleFont(labelFont); 
        pnlCheck.setBorder(checkBorder);

        chkKhachHang = new JCheckBox("Khách hàng", true);
        chkKhachHang.setFont(labelFont);
        
        chkHoaDon = new JCheckBox("Hóa đơn", true);
        chkHoaDon.setFont(labelFont);
        
        pnlCheck.add(chkKhachHang);
        pnlCheck.add(chkHoaDon);

        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        btnSearch = new JButton("Tìm Kiếm");
        btnSearch.setIcon(createIcon("/images/search.png", 20, 20));
        
        btnLamMoi = new JButton("Làm Mới");
        btnLamMoi.setIcon(createIcon("/images/refresh.png", 20, 20));
        
        btnPanel.add(btnSearch);
        btnPanel.add(btnLamMoi);

        pnlForm.add(pnlCheck);
        pnlForm.add(Box.createVerticalStrut(10));
        pnlForm.add(btnPanel);

        return pnlForm;
    }

    private Box createFieldRow(String labelText, JComponent component) {
        Box box = Box.createHorizontalBox(); 
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setPreferredSize(new Dimension(150, 30));
        box.add(label);
        box.add(Box.createHorizontalStrut(5));
        box.add(component);
        return box;
    }

    private JTabbedPane createResultsTabbedPane() {
        tabbedResults = new JTabbedPane();
        tabbedResults.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        String[] colsKH = {"Mã Khách Hàng", "Họ Tên", "Số Điện Thoại", "Điểm tích lũy"};
        modelKhachHang = new DefaultTableModel(colsKH, 0);
        tabbedResults.addTab("Khách hàng (0)", new JScrollPane(new JTable(modelKhachHang)));

        String[] colsHD = {"Mã Hoá Đơn", "Ngày Lập", "Nhân Viên", "Khách Hàng", "Sản phẩm", "Tổng Tiền"};
        modelHoaDon = new DefaultTableModel(colsHD, 0);
        tabbedResults.addTab("Hóa đơn (0)", new JScrollPane(new JTable(modelHoaDon)));
        
        return tabbedResults;
    }
    
    private void lamMoi() {
        txtMa.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtSanPham.setText("");
        
        modelKhachHang.setRowCount(0);
        modelHoaDon.setRowCount(0);
        tabbedResults.setTitleAt(0, "Khách hàng (0)");
        tabbedResults.setTitleAt(1, "Hóa đơn (0)");
        
        txtMa.requestFocusInWindow();
    }

    private void handleSearch() {
        String ma = txtMa.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String sanPham = txtSanPham.getText().trim();

        boolean timKH = chkKhachHang.isSelected();
        boolean timHD = chkHoaDon.isSelected();

        if (ma.isEmpty() && hoTen.isEmpty() && sdt.isEmpty() && sanPham.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ít nhất một thông tin tìm kiếm.");
            return;
        }
        if (!timKH && !timHD) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một mục (Khách hàng, Hóa đơn) để tìm.");
            return;
        }
        
        modelKhachHang.setRowCount(0);
        modelHoaDon.setRowCount(0);

        int totalResults = 0;
        
        try {
            if (timKH) {
                List<KhachHang> dsKH = khachHangDAO.timKiemNangCao(ma, hoTen, sdt);
                for (KhachHang kh : dsKH) {
                    modelKhachHang.addRow(new Object[]{
                        kh.getMaKhachHang(),
                        kh.getTenKhachHang(),
                        kh.getSoDienThoai(),
                        kh.getDiemTichLuy()
                    });
                }
                tabbedResults.setTitleAt(0, "Khách hàng (" + dsKH.size() + ")");
                totalResults += dsKH.size();
            }

            if (timHD) {
            	List<Object[]> dsHD = hoaDonDAO.timKiemNangCao(ma, hoTen, sanPham);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                
                for (Object[] row : dsHD) {
                    String ngayTaoFormatted = dateFormat.format((Timestamp) row[1]);
                    
                    modelHoaDon.addRow(new Object[]{
                        row[0],
                        ngayTaoFormatted,
                        row[2],
                        row[3],
                        row[4] != null ? row[4] : "N/A",
                        row[5]
                    });
                }
                tabbedResults.setTitleAt(1, "Hóa đơn (" + dsHD.size() + ")");
                totalResults += dsHD.size();
            }

            if (totalResults == 0) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả nào phù hợp.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tìm kiếm trong CSDL: " + e.getMessage());
        }
    }
    
    private ImageIcon createIcon(String path, int width, int height) {
        URL url = getClass().getResource(path);
        if (url != null) {
            return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }
        System.err.println("Không tìm thấy icon: " + path);
        return null;
    }
}