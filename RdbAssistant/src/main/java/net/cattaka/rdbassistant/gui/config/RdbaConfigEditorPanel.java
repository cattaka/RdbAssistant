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
 * $Id: RdbaConfigEditorPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.config;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import net.cattaka.rdbassistant.RdbaConfigConstants;
import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.swing.datainputpanel.DIPInfo;
import net.cattaka.swing.datainputpanel.DIPInfoFile;
import net.cattaka.swing.datainputpanel.DIPInfoFont;
import net.cattaka.swing.datainputpanel.DIPInfoSelect;
import net.cattaka.swing.datainputpanel.DIPInfoString;
import net.cattaka.swing.datainputpanel.DIPInfoSwitch;
import net.cattaka.swing.datainputpanel.DataInputPanel;
import net.cattaka.swing.util.LookAndFeelBundle;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class RdbaConfigEditorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DataInputPanel dataInputPanel;
	
	private DIPInfoFont fontForEditor;
	private DIPInfoFont fontForTable;
	private DIPInfoSwitch displayStringIfNull;
	private DIPInfoString nullString;
	private DIPInfoFile sqlQuickAccess;
	private DIPInfoFile scriptWorkDir;
	private DIPInfoFile jdbcJarMysql;
	private DIPInfoFile jdbcJarOracle;
	private DIPInfoFile jdbcJarSqlite;
	private DIPInfoSelect lookAndFeel;
	private DIPInfoSelect logLevel;
	
	class ActionListenerEx implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("approve")) {
				if (dataInputPanel.isValidData()) {
					doApprove();
				} else {
					dataInputPanel.validateData();
				}
			} else if (e.getActionCommand().equals("cancel")) {
				doCancel();
			}
		}
	}
	
	public RdbaConfigEditorPanel() {
		makeLayout();
	}
	
	private void makeLayout() {
		fontForEditor = new DIPInfoFont(MessageBundle.getMessage("font_for_editor"), new Font(RdbaConfigConstants.DEFAULT_FONT_EDITOR, 0 , 12));
		fontForTable = new DIPInfoFont(MessageBundle.getMessage("font_for_table"), new Font(RdbaConfigConstants.DEFAULT_FONT_EDITOR, 0 , 12));
		nullString = new DIPInfoString(MessageBundle.getMessage("display_null_as"),RdbaConstants.DEFAULT_NULL_STRING);
		displayStringIfNull = new DIPInfoSwitch(MessageBundle.getMessage("display_string_if_null"), false, new DIPInfo[]{nullString}, new DIPInfo[0]);
		sqlQuickAccess = new DIPInfoFile(MessageBundle.getMessage("sql_quick_access"), "", DIPInfoFile.MODE_OPEN);
		scriptWorkDir = new DIPInfoFile(MessageBundle.getMessage("script_work_dir"), "", DIPInfoFile.MODE_OPEN);
		jdbcJarMysql = new DIPInfoFile(MessageBundle.getMessage("jdbc_jar_mysql"), "", DIPInfoFile.MODE_OPEN);
		jdbcJarOracle = new DIPInfoFile(MessageBundle.getMessage("jdbc_jar_oracle"), "", DIPInfoFile.MODE_OPEN);
		jdbcJarSqlite = new DIPInfoFile(MessageBundle.getMessage("jdbc_jar_sqlite"), "", DIPInfoFile.MODE_OPEN);
		lookAndFeel  = new DIPInfoSelect(MessageBundle.getMessage("look_and_feel"), LookAndFeelBundle.getLookAndFeelNames(), LookAndFeelBundle.getLookAndFeelClassNames(), LookAndFeelBundle.getDefaultLookAndFeelName());
		logLevel = new DIPInfoSelect(MessageBundle.getMessage("log_level"), ExceptionHandler.getPriorityNames(), ExceptionHandler.Priority.WARNING.name());
		
		sqlQuickAccess.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		scriptWorkDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		DIPInfo[] dipis = new DIPInfo[] {
				fontForEditor,
				fontForTable,
				nullString,
				displayStringIfNull,
				sqlQuickAccess,
				scriptWorkDir,
				jdbcJarMysql,
				jdbcJarOracle,
				jdbcJarSqlite,
				lookAndFeel,
				logLevel
		};
		
		ActionListener al = new ActionListenerEx();
		dataInputPanel = new DataInputPanel(dipis);
		dataInputPanel.getButtonApprove().addActionListener(al);
		dataInputPanel.getButtonApprove().setActionCommand("approve");
		dataInputPanel.getButtonCancel().addActionListener(al);
		dataInputPanel.getButtonCancel().setActionCommand("cancel");

		this.setLayout(new GridLayout());
		this.add(dataInputPanel);
	}
	
	public void loadRdbaConfig(RdbaConfig rdbaConfig) {
		fontForEditor.setValue(rdbaConfig.getFontForEditor());
		fontForTable.setValue(rdbaConfig.getFontForTable());
		displayStringIfNull.setValue(rdbaConfig.isDisplayStringIfNull());
		nullString.setValue(rdbaConfig.getNullString());
		sqlQuickAccess.setValue(rdbaConfig.getSqlQuickAccessRoot());
		scriptWorkDir.setValue(rdbaConfig.getScriptRoot());
		jdbcJarMysql.setStringValue(rdbaConfig.getJdbcJarMysql());
		jdbcJarOracle.setStringValue(rdbaConfig.getJdbcJarOracle());
		jdbcJarSqlite.setStringValue(rdbaConfig.getJdbcJarSqlite());
		lookAndFeel.setObjectValue(rdbaConfig.getLookAndFeel());
		logLevel.setStringValue(rdbaConfig.getLogLevel());
	}

	public void saveRdbaConfig(RdbaConfig rdbaConfig) {
		rdbaConfig.setFontForEditor(fontForEditor.getFontValue());
		rdbaConfig.setFontForTable(fontForTable.getFontValue());
		rdbaConfig.setDisplayStringIfNull(displayStringIfNull.getBooleanValue());
		rdbaConfig.setNullString(nullString.getStringValue());
		rdbaConfig.setSqlQuickAccessRoot(sqlQuickAccess.getFileValue());
		rdbaConfig.setScriptRoot(scriptWorkDir.getFileValue());
		rdbaConfig.setJdbcJarMysql(jdbcJarMysql.getStringValue());
		rdbaConfig.setJdbcJarOracle(jdbcJarOracle.getStringValue());
		rdbaConfig.setJdbcJarSqlite(jdbcJarSqlite.getStringValue());
		rdbaConfig.setLookAndFeel(lookAndFeel.getObjectValue().toString());
		rdbaConfig.setLogLevel(logLevel.getStringValue());
	}

	public void doApprove() {
		
	}
	public void doCancel() {
		
	}
}
