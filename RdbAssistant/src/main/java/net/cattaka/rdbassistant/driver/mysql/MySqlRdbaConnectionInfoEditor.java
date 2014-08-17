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
 * $Id: MySqlRdbaConnectionInfoEditor.java 258 2010-02-27 13:43:19Z cattaka $
 */
package net.cattaka.rdbassistant.driver.mysql;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPasswordField;

import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.gui.connectioninfo.RdbaConnectionInfoEditor;
import net.cattaka.swing.text.StdTextField;
import net.cattaka.util.MessageBundle;

public class MySqlRdbaConnectionInfoEditor extends RdbaConnectionInfoEditor {
	private static final long serialVersionUID = 1L;
	private StdTextField labelField;
	private StdTextField hostnameField;
	private StdTextField portField;
	private StdTextField databaseField;
	private StdTextField usernameField;
	private JPasswordField passwordField;
	
	public MySqlRdbaConnectionInfoEditor() {
		makeLayout();
	}
	
	private void makeLayout() {
		JLabel labelLabel = new JLabel(MessageBundle.getInstance().getMessage("label"));
		JLabel hostnameLabel = new JLabel(MessageBundle.getInstance().getMessage("hostname"));
		JLabel portLabel = new JLabel(MessageBundle.getInstance().getMessage("port"));
		JLabel databaseLabel = new JLabel(MessageBundle.getInstance().getMessage("database"));
		JLabel usernameLabel = new JLabel(MessageBundle.getInstance().getMessage("username"));
		JLabel passwordLabel = new JLabel(MessageBundle.getInstance().getMessage("password"));
		labelField = new StdTextField();
		hostnameField = new StdTextField();
		portField = new StdTextField();
		databaseField = new StdTextField();
		usernameField = new StdTextField();
		passwordField = new JPasswordField();
		
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
		gbl.setConstraints(hostnameLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(portLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(databaseLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(usernameLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(passwordLabel, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx=1.0;
		gbl.setConstraints(labelField, gbc);
		gbc.gridy++;
		gbl.setConstraints(hostnameField, gbc);
		gbc.gridy++;
		gbl.setConstraints(portField, gbc);
		gbc.gridy++;
		gbl.setConstraints(databaseField, gbc);
		gbc.gridy++;
		gbl.setConstraints(usernameField, gbc);
		gbc.gridy++;
		gbl.setConstraints(passwordField, gbc);

		this.setLayout(gbl);
		this.add(labelLabel);
		this.add(labelField);
		this.add(hostnameLabel);
		this.add(hostnameField);
		this.add(portLabel);
		this.add(portField);
		this.add(databaseLabel);
		this.add(databaseField);
		this.add(usernameLabel);
		this.add(usernameField);
		this.add(passwordLabel);
		this.add(passwordField);
	}
	
	@Override
	public boolean load(RdbaConnectionInfo rdbaConnectionInfo) {
		if (!(rdbaConnectionInfo instanceof MySqlRdbaConnectionInfo)) {
			return false;
		}
		MySqlRdbaConnectionInfo info = (MySqlRdbaConnectionInfo)rdbaConnectionInfo;
		labelField.setText(info.getLabel());
		hostnameField.setText(info.getHost());
		portField.setText(info.getPort().toString());
		databaseField.setText(info.getDatabase());
		usernameField.setText(info.getUserName());
		passwordField.setText(info.getPassword());
		return false;
	}

	@Override
	public RdbaConnectionInfo save() {
		MySqlRdbaConnectionInfo info = new MySqlRdbaConnectionInfo();
		info.setLabel(labelField.getText());
		info.setHost(hostnameField.getText());
		info.setPort(Integer.valueOf(portField.getText()));
		info.setDatabase(databaseField.getText());
		info.setUserName(usernameField.getText());
		info.setPassword(String.valueOf(passwordField.getPassword()));
		return info;
	}

}
