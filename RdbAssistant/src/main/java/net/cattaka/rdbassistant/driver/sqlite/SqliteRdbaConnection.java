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
 * $Id: SqliteRdbaConnection.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.rdbassistant.driver.sqlite;

import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.core.SqlEditorSelection;
import net.cattaka.rdbassistant.util.ConnectionWrapper;
import net.cattaka.swing.text.StdStyledDocument;
import net.cattaka.util.ExceptionHandler;

/**
 * 作成にはRdbConnectionFactoryを使用すること。
 * 
 * @author cattaka
 */
public class SqliteRdbaConnection implements RdbaConnection {
	private ConnectionWrapper connection;
	private SqliteSqlEditorSelection myEditorSelection;
	private String defaultDatabase;
	private static HashSet<String> reservedWords = null;
	
	SqliteRdbaConnection(ConnectionWrapper connection, String defaultDatabase) {
		this.connection = connection;
		this.defaultDatabase = defaultDatabase;
		myEditorSelection = new SqliteSqlEditorSelection(this);
		
		if (reservedWords == null) {
			createReservedWords();
		}
	}
	
	public ConnectionWrapper getConnection() {
		return connection;
	}

	public void dispose() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			// あり得ない
		}
	}

	public String getDefaultDatabase() {
		return defaultDatabase;
	}

	public SqlEditorSelection getSqlEditorSelection() {
		return myEditorSelection;
	}

	public Class<?> getClassFromSqlType(int columnType) throws SQLException {
		Class<?> result;
		result = String.class;

//		switch(columnType) {
//		case Types.BIT:
//		case Types.BOOLEAN:
//		case Types.CHAR:
//		case Types.LONGVARCHAR:
//		case Types.NULL:
//		case Types.VARCHAR:
//			result = String.class;
//			break;
//		case Types.TIME:
//			result = Time.class;
//			break;
//		case Types.DATE:
//			result = java.util.Date.class;
//			break;
//		case Types.TIMESTAMP:
//		case -102:		//	TIMESTAMPLTZ
//		case -100:		//	TIMESTAMPNS
//		case -101:		//	TIMESTAMPTZ
//			result = Timestamp.class;
//			break;
//		case -103:		// INTERVALYM
//		case -104:		// INTERVALDS
//			result = String.class;
//			break;
//		case Types.BIGINT:
//		case Types.DECIMAL:
//		case Types.DOUBLE:
//		case Types.FLOAT:
//		case Types.INTEGER:
//		case Types.TINYINT:
//		case Types.NUMERIC:
//		case Types.REAL:
//		case Types.SMALLINT:
//		case Types.CLOB:
//		case 100:		// BINARY_FLOAT
//		case 101:		// BINARY_DOUBLE
//		case -8:		// ROWID
//			result = String.class;
//			break;
//		case Types.ARRAY:
//		case Types.BINARY:
//		case Types.BLOB:
//		case Types.DATALINK:
//		case Types.DISTINCT:
//		case Types.JAVA_OBJECT:
//		case Types.LONGVARBINARY:
//		case Types.OTHER:
//		case Types.REF:
//		case Types.STRUCT:
//		case Types.VARBINARY:
//		default:
//			result = Other.class;
//			break;
//		}
		return result;
	}

	public Object extractResultSetData(ResultSet resultSet, Class<?> columnClass, int columnIndex) throws SQLException {
		if (columnClass == String.class) {
			return resultSet.getString(columnIndex);
		} else if (columnClass == BigDecimal.class) {
			return resultSet.getBigDecimal(columnIndex);
		} else if (columnClass == Timestamp.class) {
			return resultSet.getTimestamp(columnIndex);
		} else if (columnClass == Time.class) {
			return resultSet.getTime(columnIndex);
		} else if (columnClass == Date.class) {
			return resultSet.getDate(columnIndex);
		} else if (columnClass == java.util.Date.class) {
			return resultSet.getTimestamp(columnIndex);
		} else {
			return null;
		}
	}

	public StdStyledDocument createNewSqlDocument() {
		SqliteStyledDocument styledDocument = new SqliteStyledDocument();
		styledDocument.setReservedWords(this.getReservedWords());
		return styledDocument;
	}
		
	private Set<String> getReservedWords() {
		return reservedWords;
	}
	
	@SuppressWarnings("unchecked")
	private static void createReservedWords() {
		InputStream in = null;
		XMLDecoder dec = null;
		try {
			in = SqliteRdbaConnection.class.getClassLoader().getResourceAsStream(RdbaConstants.ORACLE_RESERVED_WORDS_FILE);
			dec = new XMLDecoder(in);
			reservedWords = (HashSet<String>)dec.readObject();
		} catch(Exception e) {
			ExceptionHandler.error(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch(IOException e) {
					ExceptionHandler.warn(e);
				}
			}
			if (dec != null) {
				dec.close();
			}
		}
	}
	
	public String getLastDbmsOutput() {
//		StringBuilder result = new StringBuilder();
//		CallableStatement stmt = null;
//		try {
//			String sql = "{call dbms_output.get_line(?, ?)}";
//			stmt = connection.prepareCall(sql);
//			stmt.registerOutParameter(1, Types.VARCHAR);
//			stmt.registerOutParameter(2, Types.INTEGER);
//			while (true) {
//				stmt.execute();
//				if (stmt.getInt(2) == 0) {
//					result.append(stmt.getString(1));
//					result.append('\n');
//				} else {
//					break;
//				}
//			}
//		} catch(SQLException e) {
//			ExceptionHandler.error(e);
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch(SQLException e2) {
//					ExceptionHandler.warn(e2);
//				}
//			}
//		}
//		return result.toString();
		return "";
	}
	
	public String getLineComment() {
		return "--";
	}
}
