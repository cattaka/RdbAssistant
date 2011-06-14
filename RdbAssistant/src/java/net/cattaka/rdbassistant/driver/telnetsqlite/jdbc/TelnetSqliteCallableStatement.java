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
package net.cattaka.rdbassistant.driver.telnetsqlite.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class TelnetSqliteCallableStatement implements CallableStatement {

	public Array getArray(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Array getArray(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public BigDecimal getBigDecimal(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public BigDecimal getBigDecimal(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public BigDecimal getBigDecimal(int paramInt1, int paramInt2)
			throws SQLException {
		// 不要
		return null;
	}

	public Blob getBlob(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Blob getBlob(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public boolean getBoolean(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean getBoolean(String paramString) throws SQLException {
		// 不要
		return false;
	}

	public byte getByte(int paramInt) throws SQLException {
		// 不要
		return 0;
	}

	public byte getByte(String paramString) throws SQLException {
		// 不要
		return 0;
	}

	public byte[] getBytes(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public byte[] getBytes(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public Reader getCharacterStream(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Reader getCharacterStream(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public Clob getClob(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Clob getClob(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public Date getDate(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Date getDate(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public Date getDate(int paramInt, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public Date getDate(String paramString, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public double getDouble(int paramInt) throws SQLException {
		// 不要
		return 0;
	}

	public double getDouble(String paramString) throws SQLException {
		// 不要
		return 0;
	}

	public float getFloat(int paramInt) throws SQLException {
		// 不要
		return 0;
	}

	public float getFloat(String paramString) throws SQLException {
		// 不要
		return 0;
	}

	public int getInt(int paramInt) throws SQLException {
		// 不要
		return 0;
	}

	public int getInt(String paramString) throws SQLException {
		// 不要
		return 0;
	}

	public long getLong(int paramInt) throws SQLException {
		// 不要
		return 0;
	}

	public long getLong(String paramString) throws SQLException {
		// 不要
		return 0;
	}

	public Reader getNCharacterStream(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Reader getNCharacterStream(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public String getNString(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public String getNString(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public Object getObject(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Object getObject(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public Object getObject(int paramInt, Map<String, Class<?>> paramMap)
			throws SQLException {
		// 不要
		return null;
	}

	public Object getObject(String paramString, Map<String, Class<?>> paramMap)
			throws SQLException {
		// 不要
		return null;
	}

	public Ref getRef(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Ref getRef(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public short getShort(int paramInt) throws SQLException {
		// 不要
		return 0;
	}

	public short getShort(String paramString) throws SQLException {
		// 不要
		return 0;
	}

	public String getString(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public String getString(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public Time getTime(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Time getTime(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public Time getTime(int paramInt, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public Time getTime(String paramString, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public Timestamp getTimestamp(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Timestamp getTimestamp(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public Timestamp getTimestamp(String paramString, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public URL getURL(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public URL getURL(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public void registerOutParameter(int paramInt1, int paramInt2)
			throws SQLException {
		// 不要

	}

	public void registerOutParameter(String paramString, int paramInt)
			throws SQLException {
		// 不要

	}

	public void registerOutParameter(int paramInt1, int paramInt2, int paramInt3)
			throws SQLException {
		// 不要

	}

	public void registerOutParameter(int paramInt1, int paramInt2,
			String paramString) throws SQLException {
		// 不要

	}

	public void registerOutParameter(String paramString, int paramInt1,
			int paramInt2) throws SQLException {
		// 不要

	}

	public void registerOutParameter(String paramString1, int paramInt,
			String paramString2) throws SQLException {
		// 不要

	}

	public void setAsciiStream(String paramString, InputStream paramInputStream)
			throws SQLException {
		// 不要

	}

	public void setAsciiStream(String paramString,
			InputStream paramInputStream, int paramInt) throws SQLException {
		// 不要

	}

	public void setAsciiStream(String paramString,
			InputStream paramInputStream, long paramLong) throws SQLException {
		// 不要

	}

	public void setBigDecimal(String paramString, BigDecimal paramBigDecimal)
			throws SQLException {
		// 不要

	}

	public void setBinaryStream(String paramString, InputStream paramInputStream)
			throws SQLException {
		// 不要

	}

	public void setBinaryStream(String paramString,
			InputStream paramInputStream, int paramInt) throws SQLException {
		// 不要

	}

	public void setBinaryStream(String paramString,
			InputStream paramInputStream, long paramLong) throws SQLException {
		// 不要

	}

	public void setBlob(String paramString, Blob paramBlob) throws SQLException {
		// 不要

	}

	public void setBlob(String paramString, InputStream paramInputStream)
			throws SQLException {
		// 不要

	}

	public void setBlob(String paramString, InputStream paramInputStream,
			long paramLong) throws SQLException {
		// 不要

	}

	public void setBoolean(String paramString, boolean paramBoolean)
			throws SQLException {
		// 不要

	}

	public void setByte(String paramString, byte paramByte) throws SQLException {
		// 不要

	}

	public void setBytes(String paramString, byte[] paramArrayOfByte)
			throws SQLException {
		// 不要

	}

	public void setCharacterStream(String paramString, Reader paramReader)
			throws SQLException {
		// 不要

	}

	public void setCharacterStream(String paramString, Reader paramReader,
			int paramInt) throws SQLException {
		// 不要

	}

	public void setCharacterStream(String paramString, Reader paramReader,
			long paramLong) throws SQLException {
		// 不要

	}

	public void setClob(String paramString, Clob paramClob) throws SQLException {
		// 不要

	}

	public void setClob(String paramString, Reader paramReader)
			throws SQLException {
		// 不要

	}

	public void setClob(String paramString, Reader paramReader, long paramLong)
			throws SQLException {
		// 不要

	}

	public void setDate(String paramString, Date paramDate) throws SQLException {
		// 不要

	}

	public void setDate(String paramString, Date paramDate,
			Calendar paramCalendar) throws SQLException {
		// 不要

	}

	public void setDouble(String paramString, double paramDouble)
			throws SQLException {
		// 不要

	}

	public void setFloat(String paramString, float paramFloat)
			throws SQLException {
		// 不要

	}

	public void setInt(String paramString, int paramInt) throws SQLException {
		// 不要

	}

	public void setLong(String paramString, long paramLong) throws SQLException {
		// 不要

	}

	public void setNCharacterStream(String paramString, Reader paramReader)
			throws SQLException {
		// 不要

	}

	public void setNCharacterStream(String paramString, Reader paramReader,
			long paramLong) throws SQLException {
		// 不要

	}

	public void setNClob(String paramString, Reader paramReader)
			throws SQLException {
		// 不要

	}

	public void setNClob(String paramString, Reader paramReader, long paramLong)
			throws SQLException {
		// 不要

	}

	public void setNString(String paramString1, String paramString2)
			throws SQLException {
		// 不要

	}

	public void setNull(String paramString, int paramInt) throws SQLException {
		// 不要

	}

	public void setNull(String paramString1, int paramInt, String paramString2)
			throws SQLException {
		// 不要

	}

	public void setObject(String paramString, Object paramObject)
			throws SQLException {
		// 不要

	}

	public void setObject(String paramString, Object paramObject, int paramInt)
			throws SQLException {
		// 不要

	}

	public void setObject(String paramString, Object paramObject,
			int paramInt1, int paramInt2) throws SQLException {
		// 不要

	}

	public void setShort(String paramString, short paramShort)
			throws SQLException {
		// 不要

	}

	public void setString(String paramString1, String paramString2)
			throws SQLException {
		// 不要

	}

	public void setTime(String paramString, Time paramTime) throws SQLException {
		// 不要

	}

	public void setTime(String paramString, Time paramTime,
			Calendar paramCalendar) throws SQLException {
		// 不要

	}

	public void setTimestamp(String paramString, Timestamp paramTimestamp)
			throws SQLException {
		// 不要

	}

	public void setTimestamp(String paramString, Timestamp paramTimestamp,
			Calendar paramCalendar) throws SQLException {
		// 不要

	}

	public void setURL(String paramString, URL paramURL) throws SQLException {
		// 不要

	}

	public boolean wasNull() throws SQLException {
		// 不要
		return false;
	}

	public void addBatch() throws SQLException {
		// 不要

	}

	public void clearParameters() throws SQLException {
		// 不要

	}

	public boolean execute() throws SQLException {
		// 不要
		return false;
	}

	public ResultSet executeQuery() throws SQLException {
		// 不要
		return null;
	}

	public int executeUpdate() throws SQLException {
		// 不要
		return 0;
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		// 不要
		return null;
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		// 不要
		return null;
	}

	public void setArray(int paramInt, Array paramArray) throws SQLException {
		// 不要

	}

	public void setAsciiStream(int paramInt, InputStream paramInputStream)
			throws SQLException {
		// 不要

	}

	public void setAsciiStream(int paramInt1, InputStream paramInputStream,
			int paramInt2) throws SQLException {
		// 不要

	}

	public void setAsciiStream(int paramInt, InputStream paramInputStream,
			long paramLong) throws SQLException {
		// 不要

	}

	public void setBigDecimal(int paramInt, BigDecimal paramBigDecimal)
			throws SQLException {
		// 不要

	}

	public void setBinaryStream(int paramInt, InputStream paramInputStream)
			throws SQLException {
		// 不要

	}

	public void setBinaryStream(int paramInt1, InputStream paramInputStream,
			int paramInt2) throws SQLException {
		// 不要

	}

	public void setBinaryStream(int paramInt, InputStream paramInputStream,
			long paramLong) throws SQLException {
		// 不要

	}

	public void setBlob(int paramInt, Blob paramBlob) throws SQLException {
		// 不要

	}

	public void setBlob(int paramInt, InputStream paramInputStream)
			throws SQLException {
		// 不要

	}

	public void setBlob(int paramInt, InputStream paramInputStream,
			long paramLong) throws SQLException {
		// 不要

	}

	public void setBoolean(int paramInt, boolean paramBoolean)
			throws SQLException {
		// 不要

	}

	public void setByte(int paramInt, byte paramByte) throws SQLException {
		// 不要

	}

	public void setBytes(int paramInt, byte[] paramArrayOfByte)
			throws SQLException {
		// 不要

	}

	public void setCharacterStream(int paramInt, Reader paramReader)
			throws SQLException {
		// 不要

	}

	public void setCharacterStream(int paramInt1, Reader paramReader,
			int paramInt2) throws SQLException {
		// 不要

	}

	public void setCharacterStream(int paramInt, Reader paramReader,
			long paramLong) throws SQLException {
		// 不要

	}

	public void setClob(int paramInt, Clob paramClob) throws SQLException {
		// 不要

	}

	public void setClob(int paramInt, Reader paramReader) throws SQLException {
		// 不要

	}

	public void setClob(int paramInt, Reader paramReader, long paramLong)
			throws SQLException {
		// 不要

	}

	public void setDate(int paramInt, Date paramDate) throws SQLException {
		// 不要

	}

	public void setDate(int paramInt, Date paramDate, Calendar paramCalendar)
			throws SQLException {
		// 不要

	}

	public void setDouble(int paramInt, double paramDouble) throws SQLException {
		// 不要

	}

	public void setFloat(int paramInt, float paramFloat) throws SQLException {
		// 不要

	}

	public void setInt(int paramInt1, int paramInt2) throws SQLException {
		// 不要

	}

	public void setLong(int paramInt, long paramLong) throws SQLException {
		// 不要

	}

	public void setNCharacterStream(int paramInt, Reader paramReader)
			throws SQLException {
		// 不要

	}

	public void setNCharacterStream(int paramInt, Reader paramReader,
			long paramLong) throws SQLException {
		// 不要

	}

	public void setNClob(int paramInt, Reader paramReader) throws SQLException {
		// 不要

	}

	public void setNClob(int paramInt, Reader paramReader, long paramLong)
			throws SQLException {
		// 不要

	}

	public void setNString(int paramInt, String paramString)
			throws SQLException {
		// 不要

	}

	public void setNull(int paramInt1, int paramInt2) throws SQLException {
		// 不要

	}

	public void setNull(int paramInt1, int paramInt2, String paramString)
			throws SQLException {
		// 不要

	}

	public void setObject(int paramInt, Object paramObject) throws SQLException {
		// 不要

	}

	public void setObject(int paramInt1, Object paramObject, int paramInt2)
			throws SQLException {
		// 不要

	}

	public void setObject(int paramInt1, Object paramObject, int paramInt2,
			int paramInt3) throws SQLException {
		// 不要

	}

	public void setRef(int paramInt, Ref paramRef) throws SQLException {
		// 不要

	}

	public void setShort(int paramInt, short paramShort) throws SQLException {
		// 不要

	}

	public void setString(int paramInt, String paramString) throws SQLException {
		// 不要

	}

	public void setTime(int paramInt, Time paramTime) throws SQLException {
		// 不要

	}

	public void setTime(int paramInt, Time paramTime, Calendar paramCalendar)
			throws SQLException {
		// 不要

	}

	public void setTimestamp(int paramInt, Timestamp paramTimestamp)
			throws SQLException {
		// 不要

	}

	public void setTimestamp(int paramInt, Timestamp paramTimestamp,
			Calendar paramCalendar) throws SQLException {
		// 不要

	}

	public void setURL(int paramInt, URL paramURL) throws SQLException {
		// 不要

	}

	public void setUnicodeStream(int paramInt1, InputStream paramInputStream,
			int paramInt2) throws SQLException {
		// 不要

	}

	public void addBatch(String paramString) throws SQLException {
		// 不要

	}

	public void cancel() throws SQLException {
		// 不要

	}

	public void clearBatch() throws SQLException {
		// 不要

	}

	public void clearWarnings() throws SQLException {
		// 不要

	}

	public void close() throws SQLException {
		// 不要

	}

	public boolean execute(String paramString) throws SQLException {
		// 不要
		return false;
	}

	public boolean execute(String paramString, int paramInt)
			throws SQLException {
		// 不要
		return false;
	}

	public boolean execute(String paramString, int[] paramArrayOfInt)
			throws SQLException {
		// 不要
		return false;
	}

	public boolean execute(String paramString, String[] paramArrayOfString)
			throws SQLException {
		// 不要
		return false;
	}

	public int[] executeBatch() throws SQLException {
		// 不要
		return null;
	}

	public ResultSet executeQuery(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public int executeUpdate(String paramString) throws SQLException {
		// 不要
		return 0;
	}

	public int executeUpdate(String paramString, int paramInt)
			throws SQLException {
		// 不要
		return 0;
	}

	public int executeUpdate(String paramString, int[] paramArrayOfInt)
			throws SQLException {
		// 不要
		return 0;
	}

	public int executeUpdate(String paramString, String[] paramArrayOfString)
			throws SQLException {
		// 不要
		return 0;
	}

	public Connection getConnection() throws SQLException {
		// 不要
		return null;
	}

	public int getFetchDirection() throws SQLException {
		// 不要
		return 0;
	}

	public int getFetchSize() throws SQLException {
		// 不要
		return 0;
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		// 不要
		return null;
	}

	public int getMaxFieldSize() throws SQLException {
		// 不要
		return 0;
	}

	public int getMaxRows() throws SQLException {
		// 不要
		return 0;
	}

	public boolean getMoreResults() throws SQLException {
		// 不要
		return false;
	}

	public boolean getMoreResults(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public int getQueryTimeout() throws SQLException {
		// 不要
		return 0;
	}

	public ResultSet getResultSet() throws SQLException {
		// 不要
		return null;
	}

	public int getResultSetConcurrency() throws SQLException {
		// 不要
		return 0;
	}

	public int getResultSetHoldability() throws SQLException {
		// 不要
		return 0;
	}

	public int getResultSetType() throws SQLException {
		// 不要
		return 0;
	}

	public int getUpdateCount() throws SQLException {
		// 不要
		return 0;
	}

	public SQLWarning getWarnings() throws SQLException {
		// 不要
		return null;
	}

	public boolean isClosed() throws SQLException {
		// 不要
		return false;
	}

	public boolean isPoolable() throws SQLException {
		// 不要
		return false;
	}

	public void setCursorName(String paramString) throws SQLException {
		// 不要

	}

	public void setEscapeProcessing(boolean paramBoolean) throws SQLException {
		// 不要

	}

	public void setFetchDirection(int paramInt) throws SQLException {
		// 不要

	}

	public void setFetchSize(int paramInt) throws SQLException {
		// 不要

	}

	public void setMaxFieldSize(int paramInt) throws SQLException {
		// 不要

	}

	public void setMaxRows(int paramInt) throws SQLException {
		// 不要

	}

	public void setPoolable(boolean paramBoolean) throws SQLException {
		// 不要

	}

	public void setQueryTimeout(int paramInt) throws SQLException {
		// 不要

	}

	public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
		// 不要
		return false;
	}

	public <T> T unwrap(Class<T> paramClass) throws SQLException {
		// 不要
		return null;
	}

}
