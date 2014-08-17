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
 * $Id: FindConditionDialog.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.text;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import net.cattaka.swing.text.FindCondition.ACTION;
import net.cattaka.util.MessageBundle;

public class FindConditionDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private FindConditionPanel findConditionPanel;

	public FindConditionDialog() throws HeadlessException {
		super();
		initialize();
	}

	public FindConditionDialog(Dialog owner, boolean modal)
			throws HeadlessException {
		super(owner, modal);
		initialize();
	}

	public FindConditionDialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) throws HeadlessException {
		super(owner, title, modal, gc);
		initialize();
	}

	public FindConditionDialog(Dialog owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
		initialize();
	}

	public FindConditionDialog(Dialog owner, String title)
			throws HeadlessException {
		super(owner, title);
		initialize();
	}

	public FindConditionDialog(Dialog owner) throws HeadlessException {
		super(owner);
		initialize();
	}

	public FindConditionDialog(Frame owner, boolean modal)
			throws HeadlessException {
		super(owner, modal);
		initialize();
	}

	public FindConditionDialog(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		initialize();
	}

	public FindConditionDialog(Frame owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
		initialize();
	}

	public FindConditionDialog(Frame owner, String title)
			throws HeadlessException {
		super(owner, title);
		initialize();
	}

	public FindConditionDialog(Frame owner) throws HeadlessException {
		super(owner);
		initialize();
	}

	private void initialize() {
		setTitle(MessageBundle.getInstance().getMessage("search_replace"));
		setSize(400, 300);
		
		this.findConditionPanel = new FindConditionPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void doReplace() {
				FindCondition fc = getFindCondition();
				fc.setAction(ACTION.REPLACE);
				FindConditionDialog.this.doAction(fc);
			}

			@Override
			public void doReplaceAll() {
				FindCondition fc = getFindCondition();
				fc.setAction(ACTION.REPLACE_ALL);
				FindConditionDialog.this.doAction(fc);
			}

			@Override
			public void doReplaceSearch() {
				FindCondition fc = getFindCondition();
				fc.setAction(ACTION.REPLACE_FIND);
				FindConditionDialog.this.doAction(fc);
			}

			@Override
			public void doSearch() {
				FindCondition fc = getFindCondition();
				fc.setAction(ACTION.FIND);
				FindConditionDialog.this.doAction(fc);
			}

			@Override
			public void doClose() {
				FindConditionDialog.this.setVisible(false);
			}
		};
		getContentPane().add(this.findConditionPanel);

		// ESC押下時に閉じる処理
		AbstractAction act = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		};
		InputMap imap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-it");
		getRootPane().getActionMap().put("close-it", act);
	}
	
	public void doAction(FindCondition findCondition) {
		// 空
	}

	public FindCondition getFindCondition() {
		return findConditionPanel.getFindCondition();
	}

	public ArrayList<String> getReplaceHistory() {
		return findConditionPanel.getReplaceHistory();
	}

	public ArrayList<String> getSearchHistory() {
		return findConditionPanel.getSearchHistory();
	}

	public void setFindCondition(FindCondition findCondition) {
		findConditionPanel.setFindCondition(findCondition);
	}

	public void setReplaceHistory(ArrayList<String> replaceHistory) {
		findConditionPanel.setReplaceHistory(replaceHistory);
	}

	public void setSearchHistory(ArrayList<String> searchHistory) {
		findConditionPanel.setSearchHistory(searchHistory);
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			findConditionPanel.onShow();
		}
	}
}
