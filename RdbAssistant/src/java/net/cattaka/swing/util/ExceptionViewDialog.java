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
 * $Id: ExceptionViewDialog.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.util;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import net.cattaka.swing.StdScrollPane;
import net.cattaka.util.MessageBundle;

public class ExceptionViewDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private ExceptionListTableModel exceptionListTableModel;
	private JTextArea textArea;
	private JTable table;
	private JSplitPane splitPane;
	
	public ExceptionViewDialog(ExceptionListTableModel exceptionListTableModel) {
		super();
		this.exceptionListTableModel = exceptionListTableModel;
		makeLayout();
	}
	
	public ExceptionViewDialog(Frame owner, ExceptionListTableModel exceptionListTableModel) {
		super(owner);
		this.exceptionListTableModel = exceptionListTableModel;
		makeLayout();
	}
	
	private void makeLayout() {
		this.setTitle(MessageBundle.getMessage("log_list"));
		this.setSize(600,500);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		table = new JTable() {
			private static final long serialVersionUID = 1L;

			@Override
			public void columnSelectionChanged(ListSelectionEvent e) {
				int row = this.getSelectedRow();
				int col = this.getSelectedColumn();
				if (row != -1 && col != -1) {
					Object item = table.getValueAt(row, col);
					if (item != null) {
						textArea.setText(item.toString());
						textArea.setCaretPosition(0);
					}
				}
				super.columnSelectionChanged(e);
			}
		};
		table.setColumnSelectionAllowed(true);
		table.setModel(exceptionListTableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		StdScrollPane textAreaPane = new StdScrollPane(textArea);
		StdScrollPane tablePane = new StdScrollPane(table);
		
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePane, textAreaPane);
		getContentPane().add(splitPane);
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			splitPane.setDividerLocation(0.5);
		}
	}
}
