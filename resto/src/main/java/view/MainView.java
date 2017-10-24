package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box.Filler;
import javax.swing.border.EmptyBorder;

import controller.LauncherController;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.FlowLayout;

import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;

import dto.Constants;
import dto.Parameter;
import dto.RequestDTO;
import dto.ResponseDTO;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


import java.awt.Component;
import java.io.File;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class MainView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private LauncherController controller;
	private final JTextField urlTextField;
	private RequestDTO requestDTO;
	private final JButton sendButton;
	private final JButton saveButton;

	private  JComboBox  selectedItem ;
	private JLabel stateLabel;
	private JTextArea outputTextArea;
	JComboBox methodComboBox;

	private JTable headerJTable;
	private DefaultTableModel headerModel;
	private static String [] columnsName = {"Key","Value"};
	private List<Parameter> headerParameters;
	private JTable bodyTable;
	private DefaultTableModel bodyModel;
	private List<Parameter> bodyParameters;
	

	/**
	 * Create the frame.
	 */
	/**
	 * @param controller
	 */
	public MainView(LauncherController controller) {
		this.controller = controller;
		requestDTO = new RequestDTO();
		requestDTO.setMethod(Constants.METHOD_GET);
		headerParameters = new ArrayList<Parameter>();
		bodyParameters = new ArrayList<Parameter>();
	
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Resto Client");
		setBounds(100, 100, 650, 700);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBounds(5, 11, 629, 37);
		menuPanel.setBackground(Color.RED);
		contentPane.add(menuPanel);
		menuPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		menuPanel.add(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		//declare jmenuItem
		JMenuItem openMenuItem = new JMenuItem("Open Service");
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("OPEN MENU");
				RequestDTO request=MainView.this.controller.loadRequestDTO(openService());
				viewRequest(request);
			}
		});
		fileMenu.add(openMenuItem);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBorder(new LineBorder(new Color(50, 205, 50), 1, true));
		titlePanel.setBounds(5, 41, 629, 51);
		titlePanel.setBackground(new Color(255, 255, 255));
		contentPane.add(titlePanel);
		titlePanel.setLayout(null);
		
		JLabel lblRestoClient = new JLabel("Resto Client");
		lblRestoClient.setFont(new Font("Calibri", Font.BOLD, 20));
		lblRestoClient.setHorizontalAlignment(SwingConstants.CENTER);
		lblRestoClient.setForeground(new Color(50, 205, 50));
		lblRestoClient.setBounds(249, 11, 102, 29);
		titlePanel.add(lblRestoClient);
		
		JPanel urlPanel = new JPanel();
		urlPanel.setBorder(new LineBorder(new Color(50, 205, 50), 1, true));
		urlPanel.setBackground(new Color(255, 255, 255));
		urlPanel.setBounds(5, 97, 629, 61);
		contentPane.add(urlPanel);
		urlPanel.setLayout(null);
		
		methodComboBox = new JComboBox();
		methodComboBox.addActionListener(new ActionListener() {
			/**
			 * Method setter
			 */
			public void actionPerformed(ActionEvent e) {
				
				 selectedItem = (JComboBox) e.getSource();
				requestDTO.setMethod((String) selectedItem.getSelectedItem());
			}
		});
		methodComboBox.setModel(new DefaultComboBoxModel(new String[] {Constants.METHOD_GET, Constants.METHOD_POST}));
		methodComboBox.setBackground(new Color(245, 255, 250));
		methodComboBox.setBounds(10, 11, 83, 34);
		urlPanel.add(methodComboBox);
		
		urlTextField = new JTextField();
		urlTextField.setText("http://");
		urlTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
				requestDTO.setUrl(urlTextField.getText().toString());
			}
		});
		urlTextField.setFont(new Font("Calibri", Font.BOLD, 13));
		urlTextField.setBounds(102, 11, 370, 34);
		urlPanel.add(urlTextField);
		urlTextField.setColumns(10);
		
		sendButton = new JButton("Send");
		/**
		 * Send Request Button
		 */
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			//TODO Check if the request is valid
				fillRequestDto();
				if(validateRequestDTO()){
					outputTextArea.setText("Processing Your Request ...");
					sendButton.setEnabled(false);
					fillHeaderParameters();
					fillBodyParameters();
					MainView.this.controller.sendRequest(requestDTO);
					
				}else{
					
					//TODO show error.
					System.out.println("Error .. invalid input");
				}
			}
		});
		sendButton.setBounds(482, 11,  65, 34);
		urlPanel.add(sendButton);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//to do save logic
				//TODO Check if the request is valid
				if(validateRequestDTO()){
					//outputTextArea.setText("Processing Your Request ...");
					//sendButton.setEnabled(false);
					saveButton.setEnabled(false);
					fillHeaderParameters();
					fillBodyParameters();
					
					MainView.this.controller.SaveRequest(requestDTO, CreateFolderPath());
					saveButton.setEnabled(true);
					
				}else{
					
					//TODO show error.
					//System.out.println("Error .. invalid input");
				}
			}
			

			
		});
		saveButton.setBounds(554, 11, 65, 34);
		urlPanel.add(saveButton);
		
		JPanel parametersPanel = new JPanel();
		parametersPanel.setBorder(new LineBorder(new Color(50, 205, 50), 1, true));
		parametersPanel.setBackground(new Color(255, 255, 255));
		parametersPanel.setBounds(5, 169, 629, 200);
		contentPane.add(parametersPanel);
		parametersPanel.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 6, 609, 183);
		parametersPanel.add(tabbedPane);
		
		
		
		JPanel headerPanel = new JPanel();

		
		
				
		


		tabbedPane.addTab("Header Parameters", null, headerPanel, null);
		headerPanel.setLayout(null);
		
		
		
		JButton addHeaderParamsButton = new JButton("Add Parameters");
		addHeaderParamsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				addHeaderParameter();
			}
		});
		addHeaderParamsButton.setBounds(485, 121, 109, 23);
		headerPanel.add(addHeaderParamsButton);
		
		headerModel =new  DefaultTableModel(0,0);
		headerModel.setColumnIdentifiers(columnsName);
		headerJTable = new JTable(headerModel);
		
		// body default model
		bodyModel = new DefaultTableModel(0,0);
		bodyModel.setColumnIdentifiers(columnsName);
		
		
		JScrollPane HeadersScrollPane = new JScrollPane(headerJTable);
		HeadersScrollPane.setBounds(10, 11, 584, 99);
		headerPanel.add(HeadersScrollPane);
		
		
		
		JPanel bodyPanel = new JPanel();
		tabbedPane.addTab("Body Parameters", null, bodyPanel, null);
		bodyPanel.setLayout(null);
		
		JButton addBodyParametersButton = new JButton("Add Parameters");
		addBodyParametersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				addBodyParameter();
			}
		});
		addBodyParametersButton.setBounds(485, 121, 109, 23);
		bodyPanel.add(addBodyParametersButton);
		
		JScrollPane bodyScrollPane = new JScrollPane((Component) null);
		bodyScrollPane.setBounds(10, 11, 584, 99);
		bodyPanel.add(bodyScrollPane);
		
		bodyTable = new JTable(bodyModel);
		bodyScrollPane.setViewportView(bodyTable);
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new LineBorder(new Color(50, 205, 50), 1, true));
		outputPanel.setBackground(new Color(245, 255, 250));
		outputPanel.setBounds(5, 380, 629, 280);
		contentPane.add(outputPanel);
		outputPanel.setLayout(null);
		
		JLabel lblResponse = new JLabel("Response");
		lblResponse.setForeground(new Color(50, 205, 50));
		lblResponse.setFont(new Font("Calibri", Font.BOLD, 15));
		lblResponse.setBounds(10, 11, 486, 14);
		outputPanel.add(lblResponse);
		
		stateLabel = new JLabel("200 OK");
		stateLabel.setForeground(new Color(0, 0, 0));
		stateLabel.setBackground(new Color(0, 0, 0));
		stateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stateLabel.setFont(new Font("Calibri", Font.BOLD, 14));
		stateLabel.setBounds(506, 9, 88, 18);
		outputPanel.add(stateLabel);
		
		
		outputTextArea = new JTextArea();
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setLineWrap(true);
		outputTextArea.setEditable(false);
		outputTextArea.setBounds(8, 48, 601, 215);
		
		
		
		JScrollPane outputJScrollPane = new JScrollPane(outputTextArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		outputJScrollPane.setBounds(8, 48, 601, 215);
		outputPanel.add(outputJScrollPane);
		
		//addParameter();
	
		
		// to view it 
		this.setVisible(true);
	}
	
	/**
	 * this method will be used to validate a request DTO before send it to
	 * controller.
	 * 
	 * @return
	 */
	private boolean validateRequestDTO() {

		if (requestDTO.getUrl() != null && !requestDTO.getUrl().equals("")) {
			return true;

		}

		return false;

	}


	public void viewResponse(ResponseDTO responseDTO){
		
		//Set the status
		stateLabel.setText(String.valueOf(responseDTO.getStatus()));
		outputTextArea.setText(new String(responseDTO.getBody()));
		sendButton.setEnabled(true);
		
		
	
	}
	
	public void viewRequest(RequestDTO request){
		//set selected method
		methodComboBox.setSelectedItem(request.getMethod());
		urlTextField.setText(request.getUrl());
		addHeaderParameters(request.getHeaderParams());
		addBodyParameters(request.getBodyParams());
		
		
	}
	
	private void  addHeaderParameter(){
		
		Vector<String> data = new Vector<String>();
		data.add("");
		data.add("");
		headerModel.addRow(data);
	}
	
	private void addHeaderParameters(List<Parameter> headerParams){
		resetHeaderTable();

		if(headerParams.size()>0){
			Vector<String> data = new Vector<String>();
			Iterator<Parameter> iterator = headerParams.iterator();
			while(iterator.hasNext()){
				Parameter param = iterator.next();
				data.add(param.getKey());
				data.add(param.getValue());
			}
			headerModel.addRow(data);
		}
	}
	
	
private void  addBodyParameter(){
		
		Vector<String> data = new Vector<String>();
		data.add("");
		data.add("");
		bodyModel.addRow(data);
	}

private void addBodyParameters(List<Parameter> bodyParams){
	
	resetBodyTable();
	if(bodyParams.size()>0){
		Vector<String> data = new Vector<String>();
		Iterator<Parameter> iterator = bodyParams.iterator();
		while(iterator.hasNext()){

			Parameter param = iterator.next();
			data.add(param.getKey());
			data.add(param.getValue());

		}
		bodyModel.addRow(data);}
}


	
	private void fillHeaderParameters() {
		if (headerJTable.isEditing()) {
			headerJTable.getCellEditor().stopCellEditing();
		}
		for (int i = 0; i < headerModel.getRowCount(); i++) {

			if (!headerModel.getValueAt(i, 0).equals("")) {

				Parameter parameter = new Parameter();
				parameter.setKey((String) headerModel.getValueAt(i, 0));
				parameter.setValue((String) headerModel.getValueAt(i, 1));
				headerParameters.add(parameter);

			}
			System.out.println("Row data column Key > "
					+ headerModel.getValueAt(i, 0));
			System.out.println("Row data column value > "
					+ headerModel.getValueAt(i, 1));
		}

		requestDTO.setHeaderParams(headerParameters);
	}

	private void fillBodyParameters() {

		if (bodyTable.isEditing()) {
			bodyTable.getCellEditor().stopCellEditing();
		}
		for (int i = 0; i < bodyModel.getRowCount(); i++) {

			if (!bodyModel.getValueAt(i, 0).equals("")) {

				Parameter parameter = new Parameter();
				parameter.setKey((String) bodyModel.getValueAt(i, 0));
				parameter.setValue((String) bodyModel.getValueAt(i, 1));
				bodyParameters.add(parameter);

			}

			System.out.println("Row Body data column Key > "
					+ bodyModel.getValueAt(i, 0));
			System.out.println("Row Body data column value > "
					+ bodyModel.getValueAt(i, 1));
		}

		requestDTO.setBodyParams(bodyParameters);
	}
	
	private String CreateFolderPath(){
		
		String filePath=null;
		// show file chooser  for save
		JFileChooser saveFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		saveFileChooser.setDialogTitle("Save a Service");
		saveFileChooser.setSelectedFile(new File("Service Name.json"));
		int returnValue = saveFileChooser.showSaveDialog(this);
		if(returnValue==JFileChooser.APPROVE_OPTION){
			File choosenFile = saveFileChooser.getSelectedFile();
			filePath=choosenFile.getPath();
			System.out.println(choosenFile.getAbsolutePath());
		}
		
		return filePath;
		
	}
	
	private String openService(){
		
		String filePath=null;
		JFileChooser openFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		openFileChooser.setDialogTitle("Open a Service");
		openFileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Resto Json Services ", "json");
		openFileChooser.addChoosableFileFilter(extensionFilter);
		int returnValue= openFileChooser.showOpenDialog(this);
		if(returnValue==JFileChooser.APPROVE_OPTION){
			File chosenFile = openFileChooser.getSelectedFile();
			filePath=chosenFile.getPath();
			System.out.println(filePath);
		}
		return filePath;
		
	}
	
	private void fillRequestDto(){
		//set Method
		this.requestDTO.setMethod((String) methodComboBox.getSelectedItem());
		this.requestDTO.setUrl(urlTextField.getText());
		// Setting header params.
		fillHeaderParameters();
		fillBodyParameters();
		
	}
	
	
	
	private void resetBodyTable(){
		if (bodyTable.isEditing()) {
			bodyTable.getCellEditor().stopCellEditing();
		}
		bodyModel.setRowCount(0);
	}
	
	private void resetHeaderTable(){
		
		if(headerJTable.isEditing()){
			headerJTable.getCellEditor().stopCellEditing();
		}
		
		headerModel.setRowCount(0);
	}
}
