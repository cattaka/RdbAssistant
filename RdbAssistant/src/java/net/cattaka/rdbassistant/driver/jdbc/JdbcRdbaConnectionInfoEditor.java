/*
 * Copyright (c) 2010, Takao Sumitomo
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
 * $Id: SqliteRdbaConnectionInfoEditor.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.driver.jdbc;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.config.RdbaJdbcBundle.MyURLClassLoader;
import net.cattaka.rdbassistant.core.RdbaException;
import net.cattaka.rdbassistant.gui.connectioninfo.RdbaConnectionInfoEditor;
import net.cattaka.rdbassistant.util.RdbaGuiUtil;
import net.cattaka.swing.datainputpanel.DIPInfoFile;
import net.cattaka.swing.text.StdTextField;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.StringUtil;

public class JdbcRdbaConnectionInfoEditor extends RdbaConnectionInfoEditor {
	private static final long serialVersionUID = 1L;
	private StdTextField labelField;
	private DIPInfoFile driverFileField;
	private JComboBox driverClassNameCombo;
	private StdTextField urlField;
	private StdTextField usernameField;
	private JPasswordField passwordField;

	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("reloadDriverFile")) {
				try {
					reloadDriverFile();
				} catch(RdbaException exc) {
					JOptionPane.showMessageDialog(RdbaGuiUtil.getParentFrame(JdbcRdbaConnectionInfoEditor.this), exc.getMessage());
					ExceptionHandler.info(exc);
				}
			}
		}
	}
	
	public JdbcRdbaConnectionInfoEditor() {
		makeLayout();
	}
	
	private void makeLayout() {
		JLabel labelLabel = new JLabel(MessageBundle.getMessage("label"));
		JLabel driverFileLabel = new JLabel(MessageBundle.getMessage("jdbc_file"));
		JLabel driverClassNameLabel = new JLabel(MessageBundle.getMessage("jdbc_class_name"));
		JLabel urlLabel = new JLabel(MessageBundle.getMessage("url"));
		JLabel usernameLabel = new JLabel(MessageBundle.getMessage("username"));
		JLabel passwordLabel = new JLabel(MessageBundle.getMessage("password"));
		JButton reloadDriverFileButton = new JButton(MessageBundle.getMessage("update"));
		
		labelField = new StdTextField();
		driverFileField = new DIPInfoFile("", "", DIPInfoFile.MODE_OPEN); 
		driverClassNameCombo = new JComboBox();
		urlField = new StdTextField();
		usernameField = new StdTextField();
		passwordField = new JPasswordField();
		
		reloadDriverFileButton.addActionListener(new ActionListenerImpl());
		reloadDriverFileButton.setActionCommand("reloadDriverFile");
		
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
		gbl.setConstraints(driverFileLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(driverClassNameLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(urlLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(usernameLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(passwordLabel, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx=1.0;
		gbc.gridwidth=2;
		gbl.setConstraints(labelField, gbc);
		gbc.gridy++;
		gbl.setConstraints(driverFileField.getComponent(), gbc);
		gbc.gridy++;
		gbc.gridwidth=1;
		gbl.setConstraints(driverClassNameCombo, gbc);
		gbc.gridx++;
		gbc.weightx=0.0;
		gbl.setConstraints(reloadDriverFileButton, gbc);
		gbc.gridx--;
		gbc.gridy++;
		gbc.weightx=1.0;
		gbc.gridwidth=2;
		gbl.setConstraints(urlField, gbc);
		gbc.gridy++;
		gbl.setConstraints(usernameField, gbc);
		gbc.gridy++;
		gbl.setConstraints(passwordField, gbc);

		this.setLayout(gbl);
		this.add(labelLabel);
		this.add(labelField);
		this.add(driverFileLabel);
		this.add(driverFileField.getComponent());
		this.add(driverClassNameLabel);
		this.add(driverClassNameCombo);
		this.add(urlLabel);
		this.add(urlField);
		this.add(usernameLabel);
		this.add(usernameField);
		this.add(passwordLabel);
		this.add(passwordField);
		this.add(reloadDriverFileButton);
	}
	
	@Override
	public boolean load(RdbaConnectionInfo rdbaConnectionInfo) {
		if (!(rdbaConnectionInfo instanceof JdbcRdbaConnectionInfo)) {
			return false;
		}
		JdbcRdbaConnectionInfo info = (JdbcRdbaConnectionInfo)rdbaConnectionInfo;
		labelField.setText(info.getLabel());
		driverFileField.setStringValue(info.getDriverFile());
		driverClassNameCombo.setSelectedItem(info.getDriverClassName());
		if (info.getDriverFile() != null && !info.getDriverClassName().equals(driverClassNameCombo.getSelectedItem())) {
			driverClassNameCombo.addItem(info.getDriverClassName());
			driverClassNameCombo.setSelectedItem(info.getDriverClassName());
		}
		urlField.setText(info.getUrl());
		usernameField.setText(info.getUserName());
		passwordField.setText(info.getPassword());
		return false;
	}

	@Override
	public RdbaConnectionInfo save() {
		JdbcRdbaConnectionInfo info = new JdbcRdbaConnectionInfo();
		info.setLabel(labelField.getText());
		info.setDriverFile(String.valueOf(driverFileField.getStringValue()));
		info.setDriverClassName(String.valueOf(driverClassNameCombo.getSelectedItem()));
		info.setUrl(urlField.getText());
		info.setUserName(usernameField.getText());
		info.setPassword(String.valueOf(passwordField.getPassword()));
		return info;
	}
	
	private void reloadDriverFile() throws RdbaException {
		String driverFile = driverFileField.getStringValue();
		// JDBCのJarのURLを取得
		URL jdbcUrl = null;
		File jdbcFile = new File(driverFile);
		if (jdbcFile.exists()) {
			try {
				jdbcUrl = jdbcFile.toURI().toURL();
			} catch (MalformedURLException e2) {
				// 面倒見切れん
			}
		}
		if (jdbcUrl == null) {
			// URLが見つからないので諦め
			throw new RdbaException(MessageBundle.getMessage("jdbc_driver_file_not_found"));
		}
		
		// Zipファイルとしてclassを検索
		List<String> classNameList = new ArrayList<String>();
		try {
			ZipFile zipFile = new ZipFile(jdbcFile);
			Enumeration<? extends ZipEntry> entrys = zipFile.entries();
			while (entrys.hasMoreElements()) {
				ZipEntry entry = entrys.nextElement();
				String resourceName = entry.getName();
				if (resourceName.toLowerCase().endsWith(".class")) {
					String className = resourceName.substring(0,resourceName.length() - ".class".length()); 
					className = StringUtil.replaceString(className, "/", ".");
					classNameList.add(className);
				}
			}
		} catch (IOException e) {
			throw new RdbaException(e);
		}
		
		// 各クラスがDriverの子供かチェックする
		MyURLClassLoader urlClassLoader = new MyURLClassLoader(new URL[]{jdbcUrl});
		urlClassLoader.getExtraPermission().add(new SocketPermission("*", "connect,resolve"));
		
		List<String> resultList = new ArrayList<String>();
		for (String className: classNameList) {
			Class<?> loadedClass = null;
			try {
				loadedClass = urlClassLoader.loadClass(className);
			} catch (NoClassDefFoundError e) {
				continue;
			} catch (ClassNotFoundException e) {
				continue;
			}
			if (Driver.class.isAssignableFrom(loadedClass)) {
				resultList.add(className);
			}
		}
		
		driverClassNameCombo.removeAllItems();
		for (String result : resultList) {
			driverClassNameCombo.addItem(result);
		}
		
		// Jarファイルをロード
//		MyURLClassLoader urlClassLoader = new MyURLClassLoader(new URL[]{jdbcUrl});
//		urlClassLoader.getExtraPermission().add(new SocketPermission("*", "connect,resolve"));
//		Class<?> driverClass;
//		urlClassLoader.
//		try {
//			driverClass =  urlClassLoader.loadClass(this.driverClassName);
//		} catch (ClassNotFoundException e) {
//			throw new RdbaException(MessageBundle.getMessage("jdbc_class_not_found"));
//		}
	}
}
