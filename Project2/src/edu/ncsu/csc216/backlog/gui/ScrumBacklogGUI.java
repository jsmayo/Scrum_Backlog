package edu.ncsu.csc216.backlog.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.backlog.command.Command;
import edu.ncsu.csc216.backlog.command.Command.CommandValue;
import edu.ncsu.csc216.backlog.scrum_backlog.ScrumBacklogModel;
import edu.ncsu.csc216.backlog.task.TaskItem;

/**
 * Container for the ScrumBacklog that has the menu options for new task 
 * list files, loading existing files, saving files and quitting.
 * Depending on user actions, other {@link JPanel}s are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
 */
public class ScrumBacklogGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Scrum Backlog";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the New Task XML menu item. */
	private static final String NEW_XML_TITLE = "New";
	/** Text for the Load Task XML menu item. */
	private static final String LOAD_XML_TITLE = "Load";
	/** Text for the Save menu item. */
	private static final String SAVE_XML_TITLE = "Save";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for creating a new file containing {@link TaskItem}s. */
	private JMenuItem itemNewTaskXML;
	/** Menu item for loading a file containing {@link TaskItem}s. */
	private JMenuItem itemLoadTaskXML;
	/** Menu item for saving the task item list. */
	private JMenuItem itemSaveTaskXML;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the application. */
	private JPanel panel;
	/** Constant to identify TaskItemListPanel for {@link CardLayout}. */
	private static final String TASK_ITEM_LIST_PANEL = "TaskItemListPanel";
	/** Constant to identify BacklogPanel for {@link CardLayout}. */
	private static final String BACKLOG_PANEL = "BacklogPanel";
	/** Constant to identify OwnedPanel for {@link CardLayout}. */
	private static final String OWNED_PANEL = "OwnedPanel";
	/** Constant to identify ProcessingPanel for {@link CardLayout}. */
	private static final String PROCESSING_PANEL = "ProcessingPanel";
	/** Constant to identify VerifyingPanel for {@link CardLayout}. */
	private static final String VERIFYING_PANEL = "VerifyingPanel";
	/** Constant to identify DonePanel for {@link CardLayout}. */
	private static final String DONE_PANEL = "DonePanel";
	/** Constant to identify RejectedPanel for {@link CardLayout}. */
	private static final String REJECTED_PANEL = "RejectedPanel";
	/** Constant to identify CreateTaskItemPanel for {@link CardLayout}. */
	private static final String CREATE_TASK_ITEM_PANEL = "CreateTaskItemPanel";
	/** Task Item List panel - we only need one instance, so it's final. */
	private final TaskItemListPanel pnlTaskItemList = new TaskItemListPanel();
	/** Backlog panel - we only need one instance, so it's final. */
	private final BacklogPanel pnlBacklog = new BacklogPanel();
	/** Owned panel - we only need one instance, so it's final. */
	private final OwnedPanel pnlOwned = new OwnedPanel();
	/** Processing panel - we only need one instance, so it's final. */
	private final ProcessingPanel pnlProcessing = new ProcessingPanel();
	/** Verifying panel - we only need one instance, so it's final. */
	private final VerifyingPanel pnlVerifying = new VerifyingPanel();
	/** Done panel - we only need one instance, so it's final. */
	private final DonePanel pnlDone = new DonePanel();
	/** Rejected panel - we only need one instance, so it's final. */
	private final RejectedPanel pnlRejected = new RejectedPanel();
	/** Add Task Item panel - we only need one instance, so it's final. */
	private final CreateTaskItemPanel pnlCreateTaskItem = new CreateTaskItemPanel();
	/** Reference to {@link CardLayout} for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	
	/**
	 * Constructs a {@link ScrumBacklogGUI} object that will contain a {@link JMenuBar} and a
	 * {@link JPanel} that will hold different possible views of the data in
	 * the {@link ScrumBacklogModel}.
	 */
	public ScrumBacklogGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlTaskItemList, TASK_ITEM_LIST_PANEL);
		panel.add(pnlBacklog, BACKLOG_PANEL);
		panel.add(pnlOwned, OWNED_PANEL);
		panel.add(pnlProcessing, PROCESSING_PANEL);
		panel.add(pnlVerifying, VERIFYING_PANEL);
		panel.add(pnlDone, DONE_PANEL);
		panel.add(pnlRejected, REJECTED_PANEL);
		panel.add(pnlCreateTaskItem, CREATE_TASK_ITEM_PANEL);
		cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options for loading a file
	 * containing task items or for quitting the application.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemNewTaskXML = new JMenuItem(NEW_XML_TITLE);
		itemLoadTaskXML = new JMenuItem(LOAD_XML_TITLE);
		itemSaveTaskXML = new JMenuItem(SAVE_XML_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemNewTaskXML.addActionListener(this);
		itemLoadTaskXML.addActionListener(this);
		itemSaveTaskXML.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSaveTaskXML.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemNewTaskXML);
		menu.add(itemLoadTaskXML);
		menu.add(itemSaveTaskXML);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given {@link ActionEvent}.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {
		//Use ScrumBacklogModel singleton to create/get the sole instance.
		ScrumBacklogModel model = ScrumBacklogModel.getInstance();
		if (e.getSource() == itemNewTaskXML) {
			//Create a new task list
			model.createNewTaskItemList();
			itemSaveTaskXML.setEnabled(true);
			pnlTaskItemList.updateTable(null, null);
			cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
			validate();
			repaint();			
		} else if (e.getSource() == itemLoadTaskXML) {
			//Load an existing task list
			try {
				model.loadTasksFromFile(getFileName(true));
				itemSaveTaskXML.setEnabled(true);
				pnlTaskItemList.updateTable(null, null);
				cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to load task file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSaveTaskXML) {
			//Save current task list
			try {
				model.saveTasksToFile(getFileName(false));
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save task file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.saveTasksToFile(getFileName(false));
				System.exit(0);  //Ignore FindBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save task file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a {@link JFileChooser}
	 * object.
	 * @return the file name selected through {@link JFileChooser}
	 */
	private String getFileName(boolean chooserType) {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChoose to current working directory
		fc.setApproveButtonText("Select");
		int returnVal = Integer.MIN_VALUE;
		if (chooserType) {
			fc.setDialogTitle("Load Task List");
			returnVal = fc.showOpenDialog(this);
		} else {
			fc.setDialogTitle("Save Task List");
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File file = fc.getSelectedFile();
		return file.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the ScrumBacklog application.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new ScrumBacklogGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * shows the list of task items.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class TaskItemListPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Button for creating a new TaskItem */
		private JButton btnAddTaskItem;
		/** Button for deleting the selected TaskItem in the list */
		private JButton btnDeleteTaskItem;
		/** Button for editing the selected TaskItem in the list */
		private JButton btnEditTaskItem;
		/** Text field for a user to enter an owner name to filter TaskItem list */
		private JTextField txtFilterByOwner;
		/** Button for starting filter of list by owner */
		private JButton btnFilterByOwner;
		/** Text field for a user to enter an creator name to filter TaskItem list */
		private JTextField txtFilterByCreator;
		/** Button for starting filter of list by creator */
		private JButton btnFilterByCreator;
		/** Button that will show all TaskItems in the backlog */
		private JButton btnShowAllTaskItems;
		/** JTable for displaying the list of TaskItems */
		private JTable table;
		/** TableModel for TaskItems */
		private TaskItemTableModel taskItemTableModel;
		
		/**
		 * Creates the TaskItem list.
		 */
		public TaskItemListPanel() {
			super(new GridBagLayout());
			
			//Set up the JPanel that will hold action buttons
			JPanel pnlActions = new JPanel();
			btnAddTaskItem = new JButton("Add New Task");
			btnAddTaskItem.addActionListener(this);
			btnDeleteTaskItem = new JButton("Delete Selected Task");
			btnDeleteTaskItem.addActionListener(this);
			btnEditTaskItem = new JButton("Edit Selected Task");
			btnEditTaskItem.addActionListener(this);
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Task Actions");
			pnlActions.setBorder(border);
			pnlActions.setToolTipText("Task Actions");
			
			pnlActions.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlActions.add(btnAddTaskItem, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlActions.add(btnDeleteTaskItem, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlActions.add(btnEditTaskItem, c);
			
			//Set up the JPanel that handles search
			JPanel pnlSearch = new JPanel();
			txtFilterByOwner = new JTextField(10);
			btnFilterByOwner = new JButton("Filter List by Owner");
			btnFilterByOwner.addActionListener(this);
			txtFilterByCreator = new JTextField(10);
			btnFilterByCreator = new JButton("Filter List by Creator");
			btnFilterByCreator.addActionListener(this);
			btnShowAllTaskItems = new JButton("Show All Tasks");
			btnShowAllTaskItems.addActionListener(this);
			
			border = BorderFactory.createTitledBorder(lowerEtched, "Task Search");
			pnlSearch.setBorder(border);
			pnlSearch.setToolTipText("Task Search");
			
			pnlSearch.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlSearch.add(txtFilterByOwner, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlSearch.add(btnFilterByOwner, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlSearch.add(txtFilterByCreator, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlSearch.add(btnFilterByCreator, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlSearch.add(btnShowAllTaskItems, c);
						
			//Set up table
			taskItemTableModel = new TaskItemTableModel();
			table = new JTable(taskItemTableModel);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setPreferredScrollableViewportSize(new Dimension(500, 500));
			table.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(table);
			
			border = BorderFactory.createTitledBorder(lowerEtched, "Task List");
			listScrollPane.setBorder(border);
			listScrollPane.setToolTipText("Task List");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = .1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlActions, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = .1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlSearch, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.ipady = 400;
			c.weightx = 1;
			c.weighty = .8;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(listScrollPane, c);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAddTaskItem) {
				//If the add button is pressed switch to the createTaskItemPanel
				cardLayout.show(panel,  CREATE_TASK_ITEM_PANEL);
			} else if (e.getSource() == btnDeleteTaskItem) {
				//If the delete button is pressed, delete the TaskItem
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "No task selected");
				} else {
					try {
						int taskItemId = Integer.parseInt(taskItemTableModel.getValueAt(row, 0).toString());
						ScrumBacklogModel.getInstance().deleteTaskItemById(taskItemId);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid task id");
					}
				}
				updateTable(null, null);
			} else if (e.getSource() == btnEditTaskItem) {
				//If the edit button is pressed, switch panel based on state
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "No task selected");
				} else {
					try {
						int taskItemId = Integer.parseInt(taskItemTableModel.getValueAt(row, 0).toString());
						String stateName = ScrumBacklogModel.getInstance().getTaskItemById(taskItemId).getStateName();
						 
						if (stateName.equals(TaskItem.BACKLOG_NAME)) {
							cardLayout.show(panel, BACKLOG_PANEL);
							pnlBacklog.setTaskItemInfo(taskItemId);
						} 
						if (stateName.equals(TaskItem.OWNED_NAME)) {
							cardLayout.show(panel, OWNED_PANEL);
							pnlOwned.setTaskItemInfo(taskItemId);
						} 
						if (stateName.equals(TaskItem.PROCESSING_NAME)) {
							cardLayout.show(panel, PROCESSING_PANEL);
							pnlProcessing.setTaskItemInfo(taskItemId);
						} 
						if (stateName.equals(TaskItem.VERIFYING_NAME)) {
							cardLayout.show(panel, VERIFYING_PANEL);
							pnlVerifying.setTaskItemInfo(taskItemId);
						}
						if (stateName.equals(TaskItem.DONE_NAME)) {
							cardLayout.show(panel, DONE_PANEL);
							pnlDone.setTaskItemInfo(taskItemId);
						} 
						if (stateName.equals(TaskItem.REJECTED_NAME)) {
							cardLayout.show(panel, REJECTED_PANEL);
							pnlRejected.setTaskItemInfo(taskItemId);
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid task id");
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid task id");
					}
				}
			} else if (e.getSource() == btnFilterByOwner) {
				String owner = txtFilterByOwner.getText();
				txtFilterByOwner.setText("");
				updateTable(owner, null);
			} else if (e.getSource() == btnFilterByCreator) {
				String creator = txtFilterByCreator.getText();
				txtFilterByCreator.setText("");
				updateTable(null, creator);
			} else if (e.getSource() == btnShowAllTaskItems) {
				updateTable(null, null);
			}
			ScrumBacklogGUI.this.repaint();
			ScrumBacklogGUI.this.validate();
		}
		
		/**
		 * Updates the TaskItem table with items that may be filtered by owner or 
		 * creator.
		 * @param owner user id to filter by owner
		 * @param creator user id to filter by creator
		 */
		public void updateTable(String owner, String creator) {
			if (owner == null && creator == null) {
				taskItemTableModel.updateData();
			} else if (owner != null) {
				taskItemTableModel.updateDataWithOwner(owner);
			} else if (creator != null) {
				taskItemTableModel.updateDataWithCreator(creator);
			}
		}
		
		/**
		 * {@link TaskItemTableModel} is the object underlying the {@link JTable} object that displays
		 * the list of {@link TaskItem}s to the user.
		 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
		 */
		private class TaskItemTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Task ID", "Task State", "Task Title"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the {@link TaskItemTableModel} by requesting the latest information
			 * from the {@link ScrumBacklogModel}.
			 */
			public TaskItemTableModel() {
				updateData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row location to modify the data.
			 * @param column location to modify the data.
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with {@link TaskItem} information from the {@link ScrumBacklogModel}.
			 */
			private void updateData() {
				ScrumBacklogModel m = ScrumBacklogModel.getInstance();
				data = m.getTaskItemListAsArray();
			}
			
			/**
			 * Updates the given model with {@link TaskItem} information for the 
			 * given owner from the {@link ScrumBacklogModel}.
			 * @param owner developer id to search for.
			 */
			private void updateDataWithOwner(String owner) {
				try {
					ScrumBacklogModel m = ScrumBacklogModel.getInstance();
					data = m.getTaskItemListByOwnerAsArray(owner);
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid owner");
				}
			}
			
			/**
			 * Updates the given model with {@link TaskItem} information for the 
			 * given creator from the {@link ScrumBacklogModel}.
			 * @param creator creator id to search for.
			 */
			private void updateDataWithCreator(String creator) {
				try {
					ScrumBacklogModel m = ScrumBacklogModel.getInstance();
					data = m.getTaskItemListByCreatorAsArray(creator);
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid creator");
				}
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a backloged TaskItem.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class BacklogPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TaskItemInfoPanel} that presents the {@link TaskItem}'s information to the user */
		private TaskItemInfoPanel pnlTaskItemInfo;
		/** Label for owner id field */
		private JLabel lblOwner;
		/** Text field for owner id */
		private JTextField txtOwner;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Claim action */
		private JButton btnClaim;
		/** Reject action */
		private JButton btnReject;
		/** Cancel action */
		private JButton btnCancel;
		/** Current {@link TaskItem}'s id */
		private int taskItemId;
		
		/**
		 * Constructs the JPanel for editing a {@link TaskItem} in the NewState.
		 */
		public BacklogPanel() {
			pnlTaskItemInfo = new TaskItemInfoPanel();
			lblOwner = new JLabel("Owner");
			txtOwner = new JTextField(30);
			lblNote = new JLabel("Note Text");
			txtNote = new JTextArea(30, 5);
			btnClaim = new JButton("Claim Task");
			btnReject = new JButton("Reject Task");
			btnCancel = new JButton("Cancel");
			
			btnClaim.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskItemInfo, c);
			
			JPanel pnlBacklogInfo = new JPanel();
			pnlBacklogInfo.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Claim Task Item");
			pnlBacklogInfo.setBorder(border);
			pnlBacklogInfo.setToolTipText("Claim Task Item");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlBacklogInfo.add(lblOwner, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlBacklogInfo.add(txtOwner, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlBacklogInfo.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlBacklogInfo.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlBacklogInfo, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnClaim, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnReject, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TaskItemInfoPanel} with the given task item data.
		 * @param taskItemId id of the task item
		 */
		public void setTaskItemInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskItemInfo.setTaskItemInfo(this.taskItemId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnClaim) {
				//Take care of note.
				String note = txtNote.getText();
				if (note.equals("")) {
					note = null;
				}
				String ownerId = txtOwner.getText();
				if (ownerId == null || ownerId.equals("")) {
					//If developer id is invalid, show an error message
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid owner id");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to task list
					try {
						Command c = new Command(Command.CommandValue.CLAIM, ownerId, note);
						ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} else if (e.getSource() == btnReject) {
				//Take care of note.
				String note = txtNote.getText();
				if (note.equals("")) {
					note = null;
				}
				String ownerId = txtOwner.getText();
				if (ownerId == null || ownerId.equals("")) {
					//If developer id is invalid, show an error message
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid owner id");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to task list
					try {
						Command c = new Command(Command.CommandValue.REJECT, ownerId, note);
						ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			}
			if (reset) {
				//All buttons lead to back task item list 
				cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
				pnlTaskItemList.updateTable(null, null);
				ScrumBacklogGUI.this.repaint();
				ScrumBacklogGUI.this.validate();
				//Reset fields
				txtOwner.setText("");
				txtNote.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with an owned task.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class OwnedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TaskItemInfoPanel} that presents the {@link TaskItem}'s information to the user */
		private TaskItemInfoPanel pnlTaskItemInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Process action */
		private JButton btnProcess;
		/** Reject action */
		private JButton btnReject;
		/** Backlog action */
		private JButton btnBacklog;
		/** Cancel action */
		private JButton btnCancel;
		/** Current task item's id */
		private int taskItemId;

		/**
		 * Constructs a JPanel for editing a {@link TaskItem} in the OwnedState.
		 */
		public OwnedPanel() {
			pnlTaskItemInfo = new TaskItemInfoPanel();
			lblNote = new JLabel("Note Text");
			txtNote = new JTextArea(30, 5);
			
			btnProcess = new JButton("Process Task");
			btnReject = new JButton("Reject Task");
			btnBacklog = new JButton("Backlog Task");
			btnCancel = new JButton("Cancel");
			
			btnProcess.addActionListener(this);
			btnReject.addActionListener(this);
			btnBacklog.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskItemInfo, c);
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("Edit Task");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridLayout(1, 4));
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnProcess, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnReject, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnBacklog, c);
			c = new GridBagConstraints();
			c.gridx = 3;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TaskItemInfoPanel} with the given task item data.
		 * @param taskItemId id of the task item
		 */
		public void setTaskItemInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskItemInfo.setTaskItemInfo(this.taskItemId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() != btnCancel && note.equals("")) {
				JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid note text.");
				reset = false;
			} else if (e.getSource() == btnProcess) {
				//Try a command.  If problem, go back to task item list.
				try {
					Command c = new Command(Command.CommandValue.PROCESS, ScrumBacklogModel.getInstance().getTaskItemById(taskItemId).getOwner(), note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnReject) {
				//Try a command.  If problem, go back to task item list.
				try {
					Command c = new Command(Command.CommandValue.REJECT, ScrumBacklogModel.getInstance().getTaskItemById(taskItemId).getOwner(), note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnBacklog) {				
				//Try a command.  If problem, go back to task item list.
				try {
					Command c = new Command(Command.CommandValue.BACKLOG, ScrumBacklogModel.getInstance().getTaskItemById(taskItemId).getOwner(), note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back task item list 
				cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
				pnlTaskItemList.updateTable(null, null);
				ScrumBacklogGUI.this.repaint();
				ScrumBacklogGUI.this.validate();
				//Reset fields
				txtNote.setText("");
			}
			//Otherwise, stay on panel
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a processing task.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class ProcessingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TaskItemInfoPanel} that presents the {@link TaskItem}'s information to the user */
		private TaskItemInfoPanel pnlTaskInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Process action */
		private JButton btnProcess;
		/** Verify action */
		private JButton btnVerify;
		/** Complete action */
		private JButton btnComplete;
		/** Backlog action */
		private JButton btnBacklog;
		/** Cancel action */
		private JButton btnCancel;
		/** Current task item id */
		private int taskItemId;

		/**
		 * Constructs a JFrame for editing a {@link TaskItem} in the ProcessingState.
		 */
		public ProcessingPanel() {
			pnlTaskInfo = new TaskItemInfoPanel();
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnProcess = new JButton("Add Note");
			btnVerify = new JButton("Verify Task");
			btnComplete = new JButton("Complete Task");
			btnBacklog = new JButton("Backlog Task");
			btnCancel = new JButton("Cancel");
			
			btnProcess.addActionListener(this);
			btnVerify.addActionListener(this);
			btnComplete.addActionListener(this);
			btnBacklog.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskInfo, c);
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("Edit Task");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnProcess, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnVerify, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnComplete, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnBacklog, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TaskItemInfoPanel} with the given task data.
		 * @param taskItemId id of the task
		 */
		public void setTaskItemInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskInfo.setTaskItemInfo(this.taskItemId);
			
			//Rest buttons to enabled
			btnVerify.setEnabled(true);
			btnComplete.setEnabled(true);
			
			//Disable appropriate button depending on task type			
			TaskItem t = ScrumBacklogModel.getInstance().getTaskItemById(taskItemId);
			if (t.getType() == TaskItem.Type.KNOWLEDGE_ACQUISITION) {
				btnVerify.setEnabled(false);
			} else {
				btnComplete.setEnabled(false);
			}
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() != btnCancel && note.equals("")) {
				JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid note text.");
				reset = false;
			} else if (e.getSource() == btnProcess) {
				//Try command.  If problem, go to task list.
				try {
					Command c = new Command(Command.CommandValue.PROCESS, ScrumBacklogModel.getInstance().getTaskItemById(taskItemId).getOwner(), note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnVerify) {
				//Try command.  If problem, go to task list.
				try {
					Command c = new Command(Command.CommandValue.VERIFY, ScrumBacklogModel.getInstance().getTaskItemById(taskItemId).getOwner(), note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnComplete) {
				//Try a command.  If problem, go back to task list.
				try {
					Command c = new Command(Command.CommandValue.COMPLETE, ScrumBacklogModel.getInstance().getTaskItemById(taskItemId).getOwner(), note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnBacklog) {
				//Otherwise, try a Command.  If command fails, go back to task list
				try {
					Command c = new Command(Command.CommandValue.BACKLOG, ScrumBacklogModel.getInstance().getTaskItemById(taskItemId).getOwner(), note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back task item list 
				cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
				pnlTaskItemList.updateTable(null, null);
				ScrumBacklogGUI.this.repaint();
				ScrumBacklogGUI.this.validate();
				//Reset fields
				txtNote.setText("");
			}
			//Otherwise, stay on panel
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a verifying task item.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class VerifyingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TaskItemInfoPanel} that presents the {@link TaskItem}'s information to the user */
		private TaskItemInfoPanel pnlTaskItemInfo;
		/** Verifier user id label for the state update */
		private JLabel lblVerifier;
		/** Verifier user id for the state update */
		private JTextField txtVerifier;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Process action */
		private JButton btnProcess;
		/** Complete action */
		private JButton btnComplete;
		/** Cancel action */
		private JButton btnCancel;
		/** Current task item id */
		private int taskItemId;

		/**
		 * Constructs a JPanel for editing a {@link TaskItem} in the VerifyingState.
		 */
		public VerifyingPanel() {
			pnlTaskItemInfo = new TaskItemInfoPanel();
			lblVerifier = new JLabel("Verifier ID");
			txtVerifier = new JTextField(30);
			lblNote = new JLabel("Note Text");
			txtNote = new JTextArea(30, 5);
			btnProcess = new JButton("Return Task to Owner");
			btnComplete = new JButton("Task Verified");
			btnCancel = new JButton("Cancel");
			
			btnProcess.addActionListener(this);
			btnComplete.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskItemInfo, c);
			
			JPanel pnlNewInfo = new JPanel();
			pnlNewInfo.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlNewInfo.setBorder(border);
			pnlNewInfo.setToolTipText("Edit Task");

			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(lblVerifier, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(txtVerifier, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNewInfo.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlNewInfo.add(txtNote, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNewInfo, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnProcess, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnComplete, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TaskItemInfoPanel} with the given task item data.
		 * @param taskItemId id of the task item
		 */
		public void setTaskItemInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskItemInfo.setTaskItemInfo(this.taskItemId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			String verifier = txtVerifier.getText();
			if (e.getSource() != btnCancel && (note.equals("") || verifier.equals(""))) {
				if (note.equals("")) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid note text.");
				}
				if (verifier.equals("")) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid verifier.");
				}
				reset = false;
			} else if (e.getSource() == btnProcess) {
				//Try command.  If problem, return to task list.
				try {
					Command c = new Command(Command.CommandValue.PROCESS, verifier, note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnComplete) {
				//Try command.  If problem, return to task list.
				try {
					Command c = new Command(Command.CommandValue.COMPLETE, verifier, note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			} 
			if (reset) {
				//All buttons lead to back task item list 
				cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
				pnlTaskItemList.updateTable(null, null);
				ScrumBacklogGUI.this.repaint();
				ScrumBacklogGUI.this.validate();
				//Reset fields
				txtVerifier.setText("");
				txtNote.setText("");
			}
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a done task item.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class DonePanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TaskItemInfoPanel} that presents the {@link TaskItem}'s information to the user */
		private TaskItemInfoPanel pnlTaskInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Process action */
		private JButton btnProcess;
		/** Backlog action */
		private JButton btnBacklog;
		/** Cancel action */
		private JButton btnCancel;
		/** Current task item id */
		private int taskItemId;

		/**
		 * Constructs a JPanel for editing a {@link TaskItem} in the DoneState.
		 */
		public DonePanel() {
			pnlTaskInfo = new TaskItemInfoPanel();
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnProcess = new JButton("Return Task to Owner");
			btnBacklog = new JButton("Backlog Task");
			btnCancel = new JButton("Cancel");
			
			btnProcess.addActionListener(this);
			btnBacklog.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskInfo, c);
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("Edit Task");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);

			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnProcess, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnBacklog, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TaskItemInfoPanel} with the given task item data.
		 * @param taskItemId id of the task item
		 */
		public void setTaskItemInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskInfo.setTaskItemInfo(this.taskItemId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() != btnCancel && note.equals("")) {
				JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid note text.");
				reset = false;
			} else if (e.getSource() == btnProcess) {
				//Try command.  If problem, go back to task item list
				try {
					Command c = new Command(CommandValue.PROCESS, ScrumBacklogModel.getInstance().getTaskItemById(taskItemId).getOwner(), note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnBacklog) {
				//Otherwise, try a Command.  If command fails, go back to task item list
				try {
					Command c = new Command(Command.CommandValue.BACKLOG, ScrumBacklogModel.getInstance().getTaskItemById(taskItemId).getOwner(), note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back task item list 
				cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
				pnlTaskItemList.updateTable(null, null);
				ScrumBacklogGUI.this.repaint();
				ScrumBacklogGUI.this.validate();
				//Reset fields
				txtNote.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct developer input.
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * interacts with a rejected task item.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class RejectedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** {@link TaskItemInfoPanel} that presents the {@link TaskItem}'s information to the user */
		private TaskItemInfoPanel pnlTaskItemInfo;
		/** Note author label for the state update */
		private JLabel lblNoteAuthor;
		/** Note author for the state update */
		private JTextField txtNoteAuthor;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Backlog action */
		private JButton btnBacklog;
		/** Cancel action */
		private JButton btnCancel;
		/** Current task item id */
		private int taskItemId;

		/**
		 * Constructs a JPanel for editing a {@link TaskItem} in the RejectedState.
		 */
		public RejectedPanel() {
			pnlTaskItemInfo = new TaskItemInfoPanel();
			lblNoteAuthor = new JLabel("Note Author");
			txtNoteAuthor = new JTextField(30);
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnBacklog = new JButton("Backlog Task");
			btnCancel = new JButton("Cancel");
			
			btnBacklog.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTaskItemInfo, c);
			
			JPanel pnlNote = new JPanel();
			pnlNote.setLayout(new GridBagLayout());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Edit Task");
			pnlNote.setBorder(border);
			pnlNote.setToolTipText("Edit Task");
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(txtNoteAuthor, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlNote.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.BOTH;
			pnlNote.add(txtNote, c);

			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			add(pnlNote, c);
			
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnBacklog, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}
		
		/**
		 * Set the {@link TaskItemInfoPanel} with the given task item data.
		 * @param taskItemId id of the task item
		 */
		public void setTaskItemInfo(int taskItemId) {
			this.taskItemId = taskItemId;
			pnlTaskItemInfo.setTaskItemInfo(this.taskItemId);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			String noteAuthor = txtNoteAuthor.getText();
			if (e.getSource() != btnCancel && (note.equals("") || noteAuthor.equals(""))) {
				if (note.equals("")) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid note text.");
				}
				if (noteAuthor.equals("")) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid note author id.");
				}
				reset = false;
			} else if (e.getSource() == btnBacklog) {
				//Try command.  If problem, go back to task list
				try {
					Command c = new Command(CommandValue.BACKLOG, noteAuthor, note);
					ScrumBacklogModel.getInstance().executeCommand(taskItemId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead to back task item list
				cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
				pnlTaskItemList.updateTable(null, null);
				ScrumBacklogGUI.this.repaint();
				ScrumBacklogGUI.this.validate();
				//Reset fields
				txtNote.setText("");
				txtNoteAuthor.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct developer input.
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * shows information about the task item.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class TaskItemInfoPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for id */
		private JLabel lblId;
		/** Field for id */
		private JTextField txtId;
		/** Label for state */
		private JLabel lblState;
		/** Field for state */
		private JTextField txtState;
		/** Label for type */
		private JLabel lblType;
		/** Field for type */
		private JTextField txtType;
		/** Label for title */
		private JLabel lblTitle;
		/** Field for title */
		private JTextField txtTitle;
		/** Label for creator */
		private JLabel lblCreator;
		/** Field for creator */
		private JTextField txtCreator;
		/** Label for owner */
		private JLabel lblOwner;
		/** Field for owner */
		private JTextField txtOwner;
		/** Label for notes */
		private JLabel lblNotes;
		/** JTable for displaying notes */
		private JTable tableNotes;
		/** Scroll pane for table */
		private JScrollPane scrollNotes;
		/** TableModel for Notes */
		private NotesTableModel notesTableModel;
		
		/** 
		 * Construct the panel for the task item information.
		 */
		public TaskItemInfoPanel() {
			super(new GridBagLayout());
			
			lblId = new JLabel("Task Id");
			lblState = new JLabel("Task State");
			lblType = new JLabel("Task Type");
			lblTitle = new JLabel("Task Title");
			lblCreator = new JLabel("Creator");
			lblOwner = new JLabel("Owner");
			lblNotes = new JLabel("Notes");
			
			txtId = new JTextField(15);
			txtState = new JTextField(15);
			txtType = new JTextField(15);
			txtTitle = new JTextField(30);
			txtCreator = new JTextField(15);
			txtOwner = new JTextField(15);
			
			txtId.setEditable(false);
			txtState.setEditable(false);
			txtType.setEditable(false);
			txtTitle.setEditable(false);
			txtCreator.setEditable(false);
			txtOwner.setEditable(false);
			
			notesTableModel = new NotesTableModel();
			tableNotes = new JTable(notesTableModel);
			tableNotes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableNotes.setPreferredScrollableViewportSize(new Dimension(500, 70));
			tableNotes.setFillsViewportHeight(true);
			
			scrollNotes = new JScrollPane(tableNotes, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Task Information");
			this.setBorder(border);
			this.setToolTipText("Task Information");
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblTitle, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtTitle, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblId, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtId, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblState, c);
			c = new GridBagConstraints();
			c.gridx = 3;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtState, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblCreator, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtCreator, c);
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblOwner, c);
			c = new GridBagConstraints();
			c.gridx = 3;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtOwner, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblType, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(txtType, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 4;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblNotes, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 5;
			c.ipady = 50;
			c.gridheight = 30;
			c.gridwidth = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(scrollNotes, c);
		}
		
		/**
		 * Adds information about the task item to the display.  
		 * @param taskItemId the id for the task item to display information about.
		 */
		public void setTaskItemInfo(int taskItemId) {
			//Get the task from the model
			TaskItem t = ScrumBacklogModel.getInstance().getTaskItemById(taskItemId);
			if (t == null) {
				//If the task item doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid task item id");
				cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
				ScrumBacklogGUI.this.repaint();
				ScrumBacklogGUI.this.validate();
			} else {
				//Otherwise, set all of the fields with the information
				txtId.setText("" + t.getTaskItemId());
				txtState.setText(t.getStateName());
				txtTitle.setText(t.getTitle());
				txtCreator.setText(t.getCreator());
				txtOwner.setText(t.getOwner());
				String typeString = t.getTypeFullString();
				if (typeString == null) {
					txtType.setText("");
				} else {
					txtType.setText("" + typeString);
				}
				notesTableModel.updateData(t.getNotesArray());
			}
		}
		
		/**
		 * {@link NotesTableModel} is the object underlying the {@link JTable} object that displays
		 * the list of Notes to the user.
		 * @author Sarah Heckman
		 */
		private class NotesTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Note Author", "Note Text"};
			/** Data stored in the table */
			private Object [][] data;

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row location to modify the data.
			 * @param column location to modify the data.
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with {@link NoteItem} information from the {@link TaskItem}.
			 */
			public void updateData(String [][] notes) {
				data = notes;
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the {@link JPanel} that 
	 * allows for creation of a new task item.
	 * 
	 * @author Dr. Sarah Heckman (heckman@csc.ncsu.edu)
	 */
	private class CreateTaskItemPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for identifying title text field */
		private JLabel lblTitle;
		/** Text field for entering title information */
		private JTextField txtTitle;
		/** Label for selecting a type */
		private JLabel lblType;
		/** ComboBox for type */
		private JComboBox<String> comboType;
		/** Label for identifying creator text field */
		private JLabel lblCreator;
		/** Text field for entering creator id */
		private JTextField txtCreator;
		/** Label for note text field */
		private JLabel lblNote;
		/** Text field for note information*/
		private JTextArea txtNote;
		/** Button to add a task item */
		private JButton btnAdd;
		/** Button for canceling add action */
		private JButton btnCancel;
		/** Possible types to list in the combo box */
		private String [] types = {"Feature", "Bug", "Technical Work", "Knowledge Acquisition"};
		
		/**
		 * Creates the {@link JPanel} for adding new task item to the 
		 * backlog.
		 */
		public CreateTaskItemPanel() {
			super(new GridBagLayout());  
			
			//Construct widgets
			lblTitle = new JLabel("Task Title");
			txtTitle = new JTextField(30);
			lblType = new JLabel("Task Type");
			comboType = new JComboBox<String>(types);
			comboType.setSelectedIndex(0);
			lblCreator = new JLabel("Task Creator");
			txtCreator = new JTextField(30);
			lblNote = new JLabel("Task Notes");
			txtNote = new JTextArea(10, 30);
			btnAdd = new JButton("Add Task to Backlog");
			btnCancel = new JButton("Cancel");
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			//Builds info panel, which is a 2 row, 1 col grid
			JPanel pnlInfo = new JPanel();
			pnlInfo.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(lblTitle, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(txtTitle, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(lblType, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(comboType, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(lblCreator, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 2;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(txtCreator, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 3;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlInfo.add(lblNote, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 4;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.BOTH;
			pnlInfo.add(txtNote, c);
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "New Task Information");
			pnlInfo.setBorder(border);
			pnlInfo.setToolTipText("New Task Information");
			
			//Build button panel, which is a 1 row, 2 col grid
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnAdd, c);
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlButtons.add(btnCancel, c);
			
			border = BorderFactory.createTitledBorder(lowerEtched, "New Task Actions");
			pnlButtons.setBorder(border);
			pnlButtons.setToolTipText("New Task Actions");
			
			//Adds all panels to main panel
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlInfo, c);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.gridheight = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.PAGE_END;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlButtons, c);
		}

		/**
		 * Performs an action based on the given {@link ActionEvent}.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean done = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				//Add task to the list
				String title = txtTitle.getText();
				String creator = txtCreator.getText();
				String note = txtNote.getText();
				
				//Get type
				TaskItem.Type type = null;
				int idx = comboType.getSelectedIndex();
				if (idx < 0 || idx >= types.length) {
					//If problem, show error and remain on page.
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid task information");
					done = false;
				} else {
					switch (idx) {
					case 0: type = TaskItem.Type.FEATURE; break;
					case 1: type = TaskItem.Type.BUG; break;
					case 2: type = TaskItem.Type.TECHNICAL_WORK; break;
					case 3: type = TaskItem.Type.KNOWLEDGE_ACQUISITION; break;
					default: type = null;
					}
				}
				//Get instance of model and add task
				try {
					ScrumBacklogModel.getInstance().addTaskItemToList(title, type, creator, note);
				} catch (IllegalArgumentException exp) {
					done = false;
					JOptionPane.showMessageDialog(ScrumBacklogGUI.this, "Invalid task information.");
				}
			} 
			if (done) {
				//All buttons lead to back task list
				cardLayout.show(panel, TASK_ITEM_LIST_PANEL);
				pnlTaskItemList.updateTable(null, null);
				ScrumBacklogGUI.this.repaint();
				ScrumBacklogGUI.this.validate();
				//Reset fields
				txtTitle.setText("");
				txtCreator.setText("");
				txtNote.setText("");
			}
		}
	}
}
