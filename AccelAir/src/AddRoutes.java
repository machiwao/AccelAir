import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

import javax.swing.JLabel;

public class AddRoutes {

	JFrame frmAddRoutes;
	private JSpinner spnHours;
	private JSpinner spnMinutes;
	private JTextField txtSource;
	private JTextField txtDestination;
	ImageIcon logo = new ImageIcon(this.getClass().getClassLoader().getResource("appLogo.png"));
	ImageIcon banner = new ImageIcon(this.getClass().getClassLoader().getResource("addRoutes.png"));

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
	public AddRoutes() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAddRoutes = new JFrame();
		frmAddRoutes.getContentPane().setBackground(new Color(252, 252, 252));
		frmAddRoutes.setResizable(false);
		frmAddRoutes.setIconImage(logo.getImage());
		frmAddRoutes.setTitle("AccelAir - Add Routes");
		frmAddRoutes.setBounds(100, 100, 480, 360);
		frmAddRoutes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAddRoutes.getContentPane().setLayout(null);
		
		txtSource = new JTextField();
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
		txtSource.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		txtSource.setBounds(20, 177, 105, 25);
		frmAddRoutes.getContentPane().add(txtSource);
		txtSource.setColumns(10);
		
		txtDestination = new JTextField();
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
		txtDestination.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		txtDestination.setColumns(10);
		txtDestination.setBounds(143, 177, 105, 25);
		frmAddRoutes.getContentPane().add(txtDestination);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnSubmit.setFont(new Font("Montserrat", Font.PLAIN, 18));
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double hours = Double.parseDouble(spnHours.getValue().toString());
				double minutes = Double.parseDouble(spnMinutes.getValue().toString()) / 60.0;
				if(txtSource.getText().isBlank() || txtDestination.getText().isBlank())
        			JOptionPane.showMessageDialog(frmAddRoutes, "Please fill in the necessary information", "Empty Fields Detected", JOptionPane.ERROR_MESSAGE);
				else if(txtSource.getText().length() != 3 || txtDestination.getText().length() != 3)
					JOptionPane.showMessageDialog(frmAddRoutes, "Please enter a valid airport 3-Letter Code", "Invalid Airport Entry", JOptionPane.ERROR_MESSAGE);
				else if(spnHours.getValue().toString().equals("0") && spnMinutes.getValue().toString().equals("0"))
					JOptionPane.showMessageDialog(frmAddRoutes, "Please enter a valid flight duration", "Invalid Flight Duration", JOptionPane.ERROR_MESSAGE);
               	else {
               		boolean isExisting = false;
               		for(String[] route : InitializeRoutes.routesList) {
    					if(txtSource.getText().equals(route[0]) && txtDestination.getText().equals(route[1])) {
    						isExisting = true;
    					}
    				}
               		if(isExisting) {
               			JOptionPane.showMessageDialog(frmAddRoutes, "Route already exists", "Invalid entry", JOptionPane.ERROR_MESSAGE);
               		}else {
	               		try {
	               			InitializeRoutes.routesList.add(new String[] {txtSource.getText().toUpperCase(), txtDestination.getText().toUpperCase(),String.valueOf(hours + minutes)});
	               			BufferedWriter updateRoutes = new BufferedWriter(new FileWriter("RoutesList.txt", true));
	    					updateRoutes.write(txtSource.getText().toUpperCase() + "\t" + txtDestination.getText().toUpperCase() + "\t" + (hours + minutes));
	    					updateRoutes.newLine();
	    					updateRoutes.close();
	    					if(InitializeRoutes.travelMap.findNode(txtSource.getText()) == null)
	    						InitializeRoutes.travelMap.addNode(new Node(txtDestination.getText()));
	    					if(InitializeRoutes.travelMap.findNode(txtSource.getText()) == null)
	    						InitializeRoutes.travelMap.addNode(new Node(txtSource.getText()));
	    					InitializeRoutes.travelMap.findNode(txtSource.getText()).addDestination(InitializeRoutes.travelMap.findNode(txtDestination.getText()), (hours + minutes));
	    					txtDestination.setText(null);
	    					txtSource.setText(null);
	    					spnHours.setValue(0);
	    					spnMinutes.setValue(0);
	    					JOptionPane.showMessageDialog(frmAddRoutes, "Registered Successfully!", "Succesful Registration", JOptionPane.INFORMATION_MESSAGE);
	    				} catch (IOException e1) {
	    					e1.printStackTrace();
	    				}
               		}
               	}
			}
		});
		btnSubmit.setBounds(70, 246, 100, 25);
		frmAddRoutes.getContentPane().add(btnSubmit);
		
		JButton btnClear = new JButton("Reset");
		btnClear.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnClear.setFont(new Font("Montserrat", Font.PLAIN, 18));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtDestination.setText(null);
				txtSource.setText(null);
				spnHours.setValue(0);
				spnMinutes.setValue(0);
			}
		});
		btnClear.setBounds(180, 246, 100, 25);
		frmAddRoutes.getContentPane().add(btnClear);
		
		JButton btnHome = new JButton("Home");
		btnHome.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		btnHome.setFont(new Font("Montserrat", Font.PLAIN, 18));
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main main = new Main();
				main.frmSwiftair.setVisible(true);
				frmAddRoutes.dispose();
			}
		});
		btnHome.setBounds(290, 246, 100, 25);
		frmAddRoutes.getContentPane().add(btnHome);
		
		spnHours = new JSpinner();
		spnHours.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		spnHours.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		spnHours.setModel(new SpinnerNumberModel(0, 0, 24, 1));
		spnHours.setBounds(268, 177, 75, 25);
		frmAddRoutes.getContentPane().add(spnHours);
		
		spnMinutes = new JSpinner();
		spnMinutes.setFont(new Font("Montserrat Light", Font.PLAIN, 18));
		spnMinutes.setBorder(new LineBorder(new Color(125, 103, 126), 2, true));
		spnMinutes.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		spnMinutes.setBounds(361, 177, 75, 25);
		frmAddRoutes.getContentPane().add(spnMinutes);
		
		JLabel lblFrom = new JLabel("From");
		lblFrom.setForeground(new Color(66, 69, 183));
		lblFrom.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		lblFrom.setBounds(20, 150, 105, 25);
		frmAddRoutes.getContentPane().add(lblFrom);
		
		JLabel lblTo = new JLabel("To");
		lblTo.setForeground(new Color(66, 69, 183));
		lblTo.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 20));
		lblTo.setBounds(143, 150, 105, 25);
		frmAddRoutes.getContentPane().add(lblTo);
		
		JLabel lblHours = new JLabel("Hours");
		lblHours.setForeground(new Color(66, 69, 183));
		lblHours.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 16));
		lblHours.setBounds(268, 152, 74, 25);
		frmAddRoutes.getContentPane().add(lblHours);
		
		JLabel lblMins = new JLabel("Minutes");
		lblMins.setForeground(new Color(66, 69, 183));
		lblMins.setFont(new Font("Montserrat SemiBold", Font.PLAIN, 16));
		lblMins.setBounds(362, 152, 74, 25);
		frmAddRoutes.getContentPane().add(lblMins);
		
		JLabel lblImage = new JLabel("");
		lblImage.setIcon(new ImageIcon(banner.getImage()));
		lblImage.setBounds(80, 27, 300, 94);
		frmAddRoutes.getContentPane().add(lblImage);
	}
}
