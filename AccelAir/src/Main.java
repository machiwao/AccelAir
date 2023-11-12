import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.Font;

public class Main {

	JFrame frmSwiftair;
	private JComboBox<String> cmbSource;
	private JComboBox<String> cmbDestination;
	private JTextArea txtShortestRoute;
	ImageIcon logo = new ImageIcon(this.getClass().getClassLoader().getResource("appLogo.png"));
	ImageIcon banner = new ImageIcon(this.getClass().getClassLoader().getResource("banner.png"));
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmSwiftair.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		InitializeRoutes.storeRoutes();
		InitializeRoutes.addComboBoxItems();
		InitializeRoutes.createGraph();
		
		frmSwiftair = new JFrame();
		frmSwiftair.getContentPane().setBackground(new Color(252, 252, 252));
		frmSwiftair.setResizable(false);
		frmSwiftair.setTitle("AccelAir");
		frmSwiftair.setIconImage(logo.getImage());
		frmSwiftair.setBounds(100, 100, 640, 480);
		frmSwiftair.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSwiftair.getContentPane().setLayout(null);
		
		JButton btnAddRoute = new JButton("Add");
		btnAddRoute.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		btnAddRoute.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnAddRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddRoutes addRoutes = new AddRoutes();
				addRoutes.frmAddRoutes.setVisible(true);
				frmSwiftair.dispose();
			}
		});
		btnAddRoute.setBounds(60, 360, 150, 50);
		frmSwiftair.getContentPane().add(btnAddRoute);
		
		JButton btnShowRoutes = new JButton("Show Routes");
		btnShowRoutes.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		btnShowRoutes.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnShowRoutes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShowRoutes showRoutes = new ShowRoutes();
				showRoutes.frmShowRoutes.setVisible(true);
				frmSwiftair.dispose();
			}
		});
		btnShowRoutes.setBounds(420, 360, 150, 50);
		frmSwiftair.getContentPane().add(btnShowRoutes);
		
		JButton btnUpdateRoutes = new JButton("Update");
		btnUpdateRoutes.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		btnUpdateRoutes.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnUpdateRoutes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateRoutes updateRoutes = new UpdateRoutes();
				updateRoutes.frmUpdateRoutes.setVisible(true);
				frmSwiftair.dispose();
			}
		});
		btnUpdateRoutes.setBounds(240, 360, 150, 50);
		frmSwiftair.getContentPane().add(btnUpdateRoutes);
		
		JButton btnShortestRoute = new JButton("Generate");
		btnShortestRoute.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		btnShortestRoute.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnShortestRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Node sourceNode = InitializeRoutes.travelMap.findNode(cmbSource.getSelectedItem().toString());
				InitializeRoutes.travelMap = Dijkstra.calculateShortestPathFromSource(InitializeRoutes.travelMap, sourceNode);
				Node destinationNode = InitializeRoutes.travelMap.findNode(cmbDestination.getSelectedItem().toString());
				int hours = (int) Math.floor(destinationNode.getDistance());
				int minutes = (int) (Math.round((destinationNode.getDistance() - hours) * 60));
				if((hours > 0 && hours < Integer.MAX_VALUE) || minutes > 0) {
					String path = "Path: ";
					for(Node pathNode : destinationNode.getShortestPath()){
						path += (pathNode.getName().toString() + "->");
					}
					path += (destinationNode.getName().toString() + "\nTotal flight duration: " + hours + "HRS " + minutes + "MINS");
					txtShortestRoute.setText(path);
				}else {
					txtShortestRoute.setText(null);
					JOptionPane.showMessageDialog(frmSwiftair, "Path does not Exist", "No Flight Route", JOptionPane.ERROR_MESSAGE);
				}
				InitializeRoutes.createGraph();
			}
		});
		btnShortestRoute.setBounds(455, 187, 115, 25);
		frmSwiftair.getContentPane().add(btnShortestRoute);
		
		cmbSource = new JComboBox<String>();
		cmbSource.setForeground(new Color(0, 0, 0));
		cmbSource.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		cmbSource.setBorder(null);
		cmbSource.setBackground(new Color(252, 252, 252));
		cmbSource.setBounds(60, 187, 134, 25);
		frmSwiftair.getContentPane().add(cmbSource);
		
		cmbDestination = new JComboBox<String>();
		cmbDestination.setForeground(new Color(0, 0, 0));
		cmbDestination.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		cmbDestination.setBorder(null);
		cmbDestination.setBackground(new Color(252, 252, 252));
		cmbDestination.setBounds(256, 187, 134, 25);
		frmSwiftair.getContentPane().add(cmbDestination);
		
		for(String airport : InitializeRoutes.airports) {
			cmbSource.addItem(airport);
			cmbDestination.addItem(airport);
		}
		
		txtShortestRoute = new JTextArea();
		txtShortestRoute.setForeground(new Color(245, 171, 14));
		txtShortestRoute.setFont(new Font("Montserrat Medium", Font.PLAIN, 20));
		txtShortestRoute.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		txtShortestRoute.setEditable(false);
		txtShortestRoute.setBounds(60, 218, 510, 121);
		frmSwiftair.getContentPane().add(txtShortestRoute);
		
		JLabel lblBanner = new JLabel("");
		lblBanner.setIcon(new ImageIcon(banner.getImage()));
		lblBanner.setBounds(130, 36, 360, 104);
		frmSwiftair.getContentPane().add(lblBanner);
		
		JLabel lblFrom = new JLabel("From");
		lblFrom.setForeground(new Color(66, 69, 183));
		lblFrom.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		lblFrom.setBounds(60, 161, 105, 25);
		frmSwiftair.getContentPane().add(lblFrom);
		
		JLabel lblTo = new JLabel("To");
		lblTo.setForeground(new Color(66, 69, 183));
		lblTo.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		lblTo.setBounds(256, 161, 105, 25);
		frmSwiftair.getContentPane().add(lblTo);
	}
}
