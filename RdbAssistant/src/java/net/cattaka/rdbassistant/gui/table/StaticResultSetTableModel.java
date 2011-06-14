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
 * $Id: StaticResultSetTableModel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.table;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.core.RdbaConnectionUtil;
import net.cattaka.rdbassistant.sql.ColumnConverter;

public class StaticResultSetTableModel extends AbstractTableModel implements ResultSetTableModel {
	private static final long serialVersionUID = 1L;
	private PropertyTable propertyTable;
	
	public StaticResultSetTableModel() {
		propertyTable = new PropertyTable();
	}

	public void extractResultSetData(RdbaConnection rdbConnection, ResultSet resultSet, String nullString) throws SQLException {
		propertyTable = new PropertyTable();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		String[] columnNames = new String[rsmd.getColumnCount()];
		int[] columnCharacters = new int[rsmd.getColumnCount()];
		for (int i=0;i<columnNames.length;i++) {
			columnNames[i] = rsmd.getColumnLabel(i+1);
			columnCharacters[i] = columnNames[i].length();
		}
		
		ColumnConverter[] columnConverterList = RdbaConnectionUtil.createColumnConverterList(rdbConnection, rsmd);
		Class<?>[] columnClasses = new Class<?>[columnConverterList.length];
		for (int i=0;i<columnConverterList.length;i++) {
			columnClasses[i] = columnConverterList[i].getOutClass();
		}
		
		propertyTable.setColumnNames(columnNames);
		propertyTable.setColumnClasses(columnClasses);
		ArrayList<Object[]> columnValues = new ArrayList<Object[]>();
		while(resultSet.next()) {
			Object[] objArray = new Object[columnNames.length];
			for (int i=0;i<objArray.length;i++) {
				Object obj = rdbConnection.extractResultSetData(resultSet, columnClasses[i], i+1);
				if (obj == null) {
					obj = nullString;
				}
				objArray[i] = obj;
			}
			columnValues.add(objArray);
		}
		
		// 列の文字数を計算
		{
			for (Object[] objArray : columnValues) {
				for (int i=0;i<objArray.length;i++) {
					if (objArray[i] instanceof String) {
						int l = objArray[i].toString().length();
						if (columnCharacters[i] < l) {
							columnCharacters[i] = l;
						}
					}
				}
			}
			for (int i=0;i<columnCharacters.length;i++) {
				if (columnCharacters[i] < RdbaConstants.MINIMUM_COLUMN_CHARACTERS) {
					columnCharacters[i] = RdbaConstants.MINIMUM_COLUMN_CHARACTERS;
				}
				if (columnCharacters[i] > RdbaConstants.MAXIMUM_COLUMN_CHARACTERS) {
					columnCharacters[i] = RdbaConstants.MAXIMUM_COLUMN_CHARACTERS;
				}
			}
		}
		propertyTable.setColumnCharacters(columnCharacters);
		propertyTable.setColumnValues(columnValues);
	}
	
	public void setTableName(String tableName) {
		this.propertyTable.setTableName(tableName);
	}
	
	public String getTableName() {
		return this.propertyTable.getTableName();
	}
	
	public int[] getColumnCharacters() {
		return this.propertyTable.getColumnCharacters();
	}
	
	public String getColumnName(int column) {
		return this.propertyTable.getColumnNames()[column];
	}
	
	public Object getRowName(int row) {
		return Integer.valueOf(row + 1);
	}

	public Class<?> getRowHeaderClass() {
		return Integer.class;
	}

	public int getColumnCount() {
		return this.propertyTable.getColumnNames().length;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return this.propertyTable.getColumnClasses()[columnIndex];
	}
	
	public int getRowCount() {
		return this.propertyTable.getColumnValues().size();
	}

	public Object getValueAt(int arg0, int arg1) {
		try {
			return this.propertyTable.getColumnValues().get(arg0)[arg1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
}
