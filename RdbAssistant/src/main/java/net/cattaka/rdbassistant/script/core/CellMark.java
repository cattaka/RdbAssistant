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
 * $Id: CellMark.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.script.core;

import java.awt.Color;

import net.cattaka.swing.table.StdStyledCell;

/**
 * テーブルのセルをマーキングする際の設定に使用します。
 * 
 * @author cattaka
 */
public class CellMark {
	private Color foreground;
	private Color background;
	private Color borderColor;
	private int borderThickness = -1;
	private String value;
	
	/**
	 * コンストラクタです。
	 */
	public CellMark() {
	}
	
	/**
	 * コピーコンストラクタです。
	 * 
	 * @param src コピー元
	 */
	public CellMark(CellMark src) {
		this.foreground = src.getForeground();
		this.background = src.getBackground();
		this.borderColor = src.getBorderColor();
		this.borderThickness = src.getBorderThickness();
		this.value = src.getValue();
	}
	
	/**
	 * 対象のセルにマーキングを行う。
	 * @param targetCell
	 */
	void mark(StdStyledCell targetCell) {
		if (targetCell == null) {
			return;
		}
		if (this.getForeground() != null) {
			targetCell.setForeground(this.getForeground());
		}
		if (this.getBackground() != null) {
			targetCell.setBackground(this.getBackground());
		}
		if (this.getBorderColor() != null) {
			targetCell.setBorderColor(this.getBorderColor());
		}
		if (this.getBorderThickness() >= 0) {
			targetCell.setBorderThickness(this.getBorderThickness());
		}
		if (this.getValue() != null) {
			targetCell.setValue(this.getValue());
		}
	}
	/**
	 * 対象のセルにマーキングを行う。
	 * @param targetCell
	 */
	void mark(StdStyledCell[] targetCells) {
		for (StdStyledCell targetCell:targetCells) {
			mark(targetCell);
		}
	}
	/**
	 * 前景色を取得します。
	 * @return 前景色
	 */
	public Color getForeground() {
		return foreground;
	}
	/**
	 * 前景色を設定します。
	 * @param foreground 前景色
	 */
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	/**
	 * 背景色を取得します。
	 * @return 背景色
	 */
	public Color getBackground() {
		return background;
	}
	/**
	 * 背景色を設定します。
	 * @param background 背景色
	 */
	public void setBackground(Color background) {
		this.background = background;
	}
	/**
	 * ボーダーの色を取得します。
	 * @return ボーダーの色
	 */
	public Color getBorderColor() {
		return borderColor;
	}
	/**
	 * ボーダーの色を設定します。
	 * @param borderColor ボーダーの色
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	/**
	 * ボーダーの太さを取得します。
	 * @return ボーダーの太さ
	 */
	public int getBorderThickness() {
		return borderThickness;
	}
	/**
	 * ボーダーの太さを設定します。
	 * @param borderThickness ボーダーの太さ
	 */
	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
	}
	/**
	 * 文字列を取得します。
	 * @return 文字列
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 文字列を設定します。
	 * @param value 文字列
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
