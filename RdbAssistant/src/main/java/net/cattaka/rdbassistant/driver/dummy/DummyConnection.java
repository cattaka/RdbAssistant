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
 * $Id: DummyConnection.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.rdbassistant.driver.dummy;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import net.cattaka.util.MessageBundle;

public class DummyConnection implements Connection {

	public void clearWarnings() throws SQLException {
		// なし
	}

	public void close() throws SQLException {
		// なし
	}

	public void commit() throws SQLException {
		// なし
	}

	public Statement createStatement() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public boolean getAutoCommit() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public String getCatalog() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public int getHoldability() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public int getTransactionIsolation() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public SQLWarning getWarnings() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public boolean isClosed() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public boolean isReadOnly() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public String nativeSQL(String sql) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public void rollback() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public void setCatalog(String catalog) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public void setHoldability(int holdability) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public Savepoint setSavepoint() throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public void setTransactionIsolation(int level) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		throw new SQLException(MessageBundle.getInstance().getMessage("not_supported_by_dummy"));
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void abort(Executor arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getClientInfo(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isValid(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setClientInfo(Properties arg0) throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	public void setClientInfo(String arg0, String arg1)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setSchema(String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

}
