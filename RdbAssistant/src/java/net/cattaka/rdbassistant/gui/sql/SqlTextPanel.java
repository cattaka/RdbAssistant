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
 * $Id: SqlTextPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.sql;

import javax.swing.text.BadLocationException;

import net.cattaka.swing.JPopupMenuForStandardText;
import net.cattaka.swing.text.StdStyledDocument;
import net.cattaka.swing.text.StdTextPane;
import net.cattaka.swing.text.TextLineInfo;
import net.cattaka.util.ExceptionHandler;

public class SqlTextPanel extends StdTextPane {
	private static final long serialVersionUID = 1L;

	public SqlTextPanel() {
		super();
	}

	public SqlTextPanel(JPopupMenuForStandardText popupMenu) {
		super(popupMenu);
	}
	
	public void doCommentOut(String comment) {
		StdStyledDocument ssd = getStdStyledDocument();
		int startPos = this.getSelectionStart();
		int endPos = this.getSelectionEnd();
		if (ssd == null || startPos >= ssd.getLength()) {
			return;
		}
		if (endPos > startPos) {
			endPos = endPos-1;
		}
		
		boolean recordUndoSepalartor = ssd.isRecordUndoSepalartor();
		ssd.addUndoSepalartor();
		ssd.setRecordUndoSepalartor(false);
		
		try {
			TextLineInfo startTli = ssd.getLine(startPos);
			if (startTli != null) {
				boolean removeFlag = true;
				{
					TextLineInfo nextTli = startTli;
					while(nextTli != null && nextTli.getStartPos() <= endPos) {
						if (nextTli.getLine().length() >= comment.length()
								&& nextTli.getLine().startsWith(comment)) {
							// コメントアウト済み
						} else {
							// コメントアウト未だ
							removeFlag = false;
							break;
						}			
						nextTli = ssd.getNextLine(nextTli);
					}
				}
				
				if (removeFlag) {
					TextLineInfo nextTli = startTli;
					while(nextTli != null && nextTli.getStartPos() <= endPos) {
						ssd.remove(nextTli.getStartPos(), comment.length());
						endPos -= comment.length();
						nextTli = ssd.getLine(nextTli.getEndPos() - comment.length() + 1);
					}
				} else {
					TextLineInfo nextTli = startTli;
					while(nextTli != null && nextTli.getStartPos() <= endPos) {
						ssd.insertString(nextTli.getStartPos(), comment, null);
						endPos += comment.length();
						nextTli = ssd.getLine(nextTli.getEndPos() + comment.length() + 1);
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
}
