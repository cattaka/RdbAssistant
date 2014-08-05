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
 * $Id: OracleRdbaConnectionInfo.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.rdbassistant.driver.oracle;

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

public class OracleRdbaConnectionInfo implements RdbaConnectionInfo {
	private static final long serialVersionUID = 1L;
	
	private String host = "";
	private Integer port = new Integer(1521);
	private String sid = "";
	private String userName = "";
	private String password = "";
	private String label = "";
	
	public String getRdbmsName() {
		return "Oracle";
	}

	public String toUrl() {
		return String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, sid);
	}
	
	public RdbaConnection createConnection(RdbaJdbcBundle rdbaJdbcBundle) throws RdbaException {
		Driver driver = null;
		driver = rdbaJdbcBundle.getDriver(RdbaConfigConstants.DRIVER_JDBC_ORACLE, "oracle.jdbc.driver.OracleDriver");
		
		Connection conn = null;
		try {
			Properties info = new Properties();
			info.setProperty("user", userName);
			info.setProperty("password", password);
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
		
		OracleRdbaConnection rdbaConnection = new OracleRdbaConnection(new ConnectionWrapper(conn), userName);
		
		return rdbaConnection;
	}

	public String[] getDisplayStrings() {
		return new String[]{"Oracle",label, host+":"+sid,userName};
	}

	public String getTooltipText() {
		return String.format("%s@%s:%d:%s", userName, host, port, sid);
	}

	public RdbaConnectionInfoEditor createEditor() {
		return new OracleRdbaConnectionInfoEditor();
	}

	public String[] toStringArray() {
		String[] result = {
			getClass().getCanonicalName(),
			"2",
			label,
			host,
			port.toString(),
			sid,
			userName,
			password,
		};
		return result;
	}
	
	public boolean restoreStringArray(String[] stringArray, int configRevision) {
		boolean result = false;
		if (stringArray[1].equals("1") && stringArray.length == 7) {
			host = stringArray[2];
			port = Integer.parseInt(stringArray[3]);
			sid = stringArray[4];
			userName = stringArray[5];
			password = stringArray[6];
			label = host+":"+sid;
			result = true;
		} else if (stringArray[1].equals("2") && stringArray.length == 8) {
			label = stringArray[2];
			host = stringArray[3];
			port = Integer.parseInt(stringArray[4]);
			sid = stringArray[5];
			userName = stringArray[6];
			password = stringArray[7];
			result = true;
		}
		return result;
	}

	public OracleRdbaConnectionInfo createClone() {
		try {
			return (OracleRdbaConnectionInfo)this.clone();
		} catch (CloneNotSupportedException e) {
			ExceptionHandler.error(e);
			return new OracleRdbaConnectionInfo();
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
