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
 * $Id: ScriptTable.java 242 2009-10-14 12:45:46Z cattaka $
 */
package net.cattaka.rdbassistant.script.core;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import net.cattaka.jspf.JspfException;
import net.cattaka.swing.table.StdStyledCell;
import net.cattaka.swing.table.StdStyledCellArrayComparator;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.StringArrayComparator;

/**
 * スクリプト内で入出力やSQLの結果を受け取る為に使用するテーブルです。
 * インデックスが０の行は列ヘッダ、０の列は行ヘッダとして使用されます。
 * 
 * @author cattaka
 */
public class ScriptTable implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tableName;
	private String description;
	private HashMap<String, Integer> columnMap;
	private StdStyledCell[][] values;
	private int rows;
	private int cols;
	private int maxRow;
	private int maxCol;
	
	/**
	 * コンストラクタ 
	 */
	public ScriptTable() {
		this(32,32);
	}
	/**
	 * コンストラクタ 
	 * 
	 * @param rows 作成するテーブルの行数。
	 * @param cols 作成するテーブルの列数。
	 */
	public ScriptTable(int rows, int cols) {
		this.columnMap = new HashMap<String, Integer>();
		this.values = new StdStyledCell[rows][cols];
		this.rows = rows;
		this.cols = cols;
	}
	
	/**
	 * コピーコンストラクタ
	 * @param src コピー元
	 */
	public ScriptTable(ScriptTable src) {
		this(src.getRowCount(), src.getColumnCount());
		for (int r=0;r<src.getRowCount();r++) {
			for (int c=0;c<src.getColumnCount();c++) {
				StdStyledCell cell = src.getCellAt(r,c); 
				this.setCellAt(cell,r,c);
			}			
		}
	}

	/**
	 * テーブルのサイズを変更します。
	 * 
	 * @param rows 新しいテーブルの行数。
	 * @param cols 新しいテーブルの列数。
	 */
	public void setSize(int rows, int cols) {
		StdStyledCell[][] newvalues = new StdStyledCell[rows][cols];
		for (int r = 0;r<this.rows && r<newvalues.length;r++) {
			System.arraycopy(values[r], 0, newvalues[r], 0, Math.min(values[r].length, newvalues[r].length));
		}
		this.values = newvalues;
		this.rows = rows;
		this.cols = cols;
	}
	
	/**
	 * このテーブルで使用されていない行と列を除去します。
	 */
	public void trim() {
		this.setSize(this.maxRow, this.maxCol);
	}
	
	/**
	 * 指定した名称の列のインデックスを取得します。
	 * 
	 * @param columnName 列の名称
	 * @return 列のインデックス
	 * @throws JspfException 指定された列名の列が存在しない場合
	 */
	public int getColumnIndex(String columnName) throws JspfException {
		Integer col = this.columnMap.get(columnName);
		if (col == null) {
			throw new JspfException(String.format(MessageBundle.getMessage("no_such_column"),columnName));
		}
		return col;
	}
	
	/**
	 * 指定したセルに値を設定します。
	 * 範囲外の行と列を指定した場合は何も行いません。
	 * 
	 * @param value 設定する値
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 */
	public void setValueAt(String value, int row, int col) {
		if (row == 0 && value != null) {
			this.columnMap.put(value, col);
		}
		prepareTable(row, col);
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			this.prepareCell(row, col);
			this.values[row][col].setValue(value);
			this.maxRow = Math.max(row+1, this.maxRow);
			this.maxCol = Math.max(col+1, this.maxCol);
		}
	}
	
	/**
	 * 指定したセルに値を設定します。
	 * 
	 * @param value 設定する値
	 * @param row 対象となるセルの行
	 * @param columnName 対象となるセルの列名
	 * @throws JspfException 存在しない列名を指定した場合にスローされます。
	 */
	public void setValueAt(String value, int row, String columnName) throws JspfException {
		this.setValueAt(value, row, getColumnIndex(columnName));
	}
	
	/**
	 * 指定したセルの値を取得します。
	 * 範囲外の行と列を指定した場合はnullを返します。
	 * 
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 * @return 対象となるセルの値
	 */
	public String getValueAt(int row, int col) {
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			return (this.values[row][col] != null) ? this.values[row][col].getValue() : null;
		} else {
			return null;
		}
	}

	/**
	 * 指定したセルの値を取得します。
	 * 
	 * @param row 対象となるセルの行
	 * @param columnName 対象となるセルの列名
	 * @return 対象となるセルの値
	 * @throws JspfException 存在しない列名を指定した場合にスローされます。
	 */
	public String getValueAt(int row, String columnName) throws JspfException {
		return this.getValueAt(row, getColumnIndex(columnName));
	}
	
	/**
	 * 指定したセルの前景色を設定します。
	 * 範囲外の行と列を指定した場合は何も行いません。
	 * 
	 * @param color 設定する前景色
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 */
	public void setForegroundAt(Color color, int row, int col) {
		prepareTable(row, col);
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			this.prepareCell(row, col);
			this.values[row][col].setForeground(color);
			this.maxRow = Math.max(row+1, this.maxRow);
			this.maxCol = Math.max(col+1, this.maxCol);
		}
	}
	
	/**
	 * 指定したセルの前景色を取得します。
	 * 範囲外の行と列を指定した場合はnullを返します。
	 * 
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 * @return 対象となるセルの前景色
	 */
	public Color getForegroundAt(int row, int col) {
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			return (this.values[row][col] != null) ? this.values[row][col].getForeground() : null;
		} else {
			return null;
		}
	}

	/**
	 * 指定したセルの背景色を設定します。
	 * 範囲外の行と列を指定した場合は何も行いません。
	 * 
	 * @param color 設定する背景色
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 */
	public void setBackgroundAt(Color color, int row, int col) {
		prepareTable(row, col);
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			this.prepareCell(row, col);
			this.values[row][col].setBackground(color);
			this.maxRow = Math.max(row+1, this.maxRow);
			this.maxCol = Math.max(col+1, this.maxCol);
		}
	}
	
	/**
	 * 指定したセルの背景色を取得します。
	 * 範囲外の行と列を指定した場合はnullを返します。
	 * 
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 * @return 対象となるセルの背景色
	 */
	public Color getBackgroundAt(int row, int col) {
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			return (this.values[row][col] != null) ? this.values[row][col].getBackground() : null;
		} else {
			return null;
		}
	}

	/**
	 * 指定したセルのボーターの色を設定します。
	 * 範囲外の行と列を指定した場合は何も行いません。
	 * 
	 * @param color 設定するボーターの色
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 */
	public void setBorderColorAt(Color color, int row, int col) {
		prepareTable(row, col);
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			this.prepareCell(row, col);
			this.values[row][col].setBorderColor(color);
			this.maxRow = Math.max(row+1, this.maxRow);
			this.maxCol = Math.max(col+1, this.maxCol);
		}
	}
	
	/**
	 * 指定したセルのボーターの色を取得します。
	 * 範囲外の行と列を指定した場合はnullを返します。
	 * 
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 * @return 対象となるセルのボーターの色
	 */
	public Color getBorderColorAt(int row, int col) {
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			return (this.values[row][col] != null) ? this.values[row][col].getBorderColor() : null;
		} else {
			return null;
		}
	}
	
	/**
	 * 指定したセルのボーターの太さを設定します。
	 * 範囲外の行と列を指定した場合は何も行いません。
	 * 
	 * @param borderThickness 設定するボーターの太さ
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 */
	public void setBorderThicknessAt(int borderThickness, int row, int col) {
		prepareTable(row, col);
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			this.prepareCell(row, col);
			this.values[row][col].setBorderThickness(borderThickness);
			this.maxRow = Math.max(row+1, this.maxRow);
			this.maxCol = Math.max(col+1, this.maxCol);
		}
	}
	
	/**
	 * 指定したセルのボーターの太さを取得します。
	 * 範囲外の行と列を指定した場合はnullを返します。
	 * 
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 * @return 対象となるセルのボーターの太さ
	 */
	public int getBorderThicknessAt(int row, int col) {
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			return (this.values[row][col] != null) ? this.values[row][col].getBorderThickness() : 0;
		} else {
			return 0;
		}
	}
	
	/**
	 * 内部用にStdStyledCellを返します。
	 * 指定されたセルの実体を返します。
	 * 
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 * @return
	 */
	StdStyledCell getCellAt(int row, int col) {
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			StdStyledCell cell = this.values[row][col];
			return (cell!=null) ? new StdStyledCell(cell) : new StdStyledCell();
		} else {
			return null;
		}
	}

	/**
	 * 内部用にStdStyledCellを設定します。
	 * 指定されたセルの内容を対象のセルに設定します。
	 * 
	 * @param cell 設定するセルの内容
	 * @param row 対象となるセルの行
	 * @param col 対象となるセルの列
	 */
	void setCellAt(StdStyledCell cell, int row, int col) {
		if (0<=row && row<this.rows && 0<=col && col<this.cols) {
			this.values[row][col] = (cell != null) ? new StdStyledCell(cell) : null;
			this.maxRow = Math.max(row+1, this.maxRow);
			this.maxCol = Math.max(col+1, this.maxCol);
		}
	}

	/**
	 * このテーブルの行数を取得します。
	 * 
	 * @return このテーブルの行数
	 */
	public int getRowCount() {
		return this.rows;
	}

	/**
	 * このテーブルの列数を取得します。
	 * 
	 * @return このテーブルの列数
	 */
	public int getColumnCount() {
		return this.cols;
	}
	
	/**
	 * このテーブルに与えられたテーブルの内容を指定した位置に書き込みます。
	 * 
	 * @param scriptTable 書き込み元のテーブル
	 * @param r このテーブル上の書き込み先の行
	 * @param c このテーブル上の書き込み先の列
	 */
	public void writeTable(ScriptTable scriptTable, int r, int c) {
		this.writeTable(scriptTable, r, c, -1, -1);
	}
	
	/**
	 * このテーブルに与えられたテーブルの内容を指定した位置に書き込みます。
	 * 
	 * @param scriptTable 書き込み元のテーブル
	 * @param r このテーブル上の書き込み先の行
	 * @param c このテーブル上の書き込み先の列
	 * @param rows 出力する行数の上限(-1なら無制限)
	 * @param cols 出力する列数の上限(-1なら無制限)
	 */
	public void writeTable(ScriptTable scriptTable, int r, int c, int rows, int cols) {
		if (rows < 0) {
			rows = scriptTable.getRowCount();
		}
		if (cols < 0) {
			cols = scriptTable.getColumnCount();
		}
		prepareTable(r+rows, c+cols);
		for (int pr=0;pr<rows && pr<scriptTable.getRowCount();pr++) {
			for (int pc=0;pc<cols && pc<scriptTable.getColumnCount();pc++) {
				StdStyledCell cell = scriptTable.getCellAt(pr, pc); 
				this.setCellAt(cell,r+pr, c+pc);
			}
		}
	}
	
	/**
	 * 指定された行の値をリストとして取得します。
	 * 
	 * @param rowIdx 対象となる行
	 * @return 指定された行の値のリスト
	 */
	public List<String> getRowValues(int rowIdx) {
		List<String> result = new ArrayList<String>(getColumnCount());
		for (int c=1;c<getColumnCount();c++) {
			String str = (this.values[rowIdx][c] != null) ? this.values[rowIdx][c].getValue() : null;
			result.add(str);
		}
		return result;
	}
	
	/**
	 * 指定された行の値をリストとして取得します。
	 * 
	 * @param rowIdx 対象となる行(複数可)
	 * @return 指定された行の値のリスト
	 */
	public List<String[]> getRowValuesAsList(int... rowIdx) {
		List<String[]> result = new ArrayList<String[]>();
		for (int c=1;c<getColumnCount();c++) {
			String[] strs = new String[rowIdx.length];
			for (int i=0;i<rowIdx.length;i++) {
				int r = rowIdx[i];
				strs[i] = (this.values[r][c] != null) ? this.values[r][c].getValue() : null;
			}
			result.add(strs);
		}
		return result;
	}

	/**
	 * 指定された行の値をセットとして取得します。
	 * 
	 * @param rowIdx 対象となる行(複数可)
	 * @return 指定された行の値のセット
	 */
	public TreeSet<String[]> getRowValuesAsSet(int... rowIdx) {
		TreeSet<String[]> result = new TreeSet<String[]>(new TreeSet<String[]>(new StringArrayComparator()));
		for (int c=1;c<getColumnCount();c++) {
			String[] strs = new String[rowIdx.length];
			for (int i=0;i<rowIdx.length;i++) {
				int r = rowIdx[i];
				strs[i] = (this.values[r][c] != null) ? this.values[r][c].getValue() : null;
			}
			result.add(strs);
		}
		return result;
	}

	/**
	 * 指定された列の値をリストとして取得します。
	 * 
	 * @param colIdx 対象となる列
	 * @return 指定された列の値のリスト
	 */
	public List<String> getColumnValues(int colIdx) {
		List<String> result = new ArrayList<String>();
		for (int r=1;r<getRowCount();r++) {
			String str = (this.values[r][colIdx] != null) ? this.values[r][colIdx].getValue() : null;
			result.add(str);
		}
		return result;
	}

	/**
	 * 指定された列の値をリストとして取得します。
	 * 
	 * @param columnName 対象となる列の名称
	 * @return 指定された列の値のリスト
	 * @throws JspfException 存在しない列名を指定した場合にスローされます。
	 */
	public List<String> getColumnValues(String columnName) throws JspfException {
		int colIdx = this.getColumnIndex(columnName);
		return getColumnValues(colIdx);
	}
	
	/**
	 * 指定された列の値をリストとして取得します。
	 * 
	 * @param colIdx 対象となる列(複数可)
	 * @return 指定された列の値のリスト
	 */
	public List<String[]> getColumnValuesAsList(int... colIdx) {
		List<String[]> result = new ArrayList<String[]>();
		for (int r=1;r<getRowCount();r++) {
			String[] strs = new String[colIdx.length];
			for (int i=0;i<colIdx.length;i++) {
				int c = colIdx[i];
				strs[i] = (this.values[r][c] != null) ? this.values[r][c].getValue() : null;
			}
			result.add(strs);
		}
		return result;
	}

	/**
	 * 指定された列の値をリストとして取得します。
	 * 
	 * @param columnName 対象となる列の名称(複数可)
	 * @return 指定された列の値のリスト
	 * @throws JspfException 存在しない列名を指定した場合にスローされます。
	 */
	public List<String[]> getColumnValuesAsList(String... columnNames) throws JspfException {
		int[] colIdx = new int[columnNames.length];
		for (int i=0;i<colIdx.length;i++) {
			colIdx[i] = this.getColumnIndex(columnNames[i]);
		}
		return getColumnValuesAsList(colIdx);
	}

	/**
	 * 指定された列の値をセットとして取得します。
	 * 
	 * @param colIdx 対象となる列(複数可)
	 * @return 指定された列の値のセット
	 */
	public TreeSet<String[]> getColumnValuesAsSet(int... colIdx) {
		TreeSet<String[]> result = new TreeSet<String[]>(new TreeSet<String[]>(new StringArrayComparator()));
		for (int r=1;r<getRowCount();r++) {
			String[] strs = new String[colIdx.length];
			for (int i=0;i<colIdx.length;i++) {
				int c = colIdx[i];
				strs[i] = (this.values[r][c] != null) ? this.values[r][c].getValue() : null;
			}
			result.add(strs);
		}
		return result;
	}

	/**
	 * 指定された列の値をセットとして取得します。
	 * 
	 * @param columnName 対象となる列の名称(複数可)
	 * @return 指定された列の値のセット
	 * @throws JspfException 存在しない列名を指定した場合にスローされます。
	 */
	public TreeSet<String[]> getColumnValuesAsSet(String... columnNames) throws JspfException {
		int[] colIdx = new int[columnNames.length];
		for (int i=0;i<colIdx.length;i++) {
			colIdx[i] = this.getColumnIndex(columnNames[i]);
		}
		return getColumnValuesAsSet(colIdx);
	}

	/**
	 * このテーブルの名前を取得します。
	 * @return テーブルの名前
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * このテーブルの名前を設定します。
	 * @param tableName テーブルの名前
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * このテーブルの説明を取得します。
	 * @return テーブルの説明
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * このテーブルの説明を設定します。
	 * @param tableName テーブルの名前
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 指定した列について並び替えます。
	 * 
	 * @param columnName 対象となる列の名称
	 * @param descending 並び順が昇順ならfalseを、降順ならtrueを指定します。
	 * @throws JspfException 存在しない列名を指定した場合にスローされます。
	 */
	public void sortRows(String columnName, boolean descending) throws JspfException {
		int colIdx = this.getColumnIndex(columnName);
		this.sortRows(colIdx, descending);
	}
	
	/**
	 * 指定した列について並び替えます。
	 * 
	 * @param colIdx 対象となる列
	 * @param descending 並び順が昇順ならfalseを、降順ならtrueを指定します。
	 */
	public void sortRows(int colIdx, boolean descending) {
		if (this.values.length <= 2) {
			// 0〜1だからソート不要
			return;
		}
		StdStyledCellArrayComparator sscc = new StdStyledCellArrayComparator(new int[]{colIdx}, descending);
		StdStyledCell[][] tmp = new StdStyledCell[this.values.length-1][];
		System.arraycopy(this.values, 1, tmp, 0, tmp.length);
		Arrays.sort(tmp, sscc);
		System.arraycopy(tmp, 0, this.values, 1, tmp.length);
	}

	List<String> getRowValuesAsFullList(int rowIdx) {
		List<String> result = new ArrayList<String>(getColumnCount());
		for (int c=0;c<getColumnCount();c++) {
			String str = (this.values[rowIdx][c] != null) ? this.values[rowIdx][c].getValue() : null;
			result.add(str);
		}
		return result;
	}

	private void prepareCell(int row, int col) {
		if (this.values[row][col] == null) {
			this.values[row][col] = new StdStyledCell();
		}
	}
	private void prepareTable(int row, int col) {
		if (row<this.rows && col<this.cols) {
			// OK
			return;
		} else {
			int newRows = this.rows;
			int newCols = this.cols;
			while(newRows > 0 && newRows <= row) {
				newRows*=2;
			}
			while(newCols > 0 && newCols <= col) {
				newCols*=2;
			}
			this.setSize(newRows, newCols);
		}
	}
}
