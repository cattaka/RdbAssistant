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
package net.cattaka.rdbassistant.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionWrapper {
	private Connection connection;

	public ConnectionWrapper(Connection connection) {
		super();
		this.connection = connection;
	}

//	public void clearWarnings() throws SQLException {
//		connection.clearWarnings();
//	}

	public void close() throws SQLException {
		connection.close();
	}

//	public void commit() throws SQLException {
//		connection.commit();
//	}
//
//	public Array createArrayOf(String paramString, Object[] paramArrayOfObject)
//			throws SQLException {
//		return connection.createArrayOf(paramString, paramArrayOfObject);
//	}
//
//	public Blob createBlob() throws SQLException {
//		return connection.createBlob();
//	}
//
//	public Clob createClob() throws SQLException {
//		return connection.createClob();
//	}
//
//	public NClob createNClob() throws SQLException {
//		return connection.createNClob();
//	}
//
//	public SQLXML createSQLXML() throws SQLException {
//		return connection.createSQLXML();
//	}
//
//	public Statement createStatement() throws SQLException {
//		return connection.createStatement();
//	}
//
//	public Statement createStatement(int paramInt1, int paramInt2, int paramInt3)
//			throws SQLException {
//		return connection.createStatement(paramInt1, paramInt2, paramInt3);
//	}
//
//	public Statement createStatement(int paramInt1, int paramInt2)
//			throws SQLException {
//		return connection.createStatement(paramInt1, paramInt2);
//	}
//
//	public Struct createStruct(String paramString, Object[] paramArrayOfObject)
//			throws SQLException {
//		return connection.createStruct(paramString, paramArrayOfObject);
//	}
//
//	public boolean getAutoCommit() throws SQLException {
//		return connection.getAutoCommit();
//	}
//
//	public String getCatalog() throws SQLException {
//		return connection.getCatalog();
//	}
//
//	public Properties getClientInfo() throws SQLException {
//		return connection.getClientInfo();
//	}
//
//	public String getClientInfo(String paramString) throws SQLException {
//		return connection.getClientInfo(paramString);
//	}
//
//	public int getHoldability() throws SQLException {
//		return connection.getHoldability();
//	}
//
	public DatabaseMetaData getMetaData() throws SQLException {
		return connection.getMetaData();
	}
//
//	public int getTransactionIsolation() throws SQLException {
//		return connection.getTransactionIsolation();
//	}
//
//	public Map<String, Class<?>> getTypeMap() throws SQLException {
//		return connection.getTypeMap();
//	}
//
//	public SQLWarning getWarnings() throws SQLException {
//		return connection.getWarnings();
//	}
//
//	public boolean isClosed() throws SQLException {
//		return connection.isClosed();
//	}
//
//	public boolean isReadOnly() throws SQLException {
//		return connection.isReadOnly();
//	}
//
//	public boolean isValid(int paramInt) throws SQLException {
//		return connection.isValid(paramInt);
//	}
//
//	public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
//		return connection.isWrapperFor(paramClass);
//	}
//
//	public String nativeSQL(String paramString) throws SQLException {
//		return connection.nativeSQL(paramString);
//	}
//
//	public CallableStatement prepareCall(String paramString, int paramInt1,
//			int paramInt2, int paramInt3) throws SQLException {
//		return connection.prepareCall(paramString, paramInt1, paramInt2,
//				paramInt3);
//	}
//
//	public CallableStatement prepareCall(String paramString, int paramInt1,
//			int paramInt2) throws SQLException {
//		return connection.prepareCall(paramString, paramInt1, paramInt2);
//	}
//
	public CallableStatement prepareCall(String paramString)
			throws SQLException {
		return connection.prepareCall(paramString);
	}
//
//	public PreparedStatement prepareStatement(String paramString,
//			int paramInt1, int paramInt2, int paramInt3) throws SQLException {
//		return connection.prepareStatement(paramString, paramInt1, paramInt2,
//				paramInt3);
//	}
//
	public PreparedStatement prepareStatement(String paramString,
			int paramInt1, int paramInt2) throws SQLException {
		return connection.prepareStatement(paramString, paramInt1, paramInt2);
	}
//
//	public PreparedStatement prepareStatement(String paramString, int paramInt)
//			throws SQLException {
//		return connection.prepareStatement(paramString, paramInt);
//	}
//
//	public PreparedStatement prepareStatement(String paramString,
//			int[] paramArrayOfInt) throws SQLException {
//		return connection.prepareStatement(paramString, paramArrayOfInt);
//	}
//
//	public PreparedStatement prepareStatement(String paramString,
//			String[] paramArrayOfString) throws SQLException {
//		return connection.prepareStatement(paramString, paramArrayOfString);
//	}
//
	public PreparedStatement prepareStatement(String paramString) throws SQLException {
		return connection.prepareStatement(paramString);
	}
//
//	public void releaseSavepoint(Savepoint paramSavepoint) throws SQLException {
//		connection.releaseSavepoint(paramSavepoint);
//	}
//
//	public void rollback() throws SQLException {
//		connection.rollback();
//	}
//
//	public void rollback(Savepoint paramSavepoint) throws SQLException {
//		connection.rollback(paramSavepoint);
//	}
//
//	public void setAutoCommit(boolean paramBoolean) throws SQLException {
//		connection.setAutoCommit(paramBoolean);
//	}
//
//	public void setCatalog(String paramString) throws SQLException {
//		connection.setCatalog(paramString);
//	}
//
//	public void setClientInfo(Properties paramProperties)
//			throws SQLClientInfoException {
//		connection.setClientInfo(paramProperties);
//	}
//
//	public void setClientInfo(String paramString1, String paramString2)
//			throws SQLClientInfoException {
//		connection.setClientInfo(paramString1, paramString2);
//	}
//
//	public void setHoldability(int paramInt) throws SQLException {
//		connection.setHoldability(paramInt);
//	}
//
//	public void setReadOnly(boolean paramBoolean) throws SQLException {
//		connection.setReadOnly(paramBoolean);
//	}
//
//	public Savepoint setSavepoint() throws SQLException {
//		return connection.setSavepoint();
//	}
//
//	public Savepoint setSavepoint(String paramString) throws SQLException {
//		return connection.setSavepoint(paramString);
//	}
//
//	public void setTransactionIsolation(int paramInt) throws SQLException {
//		connection.setTransactionIsolation(paramInt);
//	}
//
//	public void setTypeMap(Map<String, Class<?>> paramMap) throws SQLException {
//		connection.setTypeMap(paramMap);
//	}
//
//	public <T> T unwrap(Class<T> paramClass) throws SQLException {
//		return connection.unwrap(paramClass);
//	}
}
