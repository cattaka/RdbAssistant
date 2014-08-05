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
 * $Id: ScriptTableModel.java 265 2010-03-01 11:35:07Z cattaka $
 */
package net.cattaka.rdbassistant.script.core;

import javax.swing.event.TableModelListener;

import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.gui.table.ResultSetTableModel;
import net.cattaka.swing.table.StdStyledCell;

/**
 * ScriptTableをJTable上で表示するためのTableModelです。
 * 
 * @author cattaka
 */
public class ScriptTableModel implements ResultSetTableModel {
	private ScriptTable table;

	public ScriptTableModel(ScriptTable table) {
		this.table = table;
	}
	
	public void addTableModelListener(TableModelListener l) {
		// 無し
	}

	public Class<?> getColumnClass(int columnIndex) {
		return StdStyledCell.class;
	}

	public int getColumnCount() {
		return this.table.getColumnCount();
	}

	public String getColumnName(int columnIndex) {
		return String.valueOf(columnIndex);
	}

	public int getRowCount() {
		return this.table.getRowCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.table.getCellAt(rowIndex, columnIndex);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {
		// 無し
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		this.table.setCellAt((StdStyledCell)value, rowIndex, columnIndex);
	}

	public int[] getColumnCharacters() {
		int[] columnCharacters = new int[this.table.getColumnCount()];
		for (int i=0;i<columnCharacters.length;i++) {
			columnCharacters[i] = RdbaConstants.MINIMUM_COLUMN_CHARACTERS; 
		}
		return columnCharacters;
	}

	public Class<?> getRowHeaderClass() {
		return String.class;
	}

	public Object getRowName(int row) {
		return String.valueOf(row);
	}

	public String getTableName() {
		return (this.table != null) ? this.table.getTableName() : "";
	}
}
