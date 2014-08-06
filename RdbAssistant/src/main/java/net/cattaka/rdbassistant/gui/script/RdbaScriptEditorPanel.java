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
 * $Id: RdbaScriptEditorPanel.java 436 2011-04-14 16:04:17Z cattaka $
 */

package net.cattaka.rdbassistant.gui.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

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

import net.cattaka.jspf.JspfBundle;
import net.cattaka.jspf.JspfEntry;
import net.cattaka.jspf.JspfException;
import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.rdbassistant.gui.RdbaTextInterface;
import net.cattaka.rdbassistant.gui.table.DynamicResultSetTableModel;
import net.cattaka.rdbassistant.script.core.RdbaScriptUtil;
import net.cattaka.rdbassistant.script.core.ScriptTable;
import net.cattaka.rdbassistant.script.core.ScriptTableModel;
import net.cattaka.rdbassistant.script.core.ScriptTableUtil;
import net.cattaka.swing.JPopupMenuForStandardText;
import net.cattaka.swing.JTextPaneForLine;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.text.FindCondition;
import net.cattaka.swing.text.StdStyledDocument;
import net.cattaka.swing.text.StdTextComponent;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.ExceptionUtil;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.PrintWriterEx;

import org.codehaus.groovy.control.CompilationFailedException;

public class RdbaScriptEditorPanel extends JPanel implements RdbaGuiInterface, RdbaTextInterface {
	private static final long serialVersionUID = 1L;

	private RdbaGuiInterface parentComponent;
	private JTextPaneForLine scriptLineNumPane;
	private ScriptTextPanel scriptTextPane;
	private JTabbedPane resultTabbedPane;
	private ResultTablePanel outputTablePanel;
	private JTextArea resultLog;
	private JTextArea compiledSource;
	private JTextArea outputLog;
	private JSplitPane splitPane;
	private RdbaConnection rdbaConnection;
	private DynamicResultSetTableModel currentResultSetTableModel;
	private File file;
	private Charset charset;
	private ModifiedListener modifiedlistener;
	private boolean modified = false;
	private FindCondition lastFindCondition;
	
	public enum RESULT_PANEL {
		RESULT_PANEL_RESULT_LOG,
		RESULT_PANEL_SOURCE,
		RESULT_PANEL_OUTPUT_TABLE,
		RESULT_PANEL_OUTPUT_LOG,
		RESULT_PANEL_ALTERNATE,
	}
	
	class ButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("run_script")) {
				runScript();
			} else if (e.getActionCommand().equals("compile_script")) {
				compileScript();
			} else if (e.getActionCommand().equals("show_search_replace")) {
				RdbaMessage rm = new RdbaMessage(RdbaMessageConstants.FINDCONDITIONDIALOG_SHOW, parentComponent, RdbaScriptEditorPanel.this, scriptTextPane.getSelectedText());
				sendRdbaMessage(rm);
			} else if (e.getActionCommand().equals("search_next")) {
				searchNext();
			} else if (e.getActionCommand().equals("search_prev")) {
				searchPrev();
			}
		}
	}
	
	class JPopupMenuForScriptModePanel extends JPopupMenuForStandardText {
		private static final long serialVersionUID = 1L;
		public JPopupMenuForScriptModePanel(StdTextComponent component, ActionListener al) {
			super(false);
			JMenuItem runScriptItem = new JMenuItem();
			runScriptItem.setActionCommand("run_script");
			runScriptItem.addActionListener(al);
			ButtonsBundle.applyButtonDifinition(runScriptItem, "run_script");

			JMenuItem searchReplaceItem = new JMenuItem();
			JMenuItem searchNextItem = new JMenuItem();
			JMenuItem searchPrevItem = new JMenuItem();
			searchReplaceItem.setActionCommand("show_search_replace");
			searchNextItem.setActionCommand("search_next");
			searchPrevItem.setActionCommand("search_prev");
			searchReplaceItem.addActionListener(al);
			searchNextItem.addActionListener(al);
			searchPrevItem.addActionListener(al);
			ButtonsBundle.applyMenuDifinition(searchReplaceItem, "search_replace");
			ButtonsBundle.applyMenuDifinition(searchNextItem, "search_next");
			ButtonsBundle.applyMenuDifinition(searchPrevItem, "search_prev");

			this.add(runScriptItem);
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
					RdbaMessage rm = new RdbaMessage(RdbaMessageConstants.RDBASQLEDITORPANEL_MODIFIED, parentComponent, RdbaScriptEditorPanel.this, null);
					sendRdbaMessage(rm);
					modified = true;
				}
				// 行番号を更新
				{
					StdStyledDocument ssd = scriptTextPane.getStdStyledDocument();
					scriptLineNumPane.setLineCount(ssd.getLineCount());
				}
			}
		}
	}
	
	public RdbaScriptEditorPanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		this.lastFindCondition = new FindCondition();
		this.modifiedlistener = new ModifiedListener();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		ButtonAction al = new ButtonAction();
		JPopupMenuForScriptModePanel popup = new JPopupMenuForScriptModePanel(scriptTextPane, al);
		{
			// テキストエリアを作成
			StdScrollPane scriptTextScrollPane;
			scriptTextPane = new ScriptTextPanel(popup);
			scriptTextScrollPane = new StdScrollPane();
			scriptLineNumPane = new JTextPaneForLine();

			scriptTextScrollPane.setViewportView(scriptTextPane);
			scriptTextScrollPane.setRowHeaderView(scriptLineNumPane);
			scriptTextPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			
			// 結果テーブルを作成。
			StdScrollPane resultLogScrollPane;
			StdScrollPane outputLogScrollPane;
			StdScrollPane compiledSourceScrollPane;
			resultTabbedPane = new JTabbedPane();
			outputTablePanel = new ResultTablePanel(this);
			outputTablePanel.getResultSetTable().setRdbaTextInterface(this);
			resultLog = new JTextArea();
			resultLog.setEditable(false);
			outputLog = new JTextArea();
			outputLog.setEditable(false);
			resultLogScrollPane = new StdScrollPane(resultLog);
			outputLogScrollPane = new StdScrollPane(outputLog);
			compiledSource = new JTextArea();
			compiledSource.setEditable(false);
			compiledSourceScrollPane = new StdScrollPane(compiledSource);
			
			resultTabbedPane.add(resultLogScrollPane,MessageBundle.getMessage("result_log"));
			resultTabbedPane.add(compiledSourceScrollPane,MessageBundle.getMessage("compiled_source"));
			resultTabbedPane.add(outputTablePanel,MessageBundle.getMessage("output_table"));
			resultTabbedPane.add(outputLogScrollPane,MessageBundle.getMessage("output_log"));
			
			// スプリットパネルに上記で作成した物を設定。
			splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scriptTextScrollPane, resultTabbedPane);
			splitPane.setTopComponent(scriptTextScrollPane);
			splitPane.setBottomComponent(resultTabbedPane);
		
			splitPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			this.add(splitPane);
		}
	}
	
	/**
	 * コンパイルを行う。
	 * 
	 * @return 成功ならtrue、それ以外はfalse
	 */
	public JspfEntry<RdbaScriptUtil> compileScript() {
		String labelName = "";
		if (this.file != null) {
			labelName = file.getAbsolutePath();
		}
		String sourceText = scriptTextPane.getText();
		return compileScript(labelName, sourceText);
	}
	/**
	 * コンパイルを行う。
	 * 
	 * @return 成功ならtrue、それ以外はfalse
	 */
	public JspfEntry<RdbaScriptUtil> compileScript(String labelName, String sourceText) {
		appendResultLog(MessageBundle.getMessage("compile_result_separator_raw"));
		appendResultLog("\n");
		StringWriter resultSw = new StringWriter();
		PrintWriterEx resultWriter = new PrintWriterEx(resultSw);
		JspfBundle<RdbaScriptUtil> scriptBundle = getRdbaSingletonBundle().getScriptBundle();
		JspfEntry<RdbaScriptUtil> result = null;
		try {
			result = scriptBundle.compile(labelName, sourceText, resultWriter);
		} catch (JspfException e) {
			resultWriter.append(e.getMessage());
			resultWriter.append('\n');
			ExceptionHandler.info(e);
			this.switchResultPanel(RESULT_PANEL.RESULT_PANEL_RESULT_LOG);
		}
		resultWriter.flush();
		String compileLog = resultSw.getBuffer().toString();
		appendResultLog(compileLog);
		if (compileLog.length() > 0) {
			appendResultLog("\n");
		}
		
		if (result != null) {
			appendResultLog(MessageBundle.getMessage("compile_script_succeed"));
			appendResultLog("\n");
		} else {
			appendResultLog(MessageBundle.getMessage("compile_script_failed"));
			appendResultLog("\n");
		}
		
		appendResultLog("\n");
		this.setCompiledSource(result);
		return result;
	}
	
	public void runScript() {
		String labelName = "";
		if (this.file != null) {
			labelName = file.getAbsolutePath();
		}
		JspfEntry<RdbaScriptUtil> jspfEntry = null;
		{
			String sourceText = scriptTextPane.getText();
			jspfEntry = compileScript(labelName, sourceText);
		}
		
		if (jspfEntry != null) {
			boolean result = true;
			ScriptTable table = new ScriptTable(64,64);
			StringWriter resultSw = new StringWriter();
			PrintWriterEx resultWriter = new PrintWriterEx(resultSw);
			StringWriter outputSw = new StringWriter();
			PrintWriterEx outputWriter = new PrintWriterEx(outputSw);
			
			appendResultLog(MessageBundle.getMessage("script_result_separator_raw"));
			appendResultLog("\n");
			
			RdbaScriptUtil util = new RdbaScriptUtil();
			util.initialize(getRdbConnection(), resultWriter, outputWriter, table, parentComponent);
			
			Binding binding = new Binding();
//			binding.setVariable("rdbaConnection", rdbaConnection);
//			binding.setVariable("logWriter", resultWriter);
//			binding.setVariable("parentComponent", parentComponent);
			binding.setVariable("scriptTableUtil", new ScriptTableUtil());
			binding.setVariable("out", outputWriter);
			binding.setVariable("table", table);
			binding.setVariable("util", util);
			GroovyShell groovyEngine = new GroovyShell(binding);
			
			try {
				Script script = groovyEngine.parse(jspfEntry.getConvertedText());
				script.run();
			} catch (CompilationFailedException e) {
				result = false;
				resultWriter.append(e.getMessage());
				resultWriter.append('\n');
				ExceptionHandler.info(e);
				this.switchResultPanel(RESULT_PANEL.RESULT_PANEL_RESULT_LOG);
			} catch (Exception e) {
				result = false;
				resultWriter.println(ExceptionUtil.toString(e));
				ExceptionHandler.info(e);
				this.switchResultPanel(RESULT_PANEL.RESULT_PANEL_RESULT_LOG);
			} finally {
				util.releaseAll();
			}
			// 出力を表示
			outputWriter.flush();
			this.outputLog.setText(outputSw.getBuffer().toString());
			this.outputTablePanel.setResultSetTableModel(new ScriptTableModel(table));

			resultWriter.flush();
			
			String scriptLog = resultSw.getBuffer().toString();
			appendResultLog(scriptLog);
			if (scriptLog.length() > 0) {
				appendResultLog("\n");
			}
			if (result) {
				appendResultLog(MessageBundle.getMessage("executing_script_succeed"));
			} else {
				appendResultLog(MessageBundle.getMessage("executing_script_failed"));
			}
			appendResultLog("\n");
			appendResultLog("\n");
		} else {
			// コンパイルに失敗している。
		}
	}
	
	public void doGuiLayout() {
		this.splitPane.setDividerLocation(0.5);
	}
	
	public RdbaConfig getRdbaConfig() {
		return this.parentComponent.getRdbaConfig();
	}
	
	public void reloadRdbaConfig() {
		this.outputTablePanel.reloadRdbaConfig();
		
		RdbaConfig rdbaConfig = getRdbaConfig();
		Font fontForEditor = rdbaConfig.getFontForEditor();
		this.scriptTextPane.setDocumentFont(fontForEditor, rdbaConfig.getCharactersPerTab());
		this.resultLog.setFont(fontForEditor);
		this.compiledSource.setFont(fontForEditor);
		this.outputLog.setFont(fontForEditor);

		FontMetrics fontMetrics = this.scriptTextPane.getFontMetrics(fontForEditor);
		this.scriptLineNumPane.setFontMetrics(fontMetrics);
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
		StdStyledDocument styledDocument = new ScriptStyledDocument();
		this.scriptTextPane.getDocument().removeDocumentListener(modifiedlistener);
		this.scriptTextPane.setStdStyledDocument(styledDocument);
		styledDocument.addDocumentListener(modifiedlistener);
		this.rdbaConnection = rdbConnection;
	}
	
	public void switchResultPanel(RESULT_PANEL resultPanel) {
		switch(resultPanel) {
		case RESULT_PANEL_RESULT_LOG:
			resultTabbedPane.setSelectedIndex(0);
			break;
		case RESULT_PANEL_SOURCE:
			resultTabbedPane.setSelectedIndex(1);
			break;
		case RESULT_PANEL_OUTPUT_TABLE:
			resultTabbedPane.setSelectedIndex(2);
			break;
		case RESULT_PANEL_OUTPUT_LOG:
			resultTabbedPane.setSelectedIndex(3);
			break;
		case RESULT_PANEL_ALTERNATE:
			int idx = resultTabbedPane.getSelectedIndex();
			idx++;
			if (idx > 3) {
				idx = 0;
			}
			resultTabbedPane.setSelectedIndex(idx);
			break;
		}
	}
	
	/**
	 * カーソル位置に文字列を挿入する。
	 * もしカーソル位置が空白でなければ、カンマで区切った後に挿入する。
	 */
	public void appendString(String str) {
		try {
			StdStyledDocument doc = scriptTextPane.getStdStyledDocument();
			if (doc != null) {
				int pos = scriptTextPane.getCaretPosition();
				if (doc.getLength() == 0 || pos==0 || doc.getText(pos-1, 1).matches("[\\s,\\.]")) {
					doc.insertString(pos, str, null);
				} else {
					doc.insertString(pos, ",\n" + str, null);
				}
				scriptTextPane.requestFocusInWindow();
			}
		} catch (BadLocationException e) {
			// 何もしない
		}
	}
	
	public void appendResultLog(String str) {
		resultLog.append(str);
		resultLog.setCaretPosition(resultLog.getDocument().getLength());
	}
	
	public void setCompiledSource(JspfEntry<?> entry) {
		this.compiledSource.setText((entry != null && entry.getConvertedText() != null)?entry.getConvertedText() : "" );
	}
	
	public void setText(String text) {
		try {
			Document doc = scriptTextPane.getDocument();
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
			Document doc = scriptTextPane.getDocument();
			result = doc.getText(0, doc.getLength());
		} catch (BadLocationException e) {
			// 起こりえないし起こると困る
			ExceptionHandler.fatal(e);
		}
		return result;
	}
	
	public boolean loadScript(File file, Charset charset) {
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
			this.scriptTextPane.getStdStyledDocument().resetUndo();
			modified = false;
			result = true;
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
		return result;
	}

	public boolean saveScript(File file, Charset charset) {
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
		scriptTextPane.doFindAction(findCondition);
	}
	
	public void searchNext() {
		String str = scriptTextPane.getSelectedText();
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
		String str = scriptTextPane.getSelectedText();
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
		this.scriptTextPane.getCaret().deinstall(this.scriptTextPane);
	}

	/** メニューイベント用 */
	public void openFindDialog() {
		String str = this.scriptTextPane.getSelectedText();
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
		scriptTextPane.doCommentOut("//");
	}
	/** メニューイベント用 */
	public boolean canRedo() {
		return scriptTextPane.canRedo();
	}
	/** メニューイベント用 */
	public boolean canUndo() {
		return scriptTextPane.canUndo();
	}
	/** メニューイベント用 */
	public void redo() {
		scriptTextPane.redo();
	}
	/** メニューイベント用 */
	public void undo() {
		scriptTextPane.undo();
	}
	/** メニューイベント用 */
	public void copy() {
		scriptTextPane.copy();
	}
	/** メニューイベント用 */
	public void cut() {
		scriptTextPane.cut();
	}
	/** メニューイベント用 */
	public void paste() {
		scriptTextPane.paste();
	}
	/** メニューイベント用 */
	public void selectAll() {
		scriptTextPane.selectAll();
	}
}
