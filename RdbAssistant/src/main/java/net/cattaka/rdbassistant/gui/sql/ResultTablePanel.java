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
 * $Id: ResultTablePanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.sql;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.rdbassistant.gui.table.ResultSetTablePanel;
import net.cattaka.rdbassistant.gui.table.ResultSetTableModel;
import net.cattaka.swing.util.ButtonsBundle;

public class ResultTablePanel extends JPanel implements RdbaGuiInterface {
	private static final long serialVersionUID = 1L;
	private ResultSetTablePanel resultSetTable;
	private RdbaGuiInterface parentComponent;
	
	class ButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("transpose")) {
				transpose();
			}
		}
	}

	public ResultTablePanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		ButtonAction buttonAction = new ButtonAction();
		
		JButton transposeButton;
		{
			// ボタン作成
			Icon iconTranspose = (Icon)getRdbaSingletonBundle().getResource(RdbaMessageConstants.ICON_TRANSPOSE);
			transposeButton = new JButton();
			transposeButton.setActionCommand("transpose");
			transposeButton.addActionListener(buttonAction);
			ButtonsBundle.applyButtonDifinition(transposeButton, iconTranspose, "transpose", true);
		}
		resultSetTable = new ResultSetTablePanel();
		{
			resultSetTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			resultSetTable.setCellSelectionEnabled(true);
		}
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbl.setConstraints(transposeButton, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx++;
		gbc.gridheight = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbl.setConstraints(resultSetTable, gbc);
		
		this.setLayout(gbl);
		this.add(transposeButton);
		this.add(resultSetTable);
	}
	private void transpose() {
		this.resultSetTable.setTransposed(!this.resultSetTable.isTransposed());
	}

	public ResultSetTablePanel getResultSetTable() {
		return resultSetTable;
	}

	public void setResultSetTableModel(ResultSetTableModel dataModel) {
		resultSetTable.setResultSetTableModel(dataModel);
	}

	public void doGuiLayout() {
		// 無し
	}
	public RdbaConfig getRdbaConfig() {
		return parentComponent.getRdbaConfig();
	}
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return parentComponent.getRdbaSingletonBundle();
	}
	public void relayRdbaMessage(RdbaMessage rdbaMessage) {
		// 無し
	}
	public void reloadRdbaConfig() {
		RdbaConfig rdbaConfig = getRdbaConfig();
		this.resultSetTable.setFont(rdbaConfig.getFontForTable());
	}
	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		parentComponent.sendRdbaMessage(rdbaMessage);
	}
	
}
