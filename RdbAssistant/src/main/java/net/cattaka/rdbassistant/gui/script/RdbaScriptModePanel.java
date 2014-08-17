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
 * $Id: RdbaScriptModePanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.script;

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
import net.cattaka.rdbassistant.gui.script.RdbaScriptAssistTabbedPanel;
import net.cattaka.rdbassistant.gui.script.RdbaScriptEditorTabbedPanel;
import net.cattaka.swing.util.ButtonsBundle;

public class RdbaScriptModePanel extends JPanel implements RdbaGuiInterface, RdbaModeInterface {
	private static final long serialVersionUID = 1L;

	private JSplitPane splitPane;
	private RdbaScriptAssistTabbedPanel rdbaScriptAssistTabbedPanel;
	private RdbaScriptEditorTabbedPanel rdbScriptEditorPanel;
	private RdbaGuiInterface parentComponent;
	private RdbaConnection rdbaConnection;

	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("new_script")) {
				rdbScriptEditorPanel.addTab();
			} else if (e.getActionCommand().equals("open_script")) {
				rdbScriptEditorPanel.openScript();
			} else if (e.getActionCommand().equals("save_script")) {
				rdbScriptEditorPanel.saveScript(null);
			} else if (e.getActionCommand().equals("saveAs_script")) {
				rdbScriptEditorPanel.saveAsScript(null);
			} else if (e.getActionCommand().equals("close_script")) {
				rdbScriptEditorPanel.closeTab();
			} else if (e.getActionCommand().equals("editor_comment_out")) {
				rdbScriptEditorPanel.doCommentOut();
			} else if (e.getActionCommand().equals("editor_undo")) {
				rdbScriptEditorPanel.undo();
			} else if (e.getActionCommand().equals("editor_redo")) {
				rdbScriptEditorPanel.redo();
			} else if (e.getActionCommand().equals("editor_cut")) {
				rdbScriptEditorPanel.cut();
			} else if (e.getActionCommand().equals("editor_copy")) {
				rdbScriptEditorPanel.copy();
			} else if (e.getActionCommand().equals("editor_paste")) {
				rdbScriptEditorPanel.paste();
			} else if (e.getActionCommand().equals("editor_select_all")) {
				rdbScriptEditorPanel.selectAll();
			} else if (e.getActionCommand().equals("editor_find")) {
				rdbScriptEditorPanel.openFindDialog();
			} else if (e.getActionCommand().equals("editor_find_next")) {
				rdbScriptEditorPanel.searchNext();
			} else if (e.getActionCommand().equals("editor_find_prev")) {
				rdbScriptEditorPanel.searchPrev();
			} else if (e.getActionCommand().equals("run_script")) {
				rdbScriptEditorPanel.runScript();
			} else if (e.getActionCommand().equals("compile_script")) {
				rdbScriptEditorPanel.compileScript();
			} else if (e.getActionCommand().equals("next_script")) {
				rdbScriptEditorPanel.nextScript();
			} else if (e.getActionCommand().equals("prev_script")) {
				rdbScriptEditorPanel.prevScript();
			} else if (e.getActionCommand().equals("switch_result_panel")) {
				rdbScriptEditorPanel.switchResultPanel();
			}
		}
	}
	
	public RdbaScriptModePanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	private void makeLayout() {
		rdbaScriptAssistTabbedPanel = new RdbaScriptAssistTabbedPanel(this.parentComponent);
		rdbScriptEditorPanel = new RdbaScriptEditorTabbedPanel(this.parentComponent);
		rdbaScriptAssistTabbedPanel.setMinimumSize(new Dimension(0,0));
		rdbScriptEditorPanel.setMinimumSize(new Dimension(0,0));
		rdbaScriptAssistTabbedPanel.setRdbaScriptEditorTabbedPanel(rdbScriptEditorPanel);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rdbaScriptAssistTabbedPanel, rdbScriptEditorPanel);
		this.setLayout(new GridLayout());
		this.add(splitPane);
	}

	public void doGuiLayout() {
		this.splitPane.setDividerLocation(0.4);
		this.rdbScriptEditorPanel.doGuiLayout();
		this.rdbaScriptAssistTabbedPanel.doGuiLayout();
	}

	public RdbaConnection getRdbConnection() {
		return rdbaConnection;
	}
	
	public void reloadRdbaConfig() {
		this.rdbScriptEditorPanel.reloadRdbaConfig();
		this.rdbaScriptAssistTabbedPanel.reloadRdbaConfig();
	}
	
	public void setRdbConnection(RdbaConnection rdbaConnection) {
		this.rdbaScriptAssistTabbedPanel.setRdbaConnection(rdbaConnection);
		this.rdbScriptEditorPanel.setRdbConnection(rdbaConnection);
		this.rdbaConnection = rdbaConnection;
	}

	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		this.parentComponent.sendRdbaMessage(rdbaMessage);
	}
	public void relayRdbaMessage(RdbaMessage rbdaMessage) {
		this.rdbaScriptAssistTabbedPanel.relayRdbaMessage(rbdaMessage);
		this.rdbScriptEditorPanel.relayRdbaMessage(rbdaMessage);
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
		JMenu scriptMenu = new JMenu();
		ButtonsBundle.getInstance().applyButtonDifinition(scriptMenu, "menu_script");
		{
			JMenuItem runScriptItem = new JMenuItem();
			JMenuItem compileScriptItem = new JMenuItem();
			JMenuItem switchResultPanelItem = new JMenuItem();
			JMenuItem commentOutItem = new JMenuItem();
			runScriptItem.setActionCommand("run_script");
			compileScriptItem.setActionCommand("compile_script");
			switchResultPanelItem.setActionCommand("switch_result_panel");
			commentOutItem.setActionCommand("editor_comment_out");
			
			runScriptItem.addActionListener(al);
			compileScriptItem.addActionListener(al);
			switchResultPanelItem.addActionListener(al);
			commentOutItem.addActionListener(al);
			
			ButtonsBundle.getInstance().applyMenuDifinition(runScriptItem, "run_script");
			ButtonsBundle.getInstance().applyMenuDifinition(compileScriptItem, "compile_script");
			ButtonsBundle.getInstance().applyMenuDifinition(switchResultPanelItem, "switch_result_panel");
			ButtonsBundle.getInstance().applyMenuDifinition(commentOutItem, "comment_out");
			
			scriptMenu.add(runScriptItem);
			scriptMenu.add(compileScriptItem);
			scriptMenu.add(switchResultPanelItem);
			scriptMenu.addSeparator();
			scriptMenu.add(commentOutItem);
		}
		return new JMenu[]{scriptMenu};
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
			newItem.setActionCommand("new_script");
			openItem.setActionCommand("open_script");
			saveItem.setActionCommand("save_script");
			saveAsItem.setActionCommand("saveAs_script");
			closeItem.setActionCommand("close_script");
			
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
