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
 * $Id: SqliteRdbaConnectionInfo.java 271 2010-03-02 13:40:36Z cattaka $
 */
package net.cattaka.rdbassistant.driver.telnetsqlite;

import java.sql.SQLException;

import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.config.RdbaJdbcBundle;
import net.cattaka.rdbassistant.core.RdbaException;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.driver.telnetsqlite.jdbc.TelnetSqliteConnection;
import net.cattaka.rdbassistant.gui.connectioninfo.RdbaConnectionInfoEditor;
import net.cattaka.rdbassistant.util.ConnectionWrapper;
import net.cattaka.util.ExceptionHandler;

public class TelnetSqliteRdbaConnectionInfo implements RdbaConnectionInfo {
	private static final long serialVersionUID = 1L;
	
	private String label = "";
	private String hostname = "";
	private int port = 0;
	private String database = "";
	
	public String getRdbmsName() {
		return "TelnetSqlite";
	}
	
	public String toUrl() {
		return String.format("jdbc:telnetsqlite:%s:port/%s", hostname, port, database);
	}
	
	public RdbaConnection createConnection(RdbaJdbcBundle rdbaJdbcBundle) throws RdbaException {
		TelnetSqliteConnection connection = new TelnetSqliteConnection(hostname, port, database);
		TelnetSqliteRdbaConnection rdbaConnection = new TelnetSqliteRdbaConnection(new ConnectionWrapper(connection), "main");
		try {
			connection.prepareConection();
		} catch (SQLException e) {
			throw new RdbaException(e);
		}
		
		return rdbaConnection;
	}

	public String[] getDisplayStrings() {
		return new String[]{"TelnetSQLite",label, database,""};
	}

	public String getTooltipText() {
		return String.format("%s:%d/%s", hostname, port, database);
	}

	public RdbaConnectionInfoEditor createEditor() {
		return new TelnetSqliteRdbaConnectionInfoEditor();
	}

	public String[] toStringArray() {
		String[] result = {
			getClass().getCanonicalName(),
			"2",
			label,
			hostname,
			String.valueOf(port),
			database
		};
		return result;
	}
	
	public boolean restoreStringArray(String[] stringArray, int configRevision) {
		boolean result = false;
		if (stringArray[1].equals("1")) {
			result = false;
		} else if (stringArray[1].equals("2") && stringArray.length == 6) {
			label = stringArray[2];
			hostname = stringArray[3];
			try {
				port = Integer.parseInt(stringArray[4]);
			} catch (NumberFormatException e) {
				ExceptionHandler.debug(e);
			}
			database = stringArray[5];
			result = true;
		}
		return result;
	}

	public TelnetSqliteRdbaConnectionInfo createClone() {
		try {
			return (TelnetSqliteRdbaConnectionInfo)this.clone();
		} catch (CloneNotSupportedException e) {
			ExceptionHandler.error(e);
			return new TelnetSqliteRdbaConnectionInfo();
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
}
