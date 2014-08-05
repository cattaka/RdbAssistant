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
 * $Id: ResultSetTableModelUtil.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.rdbassistant.util.RdbaGuiUtil;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class ResultSetTableModelUtil {
	public static DynamicResultSetTableModel runSql(RdbaConnection rdbaConnection, String sqlStr, PrintWriter logWriter, RdbaGuiInterface parentComponent, boolean finalSqlFlag, String nullString) throws SQLException {
		DynamicResultSetTableModel tmpResultSetTableModel = null;
		
		logWriter.print(MessageBundle.getMessage("sql_result_separator_raw"));
		logWriter.print("\n");
			
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			logWriter.print(sqlStr);
			logWriter.print("\n");
			if (sqlStr.matches("\\s*")) {
				throw new SQLException("Query was empty");
			}
			stmt = rdbaConnection.getConnection().prepareStatement(sqlStr, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			long executeTime = System.currentTimeMillis();
			StatementExecuteThread statementExecuteThread = new StatementExecuteThread(stmt);
			statementExecuteThread.start();
			{
				try {
					// １秒待つ
					statementExecuteThread.join(1000);
				} catch (InterruptedException e) {
					// あり得ない
					ExceptionHandler.error(e);
				}
				
				// もし1秒以上かかっているならキャンセル用ダイアログ表示
				if (statementExecuteThread.isAlive()) {
					Frame parentFrame = RdbaGuiUtil.getParentFrame((Container)parentComponent);
					CancelExecuteStatementDialog cesd = new CancelExecuteStatementDialog(parentFrame, statementExecuteThread, executeTime);
					cesd.setModal(true);
					cesd.setLocationRelativeTo((Component)parentComponent);
					cesd.setVisible(true);
					cesd.dispose();
				}
			}
			
			// 例外が発生していないかチェック
			if (statementExecuteThread.getSqlException() != null) {
				SQLException orig = statementExecuteThread.getSqlException();
				SQLException se = new SQLException(orig.getMessage(), orig.getSQLState(), orig.getErrorCode());
				se.setNextException(statementExecuteThread.getSqlException());
				throw se;
			}
			
			if (statementExecuteThread.getExecuteResult()) {
				rs = stmt.getResultSet();
			}
			
			int updateCount = stmt.getUpdateCount();
			long extractionTime = System.currentTimeMillis();
			executeTime = extractionTime - executeTime;
			
			if (rs != null) {
				if (!finalSqlFlag) {
					// 連続実行中のため結果は取得しない
					String str = String.format(MessageBundle.getMessage("result_set_was_ignored"), executeTime);
					logWriter.print(str);
					logWriter.print("\n");
					logWriter.print("\n");
				} else {
					tmpResultSetTableModel = new DynamicResultSetTableModel();
					DynamicResultSetTableModel.ExtractResultSetDataThread extractThread = tmpResultSetTableModel.createExtractResultSetData(rdbaConnection, rs, nullString);
					// 抽出開始(別スレッド)
					extractThread.start();
					{
						try {
							// １秒待つ
							extractThread.join(1000);
						} catch (InterruptedException e) {
							// あり得ない
							ExceptionHandler.error(e);
						}
						
						// もし1秒以上かかっているならキャンセル用ダイアログ表示
						if (extractThread.isAlive()) {
							Frame parentFrame = RdbaGuiUtil.getParentFrame((Container)parentComponent);
							CancelExtractResultSetDialog cersd = new CancelExtractResultSetDialog(parentFrame, extractThread, extractionTime);
							cersd.setModal(true);
							cersd.setLocationRelativeTo((Component)parentComponent);
							cersd.setVisible(true);
							cersd.dispose();
						}
					}
					
					// 例外が発生していないかチェック
					if (extractThread.getSqlException() != null) {
						SQLException orig = extractThread.getSqlException();
						SQLException se = new SQLException(orig.getMessage(), orig.getSQLState(), orig.getErrorCode());
						se.setNextException(extractThread.getSqlException());
						throw se;
					}
					
					extractionTime = System.currentTimeMillis() - extractionTime;
					
					String statusBarMessage = "";
					if (extractThread.isCanceled()) {
						String str = MessageBundle.getMessage("the_extraction_was_canceled");
						logWriter.print(str);
						logWriter.print("\n");
						statusBarMessage += str;
					}
					{
						String str = String.format(MessageBundle.getMessage("rows_in_set"), tmpResultSetTableModel.getRowCount(), executeTime, extractionTime);
						logWriter.print(str);
						logWriter.print("\n");
						logWriter.print("\n");
						statusBarMessage += str;
					}
					// ステータスバーに表示
					parentComponent.sendRdbaMessage(new RdbaMessage(RdbaMessageConstants.RDBASTATUSBAR_MESSAGE, null, parentComponent, statusBarMessage));
				}
			} else {
				// 更新ログ
				String statusBarMessage = "";
				if (updateCount == -1) {
					String str = String.format(MessageBundle.getMessage("statement_was_executed"), executeTime);
					logWriter.print(str);
					logWriter.print("\n");
					logWriter.print("\n");
					statusBarMessage += str;
				} else {
					String str = String.format(MessageBundle.getMessage("rows_updated"), updateCount, executeTime);
					logWriter.print(str);
					logWriter.print("\n");
					logWriter.print("\n");
					statusBarMessage += str;
				}
				
				// ステータスバーに表示
				parentComponent.sendRdbaMessage(new RdbaMessage(RdbaMessageConstants.RDBASTATUSBAR_MESSAGE, null, parentComponent, statusBarMessage));
			}
		} catch(SQLException exc) {
			logWriter.print(exc.getMessage());
			logWriter.print("\n");
			logWriter.print("\n");
			throw exc;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// 起こりえないし。起こっても無視。
					ExceptionHandler.warn(e);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// 起こりえないし。起こっても無視。
					ExceptionHandler.warn(e);
				}
			}
		}
		logWriter.print(rdbaConnection.getLastDbmsOutput());
		
		return tmpResultSetTableModel;
	}
}
