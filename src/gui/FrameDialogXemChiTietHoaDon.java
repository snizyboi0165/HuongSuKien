//Người làm: Ngô Văn Thành
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog; 
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.KhuyenMai_DAO;
import dao.NhanVien_DAO;
import dao.Thue_DAO;

import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.Thue;

public class FrameDialogXemChiTietHoaDon extends JDialog {

	private JPanel pnlInfo;
	private JTextField txtMaHD;
	private JTextField txtTrangThai;
	private JTextField txtTenNV; 
	private JTextField txtTenBan;
	private JTextField txtTenKH; 
	private JTextField txtNgayLapHD;
	private JTextField txtTenThue; 
	private JTextField txtTenKM; 
    private JTable tblChiTiet;
    private DefaultTableModel modelChiTiet;
    private JLabel lblTongTienHang;
    private JLabel lblTienThue;
    private JLabel lblTienGiam;
    private JLabel lblTongThanhToan;
    
    private HoaDon_DAO hoaDonDAO;
    private KhachHang_DAO khachHangDAO;
    private NhanVien_DAO nhanVienDAO;
    private ChiTietHoaDon_DAO ctHoaDonDAO;
    private KhuyenMai_DAO khuyenMaiDAO;
    private Thue_DAO thueDAO;
    
    private final DecimalFormat df = new DecimalFormat("#,##0 VNĐ");


    public FrameDialogXemChiTietHoaDon(Frame owner, String maHoaDon) { 
        super(owner, "CHI TIẾT HÓA ĐƠN: " + maHoaDon, true); 
        
        hoaDonDAO = new HoaDon_DAO();
        nhanVienDAO = new NhanVien_DAO();
        khachHangDAO = new KhachHang_DAO();
        ctHoaDonDAO = new ChiTietHoaDon_DAO();
        khuyenMaiDAO = new KhuyenMai_DAO();
        thueDAO = new Thue_DAO();
        
        setSize(1300, 700);
        setLocationRelativeTo(owner); 
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.add(createInfoPanel(), BorderLayout.NORTH);
        pnlNorth.add(createSummaryPanel(), BorderLayout.CENTER);
        
        add(pnlNorth, BorderLayout.NORTH);
        add(createChiTietTable(), BorderLayout.CENTER);

        loadData(maHoaDon);
    }
    
    private JPanel createInfoPanel() {
        pnlInfo = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlInfo.setBorder(BorderFactory.createTitledBorder("Thông tin chung"));

        pnlInfo.add(new JLabel("Mã hóa đơn:"));
        txtMaHD = createReadOnlyField();
        pnlInfo.add(txtMaHD);

        pnlInfo.add(new JLabel("Ngày Lập hoá đơn:"));
        txtNgayLapHD = createReadOnlyField();
        pnlInfo.add(txtNgayLapHD);

        pnlInfo.add(new JLabel("Trạng thái:"));
        txtTrangThai = createReadOnlyField();
        pnlInfo.add(txtTrangThai);
        
        pnlInfo.add(new JLabel("Tên bàn:"));
        txtTenBan = createReadOnlyField();
        pnlInfo.add(txtTenBan);
        
        pnlInfo.add(new JLabel("Tên nhân viên:")); 
        txtTenNV = createReadOnlyField();
        pnlInfo.add(txtTenNV);

        pnlInfo.add(new JLabel("Tên khách hàng:")); 
        txtTenKH = createReadOnlyField();
        pnlInfo.add(txtTenKH);

        pnlInfo.add(new JLabel("Thuế áp dụng:")); 
        txtTenThue = createReadOnlyField();
        pnlInfo.add(txtTenThue);

        pnlInfo.add(new JLabel("Khuyến mãi:")); 
        txtTenKM = createReadOnlyField();
        pnlInfo.add(txtTenKM);
        
        return pnlInfo;
    }
    
    private JTextField createReadOnlyField() {
        JTextField field = new JTextField();
        field.setEditable(false);
        field.setBackground(new Color(240, 240, 240));
        return field;
    }
    
    private JPanel createSummaryPanel() {
        JPanel pnlSummary = new JPanel(new GridLayout(1, 4, 10, 10));
        pnlSummary.setBorder(BorderFactory.createTitledBorder("Tổng kết"));
        
        lblTongTienHang = createSummaryLabel("Tổng tiền hàng:", Color.BLACK);
        lblTienThue = createSummaryLabel("Thuế:", Color.BLUE);
        lblTienGiam = createSummaryLabel("Giảm giá:", new Color(220, 53, 69));
        lblTongThanhToan = createSummaryLabel("Tổng thanh toán:", Color.RED);
        lblTongThanhToan.setFont(new Font("Arial", Font.BOLD, 16));
        
        pnlSummary.add(lblTongTienHang);
        pnlSummary.add(lblTienThue);
        pnlSummary.add(lblTienGiam);
        pnlSummary.add(lblTongThanhToan);
        
        return pnlSummary;
    }
    
    private JLabel createSummaryLabel(String text, Color color) {
        JLabel label = new JLabel(text + " 0 VNĐ", SwingConstants.CENTER);
        label.setForeground(color);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return label;
    }
    
    private JScrollPane createChiTietTable() {
        String[] cols = {"Món", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        modelChiTiet = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblChiTiet = new JTable(modelChiTiet);
        tblChiTiet.setRowHeight(25);
        tblChiTiet.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblChiTiet.setFont(new Font("Arial", Font.PLAIN, 14));
        
        tblChiTiet.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
             {setHorizontalAlignment(SwingConstants.RIGHT);}
        });
        tblChiTiet.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
             {setHorizontalAlignment(SwingConstants.RIGHT);}
        });
        
        JScrollPane scroll = new JScrollPane(tblChiTiet);
        scroll.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm đã đặt"));
        return scroll;
    }
    
    private void loadData(String maHoaDon) {
        try {
            HoaDon hd = hoaDonDAO.findHoaDonByID(maHoaDon); 
            if (hd == null) {
                 throw new Exception("Không tìm thấy hóa đơn có mã: " + maHoaDon);
            }
            
            String tenNV = nhanVienDAO.getTenNhanVien(hd.getNhanVien().getMaNhanVien());
            String tenKH = "Khách vãng lai";
            if (hd.getKhachHang() != null && hd.getKhachHang().getMaKhachHang() != null) {
                List<KhachHang> khList = khachHangDAO.timKiemNangCao(hd.getKhachHang().getMaKhachHang(), "", "");
                if (khList != null && !khList.isEmpty()) {
                    tenKH = khList.get(0).getTenKhachHang();
                }
            }
            
            Thue thue = null;
            if (hd.getThue() != null && hd.getThue().getMaThue() != null) {
                 thue = thueDAO.getThueByID(hd.getThue().getMaThue()); 
            }
            KhuyenMai km = null;
            if (hd.getKhuyenMai() != null && hd.getKhuyenMai().getMaKhuyenMai() != null) {
                 km = khuyenMaiDAO.getKhuyenMaiByID(hd.getKhuyenMai().getMaKhuyenMai()); 
            }
            
            SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            txtMaHD.setText(hd.getMaHoaDon());
            txtTrangThai.setText(hd.getTrangThai());
            txtTenBan.setText(hd.getBan().getMaBan());
            txtNgayLapHD.setText(dateFmt.format(Date.from(hd.getNgayTao().atZone(java.time.ZoneId.systemDefault()).toInstant())));
            txtTenNV.setText(tenNV);
            txtTenKH.setText(tenKH);
            txtTenThue.setText(thue != null ? thue.getTenThue() + " (" + (int)(thue.getTyLeThue()*100) + "%)" : "Không");
            txtTenKM.setText(km != null ? km.getTenKhuyenMai() + " (" + (int)(km.getPhanTramGiam()*100) + "%)" : "Không");

            List<Object[]> chiTietList = ctHoaDonDAO.findAllChiTietByMaHoaDon(maHoaDon);
            double tongTienHang = 0;
            modelChiTiet.setRowCount(0);
            
            for (Object[] item : chiTietList) {
                String tenSP = (String) item[0];
                int soLuong = (int) item[1];
                double donGia = (double) item[2];
                double thanhTien = soLuong * donGia;
                
                modelChiTiet.addRow(new Object[] { 
                    tenSP, soLuong, df.format(donGia).replace(" VNĐ", ""), df.format(thanhTien).replace(" VNĐ", "")
                });
                tongTienHang += thanhTien;
            }
            
            double tienThue = 0;
            if (thue != null) {
                 tienThue = tongTienHang * thue.getTyLeThue();
            }
            double tienGiam = 0;
            if (km != null) {
                 tienGiam = tongTienHang * km.getPhanTramGiam();
            }
            double tongThanhToan = tongTienHang + tienThue - tienGiam;
            if (tongThanhToan < 0) tongThanhToan = 0;
            
            lblTongTienHang.setText("Tổng tiền hàng: " + df.format(tongTienHang));
            lblTienThue.setText("Thuế: " + df.format(tienThue));
            lblTienGiam.setText("Giảm giá: " + df.format(tienGiam));
            lblTongThanhToan.setText("Tổng thanh toán: " + df.format(tongThanhToan));

        } catch (Exception e) {
             e.printStackTrace();
             JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu chi tiết hóa đơn: " + e.getMessage(), "Lỗi Dữ Liệu", JOptionPane.ERROR_MESSAGE);
             this.dispose();
        }
    }
}