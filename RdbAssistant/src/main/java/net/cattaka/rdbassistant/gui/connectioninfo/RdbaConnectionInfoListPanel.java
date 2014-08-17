/*
 * Copyright (c) 2009, Takao Sumitomo
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the 
 *       above copyright notice, this list of conditions
 *       and the following disclaimer.
 *     * Redistributions in binary form must reproduce
 *       the above copyright notice, this list of
 *       conditions and the following disclaimer in the
 *       documentation and/or other materials provided
 *       with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software
 * and documentation are those of the authors and should
 * not be interpreted as representing official policies,
 * either expressed or implied.
 */
/*
 * $Id: RdbaConnectionInfoListPanel.java 271 2010-03-02 13:40:36Z cattaka $
 */
package net.cattaka.rdbassistant.gui.connectioninfo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.rdbassistant.util.RdbaGuiUtil;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.MessageBundle;

abstract public class RdbaConnectionInfoListPanel extends JPanel implements RdbaGuiInterface {
	private static final long serialVersionUID = 1L;
	
	private RdbaGuiInterface parentComponent;
	private List<RdbaConnectionInfo> connectionInfoList;
	private JTable connectionInfoTable;
	private JButton connectButton;
	private JButton cancelButton;
	
	private RdbaConnectionInfo rdbaConnectionInfo;

	class ActionLintenerForRdbaConnectionInfoPanel implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("move_up")) {
				int rowIdx = connectionInfoTable.getSelectedRow();
				if (rowIdx != -1 && rowIdx > 0) {
					RdbaConnectionInfo t = connectionInfoList.remove(rowIdx);
					connectionInfoList.add(rowIdx-1, t);
					updateConnectionInfoTable();
					
					connectionInfoTable.changeSelection(rowIdx-1, 0, false, false);
				}
			} else if (e.getActionCommand().equals("move_down")) {
				int rowIdx = connectionInfoTable.getSelectedRow();
				if (rowIdx != -1 && rowIdx < connectionInfoList.size()-1) {
					RdbaConnectionInfo t = connectionInfoList.remove(rowIdx);
					connectionInfoList.add(rowIdx+1, t);
					updateConnectionInfoTable();

					connectionInfoTable.changeSelection(rowIdx+1, 0, false, false);
				}
			} else if (e.getActionCommand().equals("new")) {
				RdbaConnectionInfoEditorDialog dialog = new RdbaConnectionInfoEditorDialog(RdbaGuiUtil.getParentFrame(RdbaConnectionInfoListPanel.this));
				
				dialog.setLocationRelativeTo(RdbaConnectionInfoListPanel.this);
				dialog.setVisible(true);
				RdbaConnectionInfo t = dialog.getRdbaConnectionInfo();
				if (t != null) {
					connectionInfoList.add(t);
					updateConnectionInfoTable();
				}
				dialog.dispose();
			} else if (e.getActionCommand().equals("copy")) {
				int rowIdx = connectionInfoTable.getSelectedRow();
				if (rowIdx != -1) {
					RdbaConnectionInfo orig = connectionInfoList.get(rowIdx);
					connectionInfoList.add(rowIdx+1, orig.createClone());
					updateConnectionInfoTable();
				}
			} else if (e.getActionCommand().equals("edit")) {
				RdbaConnectionInfoEditorDialog dialog = new RdbaConnectionInfoEditorDialog(RdbaGuiUtil.getParentFrame(RdbaConnectionInfoListPanel.this));

				int rowIdx = connectionInfoTable.getSelectedRow();
				if (rowIdx != -1) {
					RdbaConnectionInfo orig = connectionInfoList.get(rowIdx);
					dialog.setRdbaConnectionInfo(orig);
					dialog.setLocationRelativeTo(RdbaConnectionInfoListPanel.this);
					dialog.setVisible(true);
					RdbaConnectionInfo t = dialog.getRdbaConnectionInfo();
					if (t != null) {
						connectionInfoList.remove(rowIdx);
						connectionInfoList.add(rowIdx, t);
						updateConnectionInfoTable();
					}
				}
				dialog.dispose();
			} else if (e.getActionCommand().equals("delete")) {
				int rowIdx = connectionInfoTable.getSelectedRow();
				if (rowIdx != -1) {
					connectionInfoList.remove(rowIdx);
					updateConnectionInfoTable();
				}
			} else if (e.getActionCommand().equals("connect")) {
				connect();
			} else if (e.getActionCommand().equals("cancel")) {
				rdbaConnectionInfo = null;
				doCancel();
			}
		}
	}
	
	class JTableForRdbaConnectionInfoPanel extends JTable {
		private static final long serialVersionUID = 1L;

		public JTableForRdbaConnectionInfoPanel() {
			super();
			this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
	
	public RdbaConnectionInfoListPanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		this.connectionInfoList = new ArrayList<RdbaConnectionInfo>();
		makeLayout();
	}

	private void makeLayout() {
		ActionListener al = new ActionLintenerForRdbaConnectionInfoPanel();
		
		connectionInfoTable = new JTableForRdbaConnectionInfoPanel();
		connectionInfoTable.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					connect();
				}
			}
		});
		
		JButton upButton;
		JButton downButton;
		JButton addButton;
		JButton editButton;
		JButton deleteButton;
		JButton copyButton;
		StdScrollPane connectionInfoScrollPane = new StdScrollPane(connectionInfoTable); 

		connectButton = new JButton();
		connectButton.setActionCommand("connect");
		connectButton.addActionListener(al);
		cancelButton = new JButton();
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(al);
		upButton = new JButton();
		upButton.setActionCommand("move_up");
		upButton.addActionListener(al);
		downButton = new JButton();
		downButton.setActionCommand("move_down");
		downButton.addActionListener(al);
		addButton = new JButton();
		addButton.setActionCommand("new");
		addButton.addActionListener(al);
		editButton = new JButton();
		editButton.setActionCommand("edit");
		editButton.addActionListener(al);
		deleteButton = new JButton();
		deleteButton.setActionCommand("delete");
		deleteButton.addActionListener(al);
		copyButton = new JButton();
		copyButton.setActionCommand("copy");
		copyButton.addActionListener(al);
		ButtonsBundle.getInstance().applyButtonDifinition(connectButton, "connect");
		ButtonsBundle.getInstance().applyButtonDifinition(cancelButton, "cancel");
		ButtonsBundle.getInstance().applyButtonDifinition(upButton, "move_up");
		ButtonsBundle.getInstance().applyButtonDifinition(downButton, "move_down");
		ButtonsBundle.getInstance().applyButtonDifinition(addButton, "new");
		ButtonsBundle.getInstance().applyButtonDifinition(editButton, "edit");
		ButtonsBundle.getInstance().applyButtonDifinition(deleteButton, "delete");
		ButtonsBundle.getInstance().applyButtonDifinition(copyButton, "copy");

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.gridheight = 8;
		gbc.weightx=1.0;
		gbc.weighty=1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(connectionInfoScrollPane, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx=0.0;
		gbc.weighty=0.0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbl.setConstraints(upButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(downButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(addButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(editButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(deleteButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(copyButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(connectButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(cancelButton, gbc);
		
		this.setLayout(gbl);
		this.add(connectionInfoScrollPane);
		this.add(connectButton);
		this.add(cancelButton);

		this.add(upButton);
		this.add(downButton);
		this.add(addButton);
		this.add(editButton);
		this.add(deleteButton);
		this.add(copyButton);
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING ) {
			this.rdbaConnectionInfo = null;
			this.setVisible(false);
	    }
	}

	public List<RdbaConnectionInfo> getConnectionInfoList() {
		return connectionInfoList;
	}
	
	public RdbaConnectionInfo getRdbaConnectionInfo() {
		return rdbaConnectionInfo;
	}

	/**
	 * コネクトボタン押下時 or リストのダブルクリック時の処理
	 */
	private void connect() {
		int rowIdx = connectionInfoTable.getSelectedRow();
		if (rowIdx != -1) {
			rdbaConnectionInfo = connectionInfoList.get(rowIdx);
			if (rdbaConnectionInfo != null) {
				doConnect(rdbaConnectionInfo);
			}
		}
	}
	
	private void updateConnectionInfoTable() {
		if (this.connectionInfoList != null) {
			String[] header = new String[]{
					MessageBundle.getInstance().getMessage("target_rdbms"),
					MessageBundle.getInstance().getMessage("target_label"),
					MessageBundle.getInstance().getMessage("target_database"),
					MessageBundle.getInstance().getMessage("target_user")
				};
			
			ArrayList<String[]> displayStringsList = new ArrayList<String[]>();
			
			for (RdbaConnectionInfo info : connectionInfoList) {
				if (info != null) {
					displayStringsList.add(info.getDisplayStrings());
				}
			}
			String[][] displayStrings = new String[displayStringsList.size()][];
			displayStringsList.toArray(displayStrings);
	
			DefaultTableModel tableModel = new DefaultTableModel(displayStrings, header);
			connectionInfoTable.setModel(tableModel);
		}
		updateRdbaConfig();
	}

	/**
	 * アプリ全体のコンフィグを更新する。
	 */
	private void updateRdbaConfig() {
		this.getRdbaConfig().setRdbaConnectionInfo(this.connectionInfoList);
	}
	
	public void setButtonsVisible(boolean connectVisble, boolean cancelVisible) {
		connectButton.setVisible(connectVisble);
		cancelButton.setVisible(cancelVisible);
	}
	
	public void doGuiLayout() {
		// なし
	}

	public RdbaConfig getRdbaConfig() {
		return this.parentComponent.getRdbaConfig();
	}

	public void reloadRdbaConfig() {
		this.connectionInfoList.clear();
		this.connectionInfoList.addAll(this.getRdbaConfig().getRdbaConnectionInfo());
		updateConnectionInfoTable();
	}

	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		this.parentComponent.sendRdbaMessage(rdbaMessage);
	}
	public void relayRdbaMessage(RdbaMessage rbdaMessage) {
		// なし
	}
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return this.parentComponent.getRdbaSingletonBundle();
	}

	abstract public void doConnect(RdbaConnectionInfo rci);
	abstract public void doCancel();
}
