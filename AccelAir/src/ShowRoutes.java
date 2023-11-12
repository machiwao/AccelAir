import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.util.Map.Entry;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.LineBorder;

public class ShowRoutes {

	JFrame frmShowRoutes;
	DefaultTableModel model;
	private JTable tblDisplayRoutes;
	ImageIcon logo = new ImageIcon(this.getClass().getClassLoader().getResource("appLogo.png"));
	ImageIcon banner = new ImageIcon(this.getClass().getClassLoader().getResource("banner.png"));
	ImageIcon rawPhoto = new ImageIcon(this.getClass().getClassLoader().getResource("home.png"));
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main main = new Main();
					main.frmSwiftair.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public ShowRoutes() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {		
		frmShowRoutes = new JFrame();
		frmShowRoutes.getContentPane().setBackground(new Color(252, 252, 252));
		frmShowRoutes.setResizable(false);
		frmShowRoutes.setIconImage(logo.getImage());
		frmShowRoutes.setTitle("AccelAir - Show Routes");
		frmShowRoutes.setBounds(100, 100, 640, 480);
		frmShowRoutes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmShowRoutes.getContentPane().setLayout(null);
		
		JComboBox<String> cmbSource = new JComboBox<String>();
		cmbSource.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		for(String airport : InitializeRoutes.airports) {
			cmbSource.addItem(airport);
		}
		cmbSource.setBounds(36, 179, 166, 25);
		frmShowRoutes.getContentPane().add(cmbSource);
		
		JButton btnHome = new JButton("");
		Image scaledPhoto = (rawPhoto).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		btnHome.setIcon(new ImageIcon(scaledPhoto));
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main main = new Main();
				main.frmSwiftair.setVisible(true);
				frmShowRoutes.dispose();
			}
		});
		btnHome.setBounds(535, 36, 50, 50);
		frmShowRoutes.getContentPane().add(btnHome);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 211, 549, 190);
		frmShowRoutes.getContentPane().add(scrollPane);
		
		tblDisplayRoutes = new JTable();
		tblDisplayRoutes.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		model = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		Object[] column = {"From", "To", "Hours", "Minutes"};
		model.setColumnIdentifiers(column);
		tblDisplayRoutes.setModel(model);
		scrollPane.setViewportView(tblDisplayRoutes);
		
		JButton btnShowRoutes = new JButton("Show");
		btnShowRoutes.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnShowRoutes.setFont(new Font("Montserrat Medium", Font.PLAIN, 18));
		btnShowRoutes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setRowCount(0);
				Node selectedNode = InitializeRoutes.travelMap.findNode(cmbSource.getSelectedItem().toString());
				for(Entry<Node, Double> adjacentNode : selectedNode.getAdjacentNodes().entrySet()) {
					int hours = (int) Math.floor(adjacentNode.getValue());
					int minutes = (int) (Math.round((adjacentNode.getValue() - hours) * 60));
					Object[] entryRow = {selectedNode.getName(), adjacentNode.getKey().getName(), hours, minutes};
					model.addRow(entryRow);
				}
			}
		});
		btnShowRoutes.setBounds(225, 179, 100, 25);
		frmShowRoutes.getContentPane().add(btnShowRoutes);
		
		JButton btnReset = new JButton("Clear");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setRowCount(0);
			}
		});
		btnReset.setFont(new Font("Montserrat Medium", Font.PLAIN, 18));
		btnReset.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnReset.setBounds(345, 179, 100, 25);
		frmShowRoutes.getContentPane().add(btnReset);
		
		JLabel lblBanner = new JLabel("");
		lblBanner.setIcon(new ImageIcon(banner.getImage()));
		lblBanner.setBounds(130, 36, 360, 104);
		frmShowRoutes.getContentPane().add(lblBanner);
		
		JLabel lblFrom = new JLabel("From");
		lblFrom.setForeground(new Color(66, 69, 183));
		lblFrom.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		lblFrom.setBounds(36, 159, 69, 20);
		frmShowRoutes.getContentPane().add(lblFrom);
		
	}
}
