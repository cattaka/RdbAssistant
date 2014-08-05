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
 * $Id: SortWrapperResultSetTableModel.java 266 2010-03-01 11:35:32Z cattaka $
 */
package net.cattaka.rdbassistant.gui.table;

import javax.swing.event.TableModelListener;

public class SortWrapperResultSetTableModel implements ResultSetTableModel {
	private ResultSetTableModel resultSetTableModel;
	private int[] sortMap;
	
	public SortWrapperResultSetTableModel(ResultSetTableModel resultSetTableModel) {
		super();
		this.resultSetTableModel = resultSetTableModel;
		this.sortMap = new int[resultSetTableModel.getColumnCount()];
		for (int i=0;i<sortMap.length;i++) {
			sortMap[i] = i;
		}
	}

	public int[] getColumnCharacters() {
		int[] base = this.resultSetTableModel.getColumnCharacters();
		int[] result = new int[base.length];
		for (int i=0;i<result.length;i++) {
			result[i]  = base[sortMap[i]];
		}
		return result;
	}

	public void moveColumnFront(int column) {
		int target = sortMap[column];
		for (int i=column;i>0;i--) {
			sortMap[i] = sortMap[i-1];
		}
		sortMap[0] = target;
	}

	public void moveColumnFront(String columnName) {
		int target = -1;
		for (int i=0;i<this.getColumnCount();i++) {
			String tname = this.getColumnName(i);
			if (tname.equals(columnName)) {
				target = i;
				break;
			}
		}
		if (target != -1) {
			this.moveColumnFront(target);
		}
	}

	public void moveColumnFront(String[] columnNames) {
		for (int i=columnNames.length-1;i>=0;i--) {
			if (columnNames[i] != null) {
				this.moveColumnFront(columnNames[i]);
			}
		}
	}

	public Class<?> getColumnClass(int column) {
		return this.resultSetTableModel.getColumnClass(sortMap[column]);
	}

	public Class<?> getRowHeaderClass() {
		return this.resultSetTableModel.getRowHeaderClass();
	}

	public Object getRowName(int row) {
		return this.resultSetTableModel.getRowName(row);
	}

	public String getTableName() {
		return this.resultSetTableModel.getTableName();
	}

	public void addTableModelListener(TableModelListener l) {
		this.resultSetTableModel.addTableModelListener(l);
	}

	public int getColumnCount() {
		return this.resultSetTableModel.getColumnCount();
	}

	public String getColumnName(int columnIndex) {
		return this.resultSetTableModel.getColumnName(sortMap[columnIndex]);
	}

	public int getRowCount() {
		return this.resultSetTableModel.getRowCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.resultSetTableModel.getValueAt(rowIndex, sortMap[columnIndex]);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return this.resultSetTableModel.isCellEditable(rowIndex, sortMap[columnIndex]);
	}

	public void removeTableModelListener(TableModelListener l) {
		this.resultSetTableModel.removeTableModelListener(l);
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		this.resultSetTableModel.setValueAt(value, rowIndex, sortMap[columnIndex]);
	}
}
