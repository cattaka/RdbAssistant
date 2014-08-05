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
 * $Id: PropertyTable.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.table;

import java.util.ArrayList;
import java.util.List;

import net.cattaka.util.StringUtil;

public class PropertyTable {
	private String tableName = "";
	private String[] columnNames = new String[0];
	private List<Object[]> columnValues = new ArrayList<Object[]>();
	private Class<?>[] columnClasses = new Class<?>[0];
	private int[] columnCharacters = new int[0];
	
	public PropertyTable() {
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	public List<Object[]> getColumnValues() {
		return columnValues;
	}
	public void setColumnValues(List<Object[]> columnValues) {
		this.columnValues = columnValues;
	}
	public Class<?>[] getColumnClasses() {
		return columnClasses;
	}
	public void setColumnClasses(Class<?>[] columnClasses) {
		this.columnClasses = columnClasses;
	}
	public int[] getColumnCharacters() {
		return columnCharacters;
	}
	public void setColumnCharacters(int[] columnCharacters) {
		this.columnCharacters = columnCharacters;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TABLE_NAME : ");
		sb.append(tableName);
		sb.append('\n');
		sb.append("COLUMN_NAMES : ");
		sb.append(StringUtil.toString(columnNames));
		sb.append('\n');
		sb.append("COLUMN_VALUES : ");
		sb.append('\n');
		for(Object[] cvs : columnValues) {
			sb.append(StringUtil.toString(cvs));
			sb.append('\n');
		}
		
		return sb.toString();
	}
}
