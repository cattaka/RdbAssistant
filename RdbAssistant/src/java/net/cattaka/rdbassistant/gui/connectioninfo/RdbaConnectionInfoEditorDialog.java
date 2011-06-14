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
 * $Id: RdbaConnectionInfoEditorDialog.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.rdbassistant.gui.connectioninfo;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.driver.dummy.DummyRdbaConnectionInfo;
import net.cattaka.rdbassistant.driver.jdbc.JdbcRdbaConnectionInfo;
import net.cattaka.rdbassistant.driver.mysql.MySqlRdbaConnectionInfo;
import net.cattaka.rdbassistant.driver.oracle.OracleRdbaConnectionInfo;
import net.cattaka.rdbassistant.driver.sqlite.SqliteRdbaConnectionInfo;
import net.cattaka.rdbassistant.driver.telnetsqlite.TelnetSqliteRdbaConnectionInfo;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class RdbaConnectionInfoEditorDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private RdbaConnectionInfo[] rdbaConnectionInfoArray = new RdbaConnectionInfo[] {
		new JdbcRdbaConnectionInfo(),
		new OracleRdbaConnectionInfo(),
		new MySqlRdbaConnectionInfo(),
		new SqliteRdbaConnectionInfo(),
		new TelnetSqliteRdbaConnectionInfo(),
		new DummyRdbaConnectionInfo(),
	}; 
	private RdbaConnectionInfo rdbaConnectionInfo = null;
	
	private JComboBox rdbmsComboBox;
	private JPanel editorPanel;
	private RdbaConnectionInfoEditor rdbaConnectionInfoEditor;

	// ========================================================================
	
	class ActionListenerForEditorFrame implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("ok")) {
				rdbaConnectionInfo = rdbaConnectionInfoEditor.save();
				if (rdbaConnectionInfo != null) {
					setVisible(false);
				}
			} else if (e.getActionCommand().equals("cancel")) {
				rdbaConnectionInfo = null;
				setVisible(false);
			}
		}
	}
	
	class ItemListenerForEditorPane implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			updateRdbaConnectionInfo();
		}
	}
	
	// ========================================================================
	
	public RdbaConnectionInfoEditorDialog(Frame parentFrame) {
		super(parentFrame);
		this.setSize(500,250);
		this.setModal(true);
		makeLayout();
		updateRdbaConnectionInfo();
	}
	
	private void makeLayout() {
		ActionListener al = new ActionListenerForEditorFrame();
		JButton okButton = new JButton();
		JButton cancelButton = new JButton();
		okButton.setText(MessageBundle.getMessage("ok"));
		okButton.setActionCommand("ok");
		okButton.addActionListener(al);
		cancelButton.setText(MessageBundle.getMessage("cancel"));
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(al);
		
		rdbmsComboBox = new JComboBox();
		for (int i=0;i<rdbaConnectionInfoArray.length;i++) {
			rdbmsComboBox.addItem(rdbaConnectionInfoArray[i].getRdbmsName());
		}
		rdbmsComboBox.addItemListener(new ItemListenerForEditorPane());
		editorPanel = new JPanel();
		editorPanel.setLayout(new GridLayout());
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx=0.0;
		gbc.weighty=0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(rdbmsComboBox, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx=1.0;
		gbc.weighty=1.0;
		gbc.gridy++;
		gbl.setConstraints(editorPanel, gbc);
		
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx=0.0;
		gbc.weighty=0.0;
		gbc.gridy++;
		gbl.setConstraints(okButton, gbc);
		gbc.gridx++;
		gbl.setConstraints(cancelButton, gbc);
		
		this.getContentPane().setLayout(gbl);
		this.getContentPane().add(rdbmsComboBox);
		this.getContentPane().add(editorPanel);
		this.getContentPane().add(okButton);
		this.getContentPane().add(cancelButton);
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	}
	
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING ) {
			this.rdbaConnectionInfo = null;
			this.setVisible(false);
	    }
	}
	
	public RdbaConnectionInfo getRdbaConnectionInfo() {
		return rdbaConnectionInfo;
	}

	public void setRdbaConnectionInfo(RdbaConnectionInfo rdbaConnectionInfo) {
		this.rdbmsComboBox.setSelectedItem(rdbaConnectionInfo.getRdbmsName());
		this.rdbaConnectionInfo = rdbaConnectionInfo;
		this.updateEditorPanel();
	}

	private void updateRdbaConnectionInfo() {
		int idx = rdbmsComboBox.getSelectedIndex();
		try {
			rdbaConnectionInfo = rdbaConnectionInfoArray[idx];
		} catch (Exception exc) {
			ExceptionHandler.error(exc);
			throw new RuntimeException(exc);
		}
		updateEditorPanel();
	}
	
	private void updateEditorPanel() {
		try {
			rdbaConnectionInfoEditor = rdbaConnectionInfo.createEditor();
			rdbaConnectionInfoEditor.load(rdbaConnectionInfo);
			editorPanel.removeAll();
			editorPanel.add(rdbaConnectionInfoEditor);
			editorPanel.validate();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
