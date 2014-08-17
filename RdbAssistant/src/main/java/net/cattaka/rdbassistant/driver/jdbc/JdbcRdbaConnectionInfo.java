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
 * $Id: SqliteRdbaConnectionInfo.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.driver.jdbc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.config.RdbaJdbcBundle;
import net.cattaka.rdbassistant.config.RdbaJdbcBundle.MyURLClassLoader;
import net.cattaka.rdbassistant.core.RdbaException;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.connectioninfo.RdbaConnectionInfoEditor;
import net.cattaka.rdbassistant.util.ConnectionWrapper;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class JdbcRdbaConnectionInfo implements RdbaConnectionInfo {
	private static final long serialVersionUID = 1L;
	
	private String label = "";
	private String driverFile = "";
	private String driverClassName = "";
	private String url = "";
	private String userName = "";
	private String password = "";
	
	public String getRdbmsName() {
		return "JDBC";
	}
	
	public String toUrl() {
		return url;
	}
		public RdbaConnection createConnection(RdbaJdbcBundle rdbaJdbcBundle) throws RdbaException {
		// JDBCのJarのURLを取得
		URL jdbcUrl = null;
		try {
			jdbcUrl = new URL(this.driverFile);
		} catch (MalformedURLException e) {
			// URLでない場合はファイルとして試みる
			File jdbcFile = new File(this.driverFile);
			if (jdbcFile.exists()) {
				try {
					jdbcUrl = jdbcFile.toURI().toURL();
				} catch (MalformedURLException e2) {
					// 面倒見切れん
				}
			}
		}
		if (jdbcUrl == null) {
			// URLが見つからないので諦め
			throw new RdbaException(MessageBundle.getInstance().getMessage("jdbc_driver_file_not_found"));
		}
		
		// Jarファイルをロード
		MyURLClassLoader urlClassLoader = new MyURLClassLoader(new URL[]{jdbcUrl});
		urlClassLoader.getExtraPermission().add(new SocketPermission("*", "connect,resolve"));
		Class<?> driverClass;
		try {
			driverClass =  urlClassLoader.loadClass(this.driverClassName);
		} catch (ClassNotFoundException e) {
			throw new RdbaException(MessageBundle.getInstance().getMessage("jdbc_class_not_found"));
		}
		Driver driver = null;
		try {
			driver = (Driver)driverClass.newInstance();
		} catch (ClassCastException e) {
			throw new RdbaException(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new RdbaException(e.getMessage(),e);
		} catch (InstantiationException e) {
			throw new RdbaException(e.getMessage(),e);
		}
		
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
		
		JdbcRdbaConnection rdbaConnection = new JdbcRdbaConnection(new ConnectionWrapper(conn), userName);
		return rdbaConnection;
	}

	public String[] getDisplayStrings() {
		return new String[]{"JDBC",label, url,userName};
	}

	public String getTooltipText() {
		return String.format("%s %s", userName, url);
	}

	public RdbaConnectionInfoEditor createEditor() {
		return new JdbcRdbaConnectionInfoEditor();
	}

	public String[] toStringArray() {
		String[] result = {
			getClass().getCanonicalName(),
			"1",
			label,
			driverFile,
			driverClassName,
			url,
			userName,
			password
		};
		return result;
	}
	
	public boolean restoreStringArray(String[] stringArray, int configRevision) {
		boolean result = false;
		if (stringArray[1].equals("1") && stringArray.length == 8) {
			label = stringArray[2];
			driverFile = stringArray[3];
			driverClassName = stringArray[4];
			url = stringArray[5];
			userName = stringArray[6];
			password = stringArray[7];
			result = true;
		}
		return result;
	}

	public JdbcRdbaConnectionInfo createClone() {
		try {
			return (JdbcRdbaConnectionInfo)this.clone();
		} catch (CloneNotSupportedException e) {
			ExceptionHandler.error(e);
			return new JdbcRdbaConnectionInfo();
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDriverFile() {
		return driverFile;
	}

	public String getUrl() {
		return url;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setDriverFile(String driverFile) {
		this.driverFile = driverFile;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
}
