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
/*
 * $Id: SqliteSqlEditorSelection.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.driver.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.cattaka.rdbassistant.core.SqlEditorSelection;
import net.cattaka.rdbassistant.gui.table.ResultSetTableModel;
import net.cattaka.rdbassistant.gui.table.SortWrapperResultSetTableModel;
import net.cattaka.rdbassistant.gui.table.StaticResultSetTableModel;
import net.cattaka.rdbassistant.script.core.ScriptTable;
import net.cattaka.rdbassistant.script.core.ScriptTableModel;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class JdbcSqlEditorSelection implements SqlEditorSelection {
	private JdbcRdbaConnection rdbConnection;
	private String[] SORT_COLUMNS = new String[] {"COLUMN_NAME","REMARKS", "TYPE_NAME", "COLUMN_SIZE", "DECIMAL_DIGITS", "IS_NULLABLE"};
	private String[] SORT_PRIMARY_KEYS = new String[] {"COLUMN_NAME", "PK_NAME", "TABLE_CAT", "KEY_SEQ"};
	private String[] SORT_TABLE = new String[] {"TABLE_NAME","REMARKS","TABLE_TYPE"};
	
	static {
	}
	
	public JdbcSqlEditorSelection(JdbcRdbaConnection rdbConnection) {
		this.rdbConnection = rdbConnection;
	}

	public String getDefaultDatabase() {
		return rdbConnection.getDefaultDatabase();
	}

	public List<String> getDatabaseList() {
		DatabaseMetaData databaseMetaData;
		try {
			databaseMetaData = rdbConnection.getConnection().getMetaData();
		} catch (SQLException e) {
			ExceptionHandler.error(e);
			return new ArrayList<String>();
		}
		ArrayList<String> result = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getSchemas();
			while(rs.next()) {
				result.add(rs.getString("TABLE_SCHEM"));
			}
		} catch (SQLException e) {
			ExceptionHandler.error(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					ExceptionHandler.warn(e);
				}
			}
		}
		if (result.size() == 0) {
			result.add("");
		}
		return result;
	}

	public List<String> getObjectTypeList() {
		DatabaseMetaData databaseMetaData;
		try {
			databaseMetaData = rdbConnection.getConnection().getMetaData();
		} catch (SQLException e) {
			ExceptionHandler.error(e);
			return new ArrayList<String>();
		}
		ArrayList<String> result = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getTableTypes();
			while(rs.next()) {
				result.add(rs.getString("TABLE_TYPE"));
			}
		} catch (SQLException e) {
			ExceptionHandler.error(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					ExceptionHandler.warn(e);
				}
			}
		}
		if (result.remove("TABLE")) {
			result.add(0, "TABLE");
		}
		if (result.size() == 0) {
			result.add("-");
		}
		return result;
	}
	
	public ResultSetTableModel getTableList(String database, String objectType) {
		DatabaseMetaData databaseMetaData;
		try {
			databaseMetaData = rdbConnection.getConnection().getMetaData();
		} catch (SQLException e) {
			ExceptionHandler.error(e);
			return new StaticResultSetTableModel();
		}
		
		SortWrapperResultSetTableModel result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getTables(null, database, null, new String[]{objectType});
			StaticResultSetTableModel pt = new StaticResultSetTableModel();
			pt.extractResultSetData(rdbConnection, rs, null);
			
			result = new SortWrapperResultSetTableModel(pt);
			result.moveColumnFront(SORT_TABLE);
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
		return result;
	}

	public List<ResultSetTableModel> getTableProperties(String database,String objectType, String table) {
		ArrayList<ResultSetTableModel> result = new ArrayList<ResultSetTableModel>();

		DatabaseMetaData databaseMetaData;
		try {
			databaseMetaData = rdbConnection.getConnection().getMetaData();
		} catch (SQLException e) {
			ExceptionHandler.error(e);
			return result;
		}
		
		if (true) {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			for (int i=0;i<6;i++) {
				String tableTitle = "";
				try {
					SortWrapperResultSetTableModel rstm = null;
					StaticResultSetTableModel pt = new StaticResultSetTableModel();
					if (i==0) {
						tableTitle = MessageBundle.getMessage("list_columns");
						rs = databaseMetaData.getColumns(null, database, table, null);
						pt.extractResultSetData(rdbConnection, rs, null);
						rstm = new SortWrapperResultSetTableModel(pt);
						rstm.moveColumnFront(SORT_COLUMNS);
					} else if (i==1) {
						tableTitle = MessageBundle.getMessage("list_primary_keys");
						rs = databaseMetaData.getPrimaryKeys(null, database, table);
						pt.extractResultSetData(rdbConnection, rs, null);
						rstm = new SortWrapperResultSetTableModel(pt);
						rstm.moveColumnFront(SORT_PRIMARY_KEYS);
					} else if (i==2) {
						tableTitle = MessageBundle.getMessage("list_exported_keys");
						rs = databaseMetaData.getExportedKeys(null, database, table);
						pt.extractResultSetData(rdbConnection, rs, null);
						rstm = new SortWrapperResultSetTableModel(pt);
					} else if (i==3) {
						tableTitle = MessageBundle.getMessage("list_imported_keys");
						rs = databaseMetaData.getImportedKeys(null, database, table);
						pt.extractResultSetData(rdbConnection, rs, null);
						rstm = new SortWrapperResultSetTableModel(pt);
					} else if (i==4) {
						tableTitle = MessageBundle.getMessage("list_table_privileges");
						rs = databaseMetaData.getTablePrivileges(null, database, table);
						pt.extractResultSetData(rdbConnection, rs, null);
						rstm = new SortWrapperResultSetTableModel(pt);
					} else if (i==5) {
						tableTitle = MessageBundle.getMessage("list_column_privileges");
						rs = databaseMetaData.getColumnPrivileges(null, database, table, null);
						pt.extractResultSetData(rdbConnection, rs, null);
						rstm = new SortWrapperResultSetTableModel(pt);
					}
					pt.setTableName(tableTitle);
					
					if (rstm != null) {
						result.add(rstm);
					}
				} catch (SQLException e) {
					ExceptionHandler.debug(e);
					ScriptTable st = new ScriptTable(1,2);
					st.setValueAt("Error", 0, 0);
					st.setValueAt(e.getMessage(), 0, 1);
					ScriptTableModel stModel = new ScriptTableModel(st);
					st.setTableName(tableTitle);
					result.add(stModel);
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
