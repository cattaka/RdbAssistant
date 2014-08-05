/*
 * Copyright (c) 2010, Takao Sumitomo
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
package net.cattaka.rdbassistant.driver.telnetsqlite.jdbc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TelnetSqliteResultSetMetaData implements java.sql.ResultSetMetaData {
	private String[] columnNameList;
	private int[] columnTypeList;
	private Map<String, Integer> columnName2IndexMap;
	
	public TelnetSqliteResultSetMetaData(String[] columnNameList,
			int[] columnTypeList) {
		super();
		this.columnNameList = columnNameList;
		this.columnTypeList = columnTypeList;
		
		columnName2IndexMap = new HashMap<String, Integer>();
		for (int i=0;i<columnNameList.length;i++) {
			columnName2IndexMap.put(columnNameList[i], i);
		}
	}

	public int getColumnIndex(String columnName) {
		Integer result = columnName2IndexMap.get(columnName);
		return (result != null) ? result.intValue() : -1;
	}
	
	public String getCatalogName(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public String getColumnClassName(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public int getColumnCount() throws SQLException {
		return columnNameList.length;
	}

	public int getColumnDisplaySize(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public String getColumnLabel(int paramInt) throws SQLException {
		return this.columnNameList[paramInt-1];
	}

	public String getColumnName(int paramInt) throws SQLException {
		return this.columnNameList[paramInt-1];
	}

	public int getColumnType(int paramInt) throws SQLException {
		return this.columnTypeList[paramInt-1];
	}

	public String getColumnTypeName(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public int getPrecision(int paramInt) throws SQLException {
		return 0;
	}

	public int getScale(int paramInt) throws SQLException {
		return 10;
	}

	public String getSchemaName(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public String getTableName(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public boolean isAutoIncrement(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public boolean isCaseSensitive(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public boolean isCurrency(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public boolean isDefinitelyWritable(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public int isNullable(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public boolean isReadOnly(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public boolean isSearchable(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public boolean isSigned(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public boolean isWritable(int paramInt) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
		throw new SQLException("Not implemented.");
	}

	public <T> T unwrap(Class<T> paramClass) throws SQLException {
		throw new SQLException("Not implemented.");
	}
}
