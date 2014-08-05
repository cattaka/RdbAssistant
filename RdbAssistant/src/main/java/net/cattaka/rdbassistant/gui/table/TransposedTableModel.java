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
 * $Id: TransposedTableModel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.table;

import javax.swing.event.TableModelListener;

import net.cattaka.rdbassistant.RdbaConstants;

public class TransposedTableModel implements ResultSetTableModel {
	private ResultSetTableModel tableModel;
	private int[] columnCharacters;
	private int columnWidth;
	
	public TransposedTableModel(ResultSetTableModel tableModel) {
		this.tableModel = tableModel;
		// 列幅の計算
		int[] origColumnCharacters = this.tableModel.getColumnCharacters();
		this.columnWidth = RdbaConstants.MINIMUM_COLUMN_CHARACTERS;
		for (int i=0;i<origColumnCharacters.length;i++) {
			if (this.columnWidth < origColumnCharacters[i]) {
				this.columnWidth = origColumnCharacters[i];
			}
		}
		
		// 列幅の設定
		this.columnCharacters = new int[tableModel.getRowCount()];
		for (int i=0;i<this.columnCharacters.length;i++) {
			this.columnCharacters[i] = columnWidth;
		}
	}

	public ResultSetTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(ResultSetTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public void addTableModelListener(TableModelListener l) {
		tableModel.addTableModelListener(l);
	}

	public Class<?> getColumnClass(int columnIndex) {
		//return tableModel.getColumnClass(columnIndex);
		return String.class;
	}

	public int getColumnCount() {
		//return tableModel.getColumnCount();
		return tableModel.getRowCount();
	}

	public String getColumnName(int columnIndex) {
		return tableModel.getRowName(columnIndex).toString();
	}

	public Object getRowName(int row) {
		return tableModel.getColumnName(row);
	}

	public int getRowCount() {
		//return tableModel.getRowCount();
		return tableModel.getColumnCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableModel.getValueAt(columnIndex, rowIndex);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return tableModel.isCellEditable(columnIndex, rowIndex);
	}

	public void removeTableModelListener(TableModelListener l) {
		tableModel.removeTableModelListener(l);
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		tableModel.setValueAt(value, columnIndex, rowIndex);
	}

	public int[] getColumnCharacters() {
		//return tableModel.getColumnCharacters();
		return this.columnCharacters;
	}

	public String getTableName() {
		return tableModel.getTableName();
	}

	public Class<?> getRowHeaderClass() {
		return String.class;
	}
}
