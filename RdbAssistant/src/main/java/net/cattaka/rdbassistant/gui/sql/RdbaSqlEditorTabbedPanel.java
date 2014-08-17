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
 * $Id: RdbaSqlEditorTabbedPanel.java 248 2010-01-14 14:50:18Z cattaka $
 */
package net.cattaka.rdbassistant.gui.sql;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.rdbassistant.gui.sql.RdbaSqlEditorPanel.RESULT_PANEL;
import net.cattaka.rdbassistant.util.CloseableTabbedPane;
import net.cattaka.rdbassistant.util.CloseableTabbedPaneListener;
import net.cattaka.rdbassistant.util.RdbaGuiUtil;
import net.cattaka.swing.TextFileChooser;
import net.cattaka.swing.text.FindCondition;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.MessageBundle;

public class RdbaSqlEditorTabbedPanel extends JPanel implements RdbaGuiInterface {
	private static final long serialVersionUID = 1L;

	private RdbaGuiInterface parentComponent;
	private RdbaConnection rdbConnection;
	private CloseableTabbedPane tabbedPane;

	class ButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("new_sql")) {
				addTab();
			} else if (e.getActionCommand().equals("open_sql")) {
				openSql();
			} else if (e.getActionCommand().equals("save_sql")) {
				saveSql(null);
			} else if (e.getActionCommand().equals("saveas_sql")) {
				saveAsSql(null);
			} else if (e.getActionCommand().equals("run_sql")) {
				Component comp = tabbedPane.getSelectedComponent();
				if (comp instanceof RdbaSqlEditorPanel) {
					((RdbaSqlEditorPanel)comp).runSql();
				}
			} else if (e.getActionCommand().equals("edit_find")) {
				RdbaMessage rm = new RdbaMessage(RdbaMessageConstants.FINDCONDITIONDIALOG_SHOW, null, RdbaSqlEditorTabbedPanel.this, null);
				sendRdbaMessage(rm);
			}
		}
	}
	
	class CloseableTabbedPaneListenerEx implements CloseableTabbedPaneListener {
		public boolean closeTab(int tabIndexToClose) {
			boolean result = true;
			Component comp = tabbedPane.getComponentAt(tabIndexToClose);
			if (comp instanceof RdbaSqlEditorPanel) {
				RdbaSqlEditorPanel rsep = ((RdbaSqlEditorPanel)comp); 
				result = disposeTab(rsep);
			}
			return result;
		}
	}
	
	public RdbaSqlEditorTabbedPanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		ButtonAction al = new ButtonAction();
		JToolBar toolBar = new JToolBar();
		JButton newSqlButton = new JButton();
		JButton openSqlButton = new JButton();
		JButton saveSqlButton = new JButton();
		JButton saveAsSqlButton = new JButton();
		JButton runSqlButton = new JButton();
		JButton editFindButton = new JButton();
		{
			RdbaSingletonBundle rdbaSingletonBundle = getRdbaSingletonBundle();
			Icon iconNew = (Icon)rdbaSingletonBundle.getResource(RdbaMessageConstants.ICON_NEW);
			Icon iconOpen = (Icon)rdbaSingletonBundle.getResource(RdbaMessageConstants.ICON_OPEN);
			Icon iconSave = (Icon)rdbaSingletonBundle.getResource(RdbaMessageConstants.ICON_SAVE);
			Icon iconSaveAs = (Icon)rdbaSingletonBundle.getResource(RdbaMessageConstants.ICON_SAVEAS);
			Icon iconRun = (Icon)rdbaSingletonBundle.getResource(RdbaMessageConstants.ICON_RUN);
			Icon iconFind = (Icon)rdbaSingletonBundle.getResource(RdbaMessageConstants.ICON_FIND);
			ButtonsBundle.getInstance().applyButtonDifinition(newSqlButton, iconNew, "file_new", true);
			ButtonsBundle.getInstance().applyButtonDifinition(openSqlButton, iconOpen, "file_open", true);
			ButtonsBundle.getInstance().applyButtonDifinition(saveSqlButton, iconSave, "file_save", true);
			ButtonsBundle.getInstance().applyButtonDifinition(saveAsSqlButton, iconSaveAs, "file_save_as", true);
			ButtonsBundle.getInstance().applyButtonDifinition(runSqlButton, iconRun, "run_sql", true);
			ButtonsBundle.getInstance().applyButtonDifinition(editFindButton, iconFind, "search_replace", true);
		}

		newSqlButton.addActionListener(al);
		newSqlButton.setActionCommand("new_sql");
		runSqlButton.addActionListener(al);
		runSqlButton.setActionCommand("run_sql");
		openSqlButton.addActionListener(al);
		openSqlButton.setActionCommand("open_sql");
		saveSqlButton.addActionListener(al);
		saveSqlButton.setActionCommand("save_sql");
		saveAsSqlButton.addActionListener(al);
		saveAsSqlButton.setActionCommand("saveas_sql");
		editFindButton.addActionListener(al);
		editFindButton.setActionCommand("edit_find");

		toolBar.setFloatable(false);
		toolBar.add(newSqlButton);
		toolBar.add(openSqlButton);
		toolBar.add(saveSqlButton);
		toolBar.add(saveAsSqlButton);
		toolBar.addSeparator();
		toolBar.add(runSqlButton);
		toolBar.addSeparator();
		toolBar.add(editFindButton);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		tabbedPane = new CloseableTabbedPane();
		tabbedPane.addCloseableTabbedPaneListener(new CloseableTabbedPaneListenerEx());
		//this.addTab();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbl.setConstraints(toolBar, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbl.setConstraints(tabbedPane, gbc);
		
		this.setLayout(gbl);
		this.add(toolBar);
		this.add(tabbedPane);
	}
	
	public RdbaConnection getRdbConnection() {
		return rdbConnection;
	}
	public void setRdbConnection(RdbaConnection rdbConnection) {
		this.rdbConnection = rdbConnection;
		addTab();
	}

	public RdbaSqlEditorPanel addTab() {
		int count = 0;
		String name = "t";
		outer:while (true) {
			count++;
			name = String.format("new%02d.sql", count);
			for (int i=0;i<tabbedPane.getTabCount();i++) {
				if (name.equals(tabbedPane.getTitleAt(i))) {
					continue outer;
				}
			}
			break;
		}
		
		RdbaSqlEditorPanel rdbaSqlEditorPanel = new RdbaSqlEditorPanel(this);
		rdbaSqlEditorPanel.setRdbConnection(rdbConnection);
		tabbedPane.addTab(name, rdbaSqlEditorPanel);
		tabbedPane.setSelectedComponent(rdbaSqlEditorPanel);
		rdbaSqlEditorPanel.reloadRdbaConfig();
		RdbaGuiUtil.doLayout(rdbaSqlEditorPanel);
		
		return rdbaSqlEditorPanel;
	}
	
	/**
	 * カーソル位置に文字列を挿入する。
	 * もしカーソル位置が空白でなければ、カンマで区切った後に挿入する。
	 */
	public void appendString(String str) {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.appendString(str);
		}
	}
	
	public RdbaSqlEditorPanel getCurrentRdbaSqlEditorPanel() {
		Component comp = tabbedPane.getSelectedComponent();
		if (comp != null && comp instanceof RdbaSqlEditorPanel) {
			return ((RdbaSqlEditorPanel)comp);
		} else {
			return null;
		}
	}
	
	private int getTabIndex(RdbaSqlEditorPanel target) {
		for (int i=0;i<this.tabbedPane.getTabCount();i++) {
			if (this.tabbedPane.getComponentAt(i) == target) {
				return i;
			}
		}
		return -1;
	}
	
	private void setTabTitle(RdbaSqlEditorPanel target, String title) {
		int index = getTabIndex(target);
		if (index != -1) {
			this.tabbedPane.setTitleAt(index, title);
		}
	}
	
	public boolean openSql() {
		boolean result = false;
		TextFileChooser fileChooser = getRdbaSingletonBundle().getSqlTextFileChooser();
		fileChooser.setLocationRelativeTo(this);
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Charset charset = fileChooser.getCharset();
			result = openSql(file, charset);
		}
		return result;
	}
	public boolean openSql(File file, Charset charset) {
		boolean result = false;
		boolean existFlag = false;
		// 既に開いている場合はそれを表示
		for (int i=0;i<this.tabbedPane.getTabCount();i++) {
			Component comp = this.tabbedPane.getComponent(i);
			if (comp instanceof RdbaSqlEditorPanel) {
				RdbaSqlEditorPanel rsep = (RdbaSqlEditorPanel)comp;
				if (rsep.getFile() != null && rsep.getFile().equals(file)) {
					this.tabbedPane.setSelectedComponent(comp);
					existFlag = true;
					break;
				}
			}
		}
		
		if (!existFlag) {
			RdbaSqlEditorPanel rsep = addTab();
			result = rsep.loadSql(file, charset);
			setTabTitle(rsep, file.getName());
		}
		return result;
	}
	
	public boolean saveSql(RdbaSqlEditorPanel rsep) {
		boolean result = false;
		if (rsep == null) {
			rsep = getCurrentRdbaSqlEditorPanel();
		}
		if (rsep != null) {
			if (rsep.getFile() != null && rsep.getCharset() != null) {
				result = rsep.saveSql(rsep.getFile(), rsep.getCharset());
				File file = rsep.getFile();
				setTabTitle(rsep, file.getName());
			} else {
				result = saveAsSql(rsep);
			}
		}
		return result;
	}
	
	public boolean saveAsSql(RdbaSqlEditorPanel rsep) {
		boolean result = false;
		if (rsep == null) {
			rsep = getCurrentRdbaSqlEditorPanel();
		}
		if (rsep != null) {
			TextFileChooser fileChooser = getRdbaSingletonBundle().getSqlTextFileChooser();
			fileChooser.setLocationRelativeTo(this);
			if (rsep.getFile() != null) {
				fileChooser.setSelectedFile(rsep.getFile());
			}
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				Charset charset = fileChooser.getCharset();
				result = rsep.saveSql(file, charset);
				setTabTitle(rsep, file.getName());
			}
		}
		return result;
	}
	public boolean closeTab() {
		boolean result = false;
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null && disposeTab(rsep)) {
			this.tabbedPane.removeTabAt(getTabIndex(rsep));
			result = true;
		}
		return result;
	}

	public void runSql() {
		Component comp = tabbedPane.getSelectedComponent();
		if (comp instanceof RdbaSqlEditorPanel) {
			((RdbaSqlEditorPanel)comp).runSql();
		}
	}
	public void nextSql() {
		int idx = this.tabbedPane.getSelectedIndex() + 1;
		if (0 <= idx && idx < this.tabbedPane.getTabCount()) {
			this.tabbedPane.setSelectedIndex(idx);
		}
	}		
	public void prevSql() {
		int idx = this.tabbedPane.getSelectedIndex() - 1;
		if (0 <= idx && idx < this.tabbedPane.getTabCount()) {
			this.tabbedPane.setSelectedIndex(idx);
		}
	}
	
	public boolean disposeTab(RdbaSqlEditorPanel rsep) {
		boolean result = false;
		if (rsep == null) {
			rsep = getCurrentRdbaSqlEditorPanel();
		}
		if (rsep != null) {
			if (rsep.isModified()) {
				int ans = JOptionPane.showConfirmDialog(this, MessageBundle.getMessage("save_the_changes_to_document"), MessageBundle.getMessage("confirm"), JOptionPane.YES_NO_CANCEL_OPTION);
				if (ans == JOptionPane.YES_OPTION) {
					result = saveSql(rsep);
				} else if (ans == JOptionPane.NO_OPTION) {
					result = true;
				}
			} else {
				result = true;
			}
			if (result) {
				rsep.dispose();
			}
		}
		return result;
	}
	
	// ===========================================================================
	public void doGuiLayout() {
		for (int i=0;i<tabbedPane.getComponentCount();i++) {
			Component comp = tabbedPane.getComponentAt(i);
			if (comp instanceof RdbaGuiInterface) {
				((RdbaGuiInterface)comp).doGuiLayout();
			}
		}
	}
	public RdbaConfig getRdbaConfig() {
		return this.parentComponent.getRdbaConfig();
	}
	
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return this.parentComponent.getRdbaSingletonBundle();
	}
	
	public void relayRdbaMessage(RdbaMessage rdbaMessage) {
		if (rdbaMessage.getTarget() == null) {
			// オーナー無しのメッセージの場合
			if (rdbaMessage.getMessage().equals(RdbaMessageConstants.FINDCONDITIONDIALOG_ACTION)) {
				// 検索処理：フォーカスされているタブに対して検索
				RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
				if (rsep != null) {
					FindCondition findCondition = (FindCondition)rdbaMessage.getData();
					rsep.doFindAction(findCondition);
				}
			}
		} else if (rdbaMessage.getTarget() == this) {
//			if (rdbaMessage.getMessage().equals(RdbaMessageConstants.RDBASQLEDITORPANEL_PREVDOC)) {
//				// Swingが拾ってくれないので仕方なく
//				int idx = this.tabbedPane.getSelectedIndex() - 1;
//				if (0 <= idx && idx < this.tabbedPane.getTabCount()) {
//					this.tabbedPane.setSelectedIndex(idx);
//				}
//			} else if (rdbaMessage.getMessage().equals(RdbaMessageConstants.RDBASQLEDITORPANEL_NEXTDOC)) {
//				// Swingが拾ってくれないので仕方なく
//				int idx = this.tabbedPane.getSelectedIndex() + 1;
//				if (0 <= idx && idx < this.tabbedPane.getTabCount()) {
//					this.tabbedPane.setSelectedIndex(idx);
//				}
//			} else {
				RdbaSqlEditorPanel rsep = null;
				// 指定されたエディタを取得する。無ければ現在選択中の物
				{
					if (rdbaMessage.getOwner() != null && rdbaMessage.getOwner() instanceof RdbaSqlEditorPanel) {
						rsep = (RdbaSqlEditorPanel)rdbaMessage.getOwner();
					}
					if (rsep == null || this.getTabIndex(rsep) == -1) {
						rsep = getCurrentRdbaSqlEditorPanel();
					}
				}
				int tabIndex = this.tabbedPane.indexOfComponent(rsep);
				
				if (rdbaMessage.getMessage().equals(RdbaMessageConstants.RDBASQLEDITORPANEL_MODIFIED)) {
					if (tabIndex != -1) {
						String newTitle = "*"+this.tabbedPane.getTitleAt(tabIndex);
						this.tabbedPane.setTitleAt(tabIndex, newTitle);
					}
				}
//			}
		} else {
			// その他
			Component comp = tabbedPane.getSelectedComponent();
			if (comp instanceof RdbaGuiInterface) {
				((RdbaGuiInterface)comp).relayRdbaMessage(rdbaMessage);
			}
		}
	}
	
	public void reloadRdbaConfig() {
		for (int i=0;i<tabbedPane.getComponentCount();i++) {
			Component comp = tabbedPane.getComponentAt(i);
			if (comp instanceof RdbaGuiInterface) {
				((RdbaGuiInterface)comp).reloadRdbaConfig();
			}
		}
	}
	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		// 子コンポーネントからの特定のメッセージはその場で処理する
		this.parentComponent.sendRdbaMessage(rdbaMessage);
	}

	/** メニューイベント用 */
	public void openFindDialog() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.openFindDialog();
		}
	}
	/** メニューイベント用 */
	public void doCommentOut() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.doCommentOut();
		}
	}
	/** メニューイベント用 */
	public boolean canRedo() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			return rsep.canRedo();
		} else {
			return false;
		}
	}
	/** メニューイベント用 */
	public boolean canUndo() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			return rsep.canUndo();
		} else {
			return false;
		}
	}
	/** メニューイベント用 */
	public void redo() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.redo();
		}
	}
	/** メニューイベント用 */
	public void undo() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.undo();
		}
	}
	/** メニューイベント用 */
	public void copy() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.copy();
		}
	}
	/** メニューイベント用 */
	public void cut() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.cut();
		}
	}
	/** メニューイベント用 */
	public void paste() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.paste();
		}
	}
	/** メニューイベント用 */
	public void selectAll() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.selectAll();
		}
	}
	/** メニューイベント用 */
	public void searchNext() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.searchNext();
		}
	}
	/** メニューイベント用 */
	public void searchPrev() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.searchPrev();
		}
	}
	/** メニューイベント用 */
	public void switchResultPanel() {
		RdbaSqlEditorPanel rsep = getCurrentRdbaSqlEditorPanel();
		if (rsep != null) {
			rsep.switchResultPanel(RESULT_PANEL.RESULT_PANEL_ALTERNATE);
		}
	}
}
