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
 * $Id: RdbaSqlEditorPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */

package net.cattaka.rdbassistant.gui.sql;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.SQLException;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.rdbassistant.gui.RdbaTextInterface;
import net.cattaka.rdbassistant.gui.table.DynamicResultSetTableModel;
import net.cattaka.rdbassistant.gui.table.ResultSetTableModelUtil;
import net.cattaka.swing.JPopupMenuForStandardText;
import net.cattaka.swing.JTextPaneForLine;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.text.FindCondition;
import net.cattaka.swing.text.StdStyledDocument;
import net.cattaka.swing.text.StdTextComponent;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.PrintWriterEx;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.StringUtil;

public class RdbaSqlEditorPanel extends JPanel implements RdbaGuiInterface, RdbaTextInterface {
	private static final long serialVersionUID = 1L;

	private RdbaGuiInterface parentComponent;
	private JTextPaneForLine sqlLineNumPane;
	private SqlTextPanel sqlTextPane;
	private JTabbedPane resultTabbedPane;
	private ResultTablePanel resultTablePanel;
	private JTextArea resultLog;
	private JSplitPane splitPane;
	private RdbaConnection rdbaConnection;
	private DynamicResultSetTableModel currentResultSetTableModel;
	private File file;
	private Charset charset;
	private ModifiedListener modifiedlistener;
	private boolean modified = false;
	private FindCondition lastFindCondition;
	
	public enum RESULT_PANEL {
		RESULT_PANEL_TABLE,
		RESULT_PANEL_LOG,
		RESULT_PANEL_ALTERNATE,
	}
	
	class ButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("run_sql")) {
				runSql();
			} else if (e.getActionCommand().equals("show_search_replace")) {
				RdbaMessage rm = new RdbaMessage(RdbaMessageConstants.FINDCONDITIONDIALOG_SHOW, parentComponent, RdbaSqlEditorPanel.this, sqlTextPane.getSelectedText());
				sendRdbaMessage(rm);
			} else if (e.getActionCommand().equals("search_next")) {
				searchNext();
			} else if (e.getActionCommand().equals("search_prev")) {
				searchPrev();
			}
		}
	}
	
	class JPopupMenuForSqlModePanel extends JPopupMenuForStandardText {
		private static final long serialVersionUID = 1L;
		public JPopupMenuForSqlModePanel(StdTextComponent component, ActionListener al) {
			super(false);
			JMenuItem runSqlItem = new JMenuItem();
			runSqlItem.setActionCommand("run_sql");
			runSqlItem.addActionListener(al);
			ButtonsBundle.getInstance().applyButtonDifinition(runSqlItem, "run_sql");

			JMenuItem searchReplaceItem = new JMenuItem();
			JMenuItem searchNextItem = new JMenuItem();
			JMenuItem searchPrevItem = new JMenuItem();
			searchReplaceItem.setActionCommand("show_search_replace");
			searchNextItem.setActionCommand("search_next");
			searchPrevItem.setActionCommand("search_prev");
			searchReplaceItem.addActionListener(al);
			searchNextItem.addActionListener(al);
			searchPrevItem.addActionListener(al);
			ButtonsBundle.getInstance().applyMenuDifinition(searchReplaceItem, "search_replace");
			ButtonsBundle.getInstance().applyMenuDifinition(searchNextItem, "search_next");
			ButtonsBundle.getInstance().applyMenuDifinition(searchPrevItem, "search_prev");

			this.add(runSqlItem);
			this.addSeparator();
			this.createMenuItems();
			this.addSeparator();
			this.add(searchReplaceItem);
			this.add(searchNextItem);
			this.add(searchPrevItem);
		}
	}

	class ModifiedListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			updateEvent(e);
		}

		public void insertUpdate(DocumentEvent e) {
			updateEvent(e);
		}

		public void removeUpdate(DocumentEvent e) {
			updateEvent(e);
		}
		private void updateEvent(DocumentEvent e) {
			if (e.getType() == DocumentEvent.EventType.INSERT || e.getType() == DocumentEvent.EventType.REMOVE) {
				if (!modified) {
					// 変更されたことを親にコンポーネントに伝える。
					RdbaMessage rm = new RdbaMessage(RdbaMessageConstants.RDBASQLEDITORPANEL_MODIFIED, parentComponent, RdbaSqlEditorPanel.this, null);
					sendRdbaMessage(rm);
					modified = true;
				}
				// 行番号を更新
				{
					StdStyledDocument ssd = sqlTextPane.getStdStyledDocument();
					sqlLineNumPane.setLineCount(ssd.getLineCount());
				}
			}
		}
	}
	
	public RdbaSqlEditorPanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		this.lastFindCondition = new FindCondition();
		this.modifiedlistener = new ModifiedListener();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		ButtonAction al = new ButtonAction();
		JPopupMenuForSqlModePanel popup = new JPopupMenuForSqlModePanel(sqlTextPane, al);
		{
			// テキストエリアを作成
			StdScrollPane sqlTextScrollPane;
			sqlTextPane = new SqlTextPanel(popup);
			sqlTextScrollPane = new StdScrollPane();
			sqlLineNumPane = new JTextPaneForLine();

			sqlTextScrollPane.setViewportView(sqlTextPane);
			sqlTextScrollPane.setRowHeaderView(sqlLineNumPane);
			sqlTextPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			
			// 結果テーブルを作成。
			StdScrollPane resultLogScrollPane;
			resultTabbedPane = new JTabbedPane();
			resultTablePanel = new ResultTablePanel(this);
			resultTablePanel.getResultSetTable().setRdbaTextInterface(this);
			resultLog = new JTextArea();
			resultLog.setEditable(false);
			resultLogScrollPane = new StdScrollPane(resultLog);
			
			resultTabbedPane.add(resultTablePanel,MessageBundle.getInstance().getMessage("result_table"));
			resultTabbedPane.add(resultLogScrollPane,MessageBundle.getInstance().getMessage("result_log"));
			
			// スプリットパネルに上記で作成した物を設定。
			splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,sqlTextScrollPane, resultTabbedPane);
			splitPane.setTopComponent(sqlTextScrollPane);
			splitPane.setBottomComponent(resultTabbedPane);
		
			splitPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			this.add(splitPane);
		}
	}
	
	public void runSql() {
		String sqlText = sqlTextPane.getText();
		sqlText = StringUtil.removeCarriageReturn(sqlText);
		String[] sqlStrs = sqlText.split("\n/\n");
		for (int i=0;i<sqlStrs.length;i++) {
			sqlStrs[i] = sqlStrs[i].replace("\r", "");
		}
		boolean breakflag = false;
		
		appendResultLog(MessageBundle.getInstance().getMessage("result_separator_raw"));
		appendResultLog("\n");
		for (int i=0;!breakflag && i<sqlStrs.length;i++) {
			String sqlStr = sqlStrs[i];
			boolean finalSqlFlag = (i+1 == sqlStrs.length); 
			DynamicResultSetTableModel tmpResultSetTableModel = null;

			StringWriter stringWriter = new StringWriter();
			PrintWriterEx printWriter = new PrintWriterEx(stringWriter);
			try {
				String nullString = (getRdbaConfig().isDisplayStringIfNull()) ? getRdbaConfig().getNullString() : null;
				tmpResultSetTableModel = ResultSetTableModelUtil.runSql(rdbaConnection, sqlStr, printWriter, this, finalSqlFlag, nullString);
				printWriter.flush();
			} catch (SQLException e) {
				ExceptionHandler.info(e);
				breakflag = true;
			}
			appendResultLog(stringWriter.getBuffer().toString());
			
			if (finalSqlFlag) {
				if (tmpResultSetTableModel != null) {
					// 成功したのでテーブルを表示。
					this.currentResultSetTableModel = tmpResultSetTableModel;
					resultTablePanel.setResultSetTableModel(currentResultSetTableModel);
					switchResultPanel(RESULT_PANEL.RESULT_PANEL_TABLE);
				} else {
					// 成功したのでログを表示。
					switchResultPanel(RESULT_PANEL.RESULT_PANEL_LOG);
				}
			}
		}
	}
	
	public void doGuiLayout() {
		this.splitPane.setDividerLocation(0.5);
	}
	
	public RdbaConfig getRdbaConfig() {
		return this.parentComponent.getRdbaConfig();
	}
	
	public void reloadRdbaConfig() {
		this.resultTablePanel.reloadRdbaConfig();
		
		RdbaConfig rdbaConfig = getRdbaConfig();
		Font fontForEditor = rdbaConfig.getFontForEditor();
		this.sqlTextPane.setDocumentFont(fontForEditor, rdbaConfig.getCharactersPerTab());
		this.resultLog.setFont(fontForEditor);

		FontMetrics fontMetrics = this.sqlTextPane.getFontMetrics(fontForEditor);
		this.sqlLineNumPane.setFontMetrics(fontMetrics);
	}

	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		this.parentComponent.sendRdbaMessage(rdbaMessage);
	}
	public void relayRdbaMessage(RdbaMessage rdbaMessage) {
		// 無し
	}
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return this.parentComponent.getRdbaSingletonBundle();
	}

	public RdbaConnection getRdbConnection() {
		return rdbaConnection;
	}
	public void setRdbConnection(RdbaConnection rdbConnection) {
		StdStyledDocument styledDocument = rdbConnection.createNewSqlDocument();
		this.sqlTextPane.getDocument().removeDocumentListener(modifiedlistener);
		this.sqlTextPane.setStdStyledDocument(styledDocument);
		styledDocument.addDocumentListener(modifiedlistener);
		this.rdbaConnection = rdbConnection;
	}
	
	public void switchResultPanel(RESULT_PANEL resultPanel) {
		switch(resultPanel) {
		case RESULT_PANEL_TABLE :
			resultTabbedPane.setSelectedIndex(0);
			break;
		case RESULT_PANEL_LOG :
			resultTabbedPane.setSelectedIndex(1);
			break;
		case RESULT_PANEL_ALTERNATE:
			if (resultTabbedPane.getSelectedIndex() == 0) {
				resultTabbedPane.setSelectedIndex(1);
			} else {
				resultTabbedPane.setSelectedIndex(0);
			}
			break;
		}
	}
	
	/**
	 * カーソル位置に文字列を挿入する。
	 * もしカーソル位置が空白でなければ、カンマで区切った後に挿入する。
	 */
	public void appendString(String str) {
		try {
			StdStyledDocument doc = sqlTextPane.getStdStyledDocument();
			if (doc != null) {
				int pos = sqlTextPane.getCaretPosition();
				if (doc.getLength() == 0 || pos==0 || doc.getText(pos-1, 1).matches("[\\s,\\.]")) {
					doc.insertString(pos, str, null);
				} else {
					doc.insertString(pos, ",\n" + str, null);
				}
				sqlTextPane.requestFocusInWindow();
			}
		} catch (BadLocationException e) {
			// 何もしない
		}
	}
	
	public void appendResultLog(String str) {
		resultLog.append(str);
		resultLog.setCaretPosition(resultLog.getDocument().getLength());
	}
	
	public void setText(String text) {
		try {
			Document doc = sqlTextPane.getDocument();
			doc.remove(0, doc.getLength());
			doc.insertString(0, text, null);
		} catch (BadLocationException e) {
			// 起こりえないし起こると困る
			ExceptionHandler.fatal(e);
		}
	}
	
	public String getText() {
		String result = null;
		try {
			Document doc = sqlTextPane.getDocument();
			result = doc.getText(0, doc.getLength());
		} catch (BadLocationException e) {
			// 起こりえないし起こると困る
			ExceptionHandler.fatal(e);
		}
		return result;
	}
	
	public boolean loadSql(File file, Charset charset) {
		boolean result = false;
		try {
			Reader reader = new InputStreamReader(new FileInputStream(file), charset);
			StringBuilder sb = new StringBuilder();
			int r;
			while ((r = reader.read()) != -1) {
				sb.append((char)r);
			}
			reader.close();
			setText(sb.toString());
			setFile(file);
			setCharset(charset);
			this.sqlTextPane.getStdStyledDocument().resetUndo();
			modified = false;
			result = true;
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
		return result;
	}

	public boolean saveSql(File file, Charset charset) {
		boolean result = false;
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset);
			String text = getText();
			writer.write(text);
			writer.close();
			setFile(file);
			setCharset(charset);
			modified = false;
			result = true;
			sendRdbaMessage(new RdbaMessage(RdbaMessageConstants.RDBASQLQUICKACCESSPANEL_REFLESH, null, this, file.getParentFile()));
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
		return result;
	}
	
	public void doFindAction(FindCondition findCondition) {
		lastFindCondition = new FindCondition(findCondition);
		sqlTextPane.doFindAction(findCondition);
	}
	
	public void searchNext() {
		String str = sqlTextPane.getSelectedText();
		if (str != null && str.length() > 0) {
			lastFindCondition.setSearch(str);
			lastFindCondition.setAction(FindCondition.ACTION.FIND);
			lastFindCondition.setDownward(true);
		} else {
			lastFindCondition.setAction(FindCondition.ACTION.FIND);
			lastFindCondition.setDownward(true);
		}
		RdbaMessage rm = new RdbaMessage(RdbaMessageConstants.FINDCONDITIONDIALOG_DIRECT,null,null,lastFindCondition);
		sendRdbaMessage(rm);
	}
	public void searchPrev() {
		String str = sqlTextPane.getSelectedText();
		if (str != null && str.length() > 0) {
			lastFindCondition.setSearch(str);
			lastFindCondition.setAction(FindCondition.ACTION.FIND);
			lastFindCondition.setDownward(false);
		} else {
			lastFindCondition.setAction(FindCondition.ACTION.FIND);
			lastFindCondition.setDownward(false);
		}
		RdbaMessage rm = new RdbaMessage(RdbaMessageConstants.FINDCONDITIONDIALOG_DIRECT,null,null,lastFindCondition);
		sendRdbaMessage(rm);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	
	public boolean isModified() {
		return modified;
	}

	public void dispose() {
		if (currentResultSetTableModel != null) {
			// 無し
			currentResultSetTableModel = null;
		}
		// JRE1.5以前のメモリリーク対策
		this.sqlTextPane.getCaret().deinstall(this.sqlTextPane);
	}

	/** メニューイベント用 */
	public void openFindDialog() {
		String str = this.sqlTextPane.getSelectedText();
		RdbaMessage rm;
		if (str != null && str.length() > 0) {
			rm = new RdbaMessage(RdbaMessageConstants.FINDCONDITIONDIALOG_SHOW, null, this, str);
		} else {
			rm = new RdbaMessage(RdbaMessageConstants.FINDCONDITIONDIALOG_SHOW, null, this, null);
		}
		sendRdbaMessage(rm);
	}
	/** メニューイベント用 */
	public void doCommentOut() {
		sqlTextPane.doCommentOut(rdbaConnection.getLineComment());
	}
	/** メニューイベント用 */
	public boolean canRedo() {
		return sqlTextPane.canRedo();
	}
	/** メニューイベント用 */
	public boolean canUndo() {
		return sqlTextPane.canUndo();
	}
	/** メニューイベント用 */
	public void redo() {
		sqlTextPane.redo();
	}
	/** メニューイベント用 */
	public void undo() {
		sqlTextPane.undo();
	}
	/** メニューイベント用 */
	public void copy() {
		sqlTextPane.copy();
	}
	/** メニューイベント用 */
	public void cut() {
		sqlTextPane.cut();
	}
	/** メニューイベント用 */
	public void paste() {
		sqlTextPane.paste();
	}
	/** メニューイベント用 */
	public void selectAll() {
		sqlTextPane.selectAll();
	}
}
