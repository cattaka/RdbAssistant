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
 * $Id: AbstractStdStyledDocument.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.text;

import java.util.LinkedList;
import java.util.Set;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.StyleContext;

import net.cattaka.swing.text.StdStyledDocumentEditEvent.EVENT_TYPE;
import net.cattaka.util.ExceptionHandler;

abstract public class AbstractStdStyledDocument extends DefaultStyledDocument implements StdStyledDocument {
	private static final long serialVersionUID = 1L;
	protected StringBuilder charBuffer = new StringBuilder(8);
	protected StringBuilder attributeBuffer = new StringBuilder(8);
	protected Set<String> reservedWords;
	private int lineCount;
	
	// Undo用
	private LinkedList<StdStyledDocumentEditEvent> undoList = new LinkedList<StdStyledDocumentEditEvent>();
	private int currentUndoPos = 0;
	private boolean recordUndoFlag = false;
	private boolean recordSepalartorFlag = true;
	private StdStyledDocumentEditEvent lastStdStyledDocumentEditEvent = null;
	
	class DocumentFilterForSql extends DocumentFilter {
		@Override
		public void insertString(FilterBypass fb, int offset, String string,
				AttributeSet attr) throws BadLocationException {
			addUndoList(new StdStyledDocumentEditEvent(offset,"",string));
			fb.insertString(offset, string, null);
			int l = fb.getDocument().getLength();
			adjustCharAttributeSize(l);
			replaceBuffer(offset, 0, l, string);
			updateAttributes(offset, l);
		}

		@Override
		public void remove(FilterBypass fb, int offset, int length)
				throws BadLocationException {
			addUndoList(new StdStyledDocumentEditEvent(offset,charBuffer.substring(offset,offset+length),""));
			fb.remove(offset, length);
			int l = fb.getDocument().getLength();
			adjustCharAttributeSize(l);
			replaceBuffer(offset, length, l, "");
			updateAttributes(offset, l);
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length,
				String text, AttributeSet attrs) throws BadLocationException {
			addUndoList(new StdStyledDocumentEditEvent(offset,charBuffer.substring(offset,offset+length),text));
			fb.replace(offset, length, text, null);
			int l = fb.getDocument().getLength();
			adjustCharAttributeSize(l);
			replaceBuffer(offset, length, l, text);
			updateAttributes(offset, l);
		}
		
	}
	
	public AbstractStdStyledDocument() {
		super();
		initialize();
	}

	public AbstractStdStyledDocument(Content c, StyleContext styles) {
		super(c, styles);
		initialize();
	}

	public AbstractStdStyledDocument(StyleContext styles) {
		super(styles);
		initialize();
	}

	private void initialize() {
		this.setDocumentFilter(new DocumentFilterForSql());
		this.lineCount = 1;
	}
	
	public StringBuilder getStringBuilder() {
		return charBuffer;
	}

	public Set<String> getReservedWords() {
		return reservedWords;
	}

	public void setReservedWords(Set<String> reservedWords) {
		this.reservedWords = reservedWords;
	}

	public int getLineCount() {
		return lineCount;
	}

	private void adjustCharAttributeSize(int l) {
		if (charBuffer.capacity() > 512 && charBuffer.length() * 4 < charBuffer.capacity()) {
			charBuffer.trimToSize();
			attributeBuffer.trimToSize();
		}
	}
	
	private void replaceBuffer(int offset, int length, int tail, String text) {
		charBuffer.replace(offset, offset+length, text);
		attributeBuffer.replace(offset, offset+length, text);
		for (int i=offset;i<offset+text.length();i++) {
			attributeBuffer.setCharAt(i, (char)0);
		}
	}
	
	/**
	 * @param start 開始地点
	 * @param tail ドキュメントの終端
	 */
	abstract protected void updateAttributes(int start, int tail);
	
	/**
	 * DocumentFilterによってUndoのバッファに格納される。
	 * @param ssdee
	 */
	protected void addUndoList(StdStyledDocumentEditEvent ssdee) {
		// 行数を計算する。
		{
			String before = ssdee.getBefore();
			String after = ssdee.getAfter();
			for (int i=0;i<before.length();i++) {
				if (before.charAt(i) == '\n') {
					this.lineCount--;
				}
			}
			for (int i=0;i<after.length();i++) {
				if (after.charAt(i) == '\n') {
					this.lineCount++;
				}
			}
		}
		
		// 記録可の時だけ記録する
		if (!recordUndoFlag) {
			while(this.undoList.size() > currentUndoPos) {
				this.undoList.removeLast();
			}
			
			// セパレータの追加
			if (recordSepalartorFlag && lastStdStyledDocumentEditEvent != null) {
				StdStyledDocumentEditEvent lssdee = lastStdStyledDocumentEditEvent;
				boolean insertSepalator = false;
				if (lssdee.getAfter().length() == 1 && ssdee.getAfter().length() == 1) {
					int t1 = 0;
					int t2 = 0;
					switch(lssdee.getAfter().charAt(0)) {
					case ' ':
						t1 = 1;
						break;
					case '\n':
						t1 = 2;
						break;
					}
					switch(ssdee.getAfter().charAt(0)) {
					case ' ':
						t2 = 1;
						break;
					case '\n':
						t2 = 2;
						break;
					}
					insertSepalator = (t1 != t2);
				}
				if (lssdee.getAfter().length() > 1 || ssdee.getAfter().length() > 1) {
					insertSepalator = true;
				}
				if (Math.abs(lssdee.getOffset() - ssdee.getOffset()) > 1) {
					insertSepalator = true;
				}
				
				if (insertSepalator) {
					this.undoList.add(currentUndoPos++, new StdStyledDocumentEditEvent());
				}
			}
			// 追加
			this.undoList.add(currentUndoPos++, ssdee);
			this.lastStdStyledDocumentEditEvent = ssdee;
		}
	}
	
	public boolean canUndo() {
		return currentUndoPos > 0;
	}
	public boolean canRedo() {
		return this.undoList.size() > currentUndoPos;
	}
	public boolean undo() {
		if (currentUndoPos > 0) {
			StdStyledDocumentEditEvent.EVENT_TYPE lastEventType = null;
			while (currentUndoPos > 0) {
				StdStyledDocumentEditEvent ssdee = this.undoList.get(currentUndoPos-1);
				if (lastEventType != null && ssdee.getEventType() == EVENT_TYPE.SEPALATOR) {
					break;
				}
				currentUndoPos--;
				if (ssdee.getEventType() == EVENT_TYPE.SEPALATOR) {
					continue;
				}
				lastEventType = ssdee.getEventType();
				try {
					recordUndoFlag = true;
					replace(ssdee.getOffset(), ssdee.getAfter().length(), ssdee.getBefore(), null);
				} catch (BadLocationException e) {
					ExceptionHandler.error(e);
				} finally {
					recordUndoFlag = false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean redo() {
		if (currentUndoPos < this.undoList.size()) {
			StdStyledDocumentEditEvent.EVENT_TYPE lastEventType = null;
			while (currentUndoPos < this.undoList.size()) {
				StdStyledDocumentEditEvent ssdee = this.undoList.get(currentUndoPos);
				if (lastEventType != null && ssdee.getEventType() == EVENT_TYPE.SEPALATOR) {
					break;
				}
				currentUndoPos++;
				if (ssdee.getEventType() == EVENT_TYPE.SEPALATOR) {
					continue;
				}
				lastEventType = ssdee.getEventType();
				try {
					recordUndoFlag = true;
					replace(ssdee.getOffset(), ssdee.getBefore().length(), ssdee.getAfter(), null);
				} catch (BadLocationException e) {
					ExceptionHandler.error(e);
				} finally {
					recordUndoFlag = false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public void resetUndo() {
		this.undoList.clear();
		this.currentUndoPos = 0;
	}

	public void addUndoSepalartor() {
		this.undoList.add(currentUndoPos++, new StdStyledDocumentEditEvent());
	}

	public boolean isRecordUndoSepalartor() {
		return recordSepalartorFlag;
	}

	public void setRecordUndoSepalartor(boolean recordUndoSepalartor) {
		this.recordSepalartorFlag = recordUndoSepalartor;
	}
	
	/**
	 * 指定した位置の行を取得する。
	 * 行番号でなはないので注意
	 * @param pos
	 * @return 指定した位置が妥当ならその行の情報、それ以外はnull
	 */
	public TextLineInfo getLine(int pos) {
		TextLineInfo result = null;
		int startPos = pos;
		int endPos = pos;
		
		if (0<=pos && pos < charBuffer.length()) {
			do {
				startPos--;
			} while(startPos>=0 && charBuffer.charAt(startPos) != '\n' );
			startPos += 1;

			while(endPos<charBuffer.length() && charBuffer.charAt(endPos) != '\n' ) {
				endPos++;
			}
			
//			System.out.println(""+startPos+":"+endPos);
			
			String line = charBuffer.substring(startPos, endPos);
			result = new TextLineInfo(startPos, endPos, line);
		} else if (pos == charBuffer.length()) {
			result = new TextLineInfo(pos, pos, "");
		}
		
		return result;
	}
	public TextLineInfo getPrevLine(TextLineInfo src) {
		return getLine(src.getStartPos()-1);
	}
	public TextLineInfo getNextLine(TextLineInfo src) {
		return getLine(src.getEndPos()+1);
	}
}
