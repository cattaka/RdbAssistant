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
 * $Id: SqliteStyledDocument.java 258 2010-02-27 13:43:19Z cattaka $
 */
package net.cattaka.rdbassistant.driver.sqlite;

import java.awt.Color;
import java.awt.FontMetrics;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import net.cattaka.rdbassistant.RdbaConfigConstants;
import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.swing.text.AbstractStdStyledDocument;

public class SqliteStyledDocument extends AbstractStdStyledDocument {
	private static final long serialVersionUID = 1L;
	
	static final char CT_UNKNOWN = 0;
	static final char CT_WORD = 1;
	static final char CT_SPACE = 2;
	static final char CT_SLASH = 3;
	static final char CT_HYPHEN = 4;
	static final char CT_COMMENT1_START1 = 6;
	static final char CT_COMMENT1_START2 = 7;
	static final char CT_COMMENT1_CONTENT = 8;
	static final char CT_COMMENT1_END1 = 9;
	static final char CT_COMMENT1_END2 = 10;
	static final char CT_COMMENT2_START1 = 11;
	static final char CT_COMMENT2_START2 = 12;
	static final char CT_COMMENT2_CONTENT = 14;
	static final char CT_COMMENT2_END = 15;
	static final char CT_SQUOTED_START = 16;
	static final char CT_SQUOTED_CONTENT = 17;
	static final char CT_SQUOTED_END = 18;
	static final char CT_DQUOTED_START = 19;
	static final char CT_DQUOTED_CONTENT = 20;
	static final char CT_DQUOTED_END = 21;
	
	public static final String DELIMITOR_CHARS = " \n\t!#$%&()*+,-.:;<=>?@[\\]^`{|}~";
	
	Style regureStyle;
	Style reservedStyle;
	Style commentStyle;
	Style quotedStyle;
	
	public SqliteStyledDocument() {
		super();
		initialize();
	}

	public SqliteStyledDocument(Content c, StyleContext styles) {
		super(c, styles);
		initialize();
	}

	public SqliteStyledDocument(StyleContext styles) {
		super(styles);
		initialize();
	}

	private void initialize() {
		this.setFontMetrics(null, 4);
	}
	
	public void setFontMetrics(FontMetrics fontMetrics, int charactersPerNum) {
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		if (fontMetrics != null) {
			StyleConstants.setFontFamily(def, fontMetrics.getFont().getFamily());
			StyleConstants.setFontSize(def, fontMetrics.getFont().getSize());
		} else {
			StyleConstants.setFontFamily(def, RdbaConfigConstants.DEFAULT_FONT_EDITOR);
		}
		
		// タブストップの設定
		int charWidth = 10;
		int tabLength = charWidth * 8;
		if (fontMetrics != null) {
			String str = RdbaConstants.STRING_FOR_MESURE_WIDTH;
			charWidth = fontMetrics.stringWidth(str) / str.length();
		}		
		tabLength = charWidth * charactersPerNum;
		TabStop[] tabs = new TabStop[20];
		for(int j=0;j<tabs.length;j++) {
		  tabs[j] = new TabStop((j+1)*tabLength);
		}
		TabSet tabSet = new TabSet(tabs);
		StyleConstants.setTabSet(def, tabSet);
		
		// 残りのスタイルを作成
		regureStyle = this.addStyle("regure", def);
		reservedStyle = this.addStyle("reserved", def);
		commentStyle = this.addStyle("comment", def);
		quotedStyle = this.addStyle("quoted", def);

		StyleConstants.setForeground(reservedStyle,new Color(255,0,0));
		StyleConstants.setForeground(commentStyle,new Color(0,128,0));
		StyleConstants.setForeground(quotedStyle,new Color(0,0,255));
		
		this.setLogicalStyle(0, regureStyle);
		this.updateAttributes(0, getLength());
	}
	
	/**
	 * @param start 開始地点
	 * @param tail ドキュメントの終端
	 */
	protected void updateAttributes(int start, int tail) {
		if (attributeBuffer.length() == 0) {
			return;
		}
		
		int updateStart = start;
		int updateTail = tail;
		{
			char lastAttr = CT_SPACE;
			updateStart = start-1;
			if (updateStart > 0) {
				attributeBuffer.setCharAt(updateStart, CT_UNKNOWN);
				lastAttr = attributeBuffer.charAt(updateStart-1);
			} else {
				attributeBuffer.setCharAt(0, CT_UNKNOWN);
				lastAttr = CT_SPACE;
				updateStart = 0;
			}

			// 属性バッファの更新
			for (int i=updateStart;i<tail;i++) {
				char ch = charBuffer.charAt(i);
				
				char nextAttr = CT_UNKNOWN;
				switch(lastAttr) {
				case CT_UNKNOWN:
					throw new RuntimeException("error");
				case CT_WORD:
				case CT_SPACE:
				case CT_COMMENT1_END2:
				case CT_COMMENT2_END:
				case CT_SQUOTED_END:
				case CT_DQUOTED_END:
					if (ch == '/') {
						nextAttr = CT_SLASH;
					} else if (ch == '-') {
						nextAttr = CT_HYPHEN;
					} else if (ch == '\'') {
						nextAttr = CT_SQUOTED_START;
					} else if (ch == '"') {
						nextAttr = CT_DQUOTED_START;
					} else if (DELIMITOR_CHARS.indexOf(ch) != -1) {
						nextAttr = CT_SPACE;
					} else {
						nextAttr = CT_WORD;
					}
					break;
				case CT_COMMENT1_START1:
				case CT_SLASH:
					if (ch == '*') {
						attributeBuffer.setCharAt(i-1, CT_COMMENT1_START1);
						nextAttr = CT_COMMENT1_START2;
					} else {
						if (ch == '/') {
							nextAttr = CT_SLASH;
						} else if (ch == '-') {
							nextAttr = CT_HYPHEN;
						} else if (ch == '\'') {
							nextAttr = CT_SQUOTED_START;
						} else if (ch == '"') {
							nextAttr = CT_DQUOTED_START;
						} else if (DELIMITOR_CHARS.indexOf(ch) != -1) {
							nextAttr = CT_SPACE;
						} else {
							nextAttr = CT_WORD;
						}
					}
					break;
				case CT_HYPHEN:
				case CT_COMMENT2_START1:
					if (ch == '-') {
						attributeBuffer.setCharAt(i-1, CT_COMMENT2_START1);
						nextAttr = CT_COMMENT2_START2;
					} else {
						if (ch == '/') {
							nextAttr = CT_SLASH;
						} else if (ch == '-') {
							nextAttr = CT_HYPHEN;
						} else if (ch == '\'') {
							nextAttr = CT_SQUOTED_START;
						} else if (ch == '"') {
							nextAttr = CT_DQUOTED_START;
						} else if (DELIMITOR_CHARS.indexOf(ch) != -1) {
							nextAttr = CT_SPACE;
						} else {
							nextAttr = CT_WORD;
						}
					}
					break;
				case CT_COMMENT1_START2:
				case CT_COMMENT1_CONTENT:
					if (ch == '*') {
						nextAttr = CT_COMMENT1_END1;
					} else {
						nextAttr = CT_COMMENT1_CONTENT;
					}
					break;
				case CT_COMMENT1_END1:
					if (ch == '/') {
						nextAttr = CT_COMMENT1_END2;
					} else {
						nextAttr = CT_COMMENT1_CONTENT;
					}
					break;
				case CT_COMMENT2_START2:
				case CT_COMMENT2_CONTENT:
					if (ch == '\n') {
						nextAttr = CT_COMMENT2_END;
					} else {
						nextAttr = CT_COMMENT2_CONTENT;
					}
					break;
				case CT_SQUOTED_START:
				case CT_SQUOTED_CONTENT:
					if (ch == '\'') {
						nextAttr = CT_SQUOTED_END;
					} else {
						nextAttr = CT_SQUOTED_CONTENT;
					}
					break;
				case CT_DQUOTED_START:
				case CT_DQUOTED_CONTENT:
					if (ch == '"') {
						nextAttr = CT_DQUOTED_END;
					} else {
						nextAttr = CT_DQUOTED_CONTENT;
					}
					break;
				}
				
				// 変わってなければ終了
				if (attributeBuffer.charAt(i) == nextAttr) {
					updateTail = i;
					break;
				}
				attributeBuffer.setCharAt(i, nextAttr);
				lastAttr = nextAttr;
			}
		}
		
		this.writeLock();
		
		// 表示スタイルの更新
		try {
			char lastAttr = CT_UNKNOWN;
			char nextAttr = CT_UNKNOWN;
			
			lastAttr = attributeBuffer.charAt(updateStart);
			nextAttr = attributeBuffer.charAt(updateStart);
			while (updateStart >= 0 && attributeBuffer.charAt(updateStart) == lastAttr) {
				updateStart--;
			}
			updateStart++;
			while (updateTail < tail && attributeBuffer.charAt(updateTail) == nextAttr) {
				updateTail++;
			}
			int wordStart = updateStart;
			int wordEnd = updateTail;
			
			//long ctm = System.currentTimeMillis();
			for (int i=updateStart;i<=updateTail;i++) {
				Style style = regureStyle;
				if (i<tail) {
					nextAttr = attributeBuffer.charAt(i);
				} else {
					nextAttr = CT_SPACE;
				}
				switch(lastAttr) {
				case CT_UNKNOWN:
					style = regureStyle;
				case CT_WORD:
				case CT_SPACE:
				case CT_SLASH:
					style = regureStyle;
					break;
				case CT_COMMENT1_START1:
				case CT_COMMENT1_START2:
				case CT_COMMENT2_START1:
				case CT_COMMENT2_START2:
					style = commentStyle;
					break;
				case CT_COMMENT1_CONTENT:
				case CT_COMMENT1_END1:
				case CT_COMMENT1_END2:
				case CT_COMMENT2_CONTENT:
				case CT_COMMENT2_END:
					style = commentStyle;
					break;
				case CT_SQUOTED_START:
				case CT_SQUOTED_CONTENT:
				case CT_SQUOTED_END:
					style = quotedStyle;
					break;
				case CT_DQUOTED_START:
				case CT_DQUOTED_CONTENT:
				case CT_DQUOTED_END:
					style = quotedStyle;
					break;
				}
				
				//this.setCharacterAttributes(i, 1, style, true);
				
				if (lastAttr != nextAttr) {
					wordEnd = i;
					assert(wordStart < wordEnd);
					this.setCharacterAttributes(wordStart, wordEnd - wordStart, style, true);
					if (lastAttr == CT_WORD) {
						String word = charBuffer.substring(wordStart, wordEnd);
						if (reservedWords != null && reservedWords.contains(word.toUpperCase())) {
							this.setCharacterAttributes(wordStart, wordEnd - wordStart, reservedStyle, true);
						}
					}
					
					wordStart = i;
					lastAttr = nextAttr;
				}
				/*
				if (lastAttr != CT_WORD && nextAttr == CT_WORD) {
					wordStart = i;
				}
				if (lastAttr == CT_WORD && nextAttr != CT_WORD) {
					wordEnd = i;
					String word = charBuffer.substring(wordStart, wordEnd);
					if (reservedWords != null && reservedWords.contains(word.toUpperCase())) {
						this.setCharacterAttributes(wordStart, wordEnd - wordStart, reservedStyle, true);
					}
				}
				lastAttr = nextAttr;
				 */
			}
		} finally {
			writeUnlock();
		}
	}
}
