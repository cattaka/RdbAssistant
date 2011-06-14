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
 * $Id: TableInfo.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.jspf.core.bean;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TableInfo {
	public DatabaseMetaData databaseMetaData;
	public String name;
	private DatabaseInfo parentInfo;
	private List<ColumnInfo> columnInfoList = null;
	
	public TableInfo(DatabaseInfo parentInfo, DatabaseMetaData databaseMetaData, String name) {
		this.parentInfo = parentInfo;
		this.databaseMetaData = databaseMetaData;
		this.name = name;
	}
	
	private void createColumnInfoList() throws SQLException {
		Connection connection = databaseMetaData.getConnection();
		ResultSet rs = null;
		HashSet<String> primaryKeys = new HashSet<String>(); 
		
		// 主キーの取得
		try {
			rs = databaseMetaData.getPrimaryKeys(connection.getCatalog(), parentInfo.getName(), this.name);
			while(rs.next()) {
				String columnName = "";
				columnName = rs.getString("COLUMN_NAME");
				primaryKeys.add(columnName);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		// 列一覧の取得
		try {
			rs = databaseMetaData.getColumns(connection.getCatalog(), parentInfo.getName(), this.name, "%");
			this.columnInfoList = new ArrayList<ColumnInfo>();
			
			while(rs.next()) {
				String columnName = "";
				boolean primaryKey = false;
				
				columnName = rs.getString("COLUMN_NAME");
				primaryKey = primaryKeys.contains(columnName);
				
				ColumnInfo columnInfo = new ColumnInfo(databaseMetaData, columnName);
				columnInfo.setPrimaryKey(primaryKey);
				this.columnInfoList.add(columnInfo);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	public ColumnInfo getColumnInfo(String columnName) throws SQLException {
		ColumnInfo result = null;
		List<ColumnInfo> cil = getColumnInfoList();
		for (ColumnInfo ci : cil) {
			if (ci.getName().equals(columnName)) {
				result = ci;
				break;
			}
		}
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ColumnInfo> getColumnInfoList() throws SQLException {
		if (columnInfoList == null) {
			this.createColumnInfoList();
		}
		return columnInfoList;
	}
}
