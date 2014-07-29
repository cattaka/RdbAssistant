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
 * $Id: SqliteRdbaConnectionInfo.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.rdbassistant.driver.sqlite;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import net.cattaka.rdbassistant.RdbaConfigConstants;
import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.config.RdbaJdbcBundle;
import net.cattaka.rdbassistant.core.RdbaException;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.connectioninfo.RdbaConnectionInfoEditor;
import net.cattaka.rdbassistant.util.ConnectionWrapper;
import net.cattaka.util.ExceptionHandler;

public class SqliteRdbaConnectionInfo implements RdbaConnectionInfo {
	private static final long serialVersionUID = 1L;
	
	private String label = "";
	private String database = "";
	
	public String getRdbmsName() {
		return "Sqlite";
	}
	
	public String toUrl() {
		return String.format("jdbc:sqlite:%s", database);
	}
	
	public RdbaConnection createConnection(RdbaJdbcBundle rdbaJdbcBundle) throws RdbaException {
		Driver driver = null;
		driver = rdbaJdbcBundle.getDriver(RdbaConfigConstants.DRIVER_JDBC_SQLITE, "org.sqlite.JDBC");
		
		Connection conn = null;
		try {
			Properties info = new Properties();
			conn = driver.connect(toUrl(), info);
		} catch (SQLException e) {
			throw new RdbaException(e.getMessage(),e);
		}
		try {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		} catch (SQLException e) {
			ExceptionHandler.warn(e);
		}
		
		SqliteRdbaConnection rdbaConnection = new SqliteRdbaConnection(new ConnectionWrapper(conn), "main");
		
		return rdbaConnection;
	}

	public String[] getDisplayStrings() {
		return new String[]{"SQLite",label, database,""};
	}

	public String getTooltipText() {
		return String.format("%s", database);
	}

	public RdbaConnectionInfoEditor createEditor() {
		return new SqliteRdbaConnectionInfoEditor();
	}

	public String[] toStringArray() {
		String[] result = {
			getClass().getCanonicalName(),
			"2",
			label,
			database
		};
		return result;
	}
	
	public boolean restoreStringArray(String[] stringArray, int configRevision) {
		boolean result = false;
		if (stringArray[1].equals("1")) {
			result = false;
		} else if (stringArray[1].equals("2") && stringArray.length == 4) {
			label = stringArray[2];
			database = stringArray[3];
			result = true;
		}
		return result;
	}

	public SqliteRdbaConnectionInfo createClone() {
		try {
			return (SqliteRdbaConnectionInfo)this.clone();
		} catch (CloneNotSupportedException e) {
			ExceptionHandler.error(e);
			return new SqliteRdbaConnectionInfo();
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
}
