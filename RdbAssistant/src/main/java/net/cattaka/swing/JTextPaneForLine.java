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
 * $Id: JTextPaneForLine.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing;

import java.awt.Color;
import java.awt.FontMetrics;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import net.cattaka.rdbassistant.RdbaConfigConstants;
import net.cattaka.util.ExceptionHandler;

public class JTextPaneForLine extends JTextPane {
	private static final long serialVersionUID = 1L;
	private int lineCount = 0;

	public JTextPaneForLine() {
		this.setEditable(false);
	}

	public void setFontMetrics(FontMetrics fontMetrics) {
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		if (fontMetrics != null) {
			StyleConstants.setFontFamily(def, fontMetrics.getFont().getFamily());
			StyleConstants.setFontSize(def, fontMetrics.getFont().getSize());
		} else {
			StyleConstants.setFontFamily(def, RdbaConfigConstants.DEFAULT_FONT_EDITOR);
		}
		
		Style lineNumFont = this.addStyle("lineNum", def);

		StyleConstants.setForeground(lineNumFont,Color.BLUE);
		StyledDocument sd = getStyledDocument();
		sd.setLogicalStyle(0, lineNumFont);
		sd.setCharacterAttributes(0, sd.getLength(), lineNumFont, true);
	}
	
	public int getLineCount() {
		return lineCount;
	}
	
	public void setLineCount(int lineCount) {
		int oldLineCount = this.lineCount;
		this.lineCount = lineCount;
		if (this.lineCount < 0) {
			this.lineCount = 0;
		}
		try {
			if (oldLineCount < this.lineCount) {
				StyledDocument sd = this.getStyledDocument();
				for (int i=oldLineCount+1;i<=this.lineCount;i++) {
					String lineLabel = String.valueOf(i) + '\n';
					sd.insertString(sd.getLength(), lineLabel, null);
				}
			}
			if (oldLineCount > this.lineCount) {
				StyledDocument sd = this.getStyledDocument();
				for (int i=oldLineCount;i>this.lineCount;i--) {
					String lineLabel = String.valueOf(i) + '\n';
					sd.remove(sd.getLength()-lineLabel.length(), lineLabel.length());
				}
			}
		} catch (BadLocationException e) {
			ExceptionHandler.error(e);
		}
	}
}
