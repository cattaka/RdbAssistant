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
 * $Id: ScriptTableUtil.java 264 2010-03-01 11:34:38Z cattaka $
 */
package net.cattaka.rdbassistant.script.core;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.cattaka.jspf.JspfException;
import net.cattaka.rdbassistant.gui.table.ResultSetTableModel;
import net.cattaka.swing.table.StdStyledCell;
import net.cattaka.swing.util.KeyStringListComparator;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.StringUtil;

/**
 * ScriptTableに関する汎用的な処理を提供します。
 * 
 * @author cattaka
 */
public class ScriptTableUtil {
	/** デフォルトの等しい場合に使用するマークです */
	private CellMark equalCellMark;
	/** デフォルトの等しくない場合に使用するマークです */
	private CellMark notEqualCellMark;
	/** デフォルトの追加分に使用するマークです */
	private CellMark addedCellMark;
	/** デフォルトの削除分に使用するマークです */
	private CellMark deletedCellmark;
	/** デフォルトの枠線付きセルに使用するマークです */
	private CellMark borderCellMark;
	
	public ScriptTableUtil() {
		equalCellMark = new CellMark();
		notEqualCellMark = new CellMark();
		addedCellMark = new CellMark();
		deletedCellmark = new CellMark();
		borderCellMark = new CellMark();
		notEqualCellMark.setBackground(Color.YELLOW);
		addedCellMark.setBackground(Color.GREEN);
		deletedCellmark.setBackground(Color.RED);
		borderCellMark.setBorderThickness(1);
	}
	
	/**
	 * 内部処理用にResultSetTableModelをScriptTableに変換する。
	 * 
	 * @param rs
	 * @return
	 */
	ScriptTable createScriptTable(ResultSetTableModel rs) {
		ScriptTable result = new ScriptTable(rs.getRowCount()+1, rs.getColumnCount()+1);
		result.setValueAt("", 0, 0);
		// 列ヘッダ
		for (int c=1;c<=rs.getColumnCount();c++) {
			result.setValueAt(rs.getColumnName(c-1), 0, c);
		}

		// 行ヘッダ
		for (int r=1;r<=rs.getRowCount();r++) {
			result.setValueAt(String.valueOf(r), r, 0);
		}
		
		// テーブルの内容
		for (int r=0;r<rs.getRowCount();r++) {
			for (int c=0;c<rs.getColumnCount();c++) {
				Object obj = rs.getValueAt(r, c);
				if (obj != null) {
					result.setValueAt(obj.toString(), r+1, c+1);
				}
			}
		}
		// ボーダーの設定
		for (int r=0;r<result.getRowCount();r++) {
			for (int c=0;c<result.getColumnCount();c++) {
				StdStyledCell cell = result.getCellAt(r,c); 
				borderCellMark.mark(cell);
				result.setCellAt(cell,r,c);
			}
		}
		return result;
	}

	/**
	 * 指定されたテーブル間の差異を探し、差異のある箇所に指定したマーキングを行ったテーブルを返します。
	 * 行ヘッダと列ヘッダは一つ目のテーブルの物が使用されます。
	 * 削除された列も表示されます。
	 * 
	 * @param tbl1 比較対象となるテーブルです 
	 * @param tbl2 比較対象となるテーブルです 
	 * @param keyColumns キー項目の列名
	 * @return 比較結果
	 * @throws JspfException 指定された列名の列が存在しない場合やキー項目がユニークでない場合にスローされます。
	 */
	public ScriptTable createMarkedDiff(ScriptTable tbl1, ScriptTable tbl2, String... keyColumns) throws JspfException {
		int[] keyColumnIndices = new int[keyColumns.length];
		for (int i=0;i<keyColumns.length;i++) {
			keyColumnIndices[i] = tbl1.getColumnIndex(keyColumns[i]);
		}
		return createMarkedDiff(tbl1, tbl2, keyColumnIndices);
	}

	/**
	 * 指定されたテーブル間の差異を探し、差異のある箇所に指定したマーキングを行ったテーブルを返します。
	 * 行ヘッダと列ヘッダは一つ目のテーブルの物が使用されます。
	 * 削除された列も表示されます。
	 * 
	 * @param tbl1 比較対象となるテーブルです 
	 * @param tbl2 比較対象となるテーブルです 
	 * @param keyColumns キー項目の列番
	 * @return 比較結果
	 * @throws JspfException キー項目がユニークでない場合にスローされます。
	 */
	public ScriptTable createMarkedDiff(ScriptTable tbl1, ScriptTable tbl2, int... keyColumnIndices) throws JspfException {
		if (keyColumnIndices.length == 0) {
			keyColumnIndices = new int[]{0};
		}
		// テーブル１のユニークキーチェック
		{
			TreeSet<List<String>> uniqueTbl1 = new TreeSet<List<String>>(new KeyStringListComparator(keyColumnIndices));
			for (int r=1;r<tbl1.getRowCount();r++) {
				List<String> cols = tbl1.getRowValuesAsFullList(r);
				if (!uniqueTbl1.add(cols)) {
					throw new JspfException(MessageBundle.getMessage("keys_are_not_unique"));
				}
			}
		}
		// テーブル２のマップを作成
		TreeMap<List<String>, List<String>> indexedMap = new TreeMap<List<String>, List<String>>(new KeyStringListComparator(keyColumnIndices));
		for (int r=1;r<tbl2.getRowCount();r++) {
			List<String> cols = tbl2.getRowValuesAsFullList(r);
			if (indexedMap.put(cols,cols) != null) {
				throw new JspfException(MessageBundle.getMessage("keys_are_not_unique"));
			}
		}
		
		ScriptTable result = new ScriptTable(tbl1.getRowCount()+tbl2.getRowCount(), tbl1.getColumnCount());
	
		// 列のコピー
		for (int c=0;c<tbl2.getColumnCount();c++) {
			StdStyledCell cell = tbl1.getCellAt(0,c); 
			borderCellMark.mark(cell);
			result.setCellAt(cell,0,c);
		}
		
		// 内容のコピーと比較
		int rowCount = 0;
		for (int r=1;r<tbl1.getRowCount();r++) {
			rowCount++;
			
			List<String> cols1 = tbl1.getRowValuesAsFullList(r);
			List<String> cols2 = indexedMap.remove(cols1);
			StdStyledCell cell = result.getCellAt(r,0);
			cell.setValue(String.valueOf(rowCount));
			borderCellMark.mark(cell);
			result.setCellAt(cell,r,0);
			if (cols2 != null) {
				// 既存の列
				for (int c=1;c<cols1.size();c++) {
					String str1 = cols1.get(c);
					StdStyledCell cell2 = result.getCellAt(r,c);
					borderCellMark.mark(cell2);
					cell2.setValue(str1);
					if (c<cols2.size()) {
						String str2 = cols2.get(c);
						if (str1 == null && str2 == null) {
							equalCellMark.mark(cell2);
						} else if ((str1 != null && str2 == null) || (str1 == null && str2 != null)) {
							notEqualCellMark.mark(cell2);
						} else if (str1.equals(str2)) {
							equalCellMark.mark(cell2);
						} else {
							notEqualCellMark.mark(cell2);
						}
					} else {
						notEqualCellMark.mark(cell2);
					}
					result.setCellAt(cell2,r,c);
				}
			} else {
				// 追加された列
				for (int c=0;c<cols1.size();c++) {
					String str1 = cols1.get(c);
					StdStyledCell cell2 = result.getCellAt(r,c);
					cell2.setValue(str1);
					addedCellMark.mark(cell2);
					borderCellMark.mark(cell2);
					result.setCellAt(cell2, r, c);
				}
			}
		}
		// 削除された列
		Set<List<String>> keys = indexedMap.keySet();
		for (List<String>key : keys) {
			rowCount++;
			List<String> cols2 = indexedMap.get(key);
			result.setValueAt("", rowCount, 0);
			StdStyledCell cell = result.getCellAt(rowCount, 0);
			deletedCellmark.mark(cell);
			borderCellMark.mark(cell);
			result.setCellAt(cell, rowCount, 0);
			for (int c=1;c<cols2.size();c++) {
				String str1 = cols2.get(c);
				StdStyledCell cell2 = result.getCellAt(rowCount, c);
				cell2.setValue(str1);
				deletedCellmark.mark(cell2);
				borderCellMark.mark(cell2);
				result.setCellAt(cell2, rowCount, c);
			}
		}
		rowCount++;
		result.setSize(rowCount, result.getColumnCount());
		
		return result;
	}

	/**
	 * CSVの文字列をScriptTableとして読み込みます。
	 * 
	 * @param rawString 対象となるCSVの文字列
	 * @return CSVをScriptTableとして読み込んだ結果
	 * @throws JspfException エラー時にスローされる
	 */
	public static ScriptTable convertCsv2Table(String rawString) throws JspfException {
		StringReader reader = new StringReader(rawString);
		try {
			return loadTable(reader, ',', '"');
		} catch (IOException e) {
			throw new JspfException(e);
		}
	}
	
	/**
	 * CSVファイルをScriptTableとして読み込みます。
	 * 
	 * @param fileName 対象となるファイル名
	 * @return CSVをScriptTableとして読み込んだ結果
	 * @throws JspfException ファイルが存在しない場合などのエラー時にスローされる
	 */
	public static ScriptTable loadCsv(String fileName) throws JspfException {
		return loadCsv(fileName, null);
	}

	/**
	 * CSVファイルをScriptTableとして読み込みます。
	 * 
	 * @param fileName 対象となるファイル名
	 * @param charset 文字コード
	 * @return CSVをScriptTableとして読み込んだ結果
	 * @throws JspfException ファイルが存在しない場合などのエラー時にスローされる
	 */
	public static ScriptTable loadCsv(String fileName, String charset) throws JspfException {
		return loadTable(fileName, charset,',','"');
	}

	/**
	 * TSVの文字列をScriptTableとして読み込みます。
	 * 
	 * @param rawString 対象となるTSVの文字列
	 * @return TSVをScriptTableとして読み込んだ結果
	 * @throws JspfException エラー時にスローされる
	 */
	public static ScriptTable convertTsv2Table(String rawString) throws JspfException {
		StringReader reader = new StringReader(rawString);
		try {
			return loadTable(reader, '\t', '"');
		} catch (IOException e) {
			throw new JspfException(e);
		}
	}

	/**
	 * TSVファイルをScriptTableとして読み込みます。
	 * 
	 * @param fileName 対象となるファイル名
	 * @return TSVをScriptTableとして読み込んだ結果
	 * @throws JspfException ファイルが存在しない場合などのエラー時にスローされる
	 */
	public static ScriptTable loadTsv(String fileName) throws JspfException {
		return loadTsv(fileName, null);
	}

	/**
	 * TSVファイルをScriptTableとして読み込みます。
	 * 
	 * @param fileName 対象となるファイル名
	 * @param charset 文字コード
	 * @return TSVをScriptTableとして読み込んだ結果
	 * @throws JspfException ファイルが存在しない場合などのエラー時にスローされる
	 */
	public static ScriptTable loadTsv(String fileName, String charset) throws JspfException {
		return loadTable(fileName, charset,'\t','"');
	}

	/**
	 * ScriptTableをCSVファイルとして出力します。
	 * 
	 * @param table 対象となるテーブル
	 * @param fileName 対象となるファイル名
	 * @throws JspfException 入出力エラーが発生した場合にスローされる
	 */
	public static void saveCsv(ScriptTable table, String fileName) throws JspfException {
		saveCsv(table, fileName, null);
	}

	/**
	 * ScriptTableをCSVファイルとして出力します。
	 * 
	 * @param table 対象となるテーブル
	 * @param fileName 対象となるファイル名
	 * @param charset 文字コード
	 * @throws JspfException 入出力エラーが発生した場合にスローされる
	 */
	public static void saveCsv(ScriptTable table, String fileName, String charset) throws JspfException {
		saveTable(table, fileName, charset, ',', '"');
	}

	/**
	 * ScriptTableをTSVファイルとして出力します。
	 * 
	 * @param table 対象となるテーブル
	 * @param fileName 対象となるファイル名
	 * @throws JspfException 入出力エラーが発生した場合にスローされる
	 */
	public static void saveTsv(ScriptTable table, String fileName) throws JspfException {
		saveTsv(table, fileName, null);
	}

	/**
	 * ScriptTableをTSVファイルとして出力します。
	 * 
	 * @param table 対象となるテーブル
	 * @param fileName 対象となるファイル名
	 * @param charset 文字コード
	 * @throws JspfException 入出力エラーが発生した場合にスローされる
	 */
	public static void saveTsv(ScriptTable table, String fileName, String charset) throws JspfException {
		saveTable(table, fileName, charset, '\t', '"');
	}
	
	/**
	 * ScriptTableをTSV形式の文字列に変換します。
	 * @param table
	 * @return TSV形式の文字列
	 */
	public static String convertTable2Tsv(ScriptTable table) {
		return convertToSepalatedValue(table, '\t', '"');
	}

	/**
	 * ScriptTableをCSV形式の文字列に変換します。
	 * @param table
	 * @return CSV形式の文字列
	 */
	public static String convertTable2Csv(ScriptTable table) {
		return convertToSepalatedValue(table, ',', '"');
	}

	private static ScriptTable loadTable(String fileName, String charset, char delim, char bracket) throws JspfException {
		ScriptTable result = new ScriptTable();
		BufferedReader reader = null;
		try {
			reader = getInputStream(fileName, charset);
			result = loadTable(reader, delim, bracket);
		} catch (IOException e) {
			ExceptionHandler.error(e);
			throw new JspfException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					ExceptionHandler.warn(e);
				}
			}
		}
		return result;
	}
	private static ScriptTable loadTable(Reader reader, char delim, char bracket) throws IOException {
		ScriptTable result = new ScriptTable();
		String[][] strss = StringUtil.split(reader, delim, bracket);
		for (int r=0;r<strss.length;r++) {
			for (int c=0;c<strss[r].length;c++) {
				result.setValueAt(strss[r][c], r, c);
			}
		}
		result.trim();
		return result;
	}

	private static void saveTable(ScriptTable table, String fileName, String charset, char delim, char bracket) throws JspfException {
		BufferedWriter writer = getBufferedWriter(fileName, charset);
		try {
			writer.write(convertToSepalatedValue(table, delim, bracket));
		} catch (IOException e) {
			ExceptionHandler.error(e);
			throw new JspfException(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					ExceptionHandler.warn(e);
				}
			}
		}
	}

	private static String convertToSepalatedValue(ScriptTable table, char delim, char bracket) {
		StringBuilder writer = new StringBuilder();
		for (int r=0;r<table.getRowCount();r++) {
			for (int c=0;c<table.getColumnCount();c++) {
				if (c>0) {
					writer.append(delim);
				}
				String str = table.getValueAt(r, c);
				if (str != null) {
					writer.append(StringUtil.escapeString(str, delim, bracket));
				}
			}
			writer.append('\n');
		}
		return writer.toString();
	}

	private static BufferedReader getInputStream(String fileName, String charset) throws JspfException {
		BufferedReader result = null;
		InputStream in = null;
		File file = new File(fileName);
		if (file.exists() && file.isDirectory()) {
			String msg = String.format(MessageBundle.getMessage("this_is_directory"),fileName);
			ExceptionHandler.error(msg);
			throw new JspfException(msg);
		}
		try {
			in = new FileInputStream(file);
			if (charset == null) {
				result = new BufferedReader(new InputStreamReader(in));
			} else {
				result = new BufferedReader(new InputStreamReader(in, charset));
			}
		} catch (FileNotFoundException e) {
			ExceptionHandler.error(e);
			throw new JspfException(String.format(MessageBundle.getMessage("this_is_not_found"),fileName));
		} catch (UnsupportedEncodingException e) {
			ExceptionHandler.error(e);
			throw new JspfException(e.getMessage());
		} finally {
			if (result == null) {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// ありえない
						ExceptionHandler.warn(e);
					}
				}
			}
		}
		return result;
	}
	
	private static BufferedWriter getBufferedWriter(String fileName, String charset) throws JspfException {
		BufferedWriter result = null;
		OutputStream out = null;
		File file = new File(fileName);
		if (file.exists() && file.isDirectory()) {
			String msg = String.format(MessageBundle.getMessage("this_is_directory"),fileName);
			ExceptionHandler.error(msg);
			throw new JspfException(msg);
		}
		try {
			out = new FileOutputStream(file);
			if (charset == null) {
				result = new BufferedWriter(new OutputStreamWriter(out));
			} else {
				result = new BufferedWriter(new OutputStreamWriter(out,charset));
			}
		} catch (FileNotFoundException e) {
			ExceptionHandler.error(e);
			throw new JspfException(String.format(MessageBundle.getMessage("this_is_not_found"),fileName));
		} catch (UnsupportedEncodingException e) {
			ExceptionHandler.error(e);
			throw new JspfException(e.getMessage());
		} finally {
			if (result == null) {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// ありえない
						ExceptionHandler.warn(e);
					}
				}
			}
		}
		return result;
	}
}
