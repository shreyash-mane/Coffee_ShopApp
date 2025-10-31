package uk.ac.hw.group20.main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import uk.ac.hw.group20.admin.model.User;
import uk.ac.hw.group20.admin.service.UserLoader;
import uk.ac.hw.group20.admin.view.LoginGui;
import uk.ac.hw.group20.customer.service.CustomerServiceImpl;
import uk.ac.hw.group20.errorhandler.InvalidInputException;
import uk.ac.hw.group20.logger.Logger;
import uk.ac.hw.group20.order.ShoppingCartManager;
import uk.ac.hw.group20.order.bill.BillManager;
import uk.ac.hw.group20.order.bill.BillResponse;
import uk.ac.hw.group20.order.controller.OrderController;
import uk.ac.hw.group20.order.dto.ServerInfoDto;
import uk.ac.hw.group20.order.dto.ThreadTimerDto;
import uk.ac.hw.group20.order.interfaces.Observer;
import uk.ac.hw.group20.order.model.Order;
import uk.ac.hw.group20.order.model.ShopMenuItem;
import uk.ac.hw.group20.order.model.ShoppingCart;
import uk.ac.hw.group20.order.service.OrderLoader;
import uk.ac.hw.group20.order.service.OrderQueue;
import uk.ac.hw.group20.report.Report;
import uk.ac.hw.group20.utils.CommonMethod;
import uk.ac.hw.group20.utils.FileType;
import uk.ac.hw.group20.utils.IdGenerator;
import uk.ac.hw.group20.utils.LogLevel;
import uk.ac.hw.group20.utils.OrderStatus;
import uk.ac.hw.group20.utils.OrderType;
import uk.ac.hw.group20.utils.Server;

public class HomeGui extends JFrame implements Observer {

    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private JPanel homeContentPane;
    private JButton btnCoffee;
    private JScrollPane scrollPane;
    private JButton btnOther;
    private JButton btnFood;
    private JList lstMenuItem;
    private DefaultListModel model;
    public String loginUserId;
    private JLabel lblUserId;
    private JPanel pnlMainLeft;
    private JPanel pnlMainRight;
    private JMenuBar menuBar;
    private JMenu mnHome;
    private JMenuItem menuItemLogout;
    private JMenuItem menuItemExit;
    private JLabel lblItemSelected;
    private JLabel lblCategory;
    private JLabel lblQuantity;
    private JLabel lblPrice;
    private JTextField txtSelectedItem;
    private JTextField txtCategory;
    private JTextField txtQuantity;
    private JTextField txtPrice;
    private JButton btnAddItem;
    private JTable tblInformation;
    private DefaultTableModel tableModel;
    
    CommonMethod commonMethod = new CommonMethod();
    CustomerServiceImpl customerService = new CustomerServiceImpl();
    List<Object[]> tableRows = new ArrayList<>();
    private JLabel lblItemId;
    private JButton btnCheckout;
    private JButton btnReloadCart;
    private JButton btnRemoveCart;
    private JMenu mnOrder;
    private JMenuItem menuItemProcess;
    private JLabel lblTableTitle;
    private JMenu menuReport;
    private JMenuItem menuItemOrdersReport;
    private JLabel lblSelectCustomer;
    private JComboBox cbCustomerId;
    private JTable tblOrders;
    private JScrollPane spOrder;
    private JLabel lblShoppingList;
    private JLabel lblQueue = new JLabel("There are currently 0 people waiting in the queue:");
    private JLabel lblQueue_1;
    
    JScrollPane sclRight;
    GroupLayout glPnlMainRight;
    GroupLayout gl_homeContentPane;
    private JScrollPane sclServer1;
    private JScrollPane sclServer2;
    private JLabel lblHeader;
    private JScrollPane sclServer4;
    private JScrollPane sclServer3;
    
    int numberOfServer = 4;

    LinkedList<Order> orders = new LinkedList<>();
    private String firstLoading = null;
    private JTextArea txtServer1;
    private JTextArea txtServer2;
    private JTextArea txtServer3;
    private JTextArea txtServer4;
    private JPanel panel;
    private JButton btnChangeRuntimeValues;
    private JTextField txtProducerTimer;
    private JLabel lblTimer;
    private JLabel lblConsumerTimer;
    
    private OrderController orderController;
    private JLabel lblChangeTimer;
    private JLabel lblServer;
    private JTextField txtServer1Timer;
    private JLabel lblServer_3;
    private JTextField txtServer2Timer;
    private JLabel lblServer_1;
    private JTextField txtServer3Timer;
    private JLabel lblServer_2;
    private JTextField txtBaristaTimer;
    private ThreadTimerDto timerDto;
    
    public HomeGui(String userId, LinkedList<Order> orderQueue, String firstLoading, ServerInfoDto dto, OrderController orderController) {
        setTitle("Coffee Shop App - Edinburg Group 20");
        initializeComponent(userId);
        menuEventHendler();
        buttonHandler();
        this.firstLoading = firstLoading;
        
        orders = orderQueue;
        this.orderController = orderController;
        this.timerDto = new ThreadTimerDto();
        
        gl_homeContentPane = new GroupLayout(homeContentPane);
        gl_homeContentPane.setHorizontalGroup(
            gl_homeContentPane.createParallelGroup(Alignment.TRAILING)
                .addGroup(Alignment.LEADING, gl_homeContentPane.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlMainLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(pnlMainRight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gl_homeContentPane.setVerticalGroup(
            gl_homeContentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_homeContentPane.createSequentialGroup()
                    .addGap(38)
                    .addGroup(gl_homeContentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(pnlMainLeft, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlMainRight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        gl_homeContentPane.setAutoCreateGaps(true);
        gl_homeContentPane.setAutoCreateContainerGaps(true);
        
        sclRight = new JScrollPane();
        sclRight.setPreferredSize(new Dimension(0, 200)); // Increased height for more space
        
        sclServer1 = new JScrollPane();
        sclServer2 = new JScrollPane();
        sclServer3 = new JScrollPane();
        sclServer4 = new JScrollPane();
        
        sclServer1.setPreferredSize(new Dimension(0, 100)); // Reduced height to 100 pixels
        sclServer2.setPreferredSize(new Dimension(0, 100));
        sclServer3.setPreferredSize(new Dimension(0, 100));
        sclServer4.setPreferredSize(new Dimension(0, 100));
        
        sclServer1.setViewportBorder(new TitledBorder(null, "Server 1", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        sclServer2.setViewportBorder(new TitledBorder(null, "Server 2", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        sclServer3.setViewportBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Server 3", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        sclServer4.setViewportBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Barista", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
        panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Runtime Controls: Servers update automatically based on number of pending orders", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
        glPnlMainRight = new GroupLayout(pnlMainRight);
        glPnlMainRight.setHorizontalGroup(
        	glPnlMainRight.createParallelGroup(Alignment.LEADING)
        		.addGroup(glPnlMainRight.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(glPnlMainRight.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblTableTitle)
        				.addComponent(sclRight, GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
        				.addComponent(lblQueue_1, GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
        				.addGroup(glPnlMainRight.createSequentialGroup()
        					.addGroup(glPnlMainRight.createParallelGroup(Alignment.TRAILING)
        						.addComponent(sclServer1, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
        						.addComponent(sclServer3, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(glPnlMainRight.createParallelGroup(Alignment.LEADING)
        						.addComponent(sclServer2, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        						.addComponent(sclServer4, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))))
        			.addContainerGap())
        		.addGroup(glPnlMainRight.createSequentialGroup()
        			.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGap(36))
        );
        glPnlMainRight.setVerticalGroup(
        	glPnlMainRight.createParallelGroup(Alignment.LEADING)
        		.addGroup(glPnlMainRight.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblTableTitle)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblQueue_1)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(sclRight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(glPnlMainRight.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(sclServer2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(sclServer1, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(glPnlMainRight.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(sclServer4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(sclServer3, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
        			.addGap(14)
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        glPnlMainRight.setAutoCreateGaps(true);
        glPnlMainRight.setAutoCreateContainerGaps(true);
        
        lblTimer = new JLabel("Threads Timer (ms)");
        
        txtProducerTimer = new JTextField();
        txtProducerTimer.setText(String.valueOf(timerDto.getProducerTimer()));
        txtProducerTimer.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtProducerTimer.getPreferredSize().height));
        
        lblConsumerTimer = new JLabel("Producer");
        
        lblChangeTimer = new JLabel("");
        lblChangeTimer.setForeground(new Color(120, 182, 125));
        
        lblServer = new JLabel("Server 1");
        
        txtServer1Timer = new JTextField();
        txtServer1Timer.setText(String.valueOf(timerDto.getServer1Timer()));
        txtServer1Timer.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtServer1Timer.getPreferredSize().height));
        
        lblServer_3 = new JLabel("Server 2");
        
        txtServer2Timer = new JTextField();
        txtServer2Timer.setText(String.valueOf(timerDto.getServer2Timer()));
        txtServer2Timer.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtServer2Timer.getPreferredSize().height));
        
        lblServer_1 = new JLabel("Server 3");
        
        txtServer3Timer = new JTextField();
        txtServer3Timer.setText(String.valueOf(timerDto.getServer3Timer()));
        txtServer3Timer.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtServer3Timer.getPreferredSize().height));
        
        lblServer_2 = new JLabel("Barista");
        
        txtBaristaTimer = new JTextField();
        txtBaristaTimer.setText(String.valueOf(timerDto.getBaristaTimer()));
        txtBaristaTimer.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtBaristaTimer.getPreferredSize().height));
        
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
            gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
                        .addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
                            .addComponent(lblTimer)
                            .addGap(18)
                            .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblConsumerTimer)
                                .addComponent(txtProducerTimer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18)
                            .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblServer)
                                .addComponent(txtServer1Timer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18)
                            .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblServer_3)
                                .addComponent(txtServer2Timer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18)
                            .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblServer_1)
                                .addComponent(txtServer3Timer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18)
                            .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblServer_2)
                                .addComponent(txtBaristaTimer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(16))
                        .addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
                            .addComponent(lblChangeTimer)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnChangeRuntimeValues)
                            .addGap(12))))
        );
        gl_panel.setVerticalGroup(
            gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_panel.createSequentialGroup()
                            .addComponent(lblServer_2)
                            .addGap(6)
                            .addComponent(txtBaristaTimer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addComponent(lblServer_1)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(txtServer3Timer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addComponent(lblServer_3)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(txtServer2Timer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addComponent(lblServer)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(txtServer1Timer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addComponent(lblConsumerTimer)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(txtProducerTimer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTimer))))
                    .addGap(5)
                    .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnChangeRuntimeValues)
                        .addComponent(lblChangeTimer)))
        );
        panel.setLayout(gl_panel);
        
        txtServer1 = new JTextArea();
        txtServer2 = new JTextArea();
        txtServer3 = new JTextArea();
        txtServer4 = new JTextArea();
        
        updateOrderDisplay(orders, dto, userId, null);
        
        homeContentPane.setLayout(gl_homeContentPane);
        pnlMainRight.setLayout(glPnlMainRight);
    }
    
    public void loadOrderData(LinkedList<Order> orderQueue, ServerInfoDto srv, String userId) {
        Logger.logMessage(LogLevel.INFO, "There are currently " + (orderQueue.isEmpty() ? 0 : orderQueue.size()) + " people waiting in the queue:");
        lblQueue_1.setText("There are currently " + (orderQueue.isEmpty() ? 0 : orderQueue.size()) + " people waiting in the queue:");
        tblInformation = commonMethod.getOrderTable(orderQueue);
        sclRight.setViewportView(tblInformation);
        
        if (srv != null) {
            txtServer1.setText(srv.getLblServer1());
            txtServer2.setText(srv.getLblServer2());
            txtServer3.setText(srv.getLblServer3());
            txtServer4.setText(srv.getLblServer4());
        }
        sclServer1.setViewportView(txtServer1);
        sclServer2.setViewportView(txtServer2);
        sclServer3.setViewportView(txtServer3);
        sclServer4.setViewportView(txtServer4);

        if ("Y".equals(this.firstLoading)) {
            //SwingUtilities.invokeLater(() -> new OrderController().start(userId, 2));
        }
    }
    
    public void updateOrderDisplay(LinkedList<Order> orderQueue, ServerInfoDto srv, String userId, Server currentServer) {
        SwingUtilities.invokeLater(() -> {
            int queueSize = orderQueue.size();
            tblInformation = commonMethod.getOrderTable(orderQueue);
            sclRight.setViewportView(tblInformation);

            if (srv != null) {
                if (Server.SERVER_ONE.equals(currentServer)) {
                    updateServerLabel(txtServer1, srv.getLblServer1());
                } else if (Server.SERVER_TWO.equals(currentServer)) {
                    updateServerLabel(txtServer2, srv.getLblServer2());
                } else if (Server.SERVER_THREE.equals(currentServer)) {
                    updateServerLabel(txtServer3, srv.getLblServer3());
                } else if (Server.SERVER_FOUR.equals(currentServer)) {
                    updateServerLabel(txtServer4, srv.getLblServer4());
                } else {
                    updateServerLabel(txtServer1, srv.getLblServer1());
                    updateServerLabel(txtServer2, srv.getLblServer2());
                    updateServerLabel(txtServer3, srv.getLblServer3());
                    updateServerLabel(txtServer4, srv.getLblServer4());
                }
            } else {
                updateServerLabel(txtServer1, "");
                updateServerLabel(txtServer2, "");
                updateServerLabel(txtServer3, "");
                updateServerLabel(txtServer4, "");
            }

            sclServer1.setViewportView(txtServer1);
            sclServer2.setViewportView(txtServer2);
            sclServer3.setViewportView(txtServer3);
            sclServer4.setViewportView(txtServer4);
        });
    }

    private void updateServerLabel(JTextArea textArea, String newText) {
        if (!textArea.getText().equals(newText)) {
            textArea.setText(newText);
        }
    }

    private void initializeComponent(String userId) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setPreferredSize(new Dimension(0, 30));

        mnHome = new JMenu("Home");
        menuBar.add(mnHome);

        menuItemLogout = new JMenuItem("Logout");
        mnHome.add(menuItemLogout);

        menuItemExit = new JMenuItem("Close");
        mnHome.add(menuItemExit);

        mnOrder = new JMenu("Orders");
        menuBar.add(mnOrder);

        menuItemProcess = new JMenuItem("View Order");
        mnOrder.add(menuItemProcess);

        menuReport = new JMenu("Report");
        menuBar.add(menuReport);

        menuItemOrdersReport = new JMenuItem("Orders");
        menuReport.add(menuItemOrdersReport);

        homeContentPane = new JPanel();
        homeContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(homeContentPane);

        lblQueue_1 = new JLabel("There are currently 0 people waiting in the queue:");

        pnlMainLeft = new JPanel();
        pnlMainLeft.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), 
            "Customer's Panel", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

        pnlMainRight = new JPanel();
        pnlMainRight.setBackground(new Color(238, 238, 238));
        pnlMainRight.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), 
            "Saver's Panel", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

        btnCoffee = new JButton("Beverage");
        btnCoffee.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnFood = new JButton("Food Items");
        btnFood.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnOther = new JButton("Other Items");
        btnOther.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnChangeRuntimeValues = new JButton("Submit Values");

        lblHeader = new JLabel("Browse by Category");
        scrollPane = new JScrollPane();
        scrollPane.setMinimumSize(new Dimension(200, 100));

        lblUserId = new JLabel(userId);
        lblItemSelected = new JLabel("Select Item");
        lblCategory = new JLabel("Category");
        lblQuantity = new JLabel("Quantity");
        lblPrice = new JLabel("Price");
        lblTableTitle = new JLabel("");

        txtSelectedItem = new JTextField();
        txtSelectedItem.setEditable(false);
        txtSelectedItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtSelectedItem.getPreferredSize().height));

        txtCategory = new JTextField();
        txtCategory.setEditable(false);
        txtCategory.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtCategory.getPreferredSize().height));

        txtQuantity = new JTextField();
        txtQuantity.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtQuantity.getPreferredSize().height));

        txtPrice = new JTextField();
        txtPrice.setEditable(false);
        txtPrice.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtPrice.getPreferredSize().height));

        btnAddItem = new JButton("Add Item");
        lblItemId = new JLabel("");

        lblSelectCustomer = new JLabel("Customer");

        User user = UserLoader.getUserById(userId.trim());
        Logger.logMessage(LogLevel.INFO, "User ID: " + userId + " and other login in details: " + user);

        String[] userArray = customerService.getCusterDropDownList();
        cbCustomerId = new JComboBox(userArray);
        cbCustomerId.setVisible(true);
        lblSelectCustomer.setVisible(true);

        btnReloadCart = new JButton("Reload Cart");
        btnRemoveCart = new JButton("Remove All Items");
        btnCheckout = new JButton("Submit Order");

        spOrder = new JScrollPane();
        spOrder.setToolTipText("View Orders");

        lblShoppingList = new JLabel("Shopping List");

        GroupLayout glPnlMainLeft = new GroupLayout(pnlMainLeft);
        glPnlMainLeft.setHorizontalGroup(
            glPnlMainLeft.createParallelGroup(Alignment.LEADING)
                .addGroup(glPnlMainLeft.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(glPnlMainLeft.createParallelGroup(Alignment.LEADING)
                        .addGroup(glPnlMainLeft.createSequentialGroup()
                            .addComponent(lblHeader)
                            .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblUserId))
                        .addGroup(glPnlMainLeft.createSequentialGroup()
                            .addComponent(btnCoffee, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnFood, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnOther, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(glPnlMainLeft.createSequentialGroup()
                            .addGroup(glPnlMainLeft.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblItemSelected)
                                .addComponent(txtSelectedItem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(glPnlMainLeft.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblCategory)
                                .addComponent(txtCategory, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(glPnlMainLeft.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblQuantity)
                                .addComponent(txtQuantity, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(glPnlMainLeft.createParallelGroup(Alignment.LEADING)
                                .addComponent(lblPrice)
                                .addComponent(txtPrice, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(glPnlMainLeft.createSequentialGroup()
                            .addComponent(lblSelectCustomer)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(cbCustomerId, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnAddItem))
                        .addComponent(lblShoppingList)
                        .addComponent(spOrder, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(glPnlMainLeft.createSequentialGroup()
                            .addComponent(btnReloadCart, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnRemoveCart, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnCheckout, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addContainerGap())
        );

        glPnlMainLeft.setVerticalGroup(
            glPnlMainLeft.createParallelGroup(Alignment.LEADING)
                .addGroup(glPnlMainLeft.createSequentialGroup()
                    .addGroup(glPnlMainLeft.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblHeader)
                        .addComponent(lblUserId))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(glPnlMainLeft.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnCoffee)
                        .addComponent(btnFood)
                        .addComponent(btnOther))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(glPnlMainLeft.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblItemSelected)
                        .addComponent(lblCategory)
                        .addComponent(lblQuantity)
                        .addComponent(lblPrice))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(glPnlMainLeft.createParallelGroup(Alignment.BASELINE)
                        .addComponent(txtSelectedItem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCategory, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtQuantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(glPnlMainLeft.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblSelectCustomer)
                        .addComponent(cbCustomerId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAddItem))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(lblShoppingList)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(spOrder, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(glPnlMainLeft.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnReloadCart)
                        .addComponent(btnRemoveCart)
                        .addComponent(btnCheckout))
                    .addContainerGap())
        );

        glPnlMainLeft.setAutoCreateGaps(true);
        glPnlMainLeft.setAutoCreateContainerGaps(true);

        tblOrders = new JTable();
        spOrder.setViewportView(tblOrders);

        lstMenuItem = new JList();
        lstMenuItem.setBorder(new TitledBorder(null, "Click the menu item to select", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        lstMenuItem.setToolTipText("Click to select menu item from the list");
        scrollPane.setViewportView(lstMenuItem);
        pnlMainLeft.setLayout(glPnlMainLeft);
    }

    private void menuEventHendler() {
        menuItemLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginGui loginGui = new LoginGui();
                loginGui.setVisible(true);
            }
        });
        
        menuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Report.showOrderReportDialog();
            }
        });
        
        lstMenuItem.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String selectedValue = lstMenuItem.getSelectedValue() == null ? "" : lstMenuItem.getSelectedValue().toString();
                String[] selectedArray = selectedValue.split(",");
                
                if (selectedArray.length == 3) {
                    String categorySelected = selectedArray[0].trim().substring(0, 3);
                    String category = null;
                    
                    if ("OTH".equals(categorySelected)) category = "Others";
                    if ("BEV".equals(categorySelected)) category = "Beverage";
                    if ("FOD".equals(categorySelected)) category = "Food";
                    
                    lblItemId.setText(selectedArray[0].trim());
                    lblItemId.setVisible(false);
                    txtSelectedItem.setText(selectedArray[1].trim());
                    txtCategory.setText(category);
                    txtPrice.setText(selectedArray[2].trim());
                    Double.parseDouble(selectedArray[2].trim());
                    txtQuantity.setText("1");
                }
            }
        });
        
        menuItemProcess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String customerId = lblUserId.getText().trim();
                LinkedList<Order> orders = OrderLoader.loadOrdersFromCSV(FileType.ORDERS);
                
                if (orders == null || orders.size() < 1) {
                    JOptionPane.showMessageDialog(pnlMainRight, "There are no orders to process!", "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                tableRows = new ArrayList<>();
                for (Order order : orders) {
                    tableRows.add(new Object[]{
                        order.getOrderId(),
                        DATE_FORMAT.format(order.getDateCreated()),
                        order.getCustomerId(),
                        order.getMenuItemList(),
                        order.getSubTotal(),
                        order.getStatus()});
                }
                
                String[] columnNames = {"Order ID", "Date", "Customer", "Menu Items", "Total Price", "Status"};
                tableModel = commonMethod.getTableModel(columnNames, tableRows, null);
                lblTableTitle.setText("Order Management");
                
                tblInformation.setModel(tableModel);
                tblInformation.setVisible(true);
            }
        });
        
        menuItemOrdersReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String customerId = lblUserId.getText().trim();
                LinkedList<Order> orders = OrderLoader.loadOrdersFromCSV(FileType.ORDER_ARCHIVE);
                
                tableRows = new ArrayList<>();
                for (Order order : orders) {
                    for (ShopMenuItem item : order.getMenuItemList()) {
                        tableRows.add(new Object[]{
                            item.getName(),
                            item.getCategoryId(),
                            item.getQuantity(),
                            item.getCurrentPrice(),
                            item.getCurrentPrice() * item.getQuantity(),
                            order.getOrderId(),
                            order.getSavedBy(),
                            DATE_FORMAT.format(order.getDateCreated()),
                            DATE_FORMAT.format(order.getDateSaved())
                        });
                    }
                }
                
                String[] columnNames = {"Menu Item Name", "Category", "Quantity", "Unit Price", "Total Price", "Order ID", "Saved By", "Order Date", "Saved Date"};
                tableModel = commonMethod.getTableModel(columnNames, tableRows, "Report");
                lblTableTitle.setText("Order Report");
                
                tblInformation.setModel(tableModel);
                tblInformation.setVisible(true);
            }
        });
    }
    
    private void buttonHandler() {
        btnCoffee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model = commonMethod.getModel("BEVERAGE");
                lstMenuItem.setModel(model);
            }
        });
        
        btnFood.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model = commonMethod.getModel("FOOD");
                lstMenuItem.setModel(model);
                lstMenuItem.clearSelection();
            }
        });
        
        btnOther.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model = commonMethod.getModel("OTHERS");
                lstMenuItem.setModel(model);
                lstMenuItem.clearSelection();
            }
        });
        
        btnAddItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = txtSelectedItem.getText().trim();
                String category = txtCategory.getText().trim();
                String customerId = lblUserId.getText().trim();
                String selectedCustomer = (String) cbCustomerId.getSelectedItem();
                
                if (selectedCustomer == null && customerId == null) {
                    JOptionPane.showMessageDialog(null, "Please Select the customer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
                
                if (selectedCustomer != null) {
                    customerId = selectedCustomer.split(" - ")[0].trim();
                }
                
                int quantity;
                double price;

                try {
                    quantity = Integer.parseInt(txtQuantity.getText().trim());
                    price = Double.parseDouble(txtPrice.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers for Quantity and Price.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                ShoppingCart newItem = new ShoppingCart(
                    "SC001",
                    lblItemId.getText(),
                    name,
                    category,
                    quantity,
                    price,
                    new Date(),
                    customerId);
                ShoppingCartManager.addItemToCart(newItem);
                
                List<ShoppingCart> cartItems = ShoppingCartManager.getCartItemsByCustomerId(customerId);
                
                tableRows = new ArrayList<>();
                for (ShoppingCart cartItem : cartItems) {
                    tableRows.add(new Object[]{
                        cartItem.getItemName(),
                        cartItem.getCategory(),
                        cartItem.getQuantity(),
                        cartItem.getUnitPrice(),
                        cartItem.getQuantity() * cartItem.getUnitPrice(),
                        "Remove"});
                }
                
                String[] columnNames = {"Name", "Category", "Quantity", "Price", "Total Price", "Action"};
                tableModel = commonMethod.getTableModel(columnNames, tableRows, null);
                
                tblOrders.setModel(tableModel);
                tblOrders.setVisible(true);

                txtSelectedItem.setText("");
                txtCategory.setText("");
                txtQuantity.setText("");
                txtPrice.setText("");
            }
        });
        
        btnReloadCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String customerId = lblUserId.getText().trim();
                List<ShoppingCart> cartItems = ShoppingCartManager.getCartItemsByCustomerId(customerId);
                
                if (cartItems.isEmpty() || cartItems.size() < 1) {
                    JOptionPane.showMessageDialog(null, "There are no items to be loaded.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    tblOrders.setVisible(false);
                    return;
                }
                
                tableRows = new ArrayList<>();
                for (ShoppingCart cartItem : cartItems) {
                    tableRows.add(new Object[]{
                        cartItem.getItemName(),
                        cartItem.getCategory(),
                        cartItem.getQuantity(),
                        cartItem.getUnitPrice(),
                        cartItem.getQuantity() * cartItem.getUnitPrice(),
                        "Remove"});
                }
                
                if (tableRows.isEmpty()) {
                    tableModel = null;
                } else {
                    String[] columnNames = {"Name", "Category", "Quantity", "Price", "Total Price", "Action"};
                    tableModel = commonMethod.getTableModel(columnNames, tableRows, null);
                }
                
                tblOrders.setModel(tableModel);
                tblOrders.setVisible(true);

                txtSelectedItem.setText("");
                txtCategory.setText("");
                txtQuantity.setText("");
                txtPrice.setText("");
            }
        });
        
        btnCheckout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String customerId = null;
                String selectedCustomer = (String) cbCustomerId.getSelectedItem();
                if (selectedCustomer == null) {
                    JOptionPane.showMessageDialog(null, "Please Select the customer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (selectedCustomer != null) {
                    customerId = selectedCustomer.split(" - ")[0].trim();
                }
                
                List<ShoppingCart> cartItems = ShoppingCartManager.getCartItemsByCustomerId(customerId);
                
                if (cartItems.isEmpty() || cartItems.size() < 1) {
                    JOptionPane.showMessageDialog(null, "There are no items to checkout.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                List<ShopMenuItem> orderMenuItem = new ArrayList<>();
                StringBuilder bill = new StringBuilder("Your Bill\n");
                bill.append("----------------------------\n");
                for (ShoppingCart cartItem : cartItems) {
                    double totalPrice = cartItem.getQuantity() * cartItem.getUnitPrice();
                    
                    try {
                        orderMenuItem.add(new ShopMenuItem(cartItem.getItemId(), cartItem.getItemName(), cartItem.getCategory(), "Description", cartItem.getQuantity(), cartItem.getUnitPrice()));
                    } catch (InvalidInputException ex) {
                        Logger.logMessage(LogLevel.ERROR, ex.getMessage());
                    }
                    
                    bill.append(cartItem.getItemName() + ", " +
                            cartItem.getCategory() + ", " +
                            cartItem.getQuantity() + ", " +
                            "$" + String.format("%.2f", cartItem.getUnitPrice()) + ", " +
                            "$" + String.format("%.2f", totalPrice) + "\n");
                }

                bill.append("----------------------------\n");
                
                BillManager billManager = new BillManager();
                BillResponse billResponse = billManager.getCustomerBill(cartItems);

                bill.append(String.format("Total Bill : $%.2f\n", billResponse.getTotalAmount()));
                bill.append(String.format("Discount : $%.2f\n", billResponse.getDiscount()));
                bill.append(String.format("Tax : $%.2f\n", billResponse.getTaxAmount()));
                bill.append(String.format("Payable Amount: $%.2f\n", billResponse.getPayableAmount()));
                bill.append("----------------------------\n");
                bill.append("Thank you for shopping!\n");

                JOptionPane.showMessageDialog(pnlMainRight, bill, "Bill Confirmation", JOptionPane.INFORMATION_MESSAGE);
                
                String orderId = IdGenerator.generateIdByItem("ORDER");
                Date currentDate = new Date();
                Order newOrder = null;
                try {
                    newOrder = new Order(orderId, currentDate, customerId, orderMenuItem, billResponse.getPayableAmount(), billResponse.getDiscount(), OrderStatus.PENDING, OrderType.ONLINE);
                } catch (InvalidInputException ex) {
                    Logger.logMessage(LogLevel.INFO, ex.getMessage());
                }
                boolean addOrder = OrderLoader.addOrder(newOrder);
                if (addOrder) {
                    ShoppingCartManager.clearCart(customerId);
                    JOptionPane.showMessageDialog(pnlMainRight, "Success Placed an order, Thank you for shopping with us :)", "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    tblOrders.setVisible(false);
                }
            }
        });
        
        btnRemoveCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String customerId = lblUserId.getText().trim();
                int response = JOptionPane.showConfirmDialog(pnlMainRight, "Are you sure you want to remove all shopping items", "Bill Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    ShoppingCartManager.clearCart(customerId);
                    tblOrders.setVisible(false);
                }
            }
        });
        
        btnChangeRuntimeValues.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ThreadTimerDto timerDto = new ThreadTimerDto();
                try {
                    int consumerTimer = Integer.parseInt(txtProducerTimer.getText().trim());
                    int server1Timer = Integer.parseInt(txtServer1Timer.getText().trim());
                    int server2Timer = Integer.parseInt(txtServer2Timer.getText().trim());
                    int server3Timer = Integer.parseInt(txtServer3Timer.getText().trim());
                    int baristaTimer = Integer.parseInt(txtBaristaTimer.getText().trim());
                    
                    timerDto.setProducerTimer(consumerTimer);
                    timerDto.setServer1Timer(server1Timer);
                    timerDto.setServer2Timer(server2Timer);
                    timerDto.setServer3Timer(server3Timer);
                    timerDto.setBaristaTimer(baristaTimer);
                    
                    if (orderController != null) {
                        String response = orderController.updateThreadSleepTimer(timerDto);
                        lblChangeTimer.setText(response);
                    } else {
                        lblChangeTimer.setText("Failed to Updated Controls");
                        Logger.logMessage(LogLevel.INFO, "OrderController is not initialized yet.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number for the timer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    @Override
    public void update(OrderQueue orderQueue) {
        SwingUtilities.invokeLater(() -> {
            int queueSize = orderQueue.getQueueSize();
            //Logger.logMessage("There are currently " + queueSize + " people waiting in the queue:");
            lblQueue_1.setText("There are currently " + queueSize + " people waiting in the queue:");
        });
    }
}