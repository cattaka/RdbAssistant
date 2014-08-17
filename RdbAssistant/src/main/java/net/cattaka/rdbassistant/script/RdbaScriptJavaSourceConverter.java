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
 * $Id: RdbaScriptJavaSourceConverter.java 278 2010-03-03 12:36:02Z cattaka $
 */
package net.cattaka.rdbassistant.script;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import net.cattaka.jspf.JspfBundle;
import net.cattaka.jspf.JspfEntry;
import net.cattaka.jspf.JspfException;
import net.cattaka.jspf.JspfJavaSourceConverter;
import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.script.core.RdbaScriptUtil;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.StringUtil;

public class RdbaScriptJavaSourceConverter implements JspfJavaSourceConverter<RdbaScriptUtil> {
	static class BodyPiace {
		private boolean source;
		private String body;
		public BodyPiace(boolean source, String body) {
			super();
			this.source = source;
			this.body = body;
		}
		public boolean isSource() {
			return source;
		}
		public void setSource(boolean source) {
			this.source = source;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
	};
	
	enum ParseMode {
		BODY,
		CODE_START1,
		CODE,
		CODE_END1
	};
	
	public void convertSourceFile(JspfBundle<RdbaScriptUtil> jspfBundle, JspfEntry<RdbaScriptUtil> jspfEntry) throws JspfException {
		try {
			StringBuilder importString = new StringBuilder();
			StringBuilder bodyString = new StringBuilder();

			// ソースを変換する
			processSourceText(bodyString, importString, jspfBundle, jspfEntry.getSourceText());
			
			// ベースを読み込み
			StringBuilder sb = new StringBuilder();
			InputStream scriptBaseIn = this.getClass().getClassLoader().getResourceAsStream(RdbaConstants.RDBA_SCRIPT_BASE);
			try {
				Reader reader = new InputStreamReader(scriptBaseIn, RdbaConstants.DEFAULT_CHAR_SET);
				int r;
				while((r = reader.read()) != -1 ) {
					sb.append((char)r);
				}
				reader.close();
			} finally {
				scriptBaseIn.close();
			}
			
			// Bodyの置換
			StringUtil.replaceString(sb,"/*BODY*/", bodyString.toString());
			StringUtil.replaceString(sb,"/*IMPORT*/", importString.toString());
			jspfEntry.setConvertedText(sb.toString());
		} catch (IOException e) {
			throw new JspfException(e);
		}
	}
	
	/**
	 * JSPFソースをスクリプトレットごとに分割する
	 * @param body
	 * @return
	 */
	private List<BodyPiace> splitSourceString(String body) throws JspfException {
		ArrayList<BodyPiace> result = new ArrayList<BodyPiace>();
		boolean sourceMode = false;
		boolean modeChange = false;
		StringBuilder sb = new StringBuilder();
		char lastCh=' ';
		for (int i=0;i<body.length();i++) {
			char ch = (char)body.charAt(i);
			if(sourceMode) {
				if (lastCh == '%') {
					if (ch == '>') {
						if (sb.length() > 0) {
							result.add(new BodyPiace(sourceMode, sb.toString()));
							sb.delete(0, sb.length());
						}
						modeChange = true;
					} else {
						sb.append(lastCh);
						sb.append(ch);
					}
				} else {
					if (ch == '%') {
						// 無し
					} else {
						sb.append(ch);
					}
				}
			} else {
				if (lastCh == '<') {
					if (ch == '%') {
//						if (sb.length() > 0 && sb.charAt(0) == '\n') {
//							sb.delete(0, 1);
//						}
						if (sb.length() > 0 && sb.charAt(sb.length()-1) == '\n') {
							sb.delete(sb.length()-1, sb.length());
						}
						if (sb.length() > 0) {
							result.add(new BodyPiace(sourceMode, sb.toString()));
							sb.delete(0, sb.length());
						}
						modeChange = true;
					} else {
						sb.append(lastCh);
						sb.append(ch);
					}
				} else {
					if (ch == '<') {
						// 無し
					} else {
						sb.append(ch);
					}
				}
			}
			if (modeChange) {
				modeChange = false;
				lastCh = ' ';
				sourceMode = !sourceMode;
			} else {
				lastCh = ch;
			}
		}
		if (sourceMode) {
			throw new JspfException(MessageBundle.getInstance().getMessage("scriptlet_is_not_closed"));
		}
		if (sb.length() > 0) {
			result.add(new BodyPiace(sourceMode, sb.toString()));
		}
		
		return result;
	}
	
	private void processSourceText(StringBuilder bodyOut, StringBuilder importOut, JspfBundle<RdbaScriptUtil> jspfBundle, String sourceText) throws JspfException {
		List<BodyPiace> bodyPieceList = splitSourceString(sourceText);
		for (BodyPiace bodyPiace:bodyPieceList) {
			processBodyPiace(bodyOut, importOut, jspfBundle, bodyPiace);
		}
	}
	
	private void processBodyPiace(StringBuilder bodyOut, StringBuilder importOut, JspfBundle<RdbaScriptUtil> jspfBundle, BodyPiace bodyPiace) throws JspfException {
		String body = bodyPiace.getBody();
		if (body == null) {
			return;
		}
		if (bodyPiace.isSource()) {
			if (body.length() > 0 && body.charAt(0) == '@') {
				processDirective(bodyOut, importOut, jspfBundle, body.substring(1,body.length()));
			} else if (body.length() > 0 && body.charAt(0) == '=') {
				bodyOut.append(processOutput(body.substring(1,body.length())));
				bodyOut.append('\n');
			} else {
				bodyOut.append(replaceQuotedReturn(body));
				bodyOut.append('\n');
			}
		} else {
			bodyOut.append("out.print(\"");
			bodyOut.append(StringUtil.escapeJavaString(body));
			bodyOut.append("\");\n");
		}
	}
	
	/**
	 * 与えられたソース内における、ダブルクオート内の改行を\nに置換する
	 * 
	 * @param source
	 * @return
	 */
	private String replaceQuotedReturn(String source) {
		if (source == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean doubleQuoted = false;
		boolean escaped = false;
		for (int i=0;i<source.length();i++) {
			char ch = (char)source.charAt(i);
			if (ch == '"') {
				if (!escaped) {
					doubleQuoted = !doubleQuoted;
				}
			}
			escaped = false;
			if (ch == '\\') {
				escaped = true;
			}
			if (doubleQuoted) {
				if (ch=='\n') {
					sb.append("\\n");
				} else {
					sb.append(ch);
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	/**
	 * ディレクティブを解釈します。
	 * 例：<%@ page import="java.util.ArrayList" %>などを
	 * Java用ソースコードに変換します。
	 * 
	 * @param source
	 * @return
	 */
	private void processDirective(StringBuilder bodyOut, StringBuilder importOut, JspfBundle<RdbaScriptUtil> jspfBundle, String source) throws JspfException {
		List<String[]> attrs = StringUtil.parseAttributeString(source);
		if (attrs == null || attrs.size() == 0) {
			return;
		}
		String[] modeStr = attrs.get(0);
		if ("page".equals(modeStr[0])) {
			for (String[] attr : attrs) {
				if ("import".equals(attr[0])) {
					importOut.append("import ");
					importOut.append(attr[1]);
					importOut.append(";\n");
				}
			}
		} else if ("include".equals(modeStr[0])) {
			for (String[] attr : attrs) {
				if ("file".equals(attr[0])) {
					String labelName = attr[1];
					String sourceText = getSourceText(jspfBundle, labelName);
					processSourceText(bodyOut, importOut, jspfBundle, sourceText);
				}
			}
		}
	}
	private String getSourceText(JspfBundle<RdbaScriptUtil> jspfBundle, String labelName) throws JspfException {
		StringBuilder sb = new StringBuilder();
		String fileName = jspfBundle.getWorkDir().getAbsolutePath() + File.separatorChar + RdbaConstants.RDBA_SCRIPT_DIR + File.separatorChar + labelName; 
		File file = new File(fileName);
		Reader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file), RdbaConstants.DEFAULT_CHAR_SET);
			int r = 0;
			while((r=reader.read()) != -1) {
				sb.append((char)r);
			}
		} catch (IOException e) {
			throw new JspfException(String.format(MessageBundle.getInstance().getMessage("could_not_open_file"), fileName));
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// ありえない
					ExceptionHandler.warn(e);
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 出力スクリプトを解釈します。
	 * 例：<%=arg%>などをJava用ソースコードに変換します。
	 * 
	 * @param source
	 * @return
	 */
	private String processOutput(String source) {
		return "out.print(String.valueOf("+source+"));";
	}
}
