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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import net.cattaka.util.StringUtil;

public class TelnetSqliteResultSet implements java.sql.ResultSet {
	private TelnetSqliteResultSetMetaData metaData;
	private int columnCount;
	private int rowCount;
	private ArrayList<ArrayList<String>> resultRow; 
	private int currentRow; 
	
	private TelnetSqliteResultSet(ArrayList<ArrayList<String>> resultRow, TelnetSqliteResultSetMetaData metaData, int rowCount, int columnCount) {
		super();
		this.resultRow = resultRow;
		this.metaData = metaData;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.currentRow = -1;
	}
	
	public static TelnetSqliteResultSet createResultSet(Socket socket, Reader reader, Writer writer, int rowCount) throws IOException {
		String columnNameListArray = TelnetSqliteConnection.readLine(reader);
		TelnetSqliteResultSetMetaData metaData = null;
		int columnCount = 0;
		ArrayList<ArrayList<String>> resultRow = new ArrayList<ArrayList<String>>();
		if (columnNameListArray.length() > 0 && columnNameListArray.charAt(0) == TelnetSqliteConnection.RESULT_HEADER) {
			String[] columnNameList = StringUtil.split(columnNameListArray.substring(1), ',', '"');
			int[] columnTypeList = new int[columnNameList.length];
			for (int i=0;i<columnTypeList.length;i++) {
				columnTypeList[i] = Types.VARCHAR;
			}
			columnCount = columnNameList.length;
			
			while (true) {
				String line = TelnetSqliteConnection.readLine(reader);
				if (line.length() == 0) {
					// 終了
					break;
				} else if (line.charAt(0) == TelnetSqliteConnection.RESULT_DATA) {
					String[] csv = split(line.substring(1), ',', '"');
					ArrayList<String> row = new ArrayList<String>();
					for (String str: csv) {
						row.add(str);
					}
					while(row.size() < columnCount) {
						row.add(null);
					}
					resultRow.add(row);
				} else {
					throw new IOException(line);
				}
			}
			
			metaData = new TelnetSqliteResultSetMetaData(columnNameList,columnTypeList);
		} else if (columnNameListArray.length() == 0) {
			// INSERT 文なのでResultSetが無い
			return null;
		} else {
			throw new IOException(columnNameListArray);
		}
		
		return new TelnetSqliteResultSet(resultRow, metaData, rowCount, columnCount);
	}
	
	public boolean absolute(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public void afterLast() throws SQLException {
		// 不要
		
	}

	public void beforeFirst() throws SQLException {
		// 不要
		
	}

	public void cancelRowUpdates() throws SQLException {
		// 不要
		
	}

	public void clearWarnings() throws SQLException {
		// 不要
		
	}

	public void close() throws SQLException {
		// 不要
		
	}

	public void deleteRow() throws SQLException {
		// 不要
		
	}

	public int findColumn(String paramString) throws SQLException {
		// 不要
		return 0;
	}

	public boolean first() throws SQLException {
		// 不要
		return false;
	}

	public Array getArray(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Array getArray(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public InputStream getAsciiStream(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public InputStream getAsciiStream(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public BigDecimal getBigDecimal(int paramInt1, int paramInt2)
			throws SQLException {
		// 不要
		return null;
	}

	public BigDecimal getBigDecimal(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public BigDecimal getBigDecimal(String paramString, int paramInt)
			throws SQLException {
		// 不要
		return null;
	}

	public BigDecimal getBigDecimal(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public InputStream getBinaryStream(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public InputStream getBinaryStream(String paramString) throws SQLException {
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

	public int getConcurrency() throws SQLException {
		// 不要
		return 0;
	}

	public String getCursorName() throws SQLException {
		// 不要
		return null;
	}

	public Date getDate(int paramInt, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public Date getDate(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Date getDate(String paramString, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public Date getDate(String paramString) throws SQLException {
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

	public int getFetchDirection() throws SQLException {
		// 不要
		return 0;
	}

	public int getFetchSize() throws SQLException {
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

	public int getHoldability() throws SQLException {
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

	public ResultSetMetaData getMetaData() throws SQLException {
		return this.metaData;
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

	public Object getObject(int paramInt, Map<String, Class<?>> paramMap)
			throws SQLException {
		// 不要
		return null;
	}

	public Object getObject(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Object getObject(String paramString, Map<String, Class<?>> paramMap)
			throws SQLException {
		// 不要
		return null;
	}

	public Object getObject(String paramString) throws SQLException {
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

	public int getRow() throws SQLException {
		// 不要
		return 0;
	}

	public short getShort(int paramInt) throws SQLException {
		// 不要
		return 0;
	}

	public short getShort(String paramString) throws SQLException {
		// 不要
		return 0;
	}

	public Statement getStatement() throws SQLException {
		// 不要
		return null;
	}

	public String getString(int paramInt) throws SQLException {
		return (0 < paramInt &&paramInt <= columnCount) ? resultRow.get(currentRow).get(paramInt-1) : null;
	}

	public String getString(String paramString) throws SQLException {
		int paramInt = metaData.getColumnIndex(paramString);
		return getString(paramInt);
	}

	public Time getTime(int paramInt, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public Time getTime(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Time getTime(String paramString, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public Time getTime(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public Timestamp getTimestamp(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public Timestamp getTimestamp(String paramString, Calendar paramCalendar)
			throws SQLException {
		// 不要
		return null;
	}

	public Timestamp getTimestamp(String paramString) throws SQLException {
		// 不要
		return null;
	}

	public int getType() throws SQLException {
		// 不要
		return 0;
	}

	public InputStream getUnicodeStream(int paramInt) throws SQLException {
		// 不要
		return null;
	}

	public InputStream getUnicodeStream(String paramString) throws SQLException {
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

	public SQLWarning getWarnings() throws SQLException {
		// 不要
		return null;
	}

	public void insertRow() throws SQLException {
		// 不要
		
	}

	public boolean isAfterLast() throws SQLException {
		// 不要
		return false;
	}

	public boolean isBeforeFirst() throws SQLException {
		// 不要
		return false;
	}

	public boolean isClosed() throws SQLException {
		// 不要
		return false;
	}

	public boolean isFirst() throws SQLException {
		// 不要
		return false;
	}

	public boolean isLast() throws SQLException {
		// 不要
		return false;
	}

	public boolean last() throws SQLException {
		// 不要
		return false;
	}

	public void moveToCurrentRow() throws SQLException {
		// 不要
		
	}

	public void moveToInsertRow() throws SQLException {
		// 不要
		
	}

	public boolean next() throws SQLException {
		currentRow++;
		return (currentRow < this.rowCount);
	}

	public boolean previous() throws SQLException {
		// 不要
		return false;
	}

	public void refreshRow() throws SQLException {
		// 不要
		
	}

	public boolean relative(int paramInt) throws SQLException {
		// 不要
		return false;
	}

	public boolean rowDeleted() throws SQLException {
		// 不要
		return false;
	}

	public boolean rowInserted() throws SQLException {
		// 不要
		return false;
	}

	public boolean rowUpdated() throws SQLException {
		// 不要
		return false;
	}

	public void setFetchDirection(int paramInt) throws SQLException {
		// 不要
		
	}

	public void setFetchSize(int paramInt) throws SQLException {
		// 不要
		
	}

	public void updateArray(int paramInt, Array paramArray) throws SQLException {
		// 不要
		
	}

	public void updateArray(String paramString, Array paramArray)
			throws SQLException {
		// 不要
		
	}

	public void updateAsciiStream(int paramInt1, InputStream paramInputStream,
			int paramInt2) throws SQLException {
		// 不要
		
	}

	public void updateAsciiStream(int paramInt, InputStream paramInputStream,
			long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateAsciiStream(int paramInt, InputStream paramInputStream)
			throws SQLException {
		// 不要
		
	}

	public void updateAsciiStream(String paramString,
			InputStream paramInputStream, int paramInt) throws SQLException {
		// 不要
		
	}

	public void updateAsciiStream(String paramString,
			InputStream paramInputStream, long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateAsciiStream(String paramString,
			InputStream paramInputStream) throws SQLException {
		// 不要
		
	}

	public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal)
			throws SQLException {
		// 不要
		
	}

	public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal)
			throws SQLException {
		// 不要
		
	}

	public void updateBinaryStream(int paramInt1, InputStream paramInputStream,
			int paramInt2) throws SQLException {
		// 不要
		
	}

	public void updateBinaryStream(int paramInt, InputStream paramInputStream,
			long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateBinaryStream(int paramInt, InputStream paramInputStream)
			throws SQLException {
		// 不要
		
	}

	public void updateBinaryStream(String paramString,
			InputStream paramInputStream, int paramInt) throws SQLException {
		// 不要
		
	}

	public void updateBinaryStream(String paramString,
			InputStream paramInputStream, long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateBinaryStream(String paramString,
			InputStream paramInputStream) throws SQLException {
		// 不要
		
	}

	public void updateBlob(int paramInt, Blob paramBlob) throws SQLException {
		// 不要
		
	}

	public void updateBlob(int paramInt, InputStream paramInputStream,
			long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateBlob(int paramInt, InputStream paramInputStream)
			throws SQLException {
		// 不要
		
	}

	public void updateBlob(String paramString, Blob paramBlob)
			throws SQLException {
		// 不要
		
	}

	public void updateBlob(String paramString, InputStream paramInputStream,
			long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateBlob(String paramString, InputStream paramInputStream)
			throws SQLException {
		// 不要
		
	}

	public void updateBoolean(int paramInt, boolean paramBoolean)
			throws SQLException {
		// 不要
		
	}

	public void updateBoolean(String paramString, boolean paramBoolean)
			throws SQLException {
		// 不要
		
	}

	public void updateByte(int paramInt, byte paramByte) throws SQLException {
		// 不要
		
	}

	public void updateByte(String paramString, byte paramByte)
			throws SQLException {
		// 不要
		
	}

	public void updateBytes(int paramInt, byte[] paramArrayOfByte)
			throws SQLException {
		// 不要
		
	}

	public void updateBytes(String paramString, byte[] paramArrayOfByte)
			throws SQLException {
		// 不要
		
	}

	public void updateCharacterStream(int paramInt1, Reader paramReader,
			int paramInt2) throws SQLException {
		// 不要
		
	}

	public void updateCharacterStream(int paramInt, Reader paramReader,
			long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateCharacterStream(int paramInt, Reader paramReader)
			throws SQLException {
		// 不要
		
	}

	public void updateCharacterStream(String paramString, Reader paramReader,
			int paramInt) throws SQLException {
		// 不要
		
	}

	public void updateCharacterStream(String paramString, Reader paramReader,
			long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateCharacterStream(String paramString, Reader paramReader)
			throws SQLException {
		// 不要
		
	}

	public void updateClob(int paramInt, Clob paramClob) throws SQLException {
		// 不要
		
	}

	public void updateClob(int paramInt, Reader paramReader, long paramLong)
			throws SQLException {
		// 不要
		
	}

	public void updateClob(int paramInt, Reader paramReader)
			throws SQLException {
		// 不要
		
	}

	public void updateClob(String paramString, Clob paramClob)
			throws SQLException {
		// 不要
		
	}

	public void updateClob(String paramString, Reader paramReader,
			long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateClob(String paramString, Reader paramReader)
			throws SQLException {
		// 不要
		
	}

	public void updateDate(int paramInt, Date paramDate) throws SQLException {
		// 不要
		
	}

	public void updateDate(String paramString, Date paramDate)
			throws SQLException {
		// 不要
		
	}

	public void updateDouble(int paramInt, double paramDouble)
			throws SQLException {
		// 不要
		
	}

	public void updateDouble(String paramString, double paramDouble)
			throws SQLException {
		// 不要
		
	}

	public void updateFloat(int paramInt, float paramFloat) throws SQLException {
		// 不要
		
	}

	public void updateFloat(String paramString, float paramFloat)
			throws SQLException {
		// 不要
		
	}

	public void updateInt(int paramInt1, int paramInt2) throws SQLException {
		// 不要
		
	}

	public void updateInt(String paramString, int paramInt) throws SQLException {
		// 不要
		
	}

	public void updateLong(int paramInt, long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateLong(String paramString, long paramLong)
			throws SQLException {
		// 不要
		
	}

	public void updateNCharacterStream(int paramInt, Reader paramReader,
			long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateNCharacterStream(int paramInt, Reader paramReader)
			throws SQLException {
		// 不要
		
	}

	public void updateNCharacterStream(String paramString, Reader paramReader,
			long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateNCharacterStream(String paramString, Reader paramReader)
			throws SQLException {
		// 不要
		
	}

	public void updateNClob(int paramInt, Reader paramReader, long paramLong)
			throws SQLException {
		// 不要
		
	}

	public void updateNClob(int paramInt, Reader paramReader)
			throws SQLException {
		// 不要
		
	}

	public void updateNClob(String paramString, Reader paramReader,
			long paramLong) throws SQLException {
		// 不要
		
	}

	public void updateNClob(String paramString, Reader paramReader)
			throws SQLException {
		// 不要
		
	}

	public void updateNString(int paramInt, String paramString)
			throws SQLException {
		// 不要
		
	}

	public void updateNString(String paramString1, String paramString2)
			throws SQLException {
		// 不要
		
	}

	public void updateNull(int paramInt) throws SQLException {
		// 不要
		
	}

	public void updateNull(String paramString) throws SQLException {
		// 不要
		
	}

	public void updateObject(int paramInt1, Object paramObject, int paramInt2)
			throws SQLException {
		// 不要
		
	}

	public void updateObject(int paramInt, Object paramObject)
			throws SQLException {
		// 不要
		
	}

	public void updateObject(String paramString, Object paramObject,
			int paramInt) throws SQLException {
		// 不要
		
	}

	public void updateObject(String paramString, Object paramObject)
			throws SQLException {
		// 不要
		
	}

	public void updateRef(int paramInt, Ref paramRef) throws SQLException {
		// 不要
		
	}

	public void updateRef(String paramString, Ref paramRef) throws SQLException {
		// 不要
		
	}

	public void updateRow() throws SQLException {
		// 不要
		
	}

	public void updateShort(int paramInt, short paramShort) throws SQLException {
		// 不要
		
	}

	public void updateShort(String paramString, short paramShort)
			throws SQLException {
		// 不要
		
	}

	public void updateString(int paramInt, String paramString)
			throws SQLException {
		// 不要
		
	}

	public void updateString(String paramString1, String paramString2)
			throws SQLException {
		// 不要
		
	}

	public void updateTime(int paramInt, Time paramTime) throws SQLException {
		// 不要
		
	}

	public void updateTime(String paramString, Time paramTime)
			throws SQLException {
		// 不要
		
	}

	public void updateTimestamp(int paramInt, Timestamp paramTimestamp)
			throws SQLException {
		// 不要
		
	}

	public void updateTimestamp(String paramString, Timestamp paramTimestamp)
			throws SQLException {
		// 不要
		
	}

	public boolean wasNull() throws SQLException {
		// 不要
		return false;
	}

	public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
		// 不要
		return false;
	}

	public <T> T unwrap(Class<T> paramClass) throws SQLException {
		// 不要
		return null;
	}

	public static String[] split(String src, char delim, char bracket) {
		ArrayList<String> result = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int mode = 0;
		
		for (int i=0;i<src.length();i++) {
			char ch = src.charAt(i);
			switch(mode) {
			case 0:
				if (ch == delim) {
					result.add(null);
					sb.delete(0, sb.length());
				} else if (ch == bracket) {
					mode = 1;
				} else {
					sb.append(ch);
					mode = 3;
				}
				break;
			case 1:
				if (ch == delim) {
					sb.append(ch);
				} else if (ch == bracket) {
					mode = 2;
				} else {
					sb.append(ch);
				}
				break;
			case 2:
				if (ch == delim) {
					result.add(sb.toString());
					sb.delete(0, sb.length());
					mode = 0;
				} else if (ch == bracket) {
					sb.append(ch);
					mode = 1;
				} else {
					// 本来はあり得ない
					sb.append(delim);
					sb.append(ch);
					mode = 1;
				}
				break;
			case 3:
				if (ch == delim) {
					result.add(sb.toString());
					sb.delete(0, sb.length());
					mode = 0;
				} else if (ch == bracket) {
					sb.append(ch);
				} else {
					sb.append(ch);
				}
				break;
			default:
				throw new RuntimeException("ERROR");
			}
			//System.out.println(sb.toString());
		}
		if (mode != 0) {
			result.add(sb.toString());
		}
		
		return result.toArray(new String[result.size()]);
	}
}
