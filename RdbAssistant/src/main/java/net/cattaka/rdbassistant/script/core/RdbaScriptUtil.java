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
 * $Id: RdbaScript.java 436 2011-04-14 16:04:17Z cattaka $
 */
package net.cattaka.rdbassistant.script.core;

import java.awt.Component;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import net.cattaka.jspf.JspfException;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.core.RdbaException;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.table.DynamicResultSetTableModel;
import net.cattaka.rdbassistant.gui.table.ResultSetTableModelUtil;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.StringUtil;

/**
 * スクリプトをクラスに変換した際に、実体の親クラスとなるクラスです。
 * このクラスが実装するメソッドはスクリプト内から利用することが可能です。
 * 
 * @author cattaka
 */
public class RdbaScriptUtil {
	private RdbaConnection rdbaConnection;
	private List<RdbaConnection> rdbaConnectionBundle = new ArrayList<RdbaConnection>();
	private PrintWriter logWriter;
	private RdbaGuiInterface parentComponent;
	protected ScriptTableUtil scriptTableUtil;
	/**
	 * ログの出力先。
	 */
	protected PrintWriter out;
	/**
	 * 結果の出力先となるテーブル。
	 */
	protected ScriptTable table;
	
	/**
	 * このスクリプトの実行に必要な値を設定します。
	 * 
	 * @param rdbaConnection
	 * @param logWriter
	 * @param parentComponent
	 */
	public void initialize(RdbaConnection rdbaConnection, PrintWriter logWriter, PrintWriter out, ScriptTable table, RdbaGuiInterface parentComponent) {
		this.rdbaConnection = rdbaConnection;
		this.logWriter = logWriter;
		this.parentComponent = parentComponent;
		this.scriptTableUtil = new ScriptTableUtil();
		this.out = out;
		this.table = table;
	}
	
	/**
	 * SQL文を実行してその結果をScriptTableとして取得します。
	 * 
	 * @param sqlStr SQL文の文字列です。
	 * @return 実行結果のScriptTableです。実行するSQLがSELECT文でない場合はnullを返します。
	 * @throws SQLException SQLの実行時にエラーが発生した場合にスローされます。
	 */
	public ScriptTable runSql(String sqlStr) throws SQLException {
		return runSql(this.rdbaConnection, sqlStr);
	}

	/**
	 * SQL文を実行してその結果をScriptTableとして取得します。
	 * 
	 * @param rdbaConnection {@link #getRdbaConnection(String)}で取得したものを指定します。
	 * @param sqlStr SQL文の文字列です。
	 * @return 実行結果のScriptTableです。実行するSQLがSELECT文でない場合はnullを返します。
	 * @throws SQLException SQLの実行時にエラーが発生した場合にスローされます。
	 */
	public ScriptTable runSql(RdbaConnection rdbaConnection, String sqlStr) throws SQLException {
		ScriptTable result = null;
		String nullString = (parentComponent.getRdbaConfig().isDisplayStringIfNull()) ? parentComponent.getRdbaConfig().getNullString() : null;
		DynamicResultSetTableModel drstm = ResultSetTableModelUtil.runSql(rdbaConnection, sqlStr, logWriter, parentComponent, true, nullString);
		if (drstm != null) {
			result = scriptTableUtil.createScriptTable(drstm);
			result.setDescription(sqlStr);
		}
		return result;
	}

	/**
	 * SQL文を実行してその結果をScriptTableとして取得します。
	 * '?'をプレースホルダとして利用できます。
	 * 
	 * @param sqlStr SQL文の文字列です。
	 * @param args プレースホルダに設定する値。配列、コレクション、数値型、文字列が指定できます。
	 * @return 実行結果のScriptTableです。実行するSQLがSELECT文でない場合はnullを返します。
	 * @throws SQLException SQLの実行時にエラーが発生した場合にスローされます。
	 */
	public ScriptTable runSql(String sqlStr, Object... args) throws SQLException {
		return this.runSql(this.rdbaConnection, sqlStr, args);
	}
	
	/**
	 * SQL文を実行してその結果をScriptTableとして取得します。
	 * '?'をプレースホルダとして利用できます。
	 * 
	 * @param rdbaConnection {@link #getRdbaConnection(String)}で取得したものを指定します。
	 * @param sqlStr SQL文の文字列です。
	 * @param args プレースホルダに設定する値。配列、コレクション、数値型、文字列が指定できます。
	 * @return 実行結果のScriptTableです。実行するSQLがSELECT文でない場合はnullを返します。
	 * @throws SQLException SQLの実行時にエラーが発生した場合にスローされます。
	 */
	public ScriptTable runSql(RdbaConnection rdbaConnection, String sqlStr, Object... args) throws SQLException {
		StringBuilder sb = new StringBuilder();
		int brace = -1;
		int holderCount = 0;
		for (int i=0;i<sqlStr.length();i++) {
			char ch = sqlStr.charAt(i);
			if (brace == -1) {
				if (ch == '\'' || ch == '\"') {
					brace = ch;
				}
				if (ch == '?') {
					if (holderCount >= args.length) {
						sb.append(ch);
					} else {
						sb.append(escapeSql(args[holderCount]));
						holderCount++;
					}
				} else {
					sb.append(ch);
				}
			} else {
				if (brace == (int)ch) {
					brace = -1;
				}
				sb.append(ch);
			}
		}
		
		return this.runSql(rdbaConnection, sb.toString());
	}

	/**
	 * 与えられたObjectをSQL文用に展開します。
	 * Number型はそのまま数値、String型はクオートされます。
	 * 配列とコレクションは( )で囲まれた形になります。<br/>
	 * 例1:123 → 123<br/>
	 * 例2:"abcdef" → "'abcdef'"<br/>
	 * 例3:{1234,5678} → "(1234,5678)"<br/>
	 * 例4:{"abc","def"} → "('abc','def')"<br/>
	 * 
	 * @param obj 入力値
	 * @return SQL文用の文字列
	 */
	public String escapeSql(Object obj) {
		StringBuilder sb = new StringBuilder();
		if (obj == null) {
			sb.append("NULL");
		} else if (obj instanceof Collection<?>) {
			boolean firstFlag = true;
			Collection<?> collection = (Collection<?>)obj;
			if (collection.size() > 0) {
				sb.append("(");
				for (Object o:collection) {
					String str = escapeSql(o);
					if (firstFlag) {
						firstFlag = false;
					} else {
						sb.append(',');
					}
					sb.append(str);
				}
				sb.append(")");
			} else {
				sb.append("NULL");
			}
		} else if (obj instanceof Object[]) {
			boolean firstFlag = true;
			Object[] collection = (Object[])obj;
			if (collection.length > 0) {
				sb.append("(");
				for (Object o:collection) {
					String str = escapeSql(o);
					if (firstFlag) {
						firstFlag = false;
					} else {
						sb.append(',');
					}
					sb.append(str);
				}
				sb.append(")");
			} else {
				sb.append("NULL");
			}
		} else if (obj instanceof Number) {
			Number num = (Number)obj;
			sb.append(num);
		} else {
			sb.append('\'');
			sb.append(obj.toString().replaceAll("'", "''"));
			sb.append('\'');
		}
		return sb.toString();
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
		return scriptTableUtil.createMarkedDiff(tbl1, tbl2, keyColumns);
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
		return scriptTableUtil.createMarkedDiff(tbl1, tbl2, keyColumnIndices);
	}

	/**
	 * ユーザーにダイアログを表示し、yes/no/cancel等の確認を行います。
	 * cancel時には例外をスローします。
	 * 
	 * @param message ダイアログに表示するメッセージ
	 * @return ユーザーがyesを選択した場合true,それ以外の場合はfalseを返します。
	 * @throws JspfException ユーザーがcancelを選択した場合にスローされます
	 */
	public boolean showConfirmDialog(String message) throws JspfException {
		int result = JOptionPane.showConfirmDialog((Component)parentComponent, message, MessageBundle.getInstance().getMessage("confirm"), JOptionPane.YES_NO_CANCEL_OPTION);
		if (result == JOptionPane.CANCEL_OPTION) {
			throw new JspfException(MessageBundle.getInstance().getMessage("canceled_by_user"));
		} else if (result == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ユーザーに確認用のダイアログを表示します。
	 * 
	 * @param message ダイアログに表示するメッセージ
	 */
	public void showMessageDialog(String message) {
		JOptionPane.showMessageDialog((Component)parentComponent, message);
	}
	
	/**
	 * ユーザーに入力用のダイアログを表示します。
	 * throwOnCancelがtrueの場合はキャンセル時に例外をスローします。
	 * 
	 * @param message ダイアログに表示するメッセージ
	 * @param initialValue 入力値の初期値
	 * @param throwOnCancel キャンセル時に例外をスローするようにします。
	 * @return 入力された場合はその文字列、キャンセル時はnullを返します。
	 * @throws JspfException throwOnCancelがtrueの場合、ユーザーがcancelを選択したするとスローされます
	 */
	public String showInputDialog(String message, String initialValue, boolean throwOnCancel) throws JspfException {
		String result = JOptionPane.showInputDialog((Component)parentComponent, message, initialValue);
		if (result == null) {
			if (throwOnCancel) {
				throw new JspfException(MessageBundle.getInstance().getMessage("canceled_by_user"));
			} else {
				return null;
			}
		}
		return result;
	}
	
	/**
	 * 単語連結をキャメルケースに変換します。<br/>
	 * 例:USER_TABLE_NAME → userTableName
	 * 
	 * @param composite 元となる単語連結
	 * @param ucc trueの場合は先頭を大文字にします。
	 * @return 変換されたキャメルケース
	 */
	public static String compositeToCamel(String composite, boolean ucc) {
		return StringUtil.compositeToCamel(composite, ucc);
	}
	
	/**
	 * キャメルケースを単語連結に変換します。<br/>
	 * 例:userTableName → USER_TABLE_NAME 
	 * 
	 * @param camel 元となるキャメルケース
	 * @return 変換された単語連結
	 */
	public static String camelToComposite(String camel) {
		return StringUtil.camelToComposite(camel);
	}
	
	/**
	 * {@link #getRdbaConnection(String)}で取得したコネクションなどを開放します�?
	 * スクリプト終�?��に自動的に実行される�?+	 */
	public void releaseAll() {
		for (RdbaConnection rc : rdbaConnectionBundle) {
			rc.dispose();
		}
		rdbaConnectionBundle.clear();
	}
	
	/**
	 * 登録されて�?��接続�?から別のコネクションを取得します�?
	 * 取得されたコネクションは {@link #runSql(RdbaConnection, String)} で使用することが�?来ます�?
	 * 
	 * @param label 接続�?リストに登録されて�?��ラベルを指定しま�?+	 * @return
	 * @throws JspfException 存在しな�?��ベルを指定した�?合や接続に失敗した�?合にスローされます�?
	 */
	public RdbaConnection getRdbaConnection(String label) throws JspfException {
		if (label == null) {
			label = "";
		}
		RdbaConfig rdbaConfig = parentComponent.getRdbaConfig();
		for (RdbaConnectionInfo rcInfo : rdbaConfig.getRdbaConnectionInfo()) {
			if (label.equals(rcInfo.getLabel())) {
				try {
					RdbaConnection rdbaConnection = rcInfo.createConnection(rdbaConfig.getRdbaJdbcBundle());
					rdbaConnectionBundle.add(rdbaConnection);
					return rdbaConnection;
				} catch (RdbaException e) {
					throw new JspfException(e);
				}
			}
		}
		throw new JspfException("No such label : " + label);
	}
}
