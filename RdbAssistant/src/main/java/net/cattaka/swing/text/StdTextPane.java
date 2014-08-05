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
 * $Id: StdTextPane.java 236 2009-08-01 11:16:24Z cattaka $
 */
package net.cattaka.swing.text;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import net.cattaka.swing.JPopupMenuForStandardText;
import net.cattaka.swing.text.FindCondition.ACTION;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.StringUtil;

public class StdTextPane extends JTextPane implements StdTextComponent {
	private static final long serialVersionUID = 1L;
	public static int UNDO_LIMIT = 10000;
	
	private JPopupMenuForStandardText popupMenu;
	private String wordDelimiter = "\t\r\n !\"#$%&'()*+,-./:;<=>?@^_`{|}~";

	public StdTextPane() {
		super();
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}

	public StdTextPane(JPopupMenuForStandardText popupMenu) {
		super();
		this.popupMenu = popupMenu;
		initialize();
	}
	
	public void initialize() {
		this.popupMenu.install(this);
		this.setEditorKit(new NoWrapEditorKit());
		
		this.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_Z:	//CTRL+Zのとき、UNDO実行
					if (e.isControlDown() && canUndo()) {
						undo();
						e.consume();
					}
					break;
				case KeyEvent.VK_Y:	//CTRL+Yのとき、REDO実行
					if (e.isControlDown() && canRedo()) {
						redo();
						e.consume();
					}
					break;
				case KeyEvent.VK_TAB:
					doTabKeyAction(e.isShiftDown());
					e.consume();
					break;
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});
	}
	
	public void setStdStyledDocument(StdStyledDocument doc) {
		super.setStyledDocument(doc);
	}
	
	public StdStyledDocument getStdStyledDocument() {
		StyledDocument sd = getStyledDocument();
		if (sd instanceof StdStyledDocument) {
			return (StdStyledDocument)sd;
		} else {
			return null;
		}
	}
	
	public void setDocumentFont(Font font, int charactersPerTab) {
		StdStyledDocument ssd = getStdStyledDocument();
		if (ssd != null) {
			FontMetrics fontMetrics = getFontMetrics(font);
			ssd.setFontMetrics(fontMetrics, charactersPerTab);
		}
	}
	
	public boolean canUndo() {
		StdStyledDocument ssd = getStdStyledDocument();
		if (ssd != null) {
			return ssd.canUndo();
		} else {
			return false;
		}
	}

	public boolean canRedo() {
		StdStyledDocument ssd = getStdStyledDocument();
		if (ssd != null) {
			return ssd.canRedo();
		} else {
			return false;
		}
	}
	
	public void undo() {
		StdStyledDocument ssd = getStdStyledDocument();
		if (ssd != null) {
			ssd.undo();
		}
	}
	
	public void redo() {
		StdStyledDocument ssd = getStdStyledDocument();
		if (ssd != null) {
			ssd.redo();
		}
	}

	public JPopupMenuForStandardText getPopupMenu() {
		return popupMenu;
	}
	
	private void doTabKeyAction(boolean shiftFlag) {
		StdStyledDocument ssd = getStdStyledDocument();
		int startPos = this.getSelectionStart();
		int endPos = this.getSelectionEnd();
		if (ssd == null || startPos > ssd.getLength()) {
			return;
		}
		
		String tabString = "\t";
		
		boolean recordUndoSepalartor = ssd.isRecordUndoSepalartor();
		ssd.addUndoSepalartor();
		ssd.setRecordUndoSepalartor(false);
		
		try {
			if (endPos == startPos && !shiftFlag) {
				// 範囲選択でなければ単純にタブを追加
				ssd.insertString(startPos, tabString, null);
			} else {
				// 範囲選択なら各行にタブを追加または除去
				if (endPos > startPos) {
					endPos = endPos-1;
				}
				
				TextLineInfo startTli = ssd.getLine(startPos);
				if (startTli != null) {
					if (shiftFlag) {
						TextLineInfo nextTli = startTli;
						while(nextTli != null && nextTli.getStartPos() <= endPos) {
							if (nextTli.getLine().length() >= tabString.length()
									&& nextTli.getLine().startsWith(tabString))
							{
								ssd.remove(nextTli.getStartPos(), tabString.length());
								endPos -= tabString.length();
								nextTli = ssd.getLine(nextTli.getEndPos() - tabString.length() + 1);
							} else {
								nextTli = ssd.getLine(nextTli.getEndPos() + 1);
							}
						}
					} else {
						TextLineInfo nextTli = startTli;
						while(nextTli != null && nextTli.getStartPos() <= endPos) {
							ssd.insertString(nextTli.getStartPos(), tabString, null);
							endPos += tabString.length();
							nextTli = ssd.getLine(nextTli.getEndPos() + tabString.length() + 1);
						}
					}
				}
			}
		} catch (BadLocationException e) {
			ExceptionHandler.error(e);
		} finally {
			ssd.setRecordUndoSepalartor(recordUndoSepalartor);
			ssd.addUndoSepalartor();
		}
	}
	
	public void doFindAction(FindCondition findCondition) {
//		int result = 0;
		StdStyledDocument ssd = getStdStyledDocument();
		if (ssd == null) {
			return;
		}

		String search = findCondition.getSearch();
		String replace = StringUtil.replaceEscapedChar(findCondition.getReplace());

		if (search.length() > 0) {
			// 文字列検索の場合
			if (findCondition.getAction() == ACTION.REPLACE) {
				// 置換時処理
				if (replaceString(search, replace, findCondition.isSenseCaseSearch(), findCondition.isRegexSearch(), findCondition.isWordUnitSearch()) != -1) {
//					result++;
				}
			}
			
			if (findCondition.getAction() == ACTION.REPLACE_FIND) {
				// 置換＆検索時処理
				if (replaceFindString(search, replace, -1, findCondition.isDownward(), findCondition.isLoopSearch(), findCondition.isSenseCaseSearch(), findCondition.isRegexSearch(), findCondition.isWordUnitSearch()) > 0) {
//					result++;
				}
			}
			
			if (findCondition.getAction() == ACTION.FIND) {
				// 検索時処理
				if (findString(search, -1, findCondition.isDownward(), findCondition.isLoopSearch(), findCondition.isSenseCaseSearch(), findCondition.isRegexSearch(), findCondition.isWordUnitSearch()) != -1) {
//					result++;
				}
			}
			
			if (findCondition.getAction() == ACTION.REPLACE_ALL) {
				// 全て置換時処理
				replaceAll(search, replace, -1, findCondition.isDownward(), findCondition.isLoopSearch(), findCondition.isSenseCaseSearch(), findCondition.isRegexSearch(), findCondition.isWordUnitSearch());
			}
		}
	}

	/**
	 * 検索処理用
	 * @param search		検索文字列
	 * @param startPos	開始位置(指定しない場合は-1)
	 * @param downward	検索方向(下の場合true)
	 * @param loopSearch	循環検索
	 * @param senseCaseSearch
	 * @return	見つかったインデックス、見つからない場合は-1
	 */
	public int findString(String search, int startPos, boolean downward, boolean loopSearch, boolean senseCaseSearch, boolean regexSearch, boolean wordUnitSearch) {
		StdStyledDocument ssd = getStdStyledDocument();
		if (ssd == null || search.length() == 0) {
			return -1;
		}
		
		// 大文字小文字の区別
		StringBuilder target = ssd.getStringBuilder();
		if (!senseCaseSearch) {
			StringBuilder tsb = new StringBuilder(target.toString().toUpperCase());
			target = tsb;
			search = search.toUpperCase();
		}
		
		// 検索処理
		int result = -1;
		if (regexSearch) {
			// 正規表現検索の場合
			int patternFlags;
			if (senseCaseSearch) {
				patternFlags = Pattern.MULTILINE;
			} else {
				patternFlags = Pattern.MULTILINE | Pattern.CASE_INSENSITIVE;
			}
			Pattern ptn = Pattern.compile(search, patternFlags);
			if (downward) {
				if (startPos == -1) {
					startPos = getSelectionEnd();
				}
				Matcher matcher = ptn.matcher(target);
				int s = -1;
				int e = -1;
				if (matcher.find(startPos)) {
					s = matcher.start();
					e = matcher.end();
				}
				if (s == -1 && loopSearch) {
					// ループ検索
					if (matcher.find(0)) {
						s = matcher.start();
						e = matcher.end();
					}
				}
				if (s != -1) {
					select(s, e);
					result = s;
				}
			} else {
				if (startPos == -1) {
					startPos = getSelectionStart();
				}
				Matcher matcher = ptn.matcher(target);
				int s = -1;
				int e = -1;
				if (matcher.find(0)) {
					do {
						if (matcher.end() < startPos) {
							s = matcher.start();
							e = matcher.end();
						} else {
							break;
						}
					} while (matcher.find());
				}
				if (s == -1 && loopSearch) {
					// ループ検索
					if (matcher.find(startPos)) {
						do {
							s = matcher.start();
							e = matcher.end();
						} while (matcher.find());
					}
				}
				if (s != -1) {
					select(s, e);
					result = s;
				}
			}
		} else if (wordUnitSearch) {
			// 単語単位検索の場合
			if (downward) {
				if (startPos == -1) {
					startPos = getSelectionEnd();
				}
				int s = -1;
				int r = startPos;
				while ((r = target.indexOf(search, r)) != -1) {
					int e = r + search.length();
					if (r > 0 && wordDelimiter.indexOf(target.charAt(r-1)) == -1) {
						r = r + 1;
						continue;
					}
					if (e < target.length() && wordDelimiter.indexOf(target.charAt(e)) == -1) {
						r = r + 1;
						continue;
					}
					s = r;
					break;
				}
				if (s == -1 && loopSearch) {
					// ループ検索
					r = 0;
					while ((r = target.indexOf(search, r)) != -1) {
						int e = r + search.length();
						if (r > 0 && wordDelimiter.indexOf(target.charAt(r-1)) == -1) {
							r = r + 1;
							continue;
						}
						if (e < target.length() && wordDelimiter.indexOf(target.charAt(e)) == -1) {
							r = r + 1;
							continue;
						}
						s = r;
						break;
					}
				}
				if (s != -1) {
					select(s, s+search.length());
					result = s;
				}
			} else {
				if (startPos == -1) {
					startPos = getSelectionStart();
				}
				int s = -1;
				int r = startPos-1;
				if (r>=0) {
					while ((r = target.lastIndexOf(search, r)) != -1) {
						int e = r + search.length();
						if (r > 0 && wordDelimiter.lastIndexOf(target.charAt(r-1)) == -1) {
							r = r - 1;
							continue;
						}
						if (e < target.length() && wordDelimiter.lastIndexOf(target.charAt(e)) == -1) {
							r = r - 1;
							continue;
						}
						r = s;
						break;
					}
				} else {
					s = -1;
				}
				if (s == -1 && loopSearch) {
					// ループ検索
					r = target.length();
					while ((r = target.lastIndexOf(search, r)) != -1) {
						int e = r + search.length();
						if (r > 0 && wordDelimiter.lastIndexOf(target.charAt(r-1)) == -1) {
							r = r - 1;
							continue;
						}
						if (e < target.length() && wordDelimiter.lastIndexOf(target.charAt(e)) == -1) {
							r = r - 1;
							continue;
						}
						s = r;
						break;
					}
				}
				if (s != -1) {
					select(s, s+search.length());
					result = s;
				}
			}
		} else {
			// 文字列検索の場合
			if (downward) {
				if (startPos == -1) {
					startPos = getSelectionEnd();
				}
				int s = startPos;
				s = target.indexOf(search, s);
				if (s == -1 && loopSearch) {
					// ループ検索
					s = target.indexOf(search, 0);
				}
				if (s != -1) {
					select(s, s+search.length());
					result = s;
				}
			} else {
				if (startPos == -1) {
					startPos = getSelectionStart();
				}
				int s = startPos-1;
				if (s>=0) {
					s = target.lastIndexOf(search, s);
				} else {
					s = -1;
				}
				if (s == -1 && loopSearch) {
					// ループ検索
					s = target.lastIndexOf(search, target.length());
				}
				if (s != -1) {
					select(s, s+search.length());
					result = s;
				}
			}
		}
		return result;
	}
	/**
	 * 検索処理用。選択中の文字列を置換する。
	 * @param target		被検索対象
	 * @param search		検索文字列
	 * @param replace		置換文字列
	 * @return	成功時は0以上、失敗時は-1
	 */
	public int replaceString(String search, String replace, boolean senseCaseSearch, boolean regexSearch, boolean wordUnitSearch) {
		StdStyledDocument ssd = getStdStyledDocument();
		if (ssd == null || search.length() == 0) {
			return -1;
		}
		int result = getSelectionStart();
		StyledDocument sd = getStyledDocument();
		try {
			int ss = getSelectionStart();
			int se = getSelectionEnd();
			String selectedWord = sd.getText(ss, se-ss);
			boolean goFlag = false;
			if (regexSearch) {
				int patternFlags;
				if (senseCaseSearch) {
					patternFlags = Pattern.MULTILINE;
				} else {
					patternFlags = Pattern.MULTILINE | Pattern.CASE_INSENSITIVE;
				}
				Pattern pattern = Pattern.compile(search, patternFlags);
				goFlag = pattern.matcher(selectedWord).matches();
			} else {
				if (senseCaseSearch) {
					goFlag = selectedWord.equals(search);
				} else {
					goFlag = selectedWord.equalsIgnoreCase(search);
				}
			}
			if (goFlag) {
				sd.remove(ss, se - ss);
				sd.insertString(ss, replace, null);
				select(ss, ss+replace.length());
			}
		} catch (BadLocationException e) {
			ExceptionHandler.fatal(e);
			result = -1;
		}
		return result;
	}
	
	/**
	 * 検索処理用。置換後、検索する。
	 * @param search
	 * @param replace
	 * @param startPos
	 * @param downward
	 * @param loopSearch
	 * @param senseCaseSearch
	 * @return 置換した個数
	 */
	public int replaceFindString(String search, String replace, int startPos, boolean downward, boolean loopSearch, boolean senseCaseSearch, boolean regexSearch, boolean wordUnitSearch) {
		StdStyledDocument ssd = getStdStyledDocument();
		if (ssd == null || search.length() == 0) {
			return -1;
		}
		int result = 0;
		if (replaceString(search, replace, senseCaseSearch, regexSearch, wordUnitSearch) != -1) {
			result++;
		}

		findString(search, startPos, downward, loopSearch, senseCaseSearch, regexSearch, wordUnitSearch);
		
		return result;
	}
	/**
	 * 検索処理用。全て置換する。
	 * @param search
	 * @param replace
	 * @param startPos
	 * @param downward
	 * @param loopSearch
	 * @param senseCaseSearch
	 * @return
	 */
	public int replaceAll(String search, String replace, int startPos, boolean downward, boolean loopSearch, boolean senseCaseSearch, boolean regexSearch, boolean wordUnitSearch) {
		StdStyledDocument ssd = getStdStyledDocument();
		if (ssd == null || search.length() == 0) {
			return -1;
		}
		boolean lastRecordUndoSepalartor = ssd.isRecordUndoSepalartor();
		ssd.setRecordUndoSepalartor(false);
		int result = 0;
		try {
			if (downward) {
				if (replaceString(search, replace, senseCaseSearch, regexSearch, wordUnitSearch) != -1) {
					result++;
				}
				if (startPos == -1) {
					startPos = getSelectionEnd();
				}
				int sp = startPos;
				while((sp = findString(search, sp, true, false, senseCaseSearch, regexSearch, wordUnitSearch)) != -1) {
					replaceString(search, replace, senseCaseSearch, regexSearch, wordUnitSearch);
					sp = getSelectionEnd();
					result++;
				}
				if (loopSearch) {
					sp = 0;
					while((sp = findString(search, sp, true, false, senseCaseSearch, regexSearch, wordUnitSearch)) != -1 && sp < startPos) {
						replaceString(search, replace, senseCaseSearch, regexSearch, wordUnitSearch);
						sp = getSelectionEnd();
						result++;
					}
				}
			} else {
				if (replaceString(search, replace, senseCaseSearch, regexSearch, wordUnitSearch) != -1) {
					result++;
				}
				if (startPos == -1) {
					startPos = getSelectionStart();
				}
				int sp = startPos;
				while((sp = findString(search, sp, false, false, senseCaseSearch, regexSearch, wordUnitSearch)) != -1) {
					replaceString(search, replace, senseCaseSearch, regexSearch, wordUnitSearch);
					sp = getSelectionStart();
					result++;
				}
				if (loopSearch) {
					sp = ssd.getLength();
					while((sp = findString(search, sp, false, false, senseCaseSearch, regexSearch, wordUnitSearch)) != -1 && sp > startPos) {
						replaceString(search, replace, senseCaseSearch, regexSearch, wordUnitSearch);
						sp = getSelectionStart();
						result++;
					}
				}
			}
		} finally {
			ssd.setRecordUndoSepalartor(lastRecordUndoSepalartor);
		}
		return result;
	}
	
	public JComponent getJComponent() {
		return this;
	}
}
