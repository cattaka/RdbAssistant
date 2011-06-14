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
 * $Id: ResultSetTablePanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.table;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.cattaka.rdbassistant.gui.RdbaTextInterface;
import net.cattaka.swing.StdScrollPane;

public class ResultSetTablePanel extends StdScrollPane {
	private static final long serialVersionUID = 1L;
	private JTableForDisplay table;
	private JTableForDisplay rowHeader;
	private JViewport tableViewport;
	private JViewport rowHeaderViewport;

	class RowHeaderTableModel implements ResultSetTableModel {
		private ResultSetTableModel tableModel;
		private int columnCharacters;
		
		public RowHeaderTableModel(ResultSetTableModel tableModel) {
			this.tableModel = tableModel;
			this.columnCharacters = 1;
			int n = tableModel.getRowCount();
			for (int i=0;i<n;i++) {
				int l = tableModel.getRowName(i).toString().length();
				if (this.columnCharacters < l) {
					this.columnCharacters = l;
				}
			}
		}
		
		public void addTableModelListener(TableModelListener l) {
			return;
		}

		public Class<?> getColumnClass(int columnIndex) {
			return tableModel.getRowHeaderClass();
		}

		public int getColumnCount() {
			return 1;
		}

		public String getColumnName(int columnIndex) {
			return "";
		}

		public int getRowCount() {
			return tableModel.getRowCount();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return tableModel.getRowName(rowIndex);
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public void removeTableModelListener(TableModelListener l) {
			return;
		}

		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			return;
		}

		public int[] getColumnCharacters() {
			return new int []{this.columnCharacters};
		}

		public String getRowName(int row) {
			return "";
		}

		public String getTableName() {
			return tableModel.getTableName();
		}

		public Class<?> getRowHeaderClass() {
			return tableModel.getRowHeaderClass();
		}
		
	}
	
	public ResultSetTablePanel() {
		super();
		this.table = new JTableForDisplay();
		this.tableViewport = new JViewport();
		{
			tableViewport.setView(table);
			this.setViewport(tableViewport);
		}
		
		this.rowHeader = new JTableForDisplay();
		this.rowHeaderViewport = new JViewport();
		{
			this.rowHeader.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			this.rowHeader.setForeground(this.table.getTableHeader().getForeground());
			this.rowHeader.setBackground(this.table.getTableHeader().getBackground());
			rowHeaderViewport.setView(rowHeader);
			this.setRowHeader(rowHeaderViewport);
			
			this.setRowHeader(rowHeaderViewport);
		}
	}

	
	public int getAutoResizeMode() {
		return table.getAutoResizeMode();
	}


	public boolean getCellSelectionEnabled() {
		return table.getCellSelectionEnabled();
	}


	public void setAutoResizeMode(int mode) {
		table.setAutoResizeMode(mode);
	}


	public void setCellSelectionEnabled(boolean cellSelectionEnabled) {
		table.setCellSelectionEnabled(cellSelectionEnabled);
	}


	public TableModel getModel() {
		return table.getModel();
	}

//	public void setModel(TableModel dataModel) {
//		table.setModel(dataModel);
//	}

	public String getFilteredString(int rowIndex) {
		Object value = table.getValueAt(rowIndex, 0);
		return value.toString();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (table != null) {
			table.setFont(font);
		}
		if (rowHeader != null) {
			rowHeader.setFont(font);
		}
	}


	public void setResultSetTableModel(ResultSetTableModel dataModel) {
		table.setResultSetTableModel(dataModel);
		this.rowHeader.setResultSetTableModel(new RowHeaderTableModel(dataModel));
		this.rowHeaderViewport.setPreferredSize(this.rowHeader.getPreferredSize());
	}

	public RdbaTextInterface getRdbaTextInterface() {
		return table.getRdbaTextInterface();
	}

	public void setRdbaTextInterface(RdbaTextInterface rdbaTextInterface) {
		table.setRdbaTextInterface(rdbaTextInterface);
	}
	
	public boolean isTransposed() {
		return (table.getModel() instanceof TransposedTableModel);
	}
	/**
	 * 現在のモデルがResultSetTableModelでない場合はエラーを吐くので注意。
	 * @param transposed
	 */
	public void setTransposed(boolean transposed) {
		boolean goFlag = false;
		int[] rows = this.table.getSelectedRows();
		int[] cols = this.table.getSelectedColumns();
		if (isTransposed()) {
			if (!transposed) {
				TransposedTableModel tm = (TransposedTableModel)table.getModel();
				this.setResultSetTableModel(tm.getTableModel());
				goFlag = true;
			}
		} else {
			if (transposed) {
				if (table.getModel() instanceof ResultSetTableModel) {
					TransposedTableModel tm = new TransposedTableModel((ResultSetTableModel)table.getModel());
					this.setResultSetTableModel(tm);
					goFlag = true;
				}
			}
		}
		if (goFlag) {
			this.table.clearSelection();
			if (rows.length > 0) {
				if (rows.length == 1) {
					this.table.addColumnSelectionInterval(rows[0], rows[0]);
				} else {
					for (int i=1;i<rows.length;i++) {
						this.table.addColumnSelectionInterval(rows[i-1], rows[i]);
//						System.out.println("R:"+rows[i-1]+","+rows[i]);
					}
				}
			}
			if (cols.length > 0) {
				if (cols.length == 1) {
					this.table.addRowSelectionInterval(cols[0], cols[0]);
				} else {
					for (int i=1;i<cols.length;i++) {
						this.table.addRowSelectionInterval(cols[i-1], cols[i]);
//						System.out.println("C:"+cols[i-1]+","+cols[i]);
					}
				}
			}
		}
	}
}
