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
 * $Id: DatabaseInfo.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.jspf.core.bean;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseInfo {
	public DatabaseMetaData databaseMetaData;
	public String name;
	private List<TableInfo> tableInfoList = null;

	public DatabaseInfo(DatabaseMetaData databaseMetaData, String name) {
		this.databaseMetaData = databaseMetaData;
		this.name = name;
	}

	private void createTableInfoList() throws SQLException {
		Connection connection = databaseMetaData.getConnection();
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getTables(connection.getCatalog(), this.name, "%", null);
			this.tableInfoList = new ArrayList<TableInfo>();
			
			while(rs.next()) {
				String tableName = "";
				tableName = rs.getString("TABLE_NAME");
				TableInfo TableInfo = new TableInfo(this, databaseMetaData, tableName);
				this.tableInfoList.add(TableInfo);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	public TableInfo getTableInfo(String tableName) throws SQLException {
		TableInfo result = null;
		List<TableInfo> til = getTableInfoList();
		for (TableInfo ti : til) {
			if (ti.getName().equals(tableName)) {
				result = ti;
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
	

	public List<TableInfo> getTableInfoList() throws SQLException {
		if (tableInfoList == null) {
			this.createTableInfoList();
		}
		return tableInfoList;
	}
}
