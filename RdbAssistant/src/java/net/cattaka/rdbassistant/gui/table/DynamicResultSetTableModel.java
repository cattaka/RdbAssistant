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
 * $Id: DynamicResultSetTableModel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.table;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.swing.table.AbstractTableModel;

import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.core.RdbaConnectionUtil;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.sql.ColumnConverter;
import net.cattaka.util.ExceptionHandler;

public class DynamicResultSetTableModel extends AbstractTableModel implements ResultSetTableModel {
	private static final long serialVersionUID = 1L;
	private RdbaConnection rdbConnection;
	private String tableName;
	private int[] columnCharacters;
	private ColumnConverter[] columnConverterList;
	private String[] columnNames = new String[0];
	private List<Object[]> cache = new ArrayList<Object[]>();
	private List<Object[]> lastCache = new ArrayList<Object[]>();
	private int cacheStart = 0;
	private int cacheEnd = 0;
	private int lastCacheStart = 0;
	private int lastCacheEnd = 0;
	private int rowCount = -1;
	
	/** １つのバッファの行数 */
	private int bufferdRowsSize = 100;
	/** GZIPされたバッファのリスト */
	private ArrayList<byte[]> bufferdRowsList = new ArrayList<byte[]>();
	/** 取り出しのキャンセルフラグ */
	private volatile boolean extractResultSetDataCancelFlag = false;
	/** 取り出し中の行数 */
	private volatile int extractedRowCount = 0;
	
	public DynamicResultSetTableModel() {
	}

	/**
	 * シングルスレッドでResultSet内容を読み出す。
	 * @param rdbConnection
	 * @param resultSet
	 * @throws SQLException
	 */
	public void extractResultSetData(RdbaConnection rdbConnection, ResultSet resultSet, String nullString) throws SQLException {
		this.rdbConnection = rdbConnection;
		
		ResultSetMetaData rsmd = resultSet.getMetaData();
		this.columnNames = new String[rsmd.getColumnCount()];
		for (int i=0;i<columnNames.length;i++) {
			this.columnNames[i] = rsmd.getColumnLabel(i+1);
		}
		
		this.columnConverterList = RdbaConnectionUtil.createColumnConverterList(rdbConnection, rsmd);
		this.columnCharacters = new int[rsmd.getColumnCount()];
		for (int i=0;i<columnNames.length;i++) {
			this.columnCharacters[i] = this.columnNames[i].length();
		}
		this.rowCount = 0;
		
		// 列一覧の取得
		while (!extractResultSetDataCancelFlag && resultSet.next()) {
			ArrayList<Object[]> bufferdRows = new ArrayList<Object[]>();
			// bufferdRowSizeの行数だけ行を読み込む
			do {
				this.rowCount++;
				this.extractedRowCount = this.rowCount;
				Object[] values = new Object[this.columnNames.length];
				for (int j=0;j<this.columnNames.length;j++) {
					Object obj = this.rdbConnection.extractResultSetData(resultSet, columnConverterList[j].getInClass(), j+1);;
					if (this.columnConverterList[j].isAvalConvert()) {
						values[j] = this.columnConverterList[j].convert(obj);
						if (values[j] == null) {
							values[j] = nullString;
						}
						// 表示列幅計算用の文字数を更新
						if (values[j] != null && values[j] instanceof String) {
							int l = ((String)values[j]).length();
							if (columnCharacters[j] < l) {
								columnCharacters[j] = l;
							}
						}
					}
				}
				bufferdRows.add(values);
			} while (bufferdRows.size() < bufferdRowsSize && resultSet.next());
			// 圧縮してキャッシュにしまう。
			try {
				Deflater deflater = new Deflater(Deflater.BEST_SPEED);
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				DeflaterOutputStream gout = new DeflaterOutputStream(bout, deflater);
				//GZIPOutputStream gout = new GZIPOutputStream(bout);
				ObjectOutputStream oout = new ObjectOutputStream(gout);
				oout.writeObject(bufferdRows);
				oout.flush();
				gout.finish();
				oout.close();
				
				this.bufferdRowsList.add(bout.toByteArray());
			} catch (IOException e) {
				// あり得ない
				ExceptionHandler.error(e);
			}
		}
		
		for (int i=0;i<columnCharacters.length;i++) {
			if (columnCharacters[i] < RdbaConstants.MINIMUM_COLUMN_CHARACTERS) {
				columnCharacters[i] = RdbaConstants.MINIMUM_COLUMN_CHARACTERS;
			}
			if (columnCharacters[i] > RdbaConstants.MAXIMUM_COLUMN_CHARACTERS) {
				columnCharacters[i] = RdbaConstants.MAXIMUM_COLUMN_CHARACTERS;
			}
		}
		// ====================================================--
		
		cache.clear();
		lastCache.clear();
		cacheStart = 0;
		cacheEnd = 0;
		lastCacheStart = 0;
		lastCacheEnd = 0;
	}
	
	/**
	 * 別スレッドでResultSet内容を読み出すスレッドを作成する。。
	 * @param rdbConnection
	 * @param resultSet
	 * @return
	 */
	public ExtractResultSetDataThread createExtractResultSetData(RdbaConnection rdbConnection, ResultSet resultSet, String nullString) {
		return new ExtractResultSetDataThread(rdbConnection, resultSet, nullString);
	}
	
	/**
	 * 別スレッドでResultSetの読み出し中にキャンセルなどの機能を提供する。
	 * @author cattaka
	 */
	public class ExtractResultSetDataThread extends Thread {
		private RdbaConnection rdbConnection;
		private ResultSet resultSet;
		private SQLException sqlException;
		private String nullString;

		ExtractResultSetDataThread(RdbaConnection rdbConnection, ResultSet resultSet, String nullString) {
			this.rdbConnection = rdbConnection;
			this.resultSet = resultSet;
			this.nullString = nullString;
		}
		public void run() {
			try {
				extractResultSetData(rdbConnection, resultSet, nullString);
			} catch (SQLException e) {
				this.sqlException = e;
			}
		}
		
		public SQLException getSqlException() {
			return sqlException;
		}
		
		public int getExtractedRowCount() {
			return extractedRowCount;
		}
		
		public void cancel() {
			// 親クラスのキャンセルフラグをオンにする。
			extractResultSetDataCancelFlag = true;
		}
		public boolean isCanceled() {
			return extractResultSetDataCancelFlag;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void updateCache(int rowIndex) {
		swapCache();
		int bufferdRowNumber = (rowIndex/bufferdRowsSize); 
		cacheStart = bufferdRowNumber * bufferdRowsSize;
		try {
			//long ctm = System.currentTimeMillis();
			Inflater inflater = new Inflater();
			ByteArrayInputStream bin = new ByteArrayInputStream(this.bufferdRowsList.get(bufferdRowNumber));
			//GZIPInputStream gin = new GZIPInputStream(bin);
			InflaterInputStream gin = new InflaterInputStream(bin, inflater);
			ObjectInputStream oin = new ObjectInputStream(gin);
			cache = (List<Object[]>)oin.readObject();
			oin.close();
		} catch(ClassNotFoundException e) {
			// あり得ない
			ExceptionHandler.error(e);
		} catch(IOException e) {
			// あり得ない
			ExceptionHandler.error(e);
		}
		cacheEnd = cacheStart + cache.size();
	}

	private void swapCache() {
		int t;
		t = cacheStart; 
		cacheStart = lastCacheStart;
		lastCacheStart = t;
		t = cacheEnd; 
		cacheEnd = lastCacheEnd;
		lastCacheEnd = t;
		
		List<Object[]> lt;
		lt = cache;
		cache = lastCache;
		lastCache = lt;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return this.tableName;
	}
	
	public int[] getColumnCharacters() {
		return this.columnCharacters;
	}

	public String getColumnName(int column) {
		return this.columnNames[column];
	}
	
	public Object getRowName(int row) {
		return Integer.valueOf(row + 1);
	}

	public Class<?> getRowHeaderClass() {
		return Integer.class;
	}

	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.columnConverterList[columnIndex].getOutClass();
	}
	
	public int getRowCount() {
		return this.rowCount;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result;
		try {
			if (cacheStart <= rowIndex && rowIndex < cacheEnd) {
				result = this.cache.get(rowIndex - cacheStart)[columnIndex];
			} else if (lastCacheStart <= rowIndex && rowIndex < lastCacheEnd) {
				result = this.lastCache.get(rowIndex - lastCacheStart)[columnIndex];
				swapCache();
			} else {
				updateCache(rowIndex);
				result = this.cache.get(rowIndex - cacheStart)[columnIndex];
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			ExceptionHandler.error(e);
			result = null;
		} catch (IndexOutOfBoundsException e) {
			ExceptionHandler.error(e);
			result = null;
		}
		return result;
	}
}
