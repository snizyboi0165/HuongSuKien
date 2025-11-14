//Người làm: Nguyễn Cao Việt An
package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.FileWriter;
import java.io.File;


import dao.Ban_DAO;
import dao.HoaDon_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.KhachHang_DAO;
import dao.NhanVien_DAO;
import dao.KhuyenMai_DAO;
import dao.Thue_DAO;

import entity.Ban;
import entity.HoaDon;
import entity.KhachHang;
import entity.Thue;
import entity.KhuyenMai;

public class FrameDialogChiTietThanhToan extends JDialog implements ActionListener {

	private Ban banThanhToan;
	private HoaDon hoaDonHienTai;
	private DefaultTableModel modelChiTiet;
	private JTable tableChiTiet;
	private JLabel lblTenKhachHang;
	private JButton btnConfirmPayment;
	private JPanel pnlQRCode;
	private JLabel lblQRImage;
	private JLabel lblMaKhuyenMai; 
	private JLabel lblTenKhuyenMai;
	private String tenKhachHangFull = "Khách vãng lai";
	private String tenNhanVienFull = "NV";
	private JPanel pnlEast;
	private JPanel pnlCenter;
	private JPanel pnlHinhThuc;
	private JLabel lblTongTien;
	private JLabel lblTienThue;
	private JLabel lblTienGiam;
	private JLabel lblThanhTien;
	
	private JRadioButton radTienMat;
	private JRadioButton radMaQR;
	private HoaDon_DAO hoaDonDAO;
	private ChiTietHoaDon_DAO ctHoaDonDAO;
	private Ban_DAO banDAO;
	private KhachHang_DAO khachHangDAO;
	private NhanVien_DAO nhanVienDAO;
    private KhuyenMai_DAO khuyenMaiDAO;
    private Thue_DAO thueDAO;

	private double tongTienHang = 0;
	private double tienThue = 0;
	private double tienGiam = 0;
	private double tongThanhToan = 0;
	private boolean daThanhToan = false;
    
    private Thue thueApDung = null;
    private KhuyenMai khuyenMaiApDung = null;


	

	public FrameDialogChiTietThanhToan(Frame parent, Ban ban) {
		super(parent, "Chi Tiết Thanh Toán cho " + ban.getTenBan(), true);
		this.banThanhToan = ban;

		hoaDonDAO = new HoaDon_DAO();
		ctHoaDonDAO = new ChiTietHoaDon_DAO();
		banDAO = new Ban_DAO();
		khachHangDAO = new KhachHang_DAO();
		nhanVienDAO = new NhanVien_DAO();
        
        khuyenMaiDAO = new KhuyenMai_DAO();
        thueDAO = new Thue_DAO();

		setSize(1300, 750);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout(10, 10));
		((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

		pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));

		String[] cols = { "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền" };
		modelChiTiet = new DefaultTableModel(cols, 0);
		tableChiTiet = new JTable(modelChiTiet);
		pnlCenter.add(new JScrollPane(tableChiTiet), BorderLayout.CENTER);

		pnlEast = new JPanel();
		pnlEast.setLayout(new BoxLayout(pnlEast, BoxLayout.Y_AXIS));
		pnlEast.setPreferredSize(new Dimension(450, 0)); 
		pnlEast.setBorder(BorderFactory.createTitledBorder("Thông tin thanh toán"));

		lblTenKhachHang = createPaymentLabel("Khách hàng:", Component.CENTER_ALIGNMENT);
		lblTenKhachHang.setFont(new Font("Segoe UI", Font.ITALIC, 16));
		lblTenKhachHang.setForeground(Color.BLUE);
		pnlEast.add(lblTenKhachHang);
		pnlEast.add(new JSeparator());
        
        lblMaKhuyenMai = createPaymentLabel("Mã KM:", Component.CENTER_ALIGNMENT);
        lblTenKhuyenMai = createPaymentLabel("Khuyến mãi:", Component.CENTER_ALIGNMENT);
		lblTenKhuyenMai.setToolTipText("Khuyến mãi: Khuyến mãi 200 điểm tích lũy"); 
        pnlEast.add(lblMaKhuyenMai);
        pnlEast.add(lblTenKhuyenMai);
        pnlEast.add(new JSeparator());


		lblTongTien = createPaymentLabel("Tổng tiền hàng:", Component.CENTER_ALIGNMENT);
		lblTienThue = createPaymentLabel("Thuế (VAT 10%):", Component.CENTER_ALIGNMENT);
		lblTienGiam = createPaymentLabel("Giảm giá:", Component.CENTER_ALIGNMENT);
		lblThanhTien = createPaymentLabel("THÀNH TIỀN:", Component.CENTER_ALIGNMENT);
		lblThanhTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblThanhTien.setForeground(Color.RED);

		pnlEast.add(lblTongTien);
		pnlEast.add(lblTienThue);
		pnlEast.add(lblTienGiam);
		pnlEast.add(new JSeparator());
		pnlEast.add(lblThanhTien);
		pnlEast.add(Box.createVerticalStrut(20));

		pnlHinhThuc = new JPanel(new GridLayout(0, 1));
		pnlHinhThuc.setBorder(BorderFactory.createTitledBorder("Hình thức"));
		radTienMat = new JRadioButton("Tiền mặt", true);
		radMaQR = new JRadioButton("Quét mã QR");
		ButtonGroup bg = new ButtonGroup();
		bg.add(radTienMat);
		bg.add(radMaQR);
		pnlHinhThuc.add(radTienMat);
		pnlHinhThuc.add(radMaQR);

		pnlHinhThuc.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlHinhThuc.setMaximumSize(new Dimension(400, 80)); 
		pnlEast.add(pnlHinhThuc);

		pnlEast.add(Box.createVerticalStrut(10));

		pnlQRCode = new JPanel(new BorderLayout());
		pnlQRCode.setBorder(BorderFactory.createTitledBorder("Mã QR Thanh Toán"));
		lblQRImage = new JLabel();
		lblQRImage.setHorizontalAlignment(SwingConstants.CENTER);
		ImageIcon qrIcon = createIcon("/images/qr.png", 250, 250);
		if (qrIcon != null) {
			lblQRImage.setIcon(qrIcon);
		} else {
			lblQRImage.setText("Không thể tải ảnh QR");
		}
		pnlQRCode.add(lblQRImage, BorderLayout.CENTER);
		pnlQRCode.setVisible(false);
		pnlQRCode.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlEast.add(pnlQRCode);

		pnlEast.add(Box.createVerticalGlue());

		btnConfirmPayment = new JButton("Xác Nhận Thanh Toán");
		btnConfirmPayment.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnConfirmPayment.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnConfirmPayment.setBackground(new Color(40, 167, 69));
		btnConfirmPayment.setForeground(Color.WHITE);
		pnlEast.add(btnConfirmPayment);

		getContentPane().add(pnlCenter, BorderLayout.CENTER);
		getContentPane().add(pnlEast, BorderLayout.EAST);

		btnConfirmPayment.addActionListener(this);
		radTienMat.addActionListener(this);
		radMaQR.addActionListener(this);

		loadHoaDon();
	}

	private JLabel createPaymentLabel(String text, float alignment) {
		JLabel label = new JLabel(text + " 0 VNĐ");
		label.setFont(new Font("Segoe UI", Font.BOLD, 16));
		label.setBorder(new EmptyBorder(5, 10, 5, 10));
		label.setAlignmentX(alignment);
		return label;
	}

	private void loadHoaDon() {
		try {
			hoaDonHienTai = hoaDonDAO.findActiveByMaBan(banThanhToan.getMaBan());

			if (hoaDonHienTai == null) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn 'Chưa thanh toán' cho bàn này!");
				this.dispose();
				return;
			}
            
			if (hoaDonHienTai.getNhanVien() != null && hoaDonHienTai.getNhanVien().getMaNhanVien() != null) {
				String tenNV = nhanVienDAO.getTenNhanVien(hoaDonHienTai.getNhanVien().getMaNhanVien());
				if (tenNV != null) {
					this.tenNhanVienFull = tenNV;
				}
			}

            KhachHang khachHang = null;
			if (hoaDonHienTai.getKhachHang() != null && hoaDonHienTai.getKhachHang().getMaKhachHang() != null) {
				String maKH = hoaDonHienTai.getKhachHang().getMaKhachHang();
				List<KhachHang> khList = khachHangDAO.timKiemNangCao(maKH, "", "");
				if (khList != null && !khList.isEmpty()) {
					khachHang = khList.get(0);
					tenKhachHangFull = khachHang.getTenKhachHang();
					lblTenKhachHang.setText("Khách hàng: " + tenKhachHangFull);
				} else {
					lblTenKhachHang.setText("Khách hàng: Vãng lai (Lỗi tìm tên)");
				}
			} else {
				lblTenKhachHang.setText("Khách hàng: Vãng lai");
			}
            
			List<Object[]> chiTietList = ctHoaDonDAO.findAllChiTietByMaHoaDon(hoaDonHienTai.getMaHoaDon());

			tongTienHang = 0;
			modelChiTiet.setRowCount(0);
			DecimalFormat df = new DecimalFormat("#,##0"); 
            
			for (Object[] item : chiTietList) {
				String tenSP = (String) item[0];
				int soLuong = (int) item[1];
				double donGia = (double) item[2];
				double thanhTien = soLuong * donGia;

				modelChiTiet.addRow(new Object[] { tenSP, soLuong, df.format(donGia) + " VNĐ", df.format(thanhTien) + " VNĐ" });
				tongTienHang += thanhTien;
			}
            
            if (khachHang != null) {
                 khuyenMaiApDung = khuyenMaiDAO.findKhuyenMaiTuDong(khachHang.getDiemTichLuy());
            }
            
            thueApDung = thueDAO.getThueMacDinh();
            
            tinhToanTongTien();
            
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết hóa đơn: " + e.getMessage());
		}
	}
    
    private void tinhToanTongTien() {
        DecimalFormat df = new DecimalFormat("#,##0");
        
        tienGiam = 0;
        if (khuyenMaiApDung != null) {
             tienGiam = tongTienHang * khuyenMaiApDung.getPhanTramGiam();
        }
        
        tienThue = 0;
        if (thueApDung != null) {
            tienThue = tongTienHang * thueApDung.getTyLeThue(); 
        }
        
        tongThanhToan = tongTienHang + tienThue - tienGiam;
        if (tongThanhToan < 0) tongThanhToan = 0;

        if (khuyenMaiApDung != null) {
             lblMaKhuyenMai.setText("Mã Khuyến Mãi: " + khuyenMaiApDung.getMaKhuyenMai());
             lblTenKhuyenMai.setText("Khuyến mãi: " + khuyenMaiApDung.getTenKhuyenMai() + " (" + (int)(khuyenMaiApDung.getPhanTramGiam()*100) + "%)");
             lblTienGiam.setForeground(Color.RED);
        } else {
             lblMaKhuyenMai.setText("Mã Khuyến Mãi: N/A");
             lblTenKhuyenMai.setText("Khuyến mãi: Không áp dụng");
             lblTienGiam.setForeground(Color.BLACK);
        }
        
        if (thueApDung != null) {
             lblTienThue.setText("Thuế (" + thueApDung.getTenThue() + " " + (int)(thueApDung.getTyLeThue()*100) + "%): " + df.format(tienThue) + " VNĐ");
        } else {
             lblTienThue.setText("Thuế (N/A): 0 VNĐ");
        }
        
        lblTongTien.setText("Tổng tiền hàng: " + df.format(tongTienHang) + " VNĐ");
        lblTienGiam.setText("Giảm giá: " + df.format(tienGiam) + " VNĐ");
        lblThanhTien.setText("THÀNH TIỀN: " + df.format(tongThanhToan) + " VNĐ");
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == radTienMat) {
			pnlQRCode.setVisible(false);
			btnConfirmPayment.setEnabled(true);
			btnConfirmPayment.setText("Xác Nhận Đã Thanh Toán");

		} else if (src == radMaQR) {
			pnlQRCode.setVisible(true);
			btnConfirmPayment.setText("Xác Nhận Đã Thanh Toán");
			btnConfirmPayment.setEnabled(true);

		} else if (src == btnConfirmPayment) {
			int confirm = JOptionPane.showConfirmDialog(this,
					"Xác nhận thanh toán hoàn tất cho " + banThanhToan.getTenBan() + "?", "Xác nhận",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				hienThiHoaDonIn();
				xuLyThanhToanHoanTat();
			}
		}
	}

	private void hienThiHoaDonIn() {
		DialogXemHoaDon dialogIn = new DialogXemHoaDon(this, xayDungHoaDon());
		dialogIn.setVisible(true);
	}

	private String xayDungHoaDon() {
		StringBuilder bill = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#,##0");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

		bill.append("\t      CAFE MEME\n");
		bill.append("\tĐịa chỉ: 12 Nguyễn Văn Bảo, P.4, Q.Gò Vấp\n");
		bill.append("\tSĐT: 0123.456.789\n");
		bill.append("\t=================================\n");
		bill.append("\t      HÓA ĐƠN THANH TOÁN\n");
		bill.append("\tMã HĐ: ").append(hoaDonHienTai.getMaHoaDon()).append("\n");
		bill.append("\tBàn: ").append(banThanhToan.getTenBan()).append("\n");
		bill.append("\tNhân viên: ").append(tenNhanVienFull).append("\n");
		bill.append("\tKhách hàng: ").append(tenKhachHangFull).append("\n");
		bill.append("\tGiờ vào: ")
				.append(timeFormat.format(
						Date.from(hoaDonHienTai.getNgayTao().atZone(java.time.ZoneId.systemDefault()).toInstant())))
				.append("\n");
		bill.append("\tGiờ ra: ").append(timeFormat.format(new Date())).append("\n");
		bill.append("\t=================================\n");
		bill.append("\tTên món\tSL\tĐơn giá\tThành tiền\n");
		bill.append("\t---------------------------------\n");

		for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
			String tenSP = modelChiTiet.getValueAt(i, 0).toString();
			String soLuong = modelChiTiet.getValueAt(i, 1).toString();
			String donGia = modelChiTiet.getValueAt(i, 2).toString().replace(" VNĐ", "");
			String thanhTien = modelChiTiet.getValueAt(i, 3).toString().replace(" VNĐ", "");

			if (tenSP.length() > 15) {
				bill.append("\t").append(tenSP.substring(0, 15)).append("...\n");
				bill.append("\t\t").append(soLuong).append("\t").append(donGia).append("\t").append(thanhTien)
						.append("\n");
			} else {
				bill.append(String.format("\t%-15s\t%s\t%s\t%s\n", tenSP, soLuong, donGia, thanhTien));
			}
		}

		bill.append("\t=================================\n");
		bill.append("\tTổng tiền hàng:\t").append(df.format(tongTienHang)).append(" VNĐ\n");
		bill.append("\tThuế (VAT 10%):\t").append(df.format(tienThue)).append(" VNĐ\n");
		bill.append("\tGiảm giá:\t").append(df.format(tienGiam)).append(" VNĐ\n");
		bill.append("\tTHÀNH TIỀN:\t").append(df.format(tongThanhToan)).append(" VNĐ\n");
		bill.append("\t=================================\n");
		bill.append("\tCảm ơn quý khách. Hẹn gặp lại!\n");

		return bill.toString();
	}

	private void xuLyThanhToanHoanTat() {
		try {
            String maThue = thueApDung != null ? thueApDung.getMaThue() : null;
            String maKM = khuyenMaiApDung != null ? khuyenMaiApDung.getMaKhuyenMai() : null;
            
            hoaDonDAO.updateMaThueAndMaKhuyenMai(hoaDonHienTai.getMaHoaDon(), maThue, maKM); 

			boolean hdSuccess = hoaDonDAO.updateTrangThai(hoaDonHienTai.getMaHoaDon(), "Đã thanh toán");
			boolean banSuccess = banDAO.updateTrangThaiBan(banThanhToan.getMaBan(), "Trống");

			if (hoaDonHienTai.getKhachHang() != null) {
				String maKH = hoaDonHienTai.getKhachHang().getMaKhachHang();
				int diemCong = (int) (tongThanhToan / 10000);
				if (diemCong > 0) {
					khachHangDAO.congDiemTichLuy(maKH, diemCong);
				}
			}

			if (hdSuccess && banSuccess) {
				this.dispose();
				daThanhToan = true;
                JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
			} else {
				JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi cập nhật trạng thái CSDL.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Lỗi SQL khi xác nhận thanh toán: " + e.getMessage());
		}
	}

	public boolean daThanhToanThanhCong() {
		return daThanhToan;
	}

	private ImageIcon createIcon(String path, int width, int height) {
		URL url = getClass().getResource(path);
		if (url != null) {
			return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		}
		System.err.println("Không tìm thấy icon: " + path);
		return null;
	}

	class DialogXemHoaDon extends JDialog {
		public DialogXemHoaDon(Dialog owner, String billText) {
			super(owner, "Hóa Đơn (Xem trước khi in)", true);
			setSize(400, 600);
			setLocationRelativeTo(owner);
			setLayout(new BorderLayout());

			JTextArea txtHoaDon = new JTextArea(billText);
			txtHoaDon.setFont(new Font("Monospaced", Font.PLAIN, 12));
			txtHoaDon.setEditable(false);

			JButton btnPrint = new JButton("In Hóa Đơn");
			btnPrint.addActionListener(e -> {
				try {
                    String htmlContent = convertBillTextToHtml(billText);
                    
                    File tempFile = File.createTempFile("hoadon_", ".html");
                    
                    try (FileWriter writer = new FileWriter(tempFile)) {
                        writer.write(htmlContent);
                    }
                    
                    String os = System.getProperty("os.name").toLowerCase();
                    if (os.contains("win")) {
                        Runtime.getRuntime().exec("cmd /c start msedge \"" + tempFile.getAbsolutePath() + "\"");
                    } else if (os.contains("mac")) {
                         Runtime.getRuntime().exec("open -a \"Microsoft Edge\" \"" + tempFile.getAbsolutePath() + "\"");
                    } else {
                        Desktop.getDesktop().browse(tempFile.toURI());
                    }

                    tempFile.deleteOnExit(); 

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi mở trình duyệt để in: " + ex.getMessage());
                }
			});

			JButton btnClose = new JButton("Đóng");
			btnClose.addActionListener(e -> this.dispose());

			JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
			pnlButtons.add(btnPrint);
			pnlButtons.add(btnClose);

			add(new JScrollPane(txtHoaDon), BorderLayout.CENTER);
			add(pnlButtons, BorderLayout.SOUTH);
		}
        
        private String convertBillTextToHtml(String billText) {
            String htmlContent = billText.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;").replace("\n", "<br>");
            
            return "<html><head><style>" +
                   "body { font-family: monospace; font-size: 10pt; width: 400px; margin: 20px; }" +
                   "@media print { body { visibility: visible; width: 100%; margin: 0; }" + 
                   "button { display: none; }" +
                   "}</style></head><body>" +
                   "<div id='printableArea'>" + htmlContent + "</div>" +
                   "<script>window.onload = function() { window.print(); }</script>" + 
                   "</body></html>";
        }
	}
}