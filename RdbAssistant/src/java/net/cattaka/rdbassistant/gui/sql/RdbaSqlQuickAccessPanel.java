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
 * $Id: RdbaSqlQuickAccessPanel.java 275 2010-03-02 13:42:49Z cattaka $
 */
package net.cattaka.rdbassistant.gui.sql;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.rdbassistant.util.RdbaGuiUtil;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.fileview.FileViewTree;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class RdbaSqlQuickAccessPanel extends JPanel implements RdbaGuiInterface {
	private static final long serialVersionUID = 1L;
	private RdbaGuiInterface parentComponent;
	private FileViewTreeEx fileViewTree;
	private RdbaSqlEditorTabbedPanel rdbaSqlEditorTabbedPanel;
	
	class FileViewTreeEx extends FileViewTree {
		private static final long serialVersionUID = 1L;

		public FileViewTreeEx() {
			super();
		}

		@Override
		public void doOpen(File file) {
			boolean goFlag = false;
			if (!file.getName().toUpperCase().endsWith(".SQL")) {
				if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(RdbaGuiUtil.getParentFrame(RdbaSqlQuickAccessPanel.this), MessageBundle.getMessage("this_is_not_sql_open_really"), MessageBundle.getMessage("confirm"), JOptionPane.YES_NO_OPTION)) {
					goFlag = true;
				}
			} else {
				goFlag = true;
			}
			if (goFlag) {
				rdbaSqlEditorTabbedPanel.openSql(file, RdbaConstants.DEFAULT_CHAR_SET);
			}
		}
		
		public void refleshTarget(File target) {
			FileTreeNode rootNode = (FileTreeNode)getModel().getRoot();
			refleshTarget(target, rootNode);
		}
		private void refleshTarget(File target, FileTreeNode node) {
			File nodeFile = node.getFile();
			try {
				String targetCanonicalPath = target.getCanonicalPath();
				String nodeCanonicalPath = nodeFile.getCanonicalPath();
				if (targetCanonicalPath.indexOf(nodeCanonicalPath) == 0) {
					if (targetCanonicalPath.length() == nodeCanonicalPath.length()) {
						// 更新対象を特定した
						node.reflesh();
					} else {
						// 子供を探しに行く
				 		FileTreeNode[] children = node.getChildren();
				 		for (FileTreeNode child : children) {
				 			refleshTarget(target, child);
				 		}
				 	}
				}
			} catch(IOException e) {
				ExceptionHandler.error(e);
			}
		}
	}
	
	public RdbaSqlQuickAccessPanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		this.fileViewTree = new FileViewTreeEx();
		StdScrollPane fileViewTreePane;
		fileViewTreePane = new StdScrollPane(this.fileViewTree);
		
		this.setLayout(new GridLayout());
		this.add(fileViewTreePane);
	}

	/**
	 * ファイル選択時に開く先を設定。
	 * @param rdbaSqlEditorPanel
	 */
	public void setRdbaSqlEditorTabbedPanel(RdbaSqlEditorTabbedPanel rdbaSqlEditorPanel) {
		this.rdbaSqlEditorTabbedPanel = rdbaSqlEditorPanel;
	}

	/** {@link RdbaGuiInterface} */
	public void doGuiLayout() {
		// 無し
	}

	/** {@link RdbaGuiInterface} */
	public RdbaConfig getRdbaConfig() {
		return parentComponent.getRdbaConfig();
	}

	/** {@link RdbaGuiInterface} */
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return parentComponent.getRdbaSingletonBundle();
	}

	/** {@link RdbaGuiInterface} */
	public void relayRdbaMessage(RdbaMessage rdbaMessage) {
		// ファイルが更新された場合はそれに関係するディレクトリをリフレッシュする
		if (rdbaMessage.getMessage().equals(RdbaMessageConstants.RDBASQLQUICKACCESSPANEL_REFLESH)) {
			Object obj = rdbaMessage.getData();
			if (obj != null && obj instanceof File) {
				this.fileViewTree.refleshTarget((File)obj);
			}
		}
	}

	/** {@link RdbaGuiInterface} */
	public void reloadRdbaConfig() {
		this.fileViewTree.setRootDirectory(getRdbaConfig().getSqlQuickAccessRoot());
	}

	/** {@link RdbaGuiInterface} */
	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		parentComponent.sendRdbaMessage(rdbaMessage);
	}
}
