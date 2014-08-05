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
 * $Id: SqliteSqlEditorSelection.java 258 2010-02-27 13:43:19Z cattaka $
 */
package net.cattaka.rdbassistant.driver.telnetsqlite;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.cattaka.rdbassistant.core.SqlEditorSelection;
import net.cattaka.rdbassistant.gui.table.ResultSetTableModel;
import net.cattaka.rdbassistant.gui.table.SortWrapperResultSetTableModel;
import net.cattaka.rdbassistant.gui.table.StaticResultSetTableModel;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.PropertiesEx;
import net.cattaka.util.StringUtil;

public class TelnetSqliteSqlEditorSelection implements SqlEditorSelection {
	public static String SELECT_DATABASE_LIST;
	public static String SELECT_TABLES_LIST;
	public static String[] SELECT_DETAIL_NAME_LIST;
	public static String[] SELECT_DETAIL_SQL_LIST;
	public static String[] SELECT_DETAIL_TOP_COLUMN_LIST;
	
	private TelnetSqliteRdbaConnection rdbConnection;
	
	static {
		PropertiesEx properties = new PropertiesEx();
		InputStream in = TelnetSqliteSqlEditorSelection.class.getClassLoader().getResourceAsStream("SqlEditorSelection.sqlite.properties");
		try {
			properties.loadFromXML(in);
			in.close();
		} catch (IOException e) {
			ExceptionHandler.fatal(e);
		}
		SELECT_DATABASE_LIST = properties.getProperty("SELECT_DATABASE_LIST");
		SELECT_TABLES_LIST = properties.getProperty("SELECT_TABLES_LIST");
		SELECT_DETAIL_NAME_LIST = properties.getPropertyArray("SELECT_DETAIL_NAME_LIST");
		SELECT_DETAIL_SQL_LIST = properties.getPropertyArray("SELECT_DETAIL_SQL_LIST");
		SELECT_DETAIL_TOP_COLUMN_LIST = properties.getPropertyArray("SELECT_DETAIL_TOP_COLUMN_LIST");
	}
	
	public TelnetSqliteSqlEditorSelection(TelnetSqliteRdbaConnection rdbConnection) {
		this.rdbConnection = rdbConnection;
	}

	public String getDefaultDatabase() {
		return rdbConnection.getDefaultDatabase();
	}

	public List<String> getDatabaseList() {
		String sqlStr = SELECT_DATABASE_LIST;
		ArrayList<String> result = new ArrayList<String>();
		try {
			PreparedStatement stmt = rdbConnection.getConnection().prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				// TODO : 列番を決め打ちしている
				result.add(rs.getString(2));
			}
			stmt.close();
		} catch (SQLException e) {
			ExceptionHandler.error(e);
		}
		return result;
	}

	public List<String> getObjectTypeList() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("ALL");
		return result;
	}
	
	public ResultSetTableModel getTableList(String database, String objectType) {
		String sqlStr = null;
		sqlStr = SELECT_TABLES_LIST;
		StaticResultSetTableModel result = new StaticResultSetTableModel();
		if (sqlStr != null) {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				stmt = rdbConnection.getConnection().prepareStatement(sqlStr);
//				stmt.setString(1, database);
				rs = stmt.executeQuery();
				result.extractResultSetData(rdbConnection, rs, null);
			} catch (SQLException e) {
				ExceptionHandler.error(e);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					ExceptionHandler.warn(e);
				}
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException e) {
					ExceptionHandler.warn(e);
				}
			}
		}
		return result;
	}

	public List<ResultSetTableModel> getTableProperties(String database,String objectType, String table) {
		ArrayList<ResultSetTableModel> result = new ArrayList<ResultSetTableModel>();
		if (objectType.equals("ALL")) {
			for (int i=0;i<SELECT_DETAIL_NAME_LIST.length;i++) {
				PreparedStatement stmt = null;
				ResultSet rs = null;
				String sqlStr = String.format(SELECT_DETAIL_SQL_LIST[i], database, table);
				try {
					StaticResultSetTableModel pt = new StaticResultSetTableModel();
					// 実テーブルの列情報
					sqlStr = StringUtil.replaceString(sqlStr, "?", table);
					stmt = rdbConnection.getConnection().prepareStatement(sqlStr);
					rs = stmt.executeQuery();
					if (rs != null) {
						pt.extractResultSetData(rdbConnection, rs, null);
						pt.setTableName(SELECT_DETAIL_NAME_LIST[i]);
						
						SortWrapperResultSetTableModel rstm = new SortWrapperResultSetTableModel(pt);
						rstm.moveColumnFront(SELECT_DETAIL_TOP_COLUMN_LIST[i]);
						
						result.add(rstm);
					}
				} catch (SQLException e) {
					ExceptionHandler.error(e);
				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
					} catch (SQLException e) {
						ExceptionHandler.warn(e);
					}
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException e) {
						ExceptionHandler.warn(e);
					}
				}
			}
		}
		return result;
	}
		
	public String[] getColumnDefNameAsArray() {
		// TODO : ローカライズ
		return new String[]{"COLUMN_NAME", "TYPE", "NULLABLE", "PRIMARY KEY", "DEFUALT_VALUE", "COMMENT"};
	}
}
