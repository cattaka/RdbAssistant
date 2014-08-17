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
 * $Id: SqliteRdbaConnectionInfoEditor.java 258 2010-02-27 13:43:19Z cattaka $
 */
package net.cattaka.rdbassistant.driver.sqlite;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.gui.connectioninfo.RdbaConnectionInfoEditor;
import net.cattaka.swing.datainputpanel.DIPInfoFile;
import net.cattaka.swing.text.StdTextField;
import net.cattaka.util.MessageBundle;

public class SqliteRdbaConnectionInfoEditor extends RdbaConnectionInfoEditor {
	private static final long serialVersionUID = 1L;
	private StdTextField labelField;
	private DIPInfoFile databaseField;
	
	public SqliteRdbaConnectionInfoEditor() {
		makeLayout();
	}
	
	private void makeLayout() {
		JLabel labelLabel = new JLabel(MessageBundle.getInstance().getMessage("label"));
		JLabel databaseLabel = new JLabel(MessageBundle.getInstance().getMessage("database"));
		labelField = new StdTextField();
		databaseField = new DIPInfoFile("", "", DIPInfoFile.MODE_OPEN);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx=0.0;
		gbc.weighty=0.0;
		gbl.setConstraints(labelLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(databaseLabel, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx=1.0;
		gbl.setConstraints(labelField, gbc);
		gbc.gridy++;
		gbl.setConstraints(databaseField.getComponent(), gbc);

		this.setLayout(gbl);
		this.add(labelLabel);
		this.add(labelField);
		this.add(databaseLabel);
		this.add(databaseField.getComponent());
	}
	
	@Override
	public boolean load(RdbaConnectionInfo rdbaConnectionInfo) {
		if (!(rdbaConnectionInfo instanceof SqliteRdbaConnectionInfo)) {
			return false;
		}
		SqliteRdbaConnectionInfo info = (SqliteRdbaConnectionInfo)rdbaConnectionInfo;
		labelField.setText(info.getLabel());
		databaseField.setStringValue(info.getDatabase());
		return false;
	}

	@Override
	public RdbaConnectionInfo save() {
		SqliteRdbaConnectionInfo info = new SqliteRdbaConnectionInfo();
		info.setLabel(labelField.getText());
		info.setDatabase(String.valueOf(databaseField.getStringValue()));
		return info;
	}
}
