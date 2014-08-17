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
 * $Id: JPopupMenuForColumnList.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.gui.RdbaTextInterface;
import net.cattaka.swing.util.ButtonsBundle;

public class JPopupMenuForColumnList extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	private JTableForDisplay table;
	private RdbaTextInterface rdbaTextInterface;
	private JMenuItem inputSelectedNameItem;
	
	class ActionListenerForColumnList implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("input_selected_name")) {
				int[] rows = table.getSelectedRows();
				int[] cols = table.getSelectedColumns();
				for (int i=0;i<rows.length;i++) {
					for (int j=0;j<cols.length;j++) {
						Object value = table.getFilteredString(rows[i],cols[j]);
						if (value == null) {
							rdbaTextInterface.appendString(RdbaConstants.DEFAULT_NULL_STRING);
						} else {
							rdbaTextInterface.appendString(value.toString());
						}
					}
				}
			} else if (e.getActionCommand().equals("copy_as_csv")) {
				table.copyAsCsv(false);
			} else if (e.getActionCommand().equals("copy_as_tsv")) {
				table.copyAsTsv(false);
			} else if (e.getActionCommand().equals("copy_as_html")) {
				table.copyAsHtml(false);
			} else if (e.getActionCommand().equals("copy_as_csv_with_header")) {
				table.copyAsCsv(true);
			} else if (e.getActionCommand().equals("copy_as_tsv_with_header")) {
				table.copyAsTsv(true);
			} else if (e.getActionCommand().equals("copy_as_html_with_header")) {
				table.copyAsHtml(true);
			}
		}
	}
	
	class MouseAdapterImpl extends MouseAdapter {
		public MouseAdapterImpl() {
		}
		public void mouseClicked(MouseEvent e) {
			if ( rdbaTextInterface != null ) {
				// ダブルクリックの内容を関係つけられたテキストコンポーネントに出力
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					Object value = table.getFilteredString(table.getSelectedRow(), table.getSelectedColumn());
					if (value != null) {
						rdbaTextInterface.appendString(value.toString());
					}
				}
			}
		}
	}


	public JPopupMenuForColumnList(boolean createMenuItems) {
		if (createMenuItems) {
			createMenuItems();
		}
	}
	
	public void createMenuItems() {
		ActionListener ac = new ActionListenerForColumnList();

		inputSelectedNameItem = new JMenuItem();
		JMenuItem copyAsCsvItem = new JMenuItem();
		JMenuItem copyAsTsvItem = new JMenuItem();
		JMenuItem copyAsHtmlItem = new JMenuItem();
		JMenuItem copyAsCsvWithHeaderItem = new JMenuItem();
		JMenuItem copyAsTsvWithHeaderItem = new JMenuItem();
		JMenuItem copyAsHtmlWithHeaderItem = new JMenuItem();
		inputSelectedNameItem.setActionCommand("input_selected_name");
		inputSelectedNameItem.addActionListener(ac);
		copyAsCsvItem.setActionCommand("copy_as_csv");
		copyAsCsvItem.addActionListener(ac);
		copyAsTsvItem.setActionCommand("copy_as_tsv");
		copyAsTsvItem.addActionListener(ac);
		copyAsHtmlItem.setActionCommand("copy_as_html");
		copyAsHtmlItem.addActionListener(ac);
		copyAsCsvWithHeaderItem.setActionCommand("copy_as_csv_with_header");
		copyAsCsvWithHeaderItem.addActionListener(ac);
		copyAsTsvWithHeaderItem.setActionCommand("copy_as_tsv_with_header");
		copyAsTsvWithHeaderItem.addActionListener(ac);
		copyAsHtmlWithHeaderItem.setActionCommand("copy_as_html_with_header");
		copyAsHtmlWithHeaderItem.addActionListener(ac);
		ButtonsBundle.getInstance().applyMenuDifinition(inputSelectedNameItem, "input_selected_name");
		ButtonsBundle.getInstance().applyMenuDifinition(copyAsCsvItem, "copy_as_csv");
		ButtonsBundle.getInstance().applyMenuDifinition(copyAsTsvItem, "copy_as_tsv");
		ButtonsBundle.getInstance().applyMenuDifinition(copyAsHtmlItem, "copy_as_html");
		ButtonsBundle.getInstance().applyMenuDifinition(copyAsCsvWithHeaderItem, "copy_as_csv_with_header");
		ButtonsBundle.getInstance().applyMenuDifinition(copyAsTsvWithHeaderItem, "copy_as_tsv_with_header");
		ButtonsBundle.getInstance().applyMenuDifinition(copyAsHtmlWithHeaderItem, "copy_as_html_with_header");
		
		inputSelectedNameItem.setVisible(false);
		
		this.add(inputSelectedNameItem);
		this.add(copyAsCsvItem);
		this.add(copyAsTsvItem);
		this.add(copyAsHtmlItem);
		this.add(copyAsCsvWithHeaderItem);
		this.add(copyAsTsvWithHeaderItem);
		this.add(copyAsHtmlWithHeaderItem);
	}
	
	public void install(JTableForDisplay table) {
		this.table = table;
		MouseAdapterImpl ml = new MouseAdapterImpl();
		table.addMouseListener(ml);
		table.setComponentPopupMenu(this);
	}
	
	public void setMenuItemVisible(boolean inputSelectedNameVisible) {
		inputSelectedNameItem.setVisible(inputSelectedNameVisible);
	}

	public JTableForDisplay getTable() {
		return table;
	}

	public void setTable(JTableForDisplay table) {
		this.table = table;
	}

	public RdbaTextInterface getRdbaTextInterface() {
		return rdbaTextInterface;
	}

	public void setRdbaTextInterface(RdbaTextInterface rdbaTextInterface) {
		this.rdbaTextInterface = rdbaTextInterface;
	}
}
