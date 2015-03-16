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
 * $Id: RdbaSqlModePanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.sql;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.rdbassistant.gui.RdbaModeInterface;
import net.cattaka.swing.util.ButtonsBundle;

public class RdbaSqlModePanel extends JPanel implements RdbaGuiInterface, RdbaModeInterface {
	private static final long serialVersionUID = 1L;

	private JSplitPane splitPane;
	private RdbaSqlAssistTabbedPanel rdbaSqlAssistTabbedPanel;
	private RdbaSqlEditorTabbedPanel rdbSqlEditorPanel;
	private RdbaGuiInterface parentComponent;
	private RdbaConnection rdbaConnection;

	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("new_sql")) {
				rdbSqlEditorPanel.addTab();
			} else if (e.getActionCommand().equals("open_sql")) {
				rdbSqlEditorPanel.openSql();
			} else if (e.getActionCommand().equals("save_sql")) {
				rdbSqlEditorPanel.saveSql(null);
			} else if (e.getActionCommand().equals("saveAs_sql")) {
				rdbSqlEditorPanel.saveAsSql(null);
			} else if (e.getActionCommand().equals("close_sql")) {
				rdbSqlEditorPanel.closeTab();
			} else if (e.getActionCommand().equals("editor_comment_out")) {
				rdbSqlEditorPanel.doCommentOut();
			} else if (e.getActionCommand().equals("editor_undo")) {
				rdbSqlEditorPanel.undo();
			} else if (e.getActionCommand().equals("editor_redo")) {
				rdbSqlEditorPanel.redo();
			} else if (e.getActionCommand().equals("editor_cut")) {
				rdbSqlEditorPanel.cut();
			} else if (e.getActionCommand().equals("editor_copy")) {
				rdbSqlEditorPanel.copy();
			} else if (e.getActionCommand().equals("editor_paste")) {
				rdbSqlEditorPanel.paste();
			} else if (e.getActionCommand().equals("editor_select_all")) {
				rdbSqlEditorPanel.selectAll();
			} else if (e.getActionCommand().equals("editor_find")) {
				rdbSqlEditorPanel.openFindDialog();
			} else if (e.getActionCommand().equals("editor_find_next")) {
				rdbSqlEditorPanel.searchNext();
			} else if (e.getActionCommand().equals("editor_find_prev")) {
				rdbSqlEditorPanel.searchPrev();
			} else if (e.getActionCommand().equals("run_sql")) {
				rdbSqlEditorPanel.runSql();
			} else if (e.getActionCommand().equals("run_commit")) {
				rdbSqlEditorPanel.runSql("commit");
			} else if (e.getActionCommand().equals("run_rollback")) {
				rdbSqlEditorPanel.runSql("rollback");
			} else if (e.getActionCommand().equals("next_sql")) {
				rdbSqlEditorPanel.nextSql();
			} else if (e.getActionCommand().equals("prev_sql")) {
				rdbSqlEditorPanel.prevSql();
			} else if (e.getActionCommand().equals("switch_result_panel")) {
				rdbSqlEditorPanel.switchResultPanel();
			}
		}
	}
	
	public RdbaSqlModePanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	private void makeLayout() {
		rdbaSqlAssistTabbedPanel = new RdbaSqlAssistTabbedPanel(this.parentComponent);
		rdbSqlEditorPanel = new RdbaSqlEditorTabbedPanel(this.parentComponent);
		rdbaSqlAssistTabbedPanel.setMinimumSize(new Dimension(0,0));
		rdbSqlEditorPanel.setMinimumSize(new Dimension(0,0));
		rdbaSqlAssistTabbedPanel.setRdbaSqlEditorTabbedPanel(rdbSqlEditorPanel);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rdbaSqlAssistTabbedPanel, rdbSqlEditorPanel);
		this.setLayout(new GridLayout());
		this.add(splitPane);
	}

	public void doGuiLayout() {
		this.splitPane.setDividerLocation(0.4);
		this.rdbSqlEditorPanel.doGuiLayout();
		this.rdbaSqlAssistTabbedPanel.doGuiLayout();
	}

	public RdbaConnection getRdbConnection() {
		return rdbaConnection;
	}
	
	public void reloadRdbaConfig() {
		this.rdbSqlEditorPanel.reloadRdbaConfig();
		this.rdbaSqlAssistTabbedPanel.reloadRdbaConfig();
	}
	
	public void setRdbConnection(RdbaConnection rdbaConnection) {
		this.rdbaSqlAssistTabbedPanel.setRdbaConnection(rdbaConnection);
		this.rdbSqlEditorPanel.setRdbConnection(rdbaConnection);
		this.rdbaConnection = rdbaConnection;
	}

	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		this.parentComponent.sendRdbaMessage(rdbaMessage);
	}
	public void relayRdbaMessage(RdbaMessage rbdaMessage) {
		this.rdbaSqlAssistTabbedPanel.relayRdbaMessage(rbdaMessage);
		this.rdbSqlEditorPanel.relayRdbaMessage(rbdaMessage);
	}
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return this.parentComponent.getRdbaSingletonBundle();
	}

	public RdbaConfig getRdbaConfig() {
		return this.parentComponent.getRdbaConfig();
	}
	
	/** {@link RdbaModeInterface} */
	public JMenu[] getExtraMenu() {
		ActionListenerImpl al = new ActionListenerImpl();
		JMenu sqlMenu = new JMenu();
		ButtonsBundle.getInstance().applyButtonDifinition(sqlMenu, "menu_sql");
		{
			JMenuItem runSqlItem = new JMenuItem();
			JMenuItem runCommitItem = new JMenuItem();
			JMenuItem runRollbackItem = new JMenuItem();
			JMenuItem switchResultPanelItem = new JMenuItem();
			JMenuItem commentOutItem = new JMenuItem();
			runSqlItem.setActionCommand("run_sql");
			runCommitItem.setActionCommand("run_commit");
			runRollbackItem.setActionCommand("run_rollback");
			switchResultPanelItem.setActionCommand("switch_result_panel");
			commentOutItem.setActionCommand("editor_comment_out");
			
			runSqlItem.addActionListener(al);
			runCommitItem.addActionListener(al);
			runRollbackItem.addActionListener(al);
			switchResultPanelItem.addActionListener(al);
			commentOutItem.addActionListener(al);
			
			ButtonsBundle.getInstance().applyMenuDifinition(runSqlItem, "run_sql");
			ButtonsBundle.getInstance().applyMenuDifinition(runCommitItem, "run_commit");
			ButtonsBundle.getInstance().applyMenuDifinition(runRollbackItem, "run_rollback");
			ButtonsBundle.getInstance().applyMenuDifinition(switchResultPanelItem, "switch_result_panel");
			ButtonsBundle.getInstance().applyMenuDifinition(commentOutItem, "comment_out");
			
			sqlMenu.add(runSqlItem);
			sqlMenu.add(switchResultPanelItem);
			sqlMenu.addSeparator();
			sqlMenu.add(commentOutItem);
			sqlMenu.addSeparator();
			sqlMenu.add(runRollbackItem);
			sqlMenu.add(runCommitItem);
		}
		return new JMenu[]{sqlMenu};
	}
	/** {@link RdbaModeInterface} */
	public boolean updateMenu(TargetMenu targetMenu, JMenu menu) {
		boolean result = false;
		ActionListenerImpl al = new ActionListenerImpl();
		if (targetMenu == TargetMenu.FILE_MENU) {
			JMenuItem newItem = new JMenuItem();
			JMenuItem openItem = new JMenuItem();
			JMenuItem saveItem = new JMenuItem();
			JMenuItem saveAsItem = new JMenuItem();
			JMenuItem closeItem = new JMenuItem();
			newItem.setActionCommand("new_sql");
			openItem.setActionCommand("open_sql");
			saveItem.setActionCommand("save_sql");
			saveAsItem.setActionCommand("saveAs_sql");
			closeItem.setActionCommand("close_sql");
			
			newItem.addActionListener(al);
			openItem.addActionListener(al);
			saveItem.addActionListener(al);
			saveAsItem.addActionListener(al);
			closeItem.addActionListener(al);
			
			ButtonsBundle.getInstance().applyMenuDifinition(newItem, "file_new");
			ButtonsBundle.getInstance().applyMenuDifinition(openItem, "file_open");
			ButtonsBundle.getInstance().applyMenuDifinition(saveItem, "file_save");
			ButtonsBundle.getInstance().applyMenuDifinition(saveAsItem, "file_save_as");
			ButtonsBundle.getInstance().applyMenuDifinition(closeItem, "file_close");
			
			menu.add(newItem);
			menu.add(openItem);
			menu.add(saveItem);
			menu.add(saveAsItem);
			menu.add(closeItem);
			result = true;
		} else if (targetMenu == TargetMenu.EDIT_MENU) {
			JMenuItem undoItem = new JMenuItem();
			JMenuItem redoItem = new JMenuItem();
			JMenuItem cutItem = new JMenuItem();
			JMenuItem copyItem = new JMenuItem();
			JMenuItem pasteItem = new JMenuItem();
			JMenuItem selectAllItem = new JMenuItem();
			JMenuItem findItem = new JMenuItem();
			JMenuItem findNextItem = new JMenuItem();
			JMenuItem findPrevItem = new JMenuItem();
			undoItem.setActionCommand("editor_undo");
			redoItem.setActionCommand("editor_redo");
			cutItem.setActionCommand("editor_cut");
			copyItem.setActionCommand("editor_copy");
			pasteItem.setActionCommand("editor_paste");
			selectAllItem.setActionCommand("editor_select_all");
			findItem.setActionCommand("editor_find");
			findNextItem.setActionCommand("editor_find_next");
			findPrevItem.setActionCommand("editor_find_prev");
			undoItem.addActionListener(al);
			redoItem.addActionListener(al);
			cutItem.addActionListener(al);
			copyItem.addActionListener(al);
			pasteItem.addActionListener(al);
			selectAllItem.addActionListener(al);
			findItem.addActionListener(al);
			findNextItem.addActionListener(al);
			findPrevItem.addActionListener(al);
			ButtonsBundle.getInstance().applyMenuDifinition(undoItem, "editor_undo");
			ButtonsBundle.getInstance().applyMenuDifinition(redoItem, "editor_redo");
			ButtonsBundle.getInstance().applyMenuDifinition(cutItem, "editor_cut");
			ButtonsBundle.getInstance().applyMenuDifinition(copyItem, "editor_copy");
			ButtonsBundle.getInstance().applyMenuDifinition(pasteItem, "editor_paste");
			ButtonsBundle.getInstance().applyMenuDifinition(selectAllItem, "editor_select_all");
			ButtonsBundle.getInstance().applyMenuDifinition(findItem, "search_replace");
			ButtonsBundle.getInstance().applyMenuDifinition(findNextItem, "search_next");
			ButtonsBundle.getInstance().applyMenuDifinition(findPrevItem, "search_prev");
			
			menu.add(undoItem);
			menu.add(redoItem);
			menu.addSeparator();
			menu.add(cutItem);
			menu.add(copyItem);
			menu.add(pasteItem);
			menu.addSeparator();
			menu.add(selectAllItem);
			menu.addSeparator();
			menu.add(findItem);
			menu.add(findNextItem);
			menu.add(findPrevItem);
			result = true;
		}
		return result;
	}
}
