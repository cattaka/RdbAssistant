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
 * $Id: StdStyledCell.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.table;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class StdStyledCell {
	private Color foreground;
	private Color background;
	private Color borderColor;
	private int borderThickness = -1;
	private String value;
	private Border border;
	
	public StdStyledCell() {
		this.borderColor = Color.BLACK;
	}
	
	public StdStyledCell(StdStyledCell src) {
		this.foreground = src.getForeground();
		this.background = src.getBackground();
		this.borderColor = src.getBorderColor();
		this.borderThickness = src.getBorderThickness();
		this.value = src.getValue();
		this.border = src.getBorder();
	}
	
	public Border getBorder() {
		return border;
	}

	public Color getForeground() {
		return foreground;
	}
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	public Color getBackground() {
		return background;
	}
	public void setBackground(Color background) {
		this.background = background;
	}
	public Color getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		if (borderThickness >= 0 && borderColor != null) {
			this.border = BorderFactory.createLineBorder(this.borderColor, this.borderThickness);
		} else {
			this.border = null;
		}
	}
	public int getBorderThickness() {
		return borderThickness;
	}
	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
		if (borderThickness >= 0 && borderColor != null) {
			this.border = BorderFactory.createLineBorder(this.borderColor, this.borderThickness);
		} else {
			this.border = null;
		}
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		// TODO : 暫定仕様
		return (this.value != null) ? this.value : "";
	}
}
