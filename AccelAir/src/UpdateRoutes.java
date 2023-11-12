import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SpinnerNumberModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import javax.swing.border.LineBorder;

public class UpdateRoutes {

	JFrame frmUpdateRoutes;
	private DefaultTableModel model;
	private JComboBox<String> cmbSource;
	private JButton btnDelete;
	private JButton btnUpdate;
	private JSpinner spnHours;
	private JScrollPane scrollPane;
	private JSpinner spnMinutes;
	private Node origSource;
	private Node origDestination;
	private int selectedRoute;
	private JTable tblRoutesList;
	private JTextField txtSource;
	private JTextField txtDestination;
	ImageIcon logo = new ImageIcon(this.getClass().getClassLoader().getResource("appLogo.png"));
	ImageIcon banner = new ImageIcon(this.getClass().getClassLoader().getResource("addRoutes.png"));
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
	public UpdateRoutes() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		frmUpdateRoutes = new JFrame();
		frmUpdateRoutes.getContentPane().setBackground(new Color(252, 252, 252));
		frmUpdateRoutes.setResizable(false);
		frmUpdateRoutes.setIconImage(logo.getImage());
		frmUpdateRoutes.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					BufferedWriter rewriteRoutes = new BufferedWriter(new FileWriter("RoutesList.txt"));
					for(String[] routes : InitializeRoutes.routesList) {
						rewriteRoutes.write(routes[0] + "\t" + routes[1] + "\t" + routes[2]);
						rewriteRoutes.newLine();
					}
					rewriteRoutes.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frmUpdateRoutes.setTitle("AccelAir - Update Routes");
		frmUpdateRoutes.setBounds(100, 100, 640, 480);
		frmUpdateRoutes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUpdateRoutes.getContentPane().setLayout(null);
		
		cmbSource = new JComboBox<String>();
		cmbSource.setBorder(null);
		cmbSource.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		cmbSource.setBounds(36, 145, 150, 25);
		frmUpdateRoutes.getContentPane().add(cmbSource);
		for(String airport : InitializeRoutes.airports) {
			cmbSource.addItem(airport);
		}
		
		txtSource = new JTextField();
		txtSource.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		txtSource.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		txtSource.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(!(Character.isAlphabetic(e.getKeyChar())) || txtSource.getText().length() >= 3) {
					e.consume();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				int pos = txtSource.getCaretPosition();
				txtSource.setText(txtSource.getText().toUpperCase());
				txtSource.setCaretPosition(pos);
			}
		});
		txtSource.setBounds(36, 388, 75, 25);
		txtSource.setEnabled(false);
		frmUpdateRoutes.getContentPane().add(txtSource);
		txtSource.setColumns(10);
		
		txtDestination = new JTextField();
		txtDestination.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		txtDestination.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		txtDestination.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(!(Character.isAlphabetic(e.getKeyChar())) || txtDestination.getText().length() >= 3) {
					e.consume();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				int pos = txtDestination.getCaretPosition();
				txtDestination.setText(txtDestination.getText().toUpperCase());
				txtDestination.setCaretPosition(pos);
			}
		});
		txtDestination.setColumns(10);
		txtDestination.setEnabled(false);
		txtDestination.setBounds(121, 388, 75, 25);
		frmUpdateRoutes.getContentPane().add(txtDestination);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setRowCount(0);
				for(String airport : InitializeRoutes.airports) {
					Node currentNode = InitializeRoutes.travelMap.findNode(airport);
					for(Entry<Node, Double> adjacentNode : currentNode.getAdjacentNodes().entrySet()) {
						int hours = (int) Math.floor(adjacentNode.getValue());
						int minutes = (int) (Math.round((adjacentNode.getValue() - hours) * 60));
						Object[] entryRow = {currentNode.getName(), adjacentNode.getKey().getName(), hours, minutes};
						model.addRow(entryRow);
					}
				}
				resetComponents();
			}
		});
		btnReset.setFont(new Font("Montserrat Medium", Font.PLAIN, 18));
		btnReset.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnReset.setBounds(296, 145, 90, 25);
		frmUpdateRoutes.getContentPane().add(btnReset);
		
		btnUpdate = new JButton("Update");
		btnUpdate.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnUpdate.setFont(new Font("Montserrat Medium", Font.PLAIN, 18));
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tblRoutesList.getSelectedRow();
				boolean isExisting = false;
				double hours = Double.parseDouble(spnHours.getValue().toString());
				double minutes = Double.parseDouble(spnMinutes.getValue().toString()) / 60.0;
				String[] modifiedRoute = new String[] {txtSource.getText(), txtDestination.getText(),String.valueOf(hours + minutes)};
				
				if(txtSource.getText().isBlank() || txtDestination.getText().isBlank())
        			JOptionPane.showMessageDialog(frmUpdateRoutes, "Please fill in the necessary information", "Empty Fields Detected", JOptionPane.ERROR_MESSAGE);
				else if(txtSource.getText().length() != 3 || txtDestination.getText().length() != 3)
					JOptionPane.showMessageDialog(frmUpdateRoutes, "Please enter a valid airport 3-Letter Code", "Invalid Airport Entry", JOptionPane.ERROR_MESSAGE);
				else if(txtSource.getText().equals(txtDestination.getText()))
					JOptionPane.showMessageDialog(frmUpdateRoutes, "Source and Destination airports are the same", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				else if(spnHours.getValue().toString().equals("0") && spnMinutes.getValue().toString().equals("0"))
					JOptionPane.showMessageDialog(frmUpdateRoutes, "Please enter a valid flight duration", "Invalid Flight Duration", JOptionPane.ERROR_MESSAGE);
				else {
					for(String[] route : InitializeRoutes.routesList) {
						if(route.equals(InitializeRoutes.routesList.get(selectedRoute)))
							continue;
						if(route[0].equals(txtSource.getText()) && route[1].equals(txtDestination.getText())) {
							JOptionPane.showMessageDialog(frmUpdateRoutes, "Route already exists", "Invalid entry", JOptionPane.ERROR_MESSAGE);
							isExisting = true;
							break;
						}
					}
					if(!isExisting) {
						int updateConfirmation = JOptionPane.showConfirmDialog(frmUpdateRoutes, "Do you want to continue?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (updateConfirmation == JOptionPane.YES_OPTION) {
							origSource.removePath(origDestination);
							model.setValueAt(txtSource.getText(), selectedRow, 0);
							model.setValueAt(txtDestination.getText(), selectedRow, 1);
							model.setValueAt(spnHours.getValue(), selectedRow, 2);
							model.setValueAt(spnMinutes.getValue(), selectedRow, 3);
							InitializeRoutes.routesList.set(selectedRoute, modifiedRoute);
							if(InitializeRoutes.travelMap.findNode(txtSource.getText()) == null) {
								InitializeRoutes.travelMap.addNode(new Node(txtSource.getText()));
								origSource.removePath(origDestination);
							}
							if(InitializeRoutes.travelMap.findNode(txtDestination.getText()) == null) {
								InitializeRoutes.travelMap.addNode(new Node(txtDestination.getText()));
								origSource.removePath(origDestination);
							}
							InitializeRoutes.travelMap.findNode(txtSource.getText()).addDestination(InitializeRoutes.travelMap.findNode(txtDestination.getText()), (hours + minutes));
							InitializeRoutes.addComboBoxItems();
							cmbSource.removeAllItems();
							for(String airport : InitializeRoutes.airports) {
								cmbSource.addItem(airport);
							}
						}
	           		}
				}
				resetComponents();
			}
		});
		btnUpdate.setEnabled(false);
		btnUpdate.setBounds(373, 388, 100, 25);
		frmUpdateRoutes.getContentPane().add(btnUpdate);
		
		JButton btnHome = new JButton("");
		Image scaledPhoto = (rawPhoto).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		btnHome.setIcon(new ImageIcon(scaledPhoto));
		btnHome.setBorder(new LineBorder(new Color(125, 103, 126), 0, true));
		btnHome.setFont(new Font("Montserrat Medium", Font.PLAIN, 18));
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BufferedWriter rewriteRoutes = new BufferedWriter(new FileWriter("RoutesList.txt"));
					for(String[] routes : InitializeRoutes.routesList) {
						rewriteRoutes.write(routes[0] + "\t" + routes[1] + "\t" + routes[2]);
						rewriteRoutes.newLine();
					}
					rewriteRoutes.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Main main = new Main();
				main.frmSwiftair.setVisible(true);
				frmUpdateRoutes.dispose();
			}
		});
		btnHome.setBounds(533, 25, 50, 50);
		frmUpdateRoutes.getContentPane().add(btnHome);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		scrollPane.setBounds(36, 179, 547, 187);
		frmUpdateRoutes.getContentPane().add(scrollPane);
		
		tblRoutesList = new JTable();
		tblRoutesList.setSelectionForeground(new Color(245, 171, 14));
		tblRoutesList.setSelectionBackground(new Color(66, 69, 183));
		tblRoutesList.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		tblRoutesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = tblRoutesList.getSelectedRow();
				origSource = InitializeRoutes.travelMap.findNode(model.getValueAt(selectedRow, 0).toString());
				origDestination = InitializeRoutes.travelMap.findNode(model.getValueAt(selectedRow, 1).toString());
				txtSource.setText(origSource.getName());
				txtDestination.setText(origDestination.getName());
				spnHours.setValue(model.getValueAt(selectedRow, 2));
				spnMinutes.setValue(model.getValueAt(selectedRow, 3));
				txtDestination.setEnabled(true);
				txtSource.setEnabled(true);
				spnHours.setEnabled(true);
				spnMinutes.setEnabled(true);
				btnUpdate.setEnabled(true);
				btnDelete.setEnabled(true);
				for(String[] route : InitializeRoutes.routesList) {
					if(route[0].equals(txtSource.getText()) && route[1].equals(txtDestination.getText())) {
						selectedRoute = InitializeRoutes.routesList.indexOf(route);
						break;
					}
				}
			}
		});
		model = new DefaultTableModel() {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		Object[] column = {"From", "To", "Hours", "Minutes"};
		model.setColumnIdentifiers(column);
		tblRoutesList.setModel(model);
		scrollPane.setViewportView(tblRoutesList);
		
		for(String airport : InitializeRoutes.airports) {
			Node currentNode = InitializeRoutes.travelMap.findNode(airport);
			for(Entry<Node, Double> adjacentNode : currentNode.getAdjacentNodes().entrySet()) {
				int hours = (int) Math.floor(adjacentNode.getValue());
				int minutes = (int) (Math.round((adjacentNode.getValue() - hours) * 60));
				Object[] entryRow = {currentNode.getName(), adjacentNode.getKey().getName(), hours, minutes};
				model.addRow(entryRow);
			}
		}
		
		JButton btnFilter = new JButton("Filter");
		btnFilter.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnFilter.setFont(new Font("Montserrat Medium", Font.PLAIN, 18));
		btnFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cmbSource.getSelectedItem() == null)
					JOptionPane.showMessageDialog(frmUpdateRoutes, "Please add new Routes", "No Routes Detected", JOptionPane.ERROR_MESSAGE);
				else {
					model.setRowCount(0);
					Node selectedNode = InitializeRoutes.travelMap.findNode(cmbSource.getSelectedItem().toString());
					for(Entry<Node, Double> adjacentNode : selectedNode.getAdjacentNodes().entrySet()) {
						int hours = (int) Math.floor(adjacentNode.getValue());
						int minutes = (int) (Math.round((adjacentNode.getValue() - hours) * 60));
						Object[] entryRow = {selectedNode.getName(), adjacentNode.getKey().getName(), hours, minutes};
						model.addRow(entryRow);
					}
					resetComponents();
				}
			}
		});
		btnFilter.setBounds(196, 145, 90, 25);
		frmUpdateRoutes.getContentPane().add(btnFilter);
		
		btnDelete = new JButton("Delete");
		btnDelete.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnDelete.setFont(new Font("Montserrat Medium", Font.PLAIN, 18));
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tblRoutesList.getSelectedRow();
				Node source = InitializeRoutes.travelMap.findNode(model.getValueAt(selectedRow, 0).toString());
				Node destination = InitializeRoutes.travelMap.findNode(model.getValueAt(selectedRow, 1).toString());
				int deleteConfirmation = JOptionPane.showConfirmDialog(frmUpdateRoutes, "Do you want to continue?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(deleteConfirmation == JOptionPane.YES_NO_OPTION) {
					source.removePath(destination);
					for(String[] currentRoute : InitializeRoutes.routesList) {
						if(currentRoute[0].equals(source.getName()) && currentRoute[1].equals(destination.getName())) {
							InitializeRoutes.routesList.remove(currentRoute);
							break;
						}
					}
					model.removeRow(selectedRow);
					InitializeRoutes.addComboBoxItems();
					cmbSource.removeAllItems();
					for(String airport : InitializeRoutes.airports) {
						cmbSource.addItem(airport);
					}
				}
				resetComponents();
			}
		});
		btnDelete.setBounds(483, 388, 100, 25);
		frmUpdateRoutes.getContentPane().add(btnDelete);
		
		spnHours = new JSpinner();
		spnHours.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		spnHours.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		spnHours.setModel(new SpinnerNumberModel(0, 0, 12, 1));
		spnHours.setBounds(206, 388, 70, 25);
		spnHours.setEnabled(false);
		frmUpdateRoutes.getContentPane().add(spnHours);
		
		spnMinutes = new JSpinner();
		spnMinutes.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		spnMinutes.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		spnMinutes.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		spnMinutes.setBounds(286, 388, 70, 25);
		spnMinutes.setEnabled(false);
		frmUpdateRoutes.getContentPane().add(spnMinutes);
		
		JLabel lblFrom = new JLabel("From");
		lblFrom.setForeground(new Color(66, 69, 183));
		lblFrom.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		lblFrom.setBounds(36, 368, 69, 20);
		frmUpdateRoutes.getContentPane().add(lblFrom);
		
		JLabel lblTo = new JLabel("To");
		lblTo.setForeground(new Color(66, 69, 183));
		lblTo.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		lblTo.setBounds(121, 368, 45, 20);
		frmUpdateRoutes.getContentPane().add(lblTo);
		
		JLabel lblHours = new JLabel("Hours");
		lblHours.setForeground(new Color(66, 69, 183));
		lblHours.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 16));
		lblHours.setBounds(206, 368, 51, 25);
		frmUpdateRoutes.getContentPane().add(lblHours);
		
		JLabel lblMins = new JLabel("Mins");
		lblMins.setForeground(new Color(66, 69, 183));
		lblMins.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 16));
		lblMins.setBounds(286, 368, 45, 25);
		frmUpdateRoutes.getContentPane().add(lblMins);
		
		JLabel lblFrom_1 = new JLabel("From");
		lblFrom_1.setForeground(new Color(66, 69, 183));
		lblFrom_1.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		lblFrom_1.setBounds(36, 125, 69, 20);
		frmUpdateRoutes.getContentPane().add(lblFrom_1);
		
		JLabel lblImage = new JLabel("");
		lblImage.setIcon(new ImageIcon(banner.getImage()));
		lblImage.setBounds(160, 25, 300, 94);
		frmUpdateRoutes.getContentPane().add(lblImage);
	}
	
	public void resetComponents() {
		txtSource.setText(null);
		txtDestination.setText(null);
		spnHours.setValue(0);
		spnMinutes.setValue(0);
		
		txtSource.setEnabled(false);
		txtDestination.setEnabled(false);
		spnHours.setEnabled(false);
		spnMinutes.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);
	}
}
