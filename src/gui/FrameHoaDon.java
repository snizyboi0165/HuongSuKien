//Người làm: Ngô Văn Thành
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.HoaDon_DAO; 


public class FrameHoaDon extends JInternalFrame implements ActionListener, MouseListener{
	
	private JPanel pnlTim;
	private JPanel pnlTimKiem;
	private JPanel pnlLeft;
	private JButton btnLamMoi;
	private JButton btnXemChiTiet;
	private JTextField txtMaHD; 
	private JTable table;
	private JButton btnTim;
	private JLabel lblTitle;
    private DefaultTableModel model;
    
    private HoaDon_DAO hoaDonDAO;
    private DecimalFormat df = new DecimalFormat("#,##0");
	
    public FrameHoaDon() {
        setTitle("Danh sách hoá đơn");
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setIconifiable(true);
        setSize(1200, 700); 
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 255, 255));
        
        hoaDonDAO = new HoaDon_DAO();

        pnlTim = new JPanel();
        pnlTim.setPreferredSize(new Dimension(0, 80)); 
        lblTitle = new JLabel("DANH SÁCH HOÁ ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.ITALIC, 24));
        lblTitle.setPreferredSize(new Dimension(0, 50));
        lblTitle.setBackground(new Color(139, 69, 19));
        lblTitle.setOpaque(true);
        lblTitle.setForeground(new Color(255, 255, 255));
        
        pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlTimKiem.setBackground(Color.WHITE);
        
        pnlTimKiem.add(new JLabel("Mã Hoá Đơn:"));
        txtMaHD = new JTextField(12);
        pnlTimKiem.add(txtMaHD);

        btnTim = new JButton("Tìm");
        btnLamMoi = new JButton("Làm Mới");
        pnlTimKiem.add(btnTim);
        pnlTimKiem.add(btnLamMoi);
        
        pnlTim.setLayout(new BorderLayout());
        pnlTim.add(lblTitle, BorderLayout.NORTH);
        pnlTim.add(pnlTimKiem, BorderLayout.CENTER);
        
        add(pnlTim, BorderLayout.NORTH);
        
 		pnlLeft = new JPanel();
 		pnlLeft.setPreferredSize(new Dimension(200, 0));
 		pnlLeft.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
 		pnlLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
 		pnlLeft.setBackground(new Color(224, 224, 224));

        btnXemChiTiet = createStyledButton("Xem Chi Tiết");
        pnlLeft.add(btnXemChiTiet);

 		add(pnlLeft, BorderLayout.WEST);

        String[] cols = {
                "Mã hóa đơn", "Ngày lập hoá đơn", "Tên khách hàng", 
                "Tên nhân viên", "Danh sách sản phẩm", "Tổng tiền hàng"
        };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setBackground(new Color(255, 255, 255));
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                if (column == 5) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
            }
        });
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(new Color(255, 255, 255));
        add(scroll, BorderLayout.CENTER);

        taiDuLieuHoaDon();
         
        btnTim.addActionListener(this);
	    btnLamMoi.addActionListener(this);
	    btnXemChiTiet.addActionListener(this);
	    table.addMouseListener(this);
    }
    
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.white);
        btn.setBackground(new Color(139, 69, 19));
        btn.setPreferredSize(new Dimension(180, 40)); 
        return btn;
    }
    
    private void taiDuLieuHoaDon() {
        timHoaDon("", "", "");
    }
    
    private void timHoaDon(String maHD, String hoTen, String tenSP) {
        model.setRowCount(0);
        try {
            List<Object[]> dsHD = hoaDonDAO.timKiemNangCao(maHD, hoTen, tenSP);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Object[] row : dsHD) {
                String ma = (String) row[0];
                Timestamp ngayTao = (Timestamp) row[1];
                String tenNV = (String) row[2];
                String tenKHang = (String) row[3];
                String dsSanPham = (String) row[4];
                Object tongTienObj = row[5]; 
                double tongTien = 0.0;
                
                if (tongTienObj instanceof Number) { 
                    tongTien = ((Number) tongTienObj).doubleValue(); 
                }
                
                String ngayTaoFormatted = dateFormat.format(ngayTao);
                
                model.addRow(new Object[]{
                    ma,
                    ngayTaoFormatted,
                    tenKHang,
                    tenNV,
                    dsSanPham != null ? dsSanPham : "N/A",
                    df.format(tongTien) + " VNĐ"
                });
            }
            
            if (dsHD.isEmpty() && (!maHD.isEmpty() || !hoTen.isEmpty() || !tenSP.isEmpty())) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn nào khớp với tiêu chí tìm kiếm.");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu hóa đơn: " + e.getMessage(), "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src==btnLamMoi){
			clearFields();
			taiDuLieuHoaDon();
		}
		if (src==btnXemChiTiet) {
			int row = table.getSelectedRow();

	        if (row == -1) {
	            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hóa đơn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
            String maHDChon = (String) model.getValueAt(row, 0);
            
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this.getParent());
            
            FrameDialogXemChiTietHoaDon dialog = new FrameDialogXemChiTietHoaDon(parentFrame, maHDChon);
            dialog.setVisible(true);
		}
		if (src==btnTim) {
            String maHD = txtMaHD.getText().trim();
            timHoaDon(maHD, "", ""); 
		}

	}

	private void clearFields() {
        txtMaHD.setText("");
    }

	@Override
	public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            int row = table.getSelectedRow();
            if (row != -1) {
                 btnXemChiTiet.doClick();
            }
        }
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}


	@Override
	public void mouseReleased(MouseEvent e) {
	}


	@Override
	public void mouseEntered(MouseEvent e) {
	}


	@Override
	public void mouseExited(MouseEvent e) {
	}
}