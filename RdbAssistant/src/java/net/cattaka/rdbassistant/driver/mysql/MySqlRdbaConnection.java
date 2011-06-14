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
 * $Id: MySqlRdbaConnection.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.rdbassistant.driver.mysql;

import java.beans.XMLDecoder;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;


import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.core.SqlEditorSelection;
import net.cattaka.rdbassistant.sql.Other;
import net.cattaka.rdbassistant.util.ConnectionWrapper;
import net.cattaka.swing.text.StdStyledDocument;
import net.cattaka.util.ExceptionHandler;

/**
 * 作成にはRdbConnectionFactoryを使用すること。
 * 
 * @author cattaka
 */
public class MySqlRdbaConnection implements RdbaConnection {
	private ConnectionWrapper connection;
	private MySqlSqlEditorSelection myEditorSelection;
	private String defaultDatabase;
	private static HashSet<String> reservedWords = null;
	
	MySqlRdbaConnection(ConnectionWrapper connection, String defaultDatabase) {
		this.connection = connection;
		this.defaultDatabase = defaultDatabase;
		myEditorSelection = new MySqlSqlEditorSelection(this);
		
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
		switch(columnType) {
		case Types.BIT:
		case Types.BOOLEAN:
		case Types.CHAR:
		case Types.LONGVARCHAR:
		case Types.NULL:
		case Types.VARCHAR:
			result = String.class;
			break;
		case Types.TIME:
			result = Time.class;
			break;
		case Types.DATE:
			result = Date.class;
			break;
		case Types.TIMESTAMP:
			result = java.util.Date.class;
			break;
		case Types.BIGINT:
		case Types.DECIMAL:
		case Types.INTEGER:
		case Types.TINYINT:
		case Types.NUMERIC:
		case Types.SMALLINT:
			result = BigDecimal.class;
			break;
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.REAL:
			result = String.class;
			break;
		case Types.ARRAY:
		case Types.BINARY:
		case Types.BLOB:
		case Types.CLOB:
		case Types.DATALINK:
		case Types.DISTINCT:
		case Types.JAVA_OBJECT:
		case Types.LONGVARBINARY:
		case Types.OTHER:
		case Types.REF:
		case Types.STRUCT:
		case Types.VARBINARY:
		default:
			result = Other.class;;
			break;
		}
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
			return resultSet.getDate(columnIndex);
		} else {
			return null;
		}
	}

	public StdStyledDocument createNewSqlDocument() {
		MySqlStyledDocument styledDocument = new MySqlStyledDocument();
		styledDocument.setReservedWords(this.getReservedWords());
		return styledDocument;
	}
		
	private Set<String> getReservedWords() {
		return reservedWords;
	}
	
	@SuppressWarnings("unchecked")
	private static void createReservedWords() {
		try {
			InputStream in = MySqlRdbaConnection.class.getClassLoader().getResourceAsStream(RdbaConstants.MYSQL_RESERVED_WORDS_FILE);
			XMLDecoder dec = new XMLDecoder(in);
			reservedWords = (HashSet<String>)dec.readObject();
			in.close();
		} catch(Exception e) {
			ExceptionHandler.error(e);
		}
	}
	
	public String getLastDbmsOutput() {
		return "";
	}

	public String getLineComment() {
		return "-- ";
	}
}
