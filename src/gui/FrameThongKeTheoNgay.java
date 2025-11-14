//Người làm: Ngô Văn Thành
package gui;

import javax.swing.*; 
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import dao.HoaDon_DAO;

public class FrameThongKeTheoNgay extends JInternalFrame implements ActionListener {
	private JLabel lblTitle;
	private JLabel lblChonNgayThang;
	private JLabel lblTongDoanhThu;
	private JLabel lblTongDoanhThuSauKM;
	private JLabel lblDoanhThuTB;
    private JTextField txtTongDoanhThu;
    private JTextField txtTongDoanhThuSauKM;
    private JTextField txtDoanhThuTB;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel northPanel;
    private JPanel rightPanel;
    private JDateChooser dateChooser;
    private DefaultTableModel model;
	private JPanel row1;
	private JPanel row2;
	private JPanel row3;
	private JPanel row4;
    
    private HoaDon_DAO hoaDonDAO;
    private DecimalFormat df = new DecimalFormat("#,##0 VNĐ");
    
	public FrameThongKeTheoNgay() {
    	setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setIconifiable(true);
        setTitle("Thống kê theo Ngày");
        setLayout(new BorderLayout());
        
        hoaDonDAO = new HoaDon_DAO();

        int frameWidth = 1300;
        int frameHeight = 800;
        setSize(frameWidth, frameHeight);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.setBackground(new Color(50, 239, 242));
        lblTitle = new JLabel("THỐNG KÊ DOANH THU THEO NGÀY");
        Font fo = new Font("Arial", Font.ITALIC, 24);
        lblTitle.setFont(fo);
        lblTitle.setOpaque(true);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setPreferredSize(new Dimension(0, 50));
        lblTitle.setBackground(new Color(139, 69, 19));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        northPanel.add(lblTitle, BorderLayout.CENTER);
        mainPanel.add(northPanel, BorderLayout.NORTH);

        leftPanel = new JPanel();
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); 
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setPreferredSize(new Dimension((int)(frameWidth * 0.30), frameHeight));

		int labelWidth = 180;
		int labelHeight = 25;
		int fieldWidth = 150;
		int fieldHeight = 25;

		row1 = new JPanel(new BorderLayout());
		row1.setPreferredSize(new Dimension(300, 30));
		row1.setBackground(Color.WHITE);
        row1.setMaximumSize(new Dimension(400, 40));
		lblChonNgayThang = new JLabel("Chọn ngày tháng");
		lblChonNgayThang.setFont(new Font("Arial", Font.BOLD, 14));
		lblChonNgayThang.setPreferredSize(new Dimension(labelWidth, labelHeight));

		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setDate(new Date());
		dateChooser.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
		dateChooser.getJCalendar().setWeekOfYearVisible(false);
		row1.setPreferredSize(new Dimension(labelWidth + 200, labelHeight));
		row1.add(lblChonNgayThang, BorderLayout.WEST);
		row1.add(dateChooser, BorderLayout.EAST);
		leftPanel.add(row1);
        
        // Bỏ btnXemBaoCao
        // Thêm PropertyChangeListener để tự động tải báo cáo
        dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName()) && evt.getNewValue() != null) {
                    loadThongKe();
                }
            }
        });


		row2 = new JPanel(new BorderLayout());
		row2.setBackground(Color.WHITE);
        row2.setMaximumSize(new Dimension(400, 40));
		row2.setPreferredSize(new Dimension(labelWidth + 200, labelHeight));
		lblTongDoanhThu = new JLabel("Tổng tiền hàng:");
		lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 14));
		lblTongDoanhThu.setPreferredSize(new Dimension(labelWidth, labelHeight));

		txtTongDoanhThu = new JTextField(15);
		txtTongDoanhThu.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
		txtTongDoanhThu.setEditable(false);

		row2.add(lblTongDoanhThu, BorderLayout.WEST);
		row2.add(txtTongDoanhThu, BorderLayout.EAST);
		leftPanel.add(row2);

		row3 = new JPanel(new BorderLayout());
		row3.setPreferredSize(new Dimension(labelWidth + 200, labelHeight));
		row3.setBackground(Color.WHITE);
        row3.setMaximumSize(new Dimension(400, 40));
		lblTongDoanhThuSauKM = new JLabel("Tổng thanh toán:");
		lblTongDoanhThuSauKM.setFont(new Font("Arial", Font.BOLD, 14));
		lblTongDoanhThuSauKM.setPreferredSize(new Dimension(labelWidth, labelHeight));

		txtTongDoanhThuSauKM = new JTextField(15);
		txtTongDoanhThuSauKM.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
		txtTongDoanhThuSauKM.setEditable(false);

		row3.add(lblTongDoanhThuSauKM, BorderLayout.WEST);
		row3.add(txtTongDoanhThuSauKM, BorderLayout.EAST);
		leftPanel.add(row3);

		row4 = new JPanel(new BorderLayout());
		row4.setPreferredSize(new Dimension(labelWidth + 200, labelHeight));
		row4.setBackground(Color.WHITE);
        row4.setMaximumSize(new Dimension(400, 40));
		lblDoanhThuTB = new JLabel("Số lượng hóa đơn:");
		lblDoanhThuTB.setFont(new Font("Arial", Font.BOLD, 14));
		lblDoanhThuTB.setPreferredSize(new Dimension(labelWidth, labelHeight));

		txtDoanhThuTB = new JTextField(15);
		txtDoanhThuTB.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
		txtDoanhThuTB.setEditable(false);

		row4.add(lblDoanhThuTB, BorderLayout.WEST);
		row4.add(txtDoanhThuTB, BorderLayout.EAST);
		leftPanel.add(row4);

		mainPanel.add(leftPanel, BorderLayout.WEST);
        
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension((int)(frameWidth * 0.65), frameHeight));
        rightPanel.setBackground(Color.WHITE);

        String[] columnNames = {"Mã HĐ", "Ngày Lập", "Mã KH", "Tên KH", "Tổng Tiền"};
        Object[][] data = {};

        model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(173, 216, 230));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 12));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension((int)(frameWidth * 0.70) - 40, frameHeight - 100));
        rightPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
        
        loadThongKe(); // Tải báo cáo lần đầu
	}
    
    private void loadThongKe() {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            txtTongDoanhThu.setText(df.format(0));
            txtTongDoanhThuSauKM.setText(df.format(0));
            txtDoanhThuTB.setText("0");
            model.setRowCount(0);
            return;
        }

        LocalDateTime startOfDay = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        
        model.setRowCount(0);
        double tongTienHang = 0;
        double tongThanhToan = 0;
        
        try {
            // SỬA: Giả định hàm layChiTietDoanhThuTheoKhoangNgay tồn tại trong HoaDon_DAO
            List<Object[]> dsHD = hoaDonDAO.layDoanhThuChiTietTheoKhoangThoiGian(startOfDay, endOfDay);
            SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

            for(Object[] row : dsHD) {
                String maHD = (String) row[0];
                LocalDateTime ngayTao = ((java.sql.Timestamp) row[1]).toLocalDateTime();
                String maKH = (String) row[2];
                String tenKH = (String) row[3];
                double tongTien = (double) row[4];
                
                model.addRow(new Object[]{
                    maHD,
                    timeFmt.format(Date.from(ngayTao.atZone(ZoneId.systemDefault()).toInstant())),
                    maKH,
                    tenKH,
                    df.format(tongTien)
                });
                
                tongThanhToan += tongTien;
                tongTienHang += tongTien; 
            }
            
            txtTongDoanhThu.setText(df.format(tongTienHang));
            txtTongDoanhThuSauKM.setText(df.format(tongThanhToan));
            txtDoanhThuTB.setText(String.valueOf(dsHD.size()));

        } catch (SQLException e) {
             JOptionPane.showMessageDialog(this, "Lỗi truy vấn CSDL: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Đã xóa logic btnXemBaoCao
    }
}