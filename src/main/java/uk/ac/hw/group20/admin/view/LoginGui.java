package uk.ac.hw.group20.admin.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import uk.ac.hw.group20.admin.controller.LoginController;
import uk.ac.hw.group20.admin.dto.LoginDto;
import uk.ac.hw.group20.admin.dto.LoginResponseDto;
import uk.ac.hw.group20.main.view.HomeGui;
import uk.ac.hw.group20.order.controller.OrderController;
import uk.ac.hw.group20.order.dto.ServerInfoDto;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.order.service.OrderLoader;
import uk.ac.hw.group20.order.service.OrderQueue;
import uk.ac.hw.group20.utils.FileType;

public class LoginGui extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JButton btnLogin;
	private JButton btnReset;
	private JLabel lblInformation;
	private JLabel lblDefaultCredentials;
	private HomeGui homeFrame;

	/**
	 * Create the frame.
	 */
	public LoginGui() {
		setTitle("Coffee Shop App - Edinburg Group 20");
		initializeComponent();
		eventHendler();
		buttonHandler();
	}

	private void buttonHandler() {
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUsername.setText("");
				txtPassword.setText("");
				lblInformation.setText("");
			}
		});
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = txtUsername.getText();
				String password = new String(txtPassword.getPassword());
				lblInformation.setText("");
				
				LoginDto dto = new LoginDto();
				dto.setUsername(username);
				dto.setPassword(password);
				
				LoginController controller = new LoginController();
				LoginResponseDto response = controller.validateUser(dto);
				
				if (response != null && "Y".equals(response.getStatus())) {
				    // Close the login window
				    dispose();
				    
				    ServerInfoDto serverDto = new ServerInfoDto();
				    serverDto.setLblServer1("Idle");
				    serverDto.setLblServer2("Idle");
				    serverDto.setLblServer3("Idle");
				    serverDto.setLblServer4("Idle");
				    
				    OrderController orderController = new OrderController();

				    LinkedList<Order> orderQueue = OrderLoader.loadOrdersFromCSV(FileType.ORDERS);
				    homeFrame = new HomeGui(response.getUserId(), orderQueue, "Y", serverDto, orderController);
				    OrderQueue order = OrderQueue.getInstance();
				    order.registerObserver(homeFrame);
				
				    new Thread(() -> {
				    	orderController.startThreads(response.getUserId(), 1, homeFrame);
				    }).start();

				    SwingUtilities.invokeLater(() -> {
				        homeFrame.setVisible(true);
				    });

				} else {
				    // Display error message in case of failed login
				    lblInformation.setText(response.getMessage());
				}
				
			}
		});

		KeyAdapter enterKeyListener = new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		            btnLogin.doClick();
		        }
		    }
		};

		txtUsername.addKeyListener(enterKeyListener);
		txtPassword.addKeyListener(enterKeyListener);
		btnLogin.addKeyListener(enterKeyListener);

	}

	private void eventHendler() {
		
		
	}

	private void initializeComponent() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JPanel pnlLogin = new JPanel();
		pnlLogin.setBorder(new TitledBorder(null, "Application Login", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(pnlLogin, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlLogin, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
		);
		
		lblUsername = new JLabel("Username:");
		
		lblPassword = new JLabel("Password:");
		
		txtUsername = new JTextField();
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		
		btnLogin = new JButton("Login");
		
		
		
		btnReset = new JButton("Reset");
		
		lblInformation = new JLabel("");
		lblInformation.setForeground(new Color(234, 35, 59));
		
		lblDefaultCredentials = new JLabel("Use username: IMA, password: IMA");
		lblDefaultCredentials.setFont(new Font("Helvetica", Font.ITALIC, 12));
		
		GroupLayout gl_pnlLogin = new GroupLayout(pnlLogin);
		gl_pnlLogin.setHorizontalGroup(
			gl_pnlLogin.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLogin.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_pnlLogin.createParallelGroup(Alignment.LEADING)
						.addComponent(lblInformation, GroupLayout.PREFERRED_SIZE, 365, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_pnlLogin.createSequentialGroup()
							.addGroup(gl_pnlLogin.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblUsername)
								.addComponent(lblPassword))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_pnlLogin.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_pnlLogin.createSequentialGroup()
									.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnReset, 0, 0, Short.MAX_VALUE))
								.addComponent(txtPassword, Alignment.TRAILING)
								.addComponent(txtUsername, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(33, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_pnlLogin.createSequentialGroup()
					.addContainerGap(337, Short.MAX_VALUE)
					.addComponent(lblDefaultCredentials)
					.addGap(18))
		);
		gl_pnlLogin.setVerticalGroup(
			gl_pnlLogin.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLogin.createSequentialGroup()
					.addGap(28)
					.addComponent(lblInformation)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_pnlLogin.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlLogin.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_pnlLogin.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLogin)
						.addComponent(btnReset))
					.addPreferredGap(ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
					.addComponent(lblDefaultCredentials)
					.addContainerGap())
		);
		pnlLogin.setLayout(gl_pnlLogin);
		contentPane.setLayout(gl_contentPane);
		
	}
}