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
 * $Id: RdbAssistantPanel.java 277 2010-03-02 14:01:27Z cattaka $
 */
package net.cattaka.rdbassistant.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.cattaka.jspf.JspfBundle;
import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.config.RdbaConfigUtil;
import net.cattaka.rdbassistant.gui.RdbaModeInterface.TargetMenu;
import net.cattaka.rdbassistant.gui.config.RdbaConfigEditorDialog;
import net.cattaka.rdbassistant.jspf.core.RdbaJspfBundle;
import net.cattaka.rdbassistant.jspf.standard.DefaultJspfBundle;
import net.cattaka.rdbassistant.script.RdbaScriptJavaSourceConverter;
import net.cattaka.rdbassistant.script.core.RdbaScriptUtil;
import net.cattaka.rdbassistant.util.DocumentDialog;
import net.cattaka.rdbassistant.util.RdbaGuiUtil;
import net.cattaka.swing.StdStatusBar;
import net.cattaka.swing.TextFileChooser;
import net.cattaka.swing.text.FindCondition;
import net.cattaka.swing.text.FindConditionDialog;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.swing.util.ExceptionViewDialog;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.ExtFileFilter;
import net.cattaka.util.MessageBundle;

public class RdbAssistantPanel extends JPanel implements RdbaGuiInterface {
	private static final long serialVersionUID = 1L;
	private RdbaConnectionTabbedPanel rdbaConnectionTabbedPanel;
	private RdbaConfig rdbaConfig = new RdbaConfig();
	private FindConditionDialogEx findConditionDialog;
	private StdStatusBar stdStatusBar;
	private JMenuBar menuBar;
	private RdbaSingletonBundle rdbaSingletonBundle;
	private JFileChooser configFileChooser;
	
	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("license")) {
				DocumentDialog dd = new DocumentDialog(RdbaGuiUtil.getParentFrame(RdbAssistantPanel.this), MessageBundle.getMessage("license"), "net/cattaka/rdbassistant/docs/license%1$s.utf8.txt");
				dd.setSize(500, 500);
				dd.setModal(true);
				dd.setLocationRelativeTo(RdbAssistantPanel.this);
				dd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dd.setVisible(true);
			} else if (e.getActionCommand().equals("readme")) {
				DocumentDialog dd = new DocumentDialog(RdbaGuiUtil.getParentFrame(RdbAssistantPanel.this), MessageBundle.getMessage("readme"), "net/cattaka/rdbassistant/docs/readme%1$s.utf8.txt");
				dd.setSize(500, 500);
				dd.setModal(true);
				dd.setLocationRelativeTo(RdbAssistantPanel.this);
				dd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dd.setVisible(true);
			} else if (e.getActionCommand().equals("about")) {
				DocumentDialog dd = new DocumentDialog(RdbaGuiUtil.getParentFrame(RdbAssistantPanel.this), MessageBundle.getMessage("about"), "net/cattaka/rdbassistant/docs/about%1$s.utf8.txt");
				dd.setSize(400, 200);
				dd.setModal(true);
				dd.setLocationRelativeTo(RdbAssistantPanel.this);
				dd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dd.setVisible(true);
			} else if (e.getActionCommand().equals("config")) {
				RdbaConfigEditorDialog dialog = new RdbaConfigEditorDialog(RdbaGuiUtil.getParentFrame(RdbAssistantPanel.this));
				RdbaConfig rdbaConfig = RdbAssistantPanel.this.getRdbaConfig();
				dialog.loadRdbaConfig(rdbaConfig);
				if (dialog.showEditor()) {
					dialog.saveRdbaConfig(rdbaConfig);
					RdbAssistantPanel.this.reloadRdbaConfig();
				}
				dialog.dispose();
			} else if (e.getActionCommand().equals("showLogList")) {
				ExceptionViewDialog dialog = ExceptionHandler.getExceptionViewDialog();
				dialog.setLocationRelativeTo(RdbAssistantPanel.this);
				dialog.setVisible(true);
			} else if (e.getActionCommand().equals("import_config")) {
				doImportConfig();
			} else if (e.getActionCommand().equals("export_config")) {
				doExportConfig();
			} else if (e.getActionCommand().equals("menu_exit")) {
				doExit();
			}
		}
	}
	
	class FindConditionDialogEx extends FindConditionDialog {
		private static final long serialVersionUID = 1L;
		
		public FindConditionDialogEx(Frame owner) throws HeadlessException {
			super(owner);
		}

		@Override
		public void doAction(FindCondition findCondition) {
			RdbaMessage rm = new RdbaMessage(RdbaMessageConstants.FINDCONDITIONDIALOG_ACTION,null,null,findCondition);
			relayRdbaMessage(rm);
		}
	}
	
	public RdbAssistantPanel(RdbaConfig rdbaConfig) {
		this.rdbaConfig = rdbaConfig;
		rdbaSingletonBundle = new RdbaSingletonBundle();
		makeLayout();
		this.reloadRdbaConfig();
	}
	
	public void makeLayout() {
		// JSPF
		{
			RdbaJspfBundle jspfBundle = new DefaultJspfBundle();
			rdbaSingletonBundle.setDefaultJspfBundle(jspfBundle);
			JspfBundle<RdbaScriptUtil> scriptBundle = new JspfBundle<RdbaScriptUtil>(new RdbaScriptJavaSourceConverter(), RdbaScriptUtil.class);
			rdbaSingletonBundle.setScriptBundle(scriptBundle);
		}
		// コンフィグファイル用ダイアログ
		{
			configFileChooser = new JFileChooser();
			configFileChooser.setFileFilter(new ExtFileFilter(".xml", MessageBundle.getMessage("config_xml_file")));
		}
		
		Icon gcIcon = (Icon)this.getRdbaSingletonBundle().getResource(RdbaMessageConstants.ICON_GC);
		this.menuBar = new JMenuBar();
		this.stdStatusBar = new StdStatusBar(gcIcon);
		this.rdbaConnectionTabbedPanel = new RdbaConnectionTabbedPanel(this);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbl.setConstraints(rdbaConnectionTabbedPanel,gbc);
		gbc.gridy++;
		gbc.weighty=0;
		gbl.setConstraints(stdStatusBar,gbc);
		
		this.setLayout(gbl);
		this.add(rdbaConnectionTabbedPanel);
		this.add(stdStatusBar);
		stdStatusBar.startMemoryUpdate();
		
		// メニューバーを作成(作成のみで配置は親パネル任せ)
		updateMenuBar();
	}
	
	private void updateMenuBar() {
		ActionListenerImpl al = new ActionListenerImpl();
		this.menuBar.removeAll();
		JMenu fileMenu = new JMenu();
		ButtonsBundle.applyButtonDifinition(fileMenu, "menu_file");
		{
			JMenuItem importConfigMenu = new JMenuItem();
			JMenuItem exportConfigMenu = new JMenuItem();
			JMenuItem exitMenu = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(importConfigMenu, "menu_import_config");
			ButtonsBundle.applyMenuDifinition(exportConfigMenu, "menu_export_config");
			ButtonsBundle.applyMenuDifinition(exitMenu, "menu_exit");
			importConfigMenu.setActionCommand("import_config");
			exportConfigMenu.setActionCommand("export_config");
			exitMenu.setActionCommand("menu_exit");
			importConfigMenu.addActionListener(al);
			exportConfigMenu.addActionListener(al);
			exitMenu.addActionListener(al);
			
			if (this.rdbaConnectionTabbedPanel.updateMenu(TargetMenu.FILE_MENU, fileMenu)) {
				fileMenu.addSeparator();
			}
			fileMenu.add(importConfigMenu);
			fileMenu.add(exportConfigMenu);
			fileMenu.addSeparator();
			fileMenu.add(exitMenu);

			menuBar.add(fileMenu);
		}
		JMenu editMenu = new JMenu();
		ButtonsBundle.applyButtonDifinition(editMenu, "menu_edit");
		{
			JMenuItem configMenu = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(configMenu, "menu_config");
			configMenu.setActionCommand("config");
			configMenu.addActionListener(al);
			
			if (this.rdbaConnectionTabbedPanel.updateMenu(TargetMenu.EDIT_MENU, editMenu)) {
				editMenu.addSeparator();
			}
			editMenu.add(configMenu);

			menuBar.add(editMenu);
		}
		// Extraメニューを追加する
		{
			JMenu[] extraMenu = this.rdbaConnectionTabbedPanel.getExtraMenu();
			for (JMenu menu:extraMenu) {
				menuBar.add(menu);
			}
		}
		JMenu helpMenu = new JMenu();
		ButtonsBundle.applyButtonDifinition(helpMenu, "menu_help");
		{
			JMenuItem readmeMenu = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(readmeMenu, "menu_readme");
			readmeMenu.setActionCommand("readme");
			readmeMenu.addActionListener(al);
			JMenuItem aboutMenu = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(aboutMenu, "menu_about");
			aboutMenu.setActionCommand("about");
			aboutMenu.addActionListener(al);
			JMenuItem licenseMenu = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(licenseMenu, "menu_license");
			licenseMenu.setActionCommand("license");
			licenseMenu.addActionListener(al);
			JMenuItem showLogList = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(showLogList, "menu_show_log_list");
			showLogList.setActionCommand("showLogList");
			showLogList.addActionListener(al);
			
			helpMenu.add(readmeMenu);
			helpMenu.add(licenseMenu);
			helpMenu.addSeparator();
			helpMenu.add(showLogList);
			helpMenu.addSeparator();
			helpMenu.add(aboutMenu);
			
			menuBar.add(helpMenu);
		}
	}
	
	public JMenuBar getMenuBar() {
		return this.menuBar;
	}
		
	public void saveRdbaConfig() {
		RdbaConfigUtil.saveRdbaConfig(this.rdbaConfig);
	}
	
	public void createWindowRelatedObject() {
		// 検索ダイアログ
		Frame owner = RdbaGuiUtil.getParentFrame(this);
		findConditionDialog = new FindConditionDialogEx(owner);
		this.findConditionDialog.setLocationRelativeTo(this);

		// ファイル選択ダイアログ
		TextFileChooser sqlFileChooser = new TextFileChooser(owner);
		{
			File dir = this.getRdbaConfig().getSqlQuickAccessRoot();
			if (dir != null && dir.exists() && dir.exists()) {
				sqlFileChooser.setCurrentDirectory(dir);
			}
		}
		rdbaSingletonBundle.setSqlTextFileChooser(sqlFileChooser);
		TextFileChooser scriptFileChooser = new TextFileChooser(owner);
		{
			File dir = this.getRdbaConfig().getScriptRoot();
			if (dir != null && dir.exists() && dir.isDirectory()) {
				File dir2 = new File(dir.getAbsolutePath() + File.separator + RdbaConstants.RDBA_SCRIPT_DIR);
				if (dir2.exists() && dir2.isDirectory()) {
					scriptFileChooser.setCurrentDirectory(dir2);
				}
			}
		}
		rdbaSingletonBundle.setScriptTextFileChooser(scriptFileChooser);
	}
	
	public void doGuiLayout() {
		this.rdbaConnectionTabbedPanel.doGuiLayout();
	}
	public RdbaConfig getRdbaConfig() {
		return this.rdbaConfig;
	}
	public void reloadRdbaConfig() {
		this.rdbaSingletonBundle.getScriptBundle().setWorkDir(rdbaConfig.getScriptRoot());
		this.rdbaConnectionTabbedPanel.reloadRdbaConfig();
	}
	
	public void doImportConfig() {
		if (configFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File configFile = configFileChooser.getSelectedFile();
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(configFile);
				this.rdbaConfig.loadFromXML(fin);
				this.reloadRdbaConfig();
			} catch(IOException e) {
				ExceptionHandler.warn(e);
				JOptionPane.showMessageDialog(this, e.getMessage());
			} finally {
				if (fin != null) {
					try {
						fin.close();
					} catch(IOException e2) {
						ExceptionHandler.warn(e2);
					}
				}
			}
		}
	}
	public void doExportConfig() {
		if (configFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File configFile = configFileChooser.getSelectedFile();
			if (!configFile.exists() || JOptionPane.showConfirmDialog(this, String.format(MessageBundle.getMessage("already_exists_overwrite_file"),configFile.getName()), MessageBundle.getMessage("confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				FileOutputStream fout = null;
				try {
					fout = new FileOutputStream(configFile);
					this.rdbaConfig.storeToXML(fout,"");
				} catch(IOException e) {
					ExceptionHandler.warn(e);
					JOptionPane.showMessageDialog(this, e.getMessage());
				} finally {
					if (fout != null) {
						try {
							fout.close();
						} catch(IOException e2) {
							ExceptionHandler.warn(e2);
						}
					}
				}
			}
		}
	}
	public void doExit() {
		RdbaGuiUtil.getParentFrame(this).setVisible(false);
	}
	
	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		// このパネルが一番上
		relayRdbaMessage(rdbaMessage);
	}
	public void relayRdbaMessage(RdbaMessage rdbaMessage) {
		if (rdbaMessage.getMessage().equals(RdbaMessageConstants.RDBAASSISTANTPANEL_UPDATEMENU)) {
			this.updateMenuBar();
			this.menuBar.repaint();
		} else if (rdbaMessage.getMessage().equals(RdbaMessageConstants.FINDCONDITIONDIALOG_SHOW)) {
			if (rdbaMessage.getData() != null) {
				String data = rdbaMessage.getData().toString();
				if (data.length() > 0) {
					FindCondition fc = findConditionDialog.getFindCondition();
					fc.setSearch(data);
					findConditionDialog.setFindCondition(fc);
				}
			}
			findConditionDialog.setVisible(true);
		} else if (rdbaMessage.getMessage().equals(RdbaMessageConstants.FINDCONDITIONDIALOG_DIRECT)) {
			FindCondition findCondition;
			if (rdbaMessage.getData() != null) {
				findCondition = (FindCondition)rdbaMessage.getData();
				this.findConditionDialog.setFindCondition(findCondition);
			} else {
				findCondition = this.findConditionDialog.getFindCondition();
			}
			RdbaMessage rm = new RdbaMessage(RdbaMessageConstants.FINDCONDITIONDIALOG_ACTION,null,null,findCondition);
			relayRdbaMessage(rm);
		} else if (rdbaMessage.getMessage().equals(RdbaMessageConstants.RDBASTATUSBAR_MESSAGE)) {
			Object message = rdbaMessage.getData();
			if (message != null) {
				this.stdStatusBar.setMessage(message.toString());
			}
		} else {
			this.rdbaConnectionTabbedPanel.relayRdbaMessage(rdbaMessage);
		}
	}
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return this.rdbaSingletonBundle;
	}
}
