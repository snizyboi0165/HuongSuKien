//Người làm: Ngô Văn Thành
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingUtilities;

import dao.SanPham_DAO;

import entity.SanPham;

public class FrameSanPham extends JInternalFrame implements ActionListener { 

	private JComboBox<String> cboLoaiSP; 
	private JTextField txtMaSP;
	private JTable tableTop;
    private DefaultTableModel modelTop; 
    private SanPham_DAO sanPhamDAO;   
	private JPanel pnlLeft;
	private JButton btnTim;
	private JButton btnLamMoi;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnSua;
	private JLabel lblTitle;
	private JPanel pnlNorth;
	private JPanel center;
	private JPanel pnlSearch;


	public FrameSanPham() {
		setTitle("Quản lý Sản Phẩm");
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setIconifiable(true);
        
		setSize(1200, 800);
		setLayout(new BorderLayout());

        sanPhamDAO = new SanPham_DAO(); 

		pnlNorth = new JPanel(new BorderLayout());
		pnlNorth.setBackground(new Color(139, 69, 19));
		lblTitle = new JLabel("DANH SÁCH SẢN PHẨM", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.ITALIC, 24));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setPreferredSize(new Dimension(0, 50));
		pnlNorth.add(lblTitle);
		add(pnlNorth, BorderLayout.NORTH);

		pnlLeft = new JPanel(); 
		pnlLeft.setLayout(new GridLayout(10, 1, 5, 5));
		pnlLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlLeft.setBackground(new Color(224, 224, 224));
        
        btnThem = createStyledButton("Thêm Sản Phẩm (F4)");
		btnXoa = createStyledButton("Xoá Sản Phẩm (F5)");
		btnSua = createStyledButton("Sửa Thông Tin (F6)"); 

		pnlLeft.add(btnThem);
		pnlLeft.add(btnXoa);
		pnlLeft.add(btnSua);

        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnSua.addActionListener(this);

		add(pnlLeft, BorderLayout.WEST); 

		center = new JPanel(new BorderLayout());
		center.setBorder(new EmptyBorder(10, 10, 10, 10));
        center.setBackground(Color.WHITE);

		pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlSearch.setBackground(Color.WHITE);

		pnlSearch.add(new JLabel("Loại: "));
		cboLoaiSP = new JComboBox<>(new String[]{"Tất cả", "Đồ uống", "Đồ ăn nhẹ"});
		pnlSearch.add(cboLoaiSP);

		pnlSearch.add(new JLabel("Nhập mã/tên sản phẩm: "));
		txtMaSP = new JTextField(15);
		pnlSearch.add(txtMaSP);

		btnTim = new JButton("Tìm");
		btnLamMoi = new JButton("Làm mới");
		pnlSearch.add(btnTim);
		pnlSearch.add(btnLamMoi);
        
        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);
        cboLoaiSP.addActionListener(this);

		center.add(pnlSearch, BorderLayout.NORTH);

		String[] colsA = {"Mã Sản Phẩm", "Tên Sản Phẩm", "Giá Bán", "Loại", "Mô Tả", "Trạng thái"};
		
		modelTop = new DefaultTableModel(colsA, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
		tableTop = new JTable(modelTop);
		tableTop.setRowHeight(26);
		tableTop.setFillsViewportHeight(true);
		tableTop.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setupTableStyle(tableTop);
		JScrollPane scrollTop = new JScrollPane(tableTop);

		scrollTop.setPreferredSize(new Dimension(900, 700)); 
		center.add(scrollTop, BorderLayout.CENTER);

		add(center, BorderLayout.CENTER);
        
        loadSanPham(); 
	}
    
    private void setupTableStyle(JTable table) {
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.white);
        btn.setBackground(new Color(139, 69, 19));
        btn.setPreferredSize(new Dimension(180, 40)); 
        return btn;
    }
    
    private void loadSanPham(String keyword, String loai) {
        modelTop.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,##0 VNĐ");
        try {
            List<SanPham> dsSP = sanPhamDAO.timKiemNangCao(keyword, loai); 
            
            for(SanPham sp : dsSP) {
                 modelTop.addRow(new Object[]{
                     sp.getMaSanPham(),
                     sp.getTenSanPham(),
                     df.format(sp.getGiaBan()),
                     sp.getLoai(),
                     sp.getMoTa(),
                     sp.getTrangThai()
                 });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu sản phẩm");
            e.printStackTrace();
        }
    }
    
    private void loadSanPham() {
         loadSanPham("", "Tất cả"); 
    }
    
    private void filterSanPham() {
        String keyword = txtMaSP.getText().trim();
        String loai = cboLoaiSP.getSelectedItem().toString();
        
        loadSanPham(keyword, loai);
    }
    
    private void addSanPham() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        
        FrameThemSuaSanPham dialog = new FrameThemSuaSanPham(parentFrame, null); 
        dialog.setVisible(true);
        
        if (dialog.isSuccess()) {
            loadSanPham();
        }
    }
    
    private void suaSanPham() {
        int selectedRow = tableTop.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa thông tin!");
            return;
        }

        String maSP = (String) modelTop.getValueAt(selectedRow, 0);
        String tenSP = (String) modelTop.getValueAt(selectedRow, 1);
        String giaStr = (String) modelTop.getValueAt(selectedRow, 2);
        String loai = (String) modelTop.getValueAt(selectedRow, 3);
        String moTa = (String) modelTop.getValueAt(selectedRow, 4);
        String trangThai = (String) modelTop.getValueAt(selectedRow, 5);
        
        double giaBan = 0;
        try {
            DecimalFormat df = new DecimalFormat("#,##0 VNĐ");
            giaBan = df.parse(giaStr).doubleValue();
        } catch (Exception e) {
             try { giaBan = Double.parseDouble(giaStr.replaceAll("[^\\d.]", "")); } catch (Exception ex) {}
        }

        SanPham spToEdit = new SanPham(maSP, tenSP, giaBan, loai, moTa, trangThai);

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        FrameThemSuaSanPham dialog = new FrameThemSuaSanPham(parentFrame, spToEdit);
        dialog.setVisible(true);

        if (dialog.isSuccess()) {
            loadSanPham();
        }
    }
    
    private void xoaSanPham() {
        int selectedRow = tableTop.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maSP = (String) modelTop.getValueAt(selectedRow, 0);
        String tenSP = (String) modelTop.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn XOÁ sản phẩm " + maSP + " - " + tenSP + "?");

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = sanPhamDAO.xoaSanPham(maSP);

                if (success) {
                    loadSanPham();
                    JOptionPane.showMessageDialog(this, "Xoá sản phẩm thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Xoá thất bại!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi truy vấn CSDL khi xoá ");
                e.printStackTrace();
            }
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
        String command = e.getActionCommand();
        
        if (src == cboLoaiSP || src == btnTim) {
            filterSanPham();
        } else if (src == btnLamMoi) {
            txtMaSP.setText("");
            cboLoaiSP.setSelectedIndex(0);
            loadSanPham();
        } else if (src == btnThem) {
             addSanPham();
        } else if (src == btnXoa) {
             xoaSanPham();
        } else if (src == btnSua) {
             suaSanPham();
        }
	}
}