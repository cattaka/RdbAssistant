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
 * $Id: RdbaScriptAccessPanel.java 275 2010-03-02 13:42:49Z cattaka $
 */
package net.cattaka.rdbassistant.gui.script;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.fileview.FileViewTree;
import net.cattaka.util.ExceptionHandler;

public class RdbaScriptAccessPanel extends JPanel implements RdbaGuiInterface {
	private static final long serialVersionUID = 1L;
	private RdbaGuiInterface parentComponent;
	private FileViewTreeEx fileViewTree;
	private RdbaScriptEditorTabbedPanel rdbaScriptEditorTabbedPanel;
	
	class FileViewTreeEx extends FileViewTree {
		private static final long serialVersionUID = 1L;

		public FileViewTreeEx() {
			super();
		}

		@Override
		public void doOpen(File file) {
			boolean goFlag = false;
			goFlag = true;
			if (goFlag) {
				rdbaScriptEditorTabbedPanel.openScript(file, RdbaConstants.DEFAULT_CHAR_SET);
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
	
	public RdbaScriptAccessPanel(RdbaGuiInterface parentComponent) {
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
	public void setRdbaScriptEditorTabbedPanel(RdbaScriptEditorTabbedPanel rdbaScriptEditorPanel) {
		this.rdbaScriptEditorTabbedPanel = rdbaScriptEditorPanel;
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
		if (rdbaMessage.getMessage().equals(RdbaMessageConstants.RDBASCRIPTACCESSPANEL_REFLESH)) {
			Object obj = rdbaMessage.getData();
			if (obj != null && obj instanceof File) {
				this.fileViewTree.refleshTarget((File)obj);
			}
		}
	}

	/** {@link RdbaGuiInterface} */
	public void reloadRdbaConfig() {
		if (getRdbaSingletonBundle().getScriptBundle().isAvailable()) {
			File workDir = getRdbaSingletonBundle().getScriptBundle().getWorkDir();
			File sourceDir = new File(workDir.getAbsolutePath() + File.separatorChar + RdbaConstants.RDBA_SCRIPT_DIR);
			if (sourceDir.exists()) {
				if (!sourceDir.isDirectory()) {
					sourceDir.delete();
				}
			} else {
				sourceDir.mkdir();
			}
			this.fileViewTree.setRootDirectory(sourceDir);
		} else {
			this.fileViewTree.setRootDirectory(null);
		}
	}

	/** {@link RdbaGuiInterface} */
	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		parentComponent.sendRdbaMessage(rdbaMessage);
	}
}
