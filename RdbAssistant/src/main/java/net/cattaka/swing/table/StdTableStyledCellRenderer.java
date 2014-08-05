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
 * $Id: StdTableStyledCellRenderer.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class StdTableStyledCellRenderer extends StdTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1); 
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

	public StdTableStyledCellRenderer() {
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof StdStyledCell) {
			StdStyledCell cell = (StdStyledCell)value;
			if (isSelected) {
				super.setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				super.setForeground((cell != null && cell.getForeground() != null) ? cell.getForeground() : table.getForeground());
				super.setBackground((cell != null && cell.getBackground() != null) ? cell.getBackground() : table.getBackground());
			}
	
			setFont(table.getFont());
	
			if (hasFocus) {
				Border border = null;
				if (isSelected) {
					border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
				}
				if (border == null) {
					border = UIManager.getBorder("Table.focusCellHighlightBorder");
				}
				setBorder(border);
	
				if (!isSelected && table.isCellEditable(row, column)) {
					Color col;
					col = UIManager.getColor("Table.focusCellForeground");
					if (col != null) {
						super.setForeground(col);
					}
					col = UIManager.getColor("Table.focusCellBackground");
					if (col != null) {
						super.setBackground(col);
					}
				}
			} else {
				Border border = (cell != null) ? cell.getBorder() : null;
				if (border == null) {
					border = getNoFocusBorder();
				}
				setBorder(border);
			}
	
			setDisplayValue(cell.getValue());
		} else {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
		return this;
	}
	
	private static Border getNoFocusBorder() {
	    if (System.getSecurityManager() != null) {
	        return SAFE_NO_FOCUS_BORDER;
	    } else {
	        return noFocusBorder;
	    }
	}
}
